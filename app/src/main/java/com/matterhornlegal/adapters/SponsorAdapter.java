package com.matterhornlegal.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.matterhornlegal.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by karan.kalsi on 10/31/2017.
 */

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.SponsorViewHolder> {
List<String> sponsorImgUrls = Arrays.asList(new String[]{
        "file:///android_asset/temp/logo1.jpg",
        "file:///android_asset/temp/logo2.jpg",
        "file:///android_asset/temp/logo3.jpg",
        "file:///android_asset/temp/logo4.jpg",
        "file:///android_asset/temp/logo5.jpg",
        "file:///android_asset/temp/logo6.jpg"


});

    @Override
    public SponsorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SponsorViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(SponsorViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return sponsorImgUrls.size();
    }

    class SponsorViewHolder extends RecyclerView.ViewHolder
    {
        ImageView sponsorView;
        public SponsorViewHolder(View itemView) {
            super(itemView);
            sponsorView= (ImageView) itemView;

        }
        public void bind(int position)
        {
            Glide.with(sponsorView.getContext()).load(sponsorImgUrls.get(position)).into(sponsorView);

             }
    }
}
