package com.jyt.enterpriseinspect.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.adapter.MissionCheckedAdapter;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.MissionTypeDetailContract;
import com.jyt.enterpriseinspect.entity.Content;
import com.jyt.enterpriseinspect.entity.MissionTypeDetail;
import com.jyt.enterpriseinspect.entity.Person;
import com.jyt.enterpriseinspect.helper.Cache;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.MissionHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.MissionTypeDetailPresenterImpl;
import com.jyt.enterpriseinspect.uitl.DensityUtils;
import com.jyt.enterpriseinspect.uitl.ScreenUtils;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import org.apmem.tools.layouts.FlowLayout;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chenweiqi on 2017/1/10.
 */

public class MissionTypeDetailActivity extends BaseActivity<MissionTypeDetailContract.MissionTypeDetailPresenter> implements MissionTypeDetailContract.MissionTypeDetailView {
    //    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;


    List<String> imagePath;
    List<Person> persons;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;
    @BindView(R.id.inputDescription)
    EditText inputDescription;
    @BindView(R.id.selImage)
    ImageView selImage;
    @BindView(R.id.addImage)
    FrameLayout addImage;
    @BindView(R.id.selPeople)
    LinearLayout selPeople;
    @BindView(R.id.selPeopleLayout)
    LinearLayout selPeopleLayout;
    @BindView(R.id.sel)
    RadioButton sel;
    @BindView(R.id.disSel)
    RadioButton disSel;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.image_layout)
    FlowLayout imageLayout;



    MissionCheckedAdapter adapter;


    String type_id;
    String patrol_id;
    String missionTypeDetail;
    boolean create;
    @Override
    public MissionTypeDetailContract.MissionTypeDetailPresenter createPresenter() {
        return new MissionTypeDetailPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_mission_type_detail2;
    }

    @Override
    public void onViewCreated() {
        type_id = getIntent().getStringExtra(IntentHelper.KEY_TYPE_ID);
        patrol_id = getIntent().getStringExtra(IntentHelper.KEY_PATROL_ID);
        missionTypeDetail = getIntent().getStringExtra(IntentHelper.KEY_MISSION_TYPE);
        create = getIntent().getBooleanExtra(IntentHelper.KEY_CREATE,true);
        title.setText(missionTypeDetail);

        int width = (ScreenUtils.getScreenWidth(getContext())- DensityUtils.dp2px(getContext(),16))/4;
        int padding = DensityUtils.dp2px(getContext(),4);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width,width);
        addImage.setPadding(padding,padding,padding,padding);
        addImage.setLayoutParams(params);


        selPeopleLayout.setVisibility(View.GONE);

        if (!create){
            getTypeDetail();
        }

//        List<Person> mp = MissionHelper.persons;
//        boolean haveSelf = false;
//        for (int i = 0, max = mp.size(); i < max; i++) {
//            Person person = mp.get(i);
//            person.user_name = person.realname;
//            person.user_id = person.userid;
//            if (person.user_id.equals(UserInfo.getID())){
//                haveSelf = true;
//            }
//        }
//        if (!haveSelf){
//            Person person = new Person();
//            person.user_id = UserInfo.getID();
//            person.user_name = UserInfo.getREALNAME();
//            mp.add(person);
//        }
//        persons = mp;
//        createPeople();
    }

    private void getTypeDetail() {
        Http.getTypeDetail(getContext(),patrol_id , type_id, new Http.HttpResultCallback<MissionTypeDetail>() {
            @Override
            public void result(boolean isSuccess, MissionTypeDetail data, String message) {
                if (isSuccess) {

                    if (data!=null){
                        text.setText(data.former_remark);
                        if (data.former_imagelist!=null) {
                            int size = data.former_imagelist.size();
                            for (int i = 0; i < size; i++) {
//                                byte[] imgByte = null;
//                                try {
//                                    imgByte = URLDecoder.decode(data.former_imagelist.get(i).bytes,"ISO-8859-1").getBytes("ISO-8859-1");
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
                                flowLayout.addView(createImageView(Http.domain + data.former_imagelist.get(i),false));
                            }
                        }

                        inputDescription.setText(data.remark);
                        if (data.imagelist!=null) {
                            for (int i = 0, max = data.imagelist.size(); i < max; i++) {
//                                byte[] imgByte = null;
//                                try {
//                                    imgByte = URLDecoder.decode(data.imagelist.get(i).bytes,"ISO-8859-1").getBytes("ISO-8859-1");
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
                                Glide.with(getContext()).load(Http.domain + data.imagelist.get(i)).downloadOnly(new SimpleTarget<File>() {
                                    @Override
                                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                                        imageLayout.addView(createImageView(resource.getPath(),true), imageLayout.getChildCount() - 1);
                                        if (imagePath==null){
                                            imagePath = new ArrayList<String>();
                                        }
                                        imagePath.add(resource.getPath());
                                    }
                                });
                            }
                        }

                        for (int i = 0, max = data.userlist.size(); i<max; i++){
                            Person person = data.userlist.get(i);
                            person.user_name = person.username;
                            person.user_id = person.userid;
                        }

//                        persons = data.userlist;
//                        createPeople();

                        if (data.result==0){
                            sel.setChecked(false);
                            disSel.setChecked(true);
                        }else {
                            sel.setChecked(true);
                            disSel.setChecked(false);

                        }
                    }

                }
            }
        });
    }

    @OnClick(R.id.addImage)
    public void onSelImageClick(){
        if (imagePath!=null&&imagePath.size()==5){
            showToast("最多只能上传5张");
            return;
        }
        IntentHelper.openSelImageActivityForResult(this);
    }

    @OnClick(R.id.save)
    public void onSaveClick() {
//        Content content = new Content();
//        content.description = inputDescription.getText().toString();
//        content.imagePath = imagePath;
//        content.persons = persons;
//        content.qualified = sel.isChecked();
//        Cache.setMissionContent(patrol_id+"_"+type_id, content);
////        Cache.addMission(MissionHelper.currentMission);
//        showToast("缓存成功");
    }

    private ImageView createImageView(String imageUrl,boolean canDel){
        ImageView imageView = new ImageView(getContext());
//        imageView.setImageDrawable(imageView.getResources().getDrawable(R.mipmap.ic_launcher));
        Glide.with(getContext()).load(imageUrl).into(imageView);
        int width = (ScreenUtils.getScreenWidth(getContext())- DensityUtils.dp2px(getContext(),16))/4;
        int padding = DensityUtils.dp2px(getContext(),4);
        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width,width);
        imageView.setPadding(padding,padding,padding,padding);
        imageView.setLayoutParams(params);
        imageView.setTag(imageUrl);
        imageView.setOnClickListener(onClickListener);
        if (canDel)
            imageView.setOnLongClickListener(onLongClickListener);
        return imageView;
    }
