package com.cmcc.pp.ui.platformmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.NetworkElement;
import com.cmcc.pp.ui.platformmonitoring.adapter.NEIPListAdapter;
import com.cmcc.pp.util.Constant;
import com.cmcc.pp.util.IntentUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.SPUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/29.
 * 所选择网元的服务器列表
 */

public class ServerlistActivity extends BActivity {

    @Bind(R.id.serverlist_tv_server)
    TextView tvServer;

    @Bind(R.id.serverlist_recyclerView)
    RecyclerView recyclerView;

    private String checkNEName;
    private HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>> hashMap;
    private HashMap<String, HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> list;
    private NetworkElement checkNE;

    private int CHOOSE_NE = 100;
    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_serverlist;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            checkNE = (NetworkElement) bundle.getSerializable("checkNE");
//            data = (HashMap<String, HashMap<String, String>>) bundle.getSerializable("data");
            data = SPUtil.getObject(Constant.PLATFORM_MONITOR, new HashMap<String, HashMap<String, String>>());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void loadData() {
        if (checkNE != null) checkNEName = checkNE.name;
        getHeader().setTitle(checkNEName);
        tvServer.setText(checkNEName);

        HashMap<String, String> hashMap = data.get(checkNEName);
        if (hashMap != null)
            list = JsonUtils.json2Obj(hashMap.get("ip_status_list"), new TypeReference<ArrayList<HashMap<String, String>>>() {});

        if (list != null) {
            NEIPListAdapter adapter = new NEIPListAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    }

    @OnClick({
            R.id.serverlist_lin_server
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.serverlist_lin_server://选择网元
                IntentUtils.openChoiceNet(this, checkNE, CHOOSE_NE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_NE && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                checkNE = (NetworkElement) extras.getSerializable("nowNEItem");
                loadData();
            }
        }
    }
}
