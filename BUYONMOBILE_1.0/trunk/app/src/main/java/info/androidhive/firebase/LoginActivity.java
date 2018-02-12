package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.firebase.restservices.dto.UserDetails;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    EditText emailBox, passwordBox;
    private ProgressBar progressBar;
    private Button loginButton,btnSignup, btnLogin, btnReset;
    private String name;
    private int userId;
    private String deviceId = "";
    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/Login/login";
    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Login/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailBox = (EditText)findViewById(R.id.email);
        passwordBox = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.btn_login);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        final Call<List<UserDetails>> usersList = BOMApplication.getRestClient().getApiService().getUserDetails();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("true")){
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            deviceId = Preferences.fcmDeviceTokenID;
                            Log.e("Login Method Device Id",deviceId);
                            Intent intent = new Intent(new Intent(LoginActivity.this,SlideMenuActivity.class));
                            startActivity(intent);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);


                            usersList.enqueue(new Callback<List<UserDetails>>() {
                                @Override
                                public void onResponse(Call<List<UserDetails>> responseCall, retrofit2.Response<List<UserDetails>> response) {


                                    if (response.code() == 200) {


                                        List<UserDetails> responseList1 = response.body();
                                        String phone;


                                        String email = Preferences.emailID.trim();

                                        Log.e("@@ Email Id",email);

                                        Log.e("@@ Response List Size", String.valueOf(responseList1.size()));



                                        for(int i = 0; i < responseList1.size(); i++){
                                            Log.e("@@ New ==>",responseList1.get(i).getEmailId());
                                            if((responseList1.get(i).getEmailId().trim()).equals(email)){

                                                Log.e("@@ Entered","");

                                                phone = responseList1.get(i).getPhoneNo();
                                                name = responseList1.get(i).getName();
                                                userId = responseList1.get(i).getUserId();
                                                Log.e("@@@ User Id", String.valueOf(userId));
                                                Log.e("@@@Mobile",phone);
                                                Log.e("@@ Name", name);
                                                Preferences.userName = name;
                                                Preferences.mobile = phone;
                                                Preferences.userID = userId;
                                                Preferences.loginStatus = true;
                                                Preferences.savePreferences(LoginActivity.this);

                                            }
                                        }

                                       } else {
                                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                    }
                                }



                                @Override
                                public void onFailure(Call<List<UserDetails>> call, Throwable t) {


                                    Toast.makeText(LoginActivity.this,  "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Preferences.loadPreferences(LoginActivity.this);
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("email", emailBox.getText().toString());
                            parameters.put("password", passwordBox.getText().toString());
                            parameters.put("fcmTokenId", Preferences.fcmDeviceTokenID);
                            Preferences.emailID = emailBox.getText().toString();
                            //Preferences.userName = name;
                           // Preferences.userID = emailBox.getText().toString();
                            Preferences.savePreferences(LoginActivity.this);

                            return parameters;
                        }
                };

                RequestQueue rQueue = Volley.newRequestQueue(LoginActivity.this);
                rQueue.add(request);
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

    }


    /*private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        // set the view now
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                  *//*  Preferences.artistName = name;
                                    Preferences.artistEmail = emailId;
                                    Preferences.locationName = locationName;
                                    Preferences.latitude = latitude;
                                    Preferences.longitude = longitude;
                                    Preferences.currentPassword = currentPassword;
                                    Preferences.connetionsCount = cnnctnsCount;
                                    if (profileImageUrl != null) {
                                        Preferences.profileImageUrl = profileImageUrl;*//*
                                    }
                                   // Preferences.savePreferences(LoginActivity.this);
                               // Toast.makeText(getApplicationContext(), R.string.login_successfully, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SlideMenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                                Preferences.loginStatus = true;
                                Preferences.savePreferences(LoginActivity.this);
                            }
                        });
            }
        });
    }*/
}

