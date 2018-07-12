package com.matterhornlegal.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.models.BlogModel;
import com.matterhornlegal.models.FirmModel;
import com.matterhornlegal.utils.AppCommons;
import com.matterhornlegal.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan.kalsi on 10/27/2017.
 */
//Updated file
public class BlogListAdapter extends RecyclerView.Adapter<BlogListAdapter.ViewHolder> {

    private List<BlogModel> blogList = new ArrayList<>();
    private Context mContext;
    private AppConstants.VIEW_TYPE viewType = AppConstants.VIEW_TYPE.GRID;


    public BlogListAdapter(Context context,List<BlogModel> blogList) {
        mContext=context;
        this.blogList=blogList;
    }

    public void setBlogList(List<BlogModel> blogList) {
        this.blogList.clear();
        this.blogList.addAll(blogList);
        notifyDataSetChanged();
    }
    public void addBlogList(List<BlogModel> blogList) {
        this.blogList.addAll(blogList);
        notifyDataSetChanged();
    }

    public List<BlogModel> getBlogList() {
        return blogList;
    }

    @Override
    public BlogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (this.viewType)
        {
            case GRID:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
                break;
            case LIST:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
                break;
            default:view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView blog_thumb;
        TextView blog_title;
        TextView blog_desc;
        TextView blog_date;

        public ViewHolder(View itemView) {
            super(itemView);

            blog_thumb = (ImageView) itemView.findViewById(R.id.blog_thumb);
            blog_title = (TextView) itemView.findViewById(R.id.blog_title);
            blog_desc = (TextView) itemView.findViewById(R.id.blog_desc);
            blog_date = (TextView) itemView.findViewById(R.id.blog_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onBlogClickListener!=null)onBlogClickListener.onBlogClick(blogList.get(position));
                }
            });
        }

        public void bind(int position) {
            blog_title.setText(blogList.get(position).getBlogTitle());
            Glide.with(itemView).load(blogList.get(position).getBlogImg())
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_rect))
                    .into(blog_thumb);
            blog_desc.setText(blogList.get(position).getBlogDescription());
            blog_date.setText(AppCommons.getMonthFullDayYear(mContext,blogList.get(position).getBlogDate()));
        }
    }

    public AppConstants.VIEW_TYPE getViewType() {
        return viewType;
    }

    public void setViewType(AppConstants.VIEW_TYPE viewType) {
        this.viewType = viewType;
    }

    public interface OnBlogClickListener
    {
        void onBlogClick(BlogModel blog);
    }
    private OnBlogClickListener onBlogClickListener;

    public BlogListAdapter.OnBlogClickListener getOnBlogClickListener() {
        return onBlogClickListener;
    }

    public void setOnBlogClickListener(BlogListAdapter.OnBlogClickListener onBlogClickListener) {
        this.onBlogClickListener = onBlogClickListener;
    }
}
