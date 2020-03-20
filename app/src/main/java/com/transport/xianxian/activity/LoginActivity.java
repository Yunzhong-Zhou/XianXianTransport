package com.transport.xianxian.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cy.dialog.BaseDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.maning.updatelibrary.InstallUtils;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.LoginModel;
import com.transport.xianxian.model.UpgradeModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.CommonUtil;
import com.transport.xianxian.utils.MyLogger;
import com.transport.xianxian.utils.permission.PermissionsActivity;
import com.transport.xianxian.utils.permission.PermissionsChecker;

import java.util.Map;

import androidx.appcompat.app.AlertDialog;

import static com.transport.xianxian.net.OkHttpClientManager.HOST;


/**
 * Created by fafukeji01 on 2017/4/25.
 * 登录
 */
public class LoginActivity extends BaseActivity {
    private EditText editText1, editText2;
    private TextView textView1, textView2, textView3, textView4;
    private ImageView imageView1;
    boolean isgouxuan = false;

    private String phonenum = "", password = "";

    //更新
    UpgradeModel model_up;

    private int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //定位
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
//            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            //录音权限
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.VIBRATE

            /*Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.VIBRATE*/
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        mImmersionBar.reset().init();
        //        findViewById(R.id.headView).setPadding(0, (int) CommonUtil.getStatusBarHeight(this), 0, 0);
//        CommonUtil.setMargins(findViewByID_My(R.id.headView),0, (int) CommonUtil.getStatusBarHeight(this), 0, 0);


        setSwipeBackEnable(false); //主 activity 可以调用该方法，禁止滑动删除

