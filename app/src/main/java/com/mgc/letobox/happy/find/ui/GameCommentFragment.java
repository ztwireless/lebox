package com.mgc.letobox.happy.find.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.StarBar;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.adapter.CommentAdapter;
import com.mgc.letobox.happy.find.bean.GameBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.event.CommentUpdateEvent;
import com.mgc.letobox.happy.find.event.LikeEvent;
import com.mgc.letobox.happy.find.model.Comment;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/*
 *
 */

public class GameCommentFragment extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener {
	RecyclerView recyclerView;

    //GameRatingView game_rating_view;
    ImageView iv_grade;
    ImageView iv_user_avatar;
    TextView tv_user_name;
    StarBar rbar_score;
    TextView tv_no_comment;

    LinearLayout ll_comment;

    private List<Comment> comments = new ArrayList<>();
    CommentAdapter mAdapter;

    private int mNextRequestPage = 1;
    private int PAGE_SIZE = 10;

    GameBean gameBean;

    boolean mUserVisibleHint = true;

    boolean isMoreEnd = false;
    boolean isLoading = false;

    public static Fragment getInstance(GameBean gameBean) {
        GameCommentFragment gameListFragment = new GameCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("game", gameBean);
        gameListFragment.setArguments(bundle);
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
        View mView = inflater.inflate(R.layout.fragment_game_comment, null);

        // find views
        recyclerView = mView.findViewById(R.id.recyclerView);
        tv_no_comment = mView.findViewById(R.id.tv_no_comment);

        EventBus.getDefault().register(this);
        setupUI(inflater);

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void setGameBean(GameBean gameBean) {
        this.gameBean = gameBean;
        //game_rating_view.setRating(gameBean.getScore(), gameBean.getStar_list());

        loadMyRating(gameBean.getGameid(), gameBean.getUser().getStar());

        mNextRequestPage = 1;
        if (gameBean != null) {
            getCommentList(gameBean.getGameid(), mNextRequestPage);
        }
    }

    private void setupUI(LayoutInflater inflater) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            gameBean = (GameBean) arguments.getSerializable("game");
        }

