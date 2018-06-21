package com.cmcc.pp.ui.transactiondata;

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
import com.cmcc.pp.entity.transactiondata.AreaItem;
import com.cmcc.pp.ui.transactiondata.adapter.AreaAdapter;
import com.cmcc.pp.ui.transactiondata.adapter.RecentChooseAreaAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ABC on 2018/1/2.
 * 选择网元
 */

public class ChooseAreaActivity extends BActivity {
    @Bind(R.id.activityChooseArea_tvNowArea)
    TextView mTvNowArea;
    @Bind(R.id.activityChooseArea_rvRecently)
    RecyclerView mRvRecently;
    @Bind(R.id.activityChooseArea_recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.activityChooseArea_tvNowProvince)
    TextView mTvNowProvince;

    private String whereSql = "(isRecent = 2)";
    private AreaItem nowAreaItem;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_choosearea;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.choicenetelement);
        getHeader().showLeftText("取消");
        getHeader().setLeftTextColor(R.color.main_blue);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nowAreaItem = (AreaItem) extras.getSerializable("nowAreaItem");
            if (nowAreaItem != null) {
                mTvNowArea.setText(getString(R.string.ne_now, nowAreaItem.areaName));
            }
        }

        mTvNowProvince.setText("安徽省");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvRecently.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
    }

    @Override
    public void loadData() {

        final List<AreaItem> areaItems = DataSupport.findAll(AreaItem.class);
        final List<AreaItem> recentAreaItems = DataSupport.where(new String[]{whereSql}).find(AreaItem.class);
        sortAreaItemByTime(recentAreaItems);

        final RecentChooseAreaAdapter recentAdapter = new RecentChooseAreaAdapter(recentAreaItems);
        mRvRecently.setAdapter(recentAdapter);

        AreaAdapter neAdapter = new AreaAdapter(areaItems);
        neAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AreaItem areaItem = areaItems.get(position);
                changeRecentChoose(areaItem);
            }
        });
        recentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AreaItem areaItem = recentAreaItems.get(position);
                changeRecentChoose(areaItem);
            }
        });
        mRecyclerView.setAdapter(neAdapter);
    }

    private void changeRecentChoose(AreaItem areaItem) {
        List<AreaItem> elements = DataSupport.where(new String[]{whereSql}).find(AreaItem.class);
        boolean isExist = elements.indexOf(areaItem) != -1;
        if (isExist) elements.remove(areaItem);
        sortAreaItemByTime(elements);

        int size = elements.size();
        if (!isExist && size >= 3) {
            AreaItem item = elements.get(0);
            item.isRecent = 1;
            item.recentTime = Long.MIN_VALUE;
            item.saveOrUpdate("(areaId='" + item.areaId + "')");
        }
        areaItem.isRecent = 2;
        areaItem.recentTime = System.currentTimeMillis();
        areaItem.saveOrUpdate("(areaId='" + areaItem.areaId + "')");

        nowAreaItem = areaItem;
        mTvNowArea.setText(getString(R.string.ne_now, areaItem.areaName));
        chooseItem();
    }

    private void sortAreaItemByTime(List<AreaItem> elements) {
        int size = elements.size();
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (elements.get(i).recentTime > elements.get(j).recentTime) {
                    AreaItem maxAreaItem = elements.get(i);
                    AreaItem minAreaItem = elements.get(j);
                    AreaItem newMinArea = new AreaItem();
                    newMinArea.recentTime = minAreaItem.recentTime;
                    newMinArea.isRecent = minAreaItem.isRecent;
                    newMinArea.areaId = minAreaItem.areaId;
                    newMinArea.areaCode = minAreaItem.areaCode;
                    newMinArea.areaName = minAreaItem.areaName;
                    AreaItem newMaxArea = new AreaItem();
                    newMaxArea.recentTime = maxAreaItem.recentTime;
                    newMaxArea.isRecent = maxAreaItem.isRecent;
                    newMaxArea.areaId = maxAreaItem.areaId;
                    newMaxArea.areaCode = maxAreaItem.areaCode;
                    newMaxArea.areaName = maxAreaItem.areaName;
                    elements.set(i , newMinArea);
                    elements.set(j , newMaxArea);
                }
            }
        }
    }

    private void chooseItem() {
        Intent intent = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable("nowAreaItem", nowAreaItem);
        intent.putExtras(extras);
        setResult(RESULT_OK, intent);
        finish();
    }
}
