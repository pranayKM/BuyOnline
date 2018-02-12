package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 7/25/2017.
 */

public class UserDetails {

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNo")
    private String phoneNo;

    @SerializedName("emailId")
    private String emailId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
