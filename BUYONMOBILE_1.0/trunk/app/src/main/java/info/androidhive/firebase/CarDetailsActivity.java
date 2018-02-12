package info.androidhive.firebase;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.androidhive.firebase.restservices.dto.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Subbareddy on 7/23/2017.
 */

public class CarDetailsActivity extends AppCompatActivity {

    private TextView carNameTv, carModelTv,carYearTv,carPriceTv,nameTv,phoneNumTv,emailTv;

    private ImageView carImageVw;

    public String carNameStr="",carModelStr,carYearStr,carPriceStr,phoneStr,emailStr,name,imagesListStr, carImageURLs="";

    private int userId,userId1,userIdCall;

    private LinearLayout phoneLl,emailLl;

    private Toolbar toolbar;

    private GridView carImageGridVw;;

    private GridImagesAdapter adapter;

    public List<String> imageArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        userId = getIntent().getIntExtra("userId",0);
        carNameStr = getIntent().getStringExtra("carName");
        carModelStr = getIntent().getStringExtra("carModel");
        carYearStr = getIntent().getStringExtra("carYear");
        carPriceStr = getIntent().getStringExtra("carPrice");
        //name = getIntent().getStringExtra("userName");
        //phoneStr = getIntent().getStringExtra("mobile");
        imagesListStr = getIntent().getStringExtra("imagesListStr");
        //imageArray = getIntent().getStringArrayListExtra("carImagesList");
        viewsInitialization();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        if (!TextUtils.isEmpty(imagesListStr)) {
            imageArray = Arrays.asList(imagesListStr.split("\\s*,\\s*"));

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (InternetConnection.isNetworkAvailable(CarDetailsActivity.this)) {

            carDetailsService();

        } else {
            Toast.makeText(CarDetailsActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
        }

        /*Phone dialer*/
        phoneLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permissions permissions = new Permissions(CarDetailsActivity.this);

                if (permissions.checkCallPermissions()) {

                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneStr));

                    if (ActivityCompat.checkSelfPermission(CarDetailsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        });

         /*PEmailIntent*/
        emailLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailStr});

                //need this to prompts email client only
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

    }

    private void carDetailsService(){

        final Call<List<UserDetails>> usersList = BOMApplication.getRestClient().getApiService().getUserDetails();

        usersList.enqueue(new Callback<List<UserDetails>>() {
            @Override
            public void onResponse(Call<List<UserDetails>> responseCall, Response<List<UserDetails>> response) {

                if (response.code() == 200) {

                    List<UserDetails> responseList = response.body();

                    if (responseList.size() > 0 && responseList != null) {
                        userId1 = getIntent().getIntExtra("userId",0);

                        for(int i = 0;i < responseList.size(); i++){
                            userIdCall = responseList.get(i).getUserId();
                            if(userId1 == userIdCall){
                                name = responseList.get(i).getName();
                                phoneStr = responseList.get(i).getPhoneNo();
                                emailStr = responseList.get(i).getEmailId();

                                setCarDetails(name,phoneStr,emailStr);

                            }
                        }

                    }
                }
            }
            @Override
            public void onFailure(Call<List<UserDetails>> call, Throwable t) {

            }
        });


    }

    /*Get and set Car details*/
    public  void setCarDetails(String userNm,String phoneNum,String emailID){

        adapter = new GridImagesAdapter(this,imageArray);
        carImageGridVw.setAdapter(adapter);

        carNameTv.setText(carNameStr);
        carModelTv.setText(carModelStr);
        carPriceTv.setText(carPriceStr);
        carYearTv.setText(carYearStr);
        nameTv.setText(userNm);
        phoneNumTv.setText(phoneNum);
        emailTv.setText(emailID);

    }
    /*Initializing views*/
    public void viewsInitialization(){

        carImageGridVw = (GridView) findViewById(R.id.carImagesGridVw);
        carNameTv=(TextView) findViewById(R.id.carNmTv);
        carYearTv=(TextView) findViewById(R.id.carYearTv);
        carModelTv=(TextView) findViewById(R.id.carModelTv);
        carPriceTv=(TextView) findViewById(R.id.carPriceTv);
        nameTv=(TextView) findViewById(R.id.nameTv);
        phoneNumTv=(TextView) findViewById(R.id.phoneNumTv);
        emailTv=(TextView) findViewById(R.id.emailTv);

        carImageVw=(ImageView) findViewById(R.id.carImgVw);

        phoneLl=(LinearLayout) findViewById(R.id.phoneLl);
        emailLl=(LinearLayout) findViewById(R.id.emailLl);

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
        private List<String> imagesList = new ArrayList<>();

        class Widgets {

            public ImageView icon;

            public Widgets(View view) {

                icon = (ImageView) view.findViewById(R.id.carImageItemVw);

            }
        }

        public GridImagesAdapter(Context context, List<String> imagesList) {
            this.context = context;
            this.imagesList = imagesList;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return imagesList.size();
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

            Picasso.with(context).load(imagesList.get(position)).fit().into(widgets.icon);

            return v;
        }
    }
}
