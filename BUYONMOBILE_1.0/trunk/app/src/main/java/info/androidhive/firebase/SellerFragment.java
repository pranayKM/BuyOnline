package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.firebase.adapters.SellCarAdapter;
import info.androidhive.firebase.restservices.dto.MatchedUserIdsResponse;
import info.androidhive.firebase.restservices.dto.UserPreferencesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Subbareddy on 7/2/2017.
 */

public class SellerFragment  extends Fragment {

    private Toolbar toolbar;

    private FragmentTransaction fragmentTransaction = null;

    private RecyclerView sellerRv;

    private LinearLayoutManager mLayoutManager;

    private TextView emptyTv;

    private ProgressDialogBar dialogProgress;

    private Button sellACarBtn;

    private ArrayList<MatchedUserIdsResponse> matchedRecordsResponseList= new ArrayList<>();

    private List<Integer> deviceIdsList;

    private Integer[] deviceIdsStrList;

    private int matchedUserId;

    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/From/UserDetailsId";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller, container, false);
        setHasOptionsMenu(true);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Sell a Car");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        sellerRv = (RecyclerView) view.findViewById(R.id.sellerRv);
        emptyTv = (TextView) view.findViewById(R.id.emptyTv);
        sellACarBtn = (Button) view.findViewById(R.id.sellCarBtn);

        mLayoutManager= new LinearLayoutManager(getActivity());
        sellerRv.setLayoutManager(mLayoutManager);
        sellerRv.setHasFixedSize(true);


        if (InternetConnection.isNetworkAvailable(getActivity())) {
            getUserPreferences();
        } else {
            Toast.makeText(getActivity(),"No Internet", Toast.LENGTH_SHORT).show();
        }

        /*Filter Button on click listener*/
        sellACarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),SellCarActivity.class);
                startActivityForResult(intent,33);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            }
        });



        return view;
    }

    /* get user preferences*/
    public void getUserPreferences() {

        dialogProgress = new ProgressDialogBar(getActivity());
        dialogProgress.showProgressDialog();
        String email = Preferences.emailID;
        Log.e("### Email Id",email);
       /* UserPreferencesResponse preferencesResponse = new UserPreferencesResponse();
        preferencesResponse.setCarColor("Red");
        preferencesResponse.setCarModel("Red");
        preferencesResponse.setCarMake("Red");*/

        final Call<List<UserPreferencesResponse>> preferencesList = BOMApplication.getRestClient().getApiService().getUserPreferences();

        preferencesList.enqueue(new Callback<List<UserPreferencesResponse>>() {
            @Override
            public void onResponse(Call<List<UserPreferencesResponse>> responseCall, Response<List<UserPreferencesResponse>> response) {

                dialogProgress.dismissProgressDialog();

                if (response.code() == 200) {

                    List<UserPreferencesResponse> responseList = response.body();

                    if (responseList.size() > 0 && responseList != null) {
                        Log.e("###Seller Fragment", String.valueOf(responseList.toArray()));
                        SellCarAdapter sellCarAdapter= new SellCarAdapter(getActivity(),responseList);
                        sellerRv.setAdapter(sellCarAdapter);

                        sellerRv.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);

                    }else {

                        sellerRv.setVisibility(View.GONE);
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserPreferencesResponse>> call, Throwable t) {

                dialogProgress.dismissProgressDialog();
                Toast.makeText(getActivity(),  "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getUserId(){
        Log.e("$$$ Entered", String.valueOf(0));
        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>(){

            @Override
            public void onResponse(String s) {

                Log.e("Response",s);
                Log.e("$$$$Response",s);
                if(!s.equals("No Records found")) {
                    matchedRecordsResponseList = new Gson().fromJson(s, new TypeToken<List<MatchedUserIdsResponse>>() {
                    }.getType());
                    Log.e("@@@Size", String.valueOf(matchedRecordsResponseList.size()));
                    deviceIdsList = new ArrayList<Integer>();
                    for(int i=0; i < matchedRecordsResponseList.size();i++){
                        //deviceIdsList.add(tokenResponse.get(i).getFcmTokenID());
                        deviceIdsList.add(matchedRecordsResponseList.get(i).getUserId());
                        //Log.e("-->SellerIDSList",deviceIdsList.toString());
                    }

                    deviceIdsStrList = new Integer[deviceIdsList.size()];
                    deviceIdsStrList = deviceIdsList.toArray(deviceIdsStrList);
                    Log.e("==>Device Tokens", String.valueOf(deviceIdsStrList));
                    matchedUserId = matchedRecordsResponseList.get(0).getUserId();
                    Log.e("-->Size", String.valueOf(matchedRecordsResponseList.get(0).getUserId()));
                    Preferences.loadPreferences(getContext());
                    Preferences.userID = matchedRecordsResponseList.get(0).getUserId();
                    Preferences.savePreferences(getActivity().getApplicationContext());
                    Log.e("@@ Hellow", String.valueOf(matchedRecordsResponseList));



                }


            }

        },new com.android.volley.Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(SellCarAdapter.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("emailId",Preferences.emailID);


                return parameters;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        rQueue.add(request);
        return matchedUserId;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 33 && resultCode == 33) {

            getUserPreferences();
            matchedUserId = getUserId();
            Log.e("$$$ RequestCode", String.valueOf(matchedUserId));

        }
    }
}
