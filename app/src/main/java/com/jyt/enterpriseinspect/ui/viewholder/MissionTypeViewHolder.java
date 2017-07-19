package com.jyt.enterpriseinspect.ui.viewholder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.entity.MissionType;
import com.jyt.enterpriseinspect.entity.Type;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionTypeViewHolder extends BaseViewHolder {
    @BindView(R.id.name)
    TextView name;
    OnMissionTypeClickListener onMissionTypeClickListener;

    public MissionTypeViewHolder(ViewGroup view) {
        super(inflateView(view, R.layout.viewholder_mission_type));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMissionTypeClickListener!=null){
                    onMissionTypeClickListener.onClick(data,index);
                }
            }
        });
    }

    @Override
    public void setData(Object data, int index) {
        super.setData(data, index);

        boolean isChecked = false;
        if (data instanceof MissionType) {
            name.setText(((MissionType) data).type_name);
           isChecked = ((MissionType) data).isChecked;

        } else if (data instanceof Type) {
            name.setText(((Type) data).patrol_typename);
            isChecked = ((Type) data).isChecked;
        }


        if (isChecked){
            name.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
        }else {
            name.setTextColor(Color.BLACK);
        }

    }

    public void setOnMissionTypeClickListener(OnMissionTypeClickListener onMissionTypeClickListener) {
        this.onMissionTypeClickListener = onMissionTypeClickListener;
    }

    public interface OnMissionTypeClickListener {
        void onClick(Object data, int position);
    }
}
