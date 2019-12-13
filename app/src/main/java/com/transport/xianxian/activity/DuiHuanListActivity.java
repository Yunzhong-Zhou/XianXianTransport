package com.transport.xianxian.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.DuiHuanListModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyLogger;
import com.zhy.adapter.recyclerview.CommonAdapter;
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
 * Created by zyz on 2019-10-08.
 * 兑换记录
 */
public class DuiHuanListActivity extends BaseActivity {
    int page = 1;
    private RecyclerView recyclerView;
    List<DuiHuanListModel> list = new ArrayList<>();
    CommonAdapter<DuiHuanListModel> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jifenmingxi);
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
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
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
        OkHttpClientManager.getAsyn(this, URLs.DuiHuanJiLu + string, new OkHttpClientManager.ResultCallback<String>() {
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
                hideProgress();
                MyLogger.i(">>>>>>>>>兑换记录" + response);
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("data");
                    list = JSON.parseArray(jsonArray.toString(), DuiHuanListModel.class);
                    if (list.size() > 0) {
                        mAdapter = new CommonAdapter<DuiHuanListModel>
                                (DuiHuanListActivity.this, R.layout.item_duihuanlist, list) {
                            @Override
                            protected void convert(ViewHolder holder, DuiHuanListModel model, int position) {
                                holder.setText(R.id.textView1, model.getTitle());
                                holder.setText(R.id.textView2, model.getSub_title());
                                holder.setText(R.id.textView3, "兑换时间：" + model.getCreated_at());

                                holder.setText(R.id.textView4, "-" + model.getScore() + "积分");
                                ImageView imageView1 = holder.getView(R.id.imageView1);
                                if (model.getImage() != null && !model.getImage().equals(""))
                                    Glide.with(DuiHuanListActivity.this)
                                            .load(IMGHOST + model.getImage())
                                            .centerCrop()
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                                            .into(imageView1);//加载图片

                                holder.setText(R.id.textView5, model.getStatus_text());//状态
                            }
                        };
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        showEmptyPage();//空数据
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }

    private void RequestMore(String string) {
        OkHttpClientManager.getAsyn(this, URLs.DuiHuanJiLu + string, new OkHttpClientManager.ResultCallback<String>() {
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
            public void onResponse(String response) {
                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>兑换记录更多" + response);
                JSONObject jObj;
                List<DuiHuanListModel> list1 = new ArrayList<>();
                try {
                    jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("data");
                    list1 = JSON.parseArray(jsonArray.toString(), DuiHuanListModel.class);
                    if (list1.size() == 0) {
                        page--;
                        myToast(getString(R.string.app_nomore));
                    } else {
                        list.addAll(list1);
                        mAdapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void updateView() {
        titleView.setTitle("兑换记录");
    }
}
