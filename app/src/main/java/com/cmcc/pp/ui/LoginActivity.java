package com.cmcc.pp.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.UserEntity;
import com.cmcc.pp.request.UserHttpUtils;
import com.cmcc.pp.ui.widget.RefreshLinearLayout;
import com.cmcc.pp.util.IntentUtils;
import com.cmcc.pp.util.JudgeErrorCodeUtils;
import com.cmcc.pp.util.TokenUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zrspring.libv2.network.RxUtils;
import com.zrspring.libv2.util.AESTool;
import com.zrspring.libv2.util.JsonUtils;
import com.zrspring.libv2.util.RegexUtils;
import com.zrspring.libv2.util.ToastUtils;
import com.zrspring.libv2.view.ProgressDlg;

import org.jetbrains.annotations.NotNull;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by ABC on 2017/12/11.
 * 登陆界面
 */

public class LoginActivity extends BActivity {
    @Bind(R.id.ed_phone)
    EditText edPhone;
    @Bind(R.id.ed_img_code)
    EditText edImgCode;
    @Bind(R.id.bt_login)
    Button btLogin;
    @Bind(R.id.login_image_close)
    ImageView loginImageClose;
    @Bind(R.id.ed_password)
    EditText edPassword;
    @Bind(R.id.content)
    RefreshLinearLayout refreshLinearLayout;
    @Bind(R.id.draweeview_verifycode)
    SimpleDraweeView draweeviewVerifycode;


    private String UserName = "";//登陆账号
    private String PassWord = "";//登陆密码
    private String VerifyCode = "";//验证码

    private String strTips = "";//提示文字
    private String imageUri = "";//验证码图片URL

    private UserEntity userEntity;

    private Handler handler;
    private boolean flag;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getHeader().setTitle(R.string.login);
        getHeader().setLeftAndImageGone();

        flag = false;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        refreshLinearLayout.updateStatus(RefreshLinearLayout.FAIL, strTips, true);
                        break;
                    case 2:
                        refreshLinearLayout.updateStatus(RefreshLinearLayout.SUCCESS);
                        break;
                    case 3:
                        if (flag) {
                            refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING);
                            handler.sendEmptyMessageDelayed(2, 5000);
                        } else {
                            refreshLinearLayout.updateStatus(RefreshLinearLayout.LOADING);
                            handler.sendEmptyMessageDelayed(1, 5000);
                        }
                        break;
                }
            }
        };

        refreshLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshLinearLayout.packUpHeader();
            }
        });

        inintViews();
    }

    @Override
    public void loadData() {

    }

    private void inintViews() {

        edPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点

                } else {//失去焦点
                    UserName = edPhone.getText().toString().trim();
                        if (!TextUtils.isEmpty(UserName) && RegexUtils.isMobile(UserName)) {
                            v.setEnabled(true);
                            getImageVerify(UserName);

                        } else {
                            ToastUtils.show("请输入正确的手机号码");
                            v.setEnabled(true);
                        }
                }
            }
        });


        edPhone.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edPassword.setEnabled(true);
                loginImageClose.setVisibility(View.VISIBLE);
            }
        });

        edImgCode.addTextChangedListener(new TextWatcher() {

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
                    btLogin.setTextColor(getResources().getColor(R.color.white));
                    btLogin.setBackgroundResource(R.drawable.bg_white_line_blue_4radius);
                }else{
                    btLogin.setTextColor(getResources().getColor(R.color.blue_alpha54));
                    btLogin.setBackgroundResource(R.drawable.bg_blue_line_blue_all_4radius);
                }
            }
        });


    }



    @OnClick({
            R.id.bt_login,
            R.id.login_image_close,
            R.id.draweeview_verifycode
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (IsValid()) {
                    //在输入了账号密码后执行验证码验证接口
                    verifyImageCode();
                }

                break;

            case R.id.login_image_close:
                UserName = "";
                edPhone.setText(UserName);
                loginImageClose.setVisibility(View.GONE);
                break;
            case R.id.draweeview_verifycode://点击，重新获取验证码
                UserName = edPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(UserName) && RegexUtils.isMobile(UserName)) {
                    getImageVerify(UserName);
                } else {
                    ToastUtils.show("请输入正确的手机号码");
                }

                break;
        }
    }

    //校验验证码
    private void verifyImageCode() {
        UserHttpUtils.verifyImageCode(VerifyCode, UserName,
                new RxUtils.DefaultListener() {
                    @Override
                    public void onResult(@NotNull String result) {
                        login();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        showUpHeader(JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage()));
                    }
                });
    }


    //获取图片验证码
    private void getImageVerify(String userName) {

        UserHttpUtils.getImageVerify(userName,
                new RxUtils.DialogListener(this) {
                    @Override
                    public void onResult(@NotNull String result) {
                        imageUri = JsonUtils.getJsonStringObjInKeyValue(result, "verifyUrl");
                        Log.e("VerifyUrl", imageUri);
                        draweeviewVerifycode.setVisibility(View.VISIBLE);
                        draweeviewVerifycode.setImageURI(imageUri);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        showUpHeader(JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage()));
                        draweeviewVerifycode.setVisibility(View.VISIBLE);
                    }
                });
    }

    //登陆
    private void login() {
        ProgressDlg progressDlg = new ProgressDlg(LoginActivity.this, "登陆中......");

        UserHttpUtils.login(UserName, AESTool.combineEncrypt(PassWord), VerifyCode,

                new RxUtils.DefaultListener(progressDlg) {
                    @Override
                    public void onResult(@NotNull String result) {
                        TokenUtils.INSTANCE.setAllUserLogout();

                        userEntity = JsonUtils.json2CommonObj(result,UserEntity.class);
                        userEntity.tokenSaveTime = System.currentTimeMillis();
                        userEntity.currentLoginIn = 2;
                        userEntity.saveOrUpdate("telePhone='" + userEntity.telePhone +"'");

                        IntentUtils.startReLoginService();
                        IntentUtils.goMain(LoginActivity.this);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        super.onError(e);
                        showUpHeader(JudgeErrorCodeUtils.getJudgeErrorTips(e.getMessage()));
                    }
                });

    }


    @Override
    protected void onFinishClick() {
        super.onFinishClick();
    }

    //判断手机号、密码是否输入
    private boolean IsValid() {
        boolean result = true;

        UserName = edPhone.getText().toString().trim();
        PassWord = edPassword.getText().toString().trim();
        VerifyCode = edImgCode.getText().toString().trim();
        Log.e("codeStr", VerifyCode);

        if (TextUtils.isEmpty(UserName)) {
            ToastUtils.show("请输入手机号码");
            result = false;
        } else if (!RegexUtils.isMobile(UserName)) {
            ToastUtils.show("请输入正确的手机号码");
            result = false;
        } else if (TextUtils.isEmpty(PassWord)) {
            ToastUtils.show("请输入登陆密码");
            result = false;
        } else if (TextUtils.isEmpty(VerifyCode)) {
            strTips = "请输入图形验证码";
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);
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
}
