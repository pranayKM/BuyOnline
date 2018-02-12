package info.androidhive.firebase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import info.androidhive.firebase.restservices.dto.UserProfileImageResponse;

/**
 * Created by Subbareddy on 5/5/17.
 */

public class SlideMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private Fragment fragment = null;

    private Activity activity = null;

    private ListView listView;

    private FragmentTransaction fragmentTransaction;

    private List<String> stringArrayList;

    private TextView tvEmail, tvUserName;

    private ImageView ivProfile;

    private String emailId1,userFirstName,userLastName,profileImageUrl;

    private List<UserProfileImageResponse> userProfileImageResponseList= new ArrayList<>();

    private ArrayList<UserProfileImageResponse> userProfileImageList= new ArrayList<>();

    private List<UserProfileImageResponse> usersList;

    private String[] response = new String[3];

    private int i = 0;

    //String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/Registration/register";

    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/UserProfile/ImageUrl";


    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/UserProfile/ImageUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);

        // Initializing Toolbar and setting it as the actionbar
        userProfileImageDetails();
        tvUserName=(TextView) findViewById(R.id.profileNmTv);
        tvEmail=(TextView) findViewById(R.id.profileEmailTv);
        ivProfile=(ImageView) findViewById(R.id.profileLoginImgVw);


        Preferences.loadPreferences(SlideMenuActivity.this);
       // tvUserName.setText(Preferences.userName);
        tvEmail.setText(Preferences.emailID);
        //tvUserName.setText(Preferences.name);
        Log.e("@@@ User image URL",Preferences.userImageUrl);
        Log.e("@@@User", String.valueOf(0));
        //Log.e("Text",response[2]);
        /*if (!TextUtils.isEmpty(response[2])) {
            //Log.e("@@@ User image URL",Preferences.userImageUrl);
            //Log.e("@@@User", String.valueOf(0));
            Picasso.with(SlideMenuActivity.this).load(response[2]).placeholder(R.mipmap.ic_launcher).fit().into(ivProfile);
            Log.e("### Image URL",response[2]);
        }*/


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        listView = (ListView) findViewById(R.id.menuLv);
        setSupportActionBar(toolbar);


        stringArrayList = new ArrayList<String>();
        stringArrayList.add("Buy a Car");
        stringArrayList.add("Sell a Car");
        stringArrayList.add("Edit/See Your Preferences");
        stringArrayList.add("My Profile");
        stringArrayList.add("Change Password");
        stringArrayList.add("Log Out");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Initializing Drawer Layout and ActionBarToggle

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.single_text_item, stringArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                if (position == 0) {
                    fragment = new BuyerFragment();
                } else if (position == 1) {
                    fragment = new SellerFragment();
                } else if (position == 3) {
                    Intent intent = new Intent(new Intent(SlideMenuActivity.this,UserProfileActivity.class));
                    startActivity(intent);


                }else if(position == 4){
                    fragment = new ChangePasswordFrag();

                } else if (position == 5) {
                    Preferences.loginStatus = false;
                    Intent intent = new Intent(SlideMenuActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Preferences.clearPreferences(SlideMenuActivity.this);
                    Preferences.savePreferences(SlideMenuActivity.this);
                    finish();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                }

                if (fragment != null) {

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    // getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    //fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                //Closing drawer on item click
                drawerLayout.closeDrawers();
            }
        });

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new BuyerFragment();
        fragmentTransaction.replace(R.id.frame, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (fragment instanceof BuyerFragment) {

            ((BuyerFragment) fragment).onActivityResult(requestCode, resultCode, data);

        } else if (fragment instanceof SellerFragment) {

            ((SellerFragment) fragment).onActivityResult(requestCode, resultCode, data);

        } else if (fragment instanceof ChangePasswordFrag) {

            ((ChangePasswordFrag) fragment).onActivityResult(requestCode, resultCode, data);

        } else if (fragment instanceof DashBoardFragment) {

            ((DashBoardFragment) fragment).onActivityResult(requestCode, resultCode, data);

        }
    }

    /*send push notifications on creating event*/
    public void userProfileImageDetails() {


        emailId1 = Preferences.emailID;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

            @Override
            public void onResponse(String s) {

                Log.e("@@String Response",s);


                StringTokenizer st = new StringTokenizer(s);
                while (st.hasMoreTokens()) {
                    response[i] = st.nextToken(",");
                    //Log.e("##Token",st.nextToken(","));
                    i++;
                }


                if (!TextUtils.isEmpty(response[2])) {
                    //Log.e("@@@ User image URL",Preferences.userImageUrl);
                    //Log.e("@@@User", String.valueOf(0));
                    Picasso.with(SlideMenuActivity.this).load(response[2]).placeholder(R.mipmap.ic_launcher).fit().into(ivProfile);
                    Log.e("### Image URL",response[2]);
                }

                tvUserName.setText(response[0]);
               /* userProfileImageList = new Gson().fromJson(s, new TypeToken<List<UserProfileImageResponse>>(){}.getType());
                Log.e("### Profile",userProfileImageList.toString());
                usersList.addAll(userProfileImageList);

                Log.e("###Converted List", String.valueOf(usersList));*/


            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SlideMenuActivity.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("emailId",emailId1);

                Log.e("Preferences Emai",emailId1);
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

        RequestQueue rQueue = Volley.newRequestQueue(SlideMenuActivity.this);
        rQueue.add(request);

    }

}
