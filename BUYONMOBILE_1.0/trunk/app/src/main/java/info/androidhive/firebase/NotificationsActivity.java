package info.androidhive.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.firebase.adapters.NotificationsAdapter;
import info.androidhive.firebase.restservices.dto.UserMatchedNotificationResponse;

/**
 * Created by Subbareddy on 10/28/2017.
 */

public class NotificationsActivity extends AppCompatActivity{

    private RecyclerView notificationsRv;
    private TextView emptyTv;
    private List<UserMatchedNotificationResponse> matchedList;
    private ArrayList<UserMatchedNotificationResponse> matchedNotificationsList= new ArrayList<>();
    private List<Integer> deviceIdsList;


    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Matched/UserNotifications";

    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/Notifications/MatchedNotifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Notifications");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        emptyTv = (TextView) findViewById(R.id.emptyTv);
        notificationsRv = (RecyclerView) findViewById(R.id.notificationsRv);

        LinearLayoutManager mLayoutManager= new LinearLayoutManager(this);
        notificationsRv.setLayoutManager(mLayoutManager);
        notificationsRv.setHasFixedSize(true);

        if (InternetConnection.isNetworkAvailable(this)) {
            //getMachedSellersDetails();
            getNotificationsList();

        } else {
            Toast.makeText(NotificationsActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
        }


    }

    /* get Notifications list */
    /*public void getNotificationsList() {

        final ProgressDialogBar dialogProgress = new ProgressDialogBar(NotificationsActivity.this);
        dialogProgress.showProgressDialog();

        final Call<List<NotificationResponse>> notificationsList = BOMApplication.getRestClient().getApiService().getNotifications();

        notificationsList.enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> responseCall, Response<List<NotificationResponse>> response) {

                dialogProgress.dismissProgressDialog();

                if (response.code() == 200) {

                    List<NotificationResponse> responseList = response.body();

                    if (responseList.size() > 0 && responseList != null) {
                        Log.e("Notifications", String.valueOf(responseList.size()));
                             NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this, responseList);
                                notificationsRv.setAdapter(adapter);

                                notificationsRv.setVisibility(View.VISIBLE);
                                emptyTv.setVisibility(View.GONE);

                    }else {

                        notificationsRv.setVisibility(View.GONE);
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(NotificationsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {

                dialogProgress.dismissProgressDialog();
                Toast.makeText(NotificationsActivity.this,  "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

   /* public void saveEventNotifications(){

        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                if(s.equals("true")){
                    Toast.makeText(NotificationsActivity.this, "Your Notifications Saved Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(new Intent(NotificationsActivity.this,SlideMenuActivity.class));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(NotificationsActivity.this, "Can't Saved Your Notification", Toast.LENGTH_LONG).show();
                }
            }
        },new com.android.volley.Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(NotificationsActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("emailId",Preferences.emailID);
                Log.e("Preferences Emai",Preferences.emailID);
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

        RequestQueue rQueue = Volley.newRequestQueue(NotificationsActivity.this);
        rQueue.add(request);
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

    @Override
    public void onBackPressed() {
        // finish() is called in super: we only override this method to be able to override the transition
        super.onBackPressed();

        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public void getNotificationsList(){
        String emailId = Preferences.emailID;
        Log.e("###Notification Email",emailId);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>(){

            @Override
            public void onResponse(String s) {
                Log.e("!!!Response", s);

                if (!s.equals("No Records found")) {
                    matchedNotificationsList = new Gson().fromJson(s, new TypeToken<List<UserMatchedNotificationResponse>>() {
                    }.getType());
                    Log.e("@@@Size", String.valueOf(matchedNotificationsList.size()));

                    //Log.e("### Matched Records", matchedNotificationsList.get(0).getUserNotiticationMsg());

                    if (matchedNotificationsList.size() > 0 && matchedNotificationsList != null) {
                        Log.e("Notifications", String.valueOf(matchedNotificationsList.size()));
                        NotificationsAdapter adapter = new NotificationsAdapter(NotificationsActivity.this, matchedNotificationsList);
                        notificationsRv.setAdapter(adapter);

                        notificationsRv.setVisibility(View.VISIBLE);
                        emptyTv.setVisibility(View.GONE);

                    }else {

                        notificationsRv.setVisibility(View.GONE);
                        emptyTv.setVisibility(View.VISIBLE);
                    }
                }
            }
            },new com.android.volley.Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(NotificationsActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("emailId",Preferences.emailID);


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

        RequestQueue rQueue = Volley.newRequestQueue(NotificationsActivity.this);
        rQueue.add(request);

    }


}
