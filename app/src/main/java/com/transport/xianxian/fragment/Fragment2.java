package com.transport.xianxian.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
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
import com.hyphenate.easeui.EaseConstant;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.activity.AddSurchargeActivity;
import com.transport.xianxian.activity.ChatActivity;
import com.transport.xianxian.activity.MainActivity;
import com.transport.xianxian.activity.MapNavigationActivity;
import com.transport.xianxian.activity.OrderDetailsActivity;
import com.transport.xianxian.activity.TrackSearchActivity;
import com.transport.xianxian.activity.ZhuanDanActivity;
import com.transport.xianxian.base.BaseFragment;
import com.transport.xianxian.model.Fragment2Model1;
import com.transport.xianxian.model.OrderDetailsModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;


/**
 * Created by fafukeji01 on 2016/1/6.
 * 订单
 */
public class Fragment2 extends BaseFragment {
    int page1 = 1, page2 = 1, page3 = 1, status = 1;
    private RecyclerView recyclerView;
    List<Fragment2Model1> list1 = new ArrayList<>();
    CommonAdapter<Fragment2Model1> mAdapter1;

    List<Fragment2Model1> list2 = new ArrayList<>();
    CommonAdapter<Fragment2Model1> mAdapter2;

    List<Fragment2Model1> list3 = new ArrayList<>();
    CommonAdapter<Fragment2Model1> mAdapter3;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private TextView textView1, textView2, textView3;
    private View view1, view2, view3;

    //定位
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    double lat = 0, lng = 0, juli = 0;
    private DPoint mStartPoint = null;
    private DPoint mEndPoint = null;

