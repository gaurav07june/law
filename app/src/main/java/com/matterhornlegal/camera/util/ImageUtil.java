package com.matterhornlegal.camera.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;


import com.matterhornlegal.camera.ICallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by tomiurankar on 06/03/16.
 */
public class ImageUtil {
    /**
     * Saves byte[] array to disk
     *
     * @param input    byte array
     * @param output   path to output file
     * @param callback will always return in originating thread
     */
    public static void saveToDiskAsync(final byte[] input, final File output, final ICallback callback) {
        final Handler handler = new Handler();
        new Thread() {
            @Override
            public void run() {
                try {
                    FileOutputStream outputStream = new FileOutputStream(output);
                    outputStream.write(input);
                    outputStream.flush();
                    outputStream.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.done(null);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.done(e);
                        }
                    });
                }
            }
        }.start();
    }

    public static void fixOrientation(String path) {

        int orientation;
        Bitmap result_bm=null;
        try {

            if(path==null){

                result_bm= null;
            }
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 300;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 4;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale++;
            }
            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            Bitmap bm = BitmapFactory.decodeFile(path,o2);


            Bitmap bitmap = bm;

            ExifInterface exif = new ExifInterface(path);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.e("orientation",""+orientation);
            Matrix m=new Matrix();

            if((orientation==3)){

                m.postRotate(180);
                m.postScale((float)bm.getWidth(), (float)bm.getHeight());

//               if(m.preRotate(90)){
                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                result_bm=  bitmap;
            }
            else if(orientation==6){

                m.postRotate(90);

                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                result_bm=  bitmap;
            }

            else if(orientation==8){

                m.postRotate(270);

                Log.e("in orientation",""+orientation);

                bitmap = Bitmap.createBitmap(bm, 0, 0,bm.getWidth(),bm.getHeight(), m, true);
                result_bm=  bitmap;
            }


            if (result_bm!=null)
            {

                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(path);
                    result_bm.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e) {
        }




    }
    /**
     * Rotates the bitmap per their EXIF flag. This is a recursive function that will
     * be called again if the image needs to be downsized more.
     *
     * @param inputFile Expects an JPEG file if corrected orientation wants to be set.
     * @return rotated bitmap or null
     */
    @Nullable
    public static Bitmap getRotatedBitmap(String inputFile, int reqWidth, int reqHeight) {
        final int rotationInDegrees = getExifDegreesFromJpeg(inputFile);

        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inputFile, opts);
        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight, rotationInDegrees);
        opts.inJustDecodeBounds = false;

        final Bitmap origBitmap = BitmapFactory.decodeFile(inputFile, opts);

        if (origBitmap == null)
            return null;

        Matrix matrix = new Matrix();
        matrix.preRotate(rotationInDegrees);
        // we need not check if the rotation is not needed, since the below function will then return the same bitmap. Thus no memory loss occurs.

        return Bitmap.createBitmap(origBitmap, 0, 0, origBitmap.getWidth(), origBitmap.getHeight(), matrix, true);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, int rotationInDegrees) {

        // Raw height and width of image
        final int height;
        final int width;
        int inSampleSize = 1;

        // Check for rotation
        if(rotationInDegrees == Degrees.DEGREES_90 || rotationInDegrees == Degrees.DEGREES_270){
            width = options.outHeight;
            height = options.outWidth;
        } else {
            height = options.outHeight;
            width = options.outWidth;
        }

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private static int getExifDegreesFromJpeg(String inputFile) {
        try {
            final ExifInterface exif = new ExifInterface(inputFile);
            final int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                return 90;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
                return 180;
            } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
                return 270;
            }
        } catch (IOException e) {
            Log.e("exif", "Error when trying to get exif data from : " + inputFile, e);
        }
        return 0;
    }
}