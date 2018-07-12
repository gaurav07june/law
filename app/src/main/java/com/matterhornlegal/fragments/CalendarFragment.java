package com.matterhornlegal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matterhornlegal.R;
import com.matterhornlegal.utils.EventDecorator;
import com.matterhornlegal.utils.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {
private MaterialCalendarView calendarView;
private OnEventDateSelectedListener onEventDateSelectedListener;
    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
     }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendar_view);
        addEvents(new ArrayList<CalendarDay>());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                if (onEventDateSelectedListener!=null)
                    onEventDateSelectedListener.onEventDateSelected(Utils.getWebDate(date.getDate()));
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public void onAttachToParentFragment(Fragment fragment)
    {
        if (fragment instanceof OnEventDateSelectedListener) {
            onEventDateSelectedListener = (OnEventDateSelectedListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
        onEventDateSelectedListener = null;
    }

public void addEvents(List<CalendarDay> calendarDays)
{
    calendarView.addDecorator(new EventDecorator(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark), calendarDays));
}

public  interface OnEventDateSelectedListener
    {
        void onEventDateSelected(String eventDate);
    }

}
