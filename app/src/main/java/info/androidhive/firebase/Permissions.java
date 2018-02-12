package info.androidhive.firebase;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranay on 21/11/16.
 */

public class Permissions {

    Activity context;
    public static final int PERMISSION_REQUEST_ID = 1;


    public Permissions(Activity context) {
        this.context = context;
    }

    public boolean checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // This condition checks whether user has earlier denied the permission or not just by clicking on deny in the permission dialog.
            //Remember not on the never ask check box then deny in the permission dialog
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.CAMERA)) {
                /**
                 * Show an explanation to user why this permission in needed by us. Here using alert dialog to show the permission use.
                 */
                new AlertDialog.Builder(context)//, R.style.TranslucentDialog
                        .setTitle("Permission Required")
                        .setMessage("This permission was denied earlier by you. This permission is required to access your camera to capture your details.So, in order to use this feature please allow this permission by clicking ok.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(context,
                                        new String[]{Manifest.permission.CAMERA},
                                        1);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else {
                // Just ask for the permission for first time. This block will come into play when user is trying to use feature which requires permission grant.
                //So for the first time user will be into this else block. So just ask for the permission you need by showing default permission dialog
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.CAMERA},1);
            }
        }else {
            // If permission is already granted by user then we will be into this else block. So do whatever is required here
//            Toast.makeText(context,"Permission Aleardy granted",Toast.LENGTH_LONG).show();

            return true;
        }

        return false;
    }

    public boolean checkStoragePermission(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // This condition checks whether user has earlier denied the permission or not just by clicking on deny in the permission dialog.
            //Remember not on the never ask check box then deny in the permission dialog
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                /**
                 * Show an explanation to user why this permission in needed by us. Here using alert dialog to show the permission use.
                 */
                new AlertDialog.Builder(context)//, R.style.TranslucentDialog
                        .setTitle("Permission Required")
                        .setMessage("This permission was denied earlier by you. This permission is required to access your storage to read/write your details.So, in order to use this feature please allow this permission by clicking ok.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(context,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        2);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            } else {
                // Just ask for the permission for first time. This block will come into play when user is trying to use feature which requires permission grant.
                //So for the first time user will be into this else block. So just ask for the permission you need by showing default permission dialog
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            }
        }else {
            // If permission is already granted by user then we will be into this else block. So do whatever is required here
//            Toast.makeText(context,"Permission Aleardy granted",Toast.LENGTH_LONG).show();

            return true;
        }

        return false;
    }

    public Boolean checkAllPermissions(){

        if (checkCameraPermission()){
            return checkStoragePermission();
        }else {
            return false;
        }

    }

    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_REQUEST_ID);
            return false;
        }
        return true;
    }

    public boolean checkLocationPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_ID);
            return false;
        }
        return true;
    }

    public boolean checkCallPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CALL_PHONE);
        int callPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (callPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),PERMISSION_REQUEST_ID);
            return false;
        }
        return true;
    }

}
