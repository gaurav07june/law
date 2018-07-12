package com.matterhornlegal.customui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/**
 * Created by gaurav.singh on 5/14/2018.
 */

public class NMGMapView extends MapView {
    public NMGMapView(Context context) {
        super(context);
    }

    public NMGMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NMGMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public NMGMapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {

            case MotionEvent.ACTION_MOVE:
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }

        // Handle MapView's touch events.
        super.onTouchEvent(ev);
        return true;
    }
}
