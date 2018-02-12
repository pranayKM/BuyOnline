package info.androidhive.firebase.restservices.service;

import java.util.List;

import info.androidhive.firebase.restservices.dto.DeviceTokenResponse;
import info.androidhive.firebase.restservices.dto.FCMNotificationsRequest;
import info.androidhive.firebase.restservices.dto.NotificationResponse;
import info.androidhive.firebase.restservices.dto.UserDetails;
import info.androidhive.firebase.restservices.dto.UserPreferencesResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Subbareddy on 21/11/16.
 */

public interface BOMServices {

    /*Login with existing credentials*/
    @GET("USER_MASTER")
    Call<Void> loginWithExistingDetails(@Query("filter") String loginDetails);

    /*Displaying Car Preferences*/
    @GET("Preferences/GetUserPrefeces")
    Call<List<UserPreferencesResponse>> getUserPreferences();
   // Call<Void> getUserPreferences(@Query("radius") String loginDetails);

    /*Displaying Registerd Users Data*/
    @GET("RegistedUser/GetRegisteredUserDetails")
    Call<List<UserDetails>> getUserDetails();

    /*post push notifications*/
    @POST("fcm/send")
    Call<Void> sendFCMPushNotifications(@Body FCMNotificationsRequest notificationsRequest);


    /*GEt device token notifications*/
    @GET("FCMDevice/GetDeviceDetails")
    Call<List<DeviceTokenResponse>> getDeviceTokeDetails();

    /*GEt device token notifications*/
   // http://10.0.2.2:8080/BOMApplication/REST/Notification/GetUserNotifications
    @GET("Notification/GetUserNotifications")
    Call<List<NotificationResponse>> getNotifications();

    /*GEt device token notifications*//*
   //"http://10.0.2.2:8080/BOMApplication/REST/Buyer/savePreferences
    @POST("Buyer/savePreferences")
    Call<List<UserPreferencesResponse>> saveBuyerPreferences(@Body BuyerPreferencesRequest preferencesRequest);*/

