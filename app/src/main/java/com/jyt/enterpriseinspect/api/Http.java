package com.jyt.enterpriseinspect.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jyt.enterpriseinspect.entity.Company;
import com.jyt.enterpriseinspect.entity.Mission;
import com.jyt.enterpriseinspect.entity.MissionType;
import com.jyt.enterpriseinspect.entity.MissionTypeDetail;
import com.jyt.enterpriseinspect.entity.Person;
import com.jyt.enterpriseinspect.entity.PersonCustom;
import com.jyt.enterpriseinspect.entity.Template;
import com.jyt.enterpriseinspect.entity.UploadImageBean;
import com.jyt.enterpriseinspect.entity.ViolateType;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.uitl.FileUtil;
import com.jyt.enterpriseinspect.uitl.LogUtil;
import com.jyt.enterpriseinspect.uitl.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by chenweiqi on 2017/2/28.
 */

public class Http {
    public static String domain = "http://59.110.44.243";

    private static String SERVICE_URL = domain+"/cxf/app?wsdl";

    private static String NAME_SPACE = "http://interfaces.jbox.com/";

    private static String METHOD_LOGIN = "login";

    private static String METHOD_MISSION_TODAY= "mission_today";

    private static String METHOD_MISSION_WEEK= "mission_week";

    private static String METHOD_MISSION_MONTH= "mission_month";

    private static String METHOD_MISSION_DETAIL = "mission_detail";

    private static String METHOD_MISSION_CONTENT_DETAIL = "content_detail";

    private static String METHOD_GET_PERSON = "getPerson";

    private static String METHOD_CREATE_CONTENT = "createContent";

    private static String METHOD_CREATE_MISSION = "createMission";

    private static String METHOD_SEARCH_MISSION = "queryCompany";

    private static String METHOD_GET_MISSION_TYPE = "getPatrolType";

    private static String METHOD_GET_CONTENT = "getContent";

    private static String METHOD_GET_VIOLATE_TYPE = "getViolateType";

    private static String METHOD_TEMPLATE_List = "templateList";

    private static String METHOD_TEMPLATE_OK = "templateOK";

    private static String METHOD_ADDVIOLATE = "addViolate";

    private static String METHOD_MISSION_COMMIT = "mission_commit";

    private static String METHOD_GET_MY_INFO = "getMyInfo";

    private static String METHOD_GET_PATROL_PERSON = "getPatrolPerson";


    public static void login(Context context , final String userName, final String password, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_LOGIN);
                soapObject.addProperty("username",userName);
                soapObject.addProperty("password",password);
                soapSerializationEnvelope.bodyOut = soapObject;
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                    String returnResult = detail.toString();
                    if (returnResult.equals("0")){
                        callback.result(true,null,"登录成功");
                    }else if (returnResult.equals("1")){
                        callback.result(false,null,"登录用户名或密码错误");
                    }else if (returnResult.equals("2")){
                        callback.result(false,null,"此用户不存在");
                    }else if (returnResult.equals("3")){
                        callback.result(false,null,"此用户被禁用");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }


