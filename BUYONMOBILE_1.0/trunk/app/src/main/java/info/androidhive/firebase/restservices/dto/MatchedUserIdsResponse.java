package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 12/8/2017.
 */

public class MatchedUserIdsResponse {


    @SerializedName("userId")
    private int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
