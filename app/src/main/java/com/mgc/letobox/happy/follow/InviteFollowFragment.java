package com.mgc.letobox.happy.follow;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.ledong.lib.leto.listener.ILetoShareListener;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.bean.SHARE_PLATFORM;
import com.leto.game.base.config.FileConfig;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.MD5;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.MarqueeTextView;
import com.leto.game.base.view.recycleview.ScrollRecyclerView;
import com.leto.game.base.view.tablayout.CommonTabLayout;
import com.leto.game.base.view.tablayout.entity.TabEntity;
import com.leto.game.base.view.tablayout.listener.CustomTabEntity;
import com.leto.game.base.view.tablayout.listener.OnTabSelectListener;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.ui.HeaderViewPagerFragment;
import com.mgc.letobox.happy.follow.bean.FollowInviteBean;
import com.mgc.letobox.happy.follow.bean.FollowRankUser;
import com.mgc.letobox.happy.util.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

/*
 *
 */

public class InviteFollowFragment extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener, ILetoShareListener {

    SwipeRefreshLayout swipeLayout;

    FrameLayout ll_wechat;
    FrameLayout ll_facetoface;
    FrameLayout ll_wechatmoments;
    LinearLayout valid_friend;
    Button view_detail_invite_rule;
    Button btn_invite;
    TextView btn_copy;
    TextView tv_invide_code;
    LinearLayout btn_invite_guide;

    ScrollRecyclerView _gridview;

    CommonTabLayout rank_tab;

    boolean mUserVisibleHint = true;

    TextSwitcher tv_message;
    int _nextNews;


    ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    String titleName[] = new String[]{"周排行", "总排行"};


    FollowInviteBean _followInviteBean;

    private final long delay_time = 3000;

    FollowRankAdapter _rankAdapter;

    public List<FollowRankUser> mRankList = new ArrayList<>();


    // handler
    private Handler _handler;
    private Runnable _switchMarqueeRunnable = new Runnable() {
        @Override
        public void run() {
            int oldIdx = _nextNews;
            _nextNews++;
            _nextNews %= _followInviteBean.getMessage().size();
            if (_nextNews != oldIdx) {
                tv_message.setVisibility(VISIBLE);
                tv_message.setText(_followInviteBean.getMessage().get(_nextNews));
                _handler.postDelayed(_switchMarqueeRunnable, delay_time);
            }
        }
    };


