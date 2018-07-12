package com.matterhornlegal.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matterhornlegal.R;
import com.matterhornlegal.adapters.LiveStreamEventAdapter;
import com.matterhornlegal.databinding.FragmentLiveStreamEventsBinding;
import com.matterhornlegal.databinding.FragmentMyEventsBinding;
import com.matterhornlegal.utils.SpacesItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LiveStreamEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LiveStreamEventsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentLiveStreamEventsBinding binding = null;

    public LiveStreamEventsFragment() {
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
    public static LiveStreamEventsFragment newInstance(String param1, String param2) {
        LiveStreamEventsFragment fragment = new LiveStreamEventsFragment();
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

       binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_live_stream_events, container, false);
        View view = binding.getRoot();
        binding.liveStreamEventsList.addItemDecoration
                (new SpacesItemDecoration(getActivity().getResources().getDimensionPixelOffset(R.dimen._15sdp)));
        binding.liveStreamEventsList.setNestedScrollingEnabled(false);
        binding.liveStreamEventsList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.liveStreamEventsList.setAdapter(new LiveStreamEventAdapter());
        binding.liveStreamEventToolbar.backBtn.setOnClickListener(this);
        binding.liveStreamEventToolbar.title.setText(R.string.live_stream_events);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back_btn:getActivity().onBackPressed();break;
        }
    }


}
