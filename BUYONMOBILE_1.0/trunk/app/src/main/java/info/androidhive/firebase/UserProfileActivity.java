package info.androidhive.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Subbareddy on 10/27/2017.
 */

public class UserProfileActivity extends AppCompatActivity{

    private TextView userProfileName, userProfilePhone,userProfileEmail;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        viewsInitialization();
        setProfileDetails();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("User Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




    }
    public void viewsInitialization(){

        userProfileName=(TextView) findViewById(R.id.userProfileNameTv);
        userProfilePhone=(TextView) findViewById(R.id.userProfilePhoneNumTv);
        userProfileEmail=(TextView) findViewById(R.id.userProfileEmailTv);

    }
    public  void setProfileDetails(){

        //Log.e("--Set Card name",name);
        Preferences.loadPreferences(UserProfileActivity.this);
        userProfileName.setText(Preferences.userName);
        userProfilePhone.setText(Preferences.mobile);
        userProfileEmail.setText(Preferences.emailID);


    }
}
