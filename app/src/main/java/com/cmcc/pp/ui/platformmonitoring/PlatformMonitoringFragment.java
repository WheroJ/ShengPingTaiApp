package com.cmcc.pp.ui.platformmonitoring;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BFragment;
import com.cmcc.pp.entity.NetworkElement;
import com.cmcc.pp.entity.transactiondata.RefreshViewObservable;
import com.cmcc.pp.entity.transactiondata.RefreshViewObserver;
import com.cmcc.pp.request.PlatformMonitorHttpUtils;
import com.cmcc.pp.ui.MainActivity;
import com.cmcc.pp.ui.platformmonitoring.adapter.PlatformMonitorAdapter;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.Constant;
import com.cmcc.pp.util.IntentUtils;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.SPUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;


/**
 * Created by ABC on 2017/12/21.
 * 平台监控
 */

public class PlatformMonitoringFragment extends BFragment {

    @Bind(R.id.platformmonitoring_tv_nomalynetworkelement)
    TextView tvNormalElement;

    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.platformmonitoring_butt_left)
    RadioButton platformmonitoringButtLeft;
    @Bind(R.id.platformmonitoring_butt_right)
    RadioButton platformmonitoringButtRight;
    @Bind(R.id.platformmonitoring_radiogroup)
    RadioGroup platformmonitoringRadiogroup;

    @Bind(R.id.platformmonitoring_recyclerView)
    RecyclerView recyclerView;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_platformmonitoring;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void initView(View fragmentView) {
        getHeader().setTitle(R.string.str_platformmoitoring);
        getHeader().showLeftImage(R.mipmap.iocn_menubar);

        platformmonitoringRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                switch (checkedId) {
                    case R.id.platformmonitoring_butt_left://刷新
                        //点击执行逻辑
                        break;

                    case R.id.platformmonitoring_butt_right://业务拨测
                        IntentUtils.goResultForServiceDialing(getActivity());

                    default:
                        break;
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void loadData() {
        final RefreshViewObservable refreshViewObservable = new RefreshViewObservable();
        refreshViewObservable.addOberver(new RefreshViewObserver(refreshLinearLayout));
        refreshViewObservable.changeStatus(RefreshLinearLayout.LOADING);
        PlatformMonitorHttpUtils.getOverallView(new RxUtils.DefaultListener(){
            @Override
            public void onResult(@NotNull String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String abNormalNeNum = jsonObject.optString("abNormalNeNum");
                    tvNormalElement.setText(abNormalNeNum + "个");
                    JSONObject serviceDetail = jsonObject.optJSONObject("serviceDetail");
                    HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>> hashMap = parseData(serviceDetail);

                    recyclerView.setAdapter(new PlatformMonitorAdapter(hashMap, getContext()));
                    refreshViewObservable.changeStatus(RefreshLinearLayout.SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                    refreshViewObservable.changeStatus(RefreshLinearLayout.FAIL);
                }
            }

            @NonNull
            private HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>> parseData(JSONObject serviceDetail) {
                Iterator<String> keys = serviceDetail.keys();
                HashMap<String, ArrayList<HashMap<String, HashMap<String, String>>>> hashMap = new HashMap<>();

                //保存所有的网元-详情  的数据
                HashMap<String, HashMap<String, String>> neDetailMap = new HashMap<>();
                if (keys != null) {
                    while (keys.hasNext()) {
                        String key = keys.next();

                        ArrayList<HashMap<String, HashMap<String, String>>> list = new ArrayList<>();
                        JSONArray jsonArray = serviceDetail.optJSONArray(key);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.optJSONObject(i);
                                Iterator<String> iterator = object.keys();

                                HashMap<String, HashMap<String, String>> map = new HashMap<>();
                                while (iterator.hasNext()) {
                                    String next = iterator.next();

                                    //保存所有网元的名称
                                    NetworkElement element = new NetworkElement();
                                    element.name = next;
                                    List<NetworkElement> elementList = DataSupport.where("(name='" + next + "')").find(NetworkElement.class);
                                    if (elementList.isEmpty()) {
                                        element.saveOrUpdate("(name='" + next + "')");
                                    }
                                    //保存所有网元的名称

                                    JSONObject object1 = object.optJSONObject(next);

                                    HashMap<String, String> map1 = new HashMap<>();
                                    map1.put("normalRatio", object1.optString("normalRatio"));
                                    map1.put("ip_status_list", object1.optString("ip_status_list"));
                                    map.put(next, map1);
                                    neDetailMap.put(next, map1);
                                }
                                list.add(map);
                            }
                        }
                        hashMap.put(key, list);
                        SPUtil.saveObject(Constant.PLATFORM_MONITOR, neDetailMap);
                    }
                }
                return hashMap;
            }

            @Override
            public void onError(@NotNull Throwable e) {
                super.onError(e);
                refreshViewObservable.changeStatus(RefreshLinearLayout.FAIL);
            }
        });
    }

    @Override
    protected void onLeftClick() {
        openmeunbar();
    }

    //打开菜单
    private void openmeunbar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.closemenu();
    }
}
