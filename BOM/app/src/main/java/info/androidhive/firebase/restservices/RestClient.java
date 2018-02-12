package info.androidhive.firebase.restservices;

import android.util.Base64;

import java.io.IOException;

import info.androidhive.firebase.restservices.service.BOMServices;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by venkareddy on 21/11/16.
 */
public class RestClient {

    /*Authentication UN: Artist  PWD: Hub */
    /*  http://api.arthub.me:5393/browser/#/arthub*/
    private static final String BASE_URL = "http://10.0.2.2:8080/BOMApplication/REST/"; //Rest heart Live URL

    //private static final String BASE_URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/"; //Rest heart Live URL

    private static final String FCM_BASE_URl = "https://fcm.googleapis.com/"; //FCM  URl

    private BOMServices apiService;

    private BOMServices fcmApiService;

    private String fcmAuthKey = "key = AAAA2Vp8fpk:APA91bHUPxVtYsZgMv3eziIsF29EcPc2HUVZHa8I1fyAra4UirTBb0lSOXTFGgzgs-8QILmAnqO0Lc8axB-dcifyVjJ92iahG-dYwfib6M6EBppfwcX1fURD-QZcPPkly_cEWMt20HDi";

    public RestClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en-gb")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });


        OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();
        httpClient2.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .header("Accept-Language", "en-gb")
                        .header("Authorization", fcmAuthKey)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(logging);
        httpClient2.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        Retrofit fcmRetrofit = new Retrofit.Builder()
                .baseUrl(FCM_BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient2.build())
                .build();


        apiService = retrofit.create(BOMServices.class);

        fcmApiService = fcmRetrofit.create(BOMServices.class);
    }

    public BOMServices getApiService() {
        return apiService;
    }

    public BOMServices getFcmApiService() {
        return fcmApiService;
    }


    public String getCustomAuth() {

        final String basicAuth = "Basic " + Base64.encodeToString("root:root".getBytes(), Base64.NO_WRAP);

        return basicAuth;

    }
}
