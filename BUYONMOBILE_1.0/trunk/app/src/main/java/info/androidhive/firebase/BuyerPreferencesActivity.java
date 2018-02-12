package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Subbareddy on 09/20/2017.
 */

public class BuyerPreferencesActivity extends AppCompatActivity {


    private EditText inputCarName, inputCarModel,inputCarYear,inputCarColor, inputCarPriceStart,inputCarPriceEnd;
    private Button btnSearch, btnSavePref;
    private ProgressBar progressBar;
    private Permissions permissions;
    private RecyclerView userPrefRv;

    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Buyer/savePreferences";

    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/Buyer/savePreferences";

    public List<String> imageArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_preferences);

        //Get Firebase auth instance
        //auth = FirebaseAuth.getInstance();

        btnSearch = (Button) findViewById(R.id.csearchBtn);
        btnSavePref = (Button) findViewById(R.id.cpreferenceBtn);
        inputCarName = (EditText) findViewById(R.id.cmake);
        inputCarModel = (EditText) findViewById(R.id.cmodel);
        inputCarYear = (EditText) findViewById(R.id.cyear);
        inputCarColor = (EditText) findViewById(R.id.ccolor);
        inputCarPriceStart = (EditText) findViewById(R.id.cpriceStart);
        inputCarPriceEnd = (EditText) findViewById(R.id.cpriceEnd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        permissions=new Permissions(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BuyerPreferencesActivity.this, ResetPasswordActivity.class));
            }
        });

        /*On click listener for  edit text location*/


        btnSavePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(inputCarName.getText().toString().trim())){

                    Toast.makeText(BuyerPreferencesActivity.this,"Enter Car Make", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(inputCarModel.getText().toString().trim())){

                    Toast.makeText(BuyerPreferencesActivity.this,"Enter Car Model", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(inputCarYear.getText().toString().trim())){

                    Toast.makeText(BuyerPreferencesActivity.this,"Enter Car Year", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(inputCarColor.getText().toString().trim())){

                    Toast.makeText(BuyerPreferencesActivity.this,"Enter Car Color", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(inputCarPriceStart.getText().toString().trim())){

                    Toast.makeText(BuyerPreferencesActivity.this,"Enter Car Price Start", Toast.LENGTH_SHORT).show();

                }else if(TextUtils.isEmpty(inputCarPriceEnd.getText().toString().trim())){

                    Toast.makeText(BuyerPreferencesActivity.this,"Enter Car Price End", Toast.LENGTH_SHORT).show();
                }else {

                    if (InternetConnection.isNetworkAvailable(BuyerPreferencesActivity.this)) {

                        saveBuyerPreferences();

                    } else {
                        Toast.makeText(BuyerPreferencesActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
                    }
                }



            }});


    }

    /*send push notifications on creating event*/
    public void saveBuyerPreferences() {

          //Toast.makeText(getApplicationContext(), "Called", Toast.LENGTH_SHORT).show();
                //String name = inputName.getText().toString().trim();
                //String phone = inputPhone.getText().toString().trim();
                String carName = inputCarName.getText().toString().trim();
                String carModel = inputCarModel.getText().toString().trim();
                String carColor = inputCarColor.getText().toString().trim();
                String carYear = inputCarYear.getText().toString().trim();
                String carPriceStart = inputCarPriceStart.getText().toString().trim();
                String carPriceEnd = inputCarPriceEnd.toString().trim();

                //Toast.makeText(getApplicationContext(),firstName,Toast.LENGTH_SHORT).show();
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String s) {

                            Log.e("Response",s);
                            Intent intent = new Intent();
                            intent.putExtra("ResponseStr",s);
                            setResult(44,intent);
                            finish();

                           /* Toast.makeText(BuyerPreferencesActivity.this, "Saved Your Preferences Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(new Intent(BuyerPreferencesActivity.this,SlideMenuActivity.class));
                            startActivity(intent);*/
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(BuyerPreferencesActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("carName",inputCarName.getText().toString().trim());
                        parameters.put("carModel",inputCarModel.getText().toString().trim());
                        parameters.put("carColor",inputCarColor.getText().toString().trim());
                        parameters.put("carYear",inputCarYear.getText().toString().trim());
                        parameters.put("carPriceStart",inputCarPriceStart.getText().toString().trim());
                        parameters.put("carPriceEnd",inputCarPriceEnd.getText().toString().trim());
                        parameters.put("emailId",Preferences.emailID);
                        Log.e("Preferences Emai",Preferences.emailID);

//                        parameters.put("email", emailBox.getText().toString());
//                        parameters.put("password", passwordBox.getText().toString());
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

                RequestQueue rQueue = Volley.newRequestQueue(BuyerPreferencesActivity.this);
                rQueue.add(request);

    }

}
