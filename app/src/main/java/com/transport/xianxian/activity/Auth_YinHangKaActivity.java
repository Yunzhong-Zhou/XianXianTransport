package com.transport.xianxian.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.Auth_YinHangKaModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyLogger;

import java.util.HashMap;

/**
 * Created by zyz on 2019-10-01.
 * 银行卡认证
 */
public class Auth_YinHangKaActivity extends BaseActivity {
    EditText editText1, editText2;
    String bank_card = "", bank_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_yinhangka);
    }

    @Override
    protected void initView() {
        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.textView1:
                //提交
                if (match()) {
                    showProgress(false, getString(R.string.app_loading1));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("type", "post_bank");
                    params.put("token", localUserInfo.getToken());
                    params.put("bank_card", bank_card);
                    params.put("bank_name", bank_name);
                    RequestUpData(params);//
                }
                break;
        }
    }

    private void RequestUpData(HashMap<String, String> params) {
        OkHttpClientManager.postAsyn(this, URLs.Auth_CheZhu, params,
                new OkHttpClientManager.ResultCallback<String>() {
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
                        MyLogger.i(">>>>>>>>>上传银行卡" + response);
                        myToast("上传成功");
                        finish();
                    }
                }, false);

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
        String string = "?token=" + localUserInfo.getToken()
                + "&type=" + "get_bank";
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(this, URLs.Auth_CheZhu + string,
                new OkHttpClientManager.ResultCallback<Auth_YinHangKaModel>() {
                    @Override
                    public void onError(Request request, String info, Exception e) {
                        hideProgress();
                        if (!info.equals("")) {
                            myToast(info);
                        }
                    }

                    @Override
                    public void onResponse(final Auth_YinHangKaModel response) {
                        hideProgress();
                        MyLogger.i(">>>>>>>>>车主认证-银行卡" + response);
                        editText1.setText(response.getBank_card());
                        editText2.setText(response.getBank_name());
                    }
                });
    }

    private boolean match() {
        bank_card = editText1.getText().toString().trim().replaceAll(" ","");
        if (TextUtils.isEmpty(bank_card)) {
            myToast("请输入本人银行卡号");
            return false;
        }
        if (bank_card.length() != 16 || bank_card.length() != 19) {
            myToast("请输入正确银行卡号");
            return false;
        }
        bank_name = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(bank_name)) {
            myToast("请输入开户行名称");
            return false;
        }
        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle("银行卡");
    }
}
