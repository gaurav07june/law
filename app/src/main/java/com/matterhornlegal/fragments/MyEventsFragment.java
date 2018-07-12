package com.matterhornlegal.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matterhornlegal.R;
import com.matterhornlegal.adapters.BlogListAdapter;
import com.matterhornlegal.adapters.MyEventListAdapter;
import com.matterhornlegal.databinding.FragmentBlogsBinding;
import com.matterhornlegal.databinding.FragmentMyEventsBinding;
import com.matterhornlegal.models.BlogModel;
import com.matterhornlegal.models.EventModel;
import com.matterhornlegal.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyEventsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentMyEventsBinding binding = null;
    private LinearLayoutManager listManager;
    private GridLayoutManager gridManager;
    private MyEventListAdapter eventListAdapter;
    private boolean isNewestFirst=true;

    public MyEventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyEventsFragment newInstance(String param1, String param2) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


       binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_my_events, container, false);
        View view = binding.getRoot();

      //  binding.videosRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        eventListAdapter=new MyEventListAdapter(getEvents());
        gridManager=new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false);
        listManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        setListView();

        binding.myEventsToolbar.title.setText(R.string.blogs);


        binding.myEventsToolbar.backBtn.setOnClickListener(this);

        return view;
    }

    public List<EventModel> getEvents() {

        List<EventModel> eventList = new ArrayList<>();
        EventModel event1 = new EventModel();
        event1.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        event1.setImg("file:///android_asset/temp/blog1.jpg");
        event1.setApproved(true);
        event1.setImportant(true);

        EventModel event2 = new EventModel();
        event2.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        event2.setImg("file:///android_asset/temp/blog2.jpg");



        eventList.add(event1);
        eventList.add(event2);




        return eventList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:getActivity().onBackPressed();break;
        }
    }


    private void setGridView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        eventListAdapter.setViewType(AppConstants.VIEW_TYPE.GRID);
        binding.myEventsRecycler.setLayoutManager(gridManager);
        binding.myEventsRecycler.setAdapter(eventListAdapter);
    }
    private void setListView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        eventListAdapter.setViewType(AppConstants.VIEW_TYPE.LIST);
        binding.myEventsRecycler.setLayoutManager(listManager);
        binding.myEventsRecycler.setAdapter(eventListAdapter);
    }
}