        View headerView = inflater.inflate(R.layout.layout_game_comment_header, null);
        ll_comment = headerView.findViewById(R.id.ll_comment);

        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameDetailActivity) getActivity()).writeComment();
            }
        });

        iv_user_avatar = headerView.findViewById(R.id.iv_user_avatar);
        iv_grade = headerView.findViewById(R.id.iv_grade);
        //game_rating_view = headerView.findViewById(R.id.game_rating_view);
        tv_user_name = headerView.findViewById(R.id.tv_user_name);
        rbar_score = headerView.findViewById(R.id.rbar_score);
       // game_rating_view = headerView.findViewById(R.id.game_rating_view);

        mAdapter = new CommentAdapter(getContext(), comments);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mUserVisibleHint) {
                    loadMore();
                }
            }
        }, recyclerView);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {
                if (Util.isFastClick()) {
                    Comment comment = (Comment) adapter.getData().get(position);
                    if (view.getId() == R.id.rl_like) {

                        like(position, Integer.parseInt(comment.getId()), comment.getIs_support());
                        int support = comment.getIs_support();
                        updateItemLike(comment.getIs_support(), position);

                        if (support == 0) {
                            ImageView iv_like = view.findViewById(R.id.iv_like);
                            ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.scale);
                            iv_like.startAnimation(scaleAnimation);

                            if (support == 0) {
//                        ImageView iv_like = view.findViewById(R.id.iv_like);
//                        ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(ArticleDetailActivity.this, R.anim.scale);
                                AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.like_show);
                                //iv_like.startAnimation(animationSet);
                                final ImageView anima = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.layout_like, (RelativeLayout) view, false);
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) anima.getLayoutParams();
                                params.leftMargin = DensityUtil.dip2px(getContext(), 10);
                                ((RelativeLayout) view).addView(anima);
                                anima.startAnimation(animationSet);
                                animationSet.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        ((RelativeLayout) view).removeView(anima);
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                            }
                        }
                    } else if (view.getId() == R.id.iv_avatar) {
                        KOLActivitiy.startActivity(getActivity(), Integer.parseInt(comment.getUser().getUid()));
                    }
                }
            }
        });

        mAdapter.setHeaderView(headerView);

        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, getActivity().getResources().getColor(R.color.bg_gray)));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        // 设置适配器
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        comments.clear();
        mNextRequestPage = 1;
        if (gameBean != null) {
            getCommentList(gameBean.getGameid(), mNextRequestPage);
        }
    }

    private void loadMyRating(final String gameId, int score) {
        Context ctx = getContext();
        GlideUtil.loadRoundedCorner(ctx,
            LoginManager.getPortrait(ctx),
            iv_user_avatar,
            34,
            R.mipmap.default_avatar);
        GlideUtil.load(ctx,
            gameBean.getUser().getLevel_pic(),
            iv_grade);
        tv_user_name.setText(LoginManager.getNickname(ctx));
        rbar_score.setIntegerMark(true);

        if (gameBean != null) {
            rbar_score.setMark(score);
            if (score > 0) {
                rbar_score.setIsIndicator(true);
            } else {
                rbar_score.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {
                        GameDetailActivity activity = (GameDetailActivity) getActivity();
                        activity.onStartComment(gameId, (int) rbar_score.getStarMark(), true);
                        return true;
                    }
                });
            }
        }
    }

    public void loadMore() {
        if (gameBean != null && !isMoreEnd && !isLoading) {
            getCommentList(gameBean.getGameid(), mNextRequestPage);
        }
    }

    private void setData(boolean isRefresh, List data) {

        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            mAdapter.addData(data);
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(true);
            mAdapter.loadMoreComplete();
            isMoreEnd = true;
        } else {
            mAdapter.loadMoreComplete();
            isMoreEnd = false;
        }
        mNextRequestPage++;

    }

    /**
     * 获取评论列表
     *
     * @param gameId UID
     */
    public void getCommentList(String gameId, final int page) {
        isLoading = true;
        Context ctx = getContext();
        FindApiUtil.getGameCommentList(ctx, Integer.parseInt(gameId), page, 10, new HttpCallbackDecode<List<Comment>>(getActivity(), null, new TypeToken<List<Comment>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<Comment> data) {
                if (data != null && !data.isEmpty()) {
                    if (page == 1) {
                        mAdapter.setNewData(data);
                    } else {
                        mAdapter.addData(data);
                    }
                    mAdapter.loadMoreComplete();
                    if (data.size() < PAGE_SIZE) {
                        //第一页如果不够一页就不显示没有更多数据布局
                        mAdapter.loadMoreEnd(true);
                        isMoreEnd = true;
                    } else {
                        //mAdapter.loadMoreEnd(false);
                        isMoreEnd = false;
                    }
                    mAdapter.notifyDataSetChanged();
                    mNextRequestPage++;
                } else {
                    mAdapter.loadMoreEnd(true);
                }
                //mAdapter.setEnableLoadMore(true);
                if (mAdapter.getData().size() == 0) {
                    tv_no_comment.setVisibility(View.VISIBLE);
                } else {
                    tv_no_comment.setVisibility(View.GONE);
                }
                recyclerView.requestLayout();
                if(null!=getActivity()){

                    ((GameDetailActivity)getActivity()).resetHeight(1);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                isLoading = false;
            }

            @Override
            public void onFailure(String code, String msg) {
                isLoading = false;
            }
        });
    }

    //点赞 type ==0 ; 取消点赞==1
    private void like(int position, int commentId, int type) {
        final Context ctx = getContext();
        FindApiUtil.likeGameComment(ctx, commentId, type, new HttpCallbackDecode<RewardResultBean>(ctx, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                //T.s(ArticleDetailActivity.this, "感谢参与！");

                //updateItemLike(type,position);
                if (data != null) {
                    ToastUtil.s(ctx, "点赞成功");
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                ToastUtil.s(getActivity(), msg);
            }
        });
    }

    private void updateItemLike(int type, int position) {
        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);  //有头部
        TextView tv_like = (TextView) viewHolder.getView(R.id.tv_like);
        ImageView iv_like = (ImageView) viewHolder.getView(R.id.iv_like);

        if (type == 0) {
            iv_like.setImageResource(R.mipmap.favorite_selected);
            tv_like.setTextColor(getResources().getColor(R.color.text_blue));
        } else {
            iv_like.setImageResource(R.mipmap.favorite_unselect);
            tv_like.setTextColor(getResources().getColor(R.color.text_lowgray));
        }
        int parse = Integer.parseInt(mAdapter.getData().get(position).getParse());
        tv_like.setText(String.valueOf(type == 0 ? parse + 1 : parse - 1));
        mAdapter.getData().get(position).setParse(String.valueOf(type == 0 ? parse + 1 : parse - 1));
        mAdapter.getData().get(position).setIs_support(type == 0 ? 1 : 0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentUpdate(CommentUpdateEvent change) {
        if (change.getType() == 1) {
            mNextRequestPage = 1;
            loadMore();
            if (change.getScore() == 0) {
                loadMyRating("" + change.getId(), gameBean.getUser().getStar());
            } else {
                loadMyRating("" + change.getId(), change.getScore());
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLikeUpdate(LikeEvent like) {
        if (null != mAdapter.getData()) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).getId().equalsIgnoreCase("" + like.getId())) {
                    int support = mAdapter.getData().get(i).getIs_support();
                    if (support != like.isLike()) {
                        mAdapter.getData().get(i).setIs_support(like.isLike());
                        int parse = Integer.parseInt(mAdapter.getData().get(i).getParse());
                        mAdapter.getData().get(i).setParse(String.valueOf(like.isLike() == 1 ? parse + 1 : parse - 1));
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }
}
