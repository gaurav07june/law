package com.matterhornlegal.fragments;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.activities.BaseActivity;
import com.matterhornlegal.activities.EventDetailActivity;
import com.matterhornlegal.adapters.EventListAdapter;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGTextView;

import com.matterhornlegal.databinding.FragmentEventListBinding;
import com.matterhornlegal.models.EventData;
import com.matterhornlegal.models.EventListResponseModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.EventListDialog;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class EventsListFragment extends BaseFragment implements EventListAdapter.EventListAdapterListener,CalendarFragment.OnEventDateSelectedListener,
        Animator.AnimatorListener, OnMapReadyCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentEventListBinding binding = null;
    private LinearLayoutManager listManager;
    private GridLayoutManager gridManager;
    public EventListAdapter eventListAdapter;
    private CalendarFragment calendarFragment;
    private boolean isNewestFirst=true;
    public int pageNo;
    private int totalPage;
    public List<EventData> eventDataListToShow;
    private boolean isLoading = true;
    private float width;
    private Display display;
    private Point point;
    private ObjectAnimator leftToRight, rightToLeft;
    private boolean isSearchView = false;
    private boolean isPerformingSearch = false;
    private String searchText = "";
    private boolean isSearchPerformed =false;
    private boolean isLocationView = false;
    private GoogleMap googleMap;
    private SupportMapFragment eventsMapFragment;
    private EventListDialog eventListDialog;
    public EventsListFragment() {
        // Required empty public constructor
    }

    public static EventsListFragment newInstance(String param1, String param2) {
        EventsListFragment fragment = new EventsListFragment();
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
        eventListDialog=new EventListDialog(getActivity(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_event_list, container, false);
        View view = binding.getRoot();
        eventsMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.event_list_map);
        calendarFragment = (CalendarFragment) getChildFragmentManager()
                .findFragmentById(R.id.calendar_fragment);
        pageNo = 1;
        totalPage = 0;
        eventDataListToShow = new ArrayList<>();
        eventDataListToShow.clear();
        isPerformingSearch = false;
        isSearchPerformed = false;
        isLocationView = false;
        display = getActivity().getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        width = (point.x) - getActivity().getResources().getDimension(R.dimen._45sdp);
        rightToLeft = ObjectAnimator.ofFloat(binding.myEventsToolbar.searchBtn, "translationX", -width);
        leftToRight = ObjectAnimator.ofFloat(binding.myEventsToolbar.searchBtn, "translationX", 0);
        rightToLeft.addListener(this);
        leftToRight.addListener(this);


        binding.myEventsToolbar.title.setText(R.string.events);
        binding.myEventsToolbar.backBtn.setOnClickListener(this);
        binding.myEventsToolbar.searchBtn.setOnClickListener(this);
        binding.myEventsToolbar.cancelBtn.setOnClickListener(this);
        binding.eventColumnToggleToolbar.calendarTypeBtn.setOnClickListener(this);
        binding.eventColumnToggleToolbar.listTypeBtn.setOnClickListener(this);
        binding.eventColumnToggleToolbar.locationTypeBtn.setOnClickListener(this);
        binding.locationLayoutContainer.setOnClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.myEventsRecycler.getContext(),
                1);
        binding.myEventsRecycler.addItemDecoration(dividerItemDecoration);
        binding.myEventsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = listManager.findLastVisibleItemPosition();

                if (lastVisibleItemPosition== eventListAdapter.getItemCount() - 1) {

                    if ((pageNo <= totalPage) && !isLoading) {
                        getData(ApiUtils.ApiActionEvents.GET_EVENTS_LIST);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.myEventsToolbar.toolbarEdtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        getData(ApiUtils.ApiActionEvents.GET_EVENTS_LIST);
        eventsMapFragment.getMapAsync(this);
        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
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
            case R.id.calendar_type_btn:
                setCalendarView();
                break;
            case R.id.list_type_btn:
                setListView();
                break;
            case R.id.location_type_btn:
                setLocationView();
                break;
            case R.id.btn_read_more:

                break;
        }
    }

    private void setCalendarView() {
        binding.myEventsRecycler.setVisibility(View.GONE);
        binding.locationLayoutContainer.setVisibility(View.GONE);
        binding.calendarContainer.setVisibility(View.VISIBLE);
        binding.eventColumnToggleToolbar.listTypeBtn.setSelected(false);
        binding.eventColumnToggleToolbar.locationTypeBtn.setSelected(false);
        binding.eventColumnToggleToolbar.calendarTypeBtn.setSelected(true);
        isLocationView = false;
    }

    private void setLocationView() {
        binding.myEventsRecycler.setVisibility(View.GONE);
        binding.locationLayoutContainer.setVisibility(View.VISIBLE);
        binding.calendarContainer.setVisibility(View.GONE);
        binding.eventColumnToggleToolbar.calendarTypeBtn.setSelected(false);
        binding.eventColumnToggleToolbar.listTypeBtn.setSelected(false);
        binding.eventColumnToggleToolbar.locationTypeBtn.setSelected(true);
        isLocationView = true;
        placeMarkers();
    }
    private void setListView() {
        binding.myEventsRecycler.setVisibility(View.VISIBLE);
        binding.locationLayoutContainer.setVisibility(View.GONE);
        binding.calendarContainer.setVisibility(View.GONE);
        binding.eventColumnToggleToolbar.calendarTypeBtn.setSelected(false);
        binding.eventColumnToggleToolbar.listTypeBtn.setSelected(true);
        binding.eventColumnToggleToolbar.locationTypeBtn.setSelected(false);
        binding.myEventsRecycler.setLayoutManager(listManager);
        binding.myEventsRecycler.setAdapter(eventListAdapter);
        Utils.runLayoutAnimation(binding.myEventsRecycler);
        isLocationView = false;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.errorInternet));
            return;
        }
        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_EVENTS_LIST:
                isLoading =true;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }
                String eventListPayload;
                String eventListUrl = "";
                if (isPerformingSearch){
                    eventListPayload = null;
                    eventListUrl = (ApiUtils.ApiUrl.BASE_URL+"firms/eventSearchList?search="+searchText+"&page="+pageNo).replaceAll(" ", "%20");
                } else{
                    eventListPayload = ApiUtils.getEventFeedPayload(pageNo).toString();
                    eventListUrl = ApiUtils.ApiUrl.GET_EVENT_LIST_URL;
                }

                RequestManager.addRequest(
                        new GsonObjectRequest<EventListResponseModel>(
                                eventListUrl, eventListPayload, EventListResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {
                            @Override
                            protected void deliverResponse(EventListResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("event list response: " + data);


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
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        super.updateUi(status, actionID, serviceResponse, statusCode);

        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_EVENTS_LIST:
                isLoading = false;
                if (getActivity()==null)return;
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.dismiss();
                    }
                    if (serviceResponse instanceof EventListResponseModel){
                        EventListResponseModel responseModel = (EventListResponseModel) serviceResponse;
                        if (responseModel.getData() != null){
                            eventDataListToShow.addAll(responseModel.getData());
                            calendarFragment.addEvents(getEventDays(responseModel.getData()));
                        }

                        if (eventDataListToShow.size() == 0){
                            binding.txtNoDataFound.setVisibility(View.VISIBLE);
                            binding.myEventsRecycler.setVisibility(View.GONE);
                        }else{
                            binding.txtNoDataFound.setVisibility(View.GONE);
                            binding.myEventsRecycler.setVisibility(View.VISIBLE);
                        }
                        if (pageNo == 1){
                            eventListAdapter=new EventListAdapter(eventDataListToShow, this);
                            listManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
                            if (isLocationView){
                                setLocationView();
                            }else{
                                setListView();
                            }
                        }else{
                            eventListAdapter.addEventListData(eventDataListToShow);
                            if (isLocationView){
                                placeMarkers();
                            }

                        }
                    }
                }
                break;
        }
    }

    private List<CalendarDay> getEventDays(List<EventData> data) {
        List<CalendarDay> calendarDays = new ArrayList<>();
        if (data==null)return calendarDays;
        for (EventData event : data)
        {
            calendarDays.add(CalendarDay.from(Utils.getDate(event.getEvent_start_date())));
        }
        return calendarDays;
    }

    @Override
    public void onEventClicked(ImageView imageView, EventData eventData) {
      //  ToastUtils.showToast(getActivity(), position+" clicked");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !eventListDialog.isShowing()) {
            Intent intent = new Intent(getActivity(), EventDetailActivity.class);

            intent.putExtra(AppConstants.EXTRA.EVENT_DATA, eventData);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), imageView, getActivity().getResources().getString(R.string.transition_name));
            startActivity(intent, options.toBundle());
        }else{
            if (eventListDialog.isShowing())eventListDialog.dismiss();

            Intent intent = new Intent(getActivity(), EventDetailActivity.class);
            intent.putExtra(AppConstants.EXTRA.EVENT_DATA, eventData);
            startActivity(intent);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        if (animation.equals(rightToLeft)){
            binding.myEventsToolbar.title.setVisibility(View.GONE);
            binding.myEventsToolbar.backBtn.setVisibility(View.GONE);
        }else if (animation.equals(leftToRight)){
            binding.myEventsToolbar.cancelBtn.setVisibility(View.GONE);
            binding.myEventsToolbar.toolbarEdtSearch.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (animation.equals(rightToLeft)){
            binding.myEventsToolbar.toolbarEdtSearch.setVisibility(View.VISIBLE);
            binding.myEventsToolbar.toolbarEdtSearch.requestFocus();
            binding.myEventsToolbar.cancelBtn.setVisibility(View.VISIBLE);
        }else if (animation.equals(leftToRight)){
            binding.myEventsToolbar.backBtn.setVisibility(View.VISIBLE);
            binding.myEventsToolbar.title.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    private void drawSearchToolbar() {
        rightToLeft.setDuration(800);
        rightToLeft.start();
        isSearchView = true;

    }
    private void drawOriginalToolbar(){
        leftToRight.setDuration(800);
        leftToRight.start();
        binding.myEventsToolbar.toolbarEdtSearch.setText("");
        isSearchView = false;
        isPerformingSearch = false;
        if (isSearchPerformed){
            pageNo = 1;
            eventDataListToShow.clear();
            getData(ApiUtils.ApiActionEvents.GET_EVENTS_LIST);
        }
        isSearchPerformed = false;
    }
    private void performSearch() {
        searchText = binding.myEventsToolbar.toolbarEdtSearch.getText().toString().trim();
        if (searchText.isEmpty() || searchText.equals("") || searchText == null){
            binding.myEventsToolbar.toolbarEdtSearch.setError(getString(R.string.search_text_empty_error));
        }else{
            isPerformingSearch = true;
            isSearchPerformed = true;
            pageNo = 1;
            eventDataListToShow.clear();
            getData(ApiUtils.ApiActionEvents.GET_EVENTS_LIST);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker != null){
                    EventData event = (EventData) marker.getTag();
                    showEventDescDialog(event);
                }
                return false;
            }
        });
    }

    private void showEventDescDialog(final EventData eventData){
        final ImageView imgEventDesc;
        NMGTextView txtEventTitle, txtEventDateRange, txtEventLocation;
        NMGButton btnReadMore;

        final Dialog mBottomSheetDialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(R.layout.event_description_layout);
        txtEventTitle = mBottomSheetDialog.findViewById(R.id.txt_event_title);
        txtEventDateRange = mBottomSheetDialog.findViewById(R.id.txt_event_date);
        txtEventLocation = mBottomSheetDialog.findViewById(R.id.txt_event_location);
        imgEventDesc = mBottomSheetDialog.findViewById(R.id.img_event_desc);
        btnReadMore = mBottomSheetDialog.findViewById(R.id.btn_read_more);
        txtEventTitle.setText(eventData.getEvent_title());
        String startEventDate = Utils.getFormatedDateTime(eventData.getEvent_start_date());
        String endEventDate = Utils.getFormatedDateTime(eventData.getEvent_end_date());
        txtEventDateRange.setText(String.format((getActivity().getResources().getString(R.string.event_date_range)), startEventDate, endEventDate));
        txtEventLocation.setText(eventData.getEvent_venue());
        Glide.with(getActivity())
                .load(eventData.getEvent_image())
                .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getActivity().getResources(), R.drawable.placeholder_rect, null)))
                .into(imgEventDesc);
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra(AppConstants.EXTRA.EVENT_DATA, eventData);
                    intent.putExtra(AppConstants.IntentConstants.EVENT_ID, eventData.getEvent_id());
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), imgEventDesc, getActivity().getResources().getString(R.string.transition_name));
                    startActivity(intent, options.toBundle());
                }else{
                    Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                    intent.putExtra(AppConstants.EXTRA.EVENT_DATA, eventData);
                    intent.putExtra(AppConstants.IntentConstants.EVENT_ID, eventData.getEvent_id());
                    startActivity(intent);
                }
            }
        });
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

    }
    private void  placeMarkers() {

        if (eventDataListToShow != null && googleMap != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (eventDataListToShow != null && googleMap != null) {

                if (eventDataListToShow != null) {
                    int counter = 0;
                    if (eventDataListToShow.size() == 0){
                        googleMap.clear();
                    }
                    for (int i = 0; i < eventDataListToShow.size(); i++) {
                        String latitude = Double.toString(eventDataListToShow.get(i).getEvent_latitute());
                        String longitude = Double.toString(eventDataListToShow.get(i).getEvent_longitute());
                        if (latitude != null && longitude != null) {
                            counter++;
                            createMarkerOnMap(latitude, longitude, googleMap, eventDataListToShow.get(i), builder);
                        }
                    }
                    if (counter > 0){
                        LatLngBounds bounds = builder.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.30); // offset from edges of the map 10% of screen
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        googleMap.animateCamera(cu);
                    }
                }else{

                    googleMap.clear();
                }
            }
        }
    }
    private void createMarkerOnMap(String latitude, String longitude, GoogleMap googleMap,EventData eventData,LatLngBounds.Builder builder)
    {
        LatLng latLng = new LatLng(Float.parseFloat(latitude), Float.parseFloat(longitude));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        final Marker myMarker = googleMap.addMarker(markerOptions);
        myMarker.setTag(eventData);
        builder.include(myMarker.getPosition());
    }

    @Override
    public void onEventDateSelected(String eventDate) {
        List<EventData> selectedEvents = new ArrayList<>();
        EventData selectedEvent = new EventData();
        selectedEvent.setEvent_start_date(eventDate);
        for(EventData event : eventDataListToShow)
        {
            if(event.equals(selectedEvent))
            {
                selectedEvents.add(event);
            }
        }

        if (selectedEvents.size()>0)
        {
            eventListDialog.show(selectedEvents);
        }
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof  CalendarFragment)
        {
            ((CalendarFragment) childFragment).onAttachToParentFragment(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}