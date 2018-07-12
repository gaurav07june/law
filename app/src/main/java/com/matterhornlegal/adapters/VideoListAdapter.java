package com.matterhornlegal.adapters;

import android.databinding.DataBindingUtil;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.databinding.VideoGridItemBinding;
import com.matterhornlegal.databinding.VideoListItemBinding;
import com.matterhornlegal.models.VideoFeedModel;
import com.matterhornlegal.models.VideoModel;
import com.matterhornlegal.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan.kalsi on 10/27/2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private List<VideoFeedModel> videoList = new ArrayList<>();
    private AppConstants.VIEW_TYPE viewType = AppConstants.VIEW_TYPE.GRID;
    private VideoListAdapterListener  videoListAdapterListener;


    public VideoListAdapter(List<VideoFeedModel> videoList, VideoListAdapterListener videoListAdapterListener) {
        this.videoList.clear();
        this.videoList.addAll(videoList);
        this.videoListAdapterListener = videoListAdapterListener;
    }

    public VideoListAdapter(){

    }
    public void setVideoList(List<VideoFeedModel> videoList) {
        this.videoList.clear();
        this.videoList.addAll(videoList);
        notifyDataSetChanged();
    }
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
       switch (this.viewType)
       {
           case GRID:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_grid_item, parent, false);
               break;
           case LIST:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
               break;
           default:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_grid_item, parent, false);
       }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /*TextView video_title;
        ImageView video_thumb;
        TextView user_name;
        ImageView user_image;*/
        VideoGridItemBinding videoGridItemBinding;
        VideoListItemBinding videoListItemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            switch(viewType){
                case GRID:
                    videoGridItemBinding = DataBindingUtil.bind(itemView);
                    break;
                case LIST:
                    videoListItemBinding = DataBindingUtil.bind(itemView);
                    break;
            }
           /* video_title = (TextView) itemView.findViewById(R.id.video_title);
            video_thumb = (ImageView) itemView.findViewById(R.id.video_thumb);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_image = (ImageView) itemView.findViewById(R.id.user_image);*/
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {

            switch (viewType){
                case GRID:
                    videoGridItemBinding.videoTitle.setText(videoList.get(position).getVideo_title());
                    Glide.with(itemView)
                            .load(videoList.get(position).getVideo_thumbnail())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                            .into(videoGridItemBinding.videoThumb);
                    videoGridItemBinding.userName.setText(videoList.get(position).getUser_name());
                    Glide.with(itemView)
                            .load(videoList.get(position).getUser_thumbnail())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                            .into(videoGridItemBinding.userImage);
                    break;
                case LIST:
                    videoListItemBinding.videoTitle.setText(videoList.get(position).getVideo_title());
                    Glide.with(itemView)
                            .load(videoList.get(position).getVideo_thumbnail())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                            .into(videoListItemBinding.videoThumb);
                    videoListItemBinding.userName.setText(videoList.get(position).getUser_name());
                    Glide.with(itemView)
                            .load(videoList.get(position).getUser_thumbnail())
                            .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                            .into(videoListItemBinding.userImage);
                    break;


            }
        }

        @Override
        public void onClick(View v) {
            switch(viewType){
                case GRID:
                    videoListAdapterListener.onVideoClicked(videoGridItemBinding.videoThumb, getLayoutPosition());
                    break;
                case LIST:
                    videoListAdapterListener.onVideoClicked(videoListItemBinding.videoThumb, getLayoutPosition());
                    break;
            }
        }
    }

    public List<VideoFeedModel> getVideoList() {
        return videoList;
    }

    public AppConstants.VIEW_TYPE getViewType() {
        return viewType;
    }

    public void setViewType(AppConstants.VIEW_TYPE viewType) {
        this.viewType = viewType;
    }

    public interface VideoListAdapterListener{
        void onVideoClicked(ImageView imageView, int position);
    }

}
