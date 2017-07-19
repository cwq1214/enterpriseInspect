package com.jyt.enterpriseinspect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.SelPeopleContract;
import com.jyt.enterpriseinspect.entity.Person;
import com.jyt.enterpriseinspect.entity.PersonCustom;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.SelPeoplePresenterImpl;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jyt.enterpriseinspect.helper.IntentHelper.KEY_TYPE;

/**
 * Created by chenweiqi on 2017/1/10.
 */
@ActivityAnnotation(showBack = true, title = "检查人员", functionText = "确定",showFunction = true)
public class SelPeopleActivity extends BaseActivity<SelPeopleContract.SelPeoplePresenter> implements SelPeopleContract.SelPeopleView {
    @BindView(R.id.layout)
    LinearLayout layout;

    List<String> userIds;
    boolean haveSelf = false;
    int type = 0;
    @Override
    public SelPeopleContract.SelPeoplePresenter createPresenter() {
        return new SelPeoplePresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sel_people;
    }

    @Override
    public void onViewCreated() {
        Intent intent = getIntent();
        if (intent!=null){
            userIds = intent.getStringArrayListExtra(IntentHelper.KEY_USER_IDS);
            haveSelf = intent.getBooleanExtra(IntentHelper.KEY_HAVE_SELF,false);
            type = intent.getIntExtra(KEY_TYPE,0);
        }

        if (type == 1) {
            getPeople();
        }else if (type == 2){
            getPeople2();
        }


    }

    @Override
    public void onFunctionClick() {
        List<Person> checkName = new ArrayList<>();
        for (int i=0;i<layout.getChildCount();i++){
            CheckBox checkBox = (CheckBox) layout.getChildAt(i);
            if (checkBox.isChecked()){
                checkName.add((Person) checkBox.getTag());
            }
        }
        if (checkName.size()==0){
            ToastUtil.showShort(getContext(),"请选择后再确定");
            return;
        }

        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(IntentHelper.KEY_PEOPLE, (ArrayList<Person>) checkName);
        setResult(RESULT_OK,intent);
        finish();


    }


    private void getPeople(){
        Http.getPeople(getContext(), new Http.HttpResultCallback<List<Person>>() {
            @Override
            public void result(boolean isSuccess, List<Person> data, String message) {
                layout.removeAllViews();
                List<Person> persons = data;
                if (persons == null){
                    persons = new ArrayList<Person>();
                }
//                if (haveSelf){
//                    Person person = new Person();
//                    person.realname = UserInfo.getREALNAME();
//                    person.user_id = UserInfo.getID();
//                    person.userid = UserInfo.getID();
//                    person.username = UserInfo.getREALNAME();
//                    person.user_name = UserInfo.getREALNAME();
//                    persons.add(person);
//                }

                if (isSuccess){
                    int size = persons.size();
                    for (int i=0;i<size;i++){
                        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_checkbox,layout,false);
                        ((CheckBox) view.findViewById(R.id.checkBox)).setText(persons.get(i).user_name);
                        ((CheckBox) view.findViewById(R.id.checkBox)).setTag(persons.get(i));

                        if (userIds!=null) {
                            for (int j=0;j<userIds.size();j++){
                                if (persons.get(i).user_id.equals(userIds.get(j))) {
                                    ((CheckBox) view.findViewById(R.id.checkBox)).setChecked(true);
                                }
                            }
                        }

                        layout.addView(view);
                    }
                }
            }
        });
    }

    private void getPeople2(){
        Http.getPeople(getContext(), new Http.HttpResultCallback<List<Person>>() {
            @Override
            public void result(boolean isSuccess, List<Person> data, String message) {
                layout.removeAllViews();
                List<Person> persons = data;
                if (persons == null){
                    persons = new ArrayList<Person>();
                }
                if (haveSelf){
                    Person person = new Person();
                    person.realname = UserInfo.getREALNAME();
                    person.user_id = UserInfo.getID();
                    person.userid = UserInfo.getID();
                    person.username = UserInfo.getREALNAME();
                    person.user_name = UserInfo.getREALNAME();
                    persons.add(person);
                }

                if (isSuccess){
                    int size = persons.size();
                    for (int i=0;i<size;i++){
                        View view= LayoutInflater.from(getContext()).inflate(R.layout.layout_checkbox,layout,false);
                        ((CheckBox) view.findViewById(R.id.checkBox)).setText(persons.get(i).user_name);
                        ((CheckBox) view.findViewById(R.id.checkBox)).setTag(persons.get(i));

                        if (userIds!=null) {
                            for (int j=0;j<userIds.size();j++){
                                if (persons.get(i).user_id.equals(userIds.get(j))) {
                                    ((CheckBox) view.findViewById(R.id.checkBox)).setChecked(true);
                                }
                            }
                        }

                        layout.addView(view);
                    }
                }
            }
        });
    }

}
