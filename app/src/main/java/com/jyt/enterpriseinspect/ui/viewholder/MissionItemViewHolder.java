package com.jyt.enterpriseinspect.ui.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.Mission;

import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionItemViewHolder extends BaseViewHolder<Mission> {
    @BindView(R.id.missionName)
    TextView missionName;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.companyName)
    TextView companyName;
    @BindView(R.id.location)
    TextView location;
    OnItemClickListener onItemClickListener;
    View.OnLongClickListener onLongClickListener;
    public MissionItemViewHolder(ViewGroup view) {
        super(inflateView(view, R.layout.viewholder_missiion_item));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(data,index);
                }
            }
        });
        itemView.setOnLongClickListener(onLongClickListener);
    }

    @Override
    public void setData(Mission data, int index) {
        super.setData(data, index);
        missionName.setText(data.patrol_type);
        if (!TextUtils.isEmpty(data.patrol_date))
            date.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.valueOf(data.patrol_date))));
        location.setText(data.patrol_status);
        companyName.setText(data.companyname);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public interface OnItemClickListener{
        void onClick(Mission mission,int position);
    }
}
