package com.jyt.enterpriseinspect.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.BaseContract;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.entity.MissionType;
import com.jyt.enterpriseinspect.entity.Person;
import com.jyt.enterpriseinspect.entity.Type;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.MissionHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.model.MainModelImpl;
import com.jyt.enterpriseinspect.presenter.BasePresenterImpl;
import com.jyt.enterpriseinspect.uitl.LogUtil;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/1/10.
 */
@ActivityAnnotation(showBack = true)
public class MissionDetailActivity extends BaseActivity {
    @BindView(R.id.inputCompanName)
    EditText inputCompanName;
    @BindView(R.id.inputCompanNum)
    EditText inputCompanNum;
    @BindView(R.id.inputCompanLegalPerson)
    EditText inputCompanLegalPerson;
    @BindView(R.id.inputContact)
    EditText inputContact;
    @BindView(R.id.inputSecurityHead)
    EditText inputSecurityHead;
    @BindView(R.id.inputSHeadContact)
    EditText inputSHeadContact;
    @BindView(R.id.textLocation)
    TextView textLocation;
    @BindView(R.id.selLocationBtn)
    ImageView selLocationBtn;
    @BindView(R.id.selMemberBtn)
    ImageView selMemberBtn;
    @BindView(R.id.location)
    LinearLayout location;
    @BindView(R.id.selMember)
    LinearLayout selMember;
    @BindView(R.id.selMession)
    TextView selMession;
    @BindView(R.id.mission)
    LinearLayout mission;
    @BindView(R.id.btn_tijiao)
    TextView tijiao;
    @BindView(R.id.btn_zhenggai)
    TextView zhenggai;
    @BindView(R.id.btn_tingchan)
    TextView tingchan;


    Mission missionData;
    List<Person> persons;

