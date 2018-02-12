package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 9/23/2017.
 */

public class FCMNotification {

      /*  "body":" msg",
            "title":"title"*/

    @SerializedName("body")
    private String fcmBody;

    @SerializedName("title")
    private String fcmTitle;

    public String getFcmBody() {
        return fcmBody;
    }

    public void setFcmBody(String fcmBody) {
        this.fcmBody = fcmBody;
    }

    public String getFcmTitle() {
        return fcmTitle;
    }

    public void setFcmTitle(String fcmTitle) {
        this.fcmTitle = fcmTitle;
    }
}
