package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 11/8/2017.
 */

public class MatchedRecordResponse {


    @SerializedName("userId")
    private int userId;

    @SerializedName("carMake")
    private String carMake;

    @SerializedName("carYear")
    private String carYear;

    @SerializedName("carColor")
    private String carColor;

    @SerializedName("carPriceStart")
    private String carPriceStart;

    @SerializedName("carPriceEnd")
    private String carPriceEnd;

    @SerializedName("userEmailId")
    private String userEmailId;

    @SerializedName("sellerFirstName")
    private String sellerFirstName;

    @SerializedName("sellerLastName")
    private String sellerLastName;

    @SerializedName("sellerPhone")
    private String sellerPhone;

    @SerializedName("buyerFcmDeviceId")
    private String buyerFcmDeviceId;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarPriceStart() {
        return carPriceStart;
    }

    public void setCarPriceStart(String carPriceStart) {
        this.carPriceStart = carPriceStart;
    }

    public String getCarPriceEnd() {
        return carPriceEnd;
    }

    public void setCarPriceEnd(String carPriceEnd) {
        this.carPriceEnd = carPriceEnd;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getSellerFirstName() {
        return sellerFirstName;
    }

    public void setSellerFirstName(String sellerFirstName) {
        this.sellerFirstName = sellerFirstName;
    }

    public String getSellerLastName() {
        return sellerLastName;
    }

    public void setSellerLastName(String sellerLastName) {
        this.sellerLastName = sellerLastName;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getBuyerFcmDeviceId() {
        return buyerFcmDeviceId;
    }

    public void setBuyerFcmDeviceId(String buyerFcmDeviceId) {
        this.buyerFcmDeviceId = buyerFcmDeviceId;
    }
}
