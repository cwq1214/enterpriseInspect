package com.jyt.enterpriseinspect.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.baidu.mapapi.model.LatLng;
import com.baidu.platform.comapi.map.C;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.entity.Type;
import com.jyt.enterpriseinspect.ui.activity.CacheListActivity;
import com.jyt.enterpriseinspect.ui.activity.CreateMissionActivity;
import com.jyt.enterpriseinspect.ui.activity.CreatePairIllegalMissionActivity;
import com.jyt.enterpriseinspect.ui.activity.LoginActivity;
import com.jyt.enterpriseinspect.ui.activity.MainActivity;
import com.jyt.enterpriseinspect.ui.activity.MissionDetailActivity;
import com.jyt.enterpriseinspect.ui.activity.MissionListActivity;
import com.jyt.enterpriseinspect.ui.activity.MissionTypeDetailActivity;
import com.jyt.enterpriseinspect.ui.activity.ModifyPsdActivity;
import com.jyt.enterpriseinspect.ui.activity.PersonalCenterActivity;
import com.jyt.enterpriseinspect.ui.activity.PrintAskOrderActivity;
import com.jyt.enterpriseinspect.ui.activity.SearchLocationActivity;
import com.jyt.enterpriseinspect.ui.activity.SearchMissionActivity;
import com.jyt.enterpriseinspect.ui.activity.SelLocationActivity;
import com.jyt.enterpriseinspect.ui.activity.SelMissionTypeActivity;
import com.jyt.enterpriseinspect.ui.activity.SelPeopleActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by v7 on 2016/12/22.
 */

public class IntentHelper {
    public static final String KEY_CITY_NAME = "KEY_CITY_NAME";
    public static final String KEY_LATLNG = "KEY_LATLNG";
    public static final String KEY_POIINFO = "KEY_POIINFO";
    public static final String KEY_ADDRESS = "KEY_ADDRESS";
    public static final String KEY_DOCUMENT_PATH = "KEY_DOCUMENT_PATH";
    public static final String KEY_PEOPLE = "KEY_PEOPLE";
    public static final String KEY_MISSION_TYPE = "KEY_MISSION_TYPE";
    public static final String KEY_MISSION = "KEY_MISSION";
    public static final String KEY_TYPE_ID = "KEY_TYPE";
    public static final String KEY_PATROL_ID = "KEY_PATROLID";
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_LONGITUDE="KEY_LONGITUDE";
    public static final String KEY_LATITUDE="KEY_LATITUDE";
    public static final String KEY_COMPANY = "KEY_COMPANY";
    public static final String KEY_CREATE = "key_create";
    public static final String KEY_USER_IDS = "key_user_ids";
    public static final String KEY_HAVE_SELF = "key_have_self";
    public static final String KEY_EDIT_ADDRESS = "key_edit_address";



    public static final int REQUIRE_CODE_SEL_LOCATION = 1;
    public static final int REQUIRE_CODE_SEARCH_LOCATION = 2;
    public static final int REQUIRE_CODE_SEL_DOCUMMENT = 3;
    public static final int REQUIRE_CODE_SEL_PEOPLE = 4;
    public static final int REQUIRE_CODE_MISSION_TYPE = 5;
    public static final int REQUIRE_CODE_SEL_IMAGE = 6;
    public static final int REQUIRE_CODE_SEL_COMPANY = 7;

    //登陆界面
    public static void openLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    //修改密码
    public static void openForgetPsdActivity(Context context){
        Intent intent = new Intent(context, ModifyPsdActivity.class);
        context.startActivity(intent);
    }

    //回到首页
    public static void openMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    //创建任务
    public static void openCreateMissionActivity(Context context){
        Intent intent = new Intent(context, CreateMissionActivity.class);
        context.startActivity(intent);
    }

    //创建任务
    public static void openCreateMissionActivity(Context context,Mission mission){
        Intent intent = new Intent(context, CreateMissionActivity.class);
        intent.putExtra(KEY_MISSION,mission);
        context.startActivity(intent);
    }

    //任务列表
    public static void openMissionListActivity(Context context){
        Intent intent = new Intent(context, MissionListActivity.class);
        context.startActivity(intent);
    }