    int scale = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null)
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    public void onStart() {
        super.onStart();
        /*if (MainActivity.item == 1) {
            requestServer();
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.item == 1) {
            requestServer();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (MainActivity.item == 1) {
            requestServer();
        }
    }

    @Override
    protected void initView(View view) {
        //刷新
        setSpringViewMore(true);//不需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                String string = "";
                switch (status) {
                    case 1:
                        page1 = 1;
                        string = "?page=" + page1//当前页号
                                + "&count=" + "10"//页面行数
                                + "&status=" + status//1进行中2已完成3已取消
                                + "&token=" + localUserInfo.getToken();
                        break;
                    case 2:
                        page2 = 1;
                        string = "?page=" + page2//当前页号
                                + "&count=" + "10"//页面行数
                                + "&status=" + status//1进行中2已完成3已取消
                                + "&token=" + localUserInfo.getToken();
                        break;
                    case 3:
                        page3 = 1;
                        string = "?page=" + page3//当前页号
                                + "&count=" + "10"//页面行数
                                + "&status=" + status//1进行中2已完成3已取消
                                + "&token=" + localUserInfo.getToken();
                        break;
                }
                Request(string);
            }

            @Override
            public void onLoadmore() {
                String string = "";
                switch (status) {
                    case 1:
                        page1++;
                        string = "?page=" + page1//当前页号
                                + "&count=" + "10"//页面行数
                                + "&status=" + status//1进行中2已完成3已取消
                                + "&token=" + localUserInfo.getToken();
                        break;
                    case 2:
                        page2++;
                        string = "?page=" + page2//当前页号
                                + "&count=" + "10"//页面行数
                                + "&status=" + status//1进行中2已完成3已取消
                                + "&token=" + localUserInfo.getToken();
                        break;
                    case 3:
                        page3++;
                        string = "?page=" + page3//当前页号
                                + "&count=" + "10"//页面行数
                                + "&status=" + status//1进行中2已完成3已取消
                                + "&token=" + localUserInfo.getToken();
                        break;
                }
                RequestMore(string);
            }
        });

        recyclerView = findViewByID_My(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);

        linearLayout1 = findViewByID_My(R.id.linearLayout1);
        linearLayout2 = findViewByID_My(R.id.linearLayout2);
        linearLayout3 = findViewByID_My(R.id.linearLayout3);

        linearLayout1.setOnClickListener(this);
        linearLayout2.setOnClickListener(this);
        linearLayout3.setOnClickListener(this);

        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);

        view1 = findViewByID_My(R.id.view1);
        view2 = findViewByID_My(R.id.view2);
        view3 = findViewByID_My(R.id.view3);

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

                        lat = aMapLocation.getLatitude();
                        lng = aMapLocation.getLongitude();
                        mStartPoint = new DPoint(lat, lng);//起点

                        String string = "";
                        switch (status) {
                            case 1:
                                page1 = 1;
                                string = "?page=" + page1//当前页号
                                        + "&count=" + "10"//页面行数
                                        + "&status=" + status//1进行中2已完成3已取消
                                        + "&token=" + localUserInfo.getToken();
                                break;
                            case 2:
                                page2 = 1;
                                string = "?page=" + page2//当前页号
                                        + "&count=" + "10"//页面行数
                                        + "&status=" + status//1进行中2已完成3已取消
                                        + "&token=" + localUserInfo.getToken();
                                break;
                            case 3:
                                page3 = 1;
                                string = "?page=" + page3//当前页号
                                        + "&count=" + "10"//页面行数
                                        + "&status=" + status//1进行中2已完成3已取消
                                        + "&token=" + localUserInfo.getToken();
                                break;
                        }
                        Request(string);

                    } else {
                        hideProgress();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout1:
                status = 1;
//                changeUI();
                requestServer();
                break;
            case R.id.linearLayout2:
                status = 2;
//                changeUI();
                requestServer();
                break;
            case R.id.linearLayout3:
                status = 3;
//                changeUI();
                requestServer();
                break;
        }
    }

    private void changeUI() {
        switch (status) {
            case 1:
                textView1.setTextColor(getResources().getColor(R.color.blue));
                textView2.setTextColor(getResources().getColor(R.color.black2));
                textView3.setTextColor(getResources().getColor(R.color.black2));
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view3.setVisibility(View.INVISIBLE);
                if (list1.size() > 0) {
                    showContentPage();
                    recyclerView.setAdapter(mAdapter1);
                    mAdapter1.notifyDataSetChanged();
                } else {
                    showEmptyPage();//空数据
                }

                break;
            case 2:
                textView1.setTextColor(getResources().getColor(R.color.black2));
                textView2.setTextColor(getResources().getColor(R.color.blue));
                textView3.setTextColor(getResources().getColor(R.color.black2));

                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.INVISIBLE);

                if (list2.size() > 0) {
                    showContentPage();
                    recyclerView.setAdapter(mAdapter2);
                    mAdapter2.notifyDataSetChanged();
                } else {
                    showEmptyPage();//空数据
                }
                break;
            case 3:
                textView1.setTextColor(getResources().getColor(R.color.black2));
                textView2.setTextColor(getResources().getColor(R.color.black2));
                textView3.setTextColor(getResources().getColor(R.color.blue));
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view3.setVisibility(View.VISIBLE);
                if (list3.size() > 0) {
                    showContentPage();
                    recyclerView.setAdapter(mAdapter3);
                    mAdapter3.notifyDataSetChanged();
                } else {
                    showEmptyPage();//空数据
                }
                break;
            default:
                break;
        }
//        requestServer();
    }

    @Override
    protected void updateView() {

    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        showProgress(true, getString(R.string.app_loading));
        if (mLocationClient != null) {
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(getActivity(), URLs.Fragment2 + string,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, String info, Exception e) {
                        showErrorPage();
                        hideProgress();
                        if (!info.equals("")) {
                            myToast(info);
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        showContentPage();
                        MyLogger.i(">>>>>>>>>订单" + response);
                        JSONObject jObj;
                        try {
                            jObj = new JSONObject(response);
                            JSONArray jsonArray = jObj.getJSONArray("data");
                            switch (status) {
                                case 1:
                                    /**
                                     * *************************进行中**************************************
                                     * */
                                    list1 = JSON.parseArray(jsonArray.toString(), Fragment2Model1.class);

                                    mAdapter1 = new CommonAdapter<Fragment2Model1>
                                            (getActivity(), R.layout.item_fragment2_1, list1) {
                                        @Override
                                        protected void convert(ViewHolder holder, Fragment2Model1 model, int position) {
                                            //订单号
                                            holder.setText(R.id.tv_ordernum, "订单号：" + model.getSn());

                                            holder.setText(R.id.textView1, model.getNow_state_action());//状态
                                            holder.setText(R.id.textView2, model.getNow_state_sub_action());//配送中
                                            //车型
                                            TextView tv_3 = holder.getView(R.id.textView3);
                                            tv_3.setText(model.getUse_type());
                                            switch (model.getUse_type_id()) {
                                                case 1:
                                                    //专车
                                                    tv_3.setBackgroundResource(R.drawable.yuanjiao_3_lanse);
                                                    break;
                                                case 2:
                                                    //顺风车
                                                    tv_3.setBackgroundResource(R.drawable.yuanjiao_3_huangse);
                                                    break;
                                                case 3:
                                                    //快递
                                                    tv_3.setBackgroundResource(R.drawable.yuanjiao_3_hongse);
                                                    break;
                                            }

                                            holder.setText(R.id.textView4, model.getNow_state() + model.getNow_state_action());//几点卸货/装货
                                            ImageView imageView1 = holder.getView(R.id.imageView1);
                                            if (!model.getSend_head().equals(""))
                                                Glide.with(getActivity())
                                                        .load(IMGHOST + model.getSend_head())
                                                        .centerCrop()
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                                                        .into(imageView1);//加载图片
                                            holder.setText(R.id.textView5, model.getSend_name());//姓名
                                            holder.setText(R.id.textView6, model.getIndustry());//行业
                                            holder.setText(R.id.textView7, "货源单号" + model.getSn());//货源单号
                                            holder.setText(R.id.textView8, model.getCreated_at() + " 发布");//发布时间
                                            holder.setText(R.id.textView9, model.getRemark());//备注
                                            holder.setText(R.id.textView10, "¥ " + model.getPrice());//订单金额
                                            //费用列表
                                            LinearLayout ll_add2 = holder.getView(R.id.ll_add2);
                                            ll_add2.removeAllViews();
                                            for (int i = 0; i < model.getPrice_detail().size(); i++) {
                                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                LayoutInflater inflater = LayoutInflater.from(getActivity());
                                                View view = inflater.inflate(R.layout.item_add_fragment2_2, null, false);
                                                view.setLayoutParams(lp);

                                                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                                                TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
                                                tv_title.setText(model.getPrice_detail().get(i).getTitle());
                                                tv_money.setText(model.getPrice_detail().get(i).getPrice() + "元");
                                                ll_add2.addView(view);
                                            }

                                            //地址列表
                                            LinearLayout ll_add = holder.getView(R.id.ll_add);
                                            ll_add.removeAllViews();
                                            for (int i = 0; i < model.getAddr_list().size(); i++) {
                                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                LayoutInflater inflater = LayoutInflater.from(getActivity());
                                                View view = inflater.inflate(R.layout.item_add_fragment2_1, null, false);
                                                view.setLayoutParams(lp);
                                                ImageView iv1 = (ImageView) view.findViewById(R.id.iv1);
                                                TextView tv1 = (TextView) view.findViewById(R.id.tv1);
                                                TextView tv2 = (TextView) view.findViewById(R.id.tv2);
                                                TextView tv3 = (TextView) view.findViewById(R.id.tv3);
                                                TextView tv4 = (TextView) view.findViewById(R.id.tv4);
                                                TextView tv5 = (TextView) view.findViewById(R.id.tv5);
                                                TextView tv6 = (TextView) view.findViewById(R.id.tv6);
                                                TextView tv7 = (TextView) view.findViewById(R.id.tv7);
                                                if (i == 0) {
                                                    tv1.setText("起");
                                                    tv1.setBackgroundResource(R.drawable.yuanxing_lanse);

                                                    //起点 开始点为 当前所在点  终点为这个点的坐标
                                                    mEndPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i).getLat()), Double.valueOf(model.getAddr_list().get(i).getLng()));//终点，39.995576,116.481288
                                                    tv6.setText("接货距离" + CommonUtil.distanceFormat(CoordinateConverter.calculateLineDistance(mStartPoint, mEndPoint)));//送货路程
                                                } else if (i == (model.getAddr_list().size() - 1)) {
                                                    tv1.setText("终");
                                                    tv1.setBackgroundResource(R.drawable.yuanxing_juse);

                                                    //开始点为 上一个点的坐标  终点为这个点的坐标
                                                    mStartPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i-1).getLat()), Double.valueOf(model.getAddr_list().get(i-1).getLng()));//起点
                                                    mEndPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i).getLat()), Double.valueOf(model.getAddr_list().get(i).getLng()));//终点，39.995576,116.481288
                                                    tv6.setText("送货路程" + CommonUtil.distanceFormat(CoordinateConverter.calculateLineDistance(mStartPoint, mEndPoint)));//送货路程
                                                } else {
                                                    tv1.setText("" + i);
                                                    tv1.setBackgroundResource(R.drawable.yuanxing_huise);

                                                    //开始点为 上一个点的坐标  终点为这个点的坐标
                                                    mStartPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i-1).getLat()), Double.valueOf(model.getAddr_list().get(i-1).getLng()));//起点
                                                    mEndPoint = new DPoint(Double.valueOf(model.getAddr_list().get(i).getLat()), Double.valueOf(model.getAddr_list().get(i).getLng()));//终点，39.995576,116.481288
                                                    tv6.setText("送货路程" + CommonUtil.distanceFormat(CoordinateConverter.calculateLineDistance(mStartPoint, mEndPoint)));//送货路程
                                                }
                                                tv2.setText(model.getAddr_list().get(i).getArrive_time() + " " + model.getAddr_list().get(i).getStatus_text());//time 装货
                                                /*if (i == 0){
                                                    tv3.setVisibility(View.GONE);
                                                }else {
                                                    tv3.setVisibility(View.VISIBLE);
                                                    tv3.setText("估计用时：" + CommonUtil.timedate3(Long.valueOf(model.getAddr_list().get(i).getPre_time())) + "分钟");//估计用时：
                                                }*/
                                                tv4.setText(model.getAddr_list().get(i).getAddr());//地址
                                                tv5.setText(model.getAddr_list().get(i).getAddr_detail());//地址详情
