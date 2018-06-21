package com.cmcc.pp.ui.platformmonitoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.NetworkElement;
import com.cmcc.pp.ui.platformmonitoring.adapter.NEAdapter;
import com.cmcc.pp.ui.platformmonitoring.adapter.RecentNEAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ABC on 2018/1/2.
 * 选择网元
 */

public class ChoiceNetElementActivity extends BActivity {
    @Bind(R.id.activityChooseNE_tvNowNE)
    TextView mTvNowNe;
    @Bind(R.id.activityChooseNE_rvRecently)
    RecyclerView mRvRecently;
    @Bind(R.id.activityChooseNE_recyclerView)
    RecyclerView mRecyclerView;

    private String whereSql = "(isRecent = 2)";
    private List<NetworkElement> networkElements;
    private NetworkElement nowNEItem;
    private final int recentNum = 3;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_choicenetelement;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.choicenetelement);
        getHeader().showLeftText("取消");
        getHeader().setLeftTextColor(R.color.main_blue);

        nowNEItem = (NetworkElement) getIntent().getSerializableExtra("nowNEItem");
        if (nowNEItem != null) mTvNowNe.setText(getString(R.string.ne_now, nowNEItem.name));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvRecently.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
    }

    @Override
    public void loadData() {
        final List<NetworkElement> recentNEs = DataSupport.where(new String[]{whereSql}).find(NetworkElement.class);
        keepRecentNum(recentNEs, recentNum);

        RecentNEAdapter recentNEAdapter = new RecentNEAdapter(recentNEs);
        recentNEAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NetworkElement networkElement = recentNEs.get(position);
                changeRecentChoose(networkElement);
            }
        });
        mRvRecently.setAdapter(recentNEAdapter);

        networkElements = DataSupport.findAll(NetworkElement.class);
        NEAdapter neAdapter = new NEAdapter(networkElements);
        neAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NetworkElement networkElement = networkElements.get(position);
                changeRecentChoose(networkElement);
            }
        });
        mRecyclerView.setAdapter(neAdapter);
    }

    private void keepRecentNum(List<NetworkElement> recentNEs, int num) {
        sortNE(recentNEs);
        if (recentNEs.size() > num) {
            for (int i = 0; i < recentNEs.size() - num; i++) {
                NetworkElement item = recentNEs.get(i);
                item.isRecent = 1;
                item.recentTime = Long.MIN_VALUE;
                item.saveOrUpdate("(name='" + item.name + "')");
            }
        }
    }

    private void changeRecentChoose(NetworkElement neItem) {
        List<NetworkElement> elements = DataSupport.where(new String[]{whereSql}).find(NetworkElement.class);
        boolean isExist = elements.indexOf(neItem) != -1;
        if (isExist) elements.remove(neItem);

        keepRecentNum(elements, recentNum - 1);

        neItem.isRecent = 2;
        neItem.recentTime = System.currentTimeMillis();
        neItem.saveOrUpdate("(name='" + neItem.name + "')");

        nowNEItem = neItem;
        chooseItem();
    }

    private void chooseItem() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable("nowNEItem", nowNEItem);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void sortNE(List<NetworkElement> elements) {
        int size = elements.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (elements.get(i).recentTime > elements.get(j).recentTime) {
                    NetworkElement maxAreaItem = elements.get(i);
                    NetworkElement minAreaItem = elements.get(j);
                    NetworkElement newMinArea = new NetworkElement();
                    newMinArea.recentTime = minAreaItem.recentTime;
                    newMinArea.isRecent = minAreaItem.isRecent;
                    newMinArea.name = minAreaItem.name;
                    NetworkElement newMaxArea = new NetworkElement();
                    newMaxArea.recentTime = maxAreaItem.recentTime;
                    newMaxArea.isRecent = maxAreaItem.isRecent;
                    newMaxArea.name = maxAreaItem.name;
                    elements.set(i , newMinArea);
                    elements.set(j , newMaxArea);
                }
            }
        }
    }
}
