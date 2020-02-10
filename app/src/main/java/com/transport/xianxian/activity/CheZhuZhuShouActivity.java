package com.transport.xianxian.activity;

import android.os.Bundle;
import android.view.View;

import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.utils.CommonUtil;

/**
 * Created by zyz on 2019-10-01.
 * 车主助手
 */
public class CheZhuZhuShouActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chezhuzhushou);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.linearLayout1:
               /* //服务中心
                CommonUtil.gotoActivity(this, Auth_ShenFenZhengActivity.class, false);*/
                break;
            case R.id.linearLayout2:
                /*//申诉中心
                CommonUtil.gotoActivity(this,Auth_JiaShiZhengActivity.class,false);*/
                break;
            case R.id.linearLayout3:
                //问题反馈
                CommonUtil.gotoActivity(this,FeedBackActivity.class,false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void updateView() {
        titleView.setTitle("车主助手");
    }
}
