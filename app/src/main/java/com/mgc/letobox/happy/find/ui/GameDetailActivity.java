package com.mgc.letobox.happy.find.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ledong.lib.leto.Leto;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.NetUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.StarBar;
import com.leto.game.base.view.tablayout.CommonTabLayout;
import com.leto.game.base.view.tablayout.entity.TabEntity;
import com.leto.game.base.view.tablayout.listener.CustomTabEntity;
import com.leto.game.base.view.tablayout.listener.OnTabSelectListener;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.bean.GameBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.dialog.RichEditDialog;
import com.mgc.letobox.happy.find.dialog.SharePlatformDialog;
import com.mgc.letobox.happy.find.event.CommentUpdateEvent;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.model.IssueGame;
import com.mgc.letobox.happy.find.model.KOL;
import com.mgc.letobox.happy.find.util.FileUtils;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.ShareUtil;
import com.mgc.letobox.happy.find.view.ChildViewPager;
import com.mgc.letobox.happy.find.view.GameBaseView;
import com.mgc.letobox.happy.find.view.ScrollBottomView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

/**
 * 带充值，但是无返利的app游戏详情页
 */
public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_title;
    RelativeLayout rl_left;
    ImageView iv_right;
    LinearLayout in_title;
    ImageView iv_avatar;
    ImageView iv_grade;
    TextView tv_name;
    TextView tv_time;
    TextView tv_follow_num;
    TextView tv_favorite;
    CheckBox cb_follow;

    GameBean gameBean;

    GameBaseView view_game_base;
    ChildViewPager homePager;
    ScrollBottomView scrollView;
    //HeaderViewPager scrollableLayout;

    LinearLayout ll_favorite;
    LinearLayout ll_share;
    ImageView iv_rating;
    ImageView iv_favorite;

    Button _launchBtn;

    View in_kol;


//    ScrollIndicatorView indicatorView;
//    ScrollIndicatorView indicatorViewMagic;
//
//    private IndicatorViewPager indicatorViewPager;
//    private IndicatorViewPager indicatorViewPagerMagic;

    CommonTabLayout indicatorView;
    CommonTabLayout indicatorViewMagic;




    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private String titleName[] = new String[]{"详情", "评论"};
    private ArrayList<HeaderViewPagerFragment> fragments;


    GameCommentFragment commentFragment;
    GameDetailFragment detailFragment;

    IssueGame issueGame;
    KOL issueKOL;
    String issueTime;

    Dialog mDialog;

    String mGameId;

    RichEditDialog mRichEditDialog;

