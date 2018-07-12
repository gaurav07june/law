package com.matterhornlegal.customui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;


/**
 * Created by gaurav.singh on 5/2/2018.
 */

public class NMGAutocompleteTextView extends AppCompatAutoCompleteTextView {

    public NMGAutocompleteTextView(Context context) {
        super(context);
    }

    public NMGAutocompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NMGAutocompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
            showDropDown();
        }
    }
}
