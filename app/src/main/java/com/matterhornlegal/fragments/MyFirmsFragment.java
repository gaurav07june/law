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
import com.matterhornlegal.activities.LawFirmDetailActivity;
import com.matterhornlegal.adapters.FirmListAdapter;
import com.matterhornlegal.databinding.FragmentMyFirmsBinding;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.FirmListResponseModel;
import com.matterhornlegal.models.LawFirmData;
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

public class MyFirmsFragment extends BaseFragment implements View.OnClickListener, FirmListAdapter.FirmListAdapterListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayoutManager listManager;
    private FragmentMyFirmsBinding binding;
    private GridLayoutManager gridManager;
    private FirmListAdapter firmListAdapter;
    private int currentPage = 1;
    private int totalPages = 1;
    private List<LawFirmData> lawFirmDataListToShow;

    private boolean isLoading = false;
    public MyFirmsFragment() {
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
    public static MyFirmsFragment newInstance(String param1, String param2) {
        MyFirmsFragment fragment = new MyFirmsFragment();
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
                inflater, R.layout.fragment_my_firms, container, false);
        View view = binding.getRoot();

        binding.firmsToolbar.title.setText("Law Firms");
        lawFirmDataListToShow = new ArrayList<>();

        binding.firmsColumnToggleToolbar.gridTypeBtn.setOnClickListener(this);
        binding.firmsColumnToggleToolbar.listTypeBtn.setOnClickListener(this);
        binding.firmsToolbar.backBtn.setOnClickListener(this);

        binding.firmFilterLayout.firmFilterEdtIndustrySector.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterEdtPracticeArea.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterEdtCountry.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterBtnApply.setOnClickListener(this);
        binding.firmFilterLayout.firmFilterBtnClearAll.setOnClickListener(this);

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
                        getData(ApiUtils.ApiActionEvents.GET_MY_FIRM_LIST);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        getData(ApiUtils.ApiActionEvents.GET_MY_FIRM_LIST);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grid_type_btn:
                if (!(firmListAdapter.getViewType() == AppConstants.VIEW_TYPE.GRID)){
                    setGridView();
                }
                break;
            case R.id.list_type_btn:
                if (!(firmListAdapter.getViewType() == AppConstants.VIEW_TYPE.LIST)){
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
        binding.firmsColumnToggleToolbar.gridTypeBtn.setSelected(true);
        binding.firmsColumnToggleToolbar.listTypeBtn.setSelected(false);

        firmListAdapter.setViewType(AppConstants.VIEW_TYPE.GRID);
        binding.firmsRecycler.setLayoutManager(gridManager);
        binding.firmsRecycler.setAdapter(firmListAdapter);
        Utils.runLayoutAnimation(binding.firmsRecycler);

    }
    private void setListView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        binding.firmsColumnToggleToolbar.gridTypeBtn.setSelected(false);
        binding.firmsColumnToggleToolbar.listTypeBtn.setSelected(true);
        firmListAdapter.setViewType(AppConstants.VIEW_TYPE.LIST);
        binding.firmsRecycler.setLayoutManager(listManager);
        binding.firmsRecycler.setAdapter(firmListAdapter);
        Utils.runLayoutAnimation(binding.firmsRecycler);

    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_MY_FIRM_LIST:
                isLoading = true;
                if (getActivity() == null){
                    return;
                }
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }
                String getFirmListPayload = ApiUtils.getVideoFeedPayload(currentPage).toString();
                Map<String, String> myFirmListHeader = ApiUtils.getCommonHeader(AppGlobalData.getInstance().getRegisteredUserDetail().getUser_id(),
                        AppGlobalData.getInstance().getRegisteredUserDetail().getApi_key());
                RequestManager.addRequest(
                    new GsonObjectRequest<FirmListResponseModel>(
                            ApiUtils.ApiUrl.GET_MY_FIRMS_LIST_URL,myFirmListHeader, getFirmListPayload, FirmListResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {
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
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID){
            case ApiUtils.ApiActionEvents.GET_MY_FIRM_LIST:
                isLoading = false;
                if (getActivity() == null){
                    return;
                }
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing()){
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
        }
    }
    @Override
    public void onFirmClicked(ImageView imageView, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
}
