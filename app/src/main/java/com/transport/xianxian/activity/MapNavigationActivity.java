package com.transport.xianxian.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviForbidType;
import com.amap.api.navi.enums.NaviLimitType;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapCarInfo;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviForbiddenInfo;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLimitInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.track.AMapTrackClient;
import com.amap.api.track.ErrorCode;
import com.amap.api.track.OnTrackLifecycleListener;
import com.amap.api.track.TrackParam;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bumptech.glide.Glide;
import com.cy.cyflowlayoutlibrary.FlowLayout;
import com.cy.cyflowlayoutlibrary.FlowLayoutAdapter;
import com.hyphenate.easeui.EaseConstant;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.lieying.Constants;
import com.transport.xianxian.lieying.SimpleOnTrackLifecycleListener;
import com.transport.xianxian.model.ErrorInfo;
import com.transport.xianxian.model.OrderDetailsModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;

/**
 * Created by zyz on 2019-10-22.
 */
public class MapNavigationActivity extends BaseActivity implements AMapNaviListener, AMapNaviViewListener {
    OrderDetailsModel model;

    AMapNaviView mAMapNaviView;
    AMapNavi mAMapNavi;
    AMapCarInfo aMapCarInfo;//车辆信息
    List<NaviLatLng> sList = new ArrayList<NaviLatLng>();//开始点
    List<NaviLatLng> eList = new ArrayList<NaviLatLng>();//结束点
    List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();//途经点

    ScrollView scrollView;
    LinearLayout linearLayout1, ll_hint1, ll_hint2;
    ImageView imageView1, imageView1_2, iv_xinxi, iv_xinxi_2, iv_dianhua, iv_dianhua_2;
    TextView textView1, textView1_2, textView2, textView2_2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11,
            tv_shouqi, tv_left, tv_right, tv_fujiafei;

    double lat = 0, lng = 0;
    int juli = 0;

    //轨迹
    private static final String TAG = "TrackServiceActivity";
    private static final String CHANNEL_ID_SERVICE_RUNNING = "CHANNEL_ID_SERVICE_RUNNING";
    private AMapTrackClient aMapTrackClient;

