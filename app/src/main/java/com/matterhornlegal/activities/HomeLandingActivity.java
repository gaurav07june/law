package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.databinding.ActivityHomeLandingBinding;
import com.matterhornlegal.databinding.HomeDrawerHeaderLayoutBinding;
import com.matterhornlegal.fragments.MyEventsListFragment;
import com.matterhornlegal.models.AppGlobalData;
import com.matterhornlegal.models.RegisteredUserDetail;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.FragmentController;
import com.matterhornlegal.utils.HomeAddVideoFirmDialog;
import com.matterhornlegal.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gaurav.singh on 6/25/2018.
 */

public class HomeLandingActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{
    private HomeAddVideoFirmDialog homeAddVideoFirmDialog;
    private ActivityHomeLandingBinding binding;
    private boolean isGuestUser = false;
    private Handler mHandler = new Handler();
    private MenuItem prevMenuItem = null;
    public boolean isOnHome=true;
    private RegisteredUserDetail registeredUserDetail;
    private CircleImageView userImage;
    private TextView txtUserName, txtUserOccupation;
    private HomeDrawerHeaderLayoutBinding homeDrawerHeaderLayoutBinding;
    public static boolean isEventListUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=   DataBindingUtil.setContentView(this, R.layout.activity_home_landing);
        View drawerHeaderLayout = binding.homeNavView.getHeaderView(0);
        MenuItem logoutItem = binding.homeNavView.getMenu().findItem(R.id.action_log_out);
        homeDrawerHeaderLayoutBinding = DataBindingUtil.bind(drawerHeaderLayout);
        isEventListUpdated = false;
        isGuestUser = AppGlobalData.getInstance().isGuest();
        if (!isGuestUser){
            //registeredUserDetail = SharedPrefsUtils.getRegisteredUserPref(HomeLandingActivity.this, AppConstants.Prefrences.PREF_NAME, AppConstants.Prefrences.REGISTERED_USER_DETAIL);
            registeredUserDetail = AppGlobalData.getInstance().getRegisteredUserDetail();
            homeDrawerHeaderLayoutBinding.txtDrawerEditProfile.setVisibility(View.VISIBLE);
            homeDrawerHeaderLayoutBinding.txtDrawerGuestLogin.setVisibility(View.GONE);
            logoutItem.setVisible(true);
            setRegisteredUserDetail();

        }else{
            homeDrawerHeaderLayoutBinding.txtDrawerUserName.setText(getResources().getString(R.string.welcome_guest));
            homeDrawerHeaderLayoutBinding.txtDrawerUserDesignation.setVisibility(View.GONE);
            homeDrawerHeaderLayoutBinding.txtDrawerEditProfile.setVisibility(View.GONE);
            homeDrawerHeaderLayoutBinding.txtDrawerGuestLogin.setVisibility(View.VISIBLE);
            logoutItem.setVisible(false);

        }
        FragmentController.replaceHomeFrag(this,R.id.frag_container);
        toggleBottomNavigation(R.id.bottom_action_home);
        homeAddVideoFirmDialog=   new HomeAddVideoFirmDialog(this);
        binding.homeNavView.setNavigationItemSelectedListener(this);
        binding.homeNavViewBottom.setNavigationItemSelectedListener(this);
        ViewCompat.setNestedScrollingEnabled(binding.homeNavView.getChildAt(0), false);

