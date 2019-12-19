package com.transport.xianxian.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.AddSurchargeModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by zyz on 2019-10-20.
 * 增加附加费
 */
public class AddSurchargeActivity extends BaseActivity {
    AddSurchargeModel model;
    EditText editText1, editText2, editText3, editText4;
    TextView textView1, textView2;
    TextView tv_add, tv_baocun, tv_queren;
    LinearLayout add_ll;

    String id = "", money1 = "", money2 = "", money3 = "", moneys = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsurcharge);
    }

    @Override
    protected void initView() {
        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);
        editText3 = findViewByID_My(R.id.editText3);
        editText4 = findViewByID_My(R.id.editText4);
        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        tv_add = findViewByID_My(R.id.tv_add);
        tv_baocun = findViewByID_My(R.id.tv_baocun);
        tv_queren = findViewByID_My(R.id.tv_queren);
        add_ll = findViewByID_My(R.id.add_ll);

        //失去焦点时触发
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    MyLogger.i(">>>>>>>>>>" + editText2.getText().toString().trim());
                    addMoney();
                }
            }
        });
        editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    MyLogger.i(">>>>>>>>>>" + editText3.getText().toString().trim());
                    addMoney();
                }
            }
        });
        editText4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    MyLogger.i(">>>>>>>>>>" + editText4.getText().toString().trim());
                    addMoney();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_add:
                //添加费用
                addView("", "");
                break;
            case R.id.tv_baocun:
                //保存
                if (match()) {
                    showProgress(true, "正在上传数据，请稍后...");
                    params.put("token", localUserInfo.getToken());//token
                    params.put("t_indent_id", id);//id
                    params.put("type", "4");//附加费
                    params.put("option", "save");//保存
                    params.put("detail", moneys);//材料消耗（json数据：种类/数量/金额） 如：[{ "material_id": "x01", "qty": 8, "money": "800"}, { "material_id": "x02", "qty": 20, "money": "2000"}]
                    RequestUpdata(params, 1);
                }
                break;
            case R.id.tv_queren:
                //发送客户确认
                if (match()) {
                    showProgress(true, "正在上传数据，请稍后...");
                    params.put("token", localUserInfo.getToken());//token
                    params.put("t_indent_id", id);//id
                    params.put("type", "4");//附加费
                    params.put("option", "send");//发送
                    params.put("detail", moneys);//材料消耗（json数据：种类/数量/金额） 如：[{ "material_id": "x01", "qty": 8, "money": "800"}, { "material_id": "x02", "qty": 20, "money": "2000"}]
                    RequestUpdata(params, 2);
                }
                break;
        }
    }

    private void RequestUpdata(Map<String, String> params, int i) {
        OkHttpClientManager.postAsyn(AddSurchargeActivity.this, URLs.OrderDetails_ZhuangHuo, params, new OkHttpClientManager.ResultCallback<String>() {
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
                MyLogger.i(">>>>>>>>>司机-确认装货/卸货/发送附加费/附加费收取确认/转单确认" + response);
//                myToast("确认成功");
                JSONObject jObj;
                try {
                    jObj = new JSONObject(response);
                    myToast(jObj.getString("msg"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                finish();
                /*if (i == 1) {
                    finish();
                }else {

                }*/

            }
        }, false);
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        showProgress(true, getString(R.string.app_loading));
        params.put("token", localUserInfo.getToken());//token
        params.put("t_indent_id", id);//id
        params.put("type", "8");//获取附加费
        Request(params);
    }

    private void Request(Map<String, String> params) {
        OkHttpClientManager.postAsyn(AddSurchargeActivity.this, URLs.OrderDetails_ZhuangHuo, params, new OkHttpClientManager.ResultCallback<AddSurchargeModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
                hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(AddSurchargeModel response) {
//                showContentPage();
                hideProgress();
                MyLogger.i(">>>>>>>>>获取附加费" + response);
                model = response;
                editText1.setText(response.getCity());
                textView1.setText(response.getRule());

                if (response.getAttach().size() >= 3) {
                    editText2.setText(response.getAttach().get(0).getMoney());
                    editText3.setText(response.getAttach().get(1).getMoney());
                    editText4.setText(response.getAttach().get(2).getMoney());
                    for (int i = 3; i < response.getAttach().size(); i++) {
                        addView(response.getAttach().get(i).getName(), response.getAttach().get(i).getMoney());
                    }
                }
                //合计
                addMoney();
            }
        });
    }

    private void addMoney() {
        double allMoney = 0;
        if (!editText2.getText().toString().trim().equals("")){
            allMoney += Double.valueOf(editText2.getText().toString().trim());
        }
        if (!editText3.getText().toString().trim().equals("")){
            allMoney += Double.valueOf(editText3.getText().toString().trim());
        }
        if (!editText4.getText().toString().trim().equals("")){
            allMoney += Double.valueOf(editText4.getText().toString().trim());
        }

        for (int i = 0; i < add_ll.getChildCount(); i++) {
            View childAt = add_ll.getChildAt(i);
            EditText editText_2 = (EditText) childAt.findViewById(R.id.editText_2);
            if (!TextUtils.isEmpty(editText_2.getText().toString())) {
                allMoney += Double.valueOf(editText_2.getText().toString().trim());
            }
        }
        textView2.setText("¥ "+allMoney);
    }

    //添加材料布局
    private void addView(String title, String money) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(AddSurchargeActivity.this);
        View view = inflater.inflate(R.layout.item_addsurcharge, null, false);
        view.setLayoutParams(lp);
        //实例化子页面的控件
        TextView textView_1 = (TextView) view.findViewById(R.id.textView_1);
        TextView editText_1 = (TextView) view.findViewById(R.id.editText_1);
        EditText editText_2 = (EditText) view.findViewById(R.id.editText_2);
        ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        if (!title.equals("")) {
//            textView_1.setText(id);
            editText_1.setText(title);
            editText_2.setText(money);
        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_1.setText("");
                editText_2.setText("");
                add_ll.removeView(view);

            }
        });

        //失去焦点时触发
        editText_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    MyLogger.i(">>>>>>>>>>" + editText_2.getText().toString().trim());
                    addMoney();
                }
            }
        });
        add_ll.addView(view);
    }

    private boolean match() {
        //附加费标准
        /*day = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(day)) {
            myToast("请输入附加费标准");
            return false;
        }*/
        //逾时等候费
        money1 = editText2.getText().toString().trim();
        /*if (TextUtils.isEmpty(money1)) {
            showToast("请输入逾时等候费");
            return false;
        }*/
        //路桥费
        money2 = editText3.getText().toString().trim();
        /*if (TextUtils.isEmpty(money2)) {
            showToast("请输入路桥费");
            return false;
        }*/
        //搬运费
        money3 = editText4.getText().toString().trim();
        /*if (TextUtils.isEmpty(money3)) {
            showToast("请输入搬运费");
            return false;
        }*/

        //材料消耗
        JSONArray moneyArray = new JSONArray();
        try {
            JSONObject object1 = new JSONObject();
            object1.put("name", "逾时等候费");
            object1.put("money", money1);
            moneyArray.put(object1);
            JSONObject object2 = new JSONObject();
            object2.put("name", "路桥费");
            object2.put("money", money2);
            moneyArray.put(object2);
            JSONObject object3 = new JSONObject();
            object3.put("name", "搬运费");
            object3.put("money", money3);
            moneyArray.put(object3);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < add_ll.getChildCount(); i++) {
            View childAt = add_ll.getChildAt(i);
            TextView textView_1 = (TextView) childAt.findViewById(R.id.textView_1);
            TextView editText_1 = (TextView) childAt.findViewById(R.id.editText_1);
            EditText editText_2 = (EditText) childAt.findViewById(R.id.editText_2);

            if (!TextUtils.isEmpty(editText_1.getText().toString())
                    && !TextUtils.isEmpty(editText_2.getText().toString())) {
                try {
                    JSONObject stoneObject = new JSONObject();
//                    stoneObject.put("id", textView_1.getText().toString());
                    stoneObject.put("name", editText_1.getText().toString());
                    stoneObject.put("money", editText_2.getText().toString().trim());
                    moneyArray.put(stoneObject);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else {
//                showToast("请完善附加费");
//                return false;
            }
        }

        moneys = moneyArray.toString();
        MyLogger.i(">>>>>" + moneys);

        if (model.isCan_update() == false){
            myToast("货主已付费，无法修改数据");
            return false;
        }

        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle("增加附加费");
    }
}
