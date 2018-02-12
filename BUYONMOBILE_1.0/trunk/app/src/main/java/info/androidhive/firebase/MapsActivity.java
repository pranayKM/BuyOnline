package info.androidhive.firebase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.androidhive.firebase.restservices.dto.DeviceTokenResponse;
import info.androidhive.firebase.restservices.dto.FCMData;
import info.androidhive.firebase.restservices.dto.FCMNotification;
import info.androidhive.firebase.restservices.dto.FCMNotificationsRequest;
import info.androidhive.firebase.restservices.dto.MatchedRecordResponse;
import info.androidhive.firebase.restservices.dto.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*** Created by Subbareddy on 5/14/2017.*/

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private boolean gps_enabled = false;

    private boolean network_enabled = false;

    private Location location;

    private Permissions permissions;

    private String cityName,countryName,postalCode,fullAddress;

    private String cityName1,countryName1,postalCode1,fullAddress1;

    private List<Marker> markers = new ArrayList<>();

    private List<LatLng> latLongsList = new ArrayList<>();

    private Double myLatitude=0.0,myLongitude=0.0,userLatitude = 0.0,userLongitude=0.0;

    private Double ulat = 0.0,ulng = 0.0;

    private GoogleApiClient googleApiClient;

    private ProgressDialogBar dialogProgress;

    private RecyclerView userPrefRv;

    private TextView emptyTv;

    private Double latitudeList[];
    private Double longitudeList[];

    final static int REQUEST_LOCATION = 199;

    private FragmentTransaction fragmentTransaction = null;

    private List<LatLng> distanceLatLngList ;

    private List<String> deviceIdsList;

    private String[] deviceIdsStrList;

    private List<MatchedRecordResponse> matchedList;

    private LatLng myLatLng;

    private boolean matchedRecordsStatus = false;

    private int i = 0;

    private ArrayList<MatchedRecordResponse> matchedRecordsResponseList= new ArrayList<>();

    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/Save/nofitications";

    //String URL1 = "http://10.0.2.2:8080/BOMApplication/REST/Seller/machedSellerDetails";;

    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Save/nofitications";

    String URL1 = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Seller/machedSellerDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Map View");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getMachedSellersDetails();
        getDeviceTokenIDsService();

        permissions=new Permissions(MapsActivity.this);

        if (permissions.checkLocationPermissions()) {
            LocationManager locManager = (LocationManager)MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
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
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(MapsActivity.this);
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
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Log.e("--==>My Current Locat@", String.valueOf(location));
            }

            if (network_enabled && location == null) {
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

            }

            if (network_enabled && location == null) {
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.e("--==>My Current Locat", String.valueOf(location));

            }
            if (location != null) {

                myLatitude = location.getLatitude();
                myLongitude = location.getLongitude();
                //getDistance(latitudeList[0],longitudeList[0]);

            } else {
                Location loc = getLastKnownLocation(MapsActivity.this);
                if (loc != null) {
                    Log.e("--==Called 3", String.valueOf(0));
                    myLatitude = loc.getLatitude();
                    myLongitude = loc.getLongitude();
                    getDistance(latitudeList[0],longitudeList[0]);

                }
            }
            locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.


            new  GPSLocation().execute();
        }
    }

    public class GPSLocation extends AsyncTask<Void, Void, Void> implements OnMapReadyCallback {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

// Getting address from found locations.
                getLatandLon();
                Geocoder geocoder;

                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);

                countryName = addresses.get(0).getCountryName();
                cityName = addresses.get(0).getLocality();
                postalCode = addresses.get(0).getPostalCode();
                fullAddress = addresses.get(0).getAddressLine(0);
                String address1 = addresses.get(0).getAddressLine(0);
                String address2 = addresses.get(0).getAddressLine(1);


                // gpsFullAddress = address1 + "," + address2;
                // you can get more details other than this . like country code, state code, etc.
                Log.e("City Name",cityName);
                Log.e("Country Name",countryName);
                Log.e("Postal Code",postalCode);
                Log.e("Full Address",fullAddress);
                Log.e("latitude", String.valueOf(myLatitude));
                Log.e("longitude", String.valueOf(myLongitude));


                distanceLatLngList = new ArrayList<>();
                Log.e("$$$ Lat Long List", String.valueOf(latLongsList));
                /*for(int i=0; i < latLongsList.size() ; i++ ){
                    Log.e("==> Entered", String.valueOf(0));
                   Double distance = distanceInKm(latLongsList.get(i).latitude, latLongsList.get(i).longitude);

                    Log.e("distance", String.valueOf(distance));

                    if(distance <= 5.0){

                        distanceLatLngList.add(new LatLng(latLongsList.get(i).latitude, latLongsList.get(i).longitude));

                    }

                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.e("@@@ This is Enetered1", String.valueOf(myLatitude));

            mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            mMap = googleMap;
            mMap.clear();

            myLatLng = new LatLng(myLatitude,myLongitude);
            mMap.addMarker(new MarkerOptions()
                    .position(myLatLng)
                    .title(fullAddress+","+cityName+","+countryName)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            /*if(distanceLatLngList.size() > 0 || distanceLatLngList !=null) {
                drawMapWithInRadius(myLatLng, distanceLatLngList);
                Log.e("Entered into loop", String.valueOf(0));
            }*/
           // getDistance(latitudeList[0],longitudeList[0]);
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

// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.

    public static Location getLastKnownLocation(Context context) {
        Location location = null;
        LocationManager locationmanager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List list = locationmanager.getAllProviders();
        boolean i = false;
        Iterator iterator = list.iterator();
        do {
            if (!iterator.hasNext())
                break;
            String s = (String) iterator.next();
            //if(i != 0 && !locationmanager.isProviderEnabled(s))
            if (i != false && !locationmanager.isProviderEnabled(s))
                continue;
            // System.out.println("provider ===> "+s);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return location;
            }
            Location location1 = locationmanager.getLastKnownLocation(s);
            if (location1 == null)
                continue;
            if (location != null) {
                //System.out.println("location ===> "+location);
                //System.out.println("location1 ===> "+location);
                float f = location.getAccuracy();
                float f1 = location1.getAccuracy();
                if (f >= f1) {
                    long l = location1.getTime();
                    long l1 = location.getTime();
                    if (l - l1 <= 600000L)
                        continue;
                }
            }
            location = location1;
            // System.out.println("location  out ===> "+location);
            //System.out.println("location1 out===> "+location);
            i = locationmanager.isProviderEnabled(s);
            // System.out.println("---------------------------------------------------------------------");
        } while (true);
        return location;
    }

   /* private void enableLocation() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

    }*/
   public void getDistance(Double lat, Double lng){

       double theta = myLongitude - lng;
       double dist = Math.sin(deg2rad(myLatitude))
               * Math.sin(deg2rad(lat))
               + Math.cos(deg2rad(myLatitude))
               * Math.cos(deg2rad(lat))
               * Math.cos(deg2rad(theta));
       dist = Math.acos(dist);
       dist = rad2deg(dist);
       dist = dist * 60 * 1.1515;

       Log.e("--===>Distance ", String.valueOf(dist));
       Log.e("==>Called", String.valueOf(myLatitude));

   }

    private double deg2rad(double deg) {

        return (deg * Math.PI / 180.0);

    }

    private double rad2deg(double rad) {

        return (rad * 180.0 / Math.PI);
    }

       /*double earthRadius = 3958.75;

       double dLat = Math.toRadians(myLatitude-lat);
       double dLng = Math.toRadians(myLatitude-lng);
       double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
               Math.cos(Math.toRadians(lat)) * Math.cos(Math.toRadians(myLatitude)) *
                       Math.sin(dLng/2) * Math.sin(dLng/2);
       double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
       double dist = earthRadius * c;*/



    /*Show locations with in the Radius*/
    private void drawMapWithInRadius(LatLng latLng, List<LatLng> positions) throws IOException {

        Geocoder geocoder1;
        List<Address> addresses1;
        geocoder1 = new Geocoder(MapsActivity.this, Locale.getDefault());

        List<Address> addresses;
        Geocoder geocoder;
        Log.e("@@@ THis is Entered","");
        for (LatLng position : positions) {
            Double latitude = position.latitude;
            Double longitude = position.longitude;
            Log.e("%%% For Each Lat", String.valueOf(latitude));
            Log.e("%%% For Each Long", String.valueOf(longitude));
            addresses1 = geocoder1.getFromLocation(latitude, longitude, 1);
            countryName1 = addresses1.get(0).getCountryName();
            cityName1 = addresses1.get(0).getLocality();
            postalCode1 = addresses1.get(0).getPostalCode();
            fullAddress1 = addresses1.get(0).getAddressLine(0);
            String street = addresses1.get(0).getSubLocality();
            /*String street1 = addresses1.get(0).getSubAdminArea();
            Log.e("%%% Street",street);
            Log.e("%%% Street1",street1);*/

            Log.e("%%% Positions", String.valueOf(positions));
            Log.e("%%% Country Name", countryName1);
            Log.e("%%% cityName", cityName1);
            Log.e(" %%%postalCode", postalCode1);
            Log.e("%%% Full Address", fullAddress1);

            Marker marker = mMap.addMarker(
                    new MarkerOptions()
                            .position(position)
                            .title(street+","+cityName1+","+countryName1)
                            .anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            //markers.add(marker);

            }

        //Draw your circle
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(5000)
                .strokeColor(Color.rgb(0, 136, 255))
                .fillColor(Color.argb(20, 0, 136, 255)));

        for (Marker marker : markers) {
            Log.e("@@ Marker", String.valueOf(marker));
            Log.e("@@@Markers size", String.valueOf(markers.size()));
            if (SphericalUtil.computeDistanceBetween(latLng, marker.getPosition()) < 5000) {
                marker.setVisible(true);
                marker.setTitle("");

            }
        }
    }

    /*Find distance between two co-ordinates*/
    public double distanceInKm(double desLatitude, double desLongitude){

        /*calculating distance between two co-ordinates*/
        /*Source location*/
        Location myLocation = new Location("");
        myLocation.setLatitude(myLatitude);
        myLocation.setLongitude(myLongitude);

        /*Destination location*/
        Location eventDesLocation = new Location("");
        eventDesLocation.setLatitude(desLatitude);
        eventDesLocation.setLongitude(desLongitude);

        Double distance = (double) (myLocation.distanceTo(eventDesLocation) / 1000);

        /*Round of decimal value to one place*/
        DecimalFormat df = new DecimalFormat("###.#");
        //        Intent intent = new Intent(MapsActivity.this,MainActivity.class);
        Log.e("@@ Distance", String.valueOf(distance));
//        startActivity(intent);
//        MapsActivity.this.finish();
        return Double.parseDouble(df.format(distance));
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {


            case android.R.id.home:

                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }

    final Call<List<UserDetails>> usersList = BOMApplication.getRestClient().getApiService().getUserDetails();

    public void getLatandLon(){
        //Log.e("@@@ Maps Users List", String.valueOf(usersList));

        Log.e("@@@Entered","");
        usersList.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> responseCall, Response<List<UserDetails>> response) {

                //dialogProgress.dismissProgressDialog();
                //Log.e("@@@MAps Response", String.valueOf(response));

                if (response.code() == 200) {

                    //distanceLatLngList = new ArrayList<>();
                    List<UserDetails> responseList1 = response.body();
                    //latitudeList = new Double[responseList1.size()];
                    //longitudeList = new Double[responseList1.size()];
                    Log.e("@@@ Maps Response", String.valueOf(responseList1));
                    for(int i = 0; i < responseList1.size(); i++){
                        Log.e("@@ New ==>",responseList1.get(i).getEmailId());
                        Log.e("@@ New Lat & Long", String.valueOf(responseList1.get(i).getLatitude()));
                        userLatitude = responseList1.get(i).getLatitude();
                        userLongitude = responseList1.get(i).getLongitude();
                        latLongsList.add(new LatLng(userLatitude,userLongitude));
                       }
                    Log.e("$$$ Lat Long List", String.valueOf(latLongsList.size()));
                    for(int i=0; i < latLongsList.size() ; i++ ){
                        Log.e("==> Entered", String.valueOf(0));
                        Double distance = distanceInKm(latLongsList.get(i).latitude, latLongsList.get(i).longitude);

                        Log.e("-->distance", String.valueOf(distance));

                        if(distance <= 5.0){
                            matchedRecordsStatus= getMachedSellersDetails();

                            Log.e("==> Entered distan loop", String.valueOf(distance));
                            distanceLatLngList.add(new LatLng(latLongsList.get(i).latitude, latLongsList.get(i).longitude));

                            Log.e("-->MatchedRecordsStatus", String.valueOf(matchedRecordsStatus));
                            if(matchedRecordsStatus)
                            {
                                sendNotificationOnTriggering("Here is the Seller details that matches your preferences Name:'"+matchedRecordsResponseList.get(0).getSellerFirstName()+"','"+matchedRecordsResponseList.get(0).getSellerPhone()+"','"+matchedRecordsResponseList.get(0).getUserEmailId()+"'  ");
                            }

                        }

                    }
                    if(distanceLatLngList.size() > 0 || distanceLatLngList !=null) {
                        try {
                            drawMapWithInRadius(myLatLng, distanceLatLngList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("Entered into loop", String.valueOf(0));
                    }
                    Log.e("==> Lat Long List", String.valueOf(distanceLatLngList));

                    //sendNotificationOnTriggering("Here is the Car owner details. Name: '"+matchedRecordsResponseList.get(0).getSellerFirstName()+"' ");

                }
            }



            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {

                dialogProgress.dismissProgressDialog();
                //Toast.makeText(getCallingActivity(),  "Server Error", Toast.LENGTH_SHORT).show();
            }
        });


    }


    /*send push notifications on creating event*/
    public void sendNotificationOnTriggering(final String messageBody) {

        final ProgressDialogBar dialogProgress = new ProgressDialogBar(MapsActivity.this);
        dialogProgress.showProgressDialog();

        FCMNotificationsRequest notificationsRequest = new FCMNotificationsRequest();
        Preferences.loadPreferences(MapsActivity.this);

//        String[] deviceIDsList = new String[userFcmDeviceIDsList.size()];
//        deviceIDsList = userFcmDeviceIDsList.toArray(deviceIDsList);
//        notificationsRequest.setRegisteredUserDeviceIDsList(deviceIDsList);

        //notificationsRequest.setToTokenID("cAXIRMIk7ck:APA91bF7ROWwGFM5wYrfnY5B4JpmK44ynz9OGaP_CyfYOuZlWKNWPylUKipR8Th-vlD8dGN3qH42zwA3ZRuB6CLTlE8MwQ3p3EYIOBOJvLZ1EV43kAcyRwx9NCNieKymjdjiC4ZxJyVT");
         notificationsRequest.setRegisteredUserDeviceIDsList(deviceIdsStrList);

        FCMNotification notifications = new FCMNotification();
        notifications.setFcmBody(messageBody);
        notifications.setFcmTitle("BuyOnMobile");
        notificationsRequest.setFcmNotification(notifications);
        FCMData fcmData = new FCMData();
        fcmData.setFcmMsg("BuyOnMobile");

        notificationsRequest.setFcmData(fcmData);

        Call<Void> sendNotifications = BOMApplication.getRestClient().getFcmApiService().sendFCMPushNotifications(notificationsRequest);

        sendNotifications.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> responseCall, Response<Void> response) {

                dialogProgress.dismissProgressDialog();

                if (response.code() == 200) {
                    //getMachedSellersDetails();
                    Log.e("*** Notification Event", String.valueOf(0));
                  saveEventNotifications(messageBody);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                dialogProgress.dismissProgressDialog();
                Toast.makeText(MapsActivity.this,"Server error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*send push notifications on creating event*/
    public void getDeviceTokenIDsService() {

        final ProgressDialogBar dialogProgress = new ProgressDialogBar(MapsActivity.this);
        dialogProgress.showProgressDialog();

        Call<List<DeviceTokenResponse>> tokenResponse = BOMApplication.getRestClient().getApiService().getDeviceTokeDetails();

        tokenResponse.enqueue(new Callback<List<DeviceTokenResponse>>() {
            @Override
            public void onResponse(Call<List<DeviceTokenResponse>> responseCall, Response<List<DeviceTokenResponse>> response) {

                dialogProgress.dismissProgressDialog();

                if (response.code() == 200 && response.body() !=null) {

                    List<DeviceTokenResponse> tokenResponse = response.body();

                    //deviceIdsList = new ArrayList<String>();

                    /*for(int i=0; i < matchedRecordsResponseList.size();i++){

                        //deviceIdsList.add(tokenResponse.get(i).getFcmTokenID());
                        deviceIdsList.add(matchedRecordsResponseList.get(i).getBuyerFcmDeviceId());

                        Log.e("IDSList",deviceIdsList.toString());
                    }

                    deviceIdsStrList = new String[deviceIdsList.size()];
                    deviceIdsStrList = deviceIdsList.toArray(deviceIdsStrList);
                    Log.e("==>Device Tokens", String.valueOf(deviceIdsStrList));*/

                    //getLatandLon();

                }

            }

            @Override
            public void onFailure(Call<List<DeviceTokenResponse>> call, Throwable t) {

                dialogProgress.dismissProgressDialog();
                Toast.makeText(MapsActivity.this,"Server error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveEventNotifications(String s) {
        Log.e("@@Before GetMa", String.valueOf(0));
        //getMachedSellersDetails();
        final String notificationMsg = s.trim();

        //Toast.makeText(getApplicationContext(),firstName,Toast.LENGTH_SHORT).show();
        if (i == 0) {
            Log.e("@@After GetMa", String.valueOf(0));
            StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    if (s.equals("true")) {
                        Toast.makeText(MapsActivity.this, "Your Notifications Saved Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(new Intent(MapsActivity.this, SlideMenuActivity.class));
                        startActivity(intent);
                    } else {
                        Toast.makeText(MapsActivity.this, "Can't Saved Your Notification", Toast.LENGTH_LONG).show();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MapsActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                    ;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("notification", notificationMsg);
                    parameters.put("emailId", Preferences.emailID);
                    Log.e("Preferences Emai", Preferences.emailID);

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

            RequestQueue rQueue = Volley.newRequestQueue(MapsActivity.this);
            rQueue.add(request);

            i++;
        }
    }

    public boolean getMachedSellersDetails() {
        String emailId = Preferences.emailID;
        Log.e("###Notification Email", emailId);
        StringRequest request = new StringRequest(Request.Method.POST, URL1, new com.android.volley.Response.Listener<String>() {

            @Override
            public void onResponse(String s) {

                Log.e("Response", s);
                Log.e("$$$$Response", s);
                if (!s.equals("No Records found")) {
                    matchedRecordsResponseList = new Gson().fromJson(s, new TypeToken<List<MatchedRecordResponse>>() {
                    }.getType());
                    Log.e("@@@Size", String.valueOf(matchedRecordsResponseList.size()));
                    deviceIdsList = new ArrayList<String>();
                    for (int i = 0; i < matchedRecordsResponseList.size(); i++) {
                        //deviceIdsList.add(tokenResponse.get(i).getFcmTokenID());
                        deviceIdsList.add(matchedRecordsResponseList.get(i).getBuyerFcmDeviceId());
                        Log.e("-->IDSList", deviceIdsList.toString());
                    }

                    deviceIdsStrList = new String[deviceIdsList.size()];
                    deviceIdsStrList = deviceIdsList.toArray(deviceIdsStrList);
                    Log.e("==>Device Tokens", String.valueOf(deviceIdsStrList));

                    Log.e("-->Size", String.valueOf(matchedRecordsResponseList.size()));
                    Log.e("@@ Hellow", String.valueOf(matchedRecordsResponseList));
                    Log.e("@@ Email Id", matchedRecordsResponseList.get(0).getUserEmailId());
                    Log.e("Hello", matchedRecordsResponseList.get(0).getSellerPhone());
                    Log.e("@@ Name", matchedRecordsResponseList.get(0).getSellerFirstName());
                    Log.e("@@ Last Name", matchedRecordsResponseList.get(0).getSellerLastName());
                    Log.e("@@ Buyer Device ID", matchedRecordsResponseList.get(0).getBuyerFcmDeviceId());

                    matchedRecordsStatus = true;
                    Log.e("@@Inside Status", String.valueOf(matchedRecordsStatus));
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MapsActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                ;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("emailId", Preferences.emailID);


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

        RequestQueue rQueue = Volley.newRequestQueue(MapsActivity.this);
        rQueue.add(request);

        return matchedRecordsStatus;

    }
    /*public void refreshList(List<MatchedRecordResponse> usersList1) {

        Log.e("@@ List Size", String.valueOf(usersList1.size()));
        if (usersList1.size() > 0) {

            //matchedList.clear();
            matchedList.addAll(usersList1);

            Log.e("@@@ Mached List", String.valueOf(matchedList));
            //notifyDataSetChanged();

        }

    }*/

}
