package com.example.androidotademo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.androidotademo.R;
import com.example.androidotademo.Utils.ChooseOptionCallBack;
import com.example.androidotademo.Utils.GlideImageLoader;
import com.example.androidotademo.Utils.JApp;
import com.example.androidotademo.View.AlertDialogView;
import com.example.remoteupgradesdk.api.RemoteUpdateManage;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {


    @BindView(R.id.setting)
    RelativeLayout setting;
    @BindView(R.id.titleBar)
    Banner banner;

    List<Integer> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    @BindView(R.id.vinSet)
    RelativeLayout vinSet;
    @BindView(R.id.one)
    RelativeLayout one;
    @BindView(R.id.two)
    RelativeLayout two;
    @BindView(R.id.three)
    RelativeLayout three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        JApp.remoteUpdateManage = new RemoteUpdateManage(this);
//        try {
//            JApp.remoteUpdateManage.initOtaSDK("32960.cer");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private void initData() {
        images.add(R.mipmap.one);
        images.add(R.mipmap.two);
        images.add(R.mipmap.three);
        titles.add("新一代新能源汽车，正在开发中。。。");
        titles.add("OTA升级功能已经发布！");
        titles.add("未来超乎你想象！");
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
            }
        });
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用//
        banner.start();
    }

    private void showDialog(String msg) {
        new AlertDialogView(this).show("系统提示", msg, "确认", null, new ChooseOptionCallBack() {
            @Override
            public void chooseOption(int var1) {
                switch (var1) {
                    case 1:
                        break;
                }
            }
        });
    }


    @OnClick({R.id.one, R.id.two, R.id.three, R.id.vinSet, R.id.setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.vinSet:
                showDialog("程序员正在开发中。。。。");
                break;
            case R.id.setting:
                startActivity(new Intent(this, SettingActivity.class));

                break;
        }
    }

}

