package com.matterhornlegal.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.matterhornlegal.R;
import com.matterhornlegal.activities.BaseActivity;
import com.matterhornlegal.activities.VideoDetailActivity;

import com.matterhornlegal.adapters.VideoListAdapter;
import com.matterhornlegal.databinding.FragmentMyVideosBinding;
import com.matterhornlegal.models.AppGlobalData;

import com.matterhornlegal.models.VideoFeedModel;
import com.matterhornlegal.models.VideoFeedResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyVideosFragment extends BaseFragment implements  View.OnClickListener, VideoListAdapter.VideoListAdapterListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentMyVideosBinding binding = null;
    private LinearLayoutManager listManager;
    private GridLayoutManager gridManager;
    private VideoListAdapter videoListAdapter;
    private List<VideoFeedModel> videoListToShow;
    private boolean isLoading = false;
    private int totalPage = 1;
    private int currentPage = 1;
    private boolean isCurrentActivityCall = true;


    public MyVideosFragment() {
    }
    public static MyVideosFragment newInstance(String param1, String param2) {
        MyVideosFragment fragment = new MyVideosFragment();
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
                inflater, R.layout.fragment_my_videos, container, false);
        View view = binding.getRoot();
        initViews();
        setListener();
        return view;
    }

    private void setListener() {
        binding.videosRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = 0;
                if (videoListAdapter.getViewType() == AppConstants.VIEW_TYPE.GRID){
                    lastVisibleItemPosition = gridManager.findLastVisibleItemPosition();
                }else if (videoListAdapter.getViewType() == AppConstants.VIEW_TYPE.LIST){
                    lastVisibleItemPosition = listManager.findLastVisibleItemPosition();
                }
                if (lastVisibleItemPosition== videoListAdapter.getItemCount() - 1) {

                    if (!isLoading && (currentPage <= totalPage)) {
                        getData(ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY);

                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.videosToolbar.title.setText(R.string.my_video_gallery);
        binding.videosColumnToggleToolbar.gridTypeBtn.setOnClickListener(this);
        binding.videosColumnToggleToolbar.listTypeBtn.setOnClickListener(this);
        binding.videosToolbar.backBtn.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterEdtCountry.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterEdtIndustrySector.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterEdtPracticeArea.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterBtnApply.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterBtnClearall.setOnClickListener(this);
        binding.videosToolbar.backBtn.setOnClickListener(this);

    }

    private void initViews() {
        isCurrentActivityCall = true;
        videoListToShow = new ArrayList<>();
        getData(ApiUtils.ApiActionEvents.GET_MY_VIDEO_GALLERY);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.grid_type_btn:
                if (!(videoListAdapter.getViewType() == AppConstants.VIEW_TYPE.GRID)){
                    setGridView();
                }

                break;
            case R.id.list_type_btn:
                if (!(videoListAdapter.getViewType() == AppConstants.VIEW_TYPE.LIST)){
                    setListView();
                }
                break;

            case R.id.back_btn:
                getActivity().onBackPressed();
                break;

        }
    }



    private void setGridView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.videosColumnToggleToolbar.gridTypeBtn.setSelected(true);
        binding.videosColumnToggleToolbar.listTypeBtn.setSelected(false);

        videoListAdapter.setViewType(AppConstants.VIEW_TYPE.GRID);
        binding.videosRecycler.setLayoutManager(gridManager);
        binding.videosRecycler.setAdapter(videoListAdapter);
        Utils.runLayoutAnimation(binding.videosRecycler);

    }
    private void setListView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.videosColumnToggleToolbar.gridTypeBtn.setSelected(false);
        binding.videosColumnToggleToolbar.listTypeBtn.setSelected(true);

        videoListAdapter.setViewType(AppConstants.VIEW_TYPE.LIST);
        binding.videosRecycler.setLayoutManager(listManager);
        binding.videosRecycler.setAdapter(videoListAdapter);
        Utils.runLayoutAnimation(binding.videosRecycler);

    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_MY_VIDEO_GALLERY:
                isLoading = true;
               if (getActivity() == null){
                   return;
               }
                if (((BaseActivity)getActivity()).progressDialog != null && !((BaseActivity)getActivity()).progressDialog.isShowing()){
                    ((BaseActivity)getActivity()).progressDialog.show();
                }
                String videoListPayload = ApiUtils.getVideoFeedPayload(currentPage).toString();
                Map<String, String> myVideoHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());

                RequestManager.addRequest(
                    new GsonObjectRequest<VideoFeedResponseModel>(
                            ApiUtils.ApiUrl.GET_MY_VIDEO_GALLERY_URL,myVideoHeader, videoListPayload, VideoFeedResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {
                        @Override
                        protected void deliverResponse(VideoFeedResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("video gallery response: " + data);

                            if (response.getStatus() == 1) {
                                updateUi(true, actionID, response, 200);
                            } else {
                                if (Utils.isNullOrEmpty(response.getMessage())) {
                                    updateUi(false, actionID, getString(R.string.errorGeneric), 200);
                                } else {
                                    updateUi(false, actionID, response.getMessage(), 200);
                                }
                            }
                        }
                    });
                break;


        }

    }
    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_MY_VIDEO_GALLERY:
                isLoading = false;

                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (status) {
                    if (serviceResponse instanceof VideoFeedResponseModel) {
                        VideoFeedResponseModel responseModel = (VideoFeedResponseModel) serviceResponse;
                        totalPage = responseModel.getTotal_pages();
                        if (responseModel.getData() != null){
                            videoListToShow.addAll(responseModel.getData());
                        }

                        if (videoListToShow.size() == 0){
                            binding.txtNoDataFound.setVisibility(View.VISIBLE);
                            binding.videosRecycler.setVisibility(View.GONE);
                        }else{
                            binding.txtNoDataFound.setVisibility(View.GONE);
                            binding.videosRecycler.setVisibility(View.VISIBLE);
                        }

                        if (currentPage == 1){

                            videoListAdapter=new VideoListAdapter(videoListToShow, this);
                            gridManager=new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
                            listManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            setGridView();
                        }else{
                            videoListAdapter.setVideoList(videoListToShow);
                        }
                        currentPage++;
                    }
                } else if (statusCode == 401) {

                } else if (serviceResponse instanceof String) {
                    ToastUtils.showToast(getActivity(), (String) serviceResponse);
                } else {
                    ToastUtils.showToast(getActivity(), getString(R.string.errorGeneric));
                }
                break;
        }
    }

    @Override
    public void onVideoClicked(ImageView imageView, int position) {

        //Toast.makeText(getActivity(), "clicked on "+position, Toast.LENGTH_SHORT).show();
        if (((BaseActivity)getActivity()).progressDialog != null && !((BaseActivity)getActivity()).progressDialog.isShowing()){
            ((BaseActivity)getActivity()).progressDialog.show();
            isCurrentActivityCall = false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(getActivity(), VideoDetailActivity.class);

            intent.putExtra(AppConstants.IntentConstants.VIDEO_ID, videoListToShow.get(position).getVideo_id());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), imageView, getActivity().getResources().getString(R.string.transition_name));
            startActivity(intent, options.toBundle());
        }else{
            Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
            intent.putExtra(AppConstants.IntentConstants.VIDEO_ID, videoListToShow.get(position).getVideo_id());
            startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (((BaseActivity)getActivity()).progressDialog.isShowing() && !isCurrentActivityCall && ((BaseActivity)getActivity()).progressDialog != null){
            ((BaseActivity)getActivity()).progressDialog.dismiss();
        }
    }
}
