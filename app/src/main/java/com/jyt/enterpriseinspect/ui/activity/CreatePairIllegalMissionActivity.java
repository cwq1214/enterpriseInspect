package com.jyt.enterpriseinspect.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.CreatePairIllegalMissionContract;
import com.jyt.enterpriseinspect.entity.ViolateType;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.CreatePairIllegalMissionPresenterImpl;
import com.jyt.enterpriseinspect.uitl.DensityUtils;
import com.jyt.enterpriseinspect.uitl.ScreenUtils;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by v7 on 2016/12/27.
 */
@ActivityAnnotation(title = "创建双违任务", showBack = true, showFunction = false)
public class CreatePairIllegalMissionActivity extends BaseActivity<CreatePairIllegalMissionContract.CreatePairIllegalMissionPresenter> implements CreatePairIllegalMissionContract.CreatePairIllegalMissionView {
    @BindView(R.id.selLocationImage)
    ImageView selLocationImage;
    @BindView(R.id.inputMissionName)
    EditText inputMissionName;
    @BindView(R.id.inputMissionDes)
    EditText inputMissionDes;
    @BindView(R.id.missionType)
    TextView missionType;
    @BindView(R.id.selMissionType)
    ImageView selMissionType;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.btn_done)
    TextView btnDone;
    @BindView(R.id.selImage)
    ImageView selImage;
    @BindView(R.id.addImage)
    FrameLayout addImage;
    @BindView(R.id.image_layout)
    FlowLayout imageLayout;




    ViolateType type;
    LatLng latLng;
    List<String> imagePath;

    @Override
    public CreatePairIllegalMissionContract.CreatePairIllegalMissionPresenter createPresenter() {
        return new CreatePairIllegalMissionPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_pair_illegal_mission;
    }

    @Override
    public void onViewCreated() {
        int width = (ScreenUtils.getScreenWidth(getContext())- DensityUtils.dp2px(getContext(),16))/4;
        int padding = DensityUtils.dp2px(getContext(),4);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width,width);
        addImage.setPadding(padding,padding,padding,padding);
        addImage.setLayoutParams(params);

    }
    @OnClick(R.id.selImage)
    public void onSelImageClick(){
        if (imagePath!=null&&imagePath.size()==5){
            showToast("最多只能上传5张");
            return;
        }
        IntentHelper.openSelImageActivityForResult(this);
    }


    @OnClick(R.id.selLocationImage)
    public void onSelLocationImageClick() {
        IntentHelper.openSelLocationActivity(getActivity(),null,null,true);
    }

    @OnClick(R.id.selMissionType)
    public void onSelMissionTypeClick() {
//        IntentHelper.openSelMissionTypeActivityForResult(getActivity());
        Http.getViolateType(getContext(), new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {

                if (data != null) {
                    final List<ViolateType> views = (List<ViolateType>) data;
                    List<String> name = new ArrayList<String>();
                    for (int i = 0, max = views.size(); i < max; i++) {
                        name.add(views.get(i).typename);
                    }

                    OptionsPickerView pickerView = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {

                            type = views.get(options1);
                            missionType.setText(type.typename);
                        }
                    }).build();

                    pickerView.setPicker(name);
                    pickerView.show();

                }
            }
        });

    }

    @OnClick(R.id.btn_done)
    public void onBtnDoneClick() {
        if (type == null) {
            ToastUtil.showShort(getContext(), "请选择类型");
            return;
        }
        if (latLng == null) {
            ToastUtil.showShort(getContext(), "请先选择定位");
            return;
        }
        if (imagePath==null||imagePath.size()==0){
            ToastUtil.showShort(getContext(),"请选择图片");
            return;
        }
        Http.addViolate(getContext(), UserInfo.getAccount(), type.typeid, latLng.longitude + "", latLng.longitude + "", inputMissionDes.getText().toString(),imagePath, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                if (isSuccess) {
                    ToastUtil.showShort(getContext(), "提交成功");
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentHelper.REQUIRE_CODE_SEL_LOCATION && resultCode == Activity.RESULT_OK) {
            onSelLocationResult(data);
        }else
        if (resultCode==Activity.RESULT_OK&&requestCode == IntentHelper.REQUIRE_CODE_SEL_IMAGE){
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            if (imagePath==null)
                imagePath = new ArrayList<>();

            imagePath.add(getRealFilePath(getContext(),uri));
            imageLayout.addView(createImageAfterSel(uri),0);
        }
    }

    private void onSelLocationResult(Intent data) {
        latLng = data.getParcelableExtra(IntentHelper.KEY_LATLNG);
        String address = data.getStringExtra(IntentHelper.KEY_ADDRESS);
        this.address.setText(address);

    }
    private ImageView createImageAfterSel(Uri uri){
        ImageView imageView = new ImageView(getContext());
        int width = (ScreenUtils.getScreenWidth(getContext())- DensityUtils.dp2px(getContext(),16))/4;
        int padding = DensityUtils.dp2px(getContext(),4);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width,width);
        imageView.setPadding(padding,padding,padding,padding);
        imageView.setLayoutParams(params);
        imageView.setOnLongClickListener(onLongClickListener);
        Glide.with(this).load(uri).into(imageView);
        imageView.setTag(uri.getPath());
        return imageView;
    }

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("移除照片");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((ViewGroup) v.getParent()).removeView(v);
                    for (int i=0;i<imagePath.size();i++){
                        if (imagePath.get(i).equals(v.getTag())){
                            imagePath.remove(i);
                            break;
                        }
                    }
                    dialog.dismiss();
                }
            });
            builder.show();
            return true;
        }
    };

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
