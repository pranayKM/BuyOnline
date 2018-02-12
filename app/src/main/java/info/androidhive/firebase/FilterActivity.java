package info.androidhive.firebase;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by pranay on 7/2/2017.
 */

public class FilterActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText carTypeEt, carModelEt, carYearEt, carPriceEt,locationEt;

    private String carBrandStr = "", carModelStr = "", carModelYrStr = "", carPriceStr = "";

    private ArrayList<String> carBrandList;


    private ArrayList<String> marutiList;
    private ArrayList<String> hyundaiList;
    private ArrayList<String> hondaList;

    private ArrayList<String> toyatoList;
    private ArrayList<String> mahindraList;
    private ArrayList<String> fordList;
    private ArrayList<String> tataList;
    private ArrayList<String> chevroletList;
    private ArrayList<String> renaultList;
    private ArrayList<String> volkswagenList;
    private ArrayList<String> nissanList;
    private ArrayList<String> datsunList;


    private ArrayList<String> carPriceList;
    private ArrayList<String> carModelsList;

    private String cityName="",countryName="", totalAddress="";

    private Double myLatitude=0.0,myLongitude=0.0;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Filter by");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        carTypeEt = (EditText) findViewById(R.id.carTypeEt);
        carModelEt = (EditText) findViewById(R.id.carModelEt);
        carYearEt = (EditText) findViewById(R.id.modelYearEt);
        carPriceEt = (EditText) findViewById(R.id.carPriceEt);
        locationEt = (EditText) findViewById(R.id.locationEt);

        carBrandList = new ArrayList<>();
        carBrandList.add("Maruti");
        carBrandList.add("Hyundai");
        carBrandList.add("Honda");
        carBrandList.add("Toyato");
        carBrandList.add("Mahindra");
        carBrandList.add("Tata");
        carBrandList.add("Ford");
        carBrandList.add("Chevrolet");
        carBrandList.add("Renault");
        carBrandList.add("Volkswagen");
        carBrandList.add("Nissan");
        carBrandList.add("Datsun");

        marutiList = new ArrayList<>();
        marutiList.add("Aaahjasfa");
        marutiList.add("Bbbb");
        marutiList.add("asnbdasn");
        marutiList.add("Dddd");
        marutiList.add("Gsndsaghd");

        hyundaiList = new ArrayList<>();
        hyundaiList.add("Aaaa");
        hyundaiList.add("Gghasfhsf");
        hyundaiList.add("Cccc");
        hyundaiList.add("Jjsasf");
        hyundaiList.add("Eeee");

        hondaList = new ArrayList<>();
        hondaList.add("Fgcjsdffjs");
        hondaList.add("Sjgasj");
        hondaList.add("Tsdsa");
        hondaList.add("Yjajsg");
        hondaList.add("Ihashj");

        toyatoList = new ArrayList<>();
        toyatoList.add("Rsahda");
        toyatoList.add("Whdjsa");
        toyatoList.add("Qnsfs");
        toyatoList.add("Anhds");
        toyatoList.add("Njsgfh");

        mahindraList = new ArrayList<>();
        mahindraList.add("Ohjsj");
        mahindraList.add("Njhgsjf");
        mahindraList.add("Xhgsfh");
        mahindraList.add("Zashd");
        mahindraList.add("Ujsjfsd");

        tataList = new ArrayList<>();
        tataList.add("Ghgash");
        tataList.add("Okjkajsj");
        tataList.add("Lkasd");
        tataList.add("Masgdjas");
        tataList.add("Zjsjdaga");

        fordList = new ArrayList<>();
        fordList.add("Thggas");
        fordList.add("Vhgagsfh");
        fordList.add("Njasaj");
        fordList.add("Skhkj");
        fordList.add("Fhghs");

        chevroletList = new ArrayList<>();
        chevroletList.add("Uhjsagj");
        chevroletList.add("Hjasas");
        chevroletList.add("Magsj");
        chevroletList.add("Bhfh");
        chevroletList.add("Eeee");

        renaultList = new ArrayList<>();
        renaultList.add("HGDjasd");
        renaultList.add("Bbbb");
        renaultList.add("Cccc");
        renaultList.add("Ngdjgsgj");
        renaultList.add("GHFhnmcx");

        volkswagenList = new ArrayList<>();
        volkswagenList.add("Polo");
        volkswagenList.add("Tiguan");
        volkswagenList.add("Ameo");
        volkswagenList.add("Vento");
        volkswagenList.add("Jetta");

        nissanList = new ArrayList<>();
        nissanList.add("Aaaa");
        nissanList.add("Bbbb");
        nissanList.add("Cccc");
        nissanList.add("Dddd");
        nissanList.add("Eeee");

        datsunList = new ArrayList<>();
        datsunList.add("Aaaa");
        datsunList.add("Ghgsdfh");
        datsunList.add("Khjashg");
        datsunList.add("Dhasgf");
        datsunList.add("Pashjhs");

        carPriceList = new ArrayList<>();
        carPriceList.add("below 1 Lakh");
        carPriceList.add("below 5 Lakh");
        carPriceList.add("below 10 Lakh");
        carPriceList.add("below 15 Lakh");
        carPriceList.add("below 20 Lakh");
        carPriceList.add("below 30 Lakh");
        carPriceList.add("below 40 Lakh");
        carPriceList.add("below 50 Lakh");
        carPriceList.add("below 75 Lakh");
        carPriceList.add("below 1 Cr");
        carPriceList.add("above 1 Cr");

        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(FilterActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }});

        /*On click listener for Car Type Edittext*/
        carTypeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carBrandsListDialog(carBrandList);
            }
        });

        /*On click listener for Car model Edit text*/
        carModelEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(carTypeEt.getText().toString().trim())) {

                    Toast.makeText(FilterActivity.this, "Please select Car Brand first", Toast.LENGTH_SHORT).show();

                } else {

                    carModelListDialog(carModelsList);
                }

            }
        });

        /*On click listener for Car Price Edittext*/
        carPriceEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                carPriceListDialog(carPriceList);
            }
        });


    }

    /*list of Car Brands*/
    public void carBrandsListDialog(final List<String> carBrandsList) {

        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.car_brand_list_dialog, null);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(alertLayout);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setView(alertLayout, 0, 0, 0, 0);

        final ListView carListVw = (ListView) alertLayout.findViewById(R.id.catListVw);
        final EditText searchEt = (EditText) alertLayout.findViewById(R.id.carSearchEt);

        Toolbar toolbarArtStyle = (Toolbar) alertLayout.findViewById(R.id.toolbarDlg);
        toolbarArtStyle.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbarArtStyle.setTitle("Select Car Brand");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.single_list_text_item, carBrandsList);
        // Assign adapter to ListView
        carListVw.setAdapter(adapter);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s.toString());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        carListVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                String carBrandStr = String.valueOf(parent.getItemAtPosition(position));
                carTypeEt.setText(carBrandStr);
                carModelEt.setText("");

                switch (carBrandStr) {

                    case "Maruti":

                        carModelsList= new ArrayList<String>();
                        carModelsList = marutiList;
                        break;
                    case "Hyundai":
                        carModelsList= new ArrayList<String>();
                        carModelsList = hyundaiList;
                        break;
                    case "Honda":
                        carModelsList= new ArrayList<String>();
                        carModelsList = hondaList;
                        break;
                    case "Toyato":
                        carModelsList= new ArrayList<String>();
                        carModelsList = toyatoList;
                        break;
                    case "Mahindra":
                        carModelsList= new ArrayList<String>();
                        carModelsList = mahindraList;
                        break;
                    case "Tata":
                        carModelsList= new ArrayList<String>();
                        carModelsList = tataList;
                        break;
                    case "Ford":
                        carModelsList= new ArrayList<String>();
                        carModelsList = fordList;
                        break;
                    case "Chevrolet":
                        carModelsList= new ArrayList<String>();
                        carModelsList = chevroletList;
                        break;
                    case "Renault":
                        carModelsList= new ArrayList<String>();
                        carModelsList = renaultList;
                        break;
                    case "Volkswagen":
                        carModelsList= new ArrayList<String>();
                        carModelsList = volkswagenList;
                        break;
                    case "Nissan":
                        carModelsList= new ArrayList<String>();
                        carModelsList = nissanList;
                        break;
                    case "Datsun":
                        carModelsList= new ArrayList<String>();
                        carModelsList = datsunList;
                        break;
                    default:
                        break;

                }

                dialog.dismiss();


            }
        });
        dialog.show();

    }

    /*list of Car prices*/
    public void carPriceListDialog(final List<String> carPriceList) {

        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.car_list_dialog, null);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(alertLayout);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setView(alertLayout, 0, 0, 0, 0);

        final ListView carListVw = (ListView) alertLayout.findViewById(R.id.catListVw);

        Toolbar toolbarArtStyle = (Toolbar) alertLayout.findViewById(R.id.toolbarDlg);
        toolbarArtStyle.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbarArtStyle.setTitle("Select Price Range");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.single_list_text_item, carPriceList);
        // Assign adapter to ListView
        carListVw.setAdapter(adapter);

        carListVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v,
                                    int position, long id) {

                String text = carPriceList.get(position);
                carPriceEt.setText(text);
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    /*list of Car Models*/
    public void carModelListDialog(final List<String> carModelsList) {

        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.car_list_dialog, null);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(alertLayout);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setView(alertLayout, 0, 0, 0, 0);

        final ListView carListVw = (ListView) alertLayout.findViewById(R.id.catListVw);

        Toolbar toolbarArtStyle = (Toolbar) alertLayout.findViewById(R.id.toolbarDlg);
        toolbarArtStyle.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbarArtStyle.setTitle("Select Car Model");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FilterActivity.this, R.layout.single_list_text_item, carModelsList);
        // Assign adapter to ListView
        carListVw.setAdapter(adapter);

        carListVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v,
                                    int position, long id) {

                String text = carModelsList.get(position);
                carModelEt.setText(text);
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                //addressTv.setText(place.getName()+",\n"+
                //    place.getAddress() +"\n" + place.getPhoneNumber());

                // placesEt.setText(place.getName());

                myLatitude = place.getLatLng().latitude;
                myLongitude = place.getLatLng().longitude;

                try {
                    // Getting address from found locations.
                    Geocoder geocoder;

                    List<Address> addresses;
                    geocoder = new Geocoder(FilterActivity.this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(myLatitude,myLongitude,1);

                    countryName = addresses.get(0).getCountryName();
                    cityName = addresses.get(0).getLocality();

                    totalAddress = cityName+","+ countryName;

                    locationEt.setText(totalAddress);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("status", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.applyBtn:

                carBrandStr = carTypeEt.getText().toString().trim();
                carModelStr = carModelEt.getText().toString().trim();
                carModelYrStr = carYearEt.getText().toString().trim();
                carPriceStr = carPriceEt.getText().toString().trim();

                if (TextUtils.isEmpty(carBrandStr)) {

                    Toast.makeText(FilterActivity.this, "Please select Car Brand", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent();
                    intent.putExtra("carBrandStr",carBrandStr);
                    intent.putExtra("carModelStr",carModelStr);
                    intent.putExtra("carPriceStr",carPriceStr);
                    intent.putExtra("carYearStr",carModelYrStr);
                    intent.putExtra("cityStr",cityName);
                    intent.putExtra("countryStr",countryName);
                    intent.putExtra("totalAddress",totalAddress);
                    intent.putExtra("latitude",myLatitude);
                    intent.putExtra("longitude",myLongitude);
                    setResult(33,intent);
                    finish();

                }
                return true;

            case android.R.id.home:

                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        super.onBackPressed();

        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }
}