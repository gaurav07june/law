package com.matterhornlegal.customui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by karan.kalsi on 5/30/2017.
 */
public class NMGRecyclerView extends RecyclerView {
    private int[] lastScrollPositions;
    private LAYOUT_MANAGER_TYPE layoutManagerType;
    private int ITEM_LEFT_TO_LOAD_MORE = 0;
    private int mPage = 0;
    private int totalPages = 0;
    private View loadMoreProgressView = null;
    private boolean typeLoadMore=true;
    private static final int PAGE_ITEMS_LIMIT=10;
    private OnMoreListener mOnMoreListener;

    private boolean isLoadingMore = false;

    public View getLoadMoreProgressView() {
        return loadMoreProgressView;
    }

    public void setLoadMoreProgressView(View loadMoreProgressView) {
        this.loadMoreProgressView = loadMoreProgressView;
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public NMGRecyclerView(Context context) {
        super(context);
        init();
    }

    public NMGRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NMGRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!typeLoadMore)return;
                RecyclerView.LayoutManager layoutManager = getLayoutManager();
                int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (ITEM_LEFT_TO_LOAD_MORE!=0 && lastVisibleItemPosition == totalItemCount-1 && !isLoadingMore) {

                    isLoadingMore = true;
                    if (mOnMoreListener != null) {
                        if (loadMoreProgressView != null)
                            loadMoreProgressView.setVisibility(VISIBLE);
                        mOnMoreListener.onMoreAsked(mPage + 1);
                    }
                }
            }
        });
    }

    private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        if (layoutManagerType == null) {
            if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                lastVisibleItemPosition = caseStaggeredGrid(layoutManager);
                break;
        }
        return lastVisibleItemPosition;
    }


    private int caseStaggeredGrid(RecyclerView.LayoutManager layoutManager) {
        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        if (lastScrollPositions == null)
            lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];

        staggeredGridLayoutManager.findLastVisibleItemPositions(lastScrollPositions);
        return findMax(lastScrollPositions);
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max)
                max = value;
        }
        return max;
    }

    public void setTotalPages(int totalPages) {
        mPage = 0;
        isLoadingMore = false;
        this.totalPages=totalPages;
        ITEM_LEFT_TO_LOAD_MORE = totalPages*PAGE_ITEMS_LIMIT;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        isLoadingMore = loadingMore;
    }


    public OnMoreListener getOnMoreListener() {
        return mOnMoreListener;
    }

    public void setOnMoreListener(OnMoreListener mOnMoreListener) {

        isLoadingMore = false;
        this.mOnMoreListener = mOnMoreListener;
    }

    public int onLoadedMore() {
        mPage++;
        isLoadingMore = false;
        if (loadMoreProgressView != null)
            loadMoreProgressView.setVisibility(GONE);
        ITEM_LEFT_TO_LOAD_MORE -= PAGE_ITEMS_LIMIT;

        return mPage;
    }

    public void setCurrentPage(int page)
    {
        mPage=page;
        ITEM_LEFT_TO_LOAD_MORE=totalPages-mPage*PAGE_ITEMS_LIMIT;
    }

    public void removeMoreListener() {
        mOnMoreListener = null;
    }

    public interface OnMoreListener {
        public void onMoreAsked(int page);
    }
    public void setLoadMoreType(boolean state)
    {
        layoutManagerType=null;
        if (loadMoreProgressView!=null)loadMoreProgressView.setVisibility(GONE);
        typeLoadMore=state;

    }
}
