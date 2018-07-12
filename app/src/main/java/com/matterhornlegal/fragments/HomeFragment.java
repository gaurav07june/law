package com.matterhornlegal.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.activities.BaseActivity;
import com.matterhornlegal.activities.BlogDetailActivity;
import com.matterhornlegal.activities.HomeLandingActivity;
import com.matterhornlegal.activities.LawFirmDetailActivity;
import com.matterhornlegal.activities.VideoDetailActivity;
import com.matterhornlegal.databinding.FragmentHomeBinding;

import com.matterhornlegal.models.BlogModel;
import com.matterhornlegal.models.HomeFeedData;
import com.matterhornlegal.models.HomeFeedResponseModel;
import com.matterhornlegal.models.LawFirmData;
import com.matterhornlegal.models.VideoFeedModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppCommons;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.FragmentController;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.ToastUtils;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FragmentHomeBinding binding = null;
    List<BlogModel>  blogModelList = null;
    List<LawFirmData> lawFirmDataList = null;
    List<VideoFeedModel> videoFeedModelList = null;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
                inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        getData(ApiUtils.ApiActionEvents.GET_HOME_FEED);
        binding.homeContent.homeHeaderIndicator.setViewPager(binding.homeContent.homeHeaderPager, 2);
        //For now the livestream data is not in the api. so show no data in the view
        binding.homeContent.liveStreamItemView.setVisibility(View.GONE);
        binding.homeContent.txtNoLivestreamData.setVisibility(View.VISIBLE);
       binding.homeContent.homeBlog.homeBlogImg1.setOnClickListener(this);
        binding.homeContent.homeBlog.homeBlogImg2.setOnClickListener(this);
        binding.homeContent.homeBlog.homeReadMoreBlogsBtn.setOnClickListener(this);
        binding.homeContent.featuredLawFirms.lawFirmThumb1.setOnClickListener(this);
        binding.homeContent.featuredLawFirms.lawFirmThumb2.setOnClickListener(this);
        binding.homeContent.featuredLawFirms.featuredLawFirmsMoreBtn.setOnClickListener(this);
        binding.homeContent.homeLivePracVideos.pracVideo1Thumb.setOnClickListener(this);
        binding.homeContent.homeLivePracVideos.pracVideo2Thumb.setOnClickListener(this);
        binding.homeContent.homeLivePracVideos.homeFindMoreVideosBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.home_blog_img1:
                if (blogModelList == null){
                    return;
                }
                if (blogModelList.size()>0){
                    startActivity(new Intent(getActivity(), BlogDetailActivity.class)
                            .putExtra(AppConstants.EXTRA.BLOG_DATA, blogModelList.get(0)));
                }

                break;
            case R.id.home_blog_img2:
                if (blogModelList == null){
                    return;
                }
                if (blogModelList.size() > 0){
                    startActivity(new Intent(getActivity(), BlogDetailActivity.class)
                            .putExtra(AppConstants.EXTRA.BLOG_DATA, blogModelList.get(1)));
                }

                break;
            case R.id.home_read_more_blogs_btn:
                ((HomeLandingActivity)getActivity()).isOnHome = false;
                FragmentController.replaceBlogFrag((HomeLandingActivity)getActivity(), R.id.frag_container);
                ((HomeLandingActivity) getActivity()).toggleBottomNavigation(0);
                break;
            case R.id.law_firm_thumb1:
                if (lawFirmDataList == null){
                    return;
                }
                if (lawFirmDataList.size() > 0){
                    Intent firm1Intent = new Intent(getActivity(), LawFirmDetailActivity.class);
                    firm1Intent.putExtra(AppConstants.IntentConstants.FIRM_DATA, lawFirmDataList.get(0));
                    firm1Intent.putExtra(AppConstants.IntentConstants.FIRM_ID, lawFirmDataList.get(0).getFirm_id());
                    startActivity(firm1Intent);
                }

                break;
            case R.id.law_firm_thumb2:
                if (lawFirmDataList == null){
                    return;
                }
                if (lawFirmDataList.size() > 0){
                    Intent firm2Intent = new Intent(getActivity(), LawFirmDetailActivity.class);
                    firm2Intent.putExtra(AppConstants.IntentConstants.FIRM_DATA, lawFirmDataList.get(1));
                    firm2Intent.putExtra(AppConstants.IntentConstants.FIRM_ID, lawFirmDataList.get(1).getFirm_id());
                    startActivity(firm2Intent);
                }

                break;

            case R.id.featured_law_firms_more_btn:
                ((HomeLandingActivity)getActivity()).isOnHome = false;
                FragmentController.replaceFirmFrag((HomeLandingActivity)getActivity(), R.id.frag_container);
                ((HomeLandingActivity) getActivity()).toggleBottomNavigation(0);
                break;
            case R.id.prac_video_1_thumb:
                if (videoFeedModelList == null){
                    return;
                }
                if (videoFeedModelList.size() > 0){
                    Intent video1Intent = new Intent(getActivity(), VideoDetailActivity.class);
                    video1Intent.putExtra(AppConstants.IntentConstants.VIDEO_DATA, videoFeedModelList.get(0));
                    video1Intent.putExtra(AppConstants.IntentConstants.VIDEO_ID, videoFeedModelList.get(0).getVideo_id());
                    startActivity(video1Intent);
                }

                break;
            case R.id.prac_video_2_thumb:
                if (videoFeedModelList == null){
                    return;
                }
                if (videoFeedModelList.size() > 0){
                    Intent video2Intent = new Intent(getActivity(), VideoDetailActivity.class);
                    video2Intent.putExtra(AppConstants.IntentConstants.VIDEO_DATA, videoFeedModelList.get(1));
                    video2Intent.putExtra(AppConstants.IntentConstants.VIDEO_ID, videoFeedModelList.get(1).getVideo_id());
                    startActivity(video2Intent);
                }

                break;
            case R.id.home_find_more_videos_btn:
                ((HomeLandingActivity)getActivity()).isOnHome = false;
                FragmentController.replaceVideoFrag((HomeLandingActivity)getActivity(), R.id.frag_container);
                ((HomeLandingActivity) getActivity()).toggleBottomNavigation(R.id.bottom_action_videos);
                break;
        }

    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        if (!Utils.isNetworkEnabled(getActivity())) {
            ToastUtils.showToast(getActivity(), getString(R.string.errorInternet));
            return;
        }
        switch (actionID) {
            case ApiUtils.ApiActionEvents.GET_HOME_FEED:
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (!((BaseActivity)getActivity()).progressDialog.isShowing()){
                        ((BaseActivity)getActivity()).progressDialog.show();
                    }
                }

                String payload = "";
                RequestManager.addRequest(
                        new GsonObjectRequest<HomeFeedResponseModel>(
                                ApiUtils.ApiUrl.GET_HOME_FEED_URL, payload, HomeFeedResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {

                            @Override
                            protected void deliverResponse(HomeFeedResponseModel response) {
                                String data = new String(mResponse.data);
                                Logger.error("login response: " + data);

                                if (response.getStatus() == 1 && response.getData() != null) {
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
            case ApiUtils.ApiActionEvents.GET_HOME_FEED:
                if (getActivity() == null){
                    return;
                }
                if (((BaseActivity)getActivity()).progressDialog != null){
                    if (((BaseActivity)getActivity()).progressDialog.isShowing())
                    ((BaseActivity)getActivity()).progressDialog.dismiss();
                }
                if (status) {
                    if (serviceResponse instanceof HomeFeedResponseModel) {
                        HomeFeedResponseModel responseModel = (HomeFeedResponseModel) serviceResponse;
                        Logger.error("  +message : " + responseModel.getMessage());
                      //  Toast.makeText(getActivity(), responseModel.getMessage(), Toast.LENGTH_SHORT).show();
                        updateHomeView(responseModel.getData());
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

    private void updateHomeView(HomeFeedData homeFeedData){


        if (homeFeedData.getBlog_data() != null){
            blogModelList = homeFeedData.getBlog_data();
        }
        if (homeFeedData.getFirm_data() != null){
            lawFirmDataList = homeFeedData.getFirm_data();
        }
        if (homeFeedData.getVideo_data() != null){
            videoFeedModelList = homeFeedData.getVideo_data();
        }
        if (blogModelList.size() == 0){
            binding.homeContent.homeBlog.lnlayHomeBlog.setVisibility(View.GONE);
            binding.homeContent.txtNoBlogData.setVisibility(View.VISIBLE);
        }else{
            binding.homeContent.homeBlog.lnlayHomeBlog.setVisibility(View.VISIBLE);
            binding.homeContent.txtNoBlogData.setVisibility(View.GONE);
            // for first blog
            Glide.with(getActivity().getApplicationContext())
                    .load(blogModelList.get(0).getBlogImg())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.homeContent.homeBlog.homeBlogImg1);
            binding.homeContent.homeBlog.homeBlogTitle1.setText(blogModelList.get(0).getBlogTitle());
            binding.homeContent.homeBlog.blogDesc1.setText(blogModelList.get(0).getBlogDescription());
            binding.homeContent.homeBlog.blogDate1.setText(AppCommons.getMonthFullDayYear(getActivity(),blogModelList.get(0).getBlogDate()));

            // for second blog
            Glide.with(getActivity().getApplicationContext())
                    .load(blogModelList.get(1).getBlogImg())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.homeContent.homeBlog.homeBlogImg2);
            binding.homeContent.homeBlog.homeBlogTitle2.setText(blogModelList.get(1).getBlogTitle());
            binding.homeContent.homeBlog.blogDesc2.setText(blogModelList.get(1).getBlogDescription());
            binding.homeContent.homeBlog.blogDate2.setText(AppCommons.getMonthFullDayYear(getActivity(),blogModelList.get(1).getBlogDate()));

        }
        if (lawFirmDataList.size() == 0){
            binding.homeContent.featuredLawFirms.relativeLayHomeFirm.setVisibility(View.GONE);
            binding.homeContent.txtNoFirmData.setVisibility(View.VISIBLE);
        }else{
            binding.homeContent.featuredLawFirms.relativeLayHomeFirm.setVisibility(View.VISIBLE);
            binding.homeContent.txtNoFirmData.setVisibility(View.GONE);
            // for first firm
            Glide.with(getActivity().getApplicationContext())
                    .load(lawFirmDataList.get(0).getImage())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.homeContent.featuredLawFirms.lawFirmThumb1);
            binding.homeContent.featuredLawFirms.lawFirmTitle1.setText(lawFirmDataList.get(0).getTitle());
            binding.homeContent.featuredLawFirms.lawFirmLocation1.setText(lawFirmDataList.get(0).getLocation());

            // for second firm
            Glide.with(getActivity().getApplicationContext())
                    .load(lawFirmDataList.get(1).getImage())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.homeContent.featuredLawFirms.lawFirmThumb2);
            binding.homeContent.featuredLawFirms.lawFirmTitle2.setText(lawFirmDataList.get(1).getTitle());
            binding.homeContent.featuredLawFirms.lawFirmLocation2.setText(lawFirmDataList.get(1).getLocation());
        }
        if (videoFeedModelList.size() == 0){
            binding.homeContent.homeLivePracVideos.relativeLayHomeVideo.setVisibility(View.GONE);
            binding.homeContent.txtNoVideoData.setVisibility(View.VISIBLE);
        }else{
            binding.homeContent.homeLivePracVideos.relativeLayHomeVideo.setVisibility(View.VISIBLE);
            binding.homeContent.txtNoVideoData.setVisibility(View.GONE);
            // for  first video
            Glide.with(getActivity().getApplicationContext())
                    .load(videoFeedModelList.get(0).getVideo_thumbnail())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.homeContent.homeLivePracVideos.pracVideo1Thumb);
            binding.homeContent.homeLivePracVideos.txtPracVideo1Title.setText(videoFeedModelList.get(0).getVideo_title());
            // for second video
            Glide.with(getActivity().getApplicationContext())
                    .load(videoFeedModelList.get(1).getVideo_thumbnail())
                    .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                    .into(binding.homeContent.homeLivePracVideos.pracVideo2Thumb);
            binding.homeContent.homeLivePracVideos.txtPracVideo2Title.setText(videoFeedModelList.get(1).getVideo_title());

        }
    }
}
