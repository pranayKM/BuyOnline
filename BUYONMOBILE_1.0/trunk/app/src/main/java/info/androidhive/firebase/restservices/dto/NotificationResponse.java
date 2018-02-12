package info.androidhive.firebase.restservices.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Subbareddy on 10/28/2017.
 */

public class NotificationResponse {

    /*{"notificationId":1,"userId":0,"userMsg":"Here is the Car that matches your preferences"}*/


    @SerializedName("notificationId")
    private int notificationID;

    @SerializedName("userMsg")
    private String notificationMsg;

    @SerializedName("userPreferedId")
    private int userPreId;

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

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("updatedDate")
    private String updatedDate;

    @SerializedName("name")
    private String name;

    @SerializedName("phoneNo")
    private String phoneNo;

    @SerializedName("emailId")
    private String emailId;

    @SerializedName("carImageURLs")
    private String carImageURLs;

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationMsg() {
        return notificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        this.notificationMsg = notificationMsg;
    }

    public int getUserPreId() {
        return userPreId;
    }

    public void setUserPreId(int userPreId) {
        this.userPreId = userPreId;
    }

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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

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

    public String getCarImageURLs() {
        return carImageURLs;
    }

    public void setCarImageURLs(String carImageURLs) {
        this.carImageURLs = carImageURLs;
    }
}
