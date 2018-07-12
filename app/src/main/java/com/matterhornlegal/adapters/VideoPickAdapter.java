package com.matterhornlegal.adapters;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.databinding.DeviceVideoListItemBinding;
import com.matterhornlegal.databinding.VideoGridItemBinding;
import com.matterhornlegal.databinding.VideoListItemBinding;
import com.matterhornlegal.models.VideoFeedModel;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;

import java.util.ArrayList;
import java.util.List;


public class VideoPickAdapter extends RecyclerView.Adapter<VideoPickAdapter.ViewHolder> {


    private Cursor cursor;
    private Context context;
    private int videoColumnIndex;
    private String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID };
    String thumbPath;
    private VideoPickAdapterListener videoPickAdapterListener;


    public VideoPickAdapter(Cursor cursor, Context context, VideoPickAdapterListener videoPickAdapterListener) {
        this.cursor = cursor;
        this.context = context;
        this.videoPickAdapterListener = videoPickAdapterListener;
    }

    public VideoPickAdapter(){

    }



    @Override
    public VideoPickAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_video_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        DeviceVideoListItemBinding deviceVideoListItemBinding;
        public ViewHolder(View itemView) {
            super(itemView);
            deviceVideoListItemBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            videoColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            cursor.moveToPosition(position);
            deviceVideoListItemBinding.txtTitle.setText(cursor.getString(videoColumnIndex));


            videoColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            cursor.moveToPosition(position);
            deviceVideoListItemBinding.txtSize.setText(" Size(KB):" + cursor.getString(videoColumnIndex));

            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            Cursor videoThumbnailCursor = context.getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                    thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID+ "=" + videoId, null, null);

            if (videoThumbnailCursor.moveToFirst())
            {
                thumbPath = videoThumbnailCursor.getString(videoThumbnailCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                Logger.error("ThumbPath: "+thumbPath);
                deviceVideoListItemBinding.imgIcon.setImageURI(Uri.parse(thumbPath));
            }
        }

        @Override
        public void onClick(View v) {
            videoPickAdapterListener.onVideoClicked(cursor, getLayoutPosition());
        }
    }


    public interface VideoPickAdapterListener{
        void onVideoClicked(Cursor cursor, int position);
    }

}
