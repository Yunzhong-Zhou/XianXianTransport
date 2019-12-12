package com.transport.xianxian.activity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.transport.xianxian.R;
import com.transport.xianxian.base.BaseActivity;
import com.transport.xianxian.model.Auth_CheLiangZhaoPianModel;
import com.transport.xianxian.net.OkHttpClientManager;
import com.transport.xianxian.net.URLs;
import com.transport.xianxian.utils.MyChooseImages;
import com.transport.xianxian.utils.FileUtil;
import com.transport.xianxian.utils.MyLogger;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import id.zelory.compressor.Compressor;

import static com.transport.xianxian.net.OkHttpClientManager.IMGHOST;
import static com.transport.xianxian.utils.MyChooseImages.REQUEST_CODE_CAPTURE_CAMEIA;
import static com.transport.xianxian.utils.MyChooseImages.REQUEST_CODE_PICK_IMAGE;

/**
 * Created by zyz on 2019-10-01.
 * 车辆照片
 */
public class Auth_CheLiangZhaoPianActivity extends BaseActivity {
    int item = 0;
    RecyclerView recyclerView;
    List<Auth_CheLiangZhaoPianModel.CarTypeListBean> stringList = new ArrayList<>();
    CommonAdapter<Auth_CheLiangZhaoPianModel.CarTypeListBean> mStringAdapter;

    ViewPager viewPager;
    private ArrayList<View> pageViews = new ArrayList<>();
    GuidePageAdapter mPageAdapter;

