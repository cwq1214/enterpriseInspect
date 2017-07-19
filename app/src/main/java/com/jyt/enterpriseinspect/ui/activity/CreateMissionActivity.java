package com.jyt.enterpriseinspect.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.CreateMissionContract;
import com.jyt.enterpriseinspect.entity.Company;
import com.jyt.enterpriseinspect.entity.Person;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.CreateMissionPresenterImpl;
import com.jyt.enterpriseinspect.uitl.DensityUtils;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/23.
 */
@ActivityAnnotation(showBack = true, title = "创建任务")
public class CreateMissionActivity extends BaseActivity<CreateMissionContract.CreateMissionPresenter> {
    @BindView(R.id.inputCompanName)
    AutoCompleteTextView inputCompanName;
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
    @BindView(R.id.createMission)
    TextView createMission;
    @BindView(R.id.searchBtn)
    TextView searchBtn;

    LatLng latLng;
    List<Person> persons;

    Company company;
    List<Company> companies;
    myAdapter myAdapter ;
    @Override
    public CreateMissionContract.CreateMissionPresenter createPresenter() {
        return new CreateMissionPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_mission;
    }

    @Override
    public void onViewCreated() {
        mission.setVisibility(View.GONE);
        searchBtn.setVisibility(View.VISIBLE);
        inputCompanName.setThreshold(1);
        inputCompanName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (companies!=null) {
                    initView(companies.get(position));
                }
            }
        });


        View firstView = selMember.getChildAt(0);
        selMember.removeAllViews();
        selMember.addView(firstView);

        int margin_8 = DensityUtils.dp2px(getContext(), 8);
        int margin_4 = DensityUtils.dp2px(getContext(), 4);


        if (persons==null){
            persons = new ArrayList<>();
        }
        Person person = new Person();
        person.realname = UserInfo.getREALNAME();
        person.user_id = UserInfo.getID();
        person.userid = UserInfo.getID();
        person.username = UserInfo.getREALNAME();
        person.user_name = UserInfo.getREALNAME();
        persons.add(person);
        TextView textView = new TextView(getContext());
        textView.setText(UserInfo.getREALNAME());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        textView.setLayoutParams(params);
        textView.setPadding(margin_8, margin_4, margin_8, margin_4);
        selMember.addView(textView);


