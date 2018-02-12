package info.androidhive.firebase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Subbareddy on 7/23/2017.
 */

public class SellCarActivity extends AppCompatActivity{

    private ImageView carImageVw;

    private EditText carNameEt,carModelEt,carYearEt,priceEt;

    private Button submitBtn,uploadImageBtn;

    private String carImageUrl,emailIdStr;

    private Toolbar toolbar;

    private String carNameStr,carModelStr,carYearStr,carPriceStr;

    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/CarPreferences/car";

    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/CarPreferences/car";

    public int userID = 0;

    private GridView carImageGridVw;

    private GridImagesAdapter adapter;

    private String imageFileName = null;

    private String imageFileNameUrl;

    private List<String> finalListOfImages = new ArrayList<>();

    private List<String> listOfUrlImages = new ArrayList<>();

    private ProgressDialogBar dialogProgress;

    private int i = 0;

    private static final int REQUEST_CODE_GALLERY = 1000;

    public List<String> imageArray = new ArrayList<>();

    public List<String> imageEditArray = new ArrayList<>();

    public ArrayList<Image> galleryImagesList = new ArrayList<>();

    private Permissions permissions;

    private int userPreferedId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_car);

        userPreferedId= getIntent().getIntExtra("carID",0);
        carNameStr = getIntent().getStringExtra("carName");
        carModelStr = getIntent().getStringExtra("carModel");
        carYearStr = getIntent().getStringExtra("carYear");
        carPriceStr = getIntent().getStringExtra("carPrice");
        userID = getIntent().getIntExtra("userId",0);
        emailIdStr=getIntent().getStringExtra("emailId");
        imageEditArray = getIntent().getStringArrayListExtra("carImages");
        //carImageUrl = getIntent().getStringExtra("carImage");
      //  Toast.makeText(getApplicationContext(),"Email Id." +emailIdStr,Toast.LENGTH_SHORT).show();


        viewsInitialization();

