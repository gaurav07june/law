package com.matterhornlegal.customui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.matterhornlegal.R;
import com.matterhornlegal.adapters.SponsorAdapter;
import com.matterhornlegal.databinding.LiveStreamEventViewBinding;
import com.matterhornlegal.utils.DateUtils;
import com.matterhornlegal.utils.SpacesItemDecoration;

/**
 * Created by karan.kalsi on 10/9/2017.
 */

public class LiveStreamItemView extends RelativeLayout {
    public LiveStreamItemView(Context context) {
        super(context);
        init();

    }

    public LiveStreamItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveStreamItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

  private LiveStreamEventViewBinding binding;
    private void init() {
        inflate(getContext(), R.layout.live_stream_event_view, this);
        binding= DataBindingUtil.bind(getChildAt(0));
    }

    public void setDummyValue(String imgUrl,String date)
    {

        binding.liveEventCdownView.startTimer(DateUtils.getRemainingMillis(date));
        Glide.with(this).load(imgUrl).into(binding.liveEventImage);
        binding.sponsorList.setLayoutManager(new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false));

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen._8sdp);
        binding.sponsorList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        binding.sponsorList.setNestedScrollingEnabled(false);
        binding.sponsorList.setAdapter(new SponsorAdapter());
    }
    public void setTitle(String title)
    {
        if(title.isEmpty())
        {
            binding.streamTitle.setVisibility(GONE);
        }
        else
        {
            binding.streamTitle.setText(title);
            binding.streamTitle.setVisibility(VISIBLE);
        }

    }



}
