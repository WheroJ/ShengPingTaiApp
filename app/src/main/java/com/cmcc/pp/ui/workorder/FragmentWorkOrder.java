package com.cmcc.pp.ui.workorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BFragment;
import com.cmcc.pp.ui.MainActivity;
import com.cmcc.pp.util.IntentUtils;
import com.zrspring.libv2.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/21.
 * 工单管理
 */

public class FragmentWorkOrder extends BFragment {

    @Bind(R.id.gateway_edt_input)
    EditText gatewayEdtInput;
    @Bind(R.id.setting_image_close)
    ImageView settingImageClose;
    @Bind(R.id.workorder_but_cheack)
    Button workorderButCheack;

    private String userAccount = "";


    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_workorder;
    }

    @Override
    public void initView(View fragmentView) {
        getHeader().setTitle(R.string.str_workordercheack);
        getHeader().showLeftImage(R.mipmap.iocn_menubar);

        gatewayEdtInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s)){
                    settingImageClose.setVisibility(View.VISIBLE);
                    workorderButCheack.setTextColor(getResources().getColor(R.color.white));
                    workorderButCheack.setBackgroundResource(R.drawable.bg_white_line_blue_4radius);
                }else{
                    settingImageClose.setVisibility(View.GONE);
                    workorderButCheack.setTextColor(getResources().getColor(R.color.blue_alpha54));
                    workorderButCheack.setBackgroundResource(R.drawable.bg_blue_line_blue_all_4radius);
                }

            }
        });
    }

    @Override
    public void loadData() {

    }

    @OnClick({
            R.id.workorder_but_cheack,
            R.id.setting_image_close
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.workorder_but_cheack:
                if(IsValid()){
                    openResults();
                }

                break;
            case R.id.setting_image_close:
                userAccount = "";
                gatewayEdtInput.setText("");
                workorderButCheack.setTextColor(getResources().getColor(R.color.blue_alpha54));
                workorderButCheack.setBackgroundResource(R.drawable.bg_blue_line_blue_all_4radius);
                settingImageClose.setVisibility(View.GONE);
                break;

        }
    }

    private boolean IsValid() {
        boolean result = true;
        userAccount = gatewayEdtInput.getText().toString().trim();
        Log.e("codeStr", userAccount);
        if (TextUtils.isEmpty(userAccount)) {
            ToastUtils.show("请输入工单号/宽带账号/loid");
            result = false;
        }else if(userAccount.equals("0") || userAccount.equals("1")){
            ToastUtils.show("请输入正确的工单号/宽带账号/loid");
            result = false;
        }
        return result;
    }

    //查询结果
    private void openResults() {
        IntentUtils.goQueryResultsForWorkOrder(getActivity(),userAccount);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
