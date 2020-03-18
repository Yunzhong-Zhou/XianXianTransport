package com.transport.xianxian.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.JiFenShangChengModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;

/**
 * Created by zyz on 2019-10-02.
 */
public class JiFenShangChengActivity extends BaseActivity {
    Banner banner;
    List<String> images = new ArrayList<>();
    JiFenShangChengModel model;

    ImageView imageView1;
    TextView textView1, textView2,
            tv_type1, tv_type2, tv_type3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifenshangcheng);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestServer();
    }

    @Override
    protected void initView() {
        banner = findViewByID_My(R.id.banner);
        imageView1 = findViewByID_My(R.id.imageView1);
        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);

        tv_type1 = findViewByID_My(R.id.tv_type1);
        tv_type2 = findViewByID_My(R.id.tv_type2);
        tv_type3 = findViewByID_My(R.id.tv_type3);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_detail:
                //积分明细
                CommonUtil.gotoActivity(this, JiFenMingXiActivity.class, false);
                break;
            case R.id.linearLayout1:
                //兑好礼
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", model.getType().get(0).getKey());
                CommonUtil.gotoActivityWithData(this, JiFenLieBiaoActivity.class, bundle1, false);
                break;
            case R.id.linearLayout2:
                //兑加油
                Bundle bundle2 = new Bundle();
                bundle2.putInt("type", model.getType().get(1).getKey());
                CommonUtil.gotoActivityWithData(this, JiFenLieBiaoActivity.class, bundle2, false);
                break;
            case R.id.linearLayout3:
                //兑话费
                Bundle bundle3 = new Bundle();
                bundle3.putInt("type", model.getType().get(2).getKey());
                CommonUtil.gotoActivityWithData(this, JiFenLieBiaoActivity.class, bundle3, false);
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
        String string = "?token=" + localUserInfo.getToken();
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(this, URLs.JiFenShangCheng + string, new OkHttpClientManager.ResultCallback<JiFenShangChengModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(final JiFenShangChengModel response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>积分商城" + response);
                model = response;
                if (!response.getHead().equals(""))
                    Glide.with(JiFenShangChengActivity.this).load(IMGHOST + response.getHead())
                            .centerCrop()
//                            .placeholder(R.mipmap.headimg)//加载站位图
//                            .error(R.mipmap.headimg)//加载失败
                            .into(imageView1);//加载图片

                textView1.setText(response.getNickname());//昵称
                textView2.setText(response.getScore() + "");//积分

                if (response.getType().size() >= 1){
                    tv_type1.setText(response.getType().get(0).getVal());
                }
                if (response.getType().size() >= 2){
                    tv_type2.setText(response.getType().get(1).getVal());
                }
                if (response.getType().size() >= 3){
                    tv_type3.setText(response.getType().get(2).getVal());
                }

                    //banner
                /*images.add("http://file02.16sucai.com/d/file/2014/0825/dcb017b51479798f6c60b7b9bd340728.jpg");
                images.add("http://file02.16sucai.com/d/file/2014/0825/dcb017b51479798f6c60b7b9bd340728.jpg");
                images.add("http://file02.16sucai.com/d/file/2014/0825/dcb017b51479798f6c60b7b9bd340728.jpg");
                images.add("http://file02.16sucai.com/d/file/2014/0825/dcb017b51479798f6c60b7b9bd340728.jpg");*/
                    images.clear();
                for (int i = 0; i < response.getBanner().size(); i++) {
                    images.add(OkHttpClientManager.IMGHOST + response.getBanner().get(i).getUrl());
                }
                //设置banner样式
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                banner.setImages(images);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.DepthPage);
                //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(3000);
                //banner设置方法全部调用完毕时最后调用
                banner.start();

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", response.getBanner().get(position).getType());
                        CommonUtil.gotoActivityWithData(JiFenShangChengActivity.this, JiFenLieBiaoActivity.class, bundle, false);
                    }
                });

            }
        });
    }

    @Override
    protected void updateView() {
        titleView.setTitle("积分商城");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //Glide 加载图片简单用法
            Glide.with(context)
                    .load(path)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .into(imageView);

            //Picasso 加载图片简单用法
//            Picasso.with(context).load(path).into(imageView);

            //用fresco加载图片简单用法，记得要写下面的createImageView方法
//            Uri uri = Uri.parse((String) path);
//            imageView.setImageURI(uri);
        }
    }
}
