package com.cmcc.pp.ui.workorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.WorkOrder.WorkOrderInfoData;
import com.cmcc.pp.entity.WorkOrder.WorkOrderOperationLogDetailEntity;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.ui.workorder.adapter.WorkOrderAdapter;
import com.cmcc.pp.ui.workorder.adapter.WorkOrderOperationLogDetailListAdapter;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.ToastUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2017/12/26.
 * 工单--操作记录
 */

public class OperationNoteForWorkOrder extends BActivity {
    @Bind(R.id.operationnoteforworkorder_tv_operationLogDayTime)
    TextView operationnoteforworkorderTvOperationLogDayTime;
    @Bind(R.id.operationnoteforworkorder_tv_operationLogStatus)
    TextView operationnoteforworkorderTvOperationLogStatus;
    @Bind(R.id.operationnoteforworkorder_recyclerView)
    RecyclerView operationnoteforworkorderRecyclerView;
    @Bind(R.id.operationnoteforworkorder_swipeRefreshLayout)
    MaterialRefreshLayout operationnoteforworkorderSwipeRefreshLayout;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.tv_null)
    TextView tvNull;
    private String operationLOID = "";
    private String operationLogDayTime = "";
    private int pageIndex = 1;
    private int pageNum = 10;
    private int totalPage = 1;
    private int thisIndex = 0;
    private JSONObject resultObject;
    private WorkOrderOperationLogDetailEntity workOrderOperationLogDetailEntity;
    private ArrayList<WorkOrderOperationLogDetailEntity> arrayListDate;
    private WorkOrderOperationLogDetailListAdapter OperationLogDetailListAdapter;

    //用于记录当前的状态
    private int curState = 0;

    //用于记录当前是何种状态，在请求完数据之后根据不同的状态进行不同的操作
    private static final int STATE_INIT = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_LOAD_MORE = 2;

    private String strTips = "";//提示文字
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    break;
            }
        }
    };
    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_operationnoteforworkorder;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.workorderoperationnote);
        operationLOID = getIntent().getStringExtra("operationLOID");
        operationLogDayTime = getIntent().getStringExtra("operationLogDayTime");
        if (!TextUtils.isEmpty(operationLOID) && !TextUtils.isEmpty(operationLogDayTime)) {
            getWorkOrderOperationLogDetailListData();
            initRefreshLayout();
        }
    }


    @Override
    public void loadData() {

    }

    private void initRefreshLayout() {
//        SwipeRefreshLayout
        //设置支持下拉加载更多
        operationnoteforworkorderSwipeRefreshLayout.setLoadMore(true);
        //刷新以及加载回调
        operationnoteforworkorderSwipeRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                curState = STATE_REFRESH;
                pageIndex = 1;
                getWorkOrderOperationLogDetailListData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                curState = STATE_LOAD_MORE;
                pageIndex = pageIndex + 1;
                if (thisIndex < totalPage) {
                    getWorkOrderOperationLogDetailListData();
                } else {
                    ToastUtils.show("没有更多数据");
                    operationnoteforworkorderSwipeRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    //获取数据
    private void getWorkOrderOperationLogDetailListData() {
        UserHttpUtils.getWorkOrderOperationLogDetailListData(operationLOID,
                operationLogDayTime,
                pageIndex,
                pageNum,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        try{
                            resultObject = new JSONObject(result);
                            Log.v("result",result);
                            totalPage = Integer.parseInt(resultObject.getString("pageTotal"));
                            ArrayList<WorkOrderOperationLogDetailEntity> ListDataResult = JsonUtils.json2Obj(resultObject.getString("workOrderOperationLogDetailList"),
                                    new TypeReference<ArrayList<WorkOrderOperationLogDetailEntity>>() {});
                            if(ListDataResult != null && ListDataResult.size() >0 ){
                                if (arrayListDate == null || pageIndex == 1) {
                                    arrayListDate = ListDataResult;
                                } else {
                                    arrayListDate.addAll(ListDataResult);
                                }

                                thisIndex = thisIndex + arrayListDate.size();
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showData();
                    }
                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        strTips = JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage());
                        handler.sendEmptyMessageDelayed(1, 3000);
                        showData();
                    }
                });
    }

    private void showData() {
        if(arrayListDate != null && arrayListDate.size() > 0){
            switch (curState) {
                case STATE_INIT:
                    //初始化状态，初始化列表
                    operationnoteforworkorderRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    OperationLogDetailListAdapter = new WorkOrderOperationLogDetailListAdapter(arrayListDate);

                    OperationLogDetailListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            workOrderOperationLogDetailEntity = arrayListDate.get(position);
                            openSettingParameterValues(workOrderOperationLogDetailEntity);
                        }
                    });
                    operationnoteforworkorderRecyclerView.setAdapter(OperationLogDetailListAdapter);
                    operationnoteforworkorderRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    break;
                case STATE_REFRESH:
                    //下拉刷新状态，刷新数据，列表回到最顶端，关闭下拉刷新
                    OperationLogDetailListAdapter.notifyDataSetChanged();
                    operationnoteforworkorderRecyclerView.scrollToPosition(0);
                    operationnoteforworkorderSwipeRefreshLayout.finishRefresh();
                    break;
                case STATE_LOAD_MORE:
                    //上拉加载更多状态，追加数据，关闭上拉加载更多
                    OperationLogDetailListAdapter.notifyDataSetChanged();
                    operationnoteforworkorderSwipeRefreshLayout.finishRefreshLoadMore();
                    break;
            }
            noDataViewGone();
        }else{
            noDataViewShow();
        }

    }

    private void noDataViewShow() {
        tvNull.setVisibility(View.VISIBLE);
        operationnoteforworkorderSwipeRefreshLayout.setVisibility(View.GONE);
    }

    private void noDataViewGone() {
        tvNull.setVisibility(View.GONE);
        operationnoteforworkorderSwipeRefreshLayout.setVisibility(View.VISIBLE);
    }

    //设定参数值
    private void openSettingParameterValues(WorkOrderOperationLogDetailEntity workOrderOperationLogDetailEntity) {
        if(workOrderOperationLogDetailEntity != null){
            IntentUtils.goSettingParameterValuesActivity(this,workOrderOperationLogDetailEntity.operationLogDetailId);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