//    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        StatusBarUtil.setTransparentForImageView(this, null);

        // find views
        indicatorViewMagic = findViewById(R.id.indicator_magic);
        indicatorView = findViewById(R.id.indicator);
        in_kol = findViewById(R.id.in_kol);
        _launchBtn = findViewById(R.id.launch);
        iv_favorite = findViewById(R.id.iv_favorite);
        iv_rating = findViewById(R.id.iv_rating);
        ll_share = findViewById(R.id.ll_share);
        ll_favorite = findViewById(R.id.ll_favorite);
        scrollView = findViewById(R.id.sv_content);
        homePager = findViewById(R.id.home_pager);
        view_game_base = findViewById(R.id.v_gamebase);
        cb_follow = findViewById(R.id.cb_follow);
        tv_favorite = findViewById(R.id.tv_favorite);
        tv_follow_num = findViewById(R.id.tv_follow_num);
        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        iv_grade = findViewById(R.id.iv_grade);
        iv_avatar = findViewById(R.id.iv_avatar);
        in_title = findViewById(R.id.in_title);
        iv_right = findViewById(R.id.iv_right);
        rl_left = findViewById(R.id.rl_left);
        tv_title = findViewById(R.id.tv_title);

        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        issueGame = (IssueGame) intent.getSerializableExtra("issue_game");
        issueKOL = (KOL) intent.getSerializableExtra("issue_kol");
        issueTime = intent.getStringExtra("issue_time");

        mGameId = intent.getStringExtra("gameId");

        setupUI();

        setupUserData();

        loadGameData();

    }

    private void setupUI() {
        tv_title.setText("游戏详情");

        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.share);
        iv_right.setOnClickListener(this);

        tv_follow_num.setVisibility(View.GONE);

        rl_left.setOnClickListener(this);
        iv_rating.setOnClickListener(this);
        ll_favorite.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        cb_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != issueKOL) {
                    userFollow(issueKOL.id, issueKOL.isfollow == 1 ? 2 : 1);
                }
            }
        });


        fragments = new ArrayList<HeaderViewPagerFragment>();
        detailFragment = (GameDetailFragment) GameDetailFragment.getInstance(gameBean);
        commentFragment = (GameCommentFragment) GameCommentFragment.getInstance(gameBean);
        fragments.add(detailFragment);
        fragments.add(commentFragment);

        scrollView.setOnScrollBottomListener(new ScrollBottomView.OnScrollBottomListener() {

            @Override
            public void onScrollBottom(int type) {

                if (homePager.getCurrentItem() == 0) {
                    //detailFragment.loadMore();
                } else if (homePager.getCurrentItem() == 1) {
                    commentFragment.loadMore();
                }
            }
        });
        scrollView.onScrollChangedListener(new ScrollBottomView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                int[] location = new int[2];
                indicatorView.getLocationOnScreen(location);
                int yPosition = location[1];
                if (yPosition < in_title.getHeight()) {
                    indicatorViewMagic.setVisibility(View.VISIBLE);
                    //scrollView.setNeedScroll(false);
                } else {
                    indicatorViewMagic.setVisibility(View.GONE);
                    //scrollView.setNeedScroll(true);
                }

            }
        });
        homePager.setOffscreenPageLimit(2);
        //homePager.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        homePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicatorViewMagic.setCurrentTab(position);
                indicatorView.setCurrentTab(position);
                homePager.resetHeight(position);
                if (position == 1) {
                    iv_rating.setVisibility(View.VISIBLE);
                } else {
                    iv_rating.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        for (int i = 0; i < titleName.length; i++) {
            mTabEntities.add(new TabEntity(titleName[i], 0, 0));
        }

        indicatorView.setDividerColor(R.color.bg_common);
        indicatorView.setDividerWidth(1);
        indicatorView.setIconVisible(false);
        indicatorView.setIndicatorColor(R.color.text_blue);
        indicatorView.setIndicatorCornerRadius(1);
        indicatorView.setIndicatorHeight(2);
        indicatorView.setIndicatorWidth(100);
        indicatorView.setTabSpaceEqual(true);
        indicatorView.setTextBold(CommonTabLayout.TEXT_BOLD_NONE);
        indicatorView.setTextSelectColor(R.color.text_blue);
        indicatorView.setTextUnselectColor(R.color.text_black);
        indicatorView.setTextsize(15);
        indicatorView.setTabData(mTabEntities);
        indicatorView.setCurrentTab(0);
        indicatorView.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                homePager.setCurrentItem(position,true);
                homePager.resetHeight(position);
                indicatorViewMagic.setCurrentTab(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        indicatorViewMagic.setDividerColor(R.color.bg_common);
        indicatorViewMagic.setDividerWidth(1);
        indicatorViewMagic.setIconVisible(false);
        indicatorViewMagic.setIndicatorColor(R.color.text_blue);
        indicatorViewMagic.setIndicatorCornerRadius(1);
        indicatorViewMagic.setIndicatorHeight(2);
        indicatorViewMagic.setIndicatorWidth(100);
        indicatorViewMagic.setTabSpaceEqual(true);
        indicatorViewMagic.setTextBold(CommonTabLayout.TEXT_BOLD_NONE);
        indicatorViewMagic.setTextSelectColor(R.color.text_blue);
        indicatorViewMagic.setTextUnselectColor(R.color.text_black);
        indicatorViewMagic.setTextsize(15);
        indicatorViewMagic.setTabData(mTabEntities);
        //indicatorViewMagic.setCurrentTab(0);
        indicatorViewMagic.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                homePager.setCurrentItem(position, true);
                homePager.resetHeight(position);
                indicatorView.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        homePager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        homePager.setCurrentItem(0,false);
        homePager.resetHeight(0);

        // launch
        _launchBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                // TODO 判断游戏类型
                Leto.getInstance().jumpMiniGameWithAppId(GameDetailActivity.this, mGameId);
                return true;
            }
        });
    }

    public void resetHeight(int pos) {
        homePager.resetHeight(pos);
    }

    private void loadGameData() {
        String gameId = issueGame != null ? issueGame.getGameid() : mGameId;
        if(!TextUtils.isEmpty(gameId)) {
            FindApiUtil.getGameDetail(this, gameId, new HttpCallbackDecode<GameBean>(this, null) {
                @Override
                public void onDataSuccess(GameBean data) {
                    if(data != null) {
                        setupData(data);
                    }
                }
            });
        }
    }

    private void setupUserData() {
        if (issueKOL == null) {
            in_kol.setVisibility(View.GONE);
            return;
        }
        tv_name.setText(issueKOL.getNickname());
        if (!TextUtils.isEmpty(issueKOL.getCover_pic())) {
            GlideUtil.loadRoundedCorner(this,
                issueKOL.getCover_pic(),
                iv_avatar,
                20,
                R.mipmap.default_avatar);
        }
        GlideUtil.load(this, issueKOL.getLevel_pic(), iv_grade);
        tv_time.setText(issueTime);

        cb_follow.setChecked(issueKOL.isfollow == 0 ? false : true);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KOLActivitiy.startActivity(GameDetailActivity.this, issueKOL.getId());
            }
        });
    }

    private void setupData(GameBean data) {
        this.gameBean = data;
        indicatorView.showMsg(1, data.getComment());
        indicatorViewMagic.showMsg(1, data.getComment());

        view_game_base.setGameBean(data);

        commentFragment.setGameBean(data);

        detailFragment.setGameBean(data);

        iv_favorite.setImageResource(data.getIs_collect() == 2 ? R.mipmap.ic_favorite : R.mipmap.ic_favorite_selected);
        tv_favorite.setText(data.getIs_collect() == 2 ? "收藏": "已收藏");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_left:
                //finish();
                onBackPressed();//销毁自己
                break;
            case R.id.iv_right:
                showShareView();
                break;

            case R.id.iv_rating:
                onStartComment(gameBean.getGameid(), gameBean.getUser().getStar(), gameBean.getUser().getStar() > 0 ? false : true);

                break;
            case R.id.ll_share:
                showShareView();
                break;

            case R.id.ll_favorite:
                gameFavorite(gameBean.getGameid(), gameBean.getIs_collect() == 1 ? 2 : 1);
                break;
        }
    }


    public static void start(Context context, String gameId) {
        if (!NetUtil.isNetWorkConneted(context)) {
            ToastUtil.s(context, "网络不通，请稍后再试！");
            return;
        }
        Intent starter = new Intent(context, GameDetailActivity.class);
        starter.putExtra("gameId", gameId);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(starter);
    }

    public static void start(Context context, IssueGame game, KOL kol, String time) {
        if (!NetUtil.isNetWorkConneted(context)) {
            ToastUtil.s(context, "网络不通，请稍后再试！");
            return;
        }
        Intent starter = new Intent(context, GameDetailActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        starter.putExtra("issue_game", game);
        starter.putExtra("issue_kol", kol);
        starter.putExtra("issue_time", time);
        context.startActivity(starter);
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


    public void gotoComment() {
        //commentFragment.loadMore();
        homePager.setCurrentItem(1, false);
    }

    public void writeComment() {
        onStartComment(gameBean.getGameid(), gameBean.getUser().getStar(), gameBean.getUser().getStar() > 0 ? false : true);
    }


    /**
     * 关注|取消关注
     *
     * @param userId UID
     * @param type   1 关注 2取消关注
     */
    public void userFollow(final int userId, final int type) {
        FindApiUtil.followUser(this, userId, type, new HttpCallbackDecode<RewardResultBean>(this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                issueKOL.isfollow = type == 1 ? 1 : 0;
                cb_follow.setChecked(type == 1 ? true : false);
                EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));
                if (data != null) {
                    ToastUtil.s(GameDetailActivity.this, "关注成功");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

            }

            @Override
            public void onFailure(String code, String msg) {
                cb_follow.setChecked(type == 1 ? false : true);
                ToastUtil.s(GameDetailActivity.this, msg);
            }
        });
    }


    /**
     * 写评论
     *
     * @param gameId UID
     * @param score  1 关注 2取消关注
     */
    public void comment(final int gameId, final int score, String content) {
        DialogUtil.showDialog(this, "发布中...");
        FindApiUtil.gameComment(this, gameId, content, score, new HttpCallbackDecode<RewardResultBean>(this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {

                if (mDialog != null) {
                    mRichEditDialog.clear();
                    mDialog.dismiss();
                }
                EventBus.getDefault().post(new CommentUpdateEvent(gameId, 1, score));

                if (data != null) {
                    ToastUtil.s(GameDetailActivity.this, "评论成功");
                }

                //重新获取游戏数据
                loadGameData();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUtil.dismissDialog();
            }

            @Override
            public void onFailure(String code, String msg) {
                ToastUtil.s(GameDetailActivity.this, msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentChange(CommentUpdateEvent change) {
//        if (change.getType() == 1 && change.getScore() > 0) {
//            loadGameData(false);
//        }
    }

    private String mImagePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK != resultCode) {
            return;
        }
        switch (requestCode) {
            case FindConst.REQUEST_CAMERA:
                //cropImage(Uri.fromFile(new File(tempfilePath)));
                break;

            case FindConst.REQUEST_ALBUM:
                try {
                    if (!TextUtils.isEmpty(mImagePath) && new File(mImagePath).exists()) {
                        new File(mImagePath).delete();
                    }
                } catch (Exception e) {

                }
                mImagePath = Environment.getExternalStorageDirectory() + File.separator + LoginManager.getNickname(this) + ".jpg";
                Uri uri = data.getData();
                if (uri != null) {
                    mRichEditDialog.bindImage(FileUtils.getRealFilePath(this, uri));
                }
                break;
        }

    }


    public void onStartComment(final String gameId, int star, boolean enableChange) {

        if (null == mRichEditDialog) {
            mRichEditDialog = new RichEditDialog();
        }

        mRichEditDialog.fillRatingDialog(GameDetailActivity.this, 0, enableChange, star, new RichEditDialog.FillDialogCallBack() {
            @Override
            public void textViewCreate(Dialog dialog, StarBar starBar, EditText contentEditer, EditText titleEditer) {
                mDialog = dialog;
                float star = starBar.getStarMark();
                if (star == 0) {
                    ToastUtil.s(GameDetailActivity.this, "请打分！");
                    return;
                }
                if (TextUtils.isEmpty(contentEditer.getText().toString().trim())) {
                    ToastUtil.s(GameDetailActivity.this, "请输入内容！");
                    return;
                }

                comment(Integer.parseInt(gameId), (int) star, mRichEditDialog.getContent());
            }

            @Override
            public void selectPicture() {
                Intent albumIntent = new Intent(Intent.ACTION_PICK);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FindConst.IMAGE_UNSPECIFIED);
                startActivityForResult(albumIntent, FindConst.REQUEST_ALBUM);
            }

            @Override
            public void cancel() {
                EventBus.getDefault().post(new CommentUpdateEvent(Integer.parseInt(gameId), 1, 0));
            }
        });
    }

    private void showShareView() {
        new SharePlatformDialog().showDialog(GameDetailActivity.this, gameBean.getShare(), new SharePlatformDialog.ConfirmDialogListener() {
            @Override
            public void setPlatform(SHARE_MEDIA platform) {
                String pkgName = GameDetailActivity.this.getPackageName();
                String shareImage = gameBean.getIcon();
                String shareTitle = gameBean.getGamename() + "(评分" + gameBean.getScore() + "， " + gameBean.getComment() + "人评价) -" + BaseAppUtil.getAppName(GameDetailActivity.this, pkgName);
                // TODO 使用什么share url?
                String shareUrl = gameBean.getShare_url();
                String shareContent = gameBean.getDesc();

                ShareUtil.shareToPlatform(GameDetailActivity.this, platform, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                    }
                }, shareUrl, shareTitle, shareContent, shareImage);
            }

            @Override
            public void cancel() {

            }
        });
    }


    //1 收藏 2取消收藏
    private void gameFavorite(String gameId, final int type) {
        FindApiUtil.gameFavorite(this, gameId, type, new HttpCallbackDecode<RewardResultBean>(this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                if (type == 1) {
                    gameBean.setCollect_count(gameBean.getCollect_count() + 1);
                    gameBean.setIs_collect(1);

                    if (null != data) {
                        ToastUtil.s(GameDetailActivity.this, "收藏成功");
                    }
                } else {
                    gameBean.setCollect_count(gameBean.getCollect_count() - 1);
                    gameBean.setIs_collect(2);
                }
                //tv_favorite.setText(String.format("收藏(%d)", issueDetail.collect_count));
                iv_favorite.setImageResource(type == 2 ? R.mipmap.ic_favorite : R.mipmap.ic_favorite_selected);
                tv_favorite.setText(type == 2 ? "收藏": "已收藏");
                view_game_base.setGameBean(gameBean);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        });
    }
}
