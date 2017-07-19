package com.jyt.enterpriseinspect.uitl;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by v7 on 2016/12/24.
 */

public class ToastUtil {
    public static void showShort(final Context context, final String message){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
