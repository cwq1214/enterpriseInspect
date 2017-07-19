package com.jyt.enterpriseinspect.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.contract.BaseContract;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by v7 on 2016/12/22.
 */

public abstract class BaseActivity<T extends BaseContract.BasePresenter> extends AppCompatActivity implements BaseContract.BaseView{

    @BindView(R.id.backBtn)
    protected View backBtn;
    @BindView(R.id.title)
    protected TextView title;
    @BindView(R.id.functionBtn)
    protected TextView functionBtn;

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        presenter = createPresenter();
        ButterKnife.bind(this);
        presenter.setContext(getContext());

        try {
            getAnnotation();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        onViewCreated();
    }

    @Optional
    @OnClick(R.id.backBtn)
    public void onBackClick(){
        finish();
    }

    @Optional
    @OnClick(R.id.functionBtn)
    public void onFunctionClick(){

    }

    private void getAnnotation() throws InvocationTargetException, IllegalAccessException {
        ActivityAnnotation annotation = this.getClass().getAnnotation(ActivityAnnotation.class);
        System.out.println(annotation);
        if (annotation==null){
            return;
        }
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Object invoke = method.invoke(annotation);
            System.out.println("invoke methd " + method.getName() + " result:" + invoke);
//            if (invoke.getClass().isArray()) {
//                Object[] temp = (Object[]) invoke;
//                for (Object o : temp) {
//                    System.out.println(o);
//                }
//            }
            if (method.getName().equals("showBack")){
                backBtn.setVisibility(((boolean) invoke)?View.VISIBLE:View.GONE);
            }else
            if (method.getName().equals("title")){
                title.setText((CharSequence) invoke);
            }else
            if (method.getName().equals("showFunction")){
                functionBtn.setVisibility(((boolean) invoke)?View.VISIBLE:View.GONE);
            }else
            if (method.getName().equals("functionText")){
                functionBtn.setText((CharSequence) invoke);
            }
        }
    }

    public void showToast(final String message){
        if (TextUtils.isEmpty(message)){
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setTitle(CharSequence title){
        super.setTitle(title);

        this.title.setText(title);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public Activity getActivity(){
        return this;
    }

    public abstract T createPresenter();

    public abstract int getLayoutId();

    public abstract void onViewCreated();
}
