
package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 10/15/2017.
 */

public class DeviceTokenResponse {

    /*[{"userDeviceId":5,"userId":13,"userName":"subbareddyiv12@gmail.com  ",
            "fcmTokenId":""}]*/

    @SerializedName("fcmTokenId")
    private String fcmTokenID;

    public String getFcmTokenID() {
        return fcmTokenID;
    }

    public void setFcmTokenID(String fcmTokenID) {
        this.fcmTokenID = fcmTokenID;
    }
}