    //选择图片及上传
    ArrayList<String> listFileNames = new ArrayList<>();
    ArrayList<File> listFiles = new ArrayList<>();
    String type = "";
    ImageView imageView1, imageView2, imageView3, imageView4;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;

    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_cheliangzhaopian);
    }

    @Override
    protected void initView() {
        imageView1 = findViewByID_My(R.id.imageView1);
        imageView2 = findViewByID_My(R.id.imageView2);
        imageView3 = findViewByID_My(R.id.imageView3);
        imageView4 = findViewByID_My(R.id.imageView4);
        linearLayout1 = findViewByID_My(R.id.linearLayout1);
        linearLayout2 = findViewByID_My(R.id.linearLayout2);
        linearLayout3 = findViewByID_My(R.id.linearLayout3);
        linearLayout4 = findViewByID_My(R.id.linearLayout4);
        textView1 = findViewByID_My(R.id.textView1);

        recyclerView = findViewByID_My(R.id.recyclerView);
        viewPager = findViewByID_My(R.id.viewPager);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_left:
                if (item > 0) {
                    item--;
                    ChangeUI();
                }
                break;
            case R.id.iv_right:
                if (item < stringList.size() - 1) {
                    item++;
                    ChangeUI();
                }
                break;
            case R.id.imageView1:
            case R.id.linearLayout1:
                type = "car_image_front";
                for (int i = 0; i < listFileNames.size(); i++) {
                    if (listFileNames.get(i).equals(type)) {
                        //删除
                        listFileNames.remove(i);
                        listFiles.remove(i);
                    }
                }
                MyChooseImages.showPhotoDialog(this);
                break;

            case R.id.imageView2:
            case R.id.linearLayout2:
                type = "car_image_back";
                for (int i = 0; i < listFileNames.size(); i++) {
                    if (listFileNames.get(i).equals(type)) {
                        //删除
                        listFileNames.remove(i);
                        listFiles.remove(i);
                    }
                }
                MyChooseImages.showPhotoDialog(this);
                break;
            case R.id.imageView3:
            case R.id.linearLayout3:
                type = "car_image_left";
                for (int i = 0; i < listFileNames.size(); i++) {
                    if (listFileNames.get(i).equals(type)) {
                        //删除
                        listFileNames.remove(i);
                        listFiles.remove(i);
                    }
                }
                MyChooseImages.showPhotoDialog(this);
                break;

            case R.id.imageView4:
            case R.id.linearLayout4:
                type = "car_image_right";
                for (int i = 0; i < listFileNames.size(); i++) {
                    if (listFileNames.get(i).equals(type)) {
                        //删除
                        listFileNames.remove(i);
                        listFiles.remove(i);
                    }
                }
                MyChooseImages.showPhotoDialog(this);
                break;
            case R.id.textView1:
                //提交
                if (match() && stringList.size() > 0) {
                    showProgress(false, getString(R.string.app_loading1));
                    String[] filenames = new String[]{};
                    File[] files = new File[]{};
                    for (int i = 0; i < listFiles.size(); i++) {
                        filenames = listFileNames.toArray(new String[i]);
                        files = listFiles.toArray(new File[i]);
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put("car_type_id", stringList.get(item).getId() + "");
                    params.put("type", "post_car_image");
                    params.put("token", localUserInfo.getToken());
                    RequestUpData(filenames, files, params);//
                }
                break;
            default:
                break;
        }
    }

    private void RequestUpData(String[] fileKeys, File[] files, HashMap<String, String> params) {
        OkHttpClientManager.postAsyn(this, URLs.Auth_CheZhu, fileKeys, files, params,
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
                        MyLogger.i(">>>>>>>>>上传驾驶证" + response);
                        myToast("上传成功");
                        finish();
                    }
                }, false);

    }

    private void ChangeUI() {
        //recyclerView切换
        recyclerView.scrollToPosition(item);
        mStringAdapter.notifyDataSetChanged();
        //设置viewPager切换
        viewPager.setCurrentItem(item);
        mPageAdapter.notifyDataSetChanged();
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
                + "&type=" + "get_car_image";
        Request(string);
    }

    private void Request(String string) {
        OkHttpClientManager.getAsyn(this, URLs.Auth_CheZhu + string,
                new OkHttpClientManager.ResultCallback<Auth_CheLiangZhaoPianModel>() {
                    @Override
                    public void onError(Request request, String info, Exception e) {
                        hideProgress();
                        if (!info.equals("")) {
                            myToast(info);
                        }
                    }

                    @Override
                    public void onResponse(final Auth_CheLiangZhaoPianModel response) {
                        hideProgress();
                        MyLogger.i(">>>>>>>>>车主认证-车辆照片认证" + response);

                        stringList = response.getCar_type_list();
                        //车型描述
                        pageViews = new ArrayList<View>();
                        for (int i = 0; i < stringList.size(); i++) {
                            if (response.getCar_type_id().equals(stringList.get(i).getId())) {
                                item = i;
                            }
                            pageViews.add(inflater.inflate(R.layout.item_auth_clzp_vp_page, null));
                        }

                        for (int i = 0; i < pageViews.size(); i++) {
                            ImageView iv = (ImageView) pageViews.get(i).findViewById(R.id.iv);
                            TextView tv1 = pageViews.get(i).findViewById(R.id.tv1);
                            TextView tv2 = pageViews.get(i).findViewById(R.id.tv2);
                            TextView tv3 = pageViews.get(i).findViewById(R.id.tv3);
                            tv1.setText("长宽高：" + stringList.get(i).getWeight());
                            tv2.setText("载重：" + stringList.get(i).getSize());
                            tv3.setText("承载体积：" + stringList.get(i).getBulk());
                            if (!stringList.get(i).getImage().equals(""))
                                Glide.with(Auth_CheLiangZhaoPianActivity.this)
                                        .load(IMGHOST + stringList.get(i).getImage())
                                        .centerCrop()
//                                    .placeholder(R.mipmap.headimg)//加载站位图
//                                    .error(R.mipmap.headimg)//加载失败
                                        .into(iv);//加载图片
                        }

                        mPageAdapter = new GuidePageAdapter();
                        viewPager.setAdapter(mPageAdapter);
                        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
                        //车型
                        mStringAdapter = new CommonAdapter<Auth_CheLiangZhaoPianModel.CarTypeListBean>
                                (Auth_CheLiangZhaoPianActivity.this, R.layout.item_auth_cheliangzhaopian_tv, stringList) {
                            @Override
                            protected void convert(ViewHolder holder, Auth_CheLiangZhaoPianModel.CarTypeListBean model, int position) {
                                TextView tv1 = holder.getView(R.id.tv1);
                                TextView tv2 = holder.getView(R.id.tv2);
                                LinearLayout ll = holder.getView(R.id.ll);
                                tv1.setText(model.getName());
                                tv2.setText(model.getName());

                                if (item == position) {
                                    ll.setVisibility(View.VISIBLE);
                                    tv1.setVisibility(View.GONE);
                                } else {
                                    ll.setVisibility(View.GONE);
                                    tv1.setVisibility(View.VISIBLE);
                                }

                            }
                        };
                        mStringAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                                item = i;
                                ChangeUI();
                            }

                            @Override
                            public boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i) {
                                return false;
                            }
                        });

                        recyclerView.setAdapter(mStringAdapter);

                        ChangeUI();

                        //车辆照片
                        if (!response.getCar_image_front().equals("")) {
                            imageView1.setVisibility(View.VISIBLE);
                            linearLayout1.setVisibility(View.GONE);
                            Glide.with(Auth_CheLiangZhaoPianActivity.this)
                                    .load(IMGHOST + response.getCar_image_front())
                                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                                    .into(imageView1);//加载图片
                        } else {
                            imageView1.setVisibility(View.GONE);
                            linearLayout1.setVisibility(View.VISIBLE);
                        }
                        if (!response.getCar_image_back().equals("")) {
                            imageView2.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.GONE);
                            Glide.with(Auth_CheLiangZhaoPianActivity.this)
                                    .load(IMGHOST + response.getCar_image_back())
                                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                                    .into(imageView2);//加载图片
                        } else {
                            imageView2.setVisibility(View.GONE);
                            linearLayout2.setVisibility(View.VISIBLE);
                        }
                        if (!response.getCar_image_left().equals("")) {
                            imageView3.setVisibility(View.VISIBLE);
                            linearLayout3.setVisibility(View.GONE);
                            Glide.with(Auth_CheLiangZhaoPianActivity.this)
                                    .load(IMGHOST + response.getCar_image_left())
                                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                                    .into(imageView3);//加载图片
                        } else {
                            imageView3.setVisibility(View.GONE);
                            linearLayout3.setVisibility(View.VISIBLE);
                        }
                        if (!response.getCar_image_right().equals("")) {
                            imageView4.setVisibility(View.VISIBLE);
                            linearLayout4.setVisibility(View.GONE);
                            Glide.with(Auth_CheLiangZhaoPianActivity.this)
                                    .load(IMGHOST + response.getCar_image_right())
                                    .centerCrop()
//                    .placeholder(R.mipmap.headimg)//加载站位图
//                    .error(R.mipmap.headimg)//加载失败
                                    .into(imageView4);//加载图片
                        } else {
                            imageView4.setVisibility(View.GONE);
                            linearLayout4.setVisibility(View.VISIBLE);
                        }


                    }
                });
    }

    private boolean match() {
        if (listFiles.size() != 4) {
            myToast("请上传车辆四面的照片");
            return false;
        }
        return true;
    }

    @Override
    protected void updateView() {
        titleView.setTitle("车辆照片");
    }

    /**
     * *****************************************选择图片********************************************
     */
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
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
                options.inSampleSize = 2;//按照比例（1 / inSampleSize）缩小bitmap的宽和高、降低分辨率
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

                switch (type) {
                    case "car_image_front":
                        imageView1.setImageBitmap(bitmap);
                        imageView1.setVisibility(View.VISIBLE);
                        linearLayout1.setVisibility(View.GONE);
                        break;
                    case "car_image_back":
                        imageView2.setImageBitmap(bitmap);
                        imageView2.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.GONE);
                        break;
                    case "car_image_left":
                        imageView3.setImageBitmap(bitmap);
                        imageView3.setVisibility(View.VISIBLE);
                        linearLayout3.setVisibility(View.GONE);
                        break;
                    case "car_image_right":
                        imageView4.setImageBitmap(bitmap);
                        imageView4.setVisibility(View.VISIBLE);
                        linearLayout4.setVisibility(View.GONE);
                        break;
                }

                //图片压缩及保存
                Uri uri1 = Uri.parse("");
                uri1 = Uri.fromFile(new File(imagePath));
                File file1 = new File(FileUtil.getPath(this, uri1));