//        inputCompanName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Http.searchCompany(getContext(), null, inputCompanName.getText().toString(), new Http.HttpResultCallback() {
//                    @Override
//                    public void result(boolean isSuccess, Object data, String message) {
//                        showToast(message);
//                        if (isSuccess){
//                           companies = (List<Company>) data;
//                            if (companies!=null){
//                                List<String> companyName = new ArrayList<String>();
//                                for (int i=0;i<companies.size();i++){
//                                    companyName.add(companies.get(i).companyname);
//                                }
//                                arrayAdapter.clear();
//                                arrayAdapter.addAll(companyName);
//                                arrayAdapter.notifyDataSetChanged();
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        inputCompanName.showDropDown();
//                                    }
//                                });
//                            }else {
//                                ToastUtil.showShort(getContext(),"查询无企业");
//                            }
//                        }
//                    }
//                });
//            }
//        });
//        company = getIntent().getParcelableExtra(IntentHelper.KEY_COMPANY);
//        if (company!=null){
//            searchBtn.setVisibility(View.GONE);
//
//            inputCompanLegalPerson.setText(company.legal_person);
//            inputCompanName.setText(company.companyname);
//            inputCompanNum.setText(company.company_code);
//            inputContact.setText(company.legal_person_tel);
//            inputSecurityHead.setText(company.respons_person);
//            inputSHeadContact.setText(company.response_person_tel);
//            textLocation.setText(company.company_address);
//            this.latLng = new LatLng(Double.valueOf(company.company_latitude),Double.valueOf(company.company_longitude));
//        }
    }


    @OnClick({R.id.selLocationBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selLocationBtn:
                setSelLocationBtn();
                break;
        }
    }

    @OnClick(R.id.searchBtn)
    public void onSearchBtnClick(){
//        IntentHelper.openSearchMissionActivityForResult(getContext());
        if (TextUtils.isEmpty(inputCompanName.getText().toString())) {
            ToastUtil.showShort(getContext(),"请输入企业名称");
            return;
        }

//        List list = Arrays.asList("1","2","#");
//        inputCompanName.setAdapter(myAdapter = new myAdapter<>(getActivity(),android.R.layout.simple_dropdown_item_1line,list));
//        inputCompanName.showDropDown();

            Http.searchCompany(getContext(), null, inputCompanName.getText().toString(), new Http.HttpResultCallback() {
                @Override
                public void result(boolean isSuccess, Object data, String message) {
                    showToast(message);
                    if (isSuccess){
                        companies = (List<Company>) data;
                        if (companies!=null){
                            List<String> companyName = new ArrayList<String>();
                            int max = Math.min(companies.size(),3);
                            for (int i=0;i<max;i++){
                                companyName.add(companies.get(i).company_name);
                            }
                                inputCompanName.setAdapter(myAdapter = new myAdapter(getContext(),android.R.layout.simple_dropdown_item_1line,companyName));

//                            inputCompanName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                    initView(companies.get(position));
//                                    arrayAdapter.clear();
//                                    arrayAdapter.notifyDataSetChanged();
//                                }
//                            });
                            inputCompanName.showDropDown();

                        }else {
                            ToastUtil.showShort(getContext(),"查询无企业");
                        }
                    }
                }
            });

    }


    private void initView(Company company){
        inputCompanLegalPerson.setText(company.legal_person);
        inputCompanName.setText(company.company_name);
        inputCompanNum.setText(company.company_code);
        inputContact.setText(company.legal_person_tel);
        inputSecurityHead.setText(company.respons_person);
        inputSHeadContact.setText(company.response_person_tel);
        textLocation.setText(company.company_address);
        try {
            latLng = new LatLng(Double.valueOf(company.company_latitude), Double.valueOf(company.company_longitude));
        }catch (Exception e) {
            e.printStackTrace();
//            showToast("该企业为未设置坐标");
        }
    }

    @OnClick(R.id.createMission)
    public void onCreateMissionClick() {
        if (persons == null || persons.size() < 2) {
            showToast("请选择至少两名检查人员");
            return;
        }
        if (latLng == null) {
            showToast("请先选择地址");
            return;
        }
        createMission();
    }

    private void createMission() {
        StringBuffer users = new StringBuffer();
        if (persons!=null)
        for (int i=0;i<persons.size();i++){
            users.append(persons.get(i).user_id);
            if (i+1!=persons.size())
                users.append(",");

        }

        Http.createMission(getContext()
                , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()))
                , inputCompanName.getText().toString()
                , inputCompanNum.getText().toString()
                , inputCompanLegalPerson.getText().toString()
                , inputContact.getText().toString()
                , inputSecurityHead.getText().toString()
                , inputSHeadContact.getText().toString()
                , textLocation.getText().toString()
                , latLng.longitude + ""
                , latLng.latitude + ""
                , users.toString()
                , new Http.HttpResultCallback() {
                    @Override
                    public void result(boolean isSuccess, Object data, String message) {
                        showToast(message);
                        if (isSuccess){
                            IntentHelper.openMissionListActivity(getContext());
                            finish();
                        }
                    }
                }
        );
    }


    public void setSelLocationBtn() {
        if (latLng!=null) {
            IntentHelper.openSelLocationActivity(this, latLng.longitude+"", latLng.latitude+"",true);
        }else {
            IntentHelper.openSelLocationActivity(this,null,null,true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IntentHelper.REQUIRE_CODE_SEL_LOCATION) {
            selLocationResult(data);
        } else if (requestCode == IntentHelper.REQUIRE_CODE_SEL_PEOPLE && resultCode == Activity.RESULT_OK) {
            selMemberResult(data);
        }
    }

    private void selLocationResult(Intent data) {
        String address = data.getExtras().getString(IntentHelper.KEY_ADDRESS);
        textLocation.setText(address);
        latLng = data.getParcelableExtra(IntentHelper.KEY_LATLNG);
    }

    private void selMemberResult(Intent data) {
        persons = data.getParcelableArrayListExtra(IntentHelper.KEY_PEOPLE);

        View firstView = selMember.getChildAt(0);
        selMember.removeAllViews();
        selMember.addView(firstView);

        int margin_8 = DensityUtils.dp2px(getContext(), 8);
        int margin_4 = DensityUtils.dp2px(getContext(), 4);
        for (int i = 0; i < persons.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(persons.get(i).user_name);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            textView.setLayoutParams(params);

            textView.setPadding(margin_8, margin_4, margin_8, margin_4);
            selMember.addView(textView);
        }
    }



    @OnClick(R.id.selMember)
    public void onSelMemberBtnClick() {
        List userIds = new ArrayList();
        if (persons!=null){
            for (int i=0;i<persons.size();i++){
                userIds.add(persons.get(i).user_id);
            }
        }
        IntentHelper.openSelPeopleActivityForResult(getActivity(),userIds,true,1);
    }


}
