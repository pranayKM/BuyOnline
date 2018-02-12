package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import info.androidhive.firebase.adapters.BuyCarAdapter;
import info.androidhive.firebase.restservices.dto.BuyerUserResponse;
import info.androidhive.firebase.restservices.dto.UserPreferencesResponse;

/**
 * Created by Subbareddy on 5/14/2017.
 */

public class BuyerFragment extends Fragment {

    private Toolbar toolbar;

    private FragmentTransaction fragmentTransaction = null;

    private RecyclerView userPrefRv;

    private LinearLayoutManager mLayoutManager;

    private TextView emptyTv;

    private ProgressDialogBar dialogProgress;

    private FloatingActionButton filterBtn;

    private Button enterPrefereBtn;

    private String carBrandStr = "", carModelStr = "", carModelYrStr = "", carPriceStr = "";

    private String savedPrefResponse = "";

    private String cityStr="",countryStr="",totalAddressStr="";

    private double latitude=0.0,longitude=0.0;

    private String email;

    private List<UserPreferencesResponse> responseList= new ArrayList<>();

    private List<BuyerUserResponse> savedResponseList= new ArrayList<>();

    private BuyCarAdapter preferencesAdapter;

    private List<BuyerUserResponse> matchedRecordsResponseList= new ArrayList<>();

    private List<BuyerUserResponse> responseList1= new ArrayList<>();

    private String sample = null;

    public String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Login/login";

    public String URL1 = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Filtered/UserCars";

    //private static final String BASE_URL = "http://10.0.2.2:8080/BOMApplication/REST/Login/login";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //filterID = getArguments().getInt("filterID");

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Buy a Car");

        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        userPrefRv = (RecyclerView) view.findViewById(R.id.userRv);
        emptyTv = (TextView) view.findViewById(R.id.emptyTv);
        filterBtn = (FloatingActionButton) view.findViewById(R.id.filterBtn);
        enterPrefereBtn = (Button) view.findViewById(R.id.enterPrefereBtn);

        mLayoutManager= new LinearLayoutManager(getActivity());
        userPrefRv.setLayoutManager(mLayoutManager);
        userPrefRv.setHasFixedSize(true);


        if (InternetConnection.isNetworkAvailable(getActivity())) {

            //getUserPreferences();
            getMachedSellersDetails();

        } else {
            Toast.makeText(getActivity(),"No Internet", Toast.LENGTH_SHORT).show();
        }

        /*Filter Button on click listener*/
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),FilterActivity.class);
                startActivity(intent);
                //startActivityForResult(intent,33);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            }
        });

        enterPrefereBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BuyerPreferencesActivity.class);
                startActivityForResult(intent,44);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            }
        });


        return view;
    }

    /* get user preferences*/
    /*public void getUserPreferences() {

        dialogProgress = new ProgressDialogBar(getActivity());
        dialogProgress.showProgressDialog();

       *//* UserPreferencesResponse preferencesResponse = new UserPreferencesResponse();
        preferencesResponse.setCarColor("Red");
        preferencesResponse.setCarModel("Red");
        preferencesResponse.setCarMake("Red");*//*

        final Call<List<UserPreferencesResponse>> preferencesList = BOMApplication.getRestClient().getApiService().getUserPreferences();
        //final Call<List<UserDetails>> usersList = BOMApplication.getRestClient().getApiService().getUserDetails();

        preferencesList.enqueue(new Callback<List<UserPreferencesResponse>>() {
            @Override
            public void onResponse(Call<List<UserPreferencesResponse>> responseCall, Response<List<UserPreferencesResponse>> response) {

                dialogProgress.dismissProgressDialog();

                if (response.code() == 200) {

                    responseList = response.body();

                    if (responseList.size() > 0 && responseList != null) {

                        preferencesAdapter = new BuyCarAdapter(getActivity(), responseList);
                        userPrefRv.setAdapter(preferencesAdapter);

                        userPrefRv.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);

                    } else {

                        userPrefRv.setVisibility(View.GONE);
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserPreferencesResponse>> call, Throwable t) {

                dialogProgress.dismissProgressDialog();
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    public void getMachedSellersDetails(){
        String emailId = Preferences.emailID;
        Log.e("###Notification Email",emailId);
        StringRequest request = new StringRequest(Request.Method.POST, URL1, new com.android.volley.Response.Listener<String>(){

            @Override
            public void onResponse(String s) {

                Log.e("Response",s);
                Log.e("$$$$Response",s);
                if(!s.equals("No Records found")) {
                    responseList1 = new Gson().fromJson(s, new TypeToken<List<BuyerUserResponse>>() {
                    }.getType());
                    Log.e("## Record Details", String.valueOf(responseList1));
                    if (responseList1.size() > 0 && responseList1 != null) {

                        preferencesAdapter = new BuyCarAdapter(getActivity(), responseList1);
                        userPrefRv.setAdapter(preferencesAdapter);

                        userPrefRv.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);

                    } else {

                        userPrefRv.setVisibility(View.GONE);
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                   // Log.e("## Records", String.valueOf(matchedRecordsResponseList.get(0).getUserID()));
                    //Log.e("## Records", String.valueOf(matchedRecordsResponseList.get(1).getUserID()));
                    /*deviceIdsList = new ArrayList<String>();
                    for(int i=0; i < matchedRecordsResponseList.size();i++){
                        //deviceIdsList.add(tokenResponse.get(i).getFcmTokenID());
                        deviceIdsList.add(matchedRecordsResponseList.get(i).getBuyerFcmDeviceId());
                        Log.e("-->IDSList",deviceIdsList.toString());
                    }

                    deviceIdsStrList = new String[deviceIdsList.size()];
                    deviceIdsStrList = deviceIdsList.toArray(deviceIdsStrList);
                    Log.e("==>Device Tokens", String.valueOf(deviceIdsStrList));*/




                }

            }
        },new com.android.volley.Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               // Toast.makeText(BuyerFragment.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
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


        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        //return matchedRecordsStatus;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 44 && resultCode == 44 && data !=null) {

            savedPrefResponse=data.getStringExtra("ResponseStr");

            savedResponseList = new Gson().fromJson(savedPrefResponse, new TypeToken<List<BuyerUserResponse>>(){}.getType());

            //List<String> imagesList = Arrays.asList(imagesListStr.split("\\s*,\\s*"));

            Log.e("savedResponseList",savedResponseList.toString());
            Log.e("^^^ Image URL",savedResponseList.get(0).getCarMake());

            preferencesAdapter.refreshList(savedResponseList);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_map_view, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.mapVw:

                Intent intent = new Intent(getActivity(),MapsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                return true;
            case R.id.notificationVw:

                Intent intent1 = new Intent(getActivity(),NotificationsActivity.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.activity_in, R.anim.activity_out);


                return true;
            default:
                break;
        }

        return false;
    }
}