//                listFiles.add(file1);
                listFileNames.add(type);
                File newFile = null;
                try {
                    newFile = new Compressor(this).compressToFile(file1);
                    listFiles.add(newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    myToast(getString(R.string.app_imgerr));
                }
            }
        }

    }

    /**
     * *****************************************GuidePageAdapter********************************************
     */
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
        private int maxPos = pageViews.size() - 1;//最后一页
        private int currentPageScrollStatus;

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub
            //记录page滑动状态，如果滑动了state就是1
            currentPageScrollStatus = state;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO Auto-generated method stub

            //recyclerView切换
            recyclerView.scrollToPosition(item);
            mStringAdapter.notifyDataSetChanged();

            if (item == 0) {
                //如果offsetPixels是0页面也被滑动了，代表在第一页还要往左划
                if (positionOffsetPixels == 0 && currentPageScrollStatus == 1) {

                }
            } else if (item == maxPos) {
                //已经在最后一页还想往右划
                if (positionOffsetPixels == 0 && currentPageScrollStatus == 1) {


                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub

            setCurrentPos(position);

        }

        public void setMaxPage(int position) {
            //设置最后一页的position值
            maxPos = position;
        }

        public void setCurrentPos(int position) {
            //设置当前页的position值
            item = position;
        }
    }
}
