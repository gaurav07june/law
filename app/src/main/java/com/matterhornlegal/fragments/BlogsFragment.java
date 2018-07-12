package com.matterhornlegal.fragments;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.matterhornlegal.R;
import com.matterhornlegal.activities.BlogDetailActivity;
import com.matterhornlegal.adapters.BlogListAdapter;
import com.matterhornlegal.customui.NMGRecyclerView;
import com.matterhornlegal.databinding.FragmentBlogsBinding;
import com.matterhornlegal.models.BlogListResponseModel;
import com.matterhornlegal.models.BlogModel;
import com.matterhornlegal.utils.ApiUtils;
import com.matterhornlegal.utils.AppConstants;
import com.matterhornlegal.utils.Logger;
import com.matterhornlegal.utils.Utils;
import com.matterhornlegal.volley.GsonObjectRequest;
import com.matterhornlegal.volley.RequestManager;
import com.matterhornlegal.volley.VolleyErrorListener;

import java.util.ArrayList;
import java.util.List;


/**
 * By Karan Kalsi
 * A simple {@link Fragment} subclass.
 * Use the {@link BlogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlogsFragment extends BaseFragment implements NMGRecyclerView.OnMoreListener, BlogListAdapter.OnBlogClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentBlogsBinding binding = null;
    private LinearLayoutManager listManager;
    private GridLayoutManager gridManager;
    private BlogListAdapter blogListAdapter;
    private boolean isNewestFirst = true;
    private static final int GET_BLOG_LIST_ACTION = 101;
    private List<BlogModel> blogList = new ArrayList<>();
    int mPage = 1;

    public BlogsFragment() {
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
    public static BlogsFragment newInstance(String param1, String param2) {
        BlogsFragment fragment = new BlogsFragment();
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
                inflater, R.layout.fragment_blogs, container, false);
        View view = binding.getRoot();

        //  binding.videosRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));


        blogListAdapter = new BlogListAdapter(getContext(), blogList);
        gridManager = new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false);
        listManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        setListView();
        blogListAdapter.setOnBlogClickListener(this);
        binding.blogListToolbar.title.setText(R.string.blogs);
        binding.blogListToolbar.backBtn.setOnClickListener(this);
        binding.blogsRecycler.setOnMoreListener(this);
        binding.blogListToolbar.sortBtn.setOnClickListener(this);
        binding.blogsRecycler.setLoadMoreProgressView(binding.loadMoreProgressView);
        getData(GET_BLOG_LIST_ACTION);
        return view;
    }

    public List<BlogModel> getModels() {

        List<BlogModel> blogList = new ArrayList<>();
        BlogModel blog1 = new BlogModel();
        blog1.setBlogTitle("The standard chunk");
        blog1.setBlogImg("file:///android_asset/temp/blog1.jpg");
        blog1.setBlogDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit");

        BlogModel blog2 = new BlogModel();
        blog2.setBlogTitle("The standard chunk");
        blog2.setBlogImg("file:///android_asset/temp/blog2.jpg");
        blog2.setBlogDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit");


        blogList.add(blog1);
        blogList.add(blog2);


        return blogList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_btn:
                setSortType();
                break;
            case R.id.back_btn:
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
        }
    }

    private void setSortType() {
        if (binding.blogListProgressView.getVisibility() == View.VISIBLE ||
                binding.loadMoreProgressView.getVisibility() == View.VISIBLE) {
            Toast.makeText(getContext(), R.string.pleaseWait,Toast.LENGTH_SHORT).show();
            return;
        }
        binding.blogListToolbar.sortBtn.setSelected(!binding.blogListToolbar.sortBtn.isSelected());
        mPage = 1;
        getData(GET_BLOG_LIST_ACTION);

    }

    private void setListView() {
        binding.filterLayoutContainer.setVisibility(View.GONE);
        blogListAdapter.setViewType(AppConstants.VIEW_TYPE.LIST);
        binding.blogsRecycler.setLayoutManager(listManager);
        binding.blogsRecycler.setAdapter(blogListAdapter);
    }

    @Override
    public void getData(final int actionID) {
        super.getData(actionID);
        switch (actionID) {
            case GET_BLOG_LIST_ACTION:
                if (mPage == 1)
                    binding.blogListProgressView.setVisibility(View.VISIBLE);
                String sort_dir = !binding.blogListToolbar.sortBtn.isSelected() ?
                        AppConstants.SORT_DIR.ASC :
                        AppConstants.SORT_DIR.DSC;
                String payload = ApiUtils.getBlogListPayLoad(sort_dir, mPage).toString();
                RequestManager.addRequest(
                        new GsonObjectRequest<BlogListResponseModel>(
                                ApiUtils.ApiUrl.GET_BLOG_LIST, payload, BlogListResponseModel.class, new VolleyErrorListener(this, getActivity(), actionID)) {

                            @Override
                            protected void deliverResponse(BlogListResponseModel response) {

                                binding.blogListProgressView.setVisibility(View.GONE);
                                String data = new String(mResponse.data);
                                Logger.error("login response: " + data);

                                if (response.getStatus() == 1 && response.getBlogList() != null) {
                                    updateUi(true, actionID, response, 200);
                                } else {
                                    if (Utils.isNullOrEmpty(response.getMessage())) {
                                        updateUi(false, actionID, getString(R.string.errorGeneric), 200);
                                    } else {
                                        updateUi(false, actionID, response.getMessage(), 200);
                                    }
                                }
                            }

                            @Override
                            public void deliverError(VolleyError error) {
                                super.deliverError(error);
                                binding.blogListProgressView.setVisibility(View.GONE);
                            }
                        });
                break;
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse, int statusCode) {
        super.updateUi(status, actionID, serviceResponse, statusCode);
        switch (actionID) {
            case GET_BLOG_LIST_ACTION:
                BlogListResponseModel responseModel = (BlogListResponseModel) serviceResponse;
                if (responseModel.getBlogList() != null) {
                    if (mPage == 1) {
                        blogList.clear();
                        blogListAdapter.notifyDataSetChanged();
                        binding.blogsRecycler.scrollToPosition(0);
                    }
                    int position_start = blogList.size();
                    int item_count_inserted = responseModel.getBlogList().size();
                    if (position_start == 0) {

                        binding.blogsRecycler.setTotalPages(responseModel.getTotal_pages());

                    }
                    binding.blogsRecycler.onLoadedMore();

                    blogList.addAll(responseModel.getBlogList());
                    blogListAdapter.notifyItemRangeInserted(position_start, item_count_inserted);

                }

                break;
        }
    }

    @Override
    public void onMoreAsked(int page) {
        mPage = page;
        getData(GET_BLOG_LIST_ACTION);
    }


    @Override
    public void onBlogClick(BlogModel blog) {
        startActivity(new Intent(getActivity(), BlogDetailActivity.class).putExtra(AppConstants.EXTRA.BLOG_DATA, blog));
    }
}
