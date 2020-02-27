package com.transport.xianxian.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.bumptech.glide.Glide;
import com.cy.cyflowlayoutlibrary.FlowLayout;
import com.cy.cyflowlayoutlibrary.FlowLayoutAdapter;
import com.cy.dialog.BaseDialog;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.activity.CapitalStatisticsActivity;
import com.transport.xianxian.activity.MainActivity;
import com.transport.xianxian.activity.NoticeListActivity;
import com.transport.xianxian.activity.OrderDetailsActivity;
import com.transport.xianxian.activity.ScoreDetailActivity;
import com.transport.xianxian.base.BaseFragment;
import com.transport.xianxian.model.Fragment1ListModel;
import com.transport.xianxian.model.Fragment1Model;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;
import com.transport.xianxian.view.RollingView;
import com.transport.xianxian.zxing.CaptureActivity;
import com.transport.xianxian.zxing.Constant;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;


/**
 * Created by fafukeji01 on 2016/1/6.
 * 首页
 */

public class Fragment1 extends BaseFragment {
    ImageView imageView1;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    String indent_use_type = "", distance = "", temperature = "", time_start = "", time_end = "",
            addr = "";
    double lat = 0, lng = 0, juli = 0;
    long time = 0;
    Fragment1Model model;
    private RecyclerView recyclerView;
    LinearLayoutManager mLinearLayoutManager;
    List<Fragment1ListModel> list = new ArrayList<>();
    CommonAdapter<Fragment1ListModel> mAdapter;

    RollingView rollingView;//消息滚动
    List<String> xiaoxiArray = new ArrayList<>();

    ImageView btn_right;
    LinearLayout ll_xiaoxi, ll_pingfen;
    TextView tv_zijintongji, tv_type, tv_distance, tv_temperature, tv_timestart, tv_timestop,
            tv_kaishijiedan, tv_hint,tv_chongzhi;
    int i1 = 0, i2 = 0, i3 = 0, i4 = 0, i5 = 0;

    Boolean isStartJieDan = false;//是否开始接单
    //定位
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    TimeCount time1 = null;

    private DPoint mStartPoint = null;
    private DPoint mEndPoint = null;

