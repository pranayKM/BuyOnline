package info.androidhive.firebase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputName,inputPhone, etLocation,inputLastName;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    //private FirebaseAuth auth;

    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Registration/register";

    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/Registration/register";
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Location location;
    private Permissions permissions;

    private String cityName="",countryName="", totalAddress="";

    private Double myLatitude=0.0,myLongitude=0.0;

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 11;

    private ImageView profileIv;

    private String imageFileName = null, imageFileNameUrl;

    private File outPutFile = null;

    private Uri mImageCaptureUri;

    private static final int CAMERA_IMAGE_CAPTURE = 1;

    private static final int REQUEST_IMAGE_PICK = 2;

    private ProgressDialogBar dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
      //  auth = FirebaseAuth.getInstance();

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
        profileIv = (ImageView) findViewById(R.id.userProfileIv);

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

        profileIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (permissions.checkAndRequestPermissions()) {
                    cameraGalleryDialog();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(inputName.getText().toString().trim()) && !TextUtils.isEmpty(inputLastName.getText().toString().trim()) && !TextUtils.isEmpty(inputEmail.getText().toString().trim())
                        && !TextUtils.isEmpty(inputPassword.getText().toString().trim())&& !TextUtils.isEmpty(etLocation.getText().toString().trim())&& !TextUtils.isEmpty(inputPhone.getText().toString().trim())) {

                    if (InternetConnection.isNetworkAvailable(SignupActivity.this)) {

                        new SignUpTask().execute();

                    } else {
                        Toast.makeText(SignupActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(SignupActivity.this,"All Fields are mandatory",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    /*Sign up service*/
    private void signUpService(){

        if (imageFileName != null) {
            if (imageFileName.length() > 0) {

                // Trail version URL
                String couldinaryURL = "cloudinary://969892397415277:yX0OqpKoM8YjfyxoYs0Tmj5_jTU@dyrcjup4p";
                Cloudinary cloudinary = new Cloudinary(couldinaryURL);
                String path = ImageUtils.filePath(this, imageFileName);
                Map uploadResult = null;
                if (path.length() > 0) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(path);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        uploadResult = cloudinary.uploader().upload(fis, ObjectUtils.asMap("public_id", "pro/" + timeStamp));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    // return "";
                }

                imageFileNameUrl = uploadResult.get("url").toString();

                Log.e("image url ", "@@@@@@@@@@@ " + imageFileNameUrl);

            }
        }

            //Toast.makeText(getApplicationContext(), "Called", Toast.LENGTH_SHORT).show();
            //String name = inputName.getText().toString().trim();
            //String phone = inputPhone.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String firstName = inputName.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            final String latitude = myLatitude.toString();
            final String longitude = myLongitude.toString();

            //Toast.makeText(getApplicationContext(),firstName,Toast.LENGTH_SHORT).show();
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(SignupActivity.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                    ;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("firstName", inputName.getText().toString());
                    parameters.put("lastName", inputLastName.getText().toString());
                    parameters.put("phone", inputPhone.getText().toString());
                    parameters.put("email", inputEmail.getText().toString());
                    parameters.put("location", etLocation.getText().toString());
                    parameters.put("password", inputPassword.getText().toString());
                    parameters.put("latitude", latitude);
                    parameters.put("longitude", longitude);
                    parameters.put("imageUrl", imageFileNameUrl);
                    parameters.put("deviceTokenId", Preferences.fcmDeviceTokenID);
                    //Log.e("@@@ImageURL",imageFileNameUrl);

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


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    // Camera dialog to capture image from camera or from gallery
    private void cameraGalleryDialog() {

        new AlertDialog.Builder(this)
                //  .setIcon(R.drawable.app_icon)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Choose image source")
                .setPositiveButton("Camera",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                takePicture();
                            }
                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                choseImageFromGallery();
                            }
                        })
                .create()
                .show();
    }

    // Open  camera to capture image
    private void takePicture() {

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        imageFileName = timeStamp + ".png";

        outPutFile = new File(ImageUtils.filePath(this, imageFileName));

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = Uri.fromFile(outPutFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_CAPTURE);
    }

    // open gallery to capture image.
    private void choseImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture)), REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CAMERA_IMAGE_CAPTURE && resultCode == RESULT_OK) {


                File filePath = new File(ImageUtils.filePath(this, imageFileName));
                File compressedCameraImageFile = Compressor.getDefault(this).compressToFile(filePath);

                InputStream imageStream = new FileInputStream(compressedCameraImageFile);

                Log.e("originalCameSize", ImageUtils.getReadableFileSize(filePath.length()));
                Log.e("compressCameSize", ImageUtils.getReadableFileSize(compressedCameraImageFile.length()));


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 0;
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream, null, options);


                try {
                    ExifInterface ei = new ExifInterface(ImageUtils.filePath(this, imageFileName));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            imageBitmap = rotateImage(imageBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            imageBitmap = rotateImage(imageBitmap, 180);
                            break;
                        // etc.
                    }
                } catch (IOException e) {
                    Log.e("error", "" + e.getMessage());
                }

                new cameraAsyncTask().execute(imageBitmap);

            } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK
                    && null != data) {

                try {

//                    final Uri imageUri = data.getData();
//                    final InputStream imageStream =getContentResolver().openInputStream(imageUri);


                    final Uri imageUri = data.getData();

                    File galleryFilePath = new File(ImageFilePath.getPath(this,imageUri));

                    File compressedGalleryImageFile = Compressor.getDefault(this).compressToFile(galleryFilePath);

                    InputStream imageStream = new FileInputStream(compressedGalleryImageFile);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 0;
                    Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream, null, options);

                    try {
                        ExifInterface ei = new ExifInterface(ImageUtils.filePath(this, imageFileName));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                imageBitmap = rotateImage(imageBitmap, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                imageBitmap = rotateImage(imageBitmap, 180);
                                break;
                            // etc.
                        }
                    } catch (IOException e) {
                        Log.e("error", "" + e.getMessage());
                    }

                    new cameraAsyncTask().execute(imageBitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
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
        } catch (Exception e) {
           // Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
        }


    }
    // rotate bitmap image in specific angle
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    // Background async task save captured image to local storage.
    public class cameraAsyncTask extends AsyncTask<Bitmap, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress = new ProgressDialogBar(SignupActivity.this);
            dialogProgress.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Bitmap... params) {

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            imageFileName = timeStamp + ".png";
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            params[0].compress(Bitmap.CompressFormat.PNG, 100, bytes);
            ImageUtils.saveFile(SignupActivity.this, params[0], imageFileName);

           /* if(!TextUtils.isEmpty(imageFileName)) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date());
                imageFileName = "JPEG_" + timeStamp + "_.jpg";
                //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               // params[0].compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                artistBitmap = ImageUtils.scaleImage(new File(imageFileName));
                ImageUtils.saveFile(ArtistSignUpActivity.this, artistBitmap, imageFileName);
            }*/
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (imageFileName != null) {

                Bitmap bitmap = ImageUtils.loadBitmap(SignupActivity.this, imageFileName);
                profileIv.setImageBitmap(bitmap);

            }

            dialogProgress.dismissProgressDialog();

        }

    }

    public class SignUpTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogProgress = new ProgressDialogBar(SignupActivity.this);
            dialogProgress.showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {

             signUpService();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            dialogProgress.dismissProgressDialog();

        }
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

                //Log.e("latitude", String.valueOf(myLatitude));
                //Log.e("longitude", String.valueOf(myLongitude));

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