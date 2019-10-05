package com.mgc.letobox.happy.find.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.ToastUtil;
import com.lzy.widget.HeaderViewPager;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.ui.CircleArticleFragment;
import com.mgc.letobox.happy.circle.ui.CircleGameFragment;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.bean.KOLResponse;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class KOLActivitiy extends AppCompatActivity {

	ViewPager viewPager;
    ImageView iv_game_cover;
    TextView tab1;
    TextView tab2;
    TextView tab3;
    View tabLine;
    HeaderViewPager scrollableLayout;
    ImageView imageView_back;
    ImageView ceate_image_circle;
    View titleBar;
    View status_bar_fix;
    View titleBar_Bg;
    TextView titleBar_title;
    CheckBox check_guanzhu;
    ImageView image_view_logo;
    ImageView iv_grade;

    TextView textViewTitle;
    TextView textViewDetails;
    TextView textViewGuanZhuNub;
    TextView textViewFenSiNub;

    private List<HeaderViewPagerFragment> fragments = new ArrayList<>();
    private int screenWidth;
    private int userId;
    private KOLResponse mResponse;

    public static void startActivity(Context mContext, int userId) {
        Intent intent = new Intent(mContext, KOLActivitiy.class);
        intent.putExtra(FindConst.EXTRA_ARTICLE_TYPE, userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kolactivitiy);

        // find views
        viewPager = findViewById(R.id.viewPager);
        iv_game_cover = findViewById(R.id.iv_game_cover);
        tab1 = findViewById(R.id.tab1);
        tab2 = findViewById(R.id.tab2);
        tab3 = findViewById(R.id.tab3);
        tabLine = findViewById(R.id.tabLine);
        scrollableLayout = findViewById(R.id.scrollableLayout);
        imageView_back = findViewById(R.id.imageView_back);
        ceate_image_circle = findViewById(R.id.ceate_image_circle);
        titleBar = findViewById(R.id.titleBar);
        status_bar_fix = findViewById(R.id.status_bar_fix);
        titleBar_Bg = findViewById(R.id.bg);
        titleBar_title = findViewById(R.id.title);
        check_guanzhu = findViewById(R.id.check_guanzhu);
        image_view_logo = findViewById(R.id.image_view_logo);
        iv_grade = findViewById(R.id.iv_grade);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDetails = findViewById(R.id.textViewDetails);
        textViewGuanZhuNub = findViewById(R.id.textViewGuanZhuNub);
        textViewFenSiNub = findViewById(R.id.textViewFenSiNub);

        EventBus.getDefault().register(this);
        StatusBarUtil.setTransparentForImageView(this, null);
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        if (null != getIntent()) {
            userId = getIntent().getIntExtra(FindConst.EXTRA_ARTICLE_TYPE, 0);
        }
        initNet();
        initView();
    }

    private void initNet() {
        FindApiUtil.getKoLDetail(this, userId, new HttpCallbackDecode<KOLResponse>(KOLActivitiy.this, null) {
            @Override
            public void onDataSuccess(final KOLResponse response) {
                if (response != null) {
                    mResponse = response;
                    // true: 已关注 false: +关注
                    check_guanzhu.setChecked(response.getIs_follow() == 0 ? false : true);
                    check_guanzhu.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                        @Override
                        public boolean onClicked() {
                            if (check_guanzhu.isChecked()) {
                                userFollow(response.getKol_id(),1);
                            } else {
                                userFollow(response.getKol_id(),2);
                            }
                            return true;
                        }
                    });
                    titleBar_title.setText(response.getNickname());
                    if (response.getIs_kol() == 0) {
                        tab1.setText("玩过的游戏");
                    } else {
                        tab1.setText("推荐的游戏");
                    }

                    if (LoginManager.getMemId(KOLActivitiy.this) != null) {
                        if (response.getKol_id() == Integer.valueOf(LoginManager.getMemId(KOLActivitiy.this))) {
                            check_guanzhu.setVisibility(View.GONE);
                        } else {
                            check_guanzhu.setVisibility(View.VISIBLE);
                        }
                    }

                    if (response.getPortrait() != null && !response.getPortrait().isEmpty()) {
                        GlideUtil.loadRoundedCorner(KOLActivitiy.this,
                            response.getPortrait(),
                            image_view_logo,
                            30,
                            R.mipmap.default_avatar);
                    }
                    GlideUtil.load(KOLActivitiy.this, response.getLevel_pic(), iv_grade);

                    textViewTitle.setText(response.getNickname());
                    if (response.getSignature() != null && !response.getSignature().isEmpty()) {
                        textViewDetails.setVisibility(View.VISIBLE);
                        textViewDetails.setText(response.getSignature());
                    } else {
                        textViewDetails.setVisibility(View.GONE);
                    }
                    textViewGuanZhuNub.setText(response.getFollow_count() + "");
                    textViewFenSiNub.setText(response.getFans() + "");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        });
    }


    /**
     * 关注返回刷新
     * @param change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowChange(FollowEvent change) {
        if (mResponse != null) {
            int countFans;
            check_guanzhu.setChecked(change.isFollow());
            if (change.isFollow()) {
                countFans = mResponse.getFans() + 1;
                textViewFenSiNub.setText(countFans + "");
            } else {
                countFans = mResponse.getFans() - 1;
                textViewFenSiNub.setText(countFans + "");
            }
            mResponse.setFans(countFans);
        }
    }

    /**
     * 关注|取消关注
     *
     * @param userId UID
     * @param type   1 关注 2取消关注
     */
    public void userFollow(final int userId, final int type) {
        if (mResponse == null) {
            return;
        }
        FindApiUtil.followUser(this, userId, type, new HttpCallbackDecode<RewardResultBean>(KOLActivitiy.this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                mResponse.is_follow = type == 1 ? 1 : 0;
//                check_guanzhu.setChecked(type == 1 ? true : false);
                EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));
                if (data != null) {
                    ToastUtil.s(KOLActivitiy.this, "关注成功");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
//                check_guanzhu.setChecked(type == 1 ? false : true);
            }
        });
    }

    private void initView() {
        CircleGameFragment circleGameFragment = new CircleGameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FindConst.EXTRA_ARTICLE_TYPE, userId);
        circleGameFragment.setArguments(bundle);
        fragments.add(circleGameFragment);

        CircleArticleFragment circleArticleFragment = new CircleArticleFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(FindConst.EXTRA_ARTICLE_TYPE, userId);
        circleArticleFragment.setArguments(bundle1);
        fragments.add(circleArticleFragment);

