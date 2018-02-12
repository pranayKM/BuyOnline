package info.androidhive.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by subbareddy on 21/11/16.
 */

public class Preferences {

    public static String artistName = "";  // Artist name / Organiser Name

    public static String artistEmail = "";

    public static String userType = "";

    public static String organisationName = "";

    public static String artStyle = "";

    public static String artCategory = "";

    public static String specialization = "";

    public static boolean loginStatus = false;

    public static String mongoId;

    public static String etagId;

    public static int returnedValue;

    public static String profileImageUrl;

    public static String faceBookID = "";

    public static String locationName="";

    public static String rating="";

    public static String latitude="";

    public static String longitude="";

    public static String currentPassword="";

    public static int connetionsCount;

    public static boolean isFlexible = false ;

    public static String fcmDeviceTokenID = "" ;
    public static String fcmServerKey = "" ;


    public static void savePreferences(Context context) {

        SharedPreferences.Editor editor;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();

        editor.putString("artistName", artistName);
        editor.putString("userType", userType);
        editor.putString("organisationName", organisationName);
        editor.putString("artistEmail", artistEmail);
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.putString("currentPassword", currentPassword);
        editor.putBoolean("loginStatus", loginStatus);
        editor.putBoolean("isFlexible", isFlexible);
        editor.putString("mongoId", mongoId);
        editor.putString("etagId", etagId);
        editor.putString("profileImageUrl", profileImageUrl);
        editor.putString("locationName", locationName);
        editor.putInt("returnedValue", returnedValue);
        editor.putInt("connectionsCount", connetionsCount);
        editor.putString("rating", rating);
        editor.putString("artStyle", artStyle);
        editor.putString("artCategory", artCategory);
        editor.putString("specialization", specialization);
        editor.putString("faceBookID", faceBookID);
        editor.putString("fcmDeviceTokenID", fcmDeviceTokenID);
        editor.putString("fcmServerKey", fcmServerKey);

        editor.commit();
    }

    public static void loadPreferences(Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        artistName = pref.getString("artistName", "");
        userType = pref.getString("userType", "");
        organisationName = pref.getString("organisationName", "");
        artistEmail = pref.getString("artistEmail", "");
        latitude = pref.getString("latitude", "");
        longitude = pref.getString("longitude", "");
        currentPassword = pref.getString("currentPassword", "");
        loginStatus = pref.getBoolean("loginStatus", false);
        isFlexible = pref.getBoolean("isFlexible", false);
        mongoId = pref.getString("mongoId", "");
        etagId = pref.getString("etagId", "");
        profileImageUrl = pref.getString("profileImageUrl", "");
        locationName = pref.getString("locationName", "");
        returnedValue = pref.getInt("returnedValue", 1);
        rating=pref.getString("rating",rating);
        artStyle=pref.getString("artStyle",artStyle);
        faceBookID=pref.getString("faceBookID",faceBookID);
        artCategory=pref.getString("artCategory",artCategory);
        specialization=pref.getString("specialization",specialization);
        connetionsCount=pref.getInt("connectionsCount",connetionsCount);
        fcmDeviceTokenID=pref.getString("fcmDeviceTokenID",fcmDeviceTokenID);
        fcmServerKey=pref.getString("fcmServerKey",fcmServerKey);

    }

    public static void clearPreferences(Context context){

        SharedPreferences.Editor editor;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        editor.clear();
        editor.commit();


    }
}
