package com.mgc.letobox.happy.find.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.mgc.letobox.happy.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ShowPicVPActivity extends AppCompatActivity implements OnPageChangeListener, OnClickListener {
    private static final String PHOTO_URLS = "photoUrlsJson";
    ViewPager photoViewPager;
    TextView photoCount;
    ImageView image_return;
    ArrayList<String> photoUrls;
    private boolean isFile;
    private int currentIndex;

    public ShowPicVPActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_show_pic_vp);
        StatusBarUtil.setColor(this, 0x7f000000);

        this.photoViewPager = (ViewPager)this.findViewById(R.id.viewpager);
        this.photoCount = (TextView)this.findViewById(R.id.tv_photo_count);
        this.image_return = (ImageView)this.findViewById(R.id.image_return);
        this.image_return.setOnClickListener(this);
        if (this.getIntent() != null) {
            this.photoUrls = this.getIntent().getStringArrayListExtra("photoUrlsJson");
            this.isFile = this.getIntent().getBooleanExtra("isFile", false);
            this.currentIndex = this.getIntent().getIntExtra("currentIndex", 0);
        }

        this.photoViewPager.setAdapter(new ShowPicVPActivity.ShowPictureAdapter(this, this.photoUrls));
        this.photoViewPager.addOnPageChangeListener(this);
        this.photoCount.setText(this.currentIndex + 1 + "/" + this.photoUrls.size());
        this.photoViewPager.setCurrentItem(this.currentIndex, false);
    }

    public static void start(Context context, ArrayList<String> photoUrlsJson, int currentIndex, boolean isFile) {
        Intent starter = new Intent(context, ShowPicVPActivity.class);
        starter.putStringArrayListExtra("photoUrlsJson", photoUrlsJson);
        starter.putExtra("isFile", isFile);
        starter.putExtra("currentIndex", currentIndex);
        context.startActivity(starter);
    }

    public void onPageScrolled(int i, float v, int i1) {
    }

    public void onPageSelected(int i) {
        this.photoCount.setText(i + 1 + "/" + this.photoUrls.size());
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onClick(View v) {
        if (R.id.image_return == v.getId()) {
            this.finish();
        }

    }

    private class ShowPictureAdapter extends PagerAdapter {
        List<String> sixinMessageList;
        private Context context;

        public ShowPictureAdapter(Context context, List<String> sixinMessageList) {
            this.context = context;
            this.sixinMessageList = sixinMessageList;
        }

        public int getCount() {
            return this.sixinMessageList == null ? 0 : this.sixinMessageList.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @TargetApi(12)
        public Object instantiateItem(ViewGroup container, int position) {
            String goodsImage = (String)this.sixinMessageList.get(position);
            View view = LayoutInflater.from(this.context).inflate(R.layout.adapter_photo_show, (ViewGroup)null);
            PhotoView imageView = (PhotoView)view.findViewById(R.id.iv_showpicture);
            imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
                public void onPhotoTap(View view, float v, float v1) {
                    ShowPicVPActivity.this.finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                }
            });
            imageView.setZoomable(true);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            if (ShowPicVPActivity.this.isFile) {
                Glide.with(ShowPicVPActivity.this).load(new File(goodsImage)).into(imageView);
            } else {
                Glide.with(ShowPicVPActivity.this).load(goodsImage).into(imageView);
            }

            container.addView(view);
            return view;
        }
    }
}
