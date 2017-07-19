package com.jyt.enterpriseinspect.ui.viewholder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.MissionTypeDetail;
import com.jyt.enterpriseinspect.uitl.DensityUtils;
import com.jyt.enterpriseinspect.uitl.ScreenUtils;

import org.apmem.tools.layouts.FlowLayout;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/1/10.
 */
@ActivityAnnotation(showBack = true,title = " ")
public class MissionCheckedItemViewHolder extends BaseViewHolder<MissionTypeDetail> {
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;


    View.OnClickListener onClickListener;
    public MissionCheckedItemViewHolder(ViewGroup view) {
        super(inflateView(view, R.layout.viewholder_mission_checked_item));



    }

    @Override
    public void setData(MissionTypeDetail data, int index) {
        super.setData(data, index);

//
//        text.setText(data.patrol_content);
//        int size = data.patrol_images.size();
//        for (int i=0;i<size;i++){
//            flowLayout.addView(createImageView((data).patrol_images.get(i).image_url));
//        }

    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private ImageView createImageView(String imageUrl){
        ImageView imageView = new ImageView(itemView.getContext());
//        imageView.setImageDrawable(imageView.getResources().getDrawable(R.mipmap.ic_launcher));
        Glide.with(itemView.getContext()).load(Http.domain+imageUrl).into(imageView);
        int width = (ScreenUtils.getScreenWidth(itemView.getContext())-DensityUtils.dp2px(itemView.getContext(),16))/4;
        int padding = DensityUtils.dp2px(itemView.getContext(),4);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width,width);
        imageView.setPadding(padding,padding,padding,padding);
        imageView.setLayoutParams(params);
        imageView.setTag(imageUrl);
        imageView.setOnClickListener(onClickListener);
        return imageView;
    }
}
