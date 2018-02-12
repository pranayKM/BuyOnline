package info.androidhive.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputName,inputPhone, etLocation,inputLastName;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    //String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/register";

    String URL = "http://10.0.2.2:8080/BOMApplication/REST/Registration/register";
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Location location;
    private Permissions permissions;

    private String cityName="",countryName="", totalAddress="";

    private Double myLatitude=0.0,myLongitude=0.0;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputEmail = (EditText) findViewById(R.id.email);
        etLocation = (EditText) findViewById(R.id.etLocation);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        permissions=new Permissions(this);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*On click listener for  edit text location*/
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                chooseLocationDialog();


            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Called", Toast.LENGTH_SHORT).show();
                //String name = inputName.getText().toString().trim();
                //String phone = inputPhone.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String firstName = inputName.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String location = etLocation.getText().toString().trim();
                final String latitude = myLatitude.toString();
                final String longitude = myLongitude.toString();
                Toast.makeText(getApplicationContext(),firstName,Toast.LENGTH_SHORT).show();
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(getApplicationContext(),latitude,Toast.LENGTH_SHORT).show();
                        if(s.equals("true")){
                            Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "Can't Register", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SignupActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("firstName",inputName.getText().toString());
                        parameters.put("lastName",inputLastName.getText().toString());
                        parameters.put("phone",inputPhone.getText().toString());
                        parameters.put("email",inputEmail.getText().toString());
                        parameters.put("location",etLocation.getText().toString());
                        parameters.put("password",inputPassword.getText().toString());
                        parameters.put("latitude",latitude);
                        parameters.put("longitude",longitude);


                        /*parameters.put("email", emailBox.getText().toString());
                        parameters.put("password", passwordBox.getText().toString());*/
                        return parameters;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(SignupActivity.this);
                rQueue.add(request);

                /*if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Entered", Toast.LENGTH_SHORT).show();
                    try
                    {
                        // create a mysql database connection
                        String myDriver = "com.mysql.jdbc.Driver";
                        Toast.makeText(getApplicationContext(), "Entered 1", Toast.LENGTH_SHORT).show();
                        String myUrl = "jdbc:mysql://localhost:3306/buyonmobile";
                        Toast.makeText(getApplicationContext(), "Entered 2", Toast.LENGTH_SHORT).show();
                        Class.forName(myDriver);
                        Toast.makeText(getApplicationContext(), "Entered 3", Toast.LENGTH_SHORT).show();
                        Connection conn = DriverManager.getConnection("jdbc:mysql://Subbareddy-PC:3306/buyonmobile", "root", "root");
                        Toast.makeText(getApplicationContext(), "Entered 4", Toast.LENGTH_SHORT).show();
                        // create a sql date object so we can use it in our INSERT statement
                        Calendar calendar = Calendar.getInstance();
                        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

                        // the mysql insert statement
                        String query = " insert into uregister (name, phone, date_created, email, password)"
                                + " values (?, ?, ?, ?)";
                        Toast.makeText(getApplicationContext(), "Insert", Toast.LENGTH_SHORT).show();
                        // create the mysql insert preparedstatement
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString (1, name);
                        preparedStmt.setString (2, phone);
                        //preparedStmt.setDate   (3, startDate);
                        preparedStmt.setString(3,email);
                        preparedStmt.setString(4,password);

                        // execute the preparedstatement
                        preparedStmt.execute();
                        Toast.makeText(getApplicationContext(), "Completed Execution", Toast.LENGTH_SHORT).show();
                        conn.close();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(), "Entered 5", Toast.LENGTH_SHORT).show();
                        System.err.println("Got an exception!");
                        System.err.println(e.getMessage());
                    }


                }*/

                /*if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(etLocation.getText().toString().trim())){

                    Toast.makeText(getApplicationContext(), "Please Choose Location", Toast.LENGTH_SHORT).show();

                    return;
                }*/


                progressBar.setVisibility(View.VISIBLE);
                //create user
               /* auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });*/

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    /*Choose Location dialog*/
    public void chooseLocationDialog() {

        LayoutInflater inflater = this.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.choose_location_dialog, null);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(alertLayout);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.setView(alertLayout, 0, 0, 0, 0);

        final Button useGpsBtn = (Button) alertLayout.findViewById(R.id.currentLocBtn);
        final Button manualBtn = (Button) alertLayout.findViewById(R.id.manualBtn);

        /*Use Current Locattion on click listener*/
        useGpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCurrentLocation();
                dialog.dismiss();

            }
        });

        /*Use manual button click listener*/
        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(SignupActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }

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
                    geocoder = new Geocoder(SignupActivity.this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(myLatitude,myLongitude,1);

                    countryName = addresses.get(0).getCountryName();
                    cityName = addresses.get(0).getLocality();

                    totalAddress = cityName+","+ countryName;

                    etLocation.setText(totalAddress);

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

    /*Get Current Location using GPS*/
    public void getCurrentLocation(){

        if (permissions.checkLocationPermissions()) {
            LocationManager locManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locListener = new MyLocationListener();
            try {
                gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            try {
                network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled || !network_enabled) {

                // enableLocation();
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SignupActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("GPS Settings");

                // Setting Dialog Message
                alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu ? ");

                // On pressing Settings button
                alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }

            if (gps_enabled) {
                if (ActivityCompat.checkSelfPermission(SignupActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return  ;
                }

                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locListener);

            }

            if (gps_enabled) {

                location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }

            if (network_enabled && location == null) {
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

            }

            if (network_enabled && location == null) {
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            }
            if (location != null) {

                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();

            }
            locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.


            new GPSLocation().execute();
        }
    }

    public class GPSLocation extends AsyncTask<Void, Void, Void>  {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

// Getting address from found locations.
                Geocoder geocoder;

                List<Address> addresses;
                geocoder = new Geocoder(SignupActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);

                countryName = addresses.get(0).getCountryName();
                cityName = addresses.get(0).getLocality();
                String address1 = addresses.get(0).getAddressLine(0);
                String address2 = addresses.get(0).getAddressLine(1);

                // gpsFullAddress = address1 + "," + address2;
                // you can get more details other than this . like country code, state code, etc.

                Log.e("latitude", String.valueOf(myLatitude));
                Log.e("longitude", String.valueOf(myLongitude));

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {


            if(!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(countryName)){

                totalAddress = cityName+","+countryName;
                etLocation.setText(totalAddress);
            }

        }
    }

    // Location listener class. to get location.
    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            if (location != null) {
            }
        }

        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }
}