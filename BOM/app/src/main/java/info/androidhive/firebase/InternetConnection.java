package info.androidhive.firebase;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

/**
 * Created by venkareddy on 21/11/16.
 */

public class InternetConnection {
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void snackBar(View view, String message, String action){

        Snackbar snackbar = Snackbar
                .make(view, "No internet connection !", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.GREEN);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    public static void snackBarNoConnection(View view){

        Snackbar.make(view, Html.fromHtml("<font color=\"#FF0000\">No internet connection !</font>"), Snackbar.LENGTH_SHORT).show();
    }

    public static void snackBarServerError(View view){
        Snackbar.make(view, "Couldn't connect to server", Snackbar.LENGTH_LONG).show();
    }
}
