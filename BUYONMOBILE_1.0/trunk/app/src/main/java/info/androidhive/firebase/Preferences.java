package info.androidhive.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*Created by subbareddy on 21/11/16.*/

public class Preferences {

    public static int userID = 0;

    public static String userName="";

    public static String emailID="";

    public static String mobile = "";

    public static boolean loginStatus = false;

    public static String userImageUrl = "";

    public static String name="";

    public static String emailStr="";

    public static String phoneStr="";

    public static String fcmDeviceTokenID="";

     public static void savePreferences(Context context) {

        SharedPreferences.Editor editor;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();

        editor.putInt("userID", userID);
        editor.putString("userName", userName);
        editor.putString("emailID", emailID);
        editor.putString("userImageUrl", userImageUrl);
        editor.putBoolean("loginStatus", loginStatus);
        editor.putString("mobile",mobile);
         editor.putString("name",name);
         editor.putString("emailStr",emailStr);
         editor.putString("phoneStr",phoneStr);
         editor.putString("fcmDeviceTokenID",fcmDeviceTokenID);

        editor.commit();
    }

    public static void loadPreferences(Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        userID = pref.getInt("userID",0);
        userName = pref.getString("userName", "");
        emailID = pref.getString("emailID", "");
        userImageUrl = pref.getString("userImageUrl", "");
        loginStatus = pref.getBoolean("loginStatus", false);
        mobile = pref.getString("mobile","");
        name = pref.getString("name","");
        phoneStr = pref.getString("phoneStr","");
        emailStr = pref.getString("emailStr", "");
        emailStr = pref.getString("fcmDeviceTokenID", "");
    }

    public static void clearPreferences(Context context){

        SharedPreferences.Editor editor;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        editor.clear();
        editor.commit();


    }
}
