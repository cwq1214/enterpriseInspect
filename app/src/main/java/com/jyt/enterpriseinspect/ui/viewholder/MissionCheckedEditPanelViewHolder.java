package com.jyt.enterpriseinspect.ui.viewholder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.base.BaseViewHolder;
import com.jyt.enterpriseinspect.helper.IntentHelper;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionCheckedEditPanelViewHolder extends BaseViewHolder {
    @BindView(R.id.text)
    EditText text;
    @BindView(R.id.selLocationImage)
    ImageView selImage;
    @BindView(R.id.addImage)
    FrameLayout addImage;
    @BindView(R.id.selPeople)
    LinearLayout selPeople;
    @BindView(R.id.selPeopleLayout)
    LinearLayout selPeopleLayout;
    @BindView(R.id.rg)
    RadioGroup rg;

    Activity activity;

    public MissionCheckedEditPanelViewHolder(ViewGroup view) {
        super(inflateView(view, R.layout.viewholder_mission_checked_edit_panel));

        selPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openSelPeopleActivityForResult(activity);
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentHelper.openSelPeopleActivityForResult(activity);
            }
        });
    }

    @Override
    public void setData(Object data, int index) {
        super.setData(data, index);

    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