        homeDrawerHeaderLayoutBinding.txtDrawerEditProfile.setOnClickListener(this);
        homeDrawerHeaderLayoutBinding.txtDrawerGuestLogin.setOnClickListener(this);
        //checking crashalytics

    }

    private void setRegisteredUserDetail() {
        if (registeredUserDetail != null){
            ImageLoader.getInstance().displayImage(registeredUserDetail.getProfile_pic(), homeDrawerHeaderLayoutBinding.imgDrawerUserImage);
            homeDrawerHeaderLayoutBinding.txtDrawerUserName.setText(registeredUserDetail.getFirst_name()+" "+registeredUserDetail.getLast_name());
            homeDrawerHeaderLayoutBinding.txtDrawerUserDesignation.setText(AppGlobalData.getInstance().getRegisteredUserDetail().getOccupation());
        }
    }

    public void onAddClicked(View view) {
        if (AppGlobalData.getInstance().isGuest()){
            showGuestUserAlert(HomeLandingActivity.this);
        }else{
            homeAddVideoFirmDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEventListUpdated){
            toggleBottomNavigation(0);
            FragmentController.replaceMyEventListFrag(this, R.id.frag_container);
            /*MyEventsListFragment frag = ((MyEventsListFragment) getSupportFragmentManager().findFragmentByTag(AppConstants.FRAGMENTS_CONST.MY_EVENT_FRAGMENT));
            frag.eventDataListToShow.clear();
            frag.pageNo= 1;
            frag.getData(ApiUtils.ApiActionEvents.GET_MY_EVENT_LIST);*/
            isEventListUpdated = false;
        }
        RegisteredUserDetail registeredUserDetail =AppGlobalData.getInstance().getRegisteredUserDetail();
        if (registeredUserDetail != null){
            if (registeredUserDetail.isProfileUpdated()){
                // Toast.makeText(this, "profile updated", Toast.LENGTH_SHORT).show();
                Glide.with(getApplicationContext())
                        .load(registeredUserDetail.getProfile_pic())
                        .apply(new RequestOptions().placeholder(ResourcesCompat.getDrawable(getResources(), R.drawable.placeholder_rect, null)))
                        .into(homeDrawerHeaderLayoutBinding.imgDrawerUserImage);
                homeDrawerHeaderLayoutBinding.txtDrawerUserDesignation.setText(registeredUserDetail.getOccupation());
                registeredUserDetail.setProfileUpdated(false);
                AppGlobalData.getInstance().setRegisteredUserDetail(registeredUserDetail);
                AppGlobalData.getInstance().saveUserDetailToPref(getApplicationContext());
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        isOnHome=false;

        handleMenuItemSelection(item);
        closeDrawerDelayed();
        switch (item.getItemId())
        {
            case R.id.action_blog_news_events:
                toggleBottomNavigation(0);
                FragmentController.replaceBlogFrag(this,R.id.frag_container);
                break;
            case R.id.action_my_events:
                if (AppGlobalData.getInstance().isGuest()){
                    showGuestUserAlert(HomeLandingActivity.this);
                }else{
                    toggleBottomNavigation(0);
                    FragmentController.replaceMyEventListFrag(this,R.id.frag_container);
                }
                break;
            case R.id.action_log_out:
                Utils.signOutFromApp(this);
                Intent i = new Intent(HomeLandingActivity.this, SplashActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

                break;
            case R.id.action_contact_us:
                startActivity(new Intent(this,ContactUsActivity.class));
                break;
            case R.id.action_law_firms:
                FragmentController.replaceFirmFrag(this,R.id.frag_container);
                break;
            case R.id.action_videos:
                toggleBottomNavigation(R.id.bottom_action_videos);
                FragmentController.replaceVideoFrag(this,R.id.frag_container);
                break;
            case R.id.action_my_videos:
                if (AppGlobalData.getInstance().isGuest()){
                    showGuestUserAlert(HomeLandingActivity.this);
                }else{
                    toggleBottomNavigation(0);
                    FragmentController.replaceMyVideoFrag(this,R.id.frag_container);
                }
                break;

            case R.id.action_my_law_firms:
                if (AppGlobalData.getInstance().isGuest()){
                    showGuestUserAlert(HomeLandingActivity.this);
                }else{
                    toggleBottomNavigation(0);
                    FragmentController.replaceMyFirmFrag(this, R.id.frag_container);
                    //Toast.makeText(this, "under development", Toast.LENGTH_SHORT).show();
                }
                break;
                /* case R.id.action_events:
                    toggleBottomNavigation(0);
                    FragmentController.replaceMyEventsFrag(this,R.id.frag_container);
                    break;*/
            case R.id.action_livestream_events:
                toggleBottomNavigation(R.id.bottom_action_livestream);
                FragmentController.replaceLiveStreamFragment(this,R.id.frag_container);

                break;
            case R.id.action_calender:
                toggleBottomNavigation(R.id.bottom_action_calendar);
                FragmentController.replaceEventListFragment(this,R.id.frag_container);

                break;

            case R.id.action_about_app:

                startActivity(new Intent(this,AboutUsActivity.class));
                break;
            case R.id.action_about_ceo:

                startActivity(new Intent(this,AboutCeoActivity.class));
                break;
            case R.id.action_t_and_c:

                startActivity(new Intent(this,CmsActivity.class).putExtra("type","tnc"));
                break;
            case R.id.action_share:
                shareTextUrl();
                break;
            case R.id.action_privacy_info:
                startActivity(new Intent(this,CmsActivity.class).putExtra("type","pp"));
                break;

            default:  Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void handleMenuItemSelection(MenuItem item) {

        if (prevMenuItem != null)
            prevMenuItem.setChecked(false);
        item.setChecked(true);
        prevMenuItem = item;
    }

    public void closeDrawerDelayed() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (binding != null) {
                    binding.homeDrawerLayout.closeDrawer(GravityCompat.START);
                    if (prevMenuItem != null)
                        prevMenuItem.setChecked(false);
                }


            }
        }, AppConstants.TIME.HOME_DRAWER_DELAY);
    }

    public void onHomeClicked(View view) {
        isOnHome=true;
        toggleBottomNavigation(R.id.bottom_action_home);
        FragmentController.replaceHomeFrag(this,R.id.frag_container);
    }
    public void onVideosClicked(View view) {
        isOnHome=false;
        toggleBottomNavigation(R.id.bottom_action_videos);
        FragmentController.replaceVideoFrag(this,R.id.frag_container);
    }
    public void onLiveStreamClicked(View view) {
        isOnHome=false;
        toggleBottomNavigation(R.id.bottom_action_livestream);
        Toast.makeText(this,"Under Development",Toast.LENGTH_SHORT).show();
    }
    public void onCalendarClicked(View view) {
        isOnHome=false;
        toggleBottomNavigation(R.id.bottom_action_calendar);
        FragmentController.replaceEventListFragment(this,R.id.frag_container);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (isOnHome)
        {

            super.onBackPressed();
        }
        else
        {
            FragmentController.replaceHomeFrag(this,R.id.frag_container);
            toggleBottomNavigation(R.id.bottom_action_home);
            isOnHome = true;
        }

    }

    public void toggleBottomNavigation(int id)
    {
        binding.homeBottomToolbar.bottomActionHome.setSelected(false);
        binding.homeBottomToolbar.bottomActionVideos.setSelected(false);
        binding.homeBottomToolbar.bottomActionLivestream.setSelected(false);
        binding.homeBottomToolbar.bottomActionCalendar.setSelected(false);
        switch (id)
        {
            case R.id.bottom_action_home:
                binding.homeBottomToolbar.bottomActionHome.setSelected(true);
                break;
            case R.id.bottom_action_videos:
                binding.homeBottomToolbar.bottomActionVideos.setSelected(true);
                break;
            case R.id.bottom_action_livestream:
                binding.homeBottomToolbar.bottomActionLivestream.setSelected(true);
                break;
            case R.id.bottom_action_calendar:
                binding.homeBottomToolbar.bottomActionCalendar.setSelected(true);
                break;
        }

    }


    public void onSettingsClicked(View view) {
        if (AppGlobalData.getInstance().isGuest()){
            showGuestUserAlert(HomeLandingActivity.this);
        }else{
            startActivity(new Intent(this,SettingsActivity.class));
        }
    }


    public void onMenuClicked(View view) {
        binding.homeDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private void shareTextUrl() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "MatterHornLaw Android App");
        share.putExtra(Intent.EXTRA_TEXT, "https://www.matterhornlaw.com");

        startActivity(Intent.createChooser(share, "Share App"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_drawer_edit_profile:
                startActivity(new Intent(this,EditProfileActivity.class));
                break;

            case R.id.txt_drawer_guest_login:
                startActivity(new Intent(this,LoginEmailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
        }

    }

}
