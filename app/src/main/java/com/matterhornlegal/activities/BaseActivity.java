package com.matterhornlegal.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.matterhornlegal.R;
import com.matterhornlegal.camera.MaterialCamera;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.CircularProgressDialog;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.SharedPrefsUtils;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.utils.image_utils.BitmapUtils;
import com.matterhornlegal.volley.IScreen;

import java.io.ByteArrayOutputStream;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


/**
 * Created by karan.kalsi on 10/30/2017.
 */

public abstract  class BaseActivity extends AppCompatActivity implements IScreen, BottomSheetListener {

    private ProgressDialog mProgressDialog;
    private static final int CAMERA_PIC_REQUEST = 101;
    private static final int GALLERY_PIC_REQUEST = 102;
    public ImagePickerListener listener;
    private BottomSheet imagePicker;
    private int imagePickRequest = 0;
    private boolean isSelfie = false;
    public CircularProgressDialog progressDialog;
    private static final int CAMERA_PERMISSION_REQ_CODE = 501;
    private static final int GALLERY_PERMISSION_REQ_CODE = 502;



    private Uri mCapturedImageURI;
    private PickImageListener pickImageListener;
    private PickVideoListener pickVideoListener;
    private String mImagePath=null, mImageBase64="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        progressDialog=new CircularProgressDialog(this);
        imagePicker = new BottomSheet.Builder(this, R.style.MyBottomSheetStyle)
                .setSheet(R.menu.img_picker_menu)
                .setTitle(R.string.pick_image_from)
                .setListener(this).create();
    }

    public void showProgressDialog(String bodyText) {
        if (isFinishing()) {
            return;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseActivity.this);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnKeyListener(new Dialog.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == KeyEvent.KEYCODE_SEARCH) {
                        return true; //
                    }
                    return false;
                }
            });
        }

        mProgressDialog.setMessage(bodyText);

        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }


    public void removeProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {

    }

    @Override
    public void getData(int actionID) {

    }

    // for image picker
    public void doImagePick(int request, boolean isSelfie, ImagePickerListener listener) {
        this.isSelfie = isSelfie;
        this.listener = listener;
        this.imagePickRequest = request;
        imagePicker.show();
    }
    public void onImageSelect(String photo_url, int request) {
        Toast.makeText(this, photo_url+"in base activity", Toast.LENGTH_SHORT).show();
        switch (request){
            case AppConstants.commonConstants.PROFILE_IMAGE_PICK:
                AppGlobalData.getInstance().getRegisteredUserDetail().setProfile_pic(photo_url);
                SharedPrefsUtils.setRegisteredUserPref(BaseActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.REGISTERED_USER_DETAIL, AppGlobalData.getInstance().getRegisteredUserDetail());
                break;
        }

    }
    public void processGalleryImagePath(int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            try {
                listener.onImageSelect(data);
                /*String photo_url = GalleryPathUtils.getRealPathFromURI(this, data.getData());
                onImageSelect(photo_url, imagePickRequest);*/
            } catch (Exception e1) {

                e1.printStackTrace();

            }
        }
    }
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALLERY_PIC_REQUEST:
                if (data != null)
                    processGalleryImagePath(resultCode, data);
                break;
            case CAMERA_PIC_REQUEST:
                if (data != null && data.getData() != null)
                    listener.onImageSelect(data);
                    //onImageSelect(data.getData().toString(), imagePickRequest);
                break;
        }
    }*/
    @Override
    public void onSheetShown(@NonNull BottomSheet var1)
    {

    }

    @Override
    public void onSheetItemSelected(@NonNull BottomSheet var1, MenuItem var2)
    {
        switch (var2.getItemId())
        {
            case R.id.img_picker_camera:
                new MaterialCamera(BaseActivity.this)
                        .stillShot()
                        .startAsResult(true)
                        .defaultToFrontFacing(isSelfie)
                        .saveDir(Utils.getAppHiddenDirectory(BaseActivity.this))
                        .start(CAMERA_PIC_REQUEST);
                break;
            case R.id.img_picker_gallery:
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_PIC_REQUEST);

                break;
        }

    }

    @Override
    public void onSheetDismissed(@NonNull BottomSheet var1, @BottomSheetListener.DismissEvent int var2)
    {

    }
    interface ImagePickerListener{
        void onImageSelect(Intent data);
    }



    public void selectVideo(PickVideoListener pickVideoListener){
        this.pickVideoListener = pickVideoListener;
        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);

        mediaIntent.setType("video/*");// Set MIME type as per requirement
        startActivityForResult(Intent.createChooser(mediaIntent, "Select Video"),AppConstants.INTENT_REQUEST_CODES.VIDEO_REQUEST);
    }


    public void showImagePicker(PickImageListener pickImageListener){
        this.pickImageListener = pickImageListener;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_image_picker, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true);
        final AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        NMGButton mBtnGallery, mBtnCamera;

        mBtnGallery=(NMGButton)mView.findViewById(R.id.galleryBtn);
        mBtnCamera=(NMGButton)mView.findViewById(R.id.cameraBtn);

        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAndroid.dismiss();
                checkForCameraPermission();

            }
        });

        mBtnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAndroid.dismiss();
                checkForGalleryPermission();


            }
        });
        alertDialogAndroid.show();
    }

    public void checkForGalleryPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQ_CODE);
        } else{
            captureImageFromGallery();
        }
    }



    public void checkForCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQ_CODE);
        } else{
            captureImageFromCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQ_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImageFromCamera();
                } else {
                    Toast.makeText(this, R.string.camera_image_capture_permission_required,Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case GALLERY_PERMISSION_REQ_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImageFromGallery();
                } else {
                    Toast.makeText(this, R.string.gallery_permission_required,Toast.LENGTH_SHORT).show();
                }
                return;

        }
    }
    private void captureImageFromCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "recycalize"+System.currentTimeMillis()+".jpeg");
        mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if(mCapturedImageURI==null){
            Logger.error("image uri is null in onClick method");
        }else{
            Logger.error("image uri in onClick method: "+mCapturedImageURI.getPath());
        }
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        cameraIntent.putExtra("return-data", true);
        try {
            startActivityForResult(cameraIntent, AppConstants.INTENT_REQUEST_CODES.CAMERA_REQUEST);
        } catch (Exception exc) {
            //ToastUtils.showToast(activity, getString(com.kelltontech.R.string.error_opening_camera), Toast.LENGTH_LONG);
        }
    }

    private void captureImageFromGallery(){
        Intent intent = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);

        try {
            if (intent != null){
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstants.INTENT_REQUEST_CODES.GALLERY_REQUEST);
            }
        } catch (Exception exc) {
            //ToastUtils.showToast(activity, getString(com.kelltontech.R.string.error_opening_gallery), Toast.LENGTH_LONG);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK) {
            if (requestCode == AppConstants.INTENT_REQUEST_CODES.CAMERA_REQUEST) {
                if(mCapturedImageURI!=null){
                    Logger.error("uri is: "+mCapturedImageURI.getPath());
                    data=new Intent();
                    data.putExtra("uri", mCapturedImageURI);
                }else{
                    data=new Intent();
                    Logger.error("uri is null ");
                }

                if (data != null && data.getExtras() != null) {
                    Uri uri = data.getExtras().getParcelable("uri");
                    if (uri != null) {
                        handleUri(uri);
                    }
                } else {
                    ToastUtils.showToast(this, "Image could not be saved.");
                }
                // getTargetFragment().onActivityResult(getTargetRequestCode(), Constants.RESPONSE_CODES.CAMERA, data);
            } else if (requestCode == AppConstants.INTENT_REQUEST_CODES.GALLERY_REQUEST) {
                // getTargetFragment().onActivityResult(getTargetRequestCode(), Constants.RESPONSE_CODES.GALLERY, data);
                Uri selectedImgFileUri = data.getData();
                if (selectedImgFileUri == null) {
                    return;
                }
                handleUri(selectedImgFileUri);
            }else if (requestCode == AppConstants.INTENT_REQUEST_CODES.VIDEO_REQUEST){
                Uri selectedVideoUri = data.getData();

                String selectedVideoPath = getPath(selectedVideoUri);
                if (selectedVideoPath != null ) {
                    if (Utils.isVideoFile(selectedVideoPath)){
                        byte[] buf = selectedVideoPath.getBytes();
                        pickVideoListener.onVideoSelected(buf);
                    }else{
                        ToastUtils.showToast(this, "Selected file is not a video. Please select a video file");
                    }
                    //Logger.error(selectedVideoPath);
                }else{
                    ToastUtils.showToast(this, "Please select a video file from gallery only.");
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void handleUri(Uri uri) {
        Bitmap bitmap;
        if (uri.getPath().contains(".")) {
            mImagePath=uri.getPath();
            bitmap= BitmapUtils.getScaledBitmapWithLandOrient(uri.getPath(), 800, 800);
            //mIvLawFirm.setImageBitmap(bitmap);
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            mImagePath=Utils.getPath(uri, this);
            bitmap=BitmapUtils.getScaledBitmapWithLandOrient(mImagePath, 800, 800);
           // mIvLawFirm.setImageBitmap(bitmap);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (bitmap.getByteCount() > 9999999) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        } else if (bitmap.getByteCount() > 999999) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        } else {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        }
        byte[] data = bos.toByteArray();
        mImageBase64= Base64.encodeToString(data, Base64.DEFAULT);
        pickImageListener.onImagePicked(bitmap, mImageBase64);

    }

    public interface PickImageListener{
        void onImagePicked(Bitmap bitmap, String imageBase64);
    }

    public interface PickVideoListener{
        void onVideoSelected(byte[] videoByte);
    }

    public void showGuestUserAlert(final Context context){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_guest_user_alert, null);
        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(true);
        final AlertDialog guestUserAlertDialog = alertDialogBuilderUserInput.create();
        NMGButton btnCancel, btnLogin;

        btnCancel = (NMGButton)mView.findViewById(R.id.btn_cancel);
        btnLogin = (NMGButton)mView.findViewById(R.id.btn_login);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestUserAlertDialog.dismiss();

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guestUserAlertDialog.dismiss();
                // to the login page

                Intent intent = new Intent(context, LoginEmailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        guestUserAlertDialog.show();
    }

}
