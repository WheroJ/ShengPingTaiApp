package com.cmcc.pp.ui.gatewayadministration;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.cmcc.pp.entity.gateway.GatewayInfo;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.MainActivity;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.ToastUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/21.
 * 网关管理
 */

public class FragmentGatewayAdministration extends BFragment {
    @Bind(R.id.setting_image_close)
    ImageView settingImageClose;
    @Bind(R.id.gateway_but_cheack)
    Button gatewayButCheack;
    @Bind(R.id.gateway_edt_userAccount)
    EditText gatewayEdtUserAccount;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;

    private String userAccount ="";//SN/MAC/宽带账号/LOID

    private GatewayInfo gatewayInfo;
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
        return R.layout.activity_gatewayadministration;
    }

    @Override
    public void initView(View fragmentView) {
        getHeader().setTitle(R.string.str_gatewaycheack);
        getHeader().showLeftImage(R.mipmap.iocn_menubar);

        gatewayEdtUserAccount.addTextChangedListener(new TextWatcher() {

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
                    gatewayButCheack.setTextColor(getResources().getColor(R.color.white));
                    gatewayButCheack.setBackgroundResource(R.drawable.bg_white_line_blue_4radius);
                }else{
                    settingImageClose.setVisibility(View.GONE);
                    gatewayButCheack.setTextColor(getResources().getColor(R.color.blue_alpha54));
                    gatewayButCheack.setBackgroundResource(R.drawable.bg_blue_line_blue_all_4radius);
                }
            }
        });

    }

    @Override
    public void loadData() {

    }

    @OnClick({
            R.id.gateway_but_cheack,
            R.id.setting_image_close
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gateway_but_cheack:
                if(IsValid()){
                    getGatewayInfoData();
                }
                break;

            case R.id.setting_image_close:
                gatewayEdtUserAccount.setText("");
                userAccount ="";
                gatewayButCheack.setTextColor(getResources().getColor(R.color.blue_alpha54));
                gatewayButCheack.setBackgroundResource(R.drawable.bg_blue_line_blue_all_4radius);
                settingImageClose.setVisibility(View.GONE);
                break;

        }
    }

    //获取网关信息数据
    public void getGatewayInfoData() {
        UserHttpUtils.getGatewayInfoData(userAccount,
                new RxUtils.DialogListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        if(result!=null){
                            String gatewayInfoStr = JsonUtils.getJsonStringObjInKeyValue(result, "getGatewayInfoData");
                            gatewayInfo = JsonUtils.json2CommonObj(gatewayInfoStr,GatewayInfo.class );
                            IntentUtils.goQueryResults(getActivity(),gatewayInfo,userAccount);
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        showUpHeader(JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage()));
                    }
                });


    }

    private boolean IsValid() {
        boolean result = true;
        userAccount = gatewayEdtUserAccount.getText().toString().trim();
        /*
         *CIOT,HW,ZTE,FH,CMDC开头的12位的字符串或者16位长度的字符串是SN,其他12位长度的字符串是MAC，20位数字是loid,其他的是宽带账号
         */
        if(userAccount.indexOf(":") != -1){
            userAccount = userAccount.replace(":", "");
        }
        if((12==userAccount.length() || 16==userAccount.length()) &&
                userAccount.matches("^(CIOT|HW|ZTE|FH|CMDC){1}.*$")){  //SN
            userAccount = userAccount.toUpperCase();
        }else if(12==userAccount.length()){  //MAC
            userAccount = userAccount.toUpperCase();
        }
        Log.e("codeStr", userAccount);
        if (TextUtils.isEmpty(userAccount)) {
            ToastUtils.show("请输入SN/MAC/宽带账号/LOID");
            result = false;
        }
        return result;
    }

    //显示顶部提示
    private void showUpHeader(String strMessage) {
        strTips = strMessage;
        Message message = Message.obtain();
        message.what = 1;
        handler.sendMessage(message);
    }


    //打开菜单
    private void openmeunbar() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.closemenu();

    }

    @Override
    protected void onLeftClick() {
        openmeunbar();
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
