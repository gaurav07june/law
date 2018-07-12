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
import com.matterhornlegal.activities.LawFirmDetailActivity;
import com.matterhornlegal.adapters.FirmListAdapter;
import com.matterhornlegal.adapters.SimpleSpinnerAdapter;
import com.matterhornlegal.databinding.FragmentFirmsBinding;
import com.matterhornlegal.models.DataModel;
import com.matterhornlegal.models.FirmListResponseModel;
import com.matterhornlegal.models.IndustryListResponseModel;
import com.matterhornlegal.models.LanguageListResponseModel;
import com.matterhornlegal.models.LawFirmData;
import com.matterhornlegal.models.LocationListResponseModel;
import com.matterhornlegal.models.PracticeAreaListResponseModel;
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

public class FirmsFragment extends BaseFragment implements View.OnClickListener, Animator.AnimatorListener , FirmListAdapter.FirmListAdapterListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean firmData, countryListData, practiceAreaData, industrySectorData, languageData;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentFirmsBinding binding = null;
    private LinearLayoutManager listManager;
    private GridLayoutManager gridManager;
    public FirmListAdapter firmListAdapter;
    private int currentPage = 1;
    private int totalPages = 1;
    private List<LawFirmData> lawFirmDataListToShow;
    private List<DataModel> industrySectorList;
    private List<DataModel> practiceAreaList;
    private List<DataModel> locationList;
    private List<DataModel> languageList;
    private float width;
    private Display display;
    private Point point;
    private ObjectAnimator leftToRight, rightToLeft;
    private boolean isFilterView = false;
    private ArrayAdapter<String> industrySectorAdapter;
    private ArrayAdapter<String> practiceAreaAdapter;
    private boolean isFilterApplied = false;
    private String getFirmListPayload = "";
    private String industySectorId = "";
    private String practiceAreaId = "";
    private String countryLocation = "";
    private String languageId = "";
    private boolean isSearchView = false;
    private boolean isLoading = false;
    private String searchText = "";
    private boolean isPerformingSearch = false;
    private boolean isSearchPerformed =false;
    private String countryName = "", practiceArea = "", industrySector = "", language = "";
    private SimpleSpinnerAdapter countrySpinnerAdapter, industrySectorSpinnerAdapter, practiceAreaSpinnerAdapter, languageSpinnerAdapter;

    public FirmsFragment() {
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
    public static FirmsFragment newInstance(String param1, String param2) {
        FirmsFragment fragment = new FirmsFragment();
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
                inflater, R.layout.fragment_firms, container, false);
        View view = binding.getRoot();



        isFilterApplied = false;
        isPerformingSearch = false;
        isSearchPerformed = false;
        firmData = false;
        countryListData = false;
        practiceAreaData= false;
        industrySectorData = false;
        languageData = false;
        display = getActivity().getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        width = (point.x) - getActivity().getResources().getDimension(R.dimen._45sdp);
        rightToLeft = ObjectAnimator.ofFloat(binding.firmsToolbar.searchBtn, "translationX", -width);
        leftToRight = ObjectAnimator.ofFloat(binding.firmsToolbar.searchBtn, "translationX", 0);

        binding.firmsToolbar.title.setText("Law Firms");
        lawFirmDataListToShow = new ArrayList<>();
        binding.firmsColumnToggleToolbar.filterBtn.setOnClickListener(this);
        binding.firmsColumnToggleToolbar.gridTypeBtn.setOnClickListener(this);
        binding.firmsColumnToggleToolbar.listTypeBtn.setOnClickListener(this);
        binding.firmsToolbar.backBtn.setOnClickListener(this);
        binding.firmsToolbar.searchBtn.setOnClickListener(this);
        binding.firmsToolbar.cancelBtn.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterEdtIndustrySector.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterEdtPracticeArea.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterEdtCountry.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterBtnApply.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterBtnClearAll.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterEdtLanguage.setOnClickListener(this);
        rightToLeft.addListener(this);
        leftToRight.addListener(this);
        binding.firmsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = 0;
                if (firmListAdapter.getViewType() == AppConstants.VIEW_TYPE.GRID){
                    lastVisibleItemPosition = gridManager.findLastVisibleItemPosition();    
                }else if (firmListAdapter.getViewType() == AppConstants.VIEW_TYPE.LIST){
                    lastVisibleItemPosition = listManager.findLastVisibleItemPosition();
                }
                if (lastVisibleItemPosition== firmListAdapter.getItemCount() - 1) {

                    if ((currentPage <= totalPages) && !isLoading) {
                        getData(ApiUtils.ApiActionEvents.GET_FIRMS_LIST);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.firmFilterLayout.firmFilterSpinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.firmFilterLayout.firmFilterEdtCountry.setText(locationList.get(position - 1).getName());
                    countryLocation = Integer.toString(locationList.get(position - 1).getId());
                    countryName = locationList.get(position - 1).getName();
                } else {
                    binding.firmFilterLayout.firmFilterEdtCountry.setText(countryName);
                    //countryLocation = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.firmFilterLayout.spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    binding.firmFilterLayout.firmFilterEdtLanguage.setText(languageList.get(position - 1).getName());
                    languageId = Integer.toString(languageList.get(position - 1).getId());
                    language = languageList.get(position - 1).getName();
                } else {
                    binding.firmFilterLayout.firmFilterEdtLanguage.setText(language);
                    //languageId = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.firmFilterLayout.spinnerIndustryArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.firmFilterLayout.firmFilterEdtIndustrySector.setText(industrySectorList.get(position - 1).getName());
                    industySectorId = Integer.toString(industrySectorList.get(position - 1).getId());
                    industrySector = industrySectorList.get(position - 1).getName();
                }else{
                    binding.firmFilterLayout.firmFilterEdtIndustrySector.setText(industrySector);
                    //industySectorId = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.firmFilterLayout.spinnerPracticeArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){
                    binding.firmFilterLayout.firmFilterEdtPracticeArea.setText(practiceAreaList.get(position - 1).getName());
                    practiceAreaId = Integer.toString(practiceAreaList.get(position - 1).getId());
                    practiceArea = practiceAreaList.get(position - 1).getName();
                }else {
                    binding.firmFilterLayout.firmFilterEdtPracticeArea.setText(practiceArea);
                    //practiceAreaId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.firmsToolbar.toolbarEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        // get the saved preference values
        /*LanguageListResponseModel languageListResponseModel =SharedPrefsUtils.getLanguagePref(getActivity(),
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.LANGUAGE_LIST_PREF);
        LocationListResponseModel locationListResponseModel = SharedPrefsUtils.getCountryListPref(getActivity(),
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.COUNTRY_LIST_PREF);
        PracticeAreaListResponseModel practiceAreaListResponseModel = SharedPrefsUtils.getPracticeAreaPref(getActivity(),
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.PRACTICE_AREA_PREF);
        IndustryListResponseModel industryListResponseModel = SharedPrefsUtils.getIndustrySectorPref(getActivity(),
                AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.INDUSTRY_SECTOR_PREF);
        if (languageListResponseModel != null){
            languageList = languageListResponseModel.getData();
            setLanguageSpinner(languageList);
        }
        if (locationListResponseModel != null){
            locationList = locationListResponseModel.getData();
            setCountrySpinner(locationList);
        }
        if (practiceAreaListResponseModel != null){
            practiceAreaList = practiceAreaListResponseModel.getData();
            setPracticeAreaSpinner(practiceAreaList);
        }
        if (industryListResponseModel != null){
            industrySectorList = industryListResponseModel.getData();
            setIndustrySectorSpinner(industrySectorList);
        }*/


        getData(ApiUtils.ApiActionEvents.GET_FIRMS_LIST);

        // hit the location, practice area and industry name apis
        getData(ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST);
        getData(ApiUtils.ApiActionEvents.GET_INDUSTRY_SECTORS_LIST);
        getData(ApiUtils.ApiActionEvents.GET_PRACTICE_AREAS);
        getData(ApiUtils.ApiActionEvents.GET_LANGUAGE);

        // also get language
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_type_btn:
                if (!(firmListAdapter.getViewType() == AppConstants.VIEW_TYPE.GRID) || isFilterView){
                    setGridView();
                }
                break;
            case R.id.list_type_btn:
                if (!(firmListAdapter.getViewType() == AppConstants.VIEW_TYPE.LIST) || isFilterView){
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
            case R.id.firm_filter_edt_industry_sector:
                binding.firmFilterLayout.spinnerIndustryArea.performClick();
                break;
            case R.id.firm_filter_edt_country:
                binding.firmFilterLayout.firmFilterSpinnerCountry.performClick();
                break;
            case R.id.firm_filter_edt_practice_area:
                binding.firmFilterLayout.spinnerPracticeArea.performClick();
                break;
            case R.id.firm_filter_edt_language:
                binding.firmFilterLayout.spinnerLanguage.performClick();
                break;
            case R.id.firm_filter_btn_apply:
                applyFilter();
                break;
            case R.id.firm_filter_btn_clear_all:
                clearFilter();
                break;
        }
    }


    private void performSearch() {

        searchText = binding.firmsToolbar.toolbarEdtSearch.getText().toString().trim();
        if (searchText.isEmpty() || searchText.equals("") || searchText == null){
            binding.firmsToolbar.toolbarEdtSearch.setError(getString(R.string.search_text_empty_error));
        }else{
            isFilterApplied = false;
            isPerformingSearch = true;
            isSearchPerformed = true;
            currentPage = 1;
            lawFirmDataListToShow.clear();
            getData(ApiUtils.ApiActionEvents.GET_FIRMS_LIST);
        }
    }

    private void applyFilter() {
        isFilterApplied = true;
        currentPage = 1;
        lawFirmDataListToShow.clear();
        getData(ApiUtils.ApiActionEvents.GET_FIRMS_LIST);
    }

    private void clearFilter() {
        isFilterApplied = false;
        countryLocation = "";
        practiceAreaId = "";
        industySectorId = "";
        languageId = "";
        countryName = "";
        practiceArea = "";
        industrySector = "";
        language = "";

        if (industrySectorList != null){
            setIndustrySectorSpinner(industrySectorList);
        }
        if (practiceAreaList != null){
            setPracticeAreaSpinner(practiceAreaList);
        }
        if (locationList != null){
            setCountrySpinner(locationList);
        }
        if (languageList != null){
            setLanguageSpinner(languageList);
        }

       /*binding.firmFilterLayout.firmFilterEdtCountry.setText("");
        binding.firmFilterLayout.firmFilterEdtIndustrySector.setText("");
        binding.firmFilterLayout.firmFilterEdtPracticeArea.setText("");*/
    }
    private void drawSearchToolbar() {
        rightToLeft.setDuration(800);
        rightToLeft.start();
        isSearchView = true;

    }
    private void drawOriginalToolbar(){
        leftToRight.setDuration(800);
        leftToRight.start();
        binding.firmsToolbar.toolbarEdtSearch.setText("");
        isSearchView = false;
        isPerformingSearch = false;
        if (isSearchPerformed){
            currentPage = 1;
            lawFirmDataListToShow.clear();
            getData(ApiUtils.ApiActionEvents.GET_FIRMS_LIST);
        }
        isSearchPerformed = false;
    }
    private void setFilterView() {
        binding.filterLayoutContainer.setVisibility(View.VISIBLE);
        binding.firmsColumnToggleToolbar.gridTypeBtn.setSelected(false);
        binding.firmsColumnToggleToolbar.listTypeBtn.setSelected(false);
        binding.firmsColumnToggleToolbar.filterBtn.setSelected(true);
        isFilterView = true;
        //drawOriginalToolbar();
    }
    private void setGridView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.firmsColumnToggleToolbar.gridTypeBtn.setSelected(true);
        binding.firmsColumnToggleToolbar.listTypeBtn.setSelected(false);
        binding.firmsColumnToggleToolbar.filterBtn.setSelected(false);
        firmListAdapter.setViewType(AppConstants.VIEW_TYPE.GRID);
        binding.firmsRecycler.setLayoutManager(gridManager);
        binding.firmsRecycler.setAdapter(firmListAdapter);
        Utils.runLayoutAnimation(binding.firmsRecycler);
        isFilterView = false;
    }
    private void setListView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.firmsColumnToggleToolbar.gridTypeBtn.setSelected(false);
        binding.firmsColumnToggleToolbar.listTypeBtn.setSelected(true);
        binding.firmsColumnToggleToolbar.filterBtn.setSelected(false);
        firmListAdapter.setViewType(AppConstants.VIEW_TYPE.LIST);
        binding.firmsRecycler.setLayoutManager(listManager);
        binding.firmsRecycler.setAdapter(firmListAdapter);
        Utils.runLayoutAnimation(binding.firmsRecycler);
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
            case ApiUtils.ApiActionEvents.GET_FIRMS_LIST:
                isLoading = true;
                firmData = false;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }

                String firmListUrl = "";
                if (isFilterApplied){
                    getFirmListPayload = ApiUtils.getFilteredFirmListPayload(industySectorId, practiceAreaId, countryLocation, languageId, Integer.toString(currentPage)).toString();
                    firmListUrl = ApiUtils.ApiUrl.GET_FIRM_FILTER;
                }else if (isPerformingSearch){
                    getFirmListPayload = null;
                    firmListUrl = (ApiUtils.ApiUrl.BASE_URL+"firms/searchListFirm?search="+searchText+"&page="+currentPage).replaceAll(" ", "%20");
                } else{
                    getFirmListPayload = ApiUtils.getVideoFeedPayload(currentPage).toString();
                    firmListUrl = ApiUtils.ApiUrl.GET_FIRMS_LIST;
                }

                RequestManager.addRequest(
                        new GsonObjectRequest<FirmListResponseModel>(
                                firmListUrl, getFirmListPayload, FirmListResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {
                            @Override
                            protected void deliverResponse(FirmListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("video gallery response: " + data);


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
            case ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST:
                /*if (((BaseActivity)getActivity()).progressDialog != null) {
                    if (!((BaseActivity) getActivity()).progressDialog.isShowing()) {
                        ((BaseActivity) getActivity()).progressDialog.show();
                    }
                }*/
                countryListData =false;
                RequestManager.addRequest(
                        new GsonObjectRequest<LocationListResponseModel>(
                                ApiUtils.ApiUrl.GET_LOCATIONS_LIST, null, LocationListResponseModel.class,
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
            case ApiUtils.ApiActionEvents.GET_INDUSTRY_SECTORS_LIST:
               /* if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }*/
                industrySectorData = false;
                RequestManager.addRequest(
                    new GsonObjectRequest<IndustryListResponseModel>(
                            ApiUtils.ApiUrl.GET_INDUSTRY_SECTORS_LIST, null, IndustryListResponseModel.class,
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
            case ApiUtils.ApiActionEvents.GET_PRACTICE_AREAS:
               /* if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }*/
                practiceAreaData = false;
                RequestManager.addRequest(
                    new GsonObjectRequest<PracticeAreaListResponseModel>(
                            ApiUtils.ApiUrl.GET_PRACTICE_AREAS_URL, null, PracticeAreaListResponseModel.class,
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
            case ApiUtils.ApiActionEvents.GET_LANGUAGE:
                /*if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }*/
                languageData = false;
                RequestManager.addRequest(
                        new GsonObjectRequest<LanguageListResponseModel>(
                                ApiUtils.ApiUrl.GET_LANGUAGE_URL, null, LanguageListResponseModel.class,
                                new VolleyErrorListener(this, getActivity(), actionID)) {

                            @Override
                            protected void deliverResponse(LanguageListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("language list response: " + data);
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
        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_FIRMS_LIST:
                isLoading = false;
                firmData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && firmData && countryListData &&
                            practiceAreaData && industrySectorData && languageData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof FirmListResponseModel){
                    FirmListResponseModel responseModel = (FirmListResponseModel) serviceResponse;
                    totalPages = responseModel.getTotal_pages();
                    if (responseModel.getData()!= null){
                        lawFirmDataListToShow.addAll(responseModel.getData());
                    }
                    if (lawFirmDataListToShow.size() == 0){
                        binding.txtNoDataFound.setVisibility(View.VISIBLE);
                        binding.firmsRecycler.setVisibility(View.GONE);
                    }else{
                        binding.txtNoDataFound.setVisibility(View.GONE);
                        binding.firmsRecycler.setVisibility(View.VISIBLE);
                    }

                    if (currentPage == 1){
                        firmListAdapter=new FirmListAdapter(lawFirmDataListToShow, this);
                        gridManager=new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
                        listManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                        setGridView();
                    }else{
                        firmListAdapter.setFirmList(lawFirmDataListToShow);
                    }
                    currentPage++;

                }
                break;
            case ApiUtils.ApiActionEvents.GET_LOCATIONS_LIST:
                countryListData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && firmData
                            && countryListData && practiceAreaData && industrySectorData && languageData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof LocationListResponseModel){
                    LocationListResponseModel responseModel = (LocationListResponseModel) serviceResponse;
                   // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel != null){
                        if (responseModel.getData() != null){
                            //SharedPrefsUtils.setCountryListPref(getActivity(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.COUNTRY_LIST_PREF, responseModel);
                            locationList = responseModel.getData();
                            setCountrySpinner(locationList);
                        }
                    }
                }
                break;
            case ApiUtils.ApiActionEvents.GET_PRACTICE_AREAS:
                practiceAreaData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && firmData
                            && countryListData && practiceAreaData && industrySectorData && languageData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof PracticeAreaListResponseModel){
                    PracticeAreaListResponseModel responseModel = (PracticeAreaListResponseModel) serviceResponse;
                    //ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel != null){
                        if (responseModel.getData() != null){
                            //SharedPrefsUtils.setPracticeAreaPref(getActivity(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.PRACTICE_AREA_PREF, responseModel);
                            practiceAreaList = responseModel.getData();
                            setPracticeAreaSpinner(practiceAreaList);
                        }
                    }


                }
                break;
            case ApiUtils.ApiActionEvents.GET_INDUSTRY_SECTORS_LIST:
                industrySectorData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && firmData && countryListData
                            && practiceAreaData && industrySectorData && languageData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof IndustryListResponseModel){
                    IndustryListResponseModel responseModel = (IndustryListResponseModel) serviceResponse;
                   // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel != null){
                        if (responseModel.getData() != null ){
                            //SharedPrefsUtils.setIndustrySectorPref(getActivity(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.INDUSTRY_SECTOR_PREF, responseModel);
                            industrySectorList = responseModel.getData();
                            setIndustrySectorSpinner(industrySectorList);
                        }
                    }


                }
                break;
            case ApiUtils.ApiActionEvents.GET_LANGUAGE:
                languageData = true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing() && firmData && countryListData && practiceAreaData && industrySectorData && languageData){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                }
                if (serviceResponse instanceof LanguageListResponseModel){
                    LanguageListResponseModel responseModel = (LanguageListResponseModel) serviceResponse;
                    // ToastUtils.showToast(getActivity(), responseModel.getMessage());
                    if (responseModel != null){
                        if (responseModel.getData() != null ){
                            //SharedPrefsUtils.setLanguagePref(getActivity(), AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.LANGUAGE_LIST_PREF, responseModel);
                            languageList = responseModel.getData();
                            setLanguageSpinner(languageList);
                        }
                    }


                }
                break;
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (animation.equals(rightToLeft)){
            binding.firmsToolbar.title.setVisibility(View.GONE);
            binding.firmsToolbar.backBtn.setVisibility(View.GONE);
        }else if (animation.equals(leftToRight)){
            binding.firmsToolbar.cancelBtn.setVisibility(View.GONE);
            binding.firmsToolbar.toolbarEdtSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation.equals(rightToLeft)){
            binding.firmsToolbar.toolbarEdtSearch.setVisibility(View.VISIBLE);
            binding.firmsToolbar.toolbarEdtSearch.requestFocus();
            binding.firmsToolbar.cancelBtn.setVisibility(View.VISIBLE);
        }else if (animation.equals(leftToRight)){
            binding.firmsToolbar.backBtn.setVisibility(View.VISIBLE);
            binding.firmsToolbar.title.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onFirmClicked(ImageView imageView, int position) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(getActivity(), LawFirmDetailActivity.class);

            intent.putExtra(AppConstants.IntentConstants.FIRM_DATA, lawFirmDataListToShow.get(position));
            intent.putExtra(AppConstants.IntentConstants.FIRM_ID, lawFirmDataListToShow.get(position).getFirm_id());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), imageView, getActivity().getResources().getString(R.string.transition_name));
            startActivity(intent, options.toBundle());
        }else{
            Intent intent = new Intent(getActivity(), LawFirmDetailActivity.class);
            intent.putExtra(AppConstants.IntentConstants.FIRM_DATA, lawFirmDataListToShow.get(position));
            intent.putExtra(AppConstants.IntentConstants.FIRM_ID, lawFirmDataListToShow.get(position).getFirm_id());
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
        binding.firmFilterLayout.spinnerIndustryArea.setAdapter(industrySectorSpinnerAdapter);
       /* industrySectorAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, industryNameList);

        industrySectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boolean[] selectedItems = new boolean[industrySectorAdapter.getCount()];

        for (int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = false;
        }
        binding.firmFilterLayout.multipleSpinnerIndustryArea.setAdapter(industrySectorAdapter, false, onIndustrySectorSelectedListener);
        binding.firmFilterLayout.multipleSpinnerIndustryArea.setSelected(selectedItems);*/
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
            binding.firmFilterLayout.firmFilterEdtIndustrySector.setText(industrySector);
        }
    };*/

   private void setLanguageSpinner(List<DataModel> languageList){
       String[] languageNameList= new String[languageList.size() + 1];
       languageNameList[0]= "Select language";
       for (int i = 0; i < languageList.size(); i++){
           languageNameList[i + 1] = languageList.get(i).getName();
       }
       languageSpinnerAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.simple_spinner_row, languageNameList);
       binding.firmFilterLayout.spinnerLanguage.setAdapter(languageSpinnerAdapter);

   }
    private void setPracticeAreaSpinner(List<DataModel> practiceAreaList) {
        String[] practiceNameList= new String[practiceAreaList.size() + 1];
        practiceNameList[0]= "Select practice area";
        for (int i = 0; i < practiceAreaList.size(); i++){
            practiceNameList[i + 1] = practiceAreaList.get(i).getName();
        }
        practiceAreaSpinnerAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.simple_spinner_row, practiceNameList);
        binding.firmFilterLayout.spinnerPracticeArea.setAdapter(practiceAreaSpinnerAdapter);
        /*practiceAreaAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, practiceNameList);

        practiceAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boolean[] selectedItems = new boolean[practiceAreaAdapter.getCount()];

        for (int i = 0; i < selectedItems.length; i++) {
            selectedItems[i] = false;
        }
        binding.firmFilterLayout.multipleSpinnerPracticeArea.setAdapter(practiceAreaAdapter, false, onPracticeAreaSelectedListener );
        binding.firmFilterLayout.multipleSpinnerPracticeArea.setSelected(selectedItems);*/
    }

    /*private MultiSpinner.MultiSpinnerListener onPracticeAreaSelectedListener = new MultiSpinner.MultiSpinnerListener() {
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
            binding.firmFilterLayout.firmFilterEdtPracticeArea.setText(practiceArea);
        }
    };*/

    private void setCountrySpinner(List<DataModel> locationList) {
        //ArrayList<String> countryNames = new ArrayList<>();
        String[] countryNames = new String[locationList.size() + 1];
        countryNames[0] = "Select country";
        for (int i = 0; i < locationList.size(); i++){
            countryNames[i + 1] = locationList.get(i).getName();
        }
        /*for (DataModel data : locationList) {
            countryNames.add(data.getName());
        }*/
        countrySpinnerAdapter = new SimpleSpinnerAdapter(getActivity(), R.layout.simple_spinner_row, countryNames);
        binding.firmFilterLayout.firmFilterSpinnerCountry.setAdapter(countrySpinnerAdapter);
        /*ArrayAdapter<String> locationAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, countryNames);

        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.firmFilterLayout.firmFilterSpinnerCountry.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        locationAdapter,
                        R.layout.inflate_nothing_selected_row,
                        getActivity()));*/

    }
}
