package com.transport.xianxian.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.WalletModel;
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
 * Created by zyz on 2019-10-02.
 * 钱包
 */
public class WalletActivity extends BaseActivity {
    ImageView imageView1;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;

    int page = 1;
    private RecyclerView recyclerView;
    List<WalletModel.TmoneyDataBean> list = new ArrayList<>();
    CommonAdapter<WalletModel.TmoneyDataBean> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestServer();
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
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(WalletActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);
        textView5 = findViewByID_My(R.id.textView5);
        textView6 = findViewByID_My(R.id.textView6);
        imageView1 = findViewByID_My(R.id.imageView1);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.txt_tixian:
                //提现
                CommonUtil.gotoActivity(WalletActivity.this, TakeCashActivity.class);
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
        String string = "?page=" + page//当前页号
                + "&count=" + "10"//页面行数
                + "&token=" + localUserInfo.getToken();
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(WalletActivity.this, URLs.Wallet + string, new OkHttpClientManager.ResultCallback<WalletModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(WalletModel response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>钱包" + response);
                textView1.setText(response.getNickname());//昵称
                textView2.setText("¥ "+response.getMoney());//余额
                textView3.setText("¥ "+response.getWait_money());//未完成收入
                textView4.setText("¥ "+response.getFrozen_money());//冻结
                textView5.setText("¥ "+response.getTotal_money());//总收入
                textView6.setText("¥ "+response.getWithdrawal_money());//总提现
                if (!response.getHead().equals(""))
                    Glide.with(WalletActivity.this)
                            .load(IMGHOST + response.getHead())
                            .centerCrop()
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                            .into(imageView1);//加载图片
                list = response.getTmoney_data();
                if (list.size() > 0) {
                    showContentPage();
                    mAdapter = new CommonAdapter<WalletModel.TmoneyDataBean>
                            (WalletActivity.this, R.layout.item_wallet, list) {
                        @Override
                        protected void convert(ViewHolder holder, WalletModel.TmoneyDataBean model, int position) {
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
        OkHttpClientManager.getAsyn(WalletActivity.this, URLs.Wallet + string, new OkHttpClientManager.ResultCallback<WalletModel>() {
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
            public void onResponse(WalletModel response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>钱包更多" + response);

                List<WalletModel.TmoneyDataBean> list1 = new ArrayList<>();
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
        titleView.setTitle("钱包");
    }
}