    //选择地址
    public static void openSelLocationActivity(Context context,String longitude,String latitude,boolean edit){
        Intent intent = new Intent(context, SelLocationActivity.class);
        intent.putExtra(KEY_LONGITUDE,longitude);
        intent.putExtra(KEY_LATITUDE,latitude);
        intent.putExtra(KEY_EDIT_ADDRESS,edit);
        if (context instanceof Activity)
        ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_SEL_LOCATION);
    }

    //搜索地址
    public static void openSearchLocationActivityForResult(Activity activity,LatLng latLng){
        Intent intent = new Intent(activity, SearchLocationActivity.class);
        intent.putExtra(KEY_LATLNG,latLng);
        activity.startActivityForResult(intent,REQUIRE_CODE_SEARCH_LOCATION);
    }

    //暂存任务
    public static void openCacheListActivity(Context context){
        Intent intent = new Intent(context, CacheListActivity.class);
        context.startActivity(intent);
    }

    //个人中心
    public static void openPersonalCenterActivity(Context context){
        Intent intent = new Intent(context, PersonalCenterActivity.class);
        context.startActivity(intent);
    }

    //双违治理
    public static void openCreatePairIllegalMissionActivity(Context context){
        Intent intent = new Intent(context, CreatePairIllegalMissionActivity.class);
        context.startActivity(intent);
    }

    //打印询问单
    public static void openPrintAskOrderActivity(Context context){
        Intent intent = new Intent(context,PrintAskOrderActivity.class);
        context.startActivity(intent);
    }

    //选者文件
    public static void openSelDocumentActivityForResult(Activity activity){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(intent,REQUIRE_CODE_SEL_DOCUMMENT);
    }

    //选择检查人员
    public static void openSelPeopleActivityForResult(Activity activity){
        Intent intent = new Intent(activity, SelPeopleActivity.class);
        activity.startActivityForResult(intent,REQUIRE_CODE_SEL_PEOPLE);
    }
    //选择检查人员
    public static void openSelPeopleActivityForResult(Activity activity, List userId , boolean haveSelf,int type){
        Intent intent = new Intent(activity, SelPeopleActivity.class);
        intent.putStringArrayListExtra(KEY_USER_IDS, (ArrayList<String>) userId);
        intent.putExtra(KEY_HAVE_SELF,haveSelf);
        intent.putExtra(KEY_TYPE,type);
        activity.startActivityForResult(intent,REQUIRE_CODE_SEL_PEOPLE);
    }

    //选择任务类型
    public static void openSelMissionTypeActivityForResult(Activity activity){
        Intent intent = new Intent(activity, SelMissionTypeActivity.class);
        activity.startActivityForResult(intent,REQUIRE_CODE_MISSION_TYPE);
    }

    //任务详情界面
    public static void openMissionDetailActivity(Context context, Mission mission){
        Intent intent = new Intent(context, MissionDetailActivity.class);
        intent.putExtra(IntentHelper.KEY_MISSION,mission);
        context.startActivity(intent);
    }

    //复检任务
    public static void openMissionTypeDetailActivity(Context context, Type type){
        Intent intent = new Intent(context, MissionTypeDetailActivity.class);
        intent.putExtra(KEY_TYPE,type);
        context.startActivity(intent);
    }
    //复检任务
    public static void openMissionTypeDetailActivity(Context context, String patrolid,String typeid,String missionType,boolean create){
        Intent intent = new Intent(context, MissionTypeDetailActivity.class);
        intent.putExtra(KEY_TYPE_ID,typeid);
        intent.putExtra(KEY_PATROL_ID,patrolid);
        intent.putExtra(KEY_MISSION_TYPE,missionType);
        intent.putExtra(KEY_CREATE,create);
        context.startActivity(intent);
    }

    //选择图片
    public static void openSelImageActivityForResult(Activity activity){
        Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent,REQUIRE_CODE_SEL_IMAGE);
    }

    //选择任务
    public static void openSearchMissionActivityForResult(Context context){
        Intent intent = new Intent(context, SearchMissionActivity.class);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent,REQUIRE_CODE_SEL_COMPANY);
        }
    }


}
