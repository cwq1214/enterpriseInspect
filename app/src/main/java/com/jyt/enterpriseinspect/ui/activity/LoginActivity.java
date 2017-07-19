package com.jyt.enterpriseinspect.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.LoginContract;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.LoginPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/22.
 */
@ActivityAnnotation(showBack = false,title = "登录")
public class LoginActivity extends BaseActivity<LoginContract.LoginPresenter> implements LoginContract.LoginView {
    @BindView(R.id.inputUserName)
    EditText inputUserName;
    @BindView(R.id.inputPassword)
    EditText inputPassword;
    @BindView(R.id.login)
    TextView login;
    @BindView(R.id.forgetPsd)
    TextView forgetPsd;

    @Override
    public LoginContract.LoginPresenter createPresenter() {
        return new LoginPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onViewCreated() {
        if (!TextUtils.isEmpty(UserInfo.getAccount())&&!TextUtils.isEmpty(UserInfo.getPassword())){
            login(UserInfo.getAccount(),UserInfo.getPassword());
        }
    }

    @OnClick({R.id.login,R.id.forgetPsd})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login:
                onLoginClick();
                break;
            case R.id.forgetPsd:
                onForgetPsdClick();
                break;
        }
    }


   public void onLoginClick(){

       String userName = inputUserName.getText().toString();
       String psd = inputPassword.getText().toString();
       if (TextUtils.isEmpty(userName)){
           showToast("请输入用户名");
           return;
       }
       if (TextUtils.isEmpty(psd)){
           showToast("请输入密码");
           return;
       }
       login(userName,psd);

   }

    private void login( String userName,  String psd){
        UserInfo.setAccount(userName);
        UserInfo.setPassword(psd);
        Http.login(getContext(), userName, psd, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                showToast(message);

                if (isSuccess){
                    IntentHelper.openMainActivity(getContext());
                    finish();
                }
            }
        });
    }
   public void onForgetPsdClick(){
       IntentHelper.openForgetPsdActivity(getContext());
   }
}
