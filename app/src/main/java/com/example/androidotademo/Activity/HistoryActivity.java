package com.example.androidotademo.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.androidotademo.Adapter.HistoryAdapter;
import com.example.androidotademo.Utils.EmptyViewHelper;
import com.example.androidotademo.Utils.JApp;
import com.example.androidotademo.R;
import com.example.remoteupgradesdk.bean.UpdateVehicleTasksResBean;
import com.example.remoteupgradesdk.interfaces.ResponseCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rv)
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int currentPage = 1;
    List<UpdateVehicleTasksResBean.ResultBean> result;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        sharedPreferences= getSharedPreferences("data", Context.MODE_PRIVATE);
        initData();
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    private void initData() {
//        if (!NetworkUtils.isConnected()) {
//            ToastUtils.showShort("网络未连接");
//            return;
//        }
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new HistoryAdapter(this, R.layout.item_list_history, null);
        adapter.isFirstOnly(true);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.setEmptyView(new EmptyViewHelper(this).setContentView(R.mipmap.ic_no_order, "暂无信息记录"));
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
//            @Override
//            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//            }
//        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                getHistoryMessage(currentPage);
            }
        }, recyclerView);

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    private void getHistoryMessage(final int page) {
        setRefreshing(false);
        JApp.remoteUpdateManage.getHistoryUpdate(sharedPreferences.getString("vin","18888888888888886"), page, 10, JApp.uDate, new ResponseCallback<UpdateVehicleTasksResBean>() {
            @Override
            public void onSuccess(UpdateVehicleTasksResBean bean) {
                result = bean.getResult();
                if (result!=null){
                    if (page > 0) {
                        if (result.size() > 0) {
                            adapter.addData(result);
                            adapter.loadMoreComplete();
                        } else {
                            currentPage = page - 1;
                            adapter.loadMoreEnd();
                        }
                    } else {
                        if (result.size() > 0) {
                            adapter.setNewData(result);
                        } else {
                            adapter.setNewData(null);
                            adapter.setEmptyView(new EmptyViewHelper(HistoryActivity.this).setContentView(R.mipmap.ic_no_order, "暂无信息记录"));
                        }
                    }
                }

            }

            @Override
            public void onError(String msg) {
                setRefreshing(false);
                ToastUtils.showShort(msg);
            }
        });
    }

    public void setRefreshing(final boolean refreshing) {
        if (refreshLayout != null)
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (refreshLayout != null)
                        refreshLayout.setRefreshing(refreshing);
                }
            });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        getHistoryMessage(currentPage);
    }
}