    private ViewSwitcher.ViewFactory _textFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {

            final MarqueeTextView tv = new MarqueeTextView(getContext());
            //设置文字大小
            tv.setTextSize(11);
            TextPaint paint = tv.getPaint();
            paint.setFakeBoldText(true);
            //设置文字 颜色
            tv.setTextColor(ColorUtil.parseColor("#FFFFFFFF"));
            tv.setSingleLine();
            tv.setEllipsize(TextUtils.TruncateAt.END);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;

            return tv;
        }
    };


    public static Fragment getInstance() {
        InviteFollowFragment gameListFragment = new InviteFollowFragment();
        return gameListFragment;
    }


    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mUserVisibleHint = true;
        } else {
            mUserVisibleHint = false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_follow_invite_follow, null);

        swipeLayout = mView.findViewById(R.id.refreshLayout);

        rank_tab = mView.findViewById(R.id.rank_tab);
        tv_message = mView.findViewById(R.id.tv_message);

        _handler = new Handler();

        //跑马灯效果必须加
        tv_message.setSelected(true);
        tv_message.setFactory(_textFactory);
        restartMarquee();


        ll_wechat = mView.findViewById(R.id.ll_wechat);
        ll_facetoface = mView.findViewById(R.id.ll_facetoface);
        ll_wechatmoments = mView.findViewById(R.id.ll_wechatmoments);
        btn_invite = mView.findViewById(R.id.btn_invite);
        valid_friend = mView.findViewById(R.id.valid_friend);
        view_detail_invite_rule = mView.findViewById(R.id.view_detail_invite_rule);
        btn_copy = mView.findViewById(R.id.copy);
        tv_invide_code = mView.findViewById(R.id.tv_invide_code);
        btn_invite_guide = mView.findViewById(R.id.btn_invite_guide);
        _gridview = mView.findViewById(R.id.gridview);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initData();
            }
        });


        ll_wechat.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                if (getActivity() != null) {
                    ((FollowInviteActivity) getActivity()).shareToWeChat(SHARE_PLATFORM.WEIXIN);
                }
                return true;
            }
        });

        ll_facetoface.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                FollowUtil.startFaceToFace(getActivity());
                return true;
            }
        });

        ll_wechatmoments.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                if (getActivity() != null) {
                    ((FollowInviteActivity) getActivity()).shareToWeChat(SHARE_PLATFORM.WEIXIN_CIRCLE);
                }

                return true;
            }
        });


        btn_invite.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                if (getActivity() != null) {
                    ((FollowInviteActivity) getActivity()).onShare();
                }

                return true;
            }
        });

        btn_copy.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                Context ctx = getActivity();
                ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboardManager != null) {
                    ClipData clipData = ClipData.newPlainText("leto_game_invide_code", tv_invide_code.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);
                    ToastUtil.s(ctx, "邀请码已拷贝到剪贴板");
                }
                return true;
            }
        });

        btn_invite_guide.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                FollowUtil.startFriendGuide(getActivity());
                return true;
            }
        });


        valid_friend.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                FollowUtil.startValidFriend(getActivity());
                return true;
            }
        });

        view_detail_invite_rule.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                FollowUtil.startInviteRule(getActivity());

                return true;
            }
        });
        // find views

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        _gridview.setLayoutManager(layoutManager);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        _gridview.setLayoutParams(params);

        _rankAdapter = new FollowRankAdapter(getActivity(), mRankList);
        _gridview.setAdapter(_rankAdapter);


        initData();

        initUI();


        return mView;
    }

    private void initUI() {

        for (int i = 0; i < titleName.length; i++) {
            mTabEntities.add(new TabEntity(titleName[i], 0, 0));
        }

        rank_tab.setIconVisible(false);
        rank_tab.setTabSpaceEqual(true);
        rank_tab.setDividerColor(ColorUtil.parseColor("#FFD8D8D8"));
        rank_tab.setDividerWidth(1);
        rank_tab.setDividerPadding(2);
        rank_tab.setTextBold(CommonTabLayout.TEXT_BOLD_WHEN_SELECT);
        rank_tab.setTextSelectColor(ColorUtil.parseColor("#FF8400"));
        rank_tab.setTextUnselectColor(ColorUtil.parseColor("#000000"));
        rank_tab.setTextsize(15);
        rank_tab.setTabData(mTabEntities);
        rank_tab.setCurrentTab(0);
        rank_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                swichRankTab(position);

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        swichRankTab(0);
    }


    private void restartMarquee() {
        _nextNews = 0;
        if (_followInviteBean != null && _followInviteBean.getMessage() != null && _nextNews < _followInviteBean.getMessage().size()) {
            tv_message.setCurrentText(_followInviteBean.getMessage().get(_nextNews));

            if (_handler != null) {
                _handler.removeCallbacks(_switchMarqueeRunnable);
            }

            _handler.postDelayed(_switchMarqueeRunnable, delay_time);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (_handler != null) {
            _handler.removeCallbacks(_switchMarqueeRunnable);
        }
        _handler = null;
    }


    @Override
    public void onRefresh() {
    }


    @Override
    public View getScrollableView() {
        return null;
    }

    public List<FollowRankUser> mWeekRank = new ArrayList<>();
    public List<FollowRankUser> mAllRank = new ArrayList<>();

    public void swichRankTab(int tabIndex) {

        List<FollowRankUser> rankList = null;
        if (tabIndex == 0) {
            rankList = mWeekRank;
        } else {
            rankList = mAllRank;
        }
        mRankList.clear();
        mRankList.addAll(rankList);
        _rankAdapter.notifyDataSetChanged();

    }

    public void initData() {

        FollowUtil.getInviteIndex(getActivity(), new HttpCallbackDecode<FollowInviteBean>(getActivity(), null) {
            @Override
            public void onDataSuccess(FollowInviteBean data) {
                try {
                    if (data != null) {
                        _followInviteBean = data;
                        if (getActivity() != null) {
                            ((FollowInviteActivity) getActivity()).setFlowInviteBean(data);
                        }

                        if (data != null) {
                            mWeekRank.clear();
                            mWeekRank.addAll(data.getRank_week());
                            mAllRank.clear();
                            mAllRank.addAll(data.getRank_month());
                        }

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    tv_invide_code.setText(_followInviteBean.getCode());


                                    restartMarquee();

                                    swichRankTab(0);

                                    if (!TextUtils.isEmpty(_followInviteBean.getShareUrl())) {
                                        getShareImage(_followInviteBean.getShareUrl());
                                    }

                                }
                            });
                        }


                    } else {
                        ToastUtil.s(getActivity(), "数据异常，请稍后再试");
                    }
                } catch (Throwable e) {

                }
            }

            @Override
            public void onFailure(String code, String message) {
                super.onFailure(code, message);

            }

            @Override
            public void onFinish() {
                super.onFinish();

                dismissLoading();

                if (getActivity() != null && swipeLayout != null) {
                    swipeLayout.setRefreshing(false);
                }



            }
        });

        showLoading(false);
    }

    public void getShareImage(String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = ImageUtil.getUrlBitmap(url);

                File file = new File(FileConfig.getSdCard(getActivity()), MD5.md5(url) + ".jpg");
                if (file.exists()) {
                    file.delete();
                }
                try {
                    ImageUtil.storeImage(bitmap, file.getAbsolutePath());

                } catch (Throwable e) {

                }
            }
        }).start();
    }

    @Override
    public void onStart(SHARE_PLATFORM platform) {

    }

    @Override
    public void onResult(SHARE_PLATFORM platform) {
        ToastUtil.s(getActivity(), "欢迎回来～");
    }

    @Override
    public void onError(SHARE_PLATFORM platform, Throwable throwable) {
        ToastUtil.s(getActivity(), "分享错误～");
    }

    @Override
    public void onCancel(SHARE_PLATFORM platform) {

    }


}
