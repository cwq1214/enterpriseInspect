package com.jyt.enterpriseinspect.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.widget.TouchImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/3/1.
 */

public class BrowImageActivity extends AppCompatActivity {

    @BindView(R.id.touchImageView)
    TouchImageView touchImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browimage);
        ButterKnife.bind(this);


        String imageUrl = getIntent().getStringExtra("imageUrl");

        Glide.with(this).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                touchImageView.setImageBitmap(resource);
            }
        });
    }

    @OnClick(R.id.backBtn)
    public void onBackClick(){
        onBackPressed();
    }
}

