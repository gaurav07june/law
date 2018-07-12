package com.matterhornlegal.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matterhornlegal.R;
import com.matterhornlegal.customui.LiveStreamItemView;


/**
 * Created by karan.kalsi on 10/31/2017.
 */
public class LiveStreamEventAdapter extends RecyclerView.Adapter<LiveStreamEventAdapter.LiveStreamHolder> {


    @Override
    public LiveStreamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from(parent.getContext()).inflate(R.layout.live_stream_event_view,parent,false)
        return new LiveStreamHolder(new LiveStreamItemView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(LiveStreamHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class LiveStreamHolder extends RecyclerView.ViewHolder {
        private LiveStreamItemView liveStreamItemView;

        public LiveStreamHolder(View itemView) {
            super(itemView);
            liveStreamItemView = (LiveStreamItemView) itemView;
        }

        public void bind(int position) {
            int day = 5 + position;
            if (position == 0) {
                liveStreamItemView.setTitle(liveStreamItemView.getContext().getString(R.string.next_event));
            } else if (position == 1) {
                liveStreamItemView.setTitle(liveStreamItemView.getContext().getString(R.string.upcoming_events));
            } else {
                liveStreamItemView.setTitle("");
            }

            liveStreamItemView.setDummyValue("file:///android_asset/temp/live_stream_pic.jpg", day + "/11/2017 10:00:00");

        }


    }
}
