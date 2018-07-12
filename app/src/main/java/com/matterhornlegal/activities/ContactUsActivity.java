package com.matterhornlegal.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.matterhornlegal.R;
import com.matterhornlegal.customui.NMGButton;
import com.matterhornlegal.customui.NMGEditText;
import com.matterhornlegal.customui.NMGTextView;

/**
 * Created by seema.gurtatta on 10/24/2017.
 */
public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private NMGEditText mEtAddress, mEtContactNum, mEtEmail;
    private NMGButton mBtnSendMessage;
    private ImageView mIvMap;
    private Toolbar mToolbar;
    private NMGTextView mTvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setToolbar();
        initView();
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        mToolbar.setNavigationIcon(R.drawable.ic_back);
        actionBar.setTitle("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        mTvTitle = (NMGTextView) findViewById(R.id.titleTv);
        mTvTitle.setText(getString(R.string.contactUs));

        mEtAddress = (NMGEditText) findViewById(R.id.contactUsAddressEt);
        mEtContactNum = (NMGEditText) findViewById(R.id.contactUsContactNumberEt);
        mEtEmail = (NMGEditText) findViewById(R.id.contactUsEmailEt);

        mBtnSendMessage = (NMGButton) findViewById(R.id.contactUsSendMessageBtn);
        mIvMap = (ImageView) findViewById(R.id.contactUsMapImage);

        String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + getString(R.string.latLongContactUs) +
                "&size=1000x400&scale=2&zoom=13&markers=color:red|scale:2|label:M|" + getString(R.string.latLongContactUs);

        Glide.with(this).load(url).into(mIvMap);

        mBtnSendMessage.setOnClickListener(this);
        mEtAddress.setOnClickListener(this);
        mEtContactNum.setOnClickListener(this);
        mEtEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contactUsContactNumberEt: {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getString(R.string.contactNumberValue)));
                startActivity(intent);
            }
            break;
            case R.id.contactUsEmailEt: {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEtEmail.getText().toString()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }

            break;
            case R.id.contactUsSendMessageBtn: {
                Intent intent = new Intent(ContactUsActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
            break;
        }
    }
}
