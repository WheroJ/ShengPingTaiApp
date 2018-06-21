package com.cmcc.pp.ui.platformmonitoring;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.gateway.WANListData;
import com.cmcc.pp.entity.platformmonitoring.DialingDetailEntity;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.platformmonitoring.adapter.DialingDetailAdapter;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.view.ProgressDlg;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ABC on 2018/1/2.
 * 拨测结果
 */

public class ResultForServiceDialingActivity extends BActivity {

    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.tv_toltal)
    TextView tvToltal;
    @Bind(R.id.tv_passNum)
    TextView tvPassNum;
    @Bind(R.id.tv_failNum)
    TextView tvFailNum;
    @Bind(R.id.tv_dialingType)
    TextView tvDialingType;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    @Bind(R.id.tv_beginTime)
    TextView tvBeginTime;
    @Bind(R.id.recyclerView_steps)
    RecyclerView recyclerViewSteps;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.lin_servicedialingresult)
    LinearLayout linServicedialingresult;
    @Bind(R.id.tv_null)
    TextView tv_null;

    private String dialingType = "1";
    private String resultStr = "";
    private String bizNum = "";//拨测编号

    private int count = 0;
    private boolean shouldTips =false;
    private CompositeDisposable disposables = new CompositeDisposable();

    private JSONObject resultObject;
    private JSONObject queryByNumObject;
    private DialingDetailAdapter dialingDetailAdapter;
    private ArrayList<DialingDetailEntity> DialingDetailList;
    private DialingDetailEntity DialingDetailEntity;
    private String strTips = "拨测失败";//提示文字
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                    tv_null.setVisibility(View.VISIBLE);
                    linServicedialingresult.setVisibility(View.GONE);
                    break;
                case 2:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS, "拨测成功", true);
                    break;
                case 3:
                    refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING, "拨测中......", false);
                    break;
                case 4:
                    linServicedialingresult.setVisibility(View.VISIBLE);
                    tv_null.setVisibility(View.GONE);
                    showDialingDetail(queryByNumObject);
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
        return R.layout.avtivity_resultforservicedialing;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.servicedialing);
        Message message = Message.obtain();
        message.what = 3;
        handler.sendMessage(message);
        businessDialing();
    }

    @Override
    public void loadData() {
    }

    //发起拨测
    private void businessDialing() {
        UserHttpUtils.businessDialing(dialingType,
                new RxUtils.DefaultListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        if (!TextUtils.isEmpty("result")) {
                            try {
                                resultObject = new JSONObject(result);
                                resultStr = resultObject.getString("result");
                                startQueryByNum(resultStr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        strTips = JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage());
                        handler.sendEmptyMessageDelayed(1, 3000);
                    }
                });
    }

    //获取拨测结果
    private void startQueryByNum(String resultStr) {
        if (resultStr.equals("0")) {//拨测成功 调用queryByNum接口获取结果
            try {
                bizNum = resultObject.getString("bizNum");
                disposables.add(Observable.interval(0, 3, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                if (count < 10) {
                                    shouldTips = true;
                                    goToQueryByNum(bizNum);
                                    ++count;
                                }else{
                                    shouldTips = false;
                                    disposables.dispose();
                                }
                            }
                        }));
            } catch (JSONException e) {
                e.printStackTrace();
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        } else if (resultStr.equals("1")) {//拨测失败
            handler.sendEmptyMessageDelayed(1, 3000);
        }
    }


    //通过bizNum查询拨测结果 3秒执行一次 执行30秒
    private void goToQueryByNum(String bizNum) {

        UserHttpUtils.queryByNum(bizNum,
                new RxUtils.DefaultListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        if (!TextUtils.isEmpty("result")) {
                            Log.v("result", result);
                            try {
                                queryByNumObject = new JSONObject(result);
                                handler.sendEmptyMessageDelayed(2, 3000);
                                Message message = Message.obtain();
                                message.what = 4;
                                handler.sendMessage(message);
                                shouldTips = false;
                                disposables.dispose();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        strTips = JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage());
                        handler.sendEmptyMessageDelayed(1, 3000);
                    }
                });
    }

    //显示拨测结果
    private void showDialingDetail(JSONObject queryByNumObject) {
        if(queryByNumObject !=null){
            try {
                if(queryByNumObject.getString("result").equals("0")){
                    tvResult.setText("通过");
                    tvResult.setTextColor(getResources().getColor(R.color.main_blue));
                }else{
                    tvResult.setText("失败");
                    tvResult.setTextColor(getResources().getColor(R.color.main_red));
                }

                tvToltal.setText(queryByNumObject.getString("toltal"));
                tvPassNum.setText(queryByNumObject.getString("passNum"));
                tvFailNum.setText(queryByNumObject.getString("failNum"));

                if(queryByNumObject.getString("dialingType").equals("1")){
                    tvDialingType.setText("宽带新装业务");
                }else{
                    tvDialingType.setText("其他");
                }

                tvNumber.setText(queryByNumObject.getString("number"));
                tvBeginTime.setText(queryByNumObject.getString("beginTime"));

                DialingDetailList = JsonUtils.json2Obj(queryByNumObject.getString("dialingDetail"),
                        new TypeReference<ArrayList<DialingDetailEntity>>() {});

                recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                dialingDetailAdapter = new DialingDetailAdapter(DialingDetailList);
                dialingDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        DialingDetailEntity = DialingDetailList.get(position);
                        openStepDetails(DialingDetailEntity);
                    }

                });
                recyclerViewSteps.setAdapter(dialingDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //步骤详情
    private void openStepDetails(DialingDetailEntity dialingDetailEntity) {
        IntentUtils.goStepDetailsActivity(this,dialingDetailEntity);
    }


    @Override
    public void onBackPressed() {
        if(shouldTips){
            showGiveupDailog();
        }else{
            finish();
        }
    }

    //是否放弃拨测提示
    private void showGiveupDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dailog_giveupservicedialing, null);
        dialog.setView(view, 0, 0, 0, 0);
        TextView tvCancel = view.findViewById(R.id.dailog_tv_cancel);
        TextView tvSure = view.findViewById(R.id.dailog_tv_sure);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();// 隐藏dialog
                disposables.dispose();
                finish();
            }
        });
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