//    private ImageView createImageView(byte[] imageUrl){
//        if (imagePath==null){
//            imagePath = new ArrayList<>();
//        }
//
//        final ImageView imageView = new ImageView(getContext());
////        imageView.setImageDrawable(imageView.getResources().getDrawable(R.mipmap.ic_launcher));
//        Glide.with(getContext()).load(imageUrl).downloadOnly(new SimpleTarget<File>() {
//            @Override
//            public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
//                System.out.println("1");
////                imageView.setImageBitmap(BitmapFactory.decodeFile(resource.getPath()));
//                imageView.setTag(resource.getPath());
//                imagePath.add(resource.getPath());
//
//            }
//        });
//        Glide.with(getContext()).load(imageUrl).asBitmap().into(imageView);
//        int width = (ScreenUtils.getScreenWidth(getContext())- DensityUtils.dp2px(getContext(),16))/4;
//        int padding = DensityUtils.dp2px(getContext(),4);
//        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(width,width);
//        imageView.setPadding(padding,padding,padding,padding);
//        imageView.setLayoutParams(params);
//        imageView.setOnClickListener(onClickListener);
//        imageView.setOnLongClickListener(onLongClickListener);
//        return imageView;
//    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), BrowImageActivity.class);
            intent.putExtra("imageUrl", (String) v.getTag());
            v.getContext().startActivity(intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentHelper.REQUIRE_CODE_SEL_PEOPLE){
            if (resultCode==RESULT_OK) {
                persons = data.getParcelableArrayListExtra(IntentHelper.KEY_PEOPLE);
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


    @OnClick(R.id.selPeople)
    public void onSelPeopleClick(){
        List personId = new ArrayList();
        if (persons!=null) {
            for (int i=0;i<persons.size();i++){
                personId.add( persons.get(i).user_id);
            }
        }
        if (create) {
            IntentHelper.openSelPeopleActivityForResult(this, personId, false,1);
        }else {
            IntentHelper.openSelPeopleActivityForResult(this, personId, false,2);
        }
    }

    private void createPeople(){
        if (selPeopleLayout.getChildCount()>2) {
            selPeopleLayout.removeViews(2, selPeopleLayout.getChildCount()-2);
        }
        if (persons==null){
            return;
        }
        int size = persons.size();
        for (int i = 0;i<size;i++){
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.RIGHT|Gravity.CENTER);
            textView.setText(persons.get(i).user_name);
            selPeopleLayout.addView(textView);
        }
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

    @OnClick(R.id.submit)
    public void onSubmitClick(){

        if (persons == null
                ||persons.size()==0){
            persons = MissionHelper.persons;
        }
        List<String> userIds = new ArrayList<>();
        if (persons!=null&&persons.size()>=2) {
            for (int i = 0; i < persons.size(); i++) {
                userIds.add(persons.get(i).user_id);

            }
        }else {

            ToastUtil.showShort(getContext(),"请选择至少两名检查人员");
            return;
        }

        if (imagePath==null||imagePath.size()==0){
            ToastUtil.showShort(getContext(),"请选择图片");
            return;
        }
        setSubmitBtnClickable(false);
        submitInput(inputDescription.getText().toString(),sel.isChecked(),userIds,imagePath);
    }

    private void setSubmitBtnClickable(final boolean clickable){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                submit.setEnabled(clickable);
            }
        });
    }


    private void submitInput(String description,boolean qualifed,List<String> userIds,List<String> imagePath){
        Http.submitContent(getContext(), patrol_id,type_id,description, qualifed?1:0,userIds, imagePath, new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                setSubmitBtnClickable(true);
                showToast(message);
                if (isSuccess){
                    finish();
                    Cache.removeMission();
                }
            }
        });
    }

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
