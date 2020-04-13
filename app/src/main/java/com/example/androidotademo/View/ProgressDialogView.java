package com.example.androidotademo.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import java.util.Timer;
import java.util.TimerTask;

public class ProgressDialogView {

    private static ProgressDialog progressDialog;

    private ProgressDialogView() {
    }

    private static Timer timer;

    /**
     * //设置超时时间 单位 秒  默认20秒
     */
    private static int seconds = 20;

    /**
     * 显示一个进度弹出组件
     *
     * @param context 上下文
     * @param title   标题
     * @param message 进度条描述
     */
    public static void show(final Context context, String title, String message) {
        show(context, title, message, true);
    }

    public static void show(final Context context, String title, String message, boolean autoDissmiss) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgressDrawable(new BitmapDrawable());
//		progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            if (autoDissmiss) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            setSeconds(20);
                        }
                    }
                }, seconds * 1000);
            }
        }
    }

    public static void updateMessage(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
        }
    }

    /**
     * 隐藏进度组件
     */
    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            setSeconds(20);
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    /**
     * //设置超时时间 单位 秒  默认20秒  优先设置超时时间,然后调用 show()
     */
    public static void setSeconds(int seconds) {
        ProgressDialogView.seconds = seconds;
    }
}
