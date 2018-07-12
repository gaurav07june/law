package com.matterhornlegal.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matterhornlegal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeHeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHeaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE = "image";

    // TODO: Rename and change types of parameters
    private int mTitleRes;
    private int mImageRes;


    public HomeHeaderFragment() {
        // Required empty public constructor
    }
    public static HomeHeaderFragment newInstance(int title_res, int image_res) {
        HomeHeaderFragment fragment = new HomeHeaderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title_res);
        args.putInt(ARG_IMAGE, image_res);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitleRes = getArguments().getInt(ARG_TITLE);
            mImageRes = getArguments().getInt(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view  = inflater.inflate(R.layout.fragment_home_header, container, false);
        TextView header_title = (TextView) view.findViewById(R.id.home_header_title);
        ImageView header_img = (ImageView) view.findViewById(R.id.home_header_img);
        header_title.setText(mTitleRes);
        header_img.setImageResource(mImageRes);
        return view;
    }

}
