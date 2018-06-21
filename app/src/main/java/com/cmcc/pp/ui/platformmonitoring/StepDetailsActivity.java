package com.cmcc.pp.ui.platformmonitoring;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.pp.R;
import com.cmcc.pp.base.BActivity;
import com.cmcc.pp.entity.platformmonitoring.DialingDetailEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ABC on 2018/1/2.
 * 步骤详情
 */

public class StepDetailsActivity extends BActivity {

    @Bind(R.id.stepdetails_status)
    TextView stepdetailsStatus;
    @Bind(R.id.pingdiag_lin_choiceWAN)
    LinearLayout pingdiagLinChoiceWAN;
    @Bind(R.id.result_tv_step)
    TextView resultTvStep;
    @Bind(R.id.result_tv_message)
    TextView resultTvMessage;
    @Bind(R.id.result_tv_log)
    TextView resultTvLog;
    @Bind(R.id.result_tv_endTime)
    TextView resultTvEndTime;
    private DialingDetailEntity dialingDetailEntity;

    @Override
    protected boolean getHasTitle() {
        return true;
    }

    @Override
    public int getContentLayoutId() {
        return R.layout.activity_stepdetails;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        dialingDetailEntity = (DialingDetailEntity) getIntent().getSerializableExtra("dialingDetailEntity");
        if (dialingDetailEntity != null) {
            getHeader().setTitle("第" + dialingDetailEntity.stage + "步");

            if(dialingDetailEntity.status.equals("0")){
                stepdetailsStatus.setText("成功");
                stepdetailsStatus.setTextColor(getResources().getColor(R.color.green_0ac));
            }else{
                stepdetailsStatus.setText("失败");
                stepdetailsStatus.setTextColor(getResources().getColor(R.color.main_red));
            }

            resultTvStep.setText("第" + dialingDetailEntity.stage + "步");
            resultTvMessage.setText(dialingDetailEntity.dialingMessage);
            resultTvLog.setText(dialingDetailEntity.log);
            resultTvEndTime.setText(dialingDetailEntity.endTime);
        }

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
