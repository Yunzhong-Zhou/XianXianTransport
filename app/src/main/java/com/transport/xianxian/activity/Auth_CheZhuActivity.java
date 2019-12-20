package com.transport.xianxian.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.Auth_CheZhuModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;

/**
 * Created by zyz on 2019-10-01.
 * 车主认证
 */
public class Auth_CheZhuActivity extends BaseActivity {
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_chezhu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestServer();
    }

    @Override
    protected void initView() {
        imageView1 = findViewByID_My(R.id.imageView1);
        imageView2 = findViewByID_My(R.id.imageView2);
        imageView3 = findViewByID_My(R.id.imageView3);
        imageView4 = findViewByID_My(R.id.imageView4);
        imageView5 = findViewByID_My(R.id.imageView5);
        imageView6 = findViewByID_My(R.id.imageView6);
        imageView7 = findViewByID_My(R.id.imageView7);
        imageView8 = findViewByID_My(R.id.imageView8);
        imageView9 = findViewByID_My(R.id.imageView9);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.linearLayout1:
                //身份证认证
                CommonUtil.gotoActivity(this, Auth_ShenFenZhengActivity.class, false);
                break;
            case R.id.linearLayout2:
                //驾驶证及行驶证认证
                CommonUtil.gotoActivity(this, Auth_JiaShiZhengActivity.class, false);
                break;
            case R.id.linearLayout3:
                //人脸认证
                CommonUtil.gotoActivity(this, Auth_RenLianActivity.class, false);
                break;
            case R.id.linearLayout4:
                //银行卡绑定
                CommonUtil.gotoActivity(this, Auth_YinHangKaActivity.class, false);
                break;
            case R.id.linearLayout5:
                //车辆保险
                CommonUtil.gotoActivity(this, Auth_CheLiangBaoXianActivity.class, false);
                break;
            case R.id.linearLayout6:
                //从业资格证
                CommonUtil.gotoActivity(this, Auth_ChongYeZhiGeActivity.class, false);
                break;
            case R.id.linearLayout7:
                //挂靠公司告知书
                CommonUtil.gotoActivity(this, Auth_GuaKaoGongSiActivity.class, false);
                break;
            case R.id.linearLayout8:
                //车牌号码
                CommonUtil.gotoActivity(this, Auth_ChePaiHaoMaActivity.class, false);
                break;
            case R.id.linearLayout9:
                //车辆照片
                CommonUtil.gotoActivity(this, Auth_CheLiangZhaoPianActivity.class, false);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();

        showProgress(true, getString(R.string.app_loading));
        String string = "?token=" + localUserInfo.getToken()
                + "&type=" + "get_list";
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(this, URLs.Auth_CheZhu + string,
                new OkHttpClientManager.ResultCallback<Auth_CheZhuModel>() {
                    @Override
                    public void onError(Request request, String info, Exception e) {
                        hideProgress();
                        if (!info.equals("")) {
                            myToast(info);
                        }
                    }

                    @Override
                    public void onResponse(final Auth_CheZhuModel response) {
                        hideProgress();
                        MyLogger.i(">>>>>>>>>车主认证" + response);
                        //保存是否认证
                        localUserInfo.setIsVerified(response.getIs_certification()+"");//1 认证 2 未认证
                        //身份证
                        switch (response.getIs_identity()) {
                            case 1:
                                imageView1.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView1.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView1.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView1.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //驾驶证
                        switch (response.getIs_license()) {
                            case 1:
                                imageView2.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView2.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView2.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView2.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //人脸
                        switch (response.getIs_face()) {
                            case 1:
                                imageView3.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView3.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView3.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView3.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //银行卡
                        switch (response.getIs_bank()) {
                            case 1:
                                imageView4.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView4.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView4.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView4.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //车辆保险
                        switch (response.getIs_car_insurance()) {
                            case 1:
                                imageView5.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView5.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView5.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView5.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //从业资格
                        switch (response.getIs_nvq()) {
                            case 1:
                                imageView6.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView6.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView6.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView6.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //挂靠公司
                        switch (response.getIs_depend_on()) {
                            case 1:
                                imageView7.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView7.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView7.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView7.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //车牌号码
                        switch (response.getIs_car_number()) {
                            case 1:
                                imageView8.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView8.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView8.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView8.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                        //车辆照片
                        switch (response.getIs_car_image()) {
                            case 1:
                                imageView9.setImageResource(R.color.transparent);
                                break;
                            case 2:
                                imageView9.setImageResource(R.mipmap.ic_auth_shenhezhong);
                                break;
                            case 3:
                                imageView9.setImageResource(R.mipmap.ic_auth_tongguo);
                                break;
                            case 4:
                                imageView9.setImageResource(R.mipmap.ic_auth_weitongguo);
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    @Override
    protected void updateView() {
        titleView.setTitle("车主认证");
    }
}
