package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 9/23/2017.
 */

public class FCMData {

//    "message":"msg"

    @SerializedName("message")
    private String fcmMsg;

    public String getFcmMsg() {
        return fcmMsg;
    }

    public void setFcmMsg(String fcmMsg) {
        this.fcmMsg = fcmMsg;
    }
}