    private OnTrackLifecycleListener onTrackListener = new SimpleOnTrackLifecycleListener() {
        @Override
        public void onBindServiceCallback(int status, String msg) {
            Log.w(TAG, "onBindServiceCallback, status: " + status + ", msg: " + msg);
        }

        @Override
        public void onStartTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_TRACK_SUCEE || status == ErrorCode.TrackListen.START_TRACK_SUCEE_NO_NETWORK) {
                // 成功启动
//                Toast.makeText(OrderDetailsActivity.this, "启动服务成功", Toast.LENGTH_SHORT).show();
                MyLogger.i(">>>>>>轨迹上报启动服务成功");

            } else if (status == ErrorCode.TrackListen.START_TRACK_ALREADY_STARTED) {
                // 已经启动
//                Toast.makeText(OrderDetailsActivity.this, "服务已经启动", Toast.LENGTH_SHORT).show();
                MyLogger.i(">>>>>>轨迹上报启动服务已经启动");
            } else {
                Log.w(TAG, "error onStartTrackCallback, status: " + status + ", msg: " + msg);
               /* Toast.makeText(OrderDetailsActivity.this,
                        "error onStartTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();*/
            }
        }

        @Override
        public void onStopTrackCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_TRACK_SUCCE) {
                // 成功停止
//                Toast.makeText(TrackServiceActivity.this, "停止服务成功", Toast.LENGTH_SHORT).show();
                MyLogger.i(">>>>>>轨迹上报停止服务成功");
            } else {
                Log.w(TAG, "error onStopTrackCallback, status: " + status + ", msg: " + msg);
                /*Toast.makeText(TrackServiceActivity.this,
                        "error onStopTrackCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();*/

            }
        }

        @Override
        public void onStartGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.START_GATHER_SUCEE) {
//                Toast.makeText(TrackServiceActivity.this, "定位采集开启成功", Toast.LENGTH_SHORT).show();
                MyLogger.i(">>>>>>轨迹上报-定位采集开启成功");
            } else if (status == ErrorCode.TrackListen.START_GATHER_ALREADY_STARTED) {
//                Toast.makeText(TrackServiceActivity.this, "定位采集已经开启", Toast.LENGTH_SHORT).show();
                MyLogger.i(">>>>>>轨迹上报-定位采集已经开启");
            } else {
                Log.w(TAG, "error onStartGatherCallback, status: " + status + ", msg: " + msg);
                /*Toast.makeText(TrackServiceActivity.this,
                        "error onStartGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();*/
            }
        }

        @Override
        public void onStopGatherCallback(int status, String msg) {
            if (status == ErrorCode.TrackListen.STOP_GATHER_SUCCE) {
//                Toast.makeText(TrackServiceActivity.this, "定位采集停止成功", Toast.LENGTH_SHORT).show();
                MyLogger.i(">>>>>>轨迹上报-定位采集停止成功");

            } else {
                Log.w(TAG, "error onStopGatherCallback, status: " + status + ", msg: " + msg);
                /*Toast.makeText(TrackServiceActivity.this,
                        "error onStopGatherCallback, status: " + status + ", msg: " + msg,
                        Toast.LENGTH_LONG).show();*/
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapnavigation);

        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(MapNavigationActivity.this);

        aMapCarInfo = getIntent().getParcelableExtra("info");

        //初始化导航
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(MapNavigationActivity.this);
        mAMapNavi.setUseInnerVoice(true);

        //轨迹
        // 不要使用Activity作为Context传入
        aMapTrackClient = new AMapTrackClient(getApplicationContext());
        aMapTrackClient.setInterval(5, 30);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
    }

    @Override
    protected void initView() {
        linearLayout1 = findViewByID_My(R.id.linearLayout1);
        ll_hint1 = findViewByID_My(R.id.ll_hint1);
        ll_hint2 = findViewByID_My(R.id.ll_hint2);

        imageView1 = findViewByID_My(R.id.imageView1);
        imageView1_2 = findViewByID_My(R.id.imageView1_2);
        iv_xinxi = findViewByID_My(R.id.iv_xinxi);
        iv_xinxi_2 = findViewByID_My(R.id.iv_xinxi_2);
        iv_dianhua = findViewByID_My(R.id.iv_dianhua);
        iv_dianhua_2 = findViewByID_My(R.id.iv_dianhua_2);

        textView1 = findViewByID_My(R.id.textView1);
        textView1_2 = findViewByID_My(R.id.textView1_2);
        textView2 = findViewByID_My(R.id.textView2);
        textView2_2 = findViewByID_My(R.id.textView2_2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);
        textView6 = findViewByID_My(R.id.textView6);
        textView7 = findViewByID_My(R.id.textView7);
        textView8 = findViewByID_My(R.id.textView8);
        textView9 = findViewByID_My(R.id.textView9);
        textView10 = findViewByID_My(R.id.textView10);
        textView11 = findViewByID_My(R.id.textView11);

        tv_shouqi = findViewByID_My(R.id.tv_shouqi);
        tv_left = findViewByID_My(R.id.tv_left);
        tv_right = findViewByID_My(R.id.tv_right);
        tv_fujiafei = findViewByID_My(R.id.tv_fujiafei);

        scrollView = findViewByID_My(R.id.scrollView);
        //收起布局
        scrollView.setVisibility(View.GONE);
        linearLayout1.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initData() {
        showProgress(true, "定位中，正在搜索路线...");
        model = (OrderDetailsModel) getIntent().getSerializableExtra("OrderDetailsModel");
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        //开始点
        sList.clear();
        NaviLatLng mStartLatlng = new NaviLatLng(lat, lng);
        sList.add(mStartLatlng);
        //地址信息

        eList.clear();//结束点
        mWayPointList.clear();//途经点
        /*for (int i = 0; i < model.getTindent().getAddr_list().size(); i++) {
            if (i == (model.getTindent().getAddr_list().size() - 1)) {
                //结束点
                NaviLatLng mEndLatlng = new NaviLatLng(Double.valueOf(model.getTindent().getAddr_list().get(i).getLat())
                        , Double.valueOf(model.getTindent().getAddr_list().get(i).getLng()));
                eList.add(mEndLatlng);
            } else {
                //途经点
                NaviLatLng mWayLatlng = new NaviLatLng(Double.valueOf(model.getTindent().getAddr_list().get(i).getLat())
                        , Double.valueOf(model.getTindent().getAddr_list().get(i).getLng()));
                mWayPointList.add(mWayLatlng);
            }
        }*/
        NaviLatLng mEndLatlng = new NaviLatLng(Double.valueOf(model.getTindent().getNext_addr().getLat())
                , Double.valueOf(model.getTindent().getNext_addr().getLng()));
        eList.add(mEndLatlng);

        if (!model.getTindent().getSend_head().equals("")) {
            Glide.with(MapNavigationActivity.this)
                    .load(IMGHOST + model.getTindent().getSend_head())
                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                    .into(imageView1);//加载图片
            Glide.with(MapNavigationActivity.this)
                    .load(IMGHOST + model.getTindent().getSend_head())
                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                    .into(imageView1_2);//加载图片
        }
        textView1.setText(model.getTindent().getSend_name());//昵称
        textView2.setText(model.getTindent().getIndustry());//行业
        textView1_2.setText(model.getTindent().getSend_name());//昵称
        textView2_2.setText(model.getTindent().getIndustry());//行业

        textView3.setText("货源单号" + model.getTindent().getSn());//货源单号
        textView4.setText(model.getTindent().getCreated_at() + " 发布");// 发布
        textView5.setText(model.getTindent().getNow_state_action());// 卸货
        textView6.setText(model.getTindent().getNow_state() + " " + model.getTindent().getNow_state_action());//time 卸货
        textView7.setText(model.getTindent().getRemark());//备注
        textView8.setText("¥ " + model.getTindent().getPrice());//订单金额
        textView9.setText(model.getTindent().getPrice_detail().getStart() + "元");//起步价
        textView10.setText(model.getTindent().getPrice_detail().getMilleage() + "元");//离装货时间还有0小时
//                textView11.setText(response.getTindent().getSend_time());//描述

        //标签
        FlowLayoutAdapter<String> flowLayoutAdapter;
        flowLayoutAdapter = new FlowLayoutAdapter<String>(model.getTindent().getTag()) {
            @Override
            public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
//                                holder.setText(R.id.tv,bean);
                TextView tv = holder.getView(R.id.tv);
                tv.setText(bean);
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(R.color.white));
                    tv.setBackgroundResource(R.drawable.yuanjiao_3_lanse);
                } else {
                    tv.setTextColor(getResources().getColor(R.color.black1));
                    tv.setBackgroundResource(R.drawable.yuanjiao_3_huise);
                }
            }

            @Override
            public void onItemClick(int position, String bean) {
//                        showToast("点击" + position);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.item_flowlayout;
            }
        };
        ((FlowLayout) findViewByID_My(R.id.flowLayout)).setAdapter(flowLayoutAdapter);

