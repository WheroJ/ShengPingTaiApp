package com.cmcc.pp.ui.platformmonitoring;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.request.PlatformMonitorHttpUtils;
import com.cmcc.pp.ui.widget.HddAndTimeTrend;
import com.cmcc.pp.ui.widget.TabLayout;
import com.zrspring.libv2.network.RxUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.Bind;

/**
 * Created by ABC on 2017/12/29.
 * 服务器详情
 */

public class ServerDetailsActivity extends BActivity{
    @Bind(R.id.activityServerDetail_trendView)
    HddAndTimeTrend trendView;
    private String ip;
    private ArrayList<HashMap<String, String>> capacityList;
    private ArrayList<HashMap<String, String>> terminalNumList;

    /**
     * 选择日，取值为day，返回7日内的数据；选择周，取值为week，返回7个周的数据，默认选择日
     */
    private String timeType = "day";

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_serverdetails;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        ip = getIntent().getStringExtra("ip");
        getHeader().setTitle(ip);

        trendView.getTrendTab().setOnCheckChangeListener(new TabLayout.OnTabCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NotNull RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.layoutTab_left:
                        timeType = "day";
                        loadData();
                        break;
                    case R.id.layoutTab_right:
                        timeType = "week";
                        loadData();
                        break;
                }
            }
        });
    }

    @Override
    public void loadData() {

        PlatformMonitorHttpUtils.getNETrend(timeType, ip, new RxUtils.DialogListener() {
            @Override
            public void onResult(@NotNull String result) {
                try {
                    JSONObject jsonObject  = new JSONObject(result);
                    Iterator<String> keys = jsonObject.keys();
                    capacityList = new ArrayList<>();
                    while (keys.hasNext()) {
                        HashMap<String, String> map = new HashMap<>();
                        String next = keys.next();
                        map.put("time", next);
                        map.put("capacity", jsonObject.optString(next));
                        capacityList.add(map);
                    }

                    loadTerminalTrend();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTerminalTrend() {
        PlatformMonitorHttpUtils.getTerminalTrend(timeType, new RxUtils.DialogListener() {
            @Override
            public void onResult(@NotNull String result) {
                try {
                    JSONObject jsonObject  = new JSONObject(result);
                    Iterator<String> keys = jsonObject.keys();
                    terminalNumList = new ArrayList<>();
                    while (keys.hasNext()) {
                        HashMap<String, String> map = new HashMap<>();
                        String next = keys.next();
                        map.put("time", next);
                        map.put("terminalNum", jsonObject.optString(next));
                        terminalNumList.add(map);
                    }

                    trendView.setData(ServerDetailsActivity.this.terminalNumList, capacityList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
