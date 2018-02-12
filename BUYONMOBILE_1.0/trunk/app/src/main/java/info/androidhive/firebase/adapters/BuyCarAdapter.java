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

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import info.androidhive.firebase.CarDetailsActivity;
import info.androidhive.firebase.Preferences;
import info.androidhive.firebase.R;
import info.androidhive.firebase.restservices.dto.BuyerUserResponse;

/**
 * Created by Subbareddy on 23/08/17.
 */

public class BuyCarAdapter extends RecyclerView.Adapter<BuyCarAdapter.ViewHolder> {

    Context context;

    private List<BuyerUserResponse> usersList;

    private StringTokenizer st;

    private int userId, userId1, userIdCall;

    private String name, phoneStr, emailStr;

    public BuyCarAdapter(Context context, List<BuyerUserResponse> usersList) {
        super();

        this.context = context;
        this.usersList = usersList;

    }

    /*Refresh list*/
    public void refreshList(List<BuyerUserResponse> usersList1) {

        if (usersList1.size() > 0) {

            usersList.clear();
            usersList.addAll(usersList1);
            Log.e("^^ Users Maket",usersList.get(0).getCarMake());
            Log.e("^^ Users List", String.valueOf(usersList.get(0).getCarImageUrls()));
            notifyDataSetChanged();

        }

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

        //this.userId = usersList.get(i).getUserID();
        Log.e("$$$ I value is", String.valueOf(i));
        this.userId1 = usersList.get(i).getUserID();
        Log.e("--->USer Id", String.valueOf(userId1));
        Log.e("*** Matches", String.valueOf(Preferences.userID));
      // for(int j=0; j < usersList.size(); j++) {
           //if (userId1 != Preferences.userID) {
               Log.e("$$$ Matches", String.valueOf(Preferences.userID));
               Log.e("@@@ Matches", String.valueOf( usersList.get(i).getUserID()));
               if (!TextUtils.isEmpty(usersList.get(i).getCarMake())) {
                   viewHolder.carNameTv.setText(usersList.get(i).getCarMake());
               }

               final String carModelStr = usersList.get(i).getCarModel();
               final String carYearStr = usersList.get(i).getCarYear();

               // imagesList = usersList.get(i).getImageUrls();

               if (!TextUtils.isEmpty(carModelStr) || !TextUtils.isEmpty(carYearStr)) {
                   viewHolder.carModelTv.setText(carModelStr + "/" + carYearStr);
               }

               if (!TextUtils.isEmpty(usersList.get(i).getCarPriceStart())) {
                   viewHolder.carPriceTv.setText(usersList.get(i).getCarPriceStart());
               }
          // }
      // }

           final String imagesListStr = usersList.get(i).getCarImageUrls();

           //if (userId1 != Preferences.userID) {
           if (!TextUtils.isEmpty(usersList.get(i).getCarImageUrls())) {
               List<String> imagesList = Arrays.asList(imagesListStr.split("\\s*,\\s*"));

               if (!TextUtils.isEmpty(imagesList.get(0))) {
                   Picasso.with(context)
                           .load(imagesList.get(0))
                           .fit()
                           .into(viewHolder.carImageVw);

               } else {
                   viewHolder.carImageVw.setImageResource(R.mipmap.ic_launcher);
               }

           }

           viewHolder.buyBtn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(context, CarDetailsActivity.class);
                   intent.putExtra("userId", usersList.get(i).getUserID());
                   intent.putExtra("carName", usersList.get(i).getCarMake());
                   intent.putExtra("carModel", usersList.get(i).getCarModel());
                   intent.putExtra("carYear", usersList.get(i).getCarYear());
                   intent.putExtra("carPrice", usersList.get(i).getCarPriceStart());
                   //intent.putExtra("name", usersList.get(i).getName());
                   //intent.putExtra("phone", usersList.get(i).getPhoneNo());
                   //intent.putExtra("email", usersList.get(i).getEmailId());
                   //Log.e("$$$ Images List", String.valueOf(imagesList));
                   intent.putExtra("imagesListStr", imagesListStr);


                   // intent.putExtra("carImage",usersList.get(i).getCarModel());
                   context.startActivity(intent);
                   ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);


               }
           });
      // }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){


    }
    });
}

    @Override
    public int getItemCount() {
        int userId1 = 0;
        int count = 0;
        /*for(int j=0; j < usersList.size(); j++){

            userId1 = usersList.get(j).getUserID();
            Log.e("--->USer Id", String.valueOf(userId1));
            if(userId1 != Preferences.userID){
                count++;
            }
        }
        Log.e("@@Count Value", String.valueOf(count));
        return count;
*/
        return usersList.size();


    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView carImageVw;
        public TextView carNameTv;
        public TextView carModelTv;
        public TextView carPriceTv;
        public TextView nameTv,phoneNoTv,emailIdTv;
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

        }
    }


}

