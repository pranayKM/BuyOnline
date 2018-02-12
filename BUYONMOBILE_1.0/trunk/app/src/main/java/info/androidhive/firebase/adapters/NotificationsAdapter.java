package info.androidhive.firebase.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import info.androidhive.firebase.Preferences;
import info.androidhive.firebase.R;
import info.androidhive.firebase.restservices.dto.UserMatchedNotificationResponse;

/**
 * Created by Subbareddy on 10/28/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    Context context;

    private List<UserMatchedNotificationResponse> notificationResponseList;

    private List<String> imagesList = new ArrayList<>();

    private String imagesListStr;

    private int userId;

    private int[] sampleArray;

    int a,b,c,k;

    public StringTokenizer st;

    public NotificationsAdapter(Context context, List<UserMatchedNotificationResponse> NotificationResponse) {
        super();

        this.context = context;
        this.notificationResponseList = NotificationResponse;

    }

    // inflating the recyler item to the view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.e("Hellow This", String.valueOf(0));
        Log.e("Notifications List", String.valueOf(notificationResponseList.size()));
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notifications_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    //Setting items to Grid adapter
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        userId = notificationResponseList.get(i).getUserID();
        Log.e("#####Notifications Size", String.valueOf(notificationResponseList));
        Log.e("$$Notification UserId", String.valueOf(userId));
        Log.e("## User Id", String.valueOf(Preferences.userID));
        Log.e("Count Value", String.valueOf(i));
           // if(userId == Preferences.userID) {
                viewHolder.userNmTv.setText(notificationResponseList.get(sampleArray[i]).getUserID() + "");
                viewHolder.notificationTv.setText(notificationResponseList.get(sampleArray[i]).getUserNotiticationMsg());
            //}


        //  imagesListStr = notificationResponseList.get(i).getCarImageURLs();
           /* Log.e("@@ List of Images",imagesListStr);
            st = new StringTokenizer(imagesListStr, ",");
            while (st.hasMoreTokens()) {
                String ele;
                ele = st.nextToken();
                imagesList.add(ele);
                //System.out.println(st.nextElement());
            }
            Log.e("@@ImagesURL", String.valueOf(imagesList));*/

         /*   if (!TextUtils.isEmpty()) {
                Log.e("=>> BuyCar Utils", String.valueOf(userId));
                Picasso.with(context)
                        .load(imagesList.get(i))
                        .fit()
                        .into(viewHolder.userIv);

            }*/

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(context, CarDetailsActivity.class);
                intent.putExtra("carName",notificationResponseList.get(i).getCarMake());
                intent.putExtra("carModel",notificationResponseList.get(i).getCarModel());
                intent.putExtra("carYear",notificationResponseList.get(i).getCarYear());
                intent.putExtra("carPrice",notificationResponseList.get(i).getCarPriceStart());
                intent.putExtra("name",notificationResponseList.get(i).getName());
                intent.putExtra("phone",notificationResponseList.get(i).getPhoneNo());
                intent.putExtra("email",notificationResponseList.get(i).getEmailId());
                intent.putExtra("carImagesList",notificationResponseList.get(i).getCarImageURLs());

                // intent.putExtra("carImage",usersList.get(i).getCarModel());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);*/


            }
        });

}

    @Override
    public int getItemCount() {
        sampleArray = new int[notificationResponseList.size()];
        int userId1;
        int count = 0;
        int count1 = 0;


        for(int j=0; j < notificationResponseList.size(); j++){
            count++;
            //userId1 = notificationResponseList.get(j).getUserID();
            /*Log.e("### Total count", String.valueOf(userId1));
            if(userId1 == Preferences.userID){
                sampleArray[k] = j;
                count1++;

            }*/
        }
        Log.e("@@Count Value", String.valueOf(count1));
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView userIv;
        public TextView userNmTv;
        public TextView notificationTv;

        public ViewHolder(View itemView) {
            super(itemView);

            userNmTv = (TextView) itemView.findViewById(R.id.userNmTv);
            userIv = (ImageView) itemView.findViewById(R.id.userIv);
            notificationTv = (TextView) itemView.findViewById(R.id.notificationTv);

        }
    }
}