    public static void getMissionList(final Context context, final String date, final HttpResultCallback callback){

        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = null;
                if (date.equals("today")) {
                    soapObject = new SoapObject(NAME_SPACE, METHOD_MISSION_TODAY);
                }else if (date.equals("week")){
                    soapObject = new SoapObject(NAME_SPACE, METHOD_MISSION_WEEK);
                }else if (date.equals("month")){
                    soapObject = new SoapObject(NAME_SPACE, METHOD_MISSION_MONTH);
                }
                soapObject.addProperty("username", UserInfo.getAccount());
                envelope.bodyOut = soapObject;
                try {
                    transportSE.call(null,envelope);
                    LogUtil.e("http",METHOD_MISSION_MONTH);
                    LogUtil.e("in",envelope.bodyIn);
                    LogUtil.e("out",envelope.bodyOut);
                    final String result = envelope.getResponse().toString();
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         callback.result(!TextUtils.isEmpty(result),result,null);
                                     }
                                 }
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                                     @Override
                                     public void run() {
                                         callback.result(false,null,null);
                                     }
                                 }
                    );
                }

            }
        }.start();
    }

    public static void getMissionDetail(final Context context , final String patrolId, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();

                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                transportSE.debug = true;
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_MISSION_DETAIL);
                soapObject.addProperty("patrolid",patrolId);
                envelope.bodyOut = soapObject;
                try {
                    transportSE.call(null,envelope);
                    LogUtil.e("in",envelope.bodyIn);
                    LogUtil.e("out",envelope.bodyOut);
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                            callback.result(!TextUtils.isEmpty(detail.toString()),new Gson().fromJson(detail.toString(),new TypeToken<Mission>(){}.getType()),null);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    System.out.println(envelope.bodyIn.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void getTypeDetail(final Context context , final String patrol_id,final String type_id, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();

                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                transportSE.debug = true;
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_CONTENT);
                soapObject.addProperty("patrolid",patrol_id);
                soapObject.addProperty("typeid",type_id);
                envelope.bodyOut = soapObject;
                try {
                    LogUtil.e("http","getTypeDetail");
                    LogUtil.e("out",envelope.bodyOut);
                    transportSE.call(null,envelope);
                    LogUtil.e("in",envelope.bodyIn);
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            Object detail = result.getProperty("return");
                            if (detail instanceof SoapObject){
                                callback.result(!TextUtils.isEmpty(detail.toString()), null, null);
                            }else if (detail instanceof SoapPrimitive){
                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                callback.result(!TextUtils.isEmpty(detail.toString()), gson.fromJson(detail.toString(), new TypeToken<MissionTypeDetail>() {
                                }.getType()), null);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    System.out.println(envelope.bodyIn.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //获取检查人员
    public static void getPeople(final Context context, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();

                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                transportSE.debug = true;
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_PERSON);

                envelope.bodyOut = soapObject;
                try {
                    transportSE.call(null,envelope);
                    LogUtil.e("http","getPeople");
                    LogUtil.e("in",envelope.bodyIn);
                    LogUtil.e("out",envelope.bodyOut);
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                            LogUtil.e("t str",detail.toString());
                            callback.result(!TextUtils.isEmpty(detail.toString()),new Gson().fromJson(detail.toString(),new TypeToken<List<Person>>(){}.getType()),null);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    System.out.println(envelope.bodyIn.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //获取复查人员
    public static void getPeople2(final Context context, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();

                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                transportSE.debug = true;
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_PERSON);

                envelope.bodyOut = soapObject;
                try {
                    transportSE.call(null,envelope);
                    LogUtil.e("http","getPeople");
                    LogUtil.e("in",envelope.bodyIn);
                    LogUtil.e("out",envelope.bodyOut);
                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                            LogUtil.e("t str",detail.toString());
                            callback.result(!TextUtils.isEmpty(detail.toString()),new Gson().fromJson(detail.toString(),new TypeToken<List<Person>>(){}.getType()),null);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    System.out.println(envelope.bodyIn.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void submitContent(final Context context, final String patrol_id, final String type_id, final String remarks, final int result, final List<String> userIds, final List<String> localImgPath , final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();

                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                transportSE.debug = true;
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_CREATE_CONTENT);

                List<String> imagebytes = new ArrayList<String>();




                soapObject.addProperty("patrolid",patrol_id);
                soapObject.addProperty("typeid",type_id);
                soapObject.addProperty("remark",remarks);
                soapObject.addProperty("result",result);

                for (int i=0;i<userIds.size();i++){
                    soapObject.addProperty("userids",userIds.get(i));
                }

                if (localImgPath!=null) {
                    ToastUtil.showShort(context,"开始压缩图片");
                    for (int i = 0, max = localImgPath.size(); i < max; i++) {
                        try {

                            File file = new File(Glide.getPhotoCacheDir(context).getPath()+"/temp.jpg");

                            Bitmap bitMap = BitmapFactory.decodeFile(localImgPath.get(i));


                            //图片允许最大空间   单位：KB
                            double maxSize =300.00;
                            //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            //将字节换成KB
                            double mid = b.length/1024;
                            //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
                            if (mid > maxSize) {
                                //获取bitmap大小 是允许最大大小的多少倍
                                double ii = mid / maxSize;
                                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                                bitMap = FileUtil.zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(ii),
                                        bitMap.getHeight() / Math.sqrt(ii));
                            }

                            baos.reset();

                            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);



                            imagebytes.add(URLEncoder.encode(new String(baos.toByteArray(),"ISO-8859-1"),"ISO-8859-1"));
//                            imagebytes.add(URLEncoder.encode(new String(FileUtil.toByteArray3(file.getPath()),"ISO-8859-1"),"ISO-8859-1"));

                            if (file.exists()){
                                file.delete();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.result(false,null,e.getMessage());
                        }

                    }
                    for (int i=0;i<imagebytes.size();i++) {
                        soapObject.addProperty("imagebytes",imagebytes.get(i));
                    }
                }



                LogUtil.e("out",envelope.bodyOut);
                envelope.bodyOut = soapObject;
                try {
                    ToastUtil.showShort(context,"开始上传");
                    transportSE.call(null,envelope);
                    LogUtil.e("http","getTypeDetail");
                    LogUtil.e("in",envelope.bodyIn);

                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                            try {
                                JSONObject jsonObject = new JSONObject(detail.toString());
                                if (jsonObject.getBoolean("success")){
                                    callback.result(true,null,"提交成功");
                                }else {
                                    callback.result(false,null,"提交失败");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                callback.result(false,null,"提交失败");

                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());

                }
            }
        }.start();
    }

    public static void createMission(final Context context, final String date, final String companyName, final String companyCode, final String legalPerson, final String legalPersonTel, final String responsePerson, final String responsePersonTel, final String address, final String longitude, final String latitude, final String protral_user, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();

                HttpTransportSE transportSE = new HttpTransportSE(SERVICE_URL);
                transportSE.debug = true;
                final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_CREATE_MISSION);
//                JSONObject jsonObject = new JSONObject();
////                JSONArray jsonArray = new JSONArray();
//                try {
//                    JSONObject addprotral = new JSONObject();
//                    addprotral.put("companyname",companyName);
//                    addprotral.put("companycode",companyCode);
//                    addprotral.put("legalPerson",legalPerson);
//                    addprotral.put("legalPersonTel",legalPersonTel);
//                    addprotral.put("responsePerson",responsePerson);
//                    addprotral.put("responsePersonTel",responsePersonTel);
//                    addprotral.put("address",address);
//                    addprotral.put("company_longitude",company_longitude);
//                    addprotral.put("company_latitude",company_latitude);
//                    String[] user = protral_user.split(",");
//                    for (int i=0,max = user.length;i<max;i++){
//                        addprotral.put("userid"+(i+1),user[i]);
//                    }
////                    jsonArray.put(addprotral);
////                    jsonObject.put("addprotral",jsonArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                soapObject.addProperty("companyname",companyName);
                soapObject.addProperty("companycode",companyCode);
                soapObject.addProperty("legalPerson",legalPerson);
                soapObject.addProperty("legalPersonTel",legalPersonTel);
                soapObject.addProperty("responsePerson",responsePerson);
                soapObject.addProperty("responsePersonTel",responsePersonTel);
                soapObject.addProperty("address",address);
                soapObject.addProperty("longitude",longitude);
                soapObject.addProperty("latitude",latitude);
                String[] user = protral_user.split(",");
                for (int i=0,max = user.length;i<5;i++){
                    if (i<max) {
                        soapObject.addProperty("userid" + (i + 1), user[i]);
                    }else {
                        soapObject.addProperty("userid" + (i + 1), "");
                    }
                }
                envelope.bodyOut = soapObject;
                try {
                    LogUtil.e("out",envelope.bodyOut);
                    transportSE.call(null,envelope);
                    LogUtil.e("http","getTypeDetail");
                    LogUtil.e("in",envelope.bodyIn);

                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            SoapObject result = (SoapObject) envelope.bodyIn;
                            SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                            LogUtil.e("t str",detail.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(detail.toString());
                                if (jsonObject.getBoolean("success")){
                                    callback.result(true,null,jsonObject.getString("msg"));
                                }else {
                                    callback.result(false,null,jsonObject.getString("msg"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    System.out.println(envelope.bodyIn.toString());
                    e.printStackTrace();
                }

            }
        }.start();;
    }

    public static void searchCompany(final Context context , final String searchKey, final String searchValue, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_SEARCH_MISSION);
                soapObject.addProperty("companyname",searchValue);
                soapSerializationEnvelope.bodyOut = soapObject;
                try {
                    Log.e("http out",soapSerializationEnvelope.bodyOut.toString());
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http in",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    final Object detail = result.getProperty("return");


                    String returnResult = detail.toString();
                    Log.e("result",returnResult);
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (detail instanceof SoapObject){
                                callback.result(false,null,"查无企业");

                            }else if (detail instanceof SoapPrimitive){
                                List<Company> companies = null;
                                companies = new Gson().fromJson(detail.toString(),new TypeToken<List<Company>>(){}.getType());
                                callback.result(true,companies,"");

                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static void getMissionType(final Context context , final String id, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_MISSION_TYPE);
                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("father_id",id);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                soapObject.addProperty("arg0",jsonObject.toString());
                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                    final List<MissionType>  companies= new Gson().fromJson(detail.toString(),new TypeToken<List<MissionType>>(){}.getType());
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.result(true,companies,"");
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static void getViolateType(final Context context,final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_VIOLATE_TYPE);
                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                    final List<ViolateType>  violateTypes= new Gson().fromJson(detail.toString(),new TypeToken<List<ViolateType>>(){}.getType());
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.result(true,violateTypes,"");
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static  void templateList(final Context context,final String userName,final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_TEMPLATE_List);
                soapObject.addProperty("username",userName);

                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");
                    final List<Template>  templates= new Gson().fromJson(detail.toString(),new TypeToken<List<Template>>(){}.getType());
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.result(true,templates,"");
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static void templateOK(final Context context,final String patrolid,final String companyname,final String templatetype,final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_TEMPLATE_OK);
                soapObject.addProperty("patrolid",patrolid);
                soapObject.addProperty("companyname",companyname);
                soapObject.addProperty("templatetype",templatetype);

                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");

                    final String fileUrl = detail.toString();
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.result(true,fileUrl,"");
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static void addViolate(final Context context,final String username,final String typeid,final String lng,final String lat,final String inutContext,final List<String> localImgPath,final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_ADDVIOLATE);
                soapObject.addProperty("username",username);
                soapObject.addProperty("typeid",typeid);
                soapObject.addProperty("lng",lng);
                soapObject.addProperty("lat",lat);
                soapObject.addProperty("context",inutContext);

                List imagebytes = new ArrayList();
                if (localImgPath!=null) {
                    for (int i = 0, max = localImgPath.size(); i < max; i++) {
                        try {

                            File file = new File(Glide.getPhotoCacheDir(context).getPath()+"/temp.jpg");

                            Bitmap bitMap = BitmapFactory.decodeFile(localImgPath.get(i));


                            //图片允许最大空间   单位：KB
                            double maxSize =300.00;
                            //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            //将字节换成KB
                            double mid = b.length/1024;
                            //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
                            if (mid > maxSize) {
                                //获取bitmap大小 是允许最大大小的多少倍
                                double ii = mid / maxSize;
                                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                                bitMap = FileUtil.zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(ii),
                                        bitMap.getHeight() / Math.sqrt(ii));
                            }

                            baos.reset();

                            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                            byte[] bytes = FileUtil.readStream(new FileInputStream(new File(localImgPath.get(i))));
//                            imagebytes.add(Arrays.toString(bytes));
                            imagebytes.add(URLEncoder.encode(new String(baos.toByteArray(),"ISO-8859-1"),"ISO-8859-1"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.result(false,null,e.getMessage());
                        }

                    }
                    for (int i=0;i<imagebytes.size();i++) {
                        soapObject.addProperty("imagebytes",imagebytes.get(i));
                    }
                }

                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    SoapPrimitive detail = (SoapPrimitive) result.getProperty("return");

                    final String fileUrl = detail.toString();
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.result(true,fileUrl,"");
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static void downLoadFile(String url, final HttpResultCallback callback){
        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),url.substring(url.lastIndexOf("/"))) {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.result(false,null,"下载失败");
            }

            @Override
            public void onResponse(File response, int id) {
                callback.result(true,response,"下载成功");
            }
        });
    }


    public static void missionCommit(final Context context,final String userName, final String patrol_id, final int type,final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                final SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_MISSION_COMMIT);
                soapObject.addProperty("username",userName);
                soapObject.addProperty("patrol_id",patrol_id);
                soapObject.addProperty("type",type);


                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    final Object detail =  result.getProperty("return");


                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (detail instanceof SoapObject){
                                callback.result(false,null,"");

                            }else if (detail instanceof SoapPrimitive){

                                try {
                                    JSONObject jsonObject = new JSONObject(detail.toString());
                                    boolean success = jsonObject.getBoolean("success");
                                    String msg = jsonObject.getString("msg");
                                    callback.result(success,null,msg);

                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    callback.result(false,null,"提交失败");
                                }

                            }
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,"提交失败");
                }
            }
        }.start();
    }


    public static void getMyInfo(final Context context , final String userName, final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                final SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_MY_INFO);
                soapObject.addProperty("username",userName);
                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    final Object detail =  result.getProperty("return");


                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (detail instanceof SoapObject){
                                callback.result(false,null,"");

                            }else if (detail instanceof SoapPrimitive){
                                callback.result(true,detail.toString(),null);
                            }
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }

    public static void getMissionPeople(final Context context,final String patrolid,final HttpResultCallback callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                HttpTransportSE httpTransportSE = new HttpTransportSE(SERVICE_URL);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                final SoapObject soapObject = new SoapObject(NAME_SPACE,METHOD_GET_PATROL_PERSON);
                soapObject.addProperty("patrolid",patrolid);
                soapSerializationEnvelope.bodyOut = soapObject;
                Log.e("out",  soapSerializationEnvelope.bodyOut.toString());
                try {
                    httpTransportSE.call(null,soapSerializationEnvelope);
                    Log.e("http",soapSerializationEnvelope.bodyIn.toString());

                    soapSerializationEnvelope.getResponse();
                    SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    final Object detail =  result.getProperty("return");


                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (detail instanceof SoapObject){
                                callback.result(false,null,"");

                            }else if (detail instanceof SoapPrimitive){
                                callback.result(true,detail.toString(),null);
                            }
                        }
                    });
                    Log.e("result",detail.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.result(false,null,e.getMessage());
                }
            }
        }.start();
    }


    public interface HttpResultCallback<T>{
        void result(boolean isSuccess,T data,String message);
    }
}
