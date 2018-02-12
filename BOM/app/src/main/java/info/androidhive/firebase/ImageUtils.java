package info.androidhive.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * Created by venkareddy on 21/11/16.
 */

public class ImageUtils {

    public static void saveFile(Context context, Bitmap b, String picName) {

        FileOutputStream fos;
        try {

            File direct = new File(Environment.getExternalStorageDirectory() + "/ArtHub");

            if (!direct.exists()) {
                File wallpaperDirectory = new File("/sdcard/ArtHub/");
                wallpaperDirectory.mkdirs();
            }

            OutputStream fOut = null;
            File filePath = new File(Environment.getExternalStorageDirectory() + "/ArtHub/" + picName);
            fOut = new FileOutputStream(filePath);

            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
            fOut.close();
        } catch (FileNotFoundException e) {
            Log.d("file", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("file", "io exception");
            e.printStackTrace();
        }

        /*FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("file", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("file", "io exception");
            e.printStackTrace();
        }*/

    }

    public static Bitmap loadBitmap(Context context, String picName) {

        Bitmap b = null;
        FileInputStream fis;
        try {
            File filePath = new File(Environment.getExternalStorageDirectory() + "/ArtHub/" + picName);
            b = scaleImage(filePath);
            fis = new FileInputStream(filePath);
//            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            Log.d("file", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("file", "io exception");
            e.printStackTrace();
        }

       /* Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d("file", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("file", "io exception");
            e.printStackTrace();
        }*/
        return b;
    }

    public static boolean deleteFile(Context context, String fileName) {

        File file = new File(Environment.getExternalStorageDirectory() + "/ArtHub/" + fileName);

//        File dir = context.getFilesDir();
//        File file = new File(dir, fileName);
        return file.delete();
    }

    public static String filePath(Context context, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/ArtHub");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/ArtHub/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/ArtHub/" + fileName);

//        File dir = context.getFilesDir();
//        File file = new File(dir, fileName);
        return file.getPath();
    }

    public static File loadFile(Context context, String picName) {

        File file = new File(Environment.getExternalStorageDirectory() + "/ArtHub/" + picName);

//        File dir = context.getFilesDir();
//        File file = new File(dir, picName);
        return file;
    }


    public static String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    // Compress image
    public static Bitmap scaleImage(File f) {

        String imagePath = f.getPath();

        float maxHeight = 1280.0f;
        float maxWidth = 1280.0f;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if(bmp!=null)
        {
            bmp.recycle();
        }

        /*ExifInterface exif;
        try {
            exif = new ExifInterface(imagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return scaledBitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
   /* // scale image bitmap
    public static Bitmap scaleImage(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }*/
}
