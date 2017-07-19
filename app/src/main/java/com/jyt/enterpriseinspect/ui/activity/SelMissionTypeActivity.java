package com.jyt.enterpriseinspect.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.MissionTypeAdapter;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.SelMissionTypeContract;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.entity.MissionType;
import com.jyt.enterpriseinspect.entity.Type;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.itemDecoration.RecycleViewDivider;
import com.jyt.enterpriseinspect.presenter.SelMissionPresenterImpl;
import com.jyt.enterpriseinspect.ui.viewholder.MissionTypeViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by chenweiqi on 2017/1/10.
 */
@ActivityAnnotation(showBack = true,title = "任务类型",functionText = "确定",showFunction = true)
public class SelMissionTypeActivity extends BaseActivity<SelMissionTypeContract.SelMissionPresenter> implements SelMissionTypeContract.SelMissionView {
    @BindView(R.id.tab1)
    RecyclerView tab1;
    @BindView(R.id.tab2)
    RecyclerView tab2;


    MissionTypeAdapter adapter1;
    MissionTypeAdapter adapter2;

    MissionType lastItem;
    MissionType lastType;

    @Override
    public SelMissionTypeContract.SelMissionPresenter createPresenter() {
        return new SelMissionPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sel_missiontype;
    }

    @Override
    public void onViewCreated() {

        tab1.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        tab1.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.dividerColor)));
        tab2.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        tab2.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL,1,getResources().getColor(R.color.dividerColor)));

        tab1.setAdapter(adapter1 = new MissionTypeAdapter());
//        adapter1.setDatas(missionTypes);
        adapter1.notifyDataSetChanged();
        adapter1.setOnMissionTypeClickListener(new MissionTypeViewHolder.OnMissionTypeClickListener() {
            @Override
            public void onClick(Object data, int position) {
                if (lastItem!=null)
                    lastItem.isChecked = false;
                MissionType missionType = (MissionType) data;
                missionType.isChecked = true;
                lastItem = missionType;


                adapter1.notifyDataSetChanged();



//                getMissionType(missionType.id);
                adapter2.setDatas(  lastItem.childrenMissionType);
                adapter2.notifyDataSetChanged();
            }
        });

        tab2.setAdapter(adapter2 = new MissionTypeAdapter());
        adapter2.setOnMissionTypeClickListener(new MissionTypeViewHolder.OnMissionTypeClickListener() {
            @Override
            public void onClick(Object data, int position) {

                if (lastType!=null)
                    lastType.isChecked=false;
                MissionType type1 = (MissionType) data;
                type1.isChecked = true;
                lastType = type1;

                adapter2.notifyDataSetChanged();
            }
        });

        getMissionType("");
    }



    private void getMissionType(final String id){
        Http.getMissionType(getContext(), id, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess){
                    List<MissionType> missionTypes = (List<MissionType>) data;
                    List<MissionType> type1 = new ArrayList<MissionType>();

                    for (int i=missionTypes.size()-1;i>=0;i--){
                        if (TextUtils.isEmpty(missionTypes.get(i).father_id)) {
                            MissionType missionType = missionTypes.get(i);
                            type1.add(missionType);
                            missionTypes.remove(missionType);
                        }
                    }

                    for (int i= missionTypes.size()-1;i>=0;i--){
                        for (int j = 0,max2 = type1.size();j<max2;j++){
                            if (missionTypes.get(i).father_id.equals(type1.get(j).id)){
                                if (type1.get(j).childrenMissionType==null){
                                    type1.get(j).childrenMissionType = new ArrayList<MissionType>();
                                }
                               type1.get(j).childrenMissionType.add(missionTypes.get(i));
                                missionTypes.remove(missionTypes.get(i));
                                break;
                            }
                        }
                    }

                    adapter1.setDatas(type1);
                    adapter1.notifyDataSetChanged();

                }
            }
        });
    }

    @Override
    public void onFunctionClick() {
        super.onFunctionClick();
        if (lastType==null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(IntentHelper.KEY_MISSION_TYPE, (Parcelable) lastType);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