//        setResult(33);
//        finish();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sell a Car");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editCarDetails();


        permissions = new Permissions(SellCarActivity.this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("UploadCarWithDetails()", "" );
                uploadCarWithDetails();
               /* if(!TextUtils.isEmpty(userID)){

                    //updateCardDetails(userID);

                }else {

                    Log.e("UploadCarWithDetails()", "" );
                    uploadCarWithDetails();

                }*/
            }
        });

     /*   carImageVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permission.checkAndRequestPermissions()) {
                    cameraGalleryDialog();
                }
            }
        });
*/
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageArray.size() < 4) {

                    if (permissions.checkAndRequestPermissions())
                        loadFormGallery();
                } else {

                    Toast.makeText(getApplicationContext(),"Limit exceeded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadFormGallery() {

        ImagePicker.create(this)
                .returnAfterFirst(false) // set whether pick or camera action should return immediate result or not. For pick image only work on single mode
                .folderMode(false) // folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(4-imageArray.size()) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .origin(galleryImagesList) // original selected images, used in multi mode
                .enableLog(false) // disabling log
                .theme(R.style.AppTheme)
                .start(REQUEST_CODE_GALLERY); // start image picker activity with request code
    }

    private void editCarDetails(){

        GridImagesAdapter adapter = new GridImagesAdapter(this);
        carImageGridVw.setAdapter(adapter);

        carNameEt.setText(carNameStr);
        carModelEt.setText(carModelStr);
        carYearEt.setText(carYearStr);
        priceEt.setText(carPriceStr);

      /*  if (!TextUtils.isEmpty(profileImageUrl)) {

            Picasso.with(context)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.dummy_pic)
                    .fit()
                    .into(viewHolder.carImageVw);

        }else {

            viewHolder.carImageVw.setImageResource(R.mipmap.ic_launcher);
        }*/
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK
                && data != null){

            galleryImagesList = (ArrayList<Image>) ImagePicker.getImages(data);

            new MultiImageLoadTask().execute();


        }
    }

    /*Load and Save Images from Gallery*/
    private void loadAndSaveImages(){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss.SSS").format(new Date());
        String imageFileName =timeStamp + ".jpg";
        Bitmap yourbitmap = ImageUtils.scaleImage(new File(galleryImagesList.get(0).getPath()));
        ImageUtils.saveFile(SellCarActivity.this, yourbitmap, imageFileName);
        imageArray.add(imageFileName);

        galleryImagesList.remove(0);
        if (galleryImagesList.size() > 0) {
            loadAndSaveImages();
        }

    }

    /* Load image from URL */
    public class MultiImageLoadTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogProgress = new ProgressDialogBar(SellCarActivity.this);
            dialogProgress.showProgressDialog();

        }

        @Override
        protected String doInBackground(Void... params) {

            loadAndSaveImages();

            for (int i = 0; i < galleryImagesList.size(); i++) {
                imageArray.add(galleryImagesList.get(i).getPath());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            adapter = new GridImagesAdapter(SellCarActivity.this);
            carImageGridVw.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            if (imageArray.size() > 0) {
                uploadImageBtn.setText("Upload more");
            }

            dialogProgress.dismissProgressDialog();

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

    public void uploadCarWithDetails(){

        carNameStr = carNameEt.getText().toString().trim();
        carModelStr = carModelEt.getText().toString().trim();
        carYearStr = carYearEt.getText().toString().trim();
        carPriceStr = priceEt.getText().toString().trim();

        if(imageArray.size() == 0){

            Toast.makeText(SellCarActivity.this,"Upload at least one image",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(carNameStr)){

            Toast.makeText(SellCarActivity.this,"Enter Car Name",Toast.LENGTH_SHORT).show();


        }else if(TextUtils.isEmpty(carModelStr)){

            Toast.makeText(SellCarActivity.this,"Enter Car Model",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(carYearStr)){

            Toast.makeText(SellCarActivity.this,"Enter Car Year",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(carPriceStr)){


            Toast.makeText(SellCarActivity.this,"Enter Car Price",Toast.LENGTH_SHORT).show();

        }else{


            new SellCarTask().execute();

        }
    }

    public void postSingleImage(){

        finalListOfImages  = imageArray ;

        if(finalListOfImages != null && finalListOfImages.size() > 0){

            imagePost(finalListOfImages.get(0));

        }

    }

    public void imagePost(String imagePost){
        String imageFileUrl = postImageToCloudinary(imagePost);
        if(i==0){
            carImageUrl = imageFileUrl;
            i++;
        }else {
            carImageUrl = carImageUrl + "," + imageFileUrl;
            i++;
        }

        Log.e("@@Car Image URL's",carImageUrl);
        listOfUrlImages.add(imageFileUrl);

        finalListOfImages.remove(0);

        if(finalListOfImages != null && finalListOfImages.size() > 0){

            imagePost(finalListOfImages.get(0));

        }

    }

    public class SellCarTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialogProgress = new ProgressDialogBar(SellCarActivity.this);
            dialogProgress.showProgressDialog();

        }

        @Override
        protected String doInBackground(Void... params) {

            postSingleImage();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            postCarDetailsService();

            dialogProgress.dismissProgressDialog();


        }

    }

    // post image to cloudinary
    private String postImageToCloudinary(String profilePicFileName) {


        if (profilePicFileName != null) {
            if (profilePicFileName.length() > 0) {

                try {

                    // Trail version URL
                    String couldinaryURL = "cloudinary://969892397415277:yX0OqpKoM8YjfyxoYs0Tmj5_jTU@dyrcjup4p";

                    Cloudinary cloudinary = new Cloudinary(couldinaryURL);

                    String path = ImageUtils.filePath(this, profilePicFileName);
                    Map uploadResult;
                    if (path.length() > 0) {
                        FileInputStream fis = new FileInputStream(path);
                        //uploadResult = cloudinary.uploader().upload(fis, ObjectUtils.emptyMap());

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        uploadResult = cloudinary.uploader().upload(fis,
                                ObjectUtils.asMap("public_id", "dev/" + timeStamp));

                    } else {
                        return "";
                    }

                    imageFileNameUrl = uploadResult.get("url").toString() ;

                    Log.e("imageFileNameUrl", imageFileNameUrl);

                    return imageFileNameUrl;


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

   public void postCarDetailsService(){

       Log.e("listOfUrlImages", String.valueOf(listOfUrlImages));

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {

                if(s.equals("true")){
                    Toast.makeText(SellCarActivity.this, "Uploaded Car Details Successful", Toast.LENGTH_LONG).show();
                    setResult(33);
                    finish();

                }
                else{
                    Toast.makeText(SellCarActivity.this, "Can't Uploaded", Toast.LENGTH_LONG).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SellCarActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("carImage",carImageUrl);
                parameters.put("carName",carNameStr);
                parameters.put("CarYear",carYearStr);
                parameters.put("CarModel",carModelStr);
                parameters.put("carPrice",carPriceStr);
                parameters.put("emailId",Preferences.emailID);

                Log.e("List of Parameters", String.valueOf(parameters));

                //listOfUrlImages
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

        RequestQueue rQueue = Volley.newRequestQueue(SellCarActivity.this);
        rQueue.add(request);
        }


    /*Initializing views*/
    public void viewsInitialization(){

        carImageGridVw = (GridView) findViewById(R.id.carImagesGridVw);

        carNameEt=(EditText) findViewById(R.id.carNameEt);
        carModelEt=(EditText) findViewById(R.id.carModelEt);
        carYearEt=(EditText) findViewById(R.id.carYearEt);
        priceEt=(EditText) findViewById(R.id.carPriceEt);

        carImageVw=(ImageView) findViewById(R.id.carImageVw);
        uploadImageBtn=(Button) findViewById(R.id.uploadBtn);
        submitBtn=(Button) findViewById(R.id.submitBtn);

    }



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

    /*inner class adapter for  grid images */
    public class GridImagesAdapter extends BaseAdapter {

        private Context context;

        class Widgets {

            public ImageView icon;

            public Widgets(View view) {

                icon = (ImageView) view.findViewById(R.id.carImageItemVw);

            }
        }

        public GridImagesAdapter(Context context) {
            this.context = context;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return imageArray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            Widgets widgets;
            if (v == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                v = inflater.inflate(R.layout.list_images_item, parent, false);
                widgets = new Widgets(v);
                v.setTag(widgets);
            } else {
                widgets = (Widgets) v.getTag();
            }

            Picasso.with(context)
                    .load("file://" + ImageUtils.loadFile(SellCarActivity.this, imageArray.get(position)))
                    .fit()
                    .into(widgets.icon);

            return v;
        }


        /*inner class adapter for  grid images */
        public class GridEditImagesAdapter extends BaseAdapter {

            private Context context;

            class Widgets {

                public ImageView icon;

                public Widgets(View view) {

                    icon = (ImageView) view.findViewById(R.id.carImageItemVw);

                }
            }

            public GridEditImagesAdapter(Context context) {
                this.context = context;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public int getCount() {
                return imageEditArray.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public View getView(int position, View v, ViewGroup parent) {
                SellCarActivity.GridImagesAdapter.Widgets widgets;
                if (v == null) {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    v = inflater.inflate(R.layout.list_images_item, parent, false);
                    widgets = new SellCarActivity.GridImagesAdapter.Widgets(v);
                    v.setTag(widgets);
                } else {
                    widgets = (SellCarActivity.GridImagesAdapter.Widgets) v.getTag();
                }

                Picasso.with(context)
                        .load(imageEditArray.get(position))
                        .fit()
                        .into(widgets.icon);

                return v;
            }
        }
    }
}