        mPermissionsChecker = new PermissionsChecker(this);
    }


    @Override
    protected void initView() {

        editText1 = findViewByID_My(R.id.editText1);
        editText2 = findViewByID_My(R.id.editText2);

        textView1 = findViewByID_My(R.id.textView1);
        textView2 = findViewByID_My(R.id.textView2);
        textView3 = findViewByID_My(R.id.textView3);
        textView4 = findViewByID_My(R.id.textView4);

        imageView1 = findViewByID_My(R.id.imageView1);
    }

    @Override
    protected void initData() {
        RequestUpgrade("?app_type=" + 1
                + "&type=" + "driver");//检查更新//"driver 司机 owner货主端"

        /*byte[] mBytes = null;
        String mString = "{阿达大as家阿sdf什顿附asd件好久}";
        AES mAes = new AES();
        try {
            mBytes = mString.getBytes("UTF-8");
        } catch (Exception e) {
            Log.i("qing", "MainActivity----catch");
        }
        String enString = mAes.encrypt(mBytes);
        MyLogger.i("加密后：" + enString);
        String deString = mAes.decrypt("P9ezA6lsRKVID383Rg5mwQ==");
        MyLogger.i("解密后：" + deString);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView1:
                //忘记密码
                CommonUtil.gotoActivity(LoginActivity.this, ForgetPasswordActivity.class, false);
                break;

            case R.id.textView2:
                //确认登录
                if (match()) {
                    textView2.setClickable(false);
                    this.showProgress(true, getString(R.string.login_h7));
                    params.put("uuid", CommonUtil.getIMEI(LoginActivity.this));//IMEI
                    params.put("mobile", phonenum);
                    params.put("password", password);
//                    params.put("mobile_state_code", localUserInfo.getMobile_State_Code());
                    RequestLogin(params);//登录
                }
//                CommonUtil.gotoActivity(LoginActivity.this, MainActivity.class, true);
                break;
            case R.id.textView3:
                //注册
                Bundle bundle1 = new Bundle();
                bundle1.putString("open_id", "");
                CommonUtil.gotoActivityWithData(LoginActivity.this, RegisteredActivity.class, bundle1, false);
                break;
            case R.id.textView4:
                //协议
                Bundle bundle = new Bundle();
                bundle.putString("url", HOST + "/api/article/detail-html?id=1bdcf5a47325e1aee7bd46ab53a42d7f");
                CommonUtil.gotoActivityWithData(LoginActivity.this, WebContentActivity.class, bundle, false);
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

    //登录
    private void RequestLogin(Map<String, String> params) {
        OkHttpClientManager.postAsyn(LoginActivity.this, URLs.Login, params, new OkHttpClientManager.ResultCallback<LoginModel>() {
            @Override
            public void onError(final Request request, String info, Exception e) {
                hideProgress();
                textView2.setClickable(true);
//                myToast("密码错误，请重新输入");
                if (!info.equals("")) {
                    myToast(info);
                }
            }

            @Override
            public void onResponse(final LoginModel response) {
                MyLogger.i(">>>>>>>>>登录" + response);
                textView2.setClickable(true);
//                localUserInfo.setTime(System.currentTimeMillis() + "");
                //保存Token
                localUserInfo.setToken(response.getFresh_token());
                //保存电话号码
                localUserInfo.setPhoneNumber(response.getMobile());
                //保存是否认证
                localUserInfo.setIsVerified(response.getIs_certification()+"");//1 认证 2 未认证
                //保存环信ID
                localUserInfo.setHxid(response.getHx_username());

                if (response.getIdentity() == 1) {//登录通过
                    //环信登录-为了登录成功，先退出登录
                    EMClient.getInstance().logout(false);
                    EMClient.getInstance().login(response.getHx_username(), "123456", new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            hideProgress();
                            //保存id
                            localUserInfo.setUserId(response.getId());
                            CommonUtil.gotoActivity(LoginActivity.this, MainActivity.class, true);
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                        }

                        @Override
                        public void onError(int code, String error) {
                            runOnUiThread(new Runnable() {
                                public void run() {
//                                    hideProgress();
                                    MyLogger.i("环信登录失败：" + error);
//                                    myToast("环信登录失败：" + error);
                                    //环信注册
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                EMClient.getInstance().createAccount(response.getHx_username(), "123456");
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //登录环信
                                                        EMClient.getInstance().login(response.getHx_username(), "123456", new EMCallBack() {
                                                            @Override
                                                            public void onSuccess() {
                                                                hideProgress();
                                                                //保存id
                                                                localUserInfo.setUserId(response.getId());
                                                                CommonUtil.gotoActivity(LoginActivity.this, MainActivity.class, true);
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
                                                });
                                            } catch (final HyphenateException e) {
                                                e.printStackTrace();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        hideProgress();
                                                        /**
                                                         * 关于错误码可以参考官方api详细说明
                                                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                                                         */
                                                        int errorCode = e.getErrorCode();
                                                        String message = e.getMessage();
                                                        Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                                                        switch (errorCode) {
                                                            // 网络错误
                                                            case EMError.NETWORK_ERROR:
                                                                MyLogger.i("网络错误 code: " + errorCode + ", message:" + message);
                                                                break;
                                                            // 用户已存在
                                                            case EMError.USER_ALREADY_EXIST:
                                                                MyLogger.i("用户已存在 code: " + errorCode + ", message:" + message);
                                                                break;
                                                            // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                                            case EMError.USER_ILLEGAL_ARGUMENT:
                                                                MyLogger.i("参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message);
                                                                break;
                                                            // 服务器未知错误
                                                            case EMError.SERVER_UNKNOWN_ERROR:
                                                                MyLogger.i("服务器未知错误 code: " + errorCode + ", message:" + message);
                                                                break;
                                                            case EMError.USER_REG_FAILED:
                                                                MyLogger.i("账户注册失败 code: " + errorCode + ", message:" + message);
                                                                break;
                                                            default:
                                                                MyLogger.i("ml_sign_up_failed code: " + errorCode + ", message:" + message);
                                                                break;
                                                        }
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            });
                        }
                    });
                } else {//未完善资料
                    hideProgress();
                    showToast("该账户尚未完善资料，前往完善资料", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("id",response.getId());
                            bundle.putString("hx_username",response.getHx_username());
                            CommonUtil.gotoActivityWithData(LoginActivity.this, Registered2Activity.class, bundle,false);
                        }
                    });
                }
            }
        }, false);

    }

    private boolean match() {
        phonenum = editText1.getText().toString().trim();
        if (TextUtils.isEmpty(phonenum)) {
            myToast(getString(R.string.login_h1));
            return false;
        }
        password = editText2.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            myToast(getString(R.string.login_h2));
            return false;
        }

        if (!isgouxuan) {
            myToast("登录请勾选同意遵守《用户登录协议和用户隐私协议》");
            return false;
        }

        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle("鲜鲜拉");
        titleView.hideLeftBtn();
    }

    /*//屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }*/

    private void RequestUpgrade(String string) {
        OkHttpClientManager.getAsyn(LoginActivity.this, URLs.Upgrade + string, new OkHttpClientManager.ResultCallback<UpgradeModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                hideProgress();
            }

            @Override
            public void onResponse(UpgradeModel response) {
                MyLogger.i(">>>>>>>>>更新" + response);
//                hideProgress();
                model_up = response;
                if (Integer.valueOf(CommonUtil.getVersionCode(LoginActivity.this)) < Integer.valueOf(response.getVersion_code())) {
//                    handler1.sendEmptyMessage(0);
                    showUpdateDialog();
                } else {
//                    showToast("已经是最新版，无需更新");
                }
            }
        });
    }

    //显示是否要更新的对话框
    private void showUpdateDialog() {
        dialog.contentView(R.layout.dialog_upgrade)
                .layoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT))
                .animType(BaseDialog.AnimInType.CENTER)
                .canceledOnTouchOutside(true)
                .dimAmount(0.8f)
                .show();
        TextView textView1 = dialog.findViewById(R.id.textView1);
        TextView textView2 = dialog.findViewById(R.id.textView2);
        TextView textView3 = dialog.findViewById(R.id.textView3);
        TextView textView4 = dialog.findViewById(R.id.textView4);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                        /*Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(model_up.getUrl());
                        intent.setData(content_url);
                        startActivity(intent);*/
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(false);//点击外部不消失，返回键没用
//        progressDialog.setCanceledOnTouchOutside(false);//点击外部不消失，返回键有用
                    progressDialog.setTitle(getString(R.string.update_hint3));
                    progressDialog.setMessage(getString(R.string.update_hint4));
                    progressDialog.setProgress(0);
                    progressDialog.show();

                    //下载APK
                    InstallUtils.with(LoginActivity.this)
                            //必须-下载地址
                            .setApkUrl(model_up.getUrl())
                            //非必须-下载保存的文件的完整路径+/name.apk，使用自定义路径需要获取读写权限
