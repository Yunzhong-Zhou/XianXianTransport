package com.transport.xianxian.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.liaoinstan.springview.widget.SpringView;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.ScoreDetailModel;
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

/**
 * Created by zyz on 2019-10-03.
 * 评分详情
 */
public class ScoreDetailActivity extends BaseActivity {
    boolean isShowList = true;
    int page = 1;
    private RecyclerView recyclerView;
    List<ScoreDetailModel> list = new ArrayList<>();
    CommonAdapter<ScoreDetailModel> mAdapter;

    TextView tv_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoredetail);
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
        tv_detail = findViewByID_My(R.id.tv_detail);

        recyclerView = findViewByID_My(R.id.recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(ScoreDetailActivity.this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override
    protected void initData() {
        requestServer();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
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
        }
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
        OkHttpClientManager.getAsyn(ScoreDetailActivity.this, URLs.ScoreDetail + string, new OkHttpClientManager.ResultCallback<String>() {
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
                MyLogger.i(">>>>>>>>>评分" + response);
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("data");
                    list = JSON.parseArray(jsonArray.toString(), ScoreDetailModel.class);
                    if (list.size() > 0) {
                        mAdapter = new CommonAdapter<ScoreDetailModel>
                                (ScoreDetailActivity.this, R.layout.item_scoredetail, list) {
                            @Override
                            protected void convert(ViewHolder holder, ScoreDetailModel model, int position) {
                                holder.setText(R.id.tv1, model.getTitle());
                                holder.setText(R.id.tv2, model.getCreated_at());
                                TextView tv3 = holder.getView(R.id.tv3);
                                if (model.getOut_in() == 1) {
//                                    tv3.setTextColor(getResources().getColor(R.color.green));
                                    tv3.setText("+" + model.getScore()+"分");
                                } else {
//                                    tv3.setTextColor(getResources().getColor(R.color.red));
                                    tv3.setText("-" + model.getScore()+"分");
                                }
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
        OkHttpClientManager.getAsyn(this, URLs.ScoreDetail + string, new OkHttpClientManager.ResultCallback<String>() {
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
                MyLogger.i(">>>>>>>>>评分更多" + response);
                JSONObject jObj;
                List<ScoreDetailModel> list1 = new ArrayList<>();
                try {
                    jObj = new JSONObject(response);
                    JSONArray jsonArray = jObj.getJSONArray("data");
                    list1 = JSON.parseArray(jsonArray.toString(), ScoreDetailModel.class);
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
        titleView.setTitle("评分详情");
    }
}
