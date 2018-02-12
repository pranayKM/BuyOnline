package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 9/23/2017.
 */

public class FCMNotificationsRequest {

    /*for single user*/
    @SerializedName("to")
    private String toTokenID;

    /*for multiple users*/
    @SerializedName("registration_ids")
    private String[] registeredUserDeviceIDsList;

    @SerializedName("data")
    private FCMData fcmData;

    @SerializedName("notification")
    private FCMNotification fcmNotification;

    @SerializedName("icon")
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String[] getRegisteredUserDeviceIDsList() {
        return registeredUserDeviceIDsList;
    }

    public void setRegisteredUserDeviceIDsList(String[] registeredUserDeviceIDsList) {
        this.registeredUserDeviceIDsList = registeredUserDeviceIDsList;
    }

    public String getToTokenID() {
        return toTokenID;
    }

    public void setToTokenID(String toTokenID) {
        this.toTokenID = toTokenID;
    }

    public FCMData getFcmData() {
        return fcmData;
    }

    public void setFcmData(FCMData fcmData) {
        this.fcmData = fcmData;
    }

    public FCMNotification getFcmNotification() {
        return fcmNotification;
    }

    public void setFcmNotification(FCMNotification fcmNotification) {
        this.fcmNotification = fcmNotification;
    }
}
