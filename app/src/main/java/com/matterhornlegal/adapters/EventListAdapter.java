package com.matterhornlegal.adapters;

import android.databinding.DataBindingUtil;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.databinding.MyEventItemBinding;
import com.matterhornlegal.models.EventData;
import com.matterhornlegal.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<EventData> eventDataList = new ArrayList<>();

    private EventListAdapterListener listener;



    public EventListAdapter(List<EventData> eventDataList, EventListAdapterListener listener) {
        this.eventDataList = eventDataList;
        this.listener = listener;
    }

    public void setEventListData(List<EventData> eventDataList) {
        this.eventDataList.clear();
        this.eventDataList.addAll(eventDataList);
        notifyDataSetChanged();
    }
    public void addEventListData(List<EventData> eventDataList) {
        int prevSize=this.eventDataList.size();
        this.eventDataList.addAll(eventDataList);
        notifyItemRangeInserted(prevSize,this.eventDataList.size());
    }

    public List<EventData> getMyEventList() {
        return eventDataList;
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        /*ImageView my_event_thumb;
        TextView my_event_title;
        TextView my_event_date;
        View is_important_badge;
        View is_approved_badge;*/
        MyEventItemBinding myEventItemBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            myEventItemBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEventClicked(myEventItemBinding.myEventThumb, eventDataList.get(getAdapterPosition()));
                }
            });
           /* my_event_thumb = (ImageView) itemView.findViewById(R.id.my_event_thumb);
            my_event_title = (TextView) itemView.findViewById(R.id.my_event_title);
            my_event_date = (TextView) itemView.findViewById(R.id.my_event_date);
            is_important_badge = itemView.findViewById(R.id.is_important_badge);
            is_approved_badge = itemView.findViewById(R.id.is_approved_badge);*/

        }

        public void bind(int position) {
            myEventItemBinding.myEventTitle.setText(eventDataList.get(position).getEvent_title());
            Glide.with(itemView)
                    .load(eventDataList.get(position).getEvent_image())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.placeholder_rect, null)))
                    .into(myEventItemBinding.myEventThumb);
            String eventStartDate = Utils.getFormatedDateTime(eventDataList.get(position).getEvent_start_date());
            String eventEndDate = Utils.getFormatedDateTime(eventDataList.get(position).getEvent_end_date());
            myEventItemBinding.myEventDate.setText(String.format(itemView.getResources().getString(R.string.event_date_range),
                    eventStartDate, eventEndDate));
            /*is_approved_badge.setVisibility(View.GONE);

            if (myEventList.get(position).isImportant())
            {
                is_important_badge.setVisibility(View.VISIBLE);
            }
            else
            {
                is_important_badge.setVisibility(View.GONE);
            }
         //   my_event_date.setText(myEventList.get(position).getDate());
            }*/
        }

    }

    public interface EventListAdapterListener{
        void onEventClicked( ImageView imageView, EventData eventData);
    }

}
