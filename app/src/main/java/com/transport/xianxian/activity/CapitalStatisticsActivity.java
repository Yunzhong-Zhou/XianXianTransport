package com.transport.xianxian.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.CapitalStatisticsModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;

/**
 * Created by zyz on 2019-10-03.
 * 资金统计
 */
public class CapitalStatisticsActivity extends BaseActivity {
    ImageView imageView1;
    TextView textView1, textView2, textView3, textView4, textView5, tv_detail;
    boolean isShowList = true;
    int page = 1;
    private RecyclerView recyclerView;
    List<CapitalStatisticsModel.TmoneyDataBean> list = new ArrayList<>();
    CommonAdapter<CapitalStatisticsModel.TmoneyDataBean> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitalstatistics);
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
                        + "&token=" + localUserInfo.getToken();
                Request(string);
            }

            @Override
            public void onLoadmore() {
                page++;
                String string = "?page=" + page//当前页号
                        + "&count=" + "10"//页面行数
                        + "&token=" + localUserInfo.getToken();
                RequestMore(string);
            }
        });
        recyclerView = findViewByID_My(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(CapitalStatisticsActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);
        tv_detail = findViewByID_My(R.id.tv_detail);
        imageView1 = findViewByID_My(R.id.imageView1);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_tixian:
                //提现
//                Bundle bundle = new Bundle();
//                bundle.putString("money",);
                CommonUtil.gotoActivity(CapitalStatisticsActivity.this, TakeCashActivity.class);
                break;
            case R.id.tv_detail:
                //明细
                Drawable drawable1 = getResources().getDrawable(R.mipmap.ic_down_black);//选中-蓝色
                Drawable drawable2 = getResources().getDrawable(R.mipmap.ic_next_black);//未选-灰色
                drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                isShowList = !isShowList;
                if (isShowList) {
                    tv_detail.setCompoundDrawables(null, null, drawable1, null);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    tv_detail.setCompoundDrawables(null, null, drawable2, null);
                    recyclerView.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        requestServer();
    }

    @Override
    public void requestServer() {
        super.requestServer();
//        this.showLoadingPage();
        showProgress(true, getString(R.string.app_loading));
        String string = "?page=" + page//当前页号
                + "&count=" + "10"//页面行数
                + "&token=" + localUserInfo.getToken();
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(CapitalStatisticsActivity.this, URLs.CapitalStatistics + string, new OkHttpClientManager.ResultCallback<CapitalStatisticsModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(CapitalStatisticsModel response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>资金统计" + response);
                textView1.setText(response.getNickname());//昵称
                textView2.setText("¥ " + response.getMoney());//账户余额
                textView3.setText("¥ " + response.getWait_money());//未完成收入
                textView4.setText("¥ " + response.getFrozen_money());//冻结
                textView5.setText("¥ " + response.getToday_money());//今日收入
                if (!response.getHead().equals(""))
                    Glide.with(CapitalStatisticsActivity.this)
                            .load(IMGHOST + response.getHead())
                            .centerCrop()
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                            .into(imageView1);//加载图片
                list = response.getTmoney_data();
                if (list.size() > 0) {
                    showContentPage();
                    mAdapter = new CommonAdapter<CapitalStatisticsModel.TmoneyDataBean>
                            (CapitalStatisticsActivity.this, R.layout.item_wallet, list) {
                        @Override
                        protected void convert(ViewHolder holder, CapitalStatisticsModel.TmoneyDataBean model, int position) {
                            holder.setText(R.id.textView1, model.getTitle());
                            holder.setText(R.id.textView2, model.getCreated_at());
                            TextView tv_money = holder.getView(R.id.textView3);
                            if (model.getOut_in() == 1) {
                                tv_money.setTextColor(getResources().getColor(R.color.green));
                                tv_money.setText("+" + model.getMoney());
                            } else {
                                tv_money.setTextColor(getResources().getColor(R.color.red));
                                tv_money.setText("-" + model.getMoney());
                            }
                            holder.setText(R.id.textView4, model.getSn());

                        }
                    };
                    recyclerView.setAdapter(mAdapter);
                } else {
                    showEmptyPage();
                }
            }
        });
    }

    private void RequestMore(String string) {
        OkHttpClientManager.getAsyn(CapitalStatisticsActivity.this, URLs.CapitalStatistics + string, new OkHttpClientManager.ResultCallback<CapitalStatisticsModel>() {
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
            public void onResponse(CapitalStatisticsModel response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>充值记录列表更多" + response);

                List<CapitalStatisticsModel.TmoneyDataBean> list1 = new ArrayList<>();
                list1 = response.getTmoney_data();
                if (list1.size() == 0) {
                    page--;
                    myToast(getString(R.string.app_nomore));
                } else {
                    list.addAll(list1);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });

    }

    @Override
    protected void updateView() {
        titleView.setTitle("资金统计");
    }
}
