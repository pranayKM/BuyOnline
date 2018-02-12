package info.androidhive.firebase;

import android.content.Context;

/**
 * Created by venkareddy on 21/11/16.
 */

public class ProgressDialogBar {

    private Context context;
    private android.app.ProgressDialog progress;

    public ProgressDialogBar(Context context) {

        this.context = context;
    }

    public void showProgressDialog(){

        progress = new android.app.ProgressDialog(context);
        progress.setInverseBackgroundForced(true);
      //  progress.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress_dialog_animator));
        progress.setMessage("Please wait...");
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    public void dismissProgressDialog(){

        if (progress.isShowing()){
            progress.dismiss();
        }

    }
}

