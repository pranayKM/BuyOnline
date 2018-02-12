package info.androidhive.firebase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Subbareddy on 7/23/2017.
 */

public class SellCarActivity extends AppCompatActivity{

    private ImageView carImageVw,uploadIv;

    private EditText carNameEt,carModelEt,carYearEt,priceEt;

    private Button submitBtn;

    private static final int CAMERA_IMAGE_CAPTURE = 1;

    private static final int REQUEST_IMAGE_PICK = 2;

    private Permissions permission;

    private Bitmap imageBitmap;

    private String imageFileName = null, imageFileNameUrl;

    private File outPutFile = null;

    private Uri mImageCaptureUri;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_car);

        viewsInitialization();

//        setResult(33);
//        finish();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sell a Car");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        permission = new Permissions(SellCarActivity.this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        carImageVw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permission.checkAndRequestPermissions()) {
                    cameraGalleryDialog();
                }
            }
        });

        uploadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permission.checkAndRequestPermissions()) {
                    cameraGalleryDialog();
                }
            }
        });

    }

    // Camera dialog to capture image from camera or from gallery
    private void cameraGalleryDialog() {


        new AlertDialog.Builder(this)
                //  .setIcon(R.drawable.app_icon)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage("Choose image source")
                .setPositiveButton("Camera",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                takePicture();
                            }
                        })
                .setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                choseImageFromGallery();
                            }
                        })
                .create()
                .show();
    }

    // Open  camera to capture image
    private void takePicture() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        imageFileName = timeStamp + ".png";

        outPutFile = new File(ImageUtils.filePath(this, imageFileName));

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = Uri.fromFile(outPutFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(cameraIntent, CAMERA_IMAGE_CAPTURE);
    }

    // open gallery to capture image.
    private void choseImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture)), REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File filePath = new File(ImageUtils.filePath(this, imageFileName));

            InputStream imageStream = null;
            try {
                imageStream = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 0;
            imageBitmap = BitmapFactory.decodeStream(imageStream, null, options);


            try {
                ExifInterface ei = new ExifInterface(ImageUtils.filePath(this, imageFileName));
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        imageBitmap = rotateImage(imageBitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        imageBitmap = rotateImage(imageBitmap, 180);
                        break;
                    // etc.
                }
            } catch (IOException e) {
                Log.e("error", "" + e.getMessage());
            }

            new cameraAsyncTask().execute(imageBitmap);

        } else if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK
                && null != data) {

            try {

                    final Uri imageUri = data.getData();
                    final InputStream imageStream =getContentResolver().openInputStream(imageUri);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 0;
                    Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream, null, options);


                try {
                    ExifInterface ei = new ExifInterface(ImageUtils.filePath(this, imageFileName));
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            imageBitmap = rotateImage(imageBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            imageBitmap = rotateImage(imageBitmap, 180);
                            break;
                        // etc.
                    }
                } catch (IOException e) {
                    Log.e("error", "" + e.getMessage());
                }

                new cameraAsyncTask().execute(imageBitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } /*else {
                Toast.makeText(getApplicationContext(), R.string.you_haven_t_picked_any_image,
                        Toast.LENGTH_SHORT).show();
            }*/


    }

    // rotate bitmap image in specific angle
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    // Background async task save captured image to local storage.
    public class cameraAsyncTask extends AsyncTask<Bitmap, Void, Void> {

        ProgressDialogBar dialogProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogProgress = new ProgressDialogBar(SellCarActivity.this);
            dialogProgress.showProgressDialog();
        }

        @Override
        protected Void doInBackground(Bitmap... params) {

            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            imageFileName = timeStamp + ".png";
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            params[0].compress(Bitmap.CompressFormat.PNG, 100, bytes);
            ImageUtils.saveFile(SellCarActivity.this, params[0], imageFileName);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialogProgress.dismissProgressDialog();

            if (imageFileName != null) {
                Bitmap bitmap = ImageUtils.loadBitmap(SellCarActivity.this, imageFileName);
                carImageVw.setImageBitmap(bitmap);

                uploadIv.setImageResource(android.R.drawable.ic_menu_edit);
            }

        }

    }

    /*Initializing views*/
    public void viewsInitialization(){

        carNameEt=(EditText) findViewById(R.id.carNameEt);
        carModelEt=(EditText) findViewById(R.id.carModelEt);
        carYearEt=(EditText) findViewById(R.id.carYearEt);
        priceEt=(EditText) findViewById(R.id.carPriceEt);

        carImageVw=(ImageView) findViewById(R.id.carImageVw);
        uploadIv=(ImageView) findViewById(R.id.uploadIv);
        submitBtn=(Button) findViewById(R.id.submitBtn);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {


            case android.R.id.home:

                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                return true;
            default:

                return super.onOptionsItemSelected(item);

        }

    }

}
