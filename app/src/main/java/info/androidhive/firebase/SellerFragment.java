package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.androidhive.firebase.adapters.UserPreferencesAdapter;
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

    private FloatingActionButton sellACarBtn;

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
        sellACarBtn = (FloatingActionButton) view.findViewById(R.id.sellCarBtn);

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

                        UserPreferencesAdapter preferencesAdapter= new UserPreferencesAdapter(getActivity(),responseList);
                        sellerRv.setAdapter(preferencesAdapter);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 33 && resultCode == 33) {

            getUserPreferences();

        }
    }
}
