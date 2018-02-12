package info.androidhive.firebase;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import info.androidhive.firebase.restservices.RestClient;

/**
 * Created by Pranay on 21/11/16.
 */

public class BOMApplication extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();

        Preferences.loadPreferences(getApplicationContext());

        restClient = new RestClient();

    }

    public static RestClient getRestClient() {
        return restClient;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
