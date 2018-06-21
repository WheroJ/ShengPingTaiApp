package com.cmcc.pp.ui.workorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.WorkOrder.WorkOrderInfoData;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.ui.workorder.adapter.WorkOrderAdapter;
import com.cmcc.pp.util.IntentUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2017/12/26.
 * 工单查询结果
 */

public class QueryResultsForWorkOrder extends BActivity {

    @Bind(R.id.tv_null)
    TextView tvNull;
    @Bind(R.id.tv_userAccount)
    TextView tvUserAccount;
    @Bind(R.id.content)
    RefreshLinearLayout content;

    @Bind(R.id.workorder_recyclerView)
    RecyclerView workorderRecyclerView;
    @Bind(R.id.workorder_swipeRefreshLayout)
    MaterialRefreshLayout workorderSwipeRefreshLayout;

    private String userAccount = "";
    private int pageIndex = 1;
    private int pageNum = 10;
    private int totalPage = 1;

    private int thisIndex = 0;

    //用于记录当前的状态
    private int curState = 0;

    //用于记录当前是何种状态，在请求完数据之后根据不同的状态进行不同的操作
    private static final int STATE_INIT = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_LOAD_MORE = 2;

    private WorkOrderInfoData workOrderInfoData;
    private ArrayList<WorkOrderInfoData> workOrderListData;
    private WorkOrderAdapter workOrderAdapter;
    private JSONObject resultObject;


    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_queryresultsforworkorder;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.str_queryresults);
        userAccount = getIntent().getStringExtra("userAccount");
        if (!TextUtils.isEmpty(userAccount)) {
            tvUserAccount.setText(userAccount);
            getWorkOrderListData();
            initRefreshLayout();
        }
    }

    @Override
    public void loadData() {

    }

    private void initRefreshLayout() {
//        SwipeRefreshLayout
        //设置支持下拉加载更多
        workorderSwipeRefreshLayout.setLoadMore(true);
        //刷新以及加载回调
        workorderSwipeRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                curState = STATE_REFRESH;
                pageIndex = 1;
                getWorkOrderListData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                curState = STATE_LOAD_MORE;
                pageIndex = pageIndex + 1;
                if (thisIndex < totalPage) {
                    getWorkOrderListData();
                } else {
                    ToastUtils.show("没有更多数据");
                    workorderSwipeRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void getWorkOrderListData() {
        UserHttpUtils.getWorkOrderListData(userAccount,
                pageIndex,
                pageNum,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try{
                            resultObject = new JSONObject(result);
                            Log.v("result",result);
                            totalPage = Integer.parseInt(resultObject.getString("pageTotal"));
                            ArrayList<WorkOrderInfoData> ListDataResult = JsonUtils.json2Obj(resultObject.getString("workOrderInfoData"), new TypeReference<ArrayList<WorkOrderInfoData>>() {});
                            if(ListDataResult != null && ListDataResult.size() >0 ){
                                if (workOrderListData == null || pageIndex == 1) {
                                    workOrderListData = ListDataResult;
                                } else {
                                    workOrderListData.addAll(ListDataResult);
                                }

                                thisIndex = thisIndex + workOrderListData.size();
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showData();
                    }
                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        showData();
                    }
                });
    }

    private void showData() {
        if(workOrderListData != null && workOrderListData.size() > 0){
            switch (curState) {
                case STATE_INIT:
                    //初始化状态，初始化列表
                    workorderRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    workOrderAdapter = new  WorkOrderAdapter(workOrderListData);

                    workOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            workOrderInfoData = workOrderListData.get(position);
                            openDetails(workOrderInfoData);
                        }
                    });
                    workorderRecyclerView.setAdapter(workOrderAdapter);
                    workorderRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    break;
                case STATE_REFRESH:
                    //下拉刷新状态，刷新数据，列表回到最顶端，关闭下拉刷新
                    workOrderAdapter.notifyDataSetChanged();
                    workorderRecyclerView.scrollToPosition(0);
                    workorderSwipeRefreshLayout.finishRefresh();
                    break;
                case STATE_LOAD_MORE:
                    //上拉加载更多状态，追加数据，关闭上拉加载更多
                    workOrderAdapter.notifyDataSetChanged();
                    workorderSwipeRefreshLayout.finishRefreshLoadMore();
                    break;
            }
            noDataViewGone();
        }else{
            noDataViewShow();
        }

    }

    private void noDataViewShow() {
        tvNull.setVisibility(View.VISIBLE);
        workorderSwipeRefreshLayout.setVisibility(View.GONE);
    }

    private void noDataViewGone() {
        tvNull.setVisibility(View.GONE);
        workorderSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    //工单查询结果
    private void openDetails(WorkOrderInfoData workOrderInfoData) {
        if(workOrderInfoData != null){
            IntentUtils.goWorkOrderDetails(this,workOrderInfoData.workOrderId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
