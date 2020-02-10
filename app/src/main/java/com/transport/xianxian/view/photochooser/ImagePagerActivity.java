package com.transport.xianxian.view.photochooser;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.transport.xianxian.R;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ImagePagerActivity extends AppCompatActivity {

    //private static final String STATE_POSITION = "STATE_POSITION";
    public final static String NEED_EDIT_EXTRA = "needEdit";
    public final static String M_PAGER_POSITION_EXTRA = "mPagerPosition";
    public final static String IS_PRIVATE_EXTRA = "isPrivate";
    public final static String M_ARRAY_URI_EXTRA = "mArrayUri";
    public final static String M_SINGLE_URI_EXTRA = "mSingleUri";

    DisplayImageOptions options;

    ViewPager pager;

    int mPagerPosition;

    ArrayList<String> mArrayUri;

    boolean isPrivate;

    String mSingleUri;

    boolean needEdit;

    ImagePager adapter;

    ArrayList<String> mDelUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_image_pager);
    	init();
    }
    
    void init() {
    	injectExtras_();
    	pager=(ViewPager) findViewById(R.id.pager);
        if (needEdit) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(android.R.color.transparent);
        } else {
            getSupportActionBar().hide();
        }

        if (mSingleUri != null) {
            mArrayUri = new ArrayList<String>();
            mArrayUri.add(mSingleUri);
            mPagerPosition = 0;
        }

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.image_empty)
                .showImageOnFail(R.mipmap.image_empty)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        if (isPrivate) {

        } else {
            initPager();
        }
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(NEED_EDIT_EXTRA)) {
                needEdit = extras_.getBoolean(NEED_EDIT_EXTRA);
            }
            if (extras_.containsKey(M_PAGER_POSITION_EXTRA)) {
                mPagerPosition = extras_.getInt(M_PAGER_POSITION_EXTRA);
            }
            if (extras_.containsKey(IS_PRIVATE_EXTRA)) {
                isPrivate = extras_.getBoolean(IS_PRIVATE_EXTRA);
            }
            if (extras_.containsKey(M_ARRAY_URI_EXTRA)) {
                mArrayUri = extras_.getStringArrayList(M_ARRAY_URI_EXTRA);
            }
            if (extras_.containsKey(M_SINGLE_URI_EXTRA)) {
                mSingleUri = extras_.getString(M_SINGLE_URI_EXTRA);
            }
        }
    }
    
    private void initPager() {
        adapter = new ImagePager(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(mPagerPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (needEdit) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.photo_pager, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mDelUrls.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra("mDelUrls", mDelUrls);
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();

        } else if (i == R.id.action_del_maopao) {
            final int selectPos = pager.getCurrentItem();
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("图片")
                    .setMessage("确定删除？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mArrayUri != null) {
                                String s = mArrayUri.remove(selectPos);
                                mDelUrls.add(s);
                                if (mArrayUri.isEmpty()) {
                                    onBackPressed();
                                } else {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            CustomDialog.dialogTitleLineColor(this, dialog);


        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(STATE_POSITION, pager.getCurrentItem());
//    }

    class ImagePager extends FragmentPagerAdapter {

        public ImagePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	ImagePagerFragment imagePagerFragment=new ImagePagerFragment();
        	Bundle bundle=new Bundle();
        	bundle.putString(ImagePagerFragment.URI_ARG, mArrayUri.get(i));
        	imagePagerFragment.setArguments(bundle);
            return imagePagerFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImagePagerFragment fragment = (ImagePagerFragment) super.instantiateItem(container, position);
            fragment.setData(mArrayUri.get(position));
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mArrayUri.size();
        }
    }

    public final static class CustomDialog {

        private static void dialogTitleLineColor(Context context, Dialog dialog, int color) {
            String dividers[] = {
                    "android:id/titleDividerTop", "android:id/titleDivider"
            };

            for (int i = 0; i < dividers.length; ++i) {
                int divierId = context.getResources().getIdentifier(dividers[i], null, null);
                View divider = dialog.findViewById(divierId);
                if (divider != null) {
                    divider.setBackgroundColor(color);
                }
            }
        }

        public static void dialogTitleLineColor(Context context, Dialog dialog) {
            if (dialog != null) {
                dialogTitleLineColor(context, dialog, context.getResources().getColor(R.color.green));
            }
        }
    }
}