package com.jyt.enterpriseinspect.uitl;

import android.util.Log;

import java.util.List;
import java.util.Objects;

/**
 * Created by v7 on 2016/12/23.
 */

public class LogUtil {
    public static String TAG = "LogUtil";


    public static void e(String TAG,String key,Object message){
        if (message==null){
            Log.e(TAG,"object is null");
            return;
        }
        if (message instanceof List){
            printList(TAG,key,(List) message);
            return;
        }

        Log.e(TAG,"key:\t"+key+"\nvalue:\t"+message.toString());
    }
    public static void e(String key,Object message){
       e(TAG,key,message);
    }

    private  static void printList(String TAG,String key,List list){
        if (list.size()==0){
            LogUtil.e(TAG,"list size is zero");
        }
        for (int i=0;i<list.size();i++){
            LogUtil.e(TAG,"key:"+key+"\n"+i+"\n\t"+list.get(i).toString());
        }
    }
}
