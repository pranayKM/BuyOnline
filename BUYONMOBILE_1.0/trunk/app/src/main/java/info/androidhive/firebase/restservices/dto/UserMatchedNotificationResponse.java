package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 11/18/2017.
 */

public class UserMatchedNotificationResponse {

    /*{"notificationId":1,"userId":0,"userMsg":"Here is the Car that matches your preferences"}*/


    @SerializedName("userId")
    private int userID;

    @SerializedName("userMessage")
    private String userNotiticationMsg;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserNotiticationMsg() {
        return userNotiticationMsg;
    }

    public void setUserNotiticationMsg(String userNotiticationMsg) {
        this.userNotiticationMsg = userNotiticationMsg;
    }
}
