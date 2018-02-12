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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import info.androidhive.firebase.adapters.UserPreferencesAdapter;
import info.androidhive.firebase.restservices.dto.UserDetails;
import info.androidhive.firebase.restservices.dto.UserPreferencesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pranay  on 5/14/2017.
 */

public class BuyerFragment extends Fragment {

    private Toolbar toolbar;

    private FragmentTransaction fragmentTransaction = null;

    private RecyclerView userPrefRv;

    private LinearLayoutManager mLayoutManager;

    private TextView emptyTv;

    private ProgressDialogBar dialogProgress;

    private FloatingActionButton filterBtn;

    private String carBrandStr = "", carModelStr = "", carModelYrStr = "", carPriceStr = "";

    private String cityStr="",countryStr="",totalAddressStr="";

    private double latitude=0.0,longitude=0.0;

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

        mLayoutManager= new LinearLayoutManager(getActivity());
        userPrefRv.setLayoutManager(mLayoutManager);
        userPrefRv.setHasFixedSize(true);


        if (InternetConnection.isNetworkAvailable(getActivity())) {
            getUserPreferences();
        } else {
            Toast.makeText(getActivity(),"No Internet", Toast.LENGTH_SHORT).show();
        }

        /*Filter Button on click listener*/
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),FilterActivity.class);
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
        final Call<List<UserDetails>> usersList = BOMApplication.getRestClient().getApiService().getUserDetails();

        preferencesList.enqueue(new Callback<List<UserPreferencesResponse>>() {
            @Override
            public void onResponse(Call<List<UserPreferencesResponse>> responseCall, Response<List<UserPreferencesResponse>> response) {

                dialogProgress.dismissProgressDialog();

                if (response.code() == 200) {

                    List<UserPreferencesResponse> responseList = response.body();

                    if (responseList.size() > 0 && responseList != null) {

                        UserPreferencesAdapter preferencesAdapter= new UserPreferencesAdapter(getActivity(),responseList);
                        userPrefRv.setAdapter(preferencesAdapter);

                        userPrefRv.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);

                    }else {

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
                Toast.makeText(getActivity(),  "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 33 && resultCode == 33) {

            carBrandStr=data.getStringExtra("carBrandStr");
            carModelStr=data.getStringExtra("carModelStr");
            carPriceStr=data.getStringExtra("carPriceStr");
            carModelYrStr=data.getStringExtra("carYearStr");
            cityStr=data.getStringExtra("cityStr");
            countryStr=data.getStringExtra("countryStr");
            totalAddressStr=data.getStringExtra("totalAddress");
            latitude= data.getDoubleExtra("latitude",0.0);
            longitude= data.getDoubleExtra("longitude",0.0);

            Log.e("CarBrand",carBrandStr);
            Log.e("carModel",carModelStr);
            Log.e("carPrice",carPriceStr);
            Log.e("carYear",carModelYrStr);
            Log.e("cityStr",cityStr);
            Log.e("countryStr",countryStr);
            Log.e("totalAddressStr",totalAddressStr);
            Log.e("latitude", String.valueOf(latitude));
            Log.e("longitude", String.valueOf(longitude));

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
            default:
                break;
        }

        return false;
    }
}
