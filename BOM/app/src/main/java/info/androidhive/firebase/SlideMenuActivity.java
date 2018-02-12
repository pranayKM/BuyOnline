package info.androidhive.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Subbareddy on 5/5/17.
 */

public class SlideMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private Fragment fragment = null;

    private ListView listView;

    private FragmentTransaction fragmentTransaction;

    private List<String> stringArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_menu);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        listView = (ListView) findViewById(R.id.menuLv);
        setSupportActionBar(toolbar);


        stringArrayList = new ArrayList<String>();
        stringArrayList.add("Buy a Car");
        stringArrayList.add("Sell a Car");
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
                } else if (position == 2) {
                    fragment = new DashBoardFragment();

                }else if(position == 3){
                    fragment = new ChangePasswordFrag();

                } else if (position == 4) {
                    Preferences.loginStatus = false;
                    Preferences.savePreferences(SlideMenuActivity.this);
                    Intent intent = new Intent(SlideMenuActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Preferences.clearPreferences(SlideMenuActivity.this);
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

}
