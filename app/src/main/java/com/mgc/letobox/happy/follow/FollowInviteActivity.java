package com.mgc.letobox.happy.follow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.leto.game.base.LetoComponent;
import com.mgc.leto.game.base.bean.SHARE_PLATFORM;
import com.mgc.leto.game.base.config.FileConfig;
import com.mgc.leto.game.base.listener.ILetoShareListener;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.MD5;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.StatusBarUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.view.tablayout.CommonTabLayout;
import com.mgc.leto.game.base.view.tablayout.entity.TabEntity;
import com.mgc.leto.game.base.view.tablayout.listener.CustomTabEntity;
import com.mgc.leto.game.base.view.tablayout.listener.OnTabSelectListener;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.BaseActivity;
import com.mgc.letobox.happy.find.ui.HeaderViewPagerFragment;
import com.mgc.letobox.happy.follow.bean.FollowInviteBean;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.ArrayList;

public class FollowInviteActivity extends BaseActivity implements IFollowShareListener, ILetoShareListener {

    // views
    private ImageView _backBtn;
    private TextView _titleLabel;
    private TextView _titleRightLabel;

    Dialog shareDialog;

    CommonTabLayout _indicatorView;
    ViewPager _viewPager;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String titleName[] = new String[]{"邀请收徒", "我的徒弟"};
    private ArrayList<HeaderViewPagerFragment> fragments;

    MyFollowingFragment myFollowingFragment;
    InviteFollowFragment inviteFragment;

    FollowInviteBean _followInviteBean;

    IFollowShareListener _shareListener;


    public static void start(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, FollowInviteActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
        }

        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_follow_invite"));

        // find views
        _backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        _titleLabel = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
        _titleRightLabel = findViewById(MResource.getIdByName(this, "R.id.ceate_circle"));
        _indicatorView = findViewById(MResource.getIdByName(this, "R.id.tabLayout"));
        _viewPager = findViewById(MResource.getIdByName(this, "R.id.viewPager"));
//        _scrollView = findViewById(MResource.getIdByName(this, "R.id.sv_content"));

        // back click
        _backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                finish();
                return true;
            }
        });

        _titleRightLabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                FollowUtil.startCommonQuestion(FollowInviteActivity.this);

                return true;
            }
        });

        // title
        _titleLabel.setText("邀请收徒");

        _titleRightLabel.setText("常见问题");

        // setup list

        fragments = new ArrayList<HeaderViewPagerFragment>();
        inviteFragment = (InviteFollowFragment) InviteFollowFragment.getInstance();
        myFollowingFragment = (MyFollowingFragment) MyFollowingFragment.getInstance();
        fragments.add(inviteFragment);
        fragments.add(myFollowingFragment);


//        _scrollView.setOnScrollBottomListener(new ScrollBottomView.OnScrollBottomListener() {
//
//            @Override
//            public void onScrollBottom(int type) {
//
//                if (_viewPager.getCurrentItem() == 0) {
//                    //detailFragment.loadMore();
//                } else if (_viewPager.getCurrentItem() == 1) {
//                    myFollowingFragment.loadMore();
//                }
//            }
//        });
//        _scrollView.onScrollChangedListener(new ScrollBottomView.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                int[] location = new int[2];
//                _indicatorView.getLocationOnScreen(location);
//
//            }
//        });
        _viewPager.setOffscreenPageLimit(2);
        _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                _indicatorView.setCurrentTab(position);
//                _viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        for (int i = 0; i < titleName.length; i++) {
            mTabEntities.add(new TabEntity(titleName[i], 0, 0));
        }

        initIndicateView();

        _viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        _viewPager.setCurrentItem(0, false);

    }


    private void initIndicateView() {

//        _indicatorView.setDividerColor(R.color.bg_common);
//        _indicatorView.setDividerWidth(1);
        _indicatorView.setTabTitlePaddingBottom(20);
        _indicatorView.setIconVisible(false);
        _indicatorView.setIndicatorColor(ColorUtil.parseColor("#FF8400"));
        _indicatorView.setIndicatorCornerRadius(1);
        _indicatorView.setIndicatorHeight(2);
        _indicatorView.setIndicatorWidth(30);
        _indicatorView.setTabSpaceEqual(true);
        _indicatorView.setTextBold(CommonTabLayout.TEXT_BOLD_WHEN_SELECT);
        _indicatorView.setTextSelectColor(ColorUtil.parseColor("#FF8400"));
        _indicatorView.setTextUnselectColor(ColorUtil.parseColor("#000000"));
        _indicatorView.setTextsize(15);
        _indicatorView.setTabData(mTabEntities);
        _indicatorView.setCurrentTab(0);
        _indicatorView.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                _viewPager.setCurrentItem(position, true);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != shareDialog && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
        shareDialog = null;

    }

    @Override
    public void onShare(SHARE_PLATFORM platform) {
        shareToWeChat(platform);
    }

    @Override
    public void onShare() {
        if (null != shareDialog && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }
        shareDialog = new SharePlatformDialog().showDialog(FollowInviteActivity.this, new ConfirmDialogListener() {

            @Override
            public void setPlatform(SHARE_PLATFORM platform) {

                if (platform == SHARE_PLATFORM.FACE_TO_FACE) {
                    FollowUtil.startFaceToFace(FollowInviteActivity.this);
                } else {
                    shareToWeChat(platform);
                }
            }

            @Override
            public void cancel() {

            }
        });
    }

    public void shareToWeChat(SHARE_PLATFORM platform) {
        if (_followInviteBean != null && !TextUtils.isEmpty(_followInviteBean.getShareUrl())) {
            File file = new File(FileConfig.getSdCard(FollowInviteActivity.this), MD5.md5(_followInviteBean.getShareUrl()) + ".jpg");
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                LetoComponent.shareImageToPlatform(FollowInviteActivity.this, platform, FollowInviteActivity.this, bitmap, "邀请收徒");
            } else {
                ToastUtil.s(FollowInviteActivity.this, "分享图片不存在～");
            }
        } else {
            ToastUtil.s(FollowInviteActivity.this, "分享异常");
        }

    }

    @Override
    public void onStart(SHARE_PLATFORM platform) {

    }

    @Override
    public void onResult(SHARE_PLATFORM platform) {
        ToastUtil.s(FollowInviteActivity.this, "欢迎回来～");
    }

    @Override
    public void onError(SHARE_PLATFORM platform, Throwable throwable) {
        ToastUtil.s(FollowInviteActivity.this, "分享错误～");
    }

    @Override
    public void onCancel(SHARE_PLATFORM platform) {
        ToastUtil.s(FollowInviteActivity.this, "分享取消～");
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleName[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public void setFlowInviteBean(FollowInviteBean inviteBean) {
        _followInviteBean = inviteBean;
    }


}
