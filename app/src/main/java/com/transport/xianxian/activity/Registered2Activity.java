package com.transport.xianxian.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.transport.xianxian.net.OkHttpClientManager.HOST;


/**
 * Created by fafukeji01 on 2017/4/25.
 * 注册
 */

public class Registered2Activity extends BaseActivity {
    String id = "", hx_username = "";
    String identity_name = "", identity_number = "";
    private EditText editText1, editText2, editText3;
    private TextView textView2;
    private ImageView imageView1;
    boolean isgouxuan = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered2);

        setSwipeBackEnable(false); //主 activity 可以调用该方法，禁止滑动删除
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);
        editText3 = findViewByID_My(R.id.editText3);
        textView2 = findViewByID_My(R.id.textView2);
        imageView1 = findViewByID_My(R.id.imageView1);

        /*//失去焦点时触发
        editText6.setOnFocusChangeListener(new View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    MyLogger.i(">>>>>>>>>>" + editText6.getText().toString().trim());
                    if (!editText6.getText().toString().trim().equals("")) {
                        String string = "?nickname=" + editText6.getText().toString().trim();
                        RequestNickName(string);//检测昵称是否可用
                    }
                }
            }
        });*/

    }

    @Override
    protected void initData() {
//        request(captchaURL);
        id = getIntent().getStringExtra("id");
        hx_username = getIntent().getStringExtra("hx_username");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView4:
                //用户注册协议
                Bundle bundle = new Bundle();
                bundle.putString("url", HOST + "/api/article/detail-html?id=7376922d287fc0cb8960acd5d43e33e9");
                CommonUtil.gotoActivityWithData(Registered2Activity.this, WebContentActivity.class, bundle, false);
                break;
            case R.id.textView2:
                //下一步
                if (match()) {
                    textView2.setClickable(false);
                    showProgress(true, getString(R.string.registered_h14));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", localUserInfo.getToken());//token
                    params.put("identity_name", identity_name);
                    params.put("identity_number", identity_number);
                    RequestRegistered(params);//注册
                }
                break;
            case R.id.imageView1:
                //勾选协议
                isgouxuan = !isgouxuan;
                if (isgouxuan)
                    imageView1.setImageResource(R.mipmap.ic_gouxuan);
                else
                    imageView1.setImageResource(R.mipmap.ic_weigouxuan);
                break;
        }
    }

    private boolean match() {
        String name1 = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(name1)) {
            myToast("请输入姓");
            return false;
        }
        String name2 = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(name2)) {
            myToast("请输入名");
            return false;
        }
        identity_name = name1 + name2;

        identity_number = editText3.getText().toString().trim();
        if (TextUtils.isEmpty(identity_number)) {
            myToast("请输入身份证号码");
            return false;
        }
        /*if (identity_number.length() !=18){
            myToast("请输入18位身份证号码");
            return false;
        }*/
        if (!isgouxuan) {
            myToast("注册请勾选同意遵守《用户注册协议》");
            return false;
        }

        if (isIdNO(this,identity_number) == false){
            return false;
        }
        return true;
    }

    /**
     * 身份证号码验证
     */
    private boolean isIdNO(Context context, String num) {
        // 去掉所有空格
        num = num.replace(" ", "");

        Pattern idNumPattern = Pattern.compile("(\\d{17}[0-9xX])");

        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(num);

        //判断用户输入是否为身份证号
        if (idNumMatcher.matches()) {
            System.out.println("您的出生年月日是：");
            //如果是，定义正则表达式提取出身份证中的出生日期
            Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");//身份证上的前6位以及出生年月日

            //通过Pattern获得Matcher
            Matcher birthDateMather = birthDatePattern.matcher(num);

            //通过Matcher获得用户的出生年月日
            if (birthDateMather.find()) {
                String year = birthDateMather.group(1);
                String month = birthDateMather.group(2);
                String date = birthDateMather.group(3);
                if (Integer.parseInt(year) < 1900 // 如果年份是1900年之前
                        || Integer.parseInt(month) > 12 // 月份>12月
                        || Integer.parseInt(date) > 31 // 日期是>31号
                ) {
                    myToast("身份证号码不正确, 请检查");
                    return false;
                }
            }
            return true;
        } else {
            myToast("请输入正确的身份证号码");
            return false;
        }
    }

    //注册
    private void RequestRegistered(Map<String, String> params) {
        OkHttpClientManager.postAsyn(Registered2Activity.this, URLs.Registered2, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                textView2.setClickable(true);
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(final String response) {
                MyLogger.i(">>>>>>>>>注册2" + response);
                textView2.setClickable(true);

                //登录环信
                EMClient.getInstance().logout(false);
                EMClient.getInstance().login(hx_username, "123456", new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        hideProgress();
                        //保存id
                        localUserInfo.setUserId(id);
                        Bundle bundle = new Bundle();
                        bundle.putInt("isShowAd", 1);
                        CommonUtil.gotoActivityWithFinishOtherAllAndData(
                                Registered2Activity.this, MainActivity.class,
                                bundle, true);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                hideProgress();
                                MyLogger.i("环信登录失败：" + error);
                                myToast("环信登录失败：" + error);
                            }
                        });
                    }
                });
            }
        }, false);

    }

    @Override
    protected void updateView() {
        titleView.setTitle("注册");
//        titleView.hideLeftBtn();
    }

    /*//屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }*/
}
