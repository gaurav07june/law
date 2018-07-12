package com.matterhornlegal.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.matterhornlegal.R;
import com.matterhornlegal.adapters.EventListAdapter;
import com.matterhornlegal.models.EventData;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by karan.kalsi on 4/7/2017.
 */
public class EventListDialog {
    private Dialog dialog;
    private RecyclerView eventListView;
    private EventListAdapter eventListAdapter;
    private List<EventData> eventList=new ArrayList<>();
    public EventListDialog(Context context, EventListAdapter.EventListAdapterListener listener)
    {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.event_list_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        eventListView = dialog.findViewById(R.id.even_list_view);
        eventListView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        eventListAdapter=new EventListAdapter(eventList, listener);
        eventListView.setAdapter(eventListAdapter);
    }
public void show(List<EventData> eventList)
{
    try{

        if(dialog==null )return;
        this.eventList.clear();
        this.eventList.addAll(eventList);
        eventListAdapter.notifyDataSetChanged();
        dialog.show();
    }
    catch (Exception e)
    {
    }

}
    public void dismiss()
    {
        try{
        if(dialog==null )return;
        dialog.dismiss();
    }
    catch (Exception e)
    {

    }
    }

    public boolean isShowing()
    {
        return dialog!=null && dialog.isShowing();
    }
}