    @Override
    public BaseContract.BasePresenter createPresenter() {
        return new BasePresenterImpl() {
            @Override
            public BaseContract.BaseModel createModel() {
                return new MainModelImpl();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mission_detail;
    }

    @Override
    public void onViewCreated() {

        missionData = getIntent().getParcelableExtra(IntentHelper.KEY_MISSION);
        inputCompanLegalPerson.setKeyListener(null);
        inputCompanName.setKeyListener(null);
        inputCompanNum.setKeyListener(null);
        inputContact.setKeyListener(null);
        inputSecurityHead.setKeyListener(null);
        inputSHeadContact.setKeyListener(null);

        inputCompanLegalPerson.setHint("");
        inputCompanName.setHint("");
        inputCompanNum.setHint("");
        inputContact.setHint("");
        inputSecurityHead.setHint("");
        inputSHeadContact.setHint("");


//        selMember.setVisibility(View.GONE);
//        selMemberBtn.setVisibility(View.GONE);

        getPerson();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMissionDetail();

    }

    private void getMissionDetail(){
        Http.getMissionDetail(getContext(), missionData.patrol_id, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess){
                    missionData = ((Mission) data);
                    createView(missionData);
//                    MissionHelper.persons = ((Mission) data).users;
                }
            }
        });
    }

    private void getPerson(){
        Http.getMissionPeople(getContext(), missionData.patrol_id, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess){
                    if (!TextUtils.isEmpty(data.toString())){
                        MissionHelper.persons = new Gson().fromJson(data.toString(),new TypeToken<List<Person>>(){}.getType());
                        boolean haveSelf = false;
                        for (Person p:
                                MissionHelper.persons) {
                            if (UserInfo.getID().equals(p.user_id)){
                                haveSelf = true;
                                break;
                            }
                        }

                        if (!haveSelf){
                            Person person = new Person();
                            person.realname = UserInfo.getREALNAME();
                            person.user_id = UserInfo.getID();
                            person.userid = UserInfo.getID();
                            person.username = UserInfo.getREALNAME();
                            person.user_name = UserInfo.getREALNAME();
                            MissionHelper.persons.add(0,person);
                        }

                        persons = MissionHelper.persons;
                        createPeople();
                    }


                }



            }
        });
    }
    @OnClick(R.id.selMember)
    public void selMemberClick(){
        List personId = new ArrayList();
        if (persons!=null) {
            for (int i=0;i<persons.size();i++){
                personId.add( persons.get(i).user_id);
            }
        }
        IntentHelper.openSelPeopleActivityForResult(getActivity(),personId,false,2);
    }


    private void createView(final Mission mission){
        inputCompanNum.setText(mission.company_code);
        inputCompanName.setText(mission.company_name);
        inputCompanLegalPerson.setText(mission.legal_person);
        inputContact.setText(mission.legal_person_tel);
        inputSHeadContact.setText(mission.response_person_tel);
        inputSecurityHead.setText(mission.response_person);
        textLocation.setText(mission.register_address);

        int size = mission.contents.size();
        if (MissionDetailActivity.this.mission.getChildCount()>1) {
            MissionDetailActivity.this.mission.removeViews(1,MissionDetailActivity.this.mission.getChildCount()-1);
        }else {

        }
        for (int i=0 ;i<size;i++){

            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_right_arrow_item,MissionDetailActivity.this.mission,false);
            view.setTag(mission.contents.get(i));
            view.findViewById(R.id.missionLayout).setOnClickListener(missTypeOnClickListener);
            ((TextView) view.findViewById(R.id.name)).setText(mission.contents.get(i).patrol_typename);
            MissionDetailActivity.this.mission.addView(view);
        }
    }

    @OnClick(R.id.selLocationBtn)
    public void onSelLocationBtnClick(){
        if (TextUtils.isEmpty(missionData.company_longitude)||TextUtils.isEmpty(missionData.company_latitude)){
//            showToast("此任务无经纬度，无法打开定位");
            LogUtil.e("无经纬度","无法打开定位");
            IntentHelper.openSelLocationActivity(getContext(),null,null,false);
            return;
        }
//        IntentHelper.openSelLocationActivity(getContext(),"113.323052","23.131304");
        IntentHelper.openSelLocationActivity(getContext(),missionData.company_longitude,missionData.company_latitude,false);
    }


    View.OnClickListener missTypeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (persons==null||persons.size()<2){
                ToastUtil.showShort(getContext(),"请选择至少两名检查人员");
                return;
            }
            IntentHelper.openMissionTypeDetailActivity(getContext(),missionData.patrol_id, ((Type) v.getTag()).type_id,missionData.patrol_type,false);
        }
    };

    @OnClick(R.id.selMession)
    public void onSelMessionClick(){
        if (persons==null||persons.size()<2){
            ToastUtil.showShort(getContext(),"请选择至少两名检查人员");
            return;
        }

        IntentHelper.openSelMissionTypeActivityForResult(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentHelper.REQUIRE_CODE_MISSION_TYPE&&resultCode==RESULT_OK){
            MissionType type = data.getParcelableExtra(IntentHelper.KEY_MISSION_TYPE);
            if (!TextUtils.isEmpty(type.id)
                    &&!TextUtils.isEmpty(missionData.patrol_id)){

                LogUtil.e("MissionType",type);
                IntentHelper.openMissionTypeDetailActivity(getContext(),missionData.patrol_id,type.id,missionData.patrol_type,true);
            }
        }else if (requestCode==IntentHelper.REQUIRE_CODE_SEL_PEOPLE){
            if (resultCode==RESULT_OK) {
                persons = data.getParcelableArrayListExtra(IntentHelper.KEY_PEOPLE);
                MissionHelper.persons = persons;
            }else {
//                persons = null;
            }
            if (persons==null)
                persons = new ArrayList<>();
//            Person person = new Person();
//            person.username = "张三";
//            persons.add(person);
//            persons.add(person);
            createPeople();
        }

    }

    private void createPeople(){
        if (selMember.getChildCount()>2) {
            selMember.removeViews(2, selMember.getChildCount()-2);
        }
        if (persons==null){
            return;
        }
        int size = persons.size();
        for (int i = 0;i<size;i++){
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.RIGHT|Gravity.CENTER);
            textView.setText(persons.get(i).user_name);
            selMember.addView(textView);
        }
    }

    @OnClick({R.id.btn_tijiao,R.id.btn_zhenggai,R.id.btn_tingchan})
    public void onMissionCommitClick(View view){
        int type = -1;
        String msg = null;
        if (view.getId()==R.id.btn_tijiao){
            type = 0;
            msg= "确认任务检查结果为合格操作吗?";
        }else if (view.getId() == R.id.btn_zhenggai){
            type = 1;
            msg= "确认任务检查结果为整改操作吗?";
        }else if (view.getId() == R.id.btn_tingchan){
            type = 2;
            msg= "确认任务检查结果为查封操作吗?";
        }else {
            return;
        }

        final int finalType = type;
        new AlertDialog.Builder(getContext()).setMessage(msg).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Http.missionCommit(getContext(), UserInfo.getAccount(), missionData.patrol_id, finalType, new Http.HttpResultCallback() {
                    @Override
                    public void result(boolean isSuccess, Object data, String message) {
                        showToast(message);
                        if (isSuccess){
                            finish();
                        }
                    }
                });
            }
        }).create().show();


    }


}
