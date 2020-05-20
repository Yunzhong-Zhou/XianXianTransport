package com.transport.xianxian.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyLogger;
import com.transport.xianxian.utils.ZxingUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zyz on 2019-10-20.
 * 转单
 */
public class ZhuanDanActivity extends BaseActivity {
    int status = 1;
    String id = "", lat = "", lng = "", scale = "";
    ImageView imageView1;
    TextView tv_quxiao;

    String zhuanDanId = "";
    TimeCount time1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuandan);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (time1 != null) {
            time1.cancel();
        }
    }

    @Override
    protected void initView() {
        imageView1 = findViewByID_My(R.id.imageView1);
        tv_quxiao = findViewByID_My(R.id.tv_quxiao);

        tv_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status ==1) {//等待接单
                    showToast("确认取消转单该订单吗？", "确定", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Map<String, String> params = new HashMap<>();
                            params.put("token", localUserInfo.getToken());
                            params.put("t_indent_confirm_id", zhuanDanId);
                            params.put("type", "transfter_cancel");//转单确认
                            RequestQuXiao(params);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");
        scale = getIntent().getStringExtra("scale");
        showProgress(true, getString(R.string.app_loading));

        Map<String, String> params = new HashMap<>();
        params.put("token", localUserInfo.getToken());
        params.put("t_indent_id", id);
        params.put("type", "5");//转单确认
        params.put("lat", lat + "");
        params.put("lng", lng + "");
        params.put("scale", scale);//动态比例
        RequestZhuanDan(params);

    }

    private void RequestZhuanDan(Map<String, String> params) {
        OkHttpClientManager.postAsyn(ZhuanDanActivity.this, URLs.OrderDetails_ZhuangHuo, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                if (!info.equals("")) {
                    showToast(info, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>司机-转单确认" + response);
//                myToast("确认成功");
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    JSONObject data = jObj.getJSONObject("data");
                    zhuanDanId = data.getString("id");
                    //生成二维码
                    Bitmap mBitmap = ZxingUtils.createQRCodeBitmap(data.getString("id"), 480, 480);
                    imageView1.setImageBitmap(mBitmap);
                    status =  data.getInt("status");
                    if (status ==1){//等待接单
                        tv_quxiao.setText("取消");
                        startTime();//开始倒计时
                    }else {
                        tv_quxiao.setText("已转派,点击按钮返回");
                        //关闭计时器
                        if (time1 != null) {
                            time1.cancel();
                        }
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, false);
    }
    private void RequestQuXiao(Map<String, String> params) {
        OkHttpClientManager.postAsyn(ZhuanDanActivity.this, URLs.OrderDetails_ZhuangHuo, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>取消转单" + response);
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    myToast(jObj.getString("msg"));
                    finish();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, false);
    }
    @Override
    protected void updateView() {
        titleView.setTitle("转单二维码");
    }

    private void startTime() {
        MyLogger.i(">>>>>>" + (10 * 1000));
        if (time1 != null) {
            time1.cancel();
        }
        time1 = new TimeCount(10 * 1000, 1000);//构造CountDownTimer对象
        time1.start();//开始计时
    }

    class TimeCount extends CountDownTimer {
        //        TextView textView;
        public TimeCount(long millisInFuture, long countDownInterval
//                , TextView textView
        ) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
//            this.textView = textView;
        }

        @Override
        public void onFinish() {//计时完毕时触发
//            textView.setText("0s");
//            MyLogger.i(">>>>>>>>计时完毕，刷新订单");

            Map<String, String> params = new HashMap<>();
            params.put("token", localUserInfo.getToken());
            params.put("t_indent_id", id);
            params.put("type", "5");//转单确认
            params.put("lat", lat + "");
            params.put("lng", lng + "");
            params.put("scale", scale);//动态比例
            RequestZhuanDan(params);

        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

//            textView.setText(CommonUtil.timedate3(millisUntilFinished) + "s");//秒计时
//            textView.setText(CommonUtil.timedate4(millisUntilFinished, getActivity()));//时分秒倒计时
        }

    }
}