    Handler startTimehandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            textView4.setText("" + (String) msg.obj);
        }
    };

    TimerTask timerTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        rollingView.resume();
        if (MainActivity.item == 0) {
            requestServer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        rollingView.pause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (MainActivity.item == 0) {
            requestServer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (time1 != null) {
            time1.cancel();
        }
        if (timerTask !=null){
            timerTask.cancel();
        }
        if (mLocationClient != null)
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    protected void initView(View view) {
        setSpringViewMore(false);//需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                String string = "?token=" + localUserInfo.getToken();
                Request(string);
            }

            @Override
            public void onLoadmore() {

            }
        });
        recyclerView = findViewByID_My(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);

        //公告消息
        rollingView = findViewByID_My(R.id.rollingView);

        btn_right = findViewByID_My(R.id.btn_right);
        btn_right.setOnClickListener(this);
        ll_xiaoxi = findViewByID_My(R.id.ll_xiaoxi);
        ll_xiaoxi.setOnClickListener(this);
        tv_zijintongji = findViewByID_My(R.id.tv_zijintongji);
        tv_zijintongji.setOnClickListener(this);
        ll_pingfen = findViewByID_My(R.id.ll_pingfen);
        ll_pingfen.setOnClickListener(this);

        tv_type = findViewByID_My(R.id.tv_type);
        tv_type.setOnClickListener(this);
        tv_distance = findViewByID_My(R.id.tv_distance);
        tv_distance.setOnClickListener(this);
        tv_temperature = findViewByID_My(R.id.tv_temperature);
        tv_temperature.setOnClickListener(this);
        tv_timestart = findViewByID_My(R.id.tv_timestart);
        tv_timestart.setOnClickListener(this);
        tv_timestop = findViewByID_My(R.id.tv_timestop);
        tv_timestop.setOnClickListener(this);
        tv_kaishijiedan = findViewByID_My(R.id.tv_kaishijiedan);
        tv_kaishijiedan.setOnClickListener(this);
        tv_hint = findViewByID_My(R.id.tv_hint);
        tv_chongzhi = findViewByID_My(R.id.tv_chongzhi);
        tv_chongzhi.setOnClickListener(this);

        imageView1 = findViewByID_My(R.id.imageView1);
        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);
        textView6 = findViewByID_My(R.id.textView6);

    }

    @Override
    protected void initData() {
//        requestServer();
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        AMapLocationClientOption option = new AMapLocationClientOption();
        //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。AMapLocationMode.Battery_Saving，低功耗模式。AMapLocationMode.Device_Sensors，仅设备模式。
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取一次定位结果：默认为false。
        option.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        option.setInterval(5 * 1000);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        option.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        option.setHttpTimeOut(30000);
        //是否开启定位缓存机制
        option.setLocationCacheEnable(false);

        mLocationClient.setLocationOption(option);

        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        /*//可在其中解析amapLocation获取相应内容。
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        aMapLocation.getLatitude();//获取纬度
                        aMapLocation.getLongitude();//获取经度
                        aMapLocation.getAccuracy();//获取精度信息
                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        aMapLocation.getCountry();//国家信息
                        aMapLocation.getProvince();//省信息
                        aMapLocation.getCity();//城市信息
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                        aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                        aMapLocation.getFloor();//获取当前室内定位的楼层
                        aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
                        //获取定位时间
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);*/
                        MyLogger.i(">>>>>>>>>>定位信息：\n纬度：" + aMapLocation.getLatitude()
                                + "\n经度:" + aMapLocation.getLongitude()
                                + "\n地址:" + aMapLocation.getAddress());
                        if (mLocationClient != null)
                            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        addr = aMapLocation.getAddress() + "";
                        lat = aMapLocation.getLatitude();
                        lng = aMapLocation.getLongitude();
                        requestlist();//请求数据

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        MyLogger.e("定位失败：", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        myToast("" + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(getActivity(), URLs.Fragment1 + string, new OkHttpClientManager.ResultCallback<Fragment1Model>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(Fragment1Model response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>首页" + response);
                //保存是否认证
                localUserInfo.setIsVerified(response.getIs_certification()+"");//1 认证 2 未认证

                model = response;
                textView1.setText(response.getNickname());//昵称
                textView2.setText("¥ " + response.getToday_money());//今日流水
                textView3.setText(response.getMoney());//账户余额
//                textView4.setText(response.getOnline_time());//在线时长
                time = response.getOnline_time() * 1000;

                if (timerTask == null) {
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            time = time + 1000;
//                        String dd = new DecimalFormat("00").format(time / 3600 / 24);
//                        String hh = new DecimalFormat("00").format(time / 3600);
//                        String mm = new DecimalFormat("00").format(time % 3600 / 60);
//                        String ss = new DecimalFormat("00").format(time % 60);
//                        String timeFormat = new String(hh + "小时" + mm + "分" + ss + "秒");
                            String timeFormat = CommonUtil.timedate5(time);//格式化时间

                            Message msg = new Message();
                            msg.obj = timeFormat;
                            startTimehandler.sendMessage(msg);
                        }

                    };
                    new Timer("在线计时器").scheduleAtFixedRate(timerTask, 0, 1000L);
                }

                textView5.setText(response.getIndent_count());//今日单量
                textView6.setText(response.getComment_score());//当前评分
                if (!response.getHead().equals(""))
                    Glide.with(getActivity())
                            .load(IMGHOST + response.getHead())
                            .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                            .into(imageView1);//加载图片

                //倒计时
                if (response.getFresh_second() > 0) {
                    if (isStartJieDan)
                        startTime();//开始倒计时
                }

                // 公告消息
                if (response.getNotice_list().size() > 0) {
                    ll_xiaoxi.setVisibility(View.VISIBLE);
                    xiaoxiArray.clear();
                    for (int i = 0; i < response.getNotice_list().size(); i++) {
                        xiaoxiArray.add(response.getNotice_list().get(i).getTitle());
                    }
                    rollingView.setPageSize(1);
                    rollingView.setClickColor(0xff888888);
//        rollingView.setLeftDrawable(R.drawable.drawable_red_dot);
                    rollingView.setRollingText(xiaoxiArray);// 绑定数据
                    rollingView.setOnItemClickListener(new RollingView.onItemClickListener() {
                        @Override
                        public void onItemClick(TextView v) {
//                        MyLogger.i(">>>>"+v.getText());
                            CommonUtil.gotoActivity(getActivity(), NoticeListActivity.class);
                        }
                    });
                } else {
                    ll_xiaoxi.setVisibility(View.GONE);
                }

                //订单类型
                indent_use_type = response.getIndent_use_type_list().get(0).getKey() + "";
                //距离
                distance = response.getDistance_list().get(0).getKey() + "";
                //温度
                temperature = response.getTemperature_list().get(0).getKey() + "";
                //开始
                time_start = response.getTime_start_list().get(0).getKey() + "";
                //结束
                time_end = response.getTime_end_list().get(0).getKey() + "";
            }
        });
    }

    private void RequestList(Map<String, String> params) {
        OkHttpClientManager.postAsyn(getActivity(), URLs.Fragment1List, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    showToast(info);
                }
                //接单出错
                showContentPage();
                isStartJieDan = false;
                tv_kaishijiedan.setBackgroundResource(R.drawable.btn_lanse);
                tv_kaishijiedan.setText("开始接单");
                tv_hint.setVisibility(View.VISIBLE);

                //停止定位
                if (mLocationClient != null)
                    mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁

                //关闭计时器
                if (time1 != null) {
                    time1.cancel();
                }

                //清空列表数据
                if (list.size() > 0) {
                    list.clear();
                    recyclerView.removeAllViews();
                }
            }

            @Override
            public void onResponse(String response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>接单列表" + response);

                JSONObject jObj;
//                List<Fragment1ListModel> list1 = new ArrayList<>();
                try {
                    jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("data");
                    list = JSON.parseArray(jsonArray.toString(), Fragment1ListModel.class);
                    MyLogger.i(">>>>>>>" + list.size());
//                        list.addAll(list1);
                    //初始化列表
                    mAdapter = new CommonAdapter<Fragment1ListModel>
                            (getActivity(), R.layout.item_fragment1, list) {
                        @Override
                        protected void convert(ViewHolder holder, Fragment1ListModel model, int position) {
                            //车型
                            TextView tv_type = holder.getView(R.id.tv_type);
                            tv_type.setText(model.getUse_type_text());
                            switch (model.getUse_type()) {
                                case 1:
                                    //专车
                                    tv_type.setBackgroundResource(R.drawable.yuanjiao_3_lanse);
                                    break;
                                case 2:
                                    //顺风车
                                    tv_type.setBackgroundResource(R.drawable.yuanjiao_3_huangse);
                                    break;
                                case 3:
                                    //快递
                                    tv_type.setBackgroundResource(R.drawable.yuanjiao_3_hongse);
                                    break;
                            }
                            //订单号
                            holder.setText(R.id.tv_ordernum, "订单号：" + model.getSn());


                            holder.setText(R.id.tv1, model.getNow_state() + " 装货");

                            if (model.getNow_state_action() >= 0) {
                                holder.setText(R.id.tv2, "已等待" + CommonUtil.timedate4(Math.abs(model.getNow_state_action()) * 1000));//离装货时间还有0小时
                            }else {
                                holder.setText(R.id.tv2, "已超过" + CommonUtil.timedate4(Math.abs(model.getNow_state_action()) * 1000));//离装货时间还有0小时
                            }
                            holder.setText(R.id.tv3, model.getRemark());//备注
                            holder.setText(R.id.tv4, model.getCreated_at() + " 发布");//发布时间
                            holder.setText(R.id.tv5, "¥ " + model.getPrice());//金额
                            //标签
                            FlowLayoutAdapter<String> flowLayoutAdapter;
                            /*List<String> stringList = new ArrayList<>();
                            for (int i = 0; i < model.getTag().size(); i++) {
                                stringList.add(model.getTag().get(i).getVal());
                            }*/
                            flowLayoutAdapter = new FlowLayoutAdapter<String>(model.getTag()) {
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
                            ((FlowLayout) holder.getView(R.id.flowLayout)).setAdapter(flowLayoutAdapter);

                            //送货地址
                            for (int i = 0; i < model.getAddr_list().size(); i++) {
                                //起点
                                if (i == 0) {
                                    holder.setText(R.id.tv_addr1, model.getAddr_list().get(i).getAddr());
                                    holder.setText(R.id.tv_title1, model.getAddr_list().get(i).getAddr_detail());
                                    if (lat != 0 && lng != 0) {
                                        mStartPoint = new DPoint(lat, lng);//起点
                                        mEndPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i).getLat()), Double.valueOf(model.getAddr_list().get(i).getLng()));//终点，39.995576,116.481288
                                        juli = CoordinateConverter.calculateLineDistance(mStartPoint, mEndPoint);
                                        holder.setText(R.id.tv_juli1, "距您" + CommonUtil.distanceFormat(juli));
                                    }
                                }
                                //终点
                                if (i == model.getAddr_list().size() - 1) {
                                    holder.setText(R.id.tv_addr2, model.getAddr_list().get(i).getAddr());
                                    holder.setText(R.id.tv_title2, model.getAddr_list().get(i).getAddr_detail());

                                    /*if (lat != 0 && lng != 0) {
                                        mStartPoint = new DPoint(lat, lng);//起点
                                        mEndPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i).getLat()), Double.valueOf(model.getAddr_list().get(i).getLng()));//终点，39.995576,116.481288
                                        juli = CoordinateConverter.calculateLineDistance(mStartPoint, mEndPoint);
                                        holder.setText(R.id.tv_juli2, "距您" + CommonUtil.distanceFormat(juli));
                                    }*/
                                    holder.setText(R.id.tv_juli2, "距起点" + CommonUtil.distanceFormat(Double.valueOf(model.getAddr_list().get(i).getMileage())));
                                }
                            }
                        }
                    };
                    mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                            //停止刷新数据
                            //停止定位
                            if (mLocationClient != null)
                                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁

                            //关闭计时器
                            if (time1 != null) {
                                time1.cancel();
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("id", list.get(i).getId());
                            CommonUtil.gotoActivityWithData(getActivity(), OrderDetailsActivity.class, bundle);
                        }

                        @Override
                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                            return false;
                        }
                    });

                    if (list.size() > 0) {
                        recyclerView.setAdapter(mAdapter);
//                    mAdapter.notifyDataSetChanged();
                    } else {
                        showEmptyPage();
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //倒计时
                if (model.getFresh_second() > 0) {
                    if (isStartJieDan)
                        startTime();//开始倒计时
                }
            }
        }, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                //扫一扫
                startQrCode();
                break;
            case R.id.ll_xiaoxi:
                //公告详情
                CommonUtil.gotoActivity(getActivity(), NoticeListActivity.class);
                break;
            case R.id.tv_zijintongji:
                //资金统计
                CommonUtil.gotoActivity(getActivity(), CapitalStatisticsActivity.class);
                break;
            case R.id.ll_pingfen:
                //评分详情
                CommonUtil.gotoActivity(getActivity(), ScoreDetailActivity.class);
                break;
            case R.id.tv_kaishijiedan:
                //开始接单
                isStartJieDan = !isStartJieDan;
                if (isStartJieDan) {
                    //点击开始接单
                    tv_kaishijiedan.setBackgroundResource(R.drawable.btn_huise);
                    tv_kaishijiedan.setText("关闭接单");
                    tv_hint.setVisibility(View.GONE);

                    //开始定位-请求数据-开始计时
                    if (mLocationClient != null) {
                        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//                        mLocationClient.stopLocation();
                        mLocationClient.startLocation();
                    }

//                    startAlarm();
                } else {
                    //点击关闭接单
                    tv_kaishijiedan.setBackgroundResource(R.drawable.btn_lanse);
                    tv_kaishijiedan.setText("开始接单");
                    tv_hint.setVisibility(View.VISIBLE);

                    //停止定位
                    if (mLocationClient != null)
                        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁

                    //关闭计时器
                    if (time1 != null) {
                        time1.cancel();
                    }

                    //清空列表数据
                    if (list.size() > 0) {
                        list.clear();
                        recyclerView.removeAllViews();
                    }

                    //调用关闭接单接口
                    Map<String, String> params = new HashMap<>();
                    params.put("token", localUserInfo.getToken());
                    params.put("work_close", "1");
                    RequestClose(params);
//                    stopAlarm();
                }
                break;
            case R.id.tv_chongzhi:
                //重置
                indent_use_type = "";
                distance = "";
                temperature = "";
                time_start = "";
                time_end = "";
                tv_type.setText("订单类型");
                tv_distance.setText("距离筛选");
                tv_temperature.setText("温层选择");
                tv_timestart.setText("接单时间起");
                tv_timestop.setText("接单时间止");
                requestlist();
                break;
            case R.id.tv_type:
                //订单类型
                BaseDialog dialog1 = new BaseDialog(getActivity());
                dialog1.contentView(R.layout.dialog_list)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView title = dialog1.findViewById(R.id.textView1);
                title.setText("订单类型");
                RecyclerView rv = dialog1.findViewById(R.id.recyclerView);
                LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(mLinearLayoutManager);

                List<String> mStringList = new ArrayList<>();
                if (model != null) {
                    for (int i = 0; i < model.getIndent_use_type_list().size(); i++) {
                        mStringList.add(model.getIndent_use_type_list().get(i).getVal());
                    }
                }

                CommonAdapter<String> adapter = new CommonAdapter<String>
                        (getActivity(), R.layout.item_dialog_list, mStringList) {
                    @Override
                    protected void convert(ViewHolder holder, String model, int position) {
                        TextView tv = holder.getView(R.id.textView);
                        ImageView iv = holder.getView(R.id.imageView);
                        tv.setText(model);
                        if (position == i1) {
                            tv.setTextColor(getResources().getColor(R.color.blue));
                            iv.setImageResource(R.mipmap.ic_xuanzhong);
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.black1));
                            iv.setImageResource(R.mipmap.ic_weixuan);
                        }
                    }
                };
                adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        i1 = i;
                        indent_use_type = model.getIndent_use_type_list().get(i).getKey() + "";
                        adapter.notifyDataSetChanged();
                        tv_type.setText(mStringList.get(i));
                        requestlist();
                        dialog1.dismiss();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        return false;
                    }
                });
                rv.setAdapter(adapter);
                dialog1.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                break;
            case R.id.tv_distance:
                //距离筛选
                BaseDialog dialog2 = new BaseDialog(getActivity());
                dialog2.contentView(R.layout.dialog_list)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView title2 = dialog2.findViewById(R.id.textView1);
                title2.setText("距离筛选");
                RecyclerView rv2 = dialog2.findViewById(R.id.recyclerView);
                LinearLayoutManager mLinearLayoutManager2 = new LinearLayoutManager(getActivity());
                rv2.setLayoutManager(mLinearLayoutManager2);

                List<String> mStringList2 = new ArrayList<>();
                if (model != null) {
                    for (int i = 0; i < model.getDistance_list().size(); i++) {
                        mStringList2.add(model.getDistance_list().get(i).getVal());
                    }
                }

                CommonAdapter<String> adapter2 = new CommonAdapter<String>
                        (getActivity(), R.layout.item_dialog_list, mStringList2) {
                    @Override
                    protected void convert(ViewHolder holder, String model, int position) {
                        TextView tv = holder.getView(R.id.textView);
                        ImageView iv = holder.getView(R.id.imageView);
                        tv.setText(model);
                        if (position == i2) {
                            tv.setTextColor(getResources().getColor(R.color.blue));
                            iv.setImageResource(R.mipmap.ic_xuanzhong);
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.black1));
                            iv.setImageResource(R.mipmap.ic_weixuan);
                        }
                    }
                };
                adapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        i2 = i;
                        distance = model.getDistance_list().get(i).getKey() + "";
                        adapter2.notifyDataSetChanged();
                        tv_distance.setText(mStringList2.get(i));
                        requestlist();
                        dialog2.dismiss();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        return false;
                    }
                });
                rv2.setAdapter(adapter2);
                dialog2.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                break;
            case R.id.tv_temperature:
                //温层选择
                BaseDialog dialog3 = new BaseDialog(getActivity());
                dialog3.contentView(R.layout.dialog_list)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView title3 = dialog3.findViewById(R.id.textView1);
                title3.setText("温层选择");
                RecyclerView rv3 = dialog3.findViewById(R.id.recyclerView);
                LinearLayoutManager mLinearLayoutManager3 = new LinearLayoutManager(getActivity());
                rv3.setLayoutManager(mLinearLayoutManager3);

                List<String> mStringList3 = new ArrayList<>();
                if (model != null) {
                    for (int i = 0; i < model.getTemperature_list().size(); i++) {
                        mStringList3.add(model.getTemperature_list().get(i).getVal());
                    }
                }

                CommonAdapter<String> adapter3 = new CommonAdapter<String>
                        (getActivity(), R.layout.item_dialog_list, mStringList3) {
                    @Override
                    protected void convert(ViewHolder holder, String model, int position) {
                        TextView tv = holder.getView(R.id.textView);
                        ImageView iv = holder.getView(R.id.imageView);
                        tv.setText(model);
                        if (position == i3) {
                            tv.setTextColor(getResources().getColor(R.color.blue));
                            iv.setImageResource(R.mipmap.ic_xuanzhong);
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.black1));
                            iv.setImageResource(R.mipmap.ic_weixuan);
                        }
                    }
                };
                adapter3.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        i3 = i;
                        temperature = model.getTemperature_list().get(i).getKey() + "";
                        adapter3.notifyDataSetChanged();
                        tv_temperature.setText(mStringList3.get(i));
                        requestlist();
                        dialog3.dismiss();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        return false;
                    }
                });
                rv3.setAdapter(adapter3);
                dialog3.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                break;
            case R.id.tv_timestart:
                //接单时间起
                BaseDialog dialog4 = new BaseDialog(getActivity());
                dialog4.contentView(R.layout.dialog_list)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView title4 = dialog4.findViewById(R.id.textView1);
                title4.setText("接单时间起");
                RecyclerView rv4 = dialog4.findViewById(R.id.recyclerView);
                LinearLayoutManager mLinearLayoutManager4 = new LinearLayoutManager(getActivity());
                rv4.setLayoutManager(mLinearLayoutManager4);

                List<String> mStringList4 = new ArrayList<>();
                if (model != null) {
                    for (int i = 0; i < model.getTime_start_list().size(); i++) {
                        mStringList4.add(model.getTime_start_list().get(i).getVal());
                    }
                }

                CommonAdapter<String> adapter4 = new CommonAdapter<String>
                        (getActivity(), R.layout.item_dialog_list, mStringList4) {
                    @Override
                    protected void convert(ViewHolder holder, String model, int position) {
                        TextView tv = holder.getView(R.id.textView);
                        ImageView iv = holder.getView(R.id.imageView);
                        tv.setText(model);
                        if (position == i4) {
                            tv.setTextColor(getResources().getColor(R.color.blue));
                            iv.setImageResource(R.mipmap.ic_xuanzhong);
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.black1));
                            iv.setImageResource(R.mipmap.ic_weixuan);
                        }
                    }
                };
                adapter4.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        i4 = i;
                        time_start = model.getTime_start_list().get(i).getKey() + "";
                        adapter4.notifyDataSetChanged();
                        tv_timestart.setText(mStringList4.get(i));
                        requestlist();
                        dialog4.dismiss();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        return false;
                    }
                });
                rv4.setAdapter(adapter4);
                dialog4.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
                break;
            case R.id.tv_timestop:
                //接单时间止
                BaseDialog dialog5 = new BaseDialog(getActivity());
                dialog5.contentView(R.layout.dialog_list)
                        .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT))
                        .animType(BaseDialog.AnimInType.CENTER)
                        .canceledOnTouchOutside(true)
                        .dimAmount(0.8f)
                        .show();
                TextView title5 = dialog5.findViewById(R.id.textView1);
                title5.setText("接单时间止");
                RecyclerView rv5 = dialog5.findViewById(R.id.recyclerView);
                LinearLayoutManager mLinearLayoutManager5 = new LinearLayoutManager(getActivity());
                rv5.setLayoutManager(mLinearLayoutManager5);

                List<String> mStringList5 = new ArrayList<>();
                if (model != null) {
                    for (int i = 0; i < model.getTime_end_list().size(); i++) {
                        mStringList5.add(model.getTime_end_list().get(i).getVal());
                    }
                }
                CommonAdapter<String> adapter5 = new CommonAdapter<String>
                        (getActivity(), R.layout.item_dialog_list, mStringList5) {
                    @Override
                    protected void convert(ViewHolder holder, String model, int position) {
                        TextView tv = holder.getView(R.id.textView);
                        ImageView iv = holder.getView(R.id.imageView);
                        tv.setText(model);
                        if (position == i5) {
                            tv.setTextColor(getResources().getColor(R.color.blue));
                            iv.setImageResource(R.mipmap.ic_xuanzhong);
                        } else {
                            tv.setTextColor(getResources().getColor(R.color.black1));
                            iv.setImageResource(R.mipmap.ic_weixuan);
                        }
                    }
                };
                adapter5.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        i5 = i;
                        time_end = model.getTime_end_list().get(i).getKey() + "";
                        adapter5.notifyDataSetChanged();
                        tv_timestop.setText(mStringList5.get(i));
                        requestlist();
                        dialog5.dismiss();
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                        return false;
                    }
                });
                rv5.setAdapter(adapter5);
                dialog5.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog5.dismiss();
                    }
                });
                break;
        }
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        showProgress(true, getString(R.string.app_loading));
        String string = "?token=" + localUserInfo.getToken();
        Request(string);
    }

    //获取接单列表
    private void requestlist() {
        Map<String, String> params = new HashMap<>();
        params.put("token", localUserInfo.getToken());
        params.put("addr", addr);
        params.put("lat", lat + "");
        params.put("lng", lng + "");
        params.put("indent_use_type", indent_use_type);
        params.put("distance", distance);
        params.put("temperature", temperature);
        params.put("time_start", time_start);
        params.put("time_end", time_end);
        if (isStartJieDan) {
            RequestList(params);
        }

    }

    private void startTime() {
        MyLogger.i(">>>>>>" + (model.getFresh_second() * 1000));
        if (time1 != null) {
            time1.cancel();
        }
        time1 = new TimeCount(model.getFresh_second() * 1000, 1000);//构造CountDownTimer对象
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
            if (MainActivity.item == 0) {
                if (mLocationClient != null) {
                    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                    mLocationClient.stopLocation();
                    mLocationClient.startLocation();
                }
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示

//            textView.setText(CommonUtil.timedate3(millisUntilFinished) + "s");//秒计时
//            textView.setText(CommonUtil.timedate4(millisUntilFinished, getActivity()));//时分秒倒计时
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        /*if (requestCode == 10086) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(getActivity(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("number", result);
                    CommonUtil.gotoActivityWithData(getActivity(), EquipmentDetailActivity.class, bundle1, false);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }*/
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
                MyLogger.i(">>>扫码返回>>>>" + scanResult);
                //点击转单-掉接口1-生成二维码
                //扫码-掉接口2-跳转到订单详情
                if (scanResult != null && !scanResult.equals("")) {
                    showToast("确认接受该单吗？", "确认", "取消",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    showProgress(true, "正在获取转单信息...");
                                    Map<String, String> params = new HashMap<>();
                                    params.put("token", localUserInfo.getToken());
                                    params.put("t_indent_confirm_id", scanResult);
                                    params.put("type", "7");//转单确认
                                    RequestZhuanDan(params);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                    /*Bundle bundle1 = new Bundle();
                    bundle1.putString("id", scanResult);
                    CommonUtil.gotoActivityWithData(getActivity(), OrderDetailsActivity.class, bundle1, false);*/
                }
            }
        }
    }

    private void RequestZhuanDan(Map<String, String> params) {
        OkHttpClientManager.postAsyn(getActivity(), URLs.OrderDetails_ZhuangHuo, params, new OkHttpClientManager.ResultCallback<String>() {
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
                MyLogger.i(">>>>>>>>>司机-转单确认" + response);
//                myToast("确认成功");
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    myToast(jObj.getString("msg"));
                    JSONObject data = jObj.getJSONObject("data");
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", data.getString("id"));
                    CommonUtil.gotoActivityWithData(getActivity(), OrderDetailsActivity.class, bundle1, false);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, false);
    }
    private void RequestClose(Map<String, String> params) {
        OkHttpClientManager.postAsyn(getActivity(), URLs.Fragment1List, params, new OkHttpClientManager.ResultCallback<String>() {
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
                MyLogger.i(">>>>>>>>>司机-关闭接单" + response);
//                myToast("关闭成功");
                /*JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    myToast(jObj.getString("msg"));

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/

            }
        }, false);
    }

}
