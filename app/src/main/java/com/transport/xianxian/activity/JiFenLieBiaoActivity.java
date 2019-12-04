package com.transport.xianxian.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.adapter.JiFenLieBiao_GridViewAdapter;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.JiFenLieBiaoModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.transport.xianxian.net.OkHttpClientManager.HOST;

/**
 * Created by zyz on 2019-10-08.
 * 商品列表
 */
public class JiFenLieBiaoActivity extends BaseActivity {
    int type = 1;
    Banner banner;
    List<String> images = new ArrayList<>();

    GridView gridView;
    List<JiFenLieBiaoModel.GoodsDataBean> list = new ArrayList<>();
    JiFenLieBiao_GridViewAdapter gridViewAdapter;

    JiFenLieBiaoModel model;

    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifenliebiao);
    }

    @Override
    protected void initView() {
        setSpringViewMore(true);//不需要加载更多
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                String string = "?page=" + page//当前页号
                        + "&count=" + "10"//页面行数
                        + "&type=" + type
                        + "&token=" + localUserInfo.getToken();
                Request(string);
            }

            @Override
            public void onLoadmore() {
                page++;
                String string = "?page=" + page//当前页号
                        + "&count=" + "10"//页面行数
                        + "&type=" + type
                        + "&token=" + localUserInfo.getToken();
                RequestMore(string);
            }
        });
        banner = findViewByID_My(R.id.banner);
        gridView = findViewByID_My(R.id.gridView);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_jilu:
                //兑换记录
                CommonUtil.gotoActivity(this, DuiHuanListActivity.class, false);
                break;
        }
    }

    @Override
    protected void initData() {
        type = getIntent().getIntExtra("type", 1);
        requestServer();
    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        showProgress(true, getString(R.string.app_loading));
        page = 1;
        String string = "?page=" + page//当前页号
                + "&count=" + "10"//页面行数
                + "&type=" + type
                + "&token=" + localUserInfo.getToken();
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(this, URLs.JiFenLieBiao + string, new OkHttpClientManager.ResultCallback<JiFenLieBiaoModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(final JiFenLieBiaoModel response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>商品列表" + response);

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
                banner.setDelayTime(1500);
                //banner设置方法全部调用完毕时最后调用
                banner.start();

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        showToast("确认兑换该商品吗？", "确认", "取消",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("token", localUserInfo.getToken());
                                        params.put("goods_id", response.getBanner().get(position).getGoods_id() + "");
                                        RequestAdd(params);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                list = response.getGoods_data();
                gridViewAdapter = new JiFenLieBiao_GridViewAdapter(JiFenLieBiaoActivity.this, list);
                gridView.setAdapter(gridViewAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        showToast("确认兑换该商品吗？", "确认", "取消",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        HashMap<String, String> params = new HashMap<>();
                                        params.put("token", localUserInfo.getToken());
                                        params.put("goods_id", list.get(position).getId() + "");
                                        RequestAdd(params);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
            }
        });
    }

    private void RequestMore(String string) {
        OkHttpClientManager.getAsyn(this, URLs.JiFenLieBiao + string, new OkHttpClientManager.ResultCallback<JiFenLieBiaoModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                page--;
                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(JiFenLieBiaoModel response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>商品列表更多" + response);
                List<JiFenLieBiaoModel.GoodsDataBean> list1 = new ArrayList<>();
                list1 = response.getGoods_data();
                if (list1.size() == 0) {
                    page--;
                    myToast(getString(R.string.app_nomore));
                } else {
                    list.addAll(list1);
                    gridViewAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void RequestAdd(Map<String, String> params) {
        OkHttpClientManager.postAsyn(JiFenLieBiaoActivity.this, URLs.JiFenLieBiao_add, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>积分兑换" + response);
                myToast("兑换成功");
                requestServer();
            }
        }, false);

    }

    @Override
    protected void updateView() {
        titleView.setTitle("商品列表");
        titleView.showRightTextview("积分规则", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("url", HOST + "/api/article/detail-html?id=5a5deb216ab181888fac372299c97be1");
                CommonUtil.gotoActivityWithData(JiFenLieBiaoActivity.this, WebContentActivity.class, bundle, false);
            }
        });
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