//                                                    juli = CoordinateConverter.calculateLineDistance(mStartPoint, mEndPoint);
                                                if (!model.getAddr_list().get(i).getOther().equals("")) {
                                                    tv7.setVisibility(View.VISIBLE);
                                                    tv7.setText(model.getAddr_list().get(i).getOther());//备注
                                                } else {
                                                    tv7.setVisibility(View.GONE);
                                                }
                                                TextView tv_daohang = view.findViewById(R.id.tv_daohang);
                                                tv_daohang.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //去导航-先请求详情数据
                                                        showProgress(true, "正在获取导航数据...");
                                                        String string = "?token=" + localUserInfo.getToken()
                                                                + "&id=" + model.getId();
                                                        RequestDetail(string);
                                                    }
                                                });
                                                //是否显示去导航按钮、左边位置标识
                                                if (model.getAddr_list().get(i).getIs_show() == 1) {
                                                    iv1.setVisibility(View.VISIBLE);
                                                    tv_daohang.setVisibility(View.VISIBLE);
                                                } else {
                                                    iv1.setVisibility(View.GONE);
                                                    tv_daohang.setVisibility(View.GONE);
                                                }

                                                ll_add.addView(view);
                                            }

                                            //客户要求
                                            FlowLayoutAdapter<String> flowLayoutAdapter1;
                                                /*List<String> tagList1 = new ArrayList<>();
                                                for (int i = 0; i < model.getTag().size(); i++) {
                                                    tagList1.add(model.getTag().get(i));
                                                }*/
                                            flowLayoutAdapter1 = new FlowLayoutAdapter<String>(model.getTag()) {
                                                @Override
                                                public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
                                                    TextView tv = holder.getView(R.id.tv);
                                                    tv.setText(bean);
                                                }

                                                @Override
                                                public void onItemClick(int position, String bean) {
                                                }

                                                @Override
                                                public int getItemLayoutID(int position, String bean) {
                                                    return R.layout.item_flowlayout;
                                                }
                                            };
                                            ((FlowLayout) holder.getView(R.id.flowLayout1)).setAdapter(flowLayoutAdapter1);

                                            //货物描述
                                            LinearLayout ll_huowumiaoshu = holder.getView(R.id.ll_huowumiaoshu);
                                            if (model.getGoods_desc().size() > 0) {
                                                ll_huowumiaoshu.setVisibility(View.VISIBLE);
                                                FlowLayoutAdapter<String> flowLayoutAdapter2;
                                                /*List<String> tagList2 = new ArrayList<>();
                                                for (int i = 0; i < model.getGoods_desc().size(); i++) {
                                                    tagList2.add(model.getGoods_desc().get(i));
                                                }*/
                                                flowLayoutAdapter2 = new FlowLayoutAdapter<String>(model.getGoods_desc()) {
                                                    @Override
                                                    public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
                                                        TextView tv = holder.getView(R.id.tv);
                                                        tv.setText(bean);
                                                    }

                                                    @Override
                                                    public void onItemClick(int position, String bean) {
                                                    }

                                                    @Override
                                                    public int getItemLayoutID(int position, String bean) {
                                                        return R.layout.item_flowlayout;
                                                    }
                                                };
                                                ((FlowLayout) holder.getView(R.id.flowLayout2)).setAdapter(flowLayoutAdapter2);
                                            } else {
                                                ll_huowumiaoshu.setVisibility(View.GONE);
                                            }


                                            //顺风车订单
                                            holder.getView(R.id.tv_shunfengche).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    MainActivity.item = 0;
                                                    MainActivity.mBottomTabBar.setCurrentTab(0);
                                                }
                                            });
                                            //转单
                                            holder.getView(R.id.tv_zhuandan).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    BaseDialog dialog1 = new BaseDialog(getActivity());
                                                    dialog1.contentView(R.layout.dialog_zhuandan)
                                                            .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                    ViewGroup.LayoutParams.WRAP_CONTENT))
                                                            .animType(BaseDialog.AnimInType.CENTER)
                                                            .canceledOnTouchOutside(true)
                                                            .dimAmount(0.8f)
                                                            .show();
                                                    TextView tv_bili = dialog1.findViewById(R.id.tv_bili);
                                                    SeekBar seekBar = dialog1.findViewById(R.id.seekBar);

                                                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                        @Override
                                                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                            scale = progress;
                                                            tv_bili.setText("金额比例："+progress+"%");
                                                        }

                                                        @Override
                                                        public void onStartTrackingTouch(SeekBar seekBar) {

                                                        }

                                                        @Override
                                                        public void onStopTrackingTouch(SeekBar seekBar) {

                                                        }
                                                    });
                                                    dialog1.findViewById(R.id.textView3).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog1.dismiss();
                                                            Bundle bundle = new Bundle();
                                                            bundle.putString("id", model.getId());
                                                            bundle.putString("lat", lat+"");
                                                            bundle.putString("lng", lng+"");
                                                            bundle.putString("scale", scale+"");
                                                            CommonUtil.gotoActivityWithData(getActivity(), ZhuanDanActivity.class, bundle, false);
                                                        }
                                                    });
                                                    dialog1.findViewById(R.id.textView4).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog1.dismiss();
                                                        }
                                                    });
                                                    dialog1.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog1.dismiss();
                                                        }
                                                    });

                                                    /*showToast("确认转派订单吗？", "确认", "取消", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();



                                                        }
                                                    }, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            dialog.dismiss();
                                                        }
                                                    });*/
                                                }
                                            });
                                            //去聊天
                                            holder.getView(R.id.iv_xinxi).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(EaseConstant.EXTRA_USER_ID, model.getHx_username());
                                                    CommonUtil.gotoActivityWithData(getActivity(), ChatActivity.class, bundle, false);
                                                }
                                            });
                                            //拨打电话
                                            holder.getView(R.id.iv_dianhua).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showToast("确认拨打 " + model.getSend_mobile() + " 吗？", "确认", "取消",
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
                                                                    intent.setData(Uri.parse("tel:" + model.getSend_mobile()));
                                                                    //开启打电话的意图
                                                                    startActivity(intent);
                                                                }
                                                            }, new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                }
                                                            });


                                                }
                                            });
                                            //附加费
                                            holder.getView(R.id.tv_fujiafei).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("id", model.getId());
                                                    CommonUtil.gotoActivityWithData(getActivity(), AddSurchargeActivity.class, bundle, false);
                                                }
                                            });
                                        }
                                    };
                                    mAdapter1.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", list1.get(i).getId());
                                            CommonUtil.gotoActivityWithData(getActivity(), OrderDetailsActivity.class, bundle);
                                        }

                                        @Override
                                        public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                                            return false;
                                        }
                                    });

                                    break;
                                case 2:
                                    /**
                                     * *************************已完成**************************************
                                     * */
                                    list2 = JSON.parseArray(jsonArray.toString(), Fragment2Model1.class);
                                    mAdapter2 = new CommonAdapter<Fragment2Model1>
                                            (getActivity(), R.layout.item_fragment2_2, list2) {
                                        @Override
                                        protected void convert(ViewHolder holder, Fragment2Model1 model, int position) {
                                            //订单号
                                            holder.setText(R.id.tv_ordernum, "订单号：" + model.getSn());
//                                            holder.setText(R.id.textView1, model.getNow_state_action());//状态
                                            //车型
                                            TextView tv_3 = holder.getView(R.id.textView2);
                                            tv_3.setText(model.getUse_type());
                                            switch (model.getUse_type_id()) {
                                                case 1:
                                                    //专车
                                                    tv_3.setBackgroundResource(R.drawable.yuanjiao_3_lanse);
                                                    break;
                                                case 2:
                                                    //顺风车
                                                    tv_3.setBackgroundResource(R.drawable.yuanjiao_3_huangse);
                                                    break;
                                                case 3:
                                                    //快递
                                                    tv_3.setBackgroundResource(R.drawable.yuanjiao_3_hongse);
                                                    break;
                                            }
                                            holder.setText(R.id.textView4, model.getNow_state() + model.getNow_state_action());//几点卸货/装货
                                            holder.setText(R.id.textView5, "送货用时：" + model.getSend_time());//送货用时：time
                                            holder.setText(R.id.textView6, "比预定卸货时间：" + model.getCompare_time());//比预定卸货时间：time
                                            ImageView imageView1 = holder.getView(R.id.imageView1);
                                            if (!model.getSend_head().equals(""))
                                                Glide.with(getActivity())
                                                        .load(IMGHOST + model.getSend_head())
                                                        .centerCrop()
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                                                        .into(imageView1);//加载图片
                                            holder.setText(R.id.textView7, model.getSend_name());//姓名
                                            holder.setText(R.id.textView8, model.getIndustry());//行业
                                            holder.setText(R.id.textView9, "货源单号" + model.getSn());//货源单号
                                            holder.setText(R.id.textView10, model.getCreated_at() + " 发布");//发布时间
                                            holder.setText(R.id.textView11, model.getRemark());//备注
                                            holder.setText(R.id.textView12, "¥ " + model.getPrice());//订单金额

                                            //费用列表
                                            LinearLayout ll_add2 = holder.getView(R.id.ll_add2);
                                            ll_add2.removeAllViews();
                                            for (int i = 0; i < model.getPrice_detail().size(); i++) {
                                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                LayoutInflater inflater = LayoutInflater.from(getActivity());
                                                View view = inflater.inflate(R.layout.item_add_fragment2_2, null, false);
                                                view.setLayoutParams(lp);

                                                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                                                TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
                                                tv_title.setText(model.getPrice_detail().get(i).getTitle());
                                                tv_money.setText(model.getPrice_detail().get(i).getPrice() + "元");
                                                ll_add2.addView(view);
                                            }
                                            //地址列表
                                            for (int i = 0; i < model.getAddr_list().size(); i++) {
                                                //起点
                                                if (i == 0) {
                                                    if (!model.getAddr_list().get(i).getArrive_time().equals(""))
                                                        holder.setText(R.id.tv_time1, model.getAddr_list().get(i).getArrive_time() + " 装货");

                                                    holder.setText(R.id.tv_addr1, model.getAddr_list().get(i).getAddr());
                                                    holder.setText(R.id.tv_title1, model.getAddr_list().get(i).getAddr_detail());
                                                    holder.setText(R.id.tv_juli1, "送货路程" + CommonUtil.distanceFormat(Double.valueOf(model.getAddr_list().get(i).getMileage())));
                                                }
                                                //终点
                                                if (i == model.getAddr_list().size() - 1) {
                                                    if (!model.getAddr_list().get(i).getArrive_time().equals(""))
                                                        holder.setText(R.id.tv_time2, model.getAddr_list().get(i).getArrive_time() + " 卸货");
                                                    holder.setText(R.id.tv_addr2, model.getAddr_list().get(i).getAddr());
                                                    holder.setText(R.id.tv_title2, model.getAddr_list().get(i).getAddr_detail());
                                                    holder.setText(R.id.tv_juli2, "送货路程" + CommonUtil.distanceFormat(Double.valueOf(model.getAddr_list().get(i).getMileage())));
                                                }
                                            }

                                            //客户要求
                                            FlowLayoutAdapter<String> flowLayoutAdapter1;
                                            flowLayoutAdapter1 = new FlowLayoutAdapter<String>(model.getTag()) {
                                                @Override
                                                public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
                                                    TextView tv = holder.getView(R.id.tv);
                                                    tv.setText(bean);
                                                }

                                                @Override
                                                public void onItemClick(int position, String bean) {
                                                }

                                                @Override
                                                public int getItemLayoutID(int position, String bean) {
                                                    return R.layout.item_flowlayout;
                                                }
                                            };
                                            ((FlowLayout) holder.getView(R.id.flowLayout1)).setAdapter(flowLayoutAdapter1);

                                            //货物描述
                                            LinearLayout ll_huowumiaoshu = holder.getView(R.id.ll_huowumiaoshu);
                                            if (model.getGoods_desc().size() > 0) {
                                                ll_huowumiaoshu.setVisibility(View.VISIBLE);
                                                FlowLayoutAdapter<String> flowLayoutAdapter2;
                                                /*List<String> tagList2 = new ArrayList<>();
                                                for (int i = 0; i < model.getGoods_desc().size(); i++) {
                                                    tagList2.add(model.getGoods_desc().get(i));
                                                }*/
                                                flowLayoutAdapter2 = new FlowLayoutAdapter<String>(model.getGoods_desc()) {
                                                    @Override
                                                    public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
                                                        TextView tv = holder.getView(R.id.tv);
                                                        tv.setText(bean);
                                                    }

                                                    @Override
                                                    public void onItemClick(int position, String bean) {
                                                    }

                                                    @Override
                                                    public int getItemLayoutID(int position, String bean) {
                                                        return R.layout.item_flowlayout;
                                                    }
                                                };
                                                ((FlowLayout) holder.getView(R.id.flowLayout2)).setAdapter(flowLayoutAdapter2);
                                            } else {
                                                ll_huowumiaoshu.setVisibility(View.GONE);
                                            }

                                            //行驶轨迹
                                            holder.getView(R.id.tv_guiji).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("Fragment2Model1", model);
                                                    CommonUtil.gotoActivityWithData(getActivity(), TrackSearchActivity.class, bundle, false);

                                                }
                                            });
                                            //去聊天
                                            holder.getView(R.id.iv_xinxi).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString(EaseConstant.EXTRA_USER_ID, model.getHx_username());
                                                    CommonUtil.gotoActivityWithData(getActivity(), ChatActivity.class, bundle, false);
                                                }
                                            });
                                            //拨打电话
                                            holder.getView(R.id.iv_dianhua).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    showToast("确认拨打 " + model.getSend_mobile() + " 吗？", "确认", "取消",
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
                                                                    intent.setData(Uri.parse("tel:" + model.getSend_mobile()));
                                                                    //开启打电话的意图
                                                                    startActivity(intent);
                                                                }
                                                            }, new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();
                                                                }
                                                            });


                                                }
                                            });
                                            //收取附加费
                                            TextView tv_shouqu = holder.getView(R.id.tv_shouqu);
                                            if (model.getIs_attach_fee() == 1) {
                                                //已收取
                                                holder.setText(R.id.textView3, "附加费已收取");//附加费未收取
                                                tv_shouqu.setText("已收取");
                                                holder.setText(R.id.tv_fujiafei, "附加费已收取");//附加费未收取
                                            } else {
                                                holder.setText(R.id.textView3, "附加费未收取");//附加费未收取
                                                tv_shouqu.setText("去收取");
                                                holder.setText(R.id.tv_fujiafei, "附加费未收取");//附加费未收取
                                            }
                                            tv_shouqu.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (model.getIs_attach_fee() == 2) {
                                                        //已收取
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("id", model.getId());
                                                        CommonUtil.gotoActivityWithData(getActivity(), AddSurchargeActivity.class, bundle, false);
                                                    }
                                                }
                                            });
                                            //附加费
                                            holder.getView(R.id.tv_fujiafei).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (model.getIs_attach_fee() == 2) {//未收取
                                                        //已收取
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("id", model.getId());
                                                        CommonUtil.gotoActivityWithData(getActivity(), AddSurchargeActivity.class, bundle, false);
                                                    }
                                                }
                                            });
                                        }
                                    };

                                    break;
                                case 3:
                                    /**
                                     * *************************已取消**************************************
                                     * */
                                    list3 = JSON.parseArray(jsonArray.toString(), Fragment2Model1.class);
                                    mAdapter3 = new CommonAdapter<Fragment2Model1>
                                            (getActivity(), R.layout.item_fragment2_3, list3) {
                                        @Override
                                        protected void convert(ViewHolder holder, Fragment2Model1 model, int position) {
                                            //订单号
                                            holder.setText(R.id.tv_ordernum, "订单号：" + model.getSn());

                                            holder.setText(R.id.textView1, model.getNow_state_action());//状态
                                            holder.setText(R.id.textView2, "货源单号" + model.getSn());//货源单号
                                            holder.setText(R.id.textView3, model.getCreated_at() + " 发布");//发布时间
                                            holder.setText(R.id.textView4, model.getRemark());//备注
                                            holder.setText(R.id.textView5, "¥ " + model.getPrice());//订单金额
                                            holder.setText(R.id.textView6, "" + model.getPrice() + "元");//起步价
                                            holder.setText(R.id.textView7, "" + model.getPrice() + "元");//里程费
//                                            holder.setText(R.id.textView8, "" + model.get);//取消理由
                                            //费用列表
                                            LinearLayout ll_add2 = holder.getView(R.id.ll_add2);
                                            ll_add2.removeAllViews();
                                            for (int i = 0; i < model.getPrice_detail().size(); i++) {
                                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                LayoutInflater inflater = LayoutInflater.from(getActivity());
                                                View view = inflater.inflate(R.layout.item_add_fragment2_2, null, false);
                                                view.setLayoutParams(lp);

                                                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                                                TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
                                                tv_title.setText(model.getPrice_detail().get(i).getTitle());
                                                tv_money.setText(model.getPrice_detail().get(i).getPrice() + "元");
                                                ll_add2.addView(view);
                                            }
                                            //地址列表
                                            for (int i = 0; i < model.getAddr_list().size(); i++) {
                                                //起点
                                                if (i == 0) {
                                                    holder.setText(R.id.tv_time1, model.getAddr_list().get(i).getArrive_time() + " 装货");
                                                    holder.setText(R.id.tv_addr1, model.getAddr_list().get(i).getAddr());
                                                    holder.setText(R.id.tv_title1, model.getAddr_list().get(i).getAddr_detail());
                                                    holder.setText(R.id.tv_juli1, "送货路程" + CommonUtil.distanceFormat(Double.valueOf(model.getAddr_list().get(i).getMileage())));
                                                }
                                                //终点
                                                if (i == model.getAddr_list().size() - 1) {
                                                    holder.setText(R.id.tv_time2, model.getAddr_list().get(i).getArrive_time() + " 卸货");
                                                    holder.setText(R.id.tv_addr2, model.getAddr_list().get(i).getAddr());
                                                    holder.setText(R.id.tv_title2, model.getAddr_list().get(i).getAddr_detail());
                                                    holder.setText(R.id.tv_juli2, "送货路程" + CommonUtil.distanceFormat(Double.valueOf(model.getAddr_list().get(i).getMileage())));
                                                }
                                            }

                                            //客户要求
                                            FlowLayoutAdapter<String> flowLayoutAdapter1;
                                            flowLayoutAdapter1 = new FlowLayoutAdapter<String>(model.getTag()) {
                                                @Override
                                                public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
                                                    TextView tv = holder.getView(R.id.tv);
                                                    tv.setText(bean);
                                                }

                                                @Override
                                                public void onItemClick(int position, String bean) {
                                                }

                                                @Override
                                                public int getItemLayoutID(int position, String bean) {
                                                    return R.layout.item_flowlayout;
                                                }
                                            };
                                            ((FlowLayout) holder.getView(R.id.flowLayout1)).setAdapter(flowLayoutAdapter1);

                                            //货物描述
                                            FlowLayoutAdapter<String> flowLayoutAdapter2;
                                            flowLayoutAdapter2 = new FlowLayoutAdapter<String>(model.getGoods_desc()) {
                                                @Override
                                                public void bindDataToView(FlowLayoutAdapter.ViewHolder holder, int position, String bean) {
                                                    TextView tv = holder.getView(R.id.tv);
                                                    tv.setText(bean);
                                                }

                                                @Override
                                                public void onItemClick(int position, String bean) {
                                                }

                                                @Override
                                                public int getItemLayoutID(int position, String bean) {
                                                    return R.layout.item_flowlayout;
                                                }
                                            };
                                            ((FlowLayout) holder.getView(R.id.flowLayout2)).setAdapter(flowLayoutAdapter2);
                                        }
                                    };
                                    break;
                            }

                            changeUI();
                            hideProgress();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void RequestMore(String string) {
        OkHttpClientManager.getAsyn(getActivity(), URLs.Fragment2 + string, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                switch (status) {
                    case 1:
                        page1--;
                        break;
                    case 2:
                        page2--;
                        break;
                    case 3:
                        page3--;
                        break;
                }
                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(String response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>订单列表更多" + response);
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("data");
                    switch (status) {
                        case 1:
                            List<Fragment2Model1> list1_1 = new ArrayList<>();
                            list1_1 = JSON.parseArray(jsonArray.toString(), Fragment2Model1.class);
                            if (list1_1.size() == 0) {
                                page1--;
                                myToast(getString(R.string.app_nomore));
                            } else {
                                list1.addAll(list1_1);
                                mAdapter1.notifyDataSetChanged();
                            }
                            break;
                        case 2:
                            List<Fragment2Model1> list2_1 = new ArrayList<>();
                            list2_1 = JSON.parseArray(jsonArray.toString(), Fragment2Model1.class);
                            if (list2_1.size() == 0) {
                                page2--;
                                myToast(getString(R.string.app_nomore));
                            } else {
                                list2.addAll(list2_1);
                                mAdapter2.notifyDataSetChanged();
                            }
                            break;
                        case 3:
                            List<Fragment2Model1> list3_1 = new ArrayList<>();
                            list3_1 = JSON.parseArray(jsonArray.toString(), Fragment2Model1.class);
                            if (list3_1.size() == 0) {
                                page3--;
                                myToast(getString(R.string.app_nomore));
                            } else {
                                list3.addAll(list3_1);
                                mAdapter3.notifyDataSetChanged();
                            }
                            break;
                        default:
                            break;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }
        });

    }

    private void RequestDetail(String string) {
        OkHttpClientManager.getAsyn(getActivity(), URLs.OrderDetails + string, new OkHttpClientManager.ResultCallback<OrderDetailsModel>() {
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
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", lat);
                bundle.putDouble("lng", lng);
                bundle.putSerializable("OrderDetailsModel", response);
                CommonUtil.gotoActivityWithData(getActivity(), MapNavigationActivity.class, bundle, false);
            }
        });
    }

}
