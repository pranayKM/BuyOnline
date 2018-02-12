package info.androidhive.firebase;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by pranay on 7/23/2017.
 */

public class CarDetailsActivity extends AppCompatActivity {

    private TextView carNameTv, carModelTv,carYearTv,carPriceTv,nameTv,phoneNumTv,emailTv;

    private ImageView carImageVw;

    private String carNameStr="",carModelStr,carYearStr,carPriceStr,phoneStr,emailStr,name;

    private LinearLayout phoneLl,emailLl;

    private Toolbar toolbar;

    //private int nameID=0;

    private String carImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        carNameStr = getIntent().getStringExtra("carName");
        carModelStr = getIntent().getStringExtra("carModel");
        carYearStr = getIntent().getStringExtra("carYear");
        carPriceStr = getIntent().getStringExtra("carPrice");
        name = getIntent().getStringExtra("name");
        phoneStr = getIntent().getStringExtra("phone");
        emailStr = getIntent().getStringExtra("email");
        //carImageUrl = getIntent().getStringExtra("carImage");

        viewsInitialization();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setCarDetails();

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

    /*Get and set Car details*/
    public  void setCarDetails(){

        carNameTv.setText(carNameStr);
        carModelTv.setText(carModelStr);
        carPriceTv.setText(carPriceStr);
        carYearTv.setText(carYearStr);
        nameTv.setText(name);
        phoneNumTv.setText(phoneStr);
        emailTv.setText(emailStr);

      /*  if (!TextUtils.isEmpty(carImageUrl)) {

            Picasso.with(CarDetailsActivity.this)
                    .load(carImageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(carImageVw);

        }*/
    }

    /*Initializing views*/
    public void viewsInitialization(){

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

}
