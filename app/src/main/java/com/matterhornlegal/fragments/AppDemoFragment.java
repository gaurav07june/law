package com.matterhornlegal.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.matterhornlegal.R;
import com.matterhornlegal.models.DemoPageScrollEvent;
import com.matterhornlegal.utils.AppConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppDemoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppDemoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppDemoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DEMO_TITLE = "demo_title";
    private static final String DEMO_DESC = "demo_desc";
    private static final String DEMO_LOGO = "demo_logo";
    private static final String POSITION = "position";

    // TODO: Rename and change types of parameters
    private int mDemoTitle;
    private int mDemoDesc;
    private int mDemoLogo;
    private int mPosition;
    private ImageView demoLogoIv;
    private OnFragmentInteractionListener mListener;

    public AppDemoFragment() {
        // Required empty public constructor
    }

    public static AppDemoFragment newInstance(int position) {
        AppDemoFragment fragment = new AppDemoFragment();
        Bundle args = new Bundle();

        args.putInt(DEMO_TITLE, AppConstants.DEMO_PAGES.DEMO_TITLE[position]);
        args.putInt(DEMO_DESC, AppConstants.DEMO_PAGES.DEMO_DESC[position]);
        args.putInt(DEMO_LOGO, AppConstants.DEMO_PAGES.DEMO_LOGO[position]);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDemoTitle = getArguments().getInt(DEMO_TITLE);
            mDemoDesc = getArguments().getInt(DEMO_DESC);
            mDemoLogo = getArguments().getInt(DEMO_LOGO);
            mPosition = getArguments().getInt(POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_demo, container, false);

        TextView demoTitleTv = (TextView) view.findViewById(R.id.demo_title);
        TextView demoDescTv = (TextView) view.findViewById(R.id.demo_desc);
         demoLogoIv = (ImageView) view.findViewById(R.id.demo_logo);

        demoTitleTv.setText(mDemoTitle);
        demoDescTv.setText(mDemoDesc);
        demoLogoIv.setImageResource(mDemoLogo);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    /*    if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
