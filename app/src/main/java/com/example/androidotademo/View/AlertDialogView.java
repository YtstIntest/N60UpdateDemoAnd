package com.example.androidotademo.View;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidotademo.R;
import com.example.androidotademo.Utils.ChooseOptionCallBack;


/**
 * @author Zhaohao
 * @Description: 弹出确认取消提示框
 * @Date 2016/11/24 15:55
 */

public class AlertDialogView implements View.OnClickListener {

    private AlertDialog dialog;
    private Context context;

    private static AlertDialogView dialogView;


    private ChooseOptionCallBack optionCallBack;

    public AlertDialogView(Context context) {

        this.context = context;
    }

    public static AlertDialogView getInstance(Context context) {
        if (dialogView == null) {
            dialogView = new AlertDialogView(context);
        }
        return dialogView;
    }

    /**
     * @param tilte     提示标题
     * @param msg       提示内容
     * @param sureTip   确认按钮提示文字
     * @param cancleTip 取消按钮提示文字
     * @param callBack  按钮返回监听
     */
    public void show(String tilte, String msg, String sureTip, String cancleTip, ChooseOptionCallBack callBack) {
        this.optionCallBack = callBack;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_alert_layout, null);
        TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView messageText = (TextView) view.findViewById(R.id.message);
        titleText.setText(tilte);
        messageText.setText(msg);
        Button cancleButton = (Button) view.findViewById(R.id.cancleButton);
        cancleButton.setText(cancleTip);
        if (TextUtils.isEmpty(cancleTip)) {
            cancleButton.setVisibility(View.GONE);
        }
        cancleButton.setOnClickListener(this);
        Button sureButton = (Button) view.findViewById(R.id.sureButton);
        sureButton.setText(sureTip);
        sureButton.setOnClickListener(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancleButton) {
            if (optionCallBack != null) {
                optionCallBack.chooseOption(0);
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        } else if (v.getId() == R.id.sureButton) {
            if (optionCallBack != null) {
                optionCallBack.chooseOption(1);
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }
}

