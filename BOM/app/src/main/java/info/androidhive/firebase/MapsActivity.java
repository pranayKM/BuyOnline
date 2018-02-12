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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Subbareddy on 5/14/2017.
 */

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Location location;
    private Permissions permissions;

    private String cityName,countryName;

    private List<Marker> markers = new ArrayList<>();

    private List<LatLng> latLongsList = new ArrayList<LatLng>();

    private Double myLatitude=0.0,myLongitude=0.0;

    private GoogleApiClient googleApiClient;

    final static int REQUEST_LOCATION = 199;

    private FragmentTransaction fragmentTransaction = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
     
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Map View");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        latLongsList.add(new LatLng(17.4385,78.4286));
        latLongsList.add(new LatLng(17.4294,78.4508));
        latLongsList.add(new LatLng(17.5058,78.4656));
        latLongsList.add(new LatLng(17.387140,78.491684));
        latLongsList.add(new LatLng(17.4566,78.4269));

        latLongsList.add(new LatLng(17.4637,78.3723));
        latLongsList.add(new LatLng(17.500010,78.401527));

        latLongsList.add(new LatLng(17.428310,78.538610));
        latLongsList.add(new LatLng(17.395976,78.502116));
        latLongsList.add(new LatLng(17.397804,78.508349));
        latLongsList.add(new LatLng(17.392001,78.496906));
        latLongsList.add(new LatLng(17.392290,78.517840));

        //Irvine Lat and Long
        latLongsList.add(new LatLng(33.751486,-117.754930));
        latLongsList.add(new LatLng(33.633210,-117.795971));
        latLongsList.add(new LatLng(33.691375,-117.790109));
        latLongsList.add(new LatLng(33.691261,-117.822351));
        latLongsList.add(new LatLng(33.658895,-117.828212));
        latLongsList.add(new LatLng(33.683250,-117.834073));
        latLongsList.add(new LatLng(33.647750,-117.834643));
        latLongsList.add(new LatLng(33.638702,-117.837004));
        latLongsList.add(new LatLng(33.665242,-117.749066));
        latLongsList.add(new LatLng(33.668242,-117.765924));
        latLongsList.add(new LatLng(33.707391,-117.766657));
        latLongsList.add(new LatLng(33.684587,-117.831880));
        latLongsList.add(new LatLng(33.675614,-117.758694));
        latLongsList.add(new LatLng(33.594176,-117.573064));
        latLongsList.add(new LatLng(33.647124,-117.842132));
        latLongsList.add(new LatLng(33.734484,-117.793040));
        latLongsList.add(new LatLng(33.650965,-117.707910));

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

            } else {
                Location loc = getLastKnownLocation(MapsActivity.this);
                if (loc != null) {

                    myLatitude = loc.getLatitude();
                    myLongitude = loc.getLongitude();

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
                Geocoder geocoder;

                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1);

                countryName = addresses.get(0).getCountryName();
                cityName = addresses.get(0).getLocality();
                //  fullAddress = addresses.get(0).getAddressLine(0);

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

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

            mMap = googleMap;
            mMap.clear();

            LatLng myLatLng = new LatLng(myLatitude,myLongitude);
            mMap.addMarker(new MarkerOptions()
                    .position(myLatLng)
                    .title(cityName+","+countryName)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            drawMapWithInRadius(myLatLng,latLongsList);
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

    /*Show locations with in the Radius*/
    private void drawMapWithInRadius(LatLng latLng, List<LatLng> positions) {
        for (LatLng position : positions) {
            Marker marker = mMap.addMarker(
                    new MarkerOptions()
                            .position(position)
                            .anchor(0.5f, 0.5f)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .visible(false)); // Invisible for now
            markers.add(marker);

        }

        //Draw your circle
        mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(5000)
                .strokeColor(Color.rgb(0, 136, 255))
                .fillColor(Color.argb(20, 0, 136, 255)));

        for (Marker marker : markers) {
            if (SphericalUtil.computeDistanceBetween(latLng, marker.getPosition()) < 5000) {
                marker.setVisible(true);
            }
        }
    }

    /*Find distance between two co-ordinates*/
    public String distanceInKm(double desLatitude, double desLongitude){

        /*calculating distance between two co-ordinates*/
        /*Source location*/
        Location myLocation = new Location("");
        myLocation.setLatitude(myLatitude);
        myLocation.setLongitude(myLongitude);

        /*Destination location*/
        Location eventDesLocation = new Location("");
        eventDesLocation.setLatitude(desLatitude);
        eventDesLocation.setLongitude(desLongitude);

        Double distance = Double.valueOf(myLocation.distanceTo(eventDesLocation) / 1000);

        /*Round of decimal value to one place*/
        DecimalFormat df = new DecimalFormat("###.#");
        String distanceInKm = df.format(distance);
        Intent intent = new Intent(MapsActivity.this,MainActivity.class);
        startActivity(intent);
        MapsActivity.this.finish();
        return  distanceInKm;
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

}
