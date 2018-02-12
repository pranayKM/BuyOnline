package info.androidhive.firebase.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.androidhive.firebase.CarDetailsActivity;
import info.androidhive.firebase.R;
import info.androidhive.firebase.restservices.dto.UserPreferencesResponse;

/**
 * Created by venkareddy on 23/11/16.
 */

public class UserPreferencesAdapter extends RecyclerView.Adapter<UserPreferencesAdapter.ViewHolder> {

    Context context;

    private List<UserPreferencesResponse> usersList;

    public UserPreferencesAdapter(Context context, List<UserPreferencesResponse> usersList) {
        super();

        this.context = context;
        this.usersList=usersList;

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

        viewHolder.carNameTv.setText(usersList.get(i).getCarMake());

        final String carModelStr= usersList.get(i).getCarModel();
        final String carYearStr= usersList.get(i).getCarYear();

        viewHolder.carModelTv.setText(carModelStr+"/"+carYearStr);
        viewHolder.carPriceTv.setText(usersList.get(i).getCarPriceStart());

/*
        if (!TextUtils.isEmpty(profileImageUrl)) {

            Picasso.with(context)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.dummy_pic)
                    .fit()
                    .into(viewHolder.carImageVw);

        }*/

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

                   // intent.putExtra("carImage",usersList.get(i).getCarModel());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);


                }
            });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @Override
    public int getItemCount() {

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

