package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 5/14/2017.
 */

public class BuyerUserResponse {

       /* "userPreferedId":1,
                "userId":1,
                "carMake":"Wolkswagan",
                "carModel":"Polo",
                "carYear":"2015",
                "carColor":"white",
                "carPriceStart":"100000",
                "carPriceEnd":"200000",
                "createdDate":"May 13, 2017",
                "updatedDate":"May 13, 2017"*/


    @SerializedName("userId")
    private int userID;

    @SerializedName("carMake")
    private String carMake;

    @SerializedName("carModel")
    private String  carModel;

    @SerializedName("carYear")
    private String carYear;

    @SerializedName("carColor")
    private String carColor;

    @SerializedName("carPriceStart")
    private String carPriceStart;

    @SerializedName("carPriceEnd")
    private String carPriceEnd;

    @SerializedName("carImageUrls")
    private String carImageUrls;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("adress")
    private String adress;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
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

    public String getCarImageUrls() {
        return carImageUrls;
    }

    public void setCarImageUrls(String carImageUrls) {
        this.carImageUrls = carImageUrls;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
