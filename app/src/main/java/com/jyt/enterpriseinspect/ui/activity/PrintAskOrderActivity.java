package com.jyt.enterpriseinspect.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.print.PrintManager;
import android.printservice.PrintDocument;
import android.printservice.PrintJob;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jyt.enterpriseinspect.R;
import com.jyt.enterpriseinspect.annotation.ActivityAnnotation;
import com.jyt.enterpriseinspect.api.Http;
import com.jyt.enterpriseinspect.base.BaseActivity;
import com.jyt.enterpriseinspect.contract.PrintAskOrderContract;
import com.jyt.enterpriseinspect.entity.Company;
import com.jyt.enterpriseinspect.entity.Template;
import com.jyt.enterpriseinspect.helper.IntentHelper;
import com.jyt.enterpriseinspect.helper.UserInfo;
import com.jyt.enterpriseinspect.presenter.PrintAskOrderPresenterImpl;
import com.jyt.enterpriseinspect.uitl.ToastUtil;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**2
 * Created by v7 on 2016/12/28.
 */
@ActivityAnnotation(title = "打印询问单",showBack = true)
public class PrintAskOrderActivity extends BaseActivity<PrintAskOrderContract.PrintAskOrderPresenter> implements PrintAskOrderContract.PrintAskOrderView {
    @BindView(R.id.print)
    TextView print;
    @BindView(R.id.input_companyName)
    AutoCompleteTextView input_companyName;
    @BindView(R.id.templateName)
    TextView templateName;

    String TAG = getClass().getSimpleName();
    PrintJob printJob;
    PrintService printService;
    PrinterDiscoverySession printerDiscoverySession;
    PrintDocument printDocument;
    PrintManager printManager;
    List<Company> companies;

    File file;
    com.jyt.enterpriseinspect.ui.activity.myAdapter myAdapter;
    @Override
    public PrintAskOrderContract.PrintAskOrderPresenter createPresenter() {
        return new PrintAskOrderPresenterImpl();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_print_ask_order;
    }

    @Override
    public void onViewCreated() {
        printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
    }

    @OnClick(R.id.print)
    public void onPrintClick(){
        if (file==null){
            ToastUtil.showShort(getContext(),"文件不存在，请重新选择模版");
            return;
        }
        printFile(file);

//        IntentHelper.openSelDocumentActivityForResult(getActivity());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IntentHelper.REQUIRE_CODE_SEL_DOCUMMENT&&resultCode== Activity.RESULT_OK){
            onSelDocumentResult(data);
        }


    }
    @OnClick(R.id.searchBtn)
    public void onSearchBtnClick(){
        if (TextUtils.isEmpty(input_companyName.getText().toString())) {
            ToastUtil.showShort(getContext(),"请输入企业名称");
            return;
        }

//        List list = Arrays.asList("1","2","#");
//        inputCompanName.setAdapter(myAdapter = new myAdapter<>(getActivity(),android.R.layout.simple_dropdown_item_1line,list));
//        inputCompanName.showDropDown();

        Http.searchCompany(getContext(), null, input_companyName.getText().toString(), new Http.HttpResultCallback() {
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
                        input_companyName.setAdapter(myAdapter = new myAdapter(getContext(),android.R.layout.simple_dropdown_item_1line,companyName));

//                            inputCompanName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                                    initView(companies.get(position));
//                                    arrayAdapter.clear();
//                                    arrayAdapter.notifyDataSetChanged();
//                                }
//                            });
                        input_companyName.showDropDown();

                    }else {
                        ToastUtil.showShort(getContext(),"查询无企业");
                    }
                }
            }
        });
    }

    @OnClick(R.id.layout_selTemplate)
    public void onSelTemplateClick(){

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(input_companyName.getWindowToken(), 0);


        Http.templateList(getContext(), UserInfo.getAccount(), new Http.HttpResultCallback() {
            @Override
            public void result(boolean isSuccess, Object data, String message) {
                final List<Template> templates = (List<Template>) data;
    
                List<String> name = new ArrayList<String>();
                for (Template t :
                        templates) {
                    String misType = "";
                    if (t.templateType.equals("0")){
                        misType = "无检查记录整治通知单";
                    }else if (t.templateType.equals("1")){
                        misType = "整治通知单";

                    }else if (t.templateType.equals("2")){
                        misType = "检查记录";

                    }
                    name.add(t.patrolName +"\t"+misType);
                }
                


               OptionsPickerView optionsPickerView  = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String companyName = input_companyName.getText().toString();
                        if (TextUtils.isEmpty(companyName)){
                            ToastUtil.showShort(getContext(),"请输入企业名称");

                            return;
                        }else {

                            Template template = templates.get(options1);
                            templateName.setText(template.templateName);
                            Http.templateOK(getContext(), template.patrolid, companyName, template.templateType, new Http.HttpResultCallback() {
                                @Override
                                public void result(boolean isSuccess, Object data, String message) {
                                    String url = (String) data;
                                    if (TextUtils.isEmpty(url)){
                                        ToastUtil.showShort(getContext(),"文件不存在");
                                        return;
                                    }
                                    if (url.startsWith("http://")){
                                        Http.downLoadFile(url,new Http.HttpResultCallback() {
                                            @Override
                                            public void result(boolean isSuccess, Object data, String message) {
                                                showToast(message);
                                                file = (File) data;
                                                if (isSuccess){
                                                        showToast("文件储存在" + file.getAbsolutePath());
                                                        printFile(file);
                                                }

                                            }
                                        });
                                        ToastUtil.showShort(getContext(),"开始下载文件");
                                    }else {
                                        ToastUtil.showShort(getContext(),url);
                                    }
                                }
                            });
                        }

                    }

                })
                       .setSubmitText("确定")
                       .setCancelText("取消")
                       .setTitleText("请选择")
                       .build();
                optionsPickerView.setPicker(name);
                optionsPickerView.show();


            }
        });

    }

    private void printFile(File file){
        try {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            ComponentName componentName = new ComponentName(
                    "jp.co.canon.bsd.ad.pixmaprint",
                    "jp.co.canon.bsd.ad.pixmaprint.EulaActivity");
            intent.setComponent(componentName);
            intent.setDataAndType(uri,getMimeType(getFileByUri(uri)));
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

            new AlertDialog.Builder(getContext()).setMessage("您的手机未安装佳能打印，请安装后再打印").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("http://shouji.baidu.com/software/7905378.html");
                    intent.setData(content_url);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

        }
    }

    private void onSelDocumentResult(Intent data){
        Uri uri = data.getData();
        ToastUtil.showShort(getContext(),uri.toString());

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            ComponentName componentName = new ComponentName(
                    "jp.co.canon.bsd.ad.pixmaprint",
                    "jp.co.canon.bsd.ad.pixmaprint.EulaActivity");
            intent.setComponent(componentName);
            intent.setDataAndType(uri,getMimeType(getFileByUri(uri)));
            getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(getContext(),"您的手机未安装佳能打印，请安装后再打印");
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("http://shouji.baidu.com/software/7905378.html");
            intent.setData(content_url);
            startActivity(intent);
        }
    }

    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    public static String getMimeType(File file){
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    /**
     * 通过Uri返回File文件
     * 注意：通过相机的是类似content://media/external/images/media/97596
     * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
     * 通过查询获取实际的地址
     * @param uri
     * @return
     */
    public File getFileByUri(Uri uri) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = getActivity().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
}

