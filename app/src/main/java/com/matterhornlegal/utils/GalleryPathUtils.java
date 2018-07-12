package com.matterhornlegal.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import id.zelory.compressor.Compressor;

/**
 * Created by sumit on 23/11/15.
 */
public class GalleryPathUtils {


    private static final long IMAGE_SIZE_STORAGE =262144;
    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }

        return result;
    }


    /**
     * returns Real path of given uri.
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        String path = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (path == null) {
            Bitmap requiredImage = null;

            String photoIdentificationKey = Uri.parse(contentUri.getLastPathSegment()).getLastPathSegment();
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream inputStream;
            OutputStream outputStream;
            path = Utils.getAppHiddenDirectory(context);
            File file = new File(path, "IMAGE" + Calendar.getInstance().getTime() + ".jpg"); // the File to save to

            try {
                outputStream = new FileOutputStream(file);
                inputStream = context.getContentResolver().openInputStream(contentUri);
                // requiredImage = BitmapFactory.decodeStream(inputStream, null, options);
                IOUtils.copy(inputStream, outputStream);
                //Save the newly downloaded image to your isolated storage and return the path
                //on which this new image has been saved.

                inputStream.close();
                outputStream.close();
                path = file.getAbsolutePath();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return path;
    }

    /**
     * Connvert the image at given path to base64 string and returns it.
     * @param context
     * @param path
     * @return
     */
    public static String getBase64Image(Context context, String path)
    {

        try{

            File reducedFile = Compressor.getDefault(context).compressToFile(new File(path));
            if(reducedFile==null)return "base64";

            path = reducedFile.getAbsolutePath();
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
        catch (Exception e)
        {

        }
        return "";
    }

    /**
     * Convert bitmap to base64 string and returns it.
     * @param bm
     * @return
     */
    public static String getBase64Image(Bitmap bm)
    {

        try{

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
        return "";
    }

    public static File saveBitmapToFile(File file, int required_size){
        try {


            if(file.exists() && FileUtils.sizeOf(file) <= IMAGE_SIZE_STORAGE)
                return file;
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=required_size;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }



}