package com.example.androidotademo.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.androidotademo.R;
import com.example.androidotademo.Utils.JApp;
import com.example.androidotademo.View.ProgressDialogView;
import com.example.remoteupgradesdk.bean.VerIformationBean;
import com.example.remoteupgradesdk.bean.WebStateBean;
import com.example.remoteupgradesdk.interfaces.ResponseCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.cverTv)
    TextView cverTv;
    @BindView(R.id.updateBtn)
    Button updateBtn;
    @BindView(R.id.historyBtn)
    Button historyBtn;
    @BindView(R.id.setVin)
    Button setVin;

    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ProgressDialogView.show(this, "系统提示", "信息加载中。。。", false);
        JApp.remoteUpdateManage.getVersionInformation(sharedPreferences.getString("vin","18888888888888886"), JApp.uDate, new ResponseCallback<VerIformationBean>() {
            @Override
            public void onSuccess(VerIformationBean bean) {
                ProgressDialogView.dismiss();
                cverTv.setText(bean.getResult().getCVer().toString());
            }

            @Override
            public void onError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
        dialog = new ProgressDialog(SettingActivity.this);
        dialog.setTitle("系统提示");
        dialog.setMessage("升级资源检查中,请稍后...");
        dialog.setCancelable(true);
        dialog.setIndeterminate(false);

    }

    @OnClick({R.id.back, R.id.updateBtn, R.id.historyBtn,R.id.setVin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.updateBtn:
                ProgressDialogView.show(this, "系统提示", "升级资源检查中,请稍后...", false);
                JApp.remoteUpdateManage.queryState(sharedPreferences.getString("vin","18888888888888886"), JApp.taskId, JApp.uDate, new ResponseCallback<WebStateBean>() {
                    @Override
                    public void onSuccess(WebStateBean bean) {
                        if (bean.getResult().getStatus() == -1) {
                            ProgressDialogView.dismiss();
                            ToastUtils.showShort("暂无更新");
                        } else {
                            ProgressDialogView.dismiss();
                            startActivity(new Intent(SettingActivity.this, UpdateMessageActivity.class));
                        }

                    }

                    @Override
                    public void onError(String msg) {
                        ToastUtils.showShort(msg);
                    }
                });
                break;
            case R.id.historyBtn:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.setVin:
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                View viewVin= LayoutInflater.from(this).inflate(R.layout.dialog_vin_set_layout, null);
                TextView cancel =viewVin.findViewById(R.id.choosepage_cancel);
                TextView sure =viewVin.findViewById(R.id.choosepage_sure);
                final EditText edittext =viewVin.findViewById(R.id.choosepage_edittext);
                final Dialog dialog= builder.create();
                dialog.show();
                dialog.getWindow().setContentView(viewVin);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SettingActivity.this, "Vin码配置成功", Toast.LENGTH_SHORT).show();
                        editor.putString("vin",edittext.getText().toString());
                        editor.commit();
                        dialog.dismiss();
                    }
                });


                break;
        }
    }


}
