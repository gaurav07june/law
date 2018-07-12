package com.matterhornlegal.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.matterhornlegal.R;
import com.matterhornlegal.models.BlogModel;
import com.matterhornlegal.models.EventModel;
import com.matterhornlegal.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan.kalsi on 10/27/2017.
 */

public class MyEventListAdapter extends RecyclerView.Adapter<MyEventListAdapter.ViewHolder> {

    private List<EventModel> myEventList = new ArrayList<>();

                private AppConstants.VIEW_TYPE viewType = AppConstants.VIEW_TYPE.GRID;


    public MyEventListAdapter(List<EventModel> myEventList) {
        this.myEventList.clear();
        this.myEventList.addAll(myEventList);
    }

    public void setMyEventListList(List<EventModel> myEventList) {
        this.myEventList.clear();
        this.myEventList.addAll(myEventList);
        notifyDataSetChanged();
    }

    public List<EventModel> getMyEventList() {
        return myEventList;
    }

    @Override
    public MyEventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
       switch (this.viewType)
       {
           case GRID:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_event_item, parent, false);
               break;
           case LIST:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_event_item, parent, false);
               break;
           default:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_event_item, parent, false);
       }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return myEventList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView my_event_thumb;
        TextView my_event_title;
        TextView my_event_date;
        View is_important_badge;
        View is_approved_badge;


        public ViewHolder(View itemView) {
            super(itemView);

            my_event_thumb = (ImageView) itemView.findViewById(R.id.my_event_thumb);
            my_event_title = (TextView) itemView.findViewById(R.id.my_event_title);
            my_event_date = (TextView) itemView.findViewById(R.id.my_event_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public void bind(int position) {
            my_event_title.setText(myEventList.get(position).getTitle());
            Glide.with(itemView).load(myEventList.get(position).getImg()).into(my_event_thumb);

            if (myEventList.get(position).isApproved())
            {
                is_approved_badge.setSelected(true);
            }
            else
            {
                is_approved_badge.setSelected(false);
            }

            if (myEventList.get(position).isImportant())
            {
                is_important_badge.setVisibility(View.VISIBLE);
            }
            else
            {
                is_important_badge.setVisibility(View.GONE);
            }
         //   my_event_date.setText(myEventList.get(position).getDate());
            }
    }

    public AppConstants.VIEW_TYPE getViewType() {
        return viewType;
    }

    public void setViewType(AppConstants.VIEW_TYPE viewType) {
        this.viewType = viewType;
    }

}
