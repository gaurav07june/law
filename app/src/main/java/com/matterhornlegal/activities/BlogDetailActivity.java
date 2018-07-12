package com.matterhornlegal.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGTextView;
import com.matterhornlegal.databinding.ActivityBlogDetailBinding;
import com.matterhornlegal.models.BlogModel;
import com.matterhornlegal.utils.AppCommons;
import com.matterhornlegal.utils.AppConstants;

/**
 * Created by seema.gurtatta on 10/26/2017.
 */
public class BlogDetailActivity extends AppCompatActivity {
    private BlogModel blog;
    private ActivityBlogDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blog_detail);
        setViews();
    }

    private void getExtras() {
        if (getIntent().getExtras()!=null)
        {
            blog = (BlogModel) getIntent().getExtras().getSerializable(AppConstants.EXTRA.BLOG_DATA);
        }
    }

    private void setToolbar() {

        setSupportActionBar(binding.toolbarLayout.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        binding.toolbarLayout.toolbar.setNavigationIcon(R.drawable.ic_back);
        actionBar.setTitle("");
        binding.toolbarLayout.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setViews() {
        setToolbar();
        binding.toolbarLayout.titleTv.setText(getString(R.string.blog));
        if (blog != null) {

            binding.blogTitle.setText(blog.getBlogTitle());
            binding.blogDesc.setText(blog.getBlogDescription());
            binding.blogDate.setText(AppCommons.getMonthFullDayYear(this,blog.getBlogDate()));
            Glide.with(getApplicationContext()).load(blog.getBlogImg())
                    .apply(new RequestOptions().placeholder(R.drawable.placeholder_rect))
                    .into(binding.blogImage);
        }
        else
        {
            binding.blogTitle.setText("");
            binding.blogDesc.setText("");
            binding.blogDate.setText("");
            binding.blogImage.setImageResource(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_blog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "You have shared blog; " + "https://www.matterhornlaw.com/about-us/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;

            default:
                break;
        }
        return true;
    }


}
