package info.androidhive.firebase.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import info.androidhive.firebase.CarDetailsActivity;
import info.androidhive.firebase.Preferences;
import info.androidhive.firebase.R;
import info.androidhive.firebase.restservices.dto.MatchedUserIdsResponse;
import info.androidhive.firebase.restservices.dto.UserPreferencesResponse;

/**
 * Created by Subbareddy on 8/27/2017.
 */

public class SellCarAdapter extends RecyclerView.Adapter<SellCarAdapter.ViewHolder> {

    Context context;

    private List<UserPreferencesResponse> usersList;

    private List<String> imagesList = new ArrayList<>();

    private String imagesListStr;

    private int userId;

    private int userId1 = 0;

    private int matchedUserId;

    private List<Integer> deviceIdsList;

    private Integer[] deviceIdsStrList;

    private ArrayList<MatchedUserIdsResponse> matchedRecordsResponseList= new ArrayList<>();

    //String URL = "http://10.0.2.2:8080/BOMApplication/REST/User/DetailsId";
    String URL = "http://ec2-54-67-72-50.us-west-1.compute.amazonaws.com:8080/BOMApplication/REST/From/UserDetailsId";

    public StringTokenizer st;


    public SellCarAdapter(Context context, List<UserPreferencesResponse> usersList) {
        super();
        this.context = context;
        this.usersList = usersList;


    }

    // inflating the recyler item to the view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_pref_row_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //Setting items to Grid adapter
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        Log.e("### Final Values", String.valueOf(i));

        for(int j=0; j < usersList.size(); j++){
            userId1 = usersList.get(j).getUserID();
            if(userId1 == Preferences.userID){
                Log.e("--===>> BuyCar User ID", String.valueOf(userId));

                viewHolder.carNameTv.setText(usersList.get(j).getCarMake());

                final String carModelStr= usersList.get(j).getCarModel();
                final String carYearStr= usersList.get(j).getCarYear();

                // imagesList = usersList.get(i).getImageUrls();
                Log.e("@@ CarModel",carModelStr);

                viewHolder.carModelTv.setText(carModelStr+"/"+carYearStr);
                viewHolder.carPriceTv.setText(usersList.get(i).getCarPriceStart());

                imagesListStr = usersList.get(j).getCarImageURLs();
                Log.e("@@ List of Images",imagesListStr);
                st = new StringTokenizer(imagesListStr, ",");
                while (st.hasMoreTokens()) {
                    String ele;
                    ele = st.nextToken();
                    imagesList.add(ele);
                    //System.out.println(st.nextElement());
                }
                Log.e("@@ImagesURL", String.valueOf(imagesList));

                if (!TextUtils.isEmpty(imagesList.get(0))) {
                    Log.e("=>> BuyCar Utils", String.valueOf(userId));
                    Picasso.with(context)
                            .load(imagesList.get(0))
                            .fit()
                            .into(viewHolder.carImageVw);

                }

                viewHolder.buyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, CarDetailsActivity.class);
                        intent.putExtra("carName",usersList.get(i).getCarMake());
                        intent.putExtra("carModel",usersList.get(i).getCarModel());
                        intent.putExtra("carYear",usersList.get(i).getCarYear());
                        intent.putExtra("carPrice",usersList.get(i).getCarPriceStart());
                        intent.putExtra("name",usersList.get(i).getName());
                        intent.putExtra("phone",usersList.get(i).getPhoneNo());
                        intent.putExtra("email",usersList.get(i).getEmailId());
                        intent.putExtra("carImagesList",usersList.get(i).getCarImageURLs());

                        // intent.putExtra("carImage",usersList.get(i).getCarModel());
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);


                    }
                });
            }
        }

         userId = usersList.get(i+1).getUserID();
        Log.e("%%%", String.valueOf(usersList.get(i+1).getUserPreId()));


//        matchedUserId = matchedRecordsResponseList.get(0).getUserId();
        Log.e("**** User Id 1", String.valueOf(userId1));
        Log.e("**** User Id", String.valueOf(userId));
        //Log.e("**** USer Id 1",userId1);

        Log.e("**** Matched User Id", String.valueOf(Preferences.userID));

        if(userId == Preferences.userID){

        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @Override
    public int getItemCount() {

        userId1 = 0;
        int count = 0;
        for(int j=0; j < usersList.size(); j++){
            userId1 = usersList.get(j).getUserID();
            if(userId1 == Preferences.userID){
                Log.e("### getItemCount", String.valueOf(userId1));
                count++;
            }
        }
        Log.e("@@Count Value", String.valueOf(count));
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView carImageVw;
        public TextView carNameTv;
        public TextView carModelTv;
        public TextView carPriceTv;
        public TextView nameTv, phoneNoTv, emailIdTv;
        public Button buyBtn;


        public ViewHolder(View itemView) {

            super(itemView);


            carNameTv = (TextView) itemView.findViewById(R.id.carNameTv);
            carImageVw = (ImageView) itemView.findViewById(R.id.carImgVw);
            carModelTv = (TextView) itemView.findViewById(R.id.carModelTv);
            carPriceTv = (TextView) itemView.findViewById(R.id.carPriceTv);
            nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            phoneNoTv = (TextView) itemView.findViewById(R.id.phoneNumTv);
            emailIdTv = (TextView) itemView.findViewById(R.id.emailTv);
            buyBtn = (Button) itemView.findViewById(R.id.buyBtn);
            buyBtn.setText("View/Edit Details");


        }
    }

    /*public int getUserId(){
        Log.e("$$$ Entered", String.valueOf(0));
        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>(){

            @Override
            public void onResponse(String s) {

                Log.e("Response",s);
                Log.e("$$$$Response",s);
                if(!s.equals("No Records found")) {
                    matchedRecordsResponseList = new Gson().fromJson(s, new TypeToken<List<MatchedUserIdsResponse>>() {
                    }.getType());
                    Log.e("@@@Size", String.valueOf(matchedRecordsResponseList.size()));
                    deviceIdsList = new ArrayList<Integer>();
                    for(int i=0; i < matchedRecordsResponseList.size();i++){
                        //deviceIdsList.add(tokenResponse.get(i).getFcmTokenID());
                        deviceIdsList.add(matchedRecordsResponseList.get(i).getUserId());
                        //Log.e("-->SellerIDSList",deviceIdsList.toString());
                    }

                    deviceIdsStrList = new Integer[deviceIdsList.size()];
                    deviceIdsStrList = deviceIdsList.toArray(deviceIdsStrList);
                    Log.e("==>Device Tokens", String.valueOf(deviceIdsStrList));
                    matchedUserId = matchedRecordsResponseList.get(0).getUserId();
                    Log.e("-->Size", String.valueOf(matchedRecordsResponseList.get(0).getUserId()));
                    Log.e("@@ Hellow", String.valueOf(matchedRecordsResponseList));



                }


            }

        },new com.android.volley.Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Toast.makeText(SellCarAdapter.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("emailId",Preferences.emailID);


                return parameters;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(context);
        rQueue.add(request);
        return matchedUserId;

    }*/
}
