package com.transport.xianxian.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.model.GuideModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;

/**
 * Created by zyz on 2016/9/12.
 * Email：1125213018@qq.com
 * description：引导页面开始
 */
public class GuideActivity extends Activity {

    private ViewPager viewPager;
    /**
     * 装分页显示的view的数组
     */
    private ArrayList<View> pageViews;
    private ImageView imageView;

    /**
     * 将小圆点的图片用数组表示
     */
    private ImageView[] imageViews;


    //包裹小圆点的LinearLayout
    private ViewGroup viewPoints;

    boolean isfirst = true;

    /**
     * Called when the activity is first created.
     */
    LayoutInflater inflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES,
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);*/

        /*//设置为英文
        Resources resources=getResources();
        // 获取应用内语言
        final Configuration configuration=resources.getConfiguration();
//        Locale locale=configuration.locale;
        final DisplayMetrics displayMetrics=resources.getDisplayMetrics();
        LocalUserInfo.getInstance(GuideActivity.this).setLanguage_Type("1");
        configuration.locale = new Locale("en","US");
        resources.updateConfiguration(configuration,displayMetrics);*/


        //将要分页显示的View装入数组中
        inflater = getLayoutInflater();
        //显示滑动图片的视图
        setContentView(R.layout.activity_guide);

        //实例化小圆点的linearLayout和viewpager
        viewPoints = (ViewGroup) findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.guidePages);

        //跳过
        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isfirst) {
                    isfirst = !isfirst;
                    //设置已经引导
                    setGuided();
                    //跳转
                    Intent mIntent = new Intent();
                    mIntent.setClass(GuideActivity.this, LoginActivity.class);
//                    mIntent.setClass(GuideActivity.this, MainActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();
                }
            }
        });

        Map<String, String> params = new HashMap<>();
        params.put("apkname","com.transport.xianxian");//司机
        Request(params);

    }

    private void Request(Map<String, String> params) {
        OkHttpClientManager.postAsyn(this, URLs.Guide, params, new OkHttpClientManager.ResultCallback<GuideModel>() {
            @Override
            public void onError(Request request, String info, Exception e) {
//                showErrorPage();
               /* hideProgress();
                if (!info.equals("")) {
                    myToast(info);
                }*/
            }

            @Override
            public void onResponse(GuideModel response) {
                MyLogger.i(">>>>>>>引导页");
                //全面屏 1080*2160 mipmap-h642dp-port-xxhdpi
//        int[] images = {R.mipmap.background1, R.mipmap.background2, R.mipmap.background3};
//                int[] images = {};

                pageViews = new ArrayList<View>();
                for (int i = 0; i < response.getApp_banner_list().size(); i++) {
                    pageViews.add(inflater.inflate(R.layout.viewpager_page, null));
                }
                for (int i = 0; i < pageViews.size(); i++) {
                    ImageView imageView = (ImageView) pageViews.get(i).findViewById(R.id.imageView);
//                    imageView.setImageResource(images[i]);
                    Glide.with(GuideActivity.this)
                            .load(IMGHOST + response.getApp_banner_list().get(i).getImage())
                            .fitCenter()
                            .into(imageView);//加载图片
                }
                //创建imageviews数组，大小是要显示的图片的数量
                imageViews = new ImageView[pageViews.size()];

                //添加小圆点的图片
                for (int i = 0; i < pageViews.size(); i++) {
                    imageView = new ImageView(GuideActivity.this);
                    //设置小圆点imageview的参数
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(60, 60));//创建一个宽高均为20 的布局
                    imageView.setPadding(10, 10, 10, 10);
                    //将小圆点layout添加到数组中
                    imageViews[i] = imageView;
                    //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
                    if (i == 0) {
                        imageViews[i].setImageResource(R.drawable.yuanxing_lanse);
                    } else {
                        imageViews[i].setImageResource(R.drawable.yuanxing_huise);
                    }

                    //将imageviews添加到小圆点视图组
                    viewPoints.addView(imageViews[i]);
                }


                //设置viewpager的适配器和监听事件
                viewPager.setAdapter(new GuidePageAdapter());
                viewPager.setOnPageChangeListener(new GuidePageChangeListener());

            }
        });
    }

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    private void setGuided() {
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_GUIDE_ACTIVITY, "false");
        editor.commit();
    }

    class GuidePageAdapter extends PagerAdapter {

        //销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager) v).removeView(pageViews.get(position));

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        //获取当前窗体界面数
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pageViews.size();
        }

        //初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            // TODO Auto-generated method stub
            ((ViewPager) v).addView(pageViews.get(position));
            return pageViews.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            // TODO Auto-generated method stub
            return v == arg1;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        private int pos = 0;
        private int maxPos = pageViews.size() - 1;//最后一页
        private int currentPageScrollStatus;
        private Toast toast;

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            //记录page滑动状态，如果滑动了state就是1
            currentPageScrollStatus = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO Auto-generated method stub
            if (pos == 0) {
                //如果offsetPixels是0页面也被滑动了，代表在第一页还要往左划
                if (positionOffsetPixels == 0 && currentPageScrollStatus == 1) {
                    /*if (toast == null || !toast.getView().isShown()) {
                        toast = Toast.makeText(GuideActivity.this, "已经是第一页", Toast.LENGTH_SHORT);
                        toast.show();
                    }*/
                }
            } else if (pos == maxPos) {
                //已经在最后一页还想往右划
                if (positionOffsetPixels == 0 && currentPageScrollStatus == 1) {

                    /*if (toast == null || !toast.getView().isShown()) {
                        toast = Toast.makeText(GuideActivity.this, "已经是最后一页", Toast.LENGTH_SHORT);
                        toast.show();
                    }*/
                    if (isfirst) {
                        isfirst = !isfirst;
                        //设置已经引导
                        setGuided();
                        //跳转
                        Intent mIntent = new Intent();
                        mIntent.setClass(GuideActivity.this, LoginActivity.class);
//                    mIntent.setClass(GuideActivity.this, MainActivity.class);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mIntent);
                        finish();
                    }

                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            setCurrentPos(position);
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setImageResource(R.drawable.yuanxing_lanse);
                //不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    imageViews[i].setImageResource(R.drawable.yuanxing_huise);
                }
            }

        }

        public void setMaxPage(int position) {
            //设置最后一页的position值
            maxPos = position;
        }

        public void setCurrentPos(int position) {
            //设置当前页的position值
            pos = position;
        }
    }

}
