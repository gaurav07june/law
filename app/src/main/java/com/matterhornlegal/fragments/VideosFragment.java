package com.matterhornlegal.fragments;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.matterhornlegal.R;
import com.matterhornlegal.activities.BaseActivity;
import com.matterhornlegal.activities.VideoDetailActivity;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.adapters.VideoListAdapter;
import com.matterhornlegal.databinding.FragmentVideosBinding;
import com.matterhornlegal.models.DataModel;
import com.matterhornlegal.models.IndustryListResponseModel;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.models.PracticeAreaListResponseModel;
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
public class VideosFragment extends BaseFragment implements Animator.AnimatorListener, View.OnClickListener, VideoListAdapter.VideoListAdapterListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentVideosBinding binding = null;
    private LinearLayoutManager listManager;
    private GridLayoutManager gridManager;
    private VideoListAdapter videoListAdapter;
    private List<VideoFeedModel> videoListToShow;
    private List<DataModel> industrySectorList;
    private List<DataModel> practiceAreaList;
    private List<DataModel> locationList;
    private ArrayAdapter<String> industrySectorAdapter;
    private ArrayAdapter<String> practiceAreaAdapter;
    private String industySectorId = "";
    private String practiceAreaId = "";
    private String countryLocation = "";
    private boolean isLoading = false;
    private int totalPage = 1;
    private int currentPage = 1;
    private boolean isFilterView = false;
    private boolean isFilterApplied = false;
    private boolean isPerformingSearch = false;
    private boolean isSearchPerformed =false;
    private boolean isSearchView = false;

    private float width;
    private Display display;
    private Point point;
    private ObjectAnimator leftToRight, rightToLeft;
    private String searchText = "";
    private SimpleSpinnerAdapter countySpinnerAdapter, practiceAreaSpinnerAdapter, industrySectorSpinnerAdapter;
    private boolean galleryData, countryListData, practiceAreaData, industrySectorData;

    public VideosFragment() {
    }
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
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
                inflater, R.layout.fragment_videos, container, false);
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
        binding.videoFilterLayout.videoFilterSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.videoFilterLayout.videoFilterEdtCountry.setText(locationList.get(position - 1).getName());
                    countryLocation = Integer.toString(locationList.get(position - 1).getId());
                } else {
                    binding.videoFilterLayout.videoFilterEdtCountry.setText("");
                    countryLocation = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.videoFilterLayout.spinnerIndustryArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.videoFilterLayout.videoFilterEdtIndustrySector.setText(industrySectorList.get(position - 1).getName());
                    industySectorId = Integer.toString(industrySectorList.get(position - 1).getId());
                }else{
                    binding.videoFilterLayout.videoFilterEdtIndustrySector.setText("");
                    industySectorId = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.videoFilterLayout.spinnerPracticeArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.videoFilterLayout.videoFilterEdtPracticeArea.setText(practiceAreaList.get(position - 1).getName());
                    practiceAreaId = Integer.toString(practiceAreaList.get(position - 1).getId());
                }else {
                    binding.videoFilterLayout.videoFilterEdtPracticeArea.setText("");
                    practiceAreaId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.videosToolbar.toolbarEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        binding.videosToolbar.title.setText(R.string.video_gallery);
        binding.videosColumnToggleToolbar.filterBtn.setOnClickListener(this);
        binding.videosColumnToggleToolbar.gridTypeBtn.setOnClickListener(this);
        binding.videosColumnToggleToolbar.listTypeBtn.setOnClickListener(this);
        binding.videosToolbar.backBtn.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterEdtCountry.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterEdtIndustrySector.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterEdtPracticeArea.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterBtnApply.setOnClickListener(this);
        binding.videoFilterLayout.videoFilterBtnClearall.setOnClickListener(this);
        binding.videosToolbar.backBtn.setOnClickListener(this);
        binding.videosToolbar.searchBtn.setOnClickListener(this);
        binding.videosToolbar.cancelBtn.setOnClickListener(this);
        rightToLeft.addListener(this);
        leftToRight.addListener(this);

    }

    private void initViews() {
        isFilterView = false;
        isFilterApplied = false;
        isPerformingSearch = false;
        isSearchPerformed = false;
        isSearchView = false;


        galleryData = false;
        countryListData = false;
        practiceAreaData= false;
        industrySectorData = false;
        display = getActivity().getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        width = (point.x) - getActivity().getResources().getDimension(R.dimen._45sdp);
        rightToLeft = ObjectAnimator.ofFloat(binding.videosToolbar.searchBtn, "translationX", -width);
        leftToRight = ObjectAnimator.ofFloat(binding.videosToolbar.searchBtn, "translationX", 0);
        videoListToShow = new ArrayList<>();
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY);

        // hit the location, practice area and industry name apis
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_LOCATION_LIST);
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_INDUSTRY_LIST);
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_PRACTICE_LIST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.grid_type_btn:
                if (!(videoListAdapter.getViewType() == AppConstants.VIEW_TYPE.GRID) || isFilterView){
                    setGridView();
                }

                break;
            case R.id.list_type_btn:
                if (!(videoListAdapter.getViewType() == AppConstants.VIEW_TYPE.LIST) || isFilterView){
                    setListView();
                }
                break;
            case R.id.filter_btn:
                if (!isFilterView){
                    setFilterView();
                }

                break;
            case R.id.back_btn:
                getActivity().onBackPressed();
                break;
            case R.id.video_filter_edt_country:
                binding.videoFilterLayout.videoFilterSpinnerCountry.performClick();
                break;
            case R.id.video_filter_edt_practice_area:
                binding.videoFilterLayout.spinnerPracticeArea.performClick();
                break;
            case R.id.video_filter_edt_industry_sector:
                binding.videoFilterLayout.spinnerIndustryArea.performClick();
                break;
            case R.id.video_filter_btn_apply:
                applyFilter();
                break;
            case R.id.video_filter_btn_clearall:
                clearFilter();
                break;
            case R.id.search_btn:
                if (isSearchView){
                    performSearch();
                }else{
                    drawSearchToolbar();
                }
                break;
            case R.id.cancel_btn:
                drawOriginalToolbar();
                break;
        }
    }

    private void setFilterView() {
        binding.filterLayoutContainer.setVisibility(View.VISIBLE);
        binding.videosColumnToggleToolbar.gridTypeBtn.setSelected(false);
        binding.videosColumnToggleToolbar.listTypeBtn.setSelected(false);
        binding.videosColumnToggleToolbar.filterBtn.setSelected(true);
        isFilterView = true;
    }

    private void setGridView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.videosColumnToggleToolbar.gridTypeBtn.setSelected(true);
        binding.videosColumnToggleToolbar.listTypeBtn.setSelected(false);
        binding.videosColumnToggleToolbar.filterBtn.setSelected(false);
        videoListAdapter.setViewType(AppConstants.VIEW_TYPE.GRID);
        binding.videosRecycler.setLayoutManager(gridManager);
        binding.videosRecycler.setAdapter(videoListAdapter);
        Utils.runLayoutAnimation(binding.videosRecycler);
        isFilterView = false;
    }
    private void setListView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.videosColumnToggleToolbar.gridTypeBtn.setSelected(false);
        binding.videosColumnToggleToolbar.listTypeBtn.setSelected(true);
        binding.videosColumnToggleToolbar.filterBtn.setSelected(false);
        videoListAdapter.setViewType(AppConstants.VIEW_TYPE.LIST);
        binding.videosRecycler.setLayoutManager(listManager);
        binding.videosRecycler.setAdapter(videoListAdapter);
        Utils.runLayoutAnimation(binding.videosRecycler);
        isFilterView = false;
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY:
                isLoading = true;
                galleryData = false;
                if (((BaseActivity)getActivity()).progressDialog != null && !((BaseActivity)getActivity()).progressDialog.isShowing()){
                    ((BaseActivity)getActivity()).progressDialog.show();
                }
                String videoListPayload = "";
                String videoListUrl = "";
                if (isFilterApplied){
                    videoListPayload = ApiUtils.getFilteredVideoListPayload(industySectorId, practiceAreaId, countryLocation, Integer.toString(currentPage)).toString();
                    videoListUrl = ApiUtils.ApiUrl.GET_VIDEO_FILTER;
                }else if (isPerformingSearch){
                    videoListPayload = null;
                    videoListUrl = (ApiUtils.ApiUrl.BASE_URL+"firms/videoSearch?search="+searchText+"&page="+currentPage).replaceAll(" ", "%20");
                }else{
                    videoListPayload = ApiUtils.getVideoFeedPayload(currentPage).toString();
                    videoListUrl = ApiUtils.ApiUrl.GET_VIDEO_GALLERY;
                }

                RequestManager.addRequest(
                        new GsonObjectRequest<VideoFeedResponseModel>(
                                videoListUrl, videoListPayload, VideoFeedResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {
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

            case ApiUtils.ApiActionEvents.GET_VIDEO_LOCATION_LIST:

                if (((BaseActivity)getActivity()).progressDialog != null && !((BaseActivity)getActivity()).progressDialog.isShowing()){
                    ((BaseActivity)getActivity()).progressDialog.show();
                }
                countryListData = false;
                RequestManager.addRequest(
                    new GsonObjectRequest<LocationListResponseModel>(
                            ApiUtils.ApiUrl.GET_VIDEO_LOCATION_LIST_URL, null, LocationListResponseModel.class,
                            new VolleyErrorListener(this, getActivity(), actionID)) {

                        @Override
                        protected void deliverResponse(LocationListResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("location response: " + data);
                            if (response.getStatus() == 1 && response != null) {
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
            case ApiUtils.ApiActionEvents.GET_VIDEO_INDUSTRY_LIST:
                if (((BaseActivity)getActivity()).progressDialog != null && !((BaseActivity)getActivity()).progressDialog.isShowing()){
                    ((BaseActivity)getActivity()).progressDialog.show();
                }
                industrySectorData = false;
                RequestManager.addRequest(
                    new GsonObjectRequest<IndustryListResponseModel>(
                            ApiUtils.ApiUrl.GET_VIDEO_INDUSTRY_LIST_URL, null, IndustryListResponseModel.class,
                            new VolleyErrorListener(this, getActivity(), actionID)) {

                        @Override
                        protected void deliverResponse(IndustryListResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("industry response: " + data);
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
            case ApiUtils.ApiActionEvents.GET_VIDEO_PRACTICE_LIST:
                if (((BaseActivity)getActivity()).progressDialog != null && !((BaseActivity)getActivity()).progressDialog.isShowing()){
                    ((BaseActivity)getActivity()).progressDialog.show();
                }
                practiceAreaData = false;
                RequestManager.addRequest(
                    new GsonObjectRequest<PracticeAreaListResponseModel>(
                            ApiUtils.ApiUrl.GET_VIDEO_PRACTICE_LIST_URL, null, PracticeAreaListResponseModel.class,
                            new VolleyErrorListener(this, getActivity(), actionID)) {

                        @Override
                        protected void deliverResponse(PracticeAreaListResponseModel response) {
                            String data = new String(mResponse.data);
                            Logger.error("practice response: " + data);
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
            case ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY:
                isLoading = false;
                galleryData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && galleryData &&
                            countryListData && practiceAreaData && industrySectorData){
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

            case ApiUtils.ApiActionEvents.GET_VIDEO_LOCATION_LIST:
                countryListData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && galleryData &&
                            countryListData && practiceAreaData && industrySectorData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof LocationListResponseModel){
                    LocationListResponseModel responseModel = (LocationListResponseModel) serviceResponse;
                    // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel.getData() != null){
                        locationList = responseModel.getData();
                        setCountrySpinner(locationList);
                    }
                }
                break;
            case ApiUtils.ApiActionEvents.GET_VIDEO_PRACTICE_LIST:
                practiceAreaData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && galleryData &&
                            countryListData && practiceAreaData && industrySectorData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }

                if (serviceResponse instanceof PracticeAreaListResponseModel){
                    PracticeAreaListResponseModel responseModel = (PracticeAreaListResponseModel) serviceResponse;
                    //ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel.getData() != null){
                        practiceAreaList = responseModel.getData();
                        setPracticeAreaSpinner(practiceAreaList);
                    }

                }
                break;
            case ApiUtils.ApiActionEvents.GET_VIDEO_INDUSTRY_LIST:
                industrySectorData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && galleryData &&
                            countryListData && practiceAreaData && industrySectorData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof IndustryListResponseModel){
                    IndustryListResponseModel responseModel = (IndustryListResponseModel) serviceResponse;
                    // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel.getData() != null ){
                        industrySectorList = responseModel.getData();
                        setIndustrySectorSpinner(industrySectorList);
                    }

                }
                break;
        }
    }

    @Override
    public void onVideoClicked(ImageView imageView, int position) {

        //Toast.makeText(getActivity(), "clicked on "+position, Toast.LENGTH_SHORT).show();
        if (getActivity() == null){
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
            intent.putExtra(AppConstants.IntentConstants.VIDEO_DATA, videoListToShow.get(position));
            intent.putExtra(AppConstants.IntentConstants.VIDEO_ID, videoListToShow.get(position).getVideo_id());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), imageView, getActivity().getResources().getString(R.string.transition_name));
            startActivity(intent, options.toBundle());
        }else{
            Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
            intent.putExtra(AppConstants.IntentConstants.VIDEO_DATA, videoListToShow.get(position));
            intent.putExtra(AppConstants.IntentConstants.VIDEO_ID, videoListToShow.get(position).getVideo_id());
            startActivity(intent);
        }

    }
    private void setIndustrySectorSpinner(List<DataModel> industrySectorsList) {
        String[] industryNameList = new String[industrySectorsList.size() + 1];
        industryNameList[0] = "Select industry sector";
        //ArrayList<String> industryNameList = new ArrayList<>();
        for (int i = 0; i < industrySectorsList.size(); i++){
            industryNameList[i + 1] = industrySectorsList.get(i).getName();
        }
        industrySectorSpinnerAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.simple_spinner_row, industryNameList);
        binding.videoFilterLayout.spinnerIndustryArea.setAdapter(industrySectorSpinnerAdapter);
    }

   /* private MultiSpinner.MultiSpinnerListener onIndustrySectorSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            String industrySector = "";
            industySectorId="";

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    if (Utils.isNullOrEmpty(industrySector)) {
                        industrySector += industrySectorList.get(i).getName();
                        industySectorId+=industrySectorList.get(i).getId();
                    } else {
                        industrySector += "," + industrySectorList.get(i).getName();
                        industySectorId+=","+industrySectorList.get(i).getId();
                    }
                }
            }
            binding.videoFilterLayout.videoFilterEdtIndustrySector.setText(industrySector);
        }
    };*/
    private void setPracticeAreaSpinner(List<DataModel> practiceAreaList) {
        String[] practiceNameList = new String[practiceAreaList.size() + 1];
        practiceNameList[0] = "Select practice area";
       for (int i = 0; i< practiceAreaList.size(); i++){
           practiceNameList[i+1] = practiceAreaList.get(i).getName();
       }
        practiceAreaSpinnerAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.simple_spinner_row, practiceNameList);
        binding.videoFilterLayout.spinnerPracticeArea.setAdapter(practiceAreaSpinnerAdapter);

    }

   /* private MultiSpinner.MultiSpinnerListener onPracticeAreaSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            String practiceArea = "";
            practiceAreaId="";
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    if (Utils.isNullOrEmpty(practiceArea)) {
                        practiceArea+= practiceAreaList.get(i).getName();
                        practiceAreaId+= practiceAreaList.get(i).getId();
                    } else {
                        practiceArea += "," + industrySectorList.get(i).getName();
                        practiceAreaId+=","+industrySectorList.get(i).getId();
                    }
                }
            }
            binding.videoFilterLayout.videoFilterEdtPracticeArea.setText(practiceArea);
        }
    };*/
    private void setCountrySpinner(List<DataModel> locationList) {
        String[] countryNames = new String[locationList.size() + 1];
        countryNames[0] = "Select country";
        for (int i = 0; i < locationList.size(); i++){
            countryNames[i + 1] = locationList.get(i).getName();
        }
        countySpinnerAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.simple_spinner_row, countryNames);
        binding.videoFilterLayout.videoFilterSpinnerCountry.setAdapter(countySpinnerAdapter);

    }

    private void applyFilter() {
        isFilterApplied = true;
        currentPage = 1;
        videoListToShow.clear();
        getData(ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY);
    }

    private void clearFilter() {
        isFilterApplied = false;
        countryLocation = "";
        practiceAreaId = "";
        industySectorId = "";

        if (industrySectorList != null){
            setIndustrySectorSpinner(industrySectorList);
        }
        if (practiceAreaList != null){
            setPracticeAreaSpinner(practiceAreaList);
        }
        if (locationList != null){
            setCountrySpinner(locationList);
        }
        binding.videoFilterLayout.videoFilterEdtCountry.setText("");
        binding.videoFilterLayout.videoFilterEdtIndustrySector.setText("");
        binding.videoFilterLayout.videoFilterEdtPracticeArea.setText("");
    }

    private void drawSearchToolbar() {
        rightToLeft.setDuration(800);
        rightToLeft.start();

        isSearchView = true;

    }
    private void drawOriginalToolbar(){
        leftToRight.setDuration(800);
        leftToRight.start();
        binding.videosToolbar.toolbarEdtSearch.setText("");
        isSearchView = false;
        isPerformingSearch = false;
        if (isSearchPerformed){
            currentPage = 1;
            videoListToShow.clear();
            getData(ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY);
        }
        isSearchPerformed = false;
    }

    private void performSearch() {

        searchText = binding.videosToolbar.toolbarEdtSearch.getText().toString().trim();
        if (searchText.isEmpty() || searchText.equals("") || searchText == null){
            binding.videosToolbar.toolbarEdtSearch.setError(getString(R.string.search_text_empty_error));
        }else{
            isFilterApplied = false;
            isPerformingSearch = true;
            isSearchPerformed = true;
            currentPage = 1;
            videoListToShow.clear();
            getData(ApiUtils.ApiActionEvents.GET_VIDEO_GALLERY);
        }
    }


    @Override
    public void onAnimationStart(Animator animation) {
        if (animation.equals(rightToLeft)){
            binding.videosToolbar.title.setVisibility(View.GONE);
            binding.videosToolbar.backBtn.setVisibility(View.GONE);
        }else if (animation.equals(leftToRight)){
            binding.videosToolbar.cancelBtn.setVisibility(View.GONE);
            binding.videosToolbar.toolbarEdtSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation.equals(rightToLeft)){
            binding.videosToolbar.toolbarEdtSearch.setVisibility(View.VISIBLE);
            binding.videosToolbar.toolbarEdtSearch.requestFocus();
            binding.videosToolbar.cancelBtn.setVisibility(View.VISIBLE);
        }else if (animation.equals(leftToRight)){
            binding.videosToolbar.backBtn.setVisibility(View.VISIBLE);
            binding.videosToolbar.title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


}
