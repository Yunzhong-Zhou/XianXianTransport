package com.transport.xianxian;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import com.hjq.toast.ToastUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.transport.xianxian.utils.huanxin.HxEaseuiHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by zyz on 2018/1/18.
 */

public class MyApplication extends Application {
    // 上下文菜单
    private Context mContext;

    // 记录是否已经初始化
    private boolean isInit = false;

    private static MyApplication myApplication;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public final void onCreate() {
        super.onCreate();
        mContext = this;
        myApplication = this;


        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //腾讯bugly 异常上报初始化-建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(context, "d0972bab05", false, strategy);


        //toast初始化
        ToastUtils.init(this);


        //极光推送
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
//        JPushInterface.setAlias(this, 0, LocalUserInfo.getInstance(this).getUserId());//设置别名


        // 初始化环信SDK
        HxEaseuiHelper.getInstance().init(this.getApplicationContext());


//        new ScreenAdaptation(this,828,1792).register();
//        new ScreenAdaptation(this,750,1334).register();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);//方法数超过64k
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