   /* *//*Displaying Registerd Users Data*//*
    @GET("SlideMenu/details")
    Call<List<SlideMenuUserDetails>> getUserDetails();*/


//    /*  posting user details for sign up service*/
//    @POST("UserProfile")
//    Call<Void>  postUserDetailsForSignUp(@Body SignUpRequest signUpRequest);
//
//    /*Checking Email Id if Exists*/
//    /*  filter={'Email':'ramya@gmail.com','Password':'ramya',,'IsActive':'1'}*/
//    @GET("UserProfile")
//    Call<LoginDto> checkingEmailIdForSignUp(@Query("filter") String loginDetails);
//
//
//    /*get connections list*/
//    @GET("UserProfile")
//    Call<LoginDto> getConnectionsList(@Query("filter") String connectionStr);
//
//    /* get my connections with key valuse from user profile*/
//    @GET("UserProfile")
//    Call<LoginDto> getConnectionsListInProfile(@Query("filter") String myConnections, @Query("keys") String connectionsKeyStr);
//
//    /*get user profile details*/
//    @GET("UserProfile/{mongo_id}")
//    Call<RHDocList> getProfileDetailsByMongoId(@Path("mongo_id") String mongoId);
//
//    /* get user profile description service*/
///*  filter={"UserId":"5719cd6a84ae1d67226c147c",'IsActive':'1'}*/
//    @GET("UserProfile")
//    Call<LoginDto> getUserProfileDescription(@Query("filter") String userDescription, @Query("keys") String biographyNotesStr);
//
//    /*  get collaborations*/
//    @GET("UserProfile")
//    Call<LoginDto> getCollaborations(@Query("filter") String collaborations, @Query("keys") String collabKeyStr);
//
//    /* get user skills service*/
//    @GET("Skills")
//    Call<LoginDto> getUserAddedSkills(@Query("filter") String userSkills);
//
//    /* get Inbox messages  service*/
//   /* filter={'UserId':'5719cd6a84ae1d67226c147c','IsActive':'1'}*/
//    @GET("Inbox")
//    Call<LoginDto> getInboxMessagesList(@Query("filter") String inboxList);
//
//    /* upadte user profile description service*/
//    @PATCH("UserProfile/{mongo_id}")
//    Call<Void> postUserDescription(@Path("mongo_id") String mongo_id, @Body MyInterestsRequest biographyNotes);
//
//    /* get user skills service*/
//    @GET("Skills")
//    Call<LoginDto> getMySkills(@Query("filter") String userSkills);
//
//    /*delete added skills*/
//    @DELETE("Skills/{skill_mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<Void> deleteSkill(@Header("if-match") String header, @Path("skill_mongo_id") String skillId);
//
//    /* post collaborations*/
//   /* {UserId:'5719cd6a84ae1d67226c147c',UserName:'Ramya',CollaborationDescription:'Collaborated with XYZ and done an event',Images:'http://res.cloudinary.com/hgpakagpi/image/upload/v1460279438/gmlpurjis8jehem62eun.jpg,http://res.cloudinary.com/hgpakagpi/image/upload/v1460027219/iqs8wosvgvckzywvz9ds.png,http://res.cloudinary.com/hgpakagpi/image/upload/v1460010738/ou7qjn3qhoe8ni9wpdqg.jpg',CreatedBy:'Ramya',CreatedDate:'22-04-2016',ModifiedBy:'Ramya',ModifiedDate:'22-04-2016',IsActive:'1'}*/
//    @PATCH("UserProfile/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<Void> postCollaborations(@Path("mongo_id") String mongoId, @Body String collaborations);
//
//    /*post user skills service*/
//    @POST("Skills")
//    Call<Void> postUserSkills(@Body PostSkills addSkillsRequest);
//
//    /*Search connections by filter*/
//    @GET("UserProfile")
//    Call<LoginDto> searchConnectionsByFilter(@Query("filter") String searchStr);
//
//    /*Get Connection Requests*/
//    @GET("UserRequests")
//    Call<LoginDto> getConnectionRequests(@Query("filter") String connectionRequests);
//
//    @POST("UserRequests")
//    Call<Void> connectNowWithUser(@Body ConnectNowRequest connectNowRequest);
//
//    @PATCH("Skills/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<String> endorseUserSkills(@Path("mongo_id") String mongoId, @Body String endorseUserSkillStr);
//
//
//    @PATCH("UserProfile/{mongoId}")
//    @Headers("Content-type: application/json")
//    Call<String> postToYourConnections(@Path("mongoId") String mongoId, @Body String toMonogoId);
//
//    @PATCH("UserRequests/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<String> acceptRequest(@Path("mongo_id") String mongoId, @Body String acceptRequestStr);
//
////    @POST("StatusFeed")
////    Call<Void> postStatusFeed(@Body StatusFeedRequest feedRequest);
//
//    /*Update profile image service*/
//    @PATCH("UserProfile/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<Void> updateProfileImage(@Path("mongo_id") String mongoId, @Body MyInterestsRequest updateImage);
//
//    /*Change password serice*/
//    @PATCH("UserProfile/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<String> changePassword(@Path("mongo_id") String mongoId, @Body String newPasswordStr);
//
//    /*update address serice*/
//    @PATCH("UserProfile/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<String> updateAddress(@Path("mongo_id") String mongoId, @Body String addressStr);
//
//    /*get user profile interests*/
//    @GET("UserProfile/{mongo_id}")
//    Call<RHDocList> getUserProfileInterests(@Path("mongo_id") String mongoId);
//
//    /* update profile Interests service*/
//    @PATCH("UserProfile/{mongo_id}")
//    Call<Void> updateUserProfileInterests(@Path("mongo_id") String mongoId,
//                                          @Body MyInterestsRequest updateInterestsRequest);
//
//    @GET("UserProfile")
//    Call<LoginDto> getEndorsersList(@Query("filter") String endorserStr);
//
//    @PATCH("StatusFeed/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<String> likeFeedStatus(@Path("mongo_id") String mongoId, @Body String likesStr);
//
//    /*create evnt*/
//    @POST("Event")
//    Call<Void> createEvent(@Body EventRequest eventRequest);
//
//    /*Get my events list*/
//    @GET("Event")
//    Call<LoginDto> getMyEventsList(@Query("filter") String myEventsStr);
//
//    /*Get public events list*/
//    @GET("Event")
//    Call<LoginDto> getPublicEventsList(@Query("filter") String publicEventsStr);
//
//    /*Get Guest public events list*/
//    @GET("Event")
//    Call<LoginDto> getGuestEventsList();
//
//    /*Get event details by mongoId*/
//    @GET("Event/{mongoId}")
//    Call<RHDocList> getEventDetailsByMongoId(@Path("mongoId") String mongoId);
//
//    @PATCH("Event/{mongoId}")
//    @Headers("Content-type: application/json")
//    Call<String> expressInterestEvent(@Path("mongoId") String mongoId, @Body String interestMongoId);
//
//    /*Post Opportunity*/
//    @POST("Opportunity")
//    Call<Void> postOpportunity(@Body PostOpportunityRequest postOpportunity);
//
//    /*  Get  opportunities from my connections */
//    @GET("Opportunity")
//    Call<MyConnectionEmbedded> getOpprtFromMyConnections(@Query("filter") String opprtFrmMyConnections);
//
//    /*Get Opportunity details by mongoId*/
//    @GET("Opportunity/{mongoId}")
//    Call<MyConnectionRequestDto> getOppDetailsByMongoId(@Path("mongoId") String mongoId);
//
//    /* Post Opportunity comments service*/
//    @PATCH("Opportunity/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<String> postOpportunityComments(@Path("mongo_id") String mongoId, @Body String commentStr);
//
//    @PATCH("Opportunity/{mongoId}")
//    @Headers("Content-type: application/json")
//    Call<String> postOppViewsCount(@Path("mongoId") String mongoId, @Body String mpngoIdStr);
//
//    /* get likes list of status feed*/
//    @GET("StatusFeed")
//    Call<LoginDto> getStatusLikedUsersList(@Query("filter") String likedUsersStr, @Query("keys") String likesKeyStr);
//
//    /* get likes list of Event*/
//    @GET("Event")
//    Call<LoginDto> getEventLikedUsersList(@Query("filter") String likedUsersStr, @Query("keys") String likesKeyStr);
//
//
//    /* update User device token ID for FCM Notifications */
//    @PATCH("UserProfile/{mongo_id}")
//    @Headers("Content-type: application/json")
//    Call<Void> updateFCMDeviceID(@Path("mongo_id") String mongo_id, @Body MyInterestsRequest biographyNotes);
//
//
//    /*Java Web services*/
//
//    /* Forgot password service*/
//    @GET("rest/arthub/reset")
//    Call<ResetPasswordResponse> forgotPassword(@Query("email") String emailIdStr);
//
//    /*http://192.168.1.7:9191/ArtHub/rest/arthub/statusfeed?value=57b2b1e82472ffdbdc9e9101&page=1*/
//    /* Get status updates */
//    @GET("rest/arthub/statusfeed")
//    Call<List<StatusFeedResponse>> getStatusFeed(@Query("value") String userMongoId, @Query("page") int page);
//
//    @POST("rest/arthub/save/statusfeed")
//    Call<Void> postStatusFeed(@Body StatusFeedRequest feedRequest);
//
//    /*Get push notifications*/
//    @GET("rest/arthub/get/notificatons")
//    Call<List<NotificationsResponse>> getNotifications(@Query("userid") String userMongoId);
//
//    /*Save notifications*/
//    @POST("rest/arthub/save/notifs")
//    Call<Void> saveNotifications(@Body NotificationsResponse notificationsRequest);
//
//    /*post push notifications*/
//    //http://192.168.1.7:9191/ArtHub/rest/arthub/send/push/notif
//    @POST("rest/arthub/send/push/notif")
//    Call<Void> sendPushNotifications(@Body FCMNotificationsRequest notificationsRequest);
//
//    /*post push notifications*/
//    @POST("fcm/send")
//    Call<Void> sendFCMPushNotifications(@Body FCMNotificationsRequest notificationsRequest);
//
//    /*Get all Refreshed Fire base IDs*/
//    @GET("rest/arthub/all/device")
//    Call<List<UserDeviceIDResponse>> getAllUserDeviceIDs();
//
//    /*Get all Refreshed private connections IDs List*/
//    @GET("rest/arthub/all/connection/deviceid")
//    Call<List<UserDeviceIDResponse>> getAllPrivateUserDeviceIDs(@Query("userid") String userMongoId);
//
//    /*post Feed back service*/
//    @POST("rest/arthub/save/feedback")
//    Call<Void> postFeedBack(@Body FeedBackRequest feedBackRequest);
//
//    /*Get up coming Events list*/
//    @GET("rest/arthub/get/upcoming/events")
//    Call<List<GuestLoginResponse>> getUpcomingEventsList();
//
//    /*Get Recent Opportunities list*/
//    @GET("rest/arthub/get/recent/opportunities")
//    Call<List<GuestLoginResponse>> getRecentOpportunitiesList();
//
//    /*Get Art Teachers list*/
//    @GET("rest/arthub/get/teachers")
//    Call<List<GuestLoginResponse>> getArtTeachersList();
//
//    /*Filter up coming Events*/
//    @POST("rest/arthub/get/filtered/upcoming/events")
//    Call<List<GuestLoginResponse>> getUpComingEventsByFilter(@Body FilterRequest filterRequest);
//
//    /*Filter Events*/
//    @POST("rest/arthub/filter/events")
//    Call<List<GuestLoginResponse>> getEventsByFilter(@Body FilterRequest filterRequest);
//
//    /*Search Events*/
//    @POST("rest/arthub/search/events")
//    Call<List<GuestLoginResponse>> getEventsBySearch(@Query("search") String searchStr, @Query("isguestUser") int isGuestUser,
//                                                     @Body SearchRequest searchRequest);
//
//    /*Filter Recent opportunities*/
//    @POST("rest/arthub/get/filtered/recent/opportunities")
//    Call<List<GuestLoginResponse>> getRecentOppByFilter(@Body FilterRequest filterRequest);
//
//    /*Filter Opportunities*/
//    @POST("rest/arthub/filter/opportunities")
//    Call<List<GuestLoginResponse>> getOpportunitiesByFilter(@Body FilterRequest filterRequest);
//
//    /*Search  Opportunities*/
//    @POST("rest/arthub/search/opportunities")
//    Call<List<GuestLoginResponse>> getOppBySearch(@Query("search") String searchStr, @Query("isguestUser") int isGuestUser,
//                                                  @Body SearchRequest searchRequest);
//
//
//    /*Filter Art Teachers*/
//    @POST("rest/arthub/get/filtered/teachers")
//    Call<List<GuestLoginResponse>> getArtTeachersByFilter(@Body FilterRequest filterRequest);
//
//    /*Search Art Teachers*/
//    @GET("rest/arthub/search/teachers")
//    Call<List<GuestLoginResponse>> getArtTeachersBySearch(@Query("search") String searchStr);
//
//    /*Get Art Details Service*/
//    @GET("rest/arthub/get/master/data")
//    Call<List<ArtDetailsResponse>> getArtDetails();
//
//
//    /*get user profile views count*//*
//    @GET("/UserProfile")
//    void getProfileViewsCount(@Query("filter") String profileVieswStr,@Query("keys") String keyStr, Callback<LoginDto> responseCallback);
//
//    *//*get user profile details*//*
//    @GET("/UserProfile/{mongo_id}")
//    void getProfileDetailsByMongoId(@Path("mongo_id") String mongoId, Callback<RHDocList> userCallback);
//
//    *//*login with existing records in DB*//*
//    *//*  filter={'Email':'ramya@gmail.com','Password':'ramya',,'IsActive':'1'}*//*
//    @GET("/UserProfile")
//    void loginDetails(@Query("filter") String loginDetails, Callback<LoginDto> userCallback);
//
//    *//*  posting user details for sign up service*//*
//    @POST("/UserProfile")
//    void postUserDetailsForSignUp(@Body SignUpRequest signUpRequest, Callback<Response> responseCallback);
//
//    *//* get user profile description service*//*
//*//*  filter={"UserId":"5719cd6a84ae1d67226c147c",'IsActive':'1'}*//*
//    @GET("/UserProfile")
//    void getUserProfileDescription(@Query("filter") String userDescription, @Query("keys") String biographyNotesStr ,Callback<LoginDto> userCallback);
//
//    *//* upadte user profile description service*//*
//    @PATCH("/UserProfile/{mongo_id}")
//    void postUserDescription(@Path("mongo_id") String mongo_id, @Body MyInterestsRequest bographyNotes, Callback<Response> responseCallback);
//
//    *//*Update profile image service*//*
//    @PATCH("/UserProfile/{mongo_id}")
//    void updateProfileImage(@Path("mongo_id") String mongo_id, @Body MyInterestsRequest updateProfileImagerequest, Callback<Response> responseCallback);
//
//    *//* get user skills service*//*
//    @GET("/Skills")
//    void getUserAddedSkills(@Query("filter") String userSkills, Callback<LoginDto> userCallback);
//
//    *//*post user skills service*//*
//    @POST("/Skills")
//    void postUserSkills(@Body PostSkills addSkillsRequest, Callback<Response> responseCallback);
//
//    *//* get my connections*//*
//    @GET("/UserProfile")
//    void getMyConnections(@Query("filter") String myConnections,@Query("keys") String connectionsKeyStr, Callback<LoginDto> responseCallback);
//
//    *//*endorse an user*//*
//    *//* {UserId:'5719cd6a84ae1d67226c147c',UserName:'Ramya',EndrosedSkill:'classical',NoofEndrosements:'50',EndrosedUsers:'5719f87884ae1d67226c1491,5719f67384ae1d67226c148f',CreatedBy:'Ramya',CreatedDate:'22-04-2016',ModifiedBy:'Ramya',ModifiedDate:'22-04-2016',IsActive:'1'}*//*
//    @POST("/Endrosement")
//    void postEndrosments(@Body EndrosmentsUserRequest endrosmentsRequest, Callback<Response> responseCallback);
//
//    *//*  get endrosments*//*
//    *//*filter={"UserId":"5719cd6a84ae1d67226c147c","IsActive":"1"}*//*
//    @GET("/Endrosement")
//    void getEndrosments(@Query("filter") String endrosmentsRequest, Callback<AboutProfileDto> responseCallback);
//
//    *//*  get collaborations*//*
//    @GET("/UserProfile")
//    void getCollaborations(@Query("filter") String collaborations,@Query("keys") String collabKeyStr, Callback<LoginDto> responseCallback);
//
//    *//* post collaborations*//*
//   *//* {UserId:'5719cd6a84ae1d67226c147c',UserName:'Ramya',CollaborationDescription:'Collaborated with XYZ and done an event',Images:'http://res.cloudinary.com/hgpakagpi/image/upload/v1460279438/gmlpurjis8jehem62eun.jpg,http://res.cloudinary.com/hgpakagpi/image/upload/v1460027219/iqs8wosvgvckzywvz9ds.png,http://res.cloudinary.com/hgpakagpi/image/upload/v1460010738/ou7qjn3qhoe8ni9wpdqg.jpg',CreatedBy:'Ramya',CreatedDate:'22-04-2016',ModifiedBy:'Ramya',ModifiedDate:'22-04-2016',IsActive:'1'}*//*
//    @PATCH("/UserProfile/{mongo_id}")
//    @Headers("Content-type: application/json")
//    void postCollaborations(@Path("mongo_id") String mongoId, @Body TypedString collaborations,
//                            Callback<Response> responseCallback);
//
//    *//* update profile details service*//*
////    @PATCH("/UserProfile/{mongo_id}")
////    @Headers("Content-type: application/json")
////    void updateUserProfileInterests(@Path("mongo_id") String mongoId,@Body TypedString updateDetailsStr,
////                            Callback<Response> responseCallback);
//
//    *//* update profile Interests service*//*
//    @PATCH("/UserProfile/{mongo_id}")
//    void updateUserProfileInterests(@Path("mongo_id") String mongoId, @Body MyInterestsRequest updateInterestsRequest,
//                                    Callback<Response> responseCallback);
//
//    *//*get user profile interests*//*
//    @GET("/UserProfile/{mongo_id}")
//    void getUserProfileInterests(@Path("mongo_id") String mongoId, Callback<RHDocList> userCallback);
//
//    *//*  To Get  opportunities from my connections *//*
//    @GET("/Opportunity")
//    void getOpprtFromMyConnections(@Query("filter") String opprtFrmMyConnections, Callback<MyConnectionEmbedded> responseCallback);
//
//
//    *//*  To Get  opportunities from other connections *//*
//    @GET("/Opportunity")
//    void getOpprtFromOtherConnections(@Query("filter") String opprtFrmOtherConnections, Callback<MyConnectionEmbedded> responseCallback);
//
//
//    @POST("/Opportunity")
//    void postOpportunityDetails(@Body PostOpportunity postOpportunity, Callback<Response> response);
//
//    *//*  To Get Public opportunities *//*
//    *//*filter={"IsActive":"1","IsPublic":"1"}*//*
//    @GET("/Opportunity")
//    void getPublicOpportunities(@Query("filter") String publicOpprtnties, Callback<AboutProfileDto> responseCallback);
//
//    @POST("/Connection")
//    void sendConnectionRequest(@Body SendConnectionRequest sendConnectionRequest, Callback<Response> responseCallback);
//
//    @DELETE("/Skills/{skill_mongo_id}")
//    @Headers("Content-type: application/json")
//    void deleteSkill(@Header("if-match") String header, @Path("skill_mongo_id") String skillId, Callback<Response> responseCallback);
//
//
//    @PATCH("/UserRequests/{mongo_id}")
//        //@Headers("Content-type: application/json")
//    void acceptRequest(@Path("mongo_id") String mongoId, @Body TypedString deleteSkill, Callback<Response> responseCallback);
//
//    *//* get user skills service*//*
//   *//* filter={'UserId':'5719cd6a84ae1d67226c147c','IsActive':'1'}*//*
//    @GET("/Inbox")
//    void getInboxMessagesList(@Query("filter") String inboxList, Callback<LoginDto> userCallback);
//
//    *//* post user text message service*//*
//    @POST("/Inbox")
//    void postTextMessage(@Body PostInboxMsgRequest postInboxRequest, Callback<Response> responseCallback);
//
//   *//* To get connection or connected requests*//**//*
//   *//**//* /Connection?filter={'$or':[{'Connected':'1'},{'Connected':'0'}],'$or':[{'RequestToUserId':'572061b31dc5840de1d2c6dd'},{'RequestFromUserId':'572061b31dc5840de1d2c6dd'}]}*//**//*
//    @GET("/Connection")
//    void getConnectionRequests(@Query("filter") String requests,Callback<AboutProfileDto> response);*//*
//
//    @GET("/EventRequest")
//    void getMyEvents(@Query("filter") String myEventsStr, Callback<LoginDto> response);
//
//    @POST("/EventRequest")
//    void createEvent(@Body EventRequest event, Callback<Response> response);
//
//    @GET("/EventRequest")
//    void getPublicEvents(@Query("filter") String publicEventsStr, Callback<LoginDto> responseCallback);
//
//    @GET("/UserProfile")
//    void getEndrosers(@Query("filter") String endrosers, Callback<LoginDto> response);
//
//    @GET("/UserRequests")
//    void getConnectionRequests(@Query("filter") String connectionRequests, Callback<LoginDto> response);
//
//    @POST("/UserRequests")
//    void connectNowWithUser(@Body ConnectNowRequest connectNowRequest, Callback<Response> response);
//
//    @PATCH("/UserProfile/{mongoId}")
//    @Headers("Content-type: application/json")
//    void postToYourConnections(@Path("mongoId") String mongoId, @Body TypedString toMonogoId, Callback<Response> response);
//
//    @GET("/UserProfile")
//    void searchBy(@Query("filter") String searchStr, Callback<LoginDto> response);
//
//    @PATCH("/Skills/{mongo_id}")
//    @Headers("Content-type: application/json")
//    void endorseUserSkills(@Path("mongo_id") String mongoId, @Body TypedString endorseUserSkillStr, Callback<Response> responseCallback);
//
//    @PATCH("/EventRequest/{mongoId}")
//    @Headers("Content-type: application/json")
//    void expressInterestEvent(@Path("mongoId") String mongoId,@Body TypedString interestMongoId, Callback<Response> response);
//
//
//    *//* update profile Interests service*//*
//    @PATCH("/Opportunity/{mongo_id}")
//    void postOpprtComments(@Path("mongo_id") String mongoId,@Body TypedString commentStr,
//                           Callback<Response> responseCallback);*/


}