//        CircleVideoFragment circleVideoFragment = new CircleVideoFragment();
//        Bundle bundle2 = new Bundle();
//        bundle2.putInt(IntentContant.EXTRA_ARTICLE_TYPE,userId);
//        circleVideoFragment.setArguments(bundle2);
//        fragments.add(circleVideoFragment);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));

        status_bar_fix.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.getStatusHeight(this)));
        titleBar_Bg.setAlpha(0);
        status_bar_fix.setAlpha(0);
        titleBar_title.setAlpha(0);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));
        scrollableLayout.setCurrentScrollableContainer(fragments.get(0));

        scrollableLayout.setOnScrollListener(new HeaderViewPager.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                //动态改变标题栏的透明度,注意转化为浮点型
                float alpha = 1.0f * currentY / maxY;
                titleBar_Bg.setAlpha(alpha);
                //注意头部局的颜色也需要改变
                status_bar_fix.setAlpha(alpha);
                titleBar_title.setAlpha(alpha);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer(fragments.get(position));
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int loction = screenWidth / 2 * position + positionOffsetPixels / 2;
                tabLine.setX(loction + (tab1.getWidth() - tabLine.getWidth()) / 2);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);

        initOnClicik();
    }

    private void initOnClicik() {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ceate_image_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareView();
            }
        });

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        //        tab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewPager.setCurrentItem(2);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        //当前窗口获取焦点时，才能正真拿到titlebar的高度，此时将需要固定的偏移量设置给scrollableLayout即可
//        scrollableLayout.setTopOffset(titleBar.getHeight());
//    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<HeaderViewPagerFragment> mFragments;

        public MyPagerAdapter(FragmentManager fm, List<HeaderViewPagerFragment> mFragments) {
            super(fm);
            this.mFragments = mFragments;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    private void showShareView() {
//        if (mResponse == null) {
//            T.s(KOLActivitiy.this, "数据异常，无法分享");
//            return;
//        }
//        new SharePlatformDialog().showDialog(KOLActivitiy.this, mResponse.getShare(), new SharePlatformDialog.ConfirmDialogListener() {
//            @Override
//            public void setPlatform(SHARE_MEDIA platform) {
//                String shareImage = null;
//                String shareTitle = mResponse.getNickname() + "的个人主页-梦工厂游戏钱包";
//                String shareUrl = mResponse.getShare_url();
//                String shareContent = mResponse.getSignature();
//
//                if (null != mResponse && null != mResponse.getPortrait()) {
//                    shareImage = mResponse.getPortrait();
//                }
//
//
//                ShareUtil.shareToPlatform(mContext, platform, new UMShareListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//
//                    }
//
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media) {
//                        getStimulate(6);
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media) {
//
//                    }
//                }, shareUrl, shareTitle, shareContent, shareImage);
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//        });
    }
}
