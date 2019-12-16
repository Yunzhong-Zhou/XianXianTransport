package com.transport.xianxian.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cy.dialog.BaseDialog;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.TakeCashModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyLogger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;

/**
 * Created by zyz on 2019-10-03.
 * 提现
 */
public class TakeCashActivity extends BaseActivity {
    TextView textView, textView1, textView2, textView3, textView4;
    EditText editText1;
    ImageView imageView1;
    TakeCashModel model;
    int i1 = 0, way = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takecash);
    }

    @Override
    protected void initView() {
        textView = findViewByID_My(R.id.textView);
        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);

        editText1 = findViewByID_My(R.id.editText1);

        imageView1 = findViewByID_My(R.id.imageView1);

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
        String string = "?token=" + localUserInfo.getToken();
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(TakeCashActivity.this, URLs.TakeCash + string, new OkHttpClientManager.ResultCallback<TakeCashModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(TakeCashModel response) {
                hideProgress();
                MyLogger.i(">>>>>>>>>提现" + response);
                model = response;
                textView3.setText("每次提现最低" + response.getLow_money() + "元");
                textView4.setText("本次可提现￥" + response.getUsable_money() + "");

                if (response.getWay_list().size() > 0) {
                    way = model.getWay_list().get(0).getType();
                    textView1.setText(model.getWay_list().get(0).getTitle());
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.linearLayout1:
                //选择到账方式
                if (model.getWay_list().size() > 0) {
                    BaseDialog dialog1 = new BaseDialog(TakeCashActivity.this);
                    dialog1.contentView(R.layout.dialog_list)
                            .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT))
                            .animType(BaseDialog.AnimInType.CENTER)
                            .canceledOnTouchOutside(true)
                            .dimAmount(0.8f)
                            .show();
                    TextView title = dialog1.findViewById(R.id.textView1);
                    title.setText("到账方式");
                    RecyclerView rv = dialog1.findViewById(R.id.recyclerView);
                    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(TakeCashActivity.this);
                    rv.setLayoutManager(mLinearLayoutManager);

                    CommonAdapter<TakeCashModel.WayListBean> adapter = new CommonAdapter<TakeCashModel.WayListBean>
                            (TakeCashActivity.this, R.layout.item_dialog_takecash, model.getWay_list()) {
                        @Override
                        protected void convert(ViewHolder holder, TakeCashModel.WayListBean model, int position) {
                            TextView tv = holder.getView(R.id.textView);
                            ImageView iv1 = holder.getView(R.id.imageView1);
                            ImageView iv2 = holder.getView(R.id.imageView2);

                            tv.setText(model.getTitle());
                            if (!model.getIcon().equals(""))
                                Glide.with(TakeCashActivity.this)
                                        .load(IMGHOST + model.getIcon())
                                        .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                                        .into(iv1);//加载图片

                            if (position == i1) {
                                tv.setTextColor(getResources().getColor(R.color.blue));
                                iv2.setImageResource(R.mipmap.ic_xuanzhong);
                            } else {
                                tv.setTextColor(getResources().getColor(R.color.black1));
                                iv2.setImageResource(R.mipmap.ic_weixuan);
                            }
                        }
                    };
                    adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                            i1 = i;
                            adapter.notifyDataSetChanged();
                            way = model.getWay_list().get(i).getType();
                            textView1.setText(model.getWay_list().get(i).getTitle());
                            textView2.setText(model.getWay_list().get(i).getSub());
                            if (!model.getWay_list().get(i).getIcon().equals(""))
                                Glide.with(TakeCashActivity.this)
                                        .load(IMGHOST + model.getWay_list().get(i).getIcon())
                                        .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                                        .into(imageView1);//加载图片

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
                }
                break;
            case R.id.textView5:
                //全部提现
                editText1.setText(model.getUsable_money());
                break;
            case R.id.textView:
                //提现
                if (!editText1.getText().toString().trim().equals("")) {
                    params.put("token", localUserInfo.getToken());//token
                    params.put("way", way + "");
                    params.put("money", editText1.getText().toString().trim());
                    RequestTakeCash(params);
                } else {
                    myToast("请输入提现金额");
                }
                break;
        }
    }

    private void RequestTakeCash(Map<String, String> params) {
        OkHttpClientManager.postAsyn(TakeCashActivity.this, URLs.TakeCash, params, new OkHttpClientManager.ResultCallback<String>() {
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
                MyLogger.i(">>>>>>>>>提现" + response);
//                myToast("确认成功");
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    myToast(jObj.getString("msg"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }, false);
    }

    @Override
    protected void updateView() {
        titleView.setTitle("提现");
    }
}
