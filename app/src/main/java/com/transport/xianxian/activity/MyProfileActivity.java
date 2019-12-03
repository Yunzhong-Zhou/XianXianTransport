package com.transport.xianxian.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.ChangeProfileModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.FileUtil;
import com.transport.xianxian.utils.MyChooseImages;
import com.transport.xianxian.utils.MyLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static com.transport.xianxian.utils.MyChooseImages.REQUEST_CODE_CAPTURE_CAMEIA;
import static com.transport.xianxian.utils.MyChooseImages.REQUEST_CODE_PICK_IMAGE;


/**
 * Created by fafukeji01 on 2017/5/8.
 * 我的资料
 */

public class MyProfileActivity extends BaseActivity {
    //选择图片及上传
    ArrayList<String> listFileNames = new ArrayList<>();
    ArrayList<File> listFiles= new ArrayList<>();
    ImageView imageView1;
    TextView textView1, textView2, textView3;
    EditText editText1, editText2;
    private TimeCount time;

    String phonenum = "", code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

    }

    @Override
    protected void initView() {
        imageView1 = findViewByID_My(R.id.imageView1);
        if (!localUserInfo.getUserImage().equals(""))
            Glide.with(MyProfileActivity.this)
                    .load(OkHttpClientManager.IMGHOST + localUserInfo.getUserImage())
                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                    .into(imageView1);//加载图片

        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);

    }

    @Override
    protected void initData() {
        time = new TimeCount(60000, 1000);//构造CountDownTimer对象
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView1:
                //头像
                MyChooseImages.showPhotoDialog(MyProfileActivity.this);
                break;
            case R.id.textView1:
                //上传头像
                if (listFiles.size() > 0){
                    textView1.setClickable(false);
                    showProgress(true, "正在修改，请稍候...");
                    String[] filenames = new String[]{};
                    File[] files = new File[]{};
                    for (int i = 0; i < listFiles.size(); i++) {
                        filenames = listFileNames.toArray(new String[i]);
                        files = listFiles.toArray(new File[i]);
                    }
                    this.showProgress(true, getString(R.string.app_loading1));
                    params.put("token", localUserInfo.getToken());
                    params.put("nickname", "");
                    RequestChangeProfile(filenames, files, params);//修改
                }else {
                    myToast("请选择头像上传");
                }
                break;

            case R.id.textView2:
                //获取验证码
                phonenum = editText1.getText().toString().trim();
                if (TextUtils.isEmpty(phonenum)) {
                    myToast("请输入新手机号码");
                } else {
                    textView2.setClickable(false);
                    this.showProgress(true, getString(R.string.app_sendcode_hint1));
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", localUserInfo.getToken());
                    params.put("mobile", phonenum);
                    params.put("type", "4");
                    RequestCode(params);//获取验证码
                }
                break;

            case R.id.textView3:
                //上传手机号
                if (match()) {
                    textView3.setClickable(false);
                    this.showProgress(true, "正在修改，请稍候...");
                    HashMap<String, String> params = new HashMap<>();
                    params.put("token", localUserInfo.getToken());
                    params.put("mobile", phonenum);
                    params.put("code", code);
                    RequestChangePhone(params);//修改手机号
                }
                break;
        }
    }
    private void RequestCode(Map<String, String> params) {
        OkHttpClientManager.postAsyn(this, URLs.Code, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                textView2.setClickable(true);
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                textView2.setClickable(true);
                MyLogger.i(">>>>>>>>>验证码" + response);
                time.start();//开始计时
                myToast(getString(R.string.app_sendcode_hint));

            }
        }, false);

    }
    //修改信息
    private void RequestChangeProfile(String[] fileKeys, File[] files, HashMap<String, String> params) {
        OkHttpClientManager.postAsyn(MyProfileActivity.this, URLs.ChangeProfile, fileKeys, files, params, new OkHttpClientManager.ResultCallback<ChangeProfileModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                textView1.setClickable(true);
                hideProgress();
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(ChangeProfileModel response) {
                textView1.setClickable(true);
                MyLogger.i(">>>>>>>>>修改信息" + response);
                myToast("修改成功");
               /* localUserInfo.setUserImage(response.getHead());
                //头像
                if (!response.getHead().equals(""))
                    Glide.with(MyProfileActivity.this)
                            .load(OkHttpClientManager.IMGHOST + response.getHead())
                            .centerCrop()
//                            .placeholder(R.mipmap.headimg)//加载站位图
//                            .error(R.mipmap.headimg)//加载失败
                            .into(imageView1);//加载图片
                else
                    imageView1.setImageResource(R.mipmap.headimg);*/
                hideProgress();
            }
        });
    }
    private void RequestChangePhone(Map<String, String> params) {
        OkHttpClientManager.postAsyn(this, URLs.ChangePhone, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, String info, Exception e) {
                hideProgress();
                textView3.setClickable(true);
                if (!info.equals("")) {
                    showToast(info);
                }
            }

            @Override
            public void onResponse(String response) {
                hideProgress();
                textView3.setClickable(true);
                MyLogger.i(">>>>>>>>>修改手机" + response);
                time.start();//开始计时
                myToast("修改手机号成功，请重新登录");
                //退出程序
                localUserInfo.setUserId("");
                localUserInfo.setUserName("");
                localUserInfo.setToken("");
                localUserInfo.setPhoneNumber("");
                localUserInfo.setNickname("");
                localUserInfo.setWalletaddr("");
                localUserInfo.setEmail("");
                localUserInfo.setUserImage("");
                //环信退出登录
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        CommonUtil.gotoActivityWithFinishOtherAll(MyProfileActivity.this, LoginActivity.class, true);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String error) {

                    }
                });

            }
        }, false);

    }
    private boolean match() {
        phonenum = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(phonenum)) {
            myToast("请输入新手机号码");
            return false;
        }
        code = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            myToast(getString(R.string.registered_h2));
            return false;
        }
        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle("修改头像/电话");
    }

    /**
     * *****************************************选择图片********************************************
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = null;
            String imagePath = null;
            switch (requestCode) {
                case REQUEST_CODE_CAPTURE_CAMEIA:
                    //相机
                    uri = Uri.parse("");
                    uri = Uri.fromFile(new File(MyChooseImages.imagepath));
                    imagePath = uri.getPath();
                    break;
                case REQUEST_CODE_PICK_IMAGE:
                    //相册
                    uri = data.getData();
                    //处理得到的url
                    ContentResolver cr = this.getContentResolver();
                    Cursor cursor = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        cursor = cr.query(uri, null, null, null, null, null);
                    }
                    if (cursor != null) {
                        cursor.moveToFirst();
                        imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    } else {
                        imagePath = uri.getPath();
                    }
                    break;
            }
            if (uri != null) {
                MyLogger.i(">>>>>>>>>>获取到的图片路径1：" + imagePath);
                //图片过大解决方法
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                imageView1.setImageBitmap(bitmap);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);

                listFileNames = new ArrayList<>();
                listFileNames.add("head");

                Uri uri1 = Uri.parse("");
                uri1 = Uri.fromFile(new File(imagePath));
                File file1 = new File(FileUtil.getPath(this, uri1));
                listFiles = new ArrayList<>();
                File newFile = null;
                try {
                    newFile = new Compressor(this).compressToFile(file1);
                    listFiles.add(newFile);
                    MyLogger.i(">>>>>选择图片结果>>>>>>>>>" + listFileNames.toString() + ">>>>>>" + listFiles.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                    myToast(getString(R.string.app_imgerr));
                }
            }
        }

    }

    //获取验证码倒计时
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            textView2.setText(getString(R.string.app_reacquirecode));
            textView2.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            textView2.setClickable(false);
            textView2.setText(millisUntilFinished / 1000 + getString(R.string.app_codethen));
        }
    }
}