//                                    .setApkPath(Constants.APK_SAVE_PATH)
                            //非必须-下载回调
                            .setCallBack(new InstallUtils.DownloadCallBack() {
                                @Override
                                public void onStart() {
                                    //下载开始
                                }

                                @Override
                                public void onComplete(final String path) {
                                    progressDialog.cancel();
                                    //下载完成
                                    //先判断有没有安装权限---适配8.0
                                    //如果不想用封装好的，可以自己去实现8.0适配
                                    InstallUtils.checkInstallPermission(LoginActivity.this, new InstallUtils.InstallPermissionCallBack() {
                                        @Override
                                        public void onGranted() {
                                            //去安装APK
                                            //一加手机8.0碰到了安装解析失败问题请添加下面判断
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                //先获取是否有安装未知来源应用的权限
                                                boolean haveInstallPermission = LoginActivity.this.getPackageManager().canRequestPackageInstalls();
                                                if (!haveInstallPermission) {
                                                    //跳转设置开启允许安装
                                                    Uri packageURI = Uri.parse("package:" + LoginActivity.this.getPackageName());
                                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                                                    startActivityForResult(intent, 1000);
                                                    return;
                                                }
                                            }
                                            InstallUtils.installAPK(LoginActivity.this, path, new InstallUtils.InstallCallBack() {
                                                @Override
                                                public void onSuccess() {
                                                    myToast(getString(R.string.update_hint5));
                                                }

                                                @Override
                                                public void onFail(Exception e) {
                                                    myToast(getString(R.string.update_hint6) + e.toString());
                                                }
                                            });
                                        }

                                        @Override
                                        public void onDenied() {
                                            //弹出弹框提醒用户
                                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                                                    .setTitle(getString(R.string.update_hint7))
                                                    .setMessage(getString(R.string.update_hint8))
                                                    .setNegativeButton(getString(R.string.app_cancel), null)
                                                    .setPositiveButton(getString(R.string.update_hint9), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //打开设置页面
                                                            InstallUtils.openInstallPermissionSetting(LoginActivity.this, new InstallUtils.InstallPermissionCallBack() {
                                                                @Override
                                                                public void onGranted() {
                                                                    //去安装APK
                                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                        //先获取是否有安装未知来源应用的权限
                                                                        boolean haveInstallPermission = LoginActivity.this.getPackageManager().canRequestPackageInstalls();
                                                                        if (!haveInstallPermission) {
                                                                            //跳转设置开启允许安装
                                                                            Uri packageURI = Uri.parse("package:" + LoginActivity.this.getPackageName());
                                                                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                                                                            startActivityForResult(intent, 1000);
                                                                            return;
                                                                        }
                                                                    }
                                                                    InstallUtils.installAPK(LoginActivity.this, path, new InstallUtils.InstallCallBack() {
                                                                        @Override
                                                                        public void onSuccess() {
                                                                            myToast(getString(R.string.update_hint5));
                                                                        }

                                                                        @Override
                                                                        public void onFail(Exception e) {
                                                                            myToast(getString(R.string.update_hint6) + e.toString());
                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onDenied() {
                                                                    //还是不允许咋搞？
                                                                    finish();
//                                                                            Toast.makeText(MainActivity.this, "不允许安装咋搞？强制更新就退出应用程序吧！", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .create();
                                            alertDialog.show();
                                        }
                                    });

                                }

                                @Override
                                public void onLoading(long total, long current) {
                                    //下载中
                                    progressDialog.setMax((int) total);
                                    progressDialog.setProgress((int) current);
                                }

                                @Override
                                public void onFail(Exception e) {
                                    //下载失败
                                }

                                @Override
                                public void cancle() {
                                    //下载取消
                                }
                            })
                            //开始下载
                            .startDownload();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.update_hint1),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