//        requestServer();

        switch (model.getTindent().getStatus()) {
            /*case 0://未接单-到不了导航页面
                if (model.getTindent().getIs_appoint() == 1) {//平台指派，可以拒绝
                    tv_left.setText("拒绝此单");//左边按钮
                    tv_left.setBackgroundResource(R.drawable.btn_huise);

                } else {
                    tv_left.setText("返回详情");//左边按钮
                    tv_left.setBackgroundResource(R.drawable.btn_juse);
                }
                tv_right.setText("确认接单");//右边按钮
                break;*/
            case 1://已接单
                tv_left.setText("取消订单");//左边按钮
                tv_left.setBackgroundResource(R.drawable.btn_huise);

                tv_right.setText("确认装货");//右边按钮
                break;
            case 2://已装货
            case 3://部分卸货
                tv_left.setText("转派订单");//左边按钮
                tv_left.setBackgroundResource(R.drawable.btn_huise);

                tv_right.setText("确认卸货");//右边按钮
                break;
            case 7://订单完成
                tv_left.setText("转派订单");//左边按钮
                tv_left.setBackgroundResource(R.drawable.btn_lanse);

                tv_right.setText("配送完毕");//右边按钮
                break;

        }
    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        showProgress(true, getString(R.string.app_loading));

        /*String string = "?token=" + localUserInfo.getToken()
                + "&id=" + id;
        Request(string);*/
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(MapNavigationActivity.this, URLs.OrderDetails + string, new OkHttpClientManager.ResultCallback<OrderDetailsModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(OrderDetailsModel response) {
//                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>订单详情" + response);
                model = response;

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_xinxi:
            case R.id.iv_xinxi_2:
                //聊天
                Bundle bundle1 = new Bundle();
                bundle1.putString(EaseConstant.EXTRA_USER_ID, model.getTindent().getHx_username());
                CommonUtil.gotoActivityWithData(this, ChatActivity.class, bundle1, false);
                break;
            case R.id.iv_dianhua:
            case R.id.iv_dianhua_2:
                //打电话
                showToast("确认拨打 " + model.getTindent().getSend_mobile() + " 吗？", "确认", "取消",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //创建打电话的意图
                                Intent intent = new Intent();
                                //设置拨打电话的动作
                                intent.setAction(Intent.ACTION_CALL);//直接拨出电话
//                                                                        intent.setAction(Intent.ACTION_DIAL);//只调用拨号界面，不拨出电话
                                //设置拨打电话的号码
                                intent.setData(Uri.parse("tel:" + model.getTindent().getSend_mobile()));
                                //开启打电话的意图
                                startActivity(intent);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.linearLayout1:
                //查看详情
                linearLayout1.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_shouqi:
                //收起
                linearLayout1.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                break;
            case R.id.tv_fujiafei:
                //附加费
                Bundle bundle = new Bundle();
                bundle.putString("id", model.getTindent().getId());
                CommonUtil.gotoActivityWithData(MapNavigationActivity.this, AddSurchargeActivity.class, bundle, false);
                break;
            case R.id.tv_left:
                //左边按钮
                switch (tv_left.getText().toString().trim()) {
                    case "取消订单":
                        showToast("确认取消订单吗？", "确认", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!model.getTindent().getTerminal_id().equals("")){
                                    aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, Long.valueOf(model.getTindent().getTerminal_id())), onTrackListener);
                                    aMapTrackClient.stopGather(onTrackListener);
                                }
                                dialog.dismiss();
                                Map<String, String> params = new HashMap<>();
                                params.put("token", localUserInfo.getToken());
                                params.put("t_indent_id", model.getTindent().getId());
                                params.put("type", "6");//确认的类型1确认装货2提醒司机装货3确认卸货4附加费5转单确认6取消订单7接收转单
                                RequestZhuangHuo(params);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case "转派订单":
                        showToast("确认转派订单吗？", "确认", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //停止轨迹上报
                                if (!model.getTindent().getTerminal_id().equals("")){
                                    aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, Long.valueOf(model.getTindent().getTerminal_id())), onTrackListener);
                                    aMapTrackClient.stopGather(onTrackListener);
                                }

                                dialog.dismiss();
                                Bundle bundle = new Bundle();
                                bundle.putString("id", model.getTindent().getId());
                                CommonUtil.gotoActivityWithData(MapNavigationActivity.this, ZhuanDanActivity.class, bundle, false);
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }

                break;
            case R.id.tv_right:
                //右边按钮
                switch (tv_right.getText().toString().trim()) {
                    case "确认装货":
                        showToast("确认装货吗？", "确认", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                //判断是否到达地点
                                if (juli < 1000) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("token", localUserInfo.getToken());
                                    params.put("t_indent_id", model.getTindent().getId());
                                    params.put("type", "1");//确认的类型1确认装货2提醒司机装货3确认卸货4附加费
                                    RequestZhuangHuo(params);
                                } else {
                                    showToast("您未到达装货点");
                                }

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case "确认卸货":
                        showToast("确认卸货吗？", "确认", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (juli < 1000) {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("token", localUserInfo.getToken());
                                    params.put("t_indent_id", model.getTindent().getId());
                                    params.put("type", "3");//确认的类型1确认装货2提醒司机装货3确认卸货4附加费
                                    params.put("t_indent_addr_id", model.getTindent().getNext_addr().getAddr_id());
                                    RequestZhuangHuo(params);
                                } else {
                                    showToast("您未到达卸货点");
                                }
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                    case "配送完毕":
                        if (!model.getTindent().getTerminal_id().equals("")){
                            aMapTrackClient.stopTrack(new TrackParam(Constants.SERVICE_ID, Long.valueOf(model.getTindent().getTerminal_id())), onTrackListener);
                            aMapTrackClient.stopGather(onTrackListener);
                        }
                        //跳转附加费
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("id", model.getTindent().getId());
                        CommonUtil.gotoActivityWithData(MapNavigationActivity.this, AddSurchargeActivity.class, bundle2, true);
                        break;
                }
                break;
        }
    }


    private void RequestZhuangHuo(Map<String, String> params) {
        OkHttpClientManager.postAsyn(MapNavigationActivity.this, URLs.OrderDetails_ZhuangHuo, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(String response) {
//                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>司机-确认装货/卸货/发送附加费/附加费收取确认/转单确认" + response);
                myToast("确认成功");
                finish();
                /*JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    *//*JSONArray jsonArray = jObj.getJSONArray("data");
                    list = JSON.parseArray(jsonArray.toString(), Fragment1ListModel.class);
                    MyLogger.i(">>>>>>>" + list.size());*//*
                    myToast(jObj.getString("msg"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
            }
        }, false);
    }

    @Override
    protected void updateView() {
        titleView.setTitle("地图导航");
    }


    /**
     * *******************************导航回调****************************************************
     */

    @Override
    public void onInitNaviSuccess() {
        //初始化成功
//        mAMapNavi.startGPS();
        //货车信息
        if (aMapCarInfo == null) {
            aMapCarInfo = new AMapCarInfo();
            aMapCarInfo.setCarType("1");//设置车辆类型，0小车，1货车

            aMapCarInfo.setCarNumber(model.getTindent().getCar_type_info().getCar_number());//设置车辆的车牌号码. 如:京DFZ239,京ABZ239
            aMapCarInfo.setVehicleSize(model.getTindent().getCar_type_info().getVehicle_siz());// * 设置货车的大小
            aMapCarInfo.setVehicleLoad(model.getTindent().getCar_type_info().getVehicle_load());//设置货车的最大载重，单位：吨。
            aMapCarInfo.setVehicleWeight(model.getTindent().getCar_type_info().getVehicle_weight());//设置货车的载重
            aMapCarInfo.setVehicleLength(model.getTindent().getCar_type_info().getVehicle_length());//  * 设置货车的最大长度，单位：米。
            aMapCarInfo.setVehicleWidth(model.getTindent().getCar_type_info().getVehicle_width());//设置货车的最大宽度，单位：米。 如:1.8，1.5等等。
            aMapCarInfo.setVehicleHeight(model.getTindent().getCar_type_info().getVehicle_height());//设置货车的高度，单位：米。
            aMapCarInfo.setVehicleAxis(model.getTindent().getCar_type_info().getVehicle_axis());//设置货车的轴数
            aMapCarInfo.setVehicleLoadSwitch(true);//设置车辆的载重是否参与算路
            aMapCarInfo.setRestriction(true);//设置是否躲避车辆限行。
        }
        aMapCarInfo.setVehicleLoadSwitch(true);
        aMapCarInfo.setCarType("1");
        mAMapNavi.setCarInfo(aMapCarInfo);
        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
         *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
//            strategy = mAMapNavi.strategyConvert(true, false, false, false, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        MyLogger.i(">>>>>>>>规划路线>>>onInitNaviSuccess" + eList.size() + ">>>>>>" + sList.size());
//        mAMapNavi.calculateDriveRoute(sList, eList, mWayPointList, strategy);//有开始点
        mAMapNavi.calculateDriveRoute(eList, mWayPointList, strategy);//没开始点

       /* AmapNaviParams naviParams = new AmapNaviParams(null, null, null, AmapNaviType.DRIVER);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), naviParams,null);*/
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        MyLogger.i(">>>>>开始导航>>>>>>onCalculateRouteSuccess");
        hideProgress();
        //多路径算路成功回调
        //设置模拟导航的行车速度
//        mAMapNavi.setEmulatorNaviSpeed(200);//模拟速度
//        mAMapNavi.startNavi(NaviType.EMULATOR);//模拟导航
        mAMapNavi.startNavi(NaviType.GPS);//实时导航
        /**
         * 获取当前路线导航限制信息（例如： 限高，限宽）
         */
        List<AMapNaviLimitInfo> limitInfos = mAMapNavi.getNaviPath().getLimitInfos();

        /**
         * 获取当前路线导航禁行信息 (例如：禁行)
         */
        List<AMapNaviForbiddenInfo> forbiddenInfos = mAMapNavi.getNaviPath().getForbiddenInfos();

        int forbiddenCount = 0;
        int limitHeight = 0;
        int limitWidth = 0;
        if (limitInfos != null) {
            for (int i = 0; i < limitInfos.size(); i++) {
                AMapNaviLimitInfo limitInfo = limitInfos.get(i);
                /**
                 * 81 : 货车限高
                 * 82 : 货车限宽
                 * 83 : 货车限重
                 */
                if (limitInfo.type == NaviLimitType.TYPE_TRUCK_WIDTH_LIMIT) {
                    ++limitWidth;
                } else if (limitInfo.type == NaviLimitType.TYPE_TRUCK_HEIGHT_LIMIT) {
                    ++limitHeight;
                }
            }
        }

        if (forbiddenInfos != null) {
            forbiddenCount = forbiddenInfos.size();

            for (int i = 0; i < forbiddenInfos.size(); i++) {
                AMapNaviForbiddenInfo forbiddenInfo = forbiddenInfos.get(i);
                /**
                 * 0: 禁止左转
                 * 1: 禁止右转
                 * 2: 禁止左掉头
                 * 3: 禁止右掉头
                 * 4: 禁止直行
                 */
                switch (forbiddenInfo.forbiddenType) {
                    case NaviForbidType.FORBID_TURN_LEFT:
                        MyLogger.i("当前路线有禁止左转");
                        break;
                    case NaviForbidType.FORBID_TURN_RIGHT:
                        MyLogger.i("当前路线有禁止右转");
                        break;
                    case NaviForbidType.FORBID_TURN_LEFT_ROUND:
                        MyLogger.i("当前路线有禁止左掉头");
                        break;
                    case NaviForbidType.FORBID_TURN_RIGHT_ROUND:
                        MyLogger.i("当前路线有禁止右掉头");
                        break;
                    case NaviForbidType.FORBID_GO_STRAIGHT:
                        MyLogger.i("当前路线有禁止直行");
                        break;
                    default:
                }

            }
        }

        String limitStr = "有";
        if (limitHeight > 0) {
            limitStr += limitHeight + "处限高，";
        }

        if (limitWidth > 0) {
            limitStr += limitHeight + "处限宽，";
        }

        if (forbiddenCount > 0) {
            limitStr += forbiddenCount + "处限行，";
        }

        if (limitStr.length() > 2) {
            limitStr = limitStr.substring(0, limitStr.length() - 1);
            limitStr += "无法避开";
        } else {
            limitStr = null;
        }

        if (!TextUtils.isEmpty(limitStr)) {
            Toast.makeText(this, limitStr, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNaviSetting() {
        //底部导航设置点击回调
    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;

    }

    @Override
    public void onNaviMapMode(int i) {
        //导航态车头模式，0:车头朝上状态；1:正北朝上模式。
    }

    @Override
    public void onNaviTurnClick() {
        //转弯view的点击回调
    }

    @Override
    public void onNextRoadClick() {
        //下一个道路View点击回调
    }

    @Override
    public void onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Override
    public void onLockMap(boolean b) {
        //锁地图状态发生变化时回调
    }

    @Override
    public void onNaviViewLoaded() {
        MyLogger.i("wlx", "导航页面加载成功");
        MyLogger.i("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    @Override
    public void onInitNaviFailure() {
        myToast("初始化导航失败");
    }

    @Override
    public void onStartNavi(int i) {
        //启动导航后的回调函数
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        //当GPS位置有更新时的回调函数
//        MyLogger.i(">>>>>>>>>GPS位置" + aMapNaviLocation.getCoord().getLatitude());

    }

    @Override
    public void onGetNavigationText(int i, String s) {
        //导航播报信息回调函数。
    }

    @Override
    public void onGetNavigationText(String s) {
        //导航播报信息回调函数。
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {
        //到达目的地后回调函数。

    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        //路线计算失败
        Log.e("dm", "--------------------------------------------");
        Log.i("dm", "路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        Log.e("dm", "--------------------------------------------");
        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        MyLogger.i(">>>>>>>>距目的地剩余距离:" + naviInfo.getPathRetainDistance());
        juli = naviInfo.getPathRetainDistance();
//        MyLogger.i(">>>>>>>>距目的地剩余时间:"+naviInfo.getPathRetainTime());

        /*if (naviInfo.getPathRetainDistance() <100){
            if (tv_right.getText().toString().trim().equals("去装货")){
                tv_right.setText("确认装货");
            }else if (tv_right.getText().toString().trim().equals("去卸货")){
                tv_right.setText("确认卸货");
            }
        }*/
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void notifyParallelRoad(int i) {
        if (i == 0) {
            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {
            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();

            Log.d("wlx", "当前在辅路");
        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        //算路成功回调
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        //步行或者驾车路径规划失败后的回调函数
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }
}
