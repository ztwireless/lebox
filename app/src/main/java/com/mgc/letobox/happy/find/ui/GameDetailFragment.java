package com.mgc.letobox.happy.find.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.recycleview.HorizontalDividerItemDecoration;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.adapter.CommentAdapter;
import com.mgc.letobox.happy.find.adapter.GameImageAdapter;
import com.mgc.letobox.happy.find.bean.GameBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.event.CommentUpdateEvent;
import com.mgc.letobox.happy.find.event.LikeEvent;
import com.mgc.letobox.happy.find.model.Comment;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.MgctUtil;
import com.mgc.letobox.happy.find.util.Util;
import com.mgc.letobox.happy.find.view.CustomRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class GameDetailFragment extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener {
    public final static String TYPE = "type";
    public final static int TYPE_LINE = 0;
    public final static int TYPE_OUT_LINE = 1;
	RecyclerView recyclerView;
//    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout swipeLayout;

    CustomRecyclerView rcyGameImgs;
    TextView expandableText;
//    @BindView(R.id.expand_text_view)
//    ExpandableTextView expandTextView;
//    @BindView(R.id.expand_collapse)
//    ImageButton expandCollapse;

    ImageView tv_expand_or_collapse;
    TextView tv_comment_more;
    TextView tv_lab_image;
    TextView tv_no_comment;
    TextView tv_version;
    TextView tv_date;
    LinearLayout ll_comment;


    private final int STATE_UNKNOW = -1;
    private final int STATE_NOT_OVERFLOW = 1;//文本行数不能超过限定行数
    private final int STATE_COLLAPSED = 2;//文本行数超过限定行数，进行折叠
    private final int STATE_EXPANDED = 3;//文本超过限定行数，被点击全文展开

    private final int MAX_LINE_COUNT = 3;

    private SparseArray<Integer> mTextStateList;
    private int expandStatus = -1;

    private List<Comment> comments = new ArrayList<>();
    CommentAdapter mAdapter;

    private LinearLayoutManager linearLayoutManager;
    int lastVisibleItem = 0;

    private ArrayList<String> images = new ArrayList<>();
    GameImageAdapter imageAdapter;

    GameBean gameBean;

    private int mNextRequestPage = 1;
    private int PAGE_SIZE = 10;

    public static Fragment getInstance(GameBean gameBean) {
        GameDetailFragment gameListFragment = new GameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("game", gameBean);
        return gameListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_game_detail, null);

        // find views
        recyclerView = mView.findViewById(R.id.recyclerView);
        rcyGameImgs = mView.findViewById(R.id.rcy_game_imgs);
        expandableText = mView.findViewById(R.id.expandable_text);
        tv_expand_or_collapse = mView.findViewById(R.id.tv_expand_or_collapse);
        tv_comment_more = mView.findViewById(R.id.tv_comment_more);
        tv_lab_image = mView.findViewById(R.id.tv_lab_image);
        tv_no_comment = mView.findViewById(R.id.tv_no_comment);
        tv_version = mView.findViewById(R.id.tv_version);
        tv_date = mView.findViewById(R.id.tv_date);
        ll_comment = mView.findViewById(R.id.ll_comment);

        Bundle arguments = getArguments();
        if (arguments != null) {
            gameBean = (GameBean) arguments.getSerializable("game");
        }

        setupUI();
        setGameBean(gameBean);

        EventBus.getDefault().register(this);

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
        if (gameBean == null) {
            return;
        }

        this.gameBean = gameBean;

        if (gameBean != null) {
            // expandTextView.setText(gameBean.getDesc());
            //expandableText.setText(gameBean.getDesc());
            setExpandText(gameBean.getDesc());
            images.clear();
            if (null != gameBean.getImage() && gameBean.getImage().size() > 0) {
                tv_lab_image.setVisibility(View.VISIBLE);
                images.addAll(gameBean.getImage());
            } else {
                tv_lab_image.setVisibility(View.GONE);
            }

            tv_version.setText(gameBean.getVersion());
            tv_date.setText(MgctUtil.timesTwo(""+gameBean.getCreate_time()));

            imageAdapter.notifyDataSetChanged();

        }
        mNextRequestPage = 1;
        getCommentList(gameBean.getGameid(), mNextRequestPage);
    }

    private void setupUI() {

        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameDetailActivity) getActivity()).writeComment();
            }
        });

        tv_comment_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameDetailActivity) getActivity()).gotoComment();
            }
        });

        imageAdapter = new GameImageAdapter(images);

//        rcyGameImgs.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        rcyGameImgs.setLayoutManager(linearLayoutManager);

        rcyGameImgs.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getContext())
            .size(10)
            .color(Color.WHITE)
            .build());

        rcyGameImgs.setHasFixedSize(true);
        rcyGameImgs.setNestedScrollingEnabled(false);
        rcyGameImgs.setAdapter(imageAdapter);

        //评论相关
        mAdapter = new CommentAdapter(getContext(), comments);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                //loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {
                if (Util.isFastClick()) {
                    Comment comment = (Comment) adapter.getData().get(position);
                    if (view.getId() == R.id.rl_like) {
                        int support = comment.getIs_support();
                        like(position, Integer.parseInt(comment.getId()), comment.getIs_support());
                        updateItemLike(comment.getIs_support(), position);
                        if (support == 0) {
//                        ImageView iv_like = view.findViewById(R.id.iv_like);
//                        ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.scale);
//                        iv_like.startAnimation(scaleAnimation);
                            AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.like_show);
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
                    } else if (view.getId() == R.id.iv_avatar) {
                        KOLActivitiy.startActivity(getActivity(), Integer.parseInt(comment.getUser().getUid()));
                    }
                }

            }
        });

        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentLayoutManager.setSmoothScrollbarEnabled(false);
        commentLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(commentLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);

        // 设置适配器
        recyclerView.setAdapter(mAdapter);

        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, getActivity().getResources().getColor(R.color.bg_gray)));


//        swipeLayout.setOnRefreshListener(this);
//        // 加载数据
//        swipeLayout.setRefreshing(false);

        loadMore();
    }


    private void setData(boolean isRefresh, List data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                if (mAdapter.getData().size() == 0) {
                    mAdapter.setNewData(data);
                } else {
                    mAdapter.addData(data);
                }
            }
        }

        mAdapter.loadMoreEnd(true);

//        if (size < PAGE_SIZE) {
//            //第一页如果不够一页就不显示没有更多数据布局
//            mAdapter.loadMoreEnd(true);
//        } else {
//            mAdapter.loadMoreComplete();
//        }
    }

    @Override
    public void onRefresh() {
        loadMore();
    }


    public void loadMore() {
        if (gameBean != null) {
            getCommentList(gameBean.getGameid(), mNextRequestPage);
        }
    }

    private void setExpandText(String strDescribe) {
        //int state=mTextStateList.get(position,STATE_UNKNOW);
        if (expandStatus == STATE_UNKNOW) {
            expandableText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回掉会调用多次，获取玩行数后记得注销监听
                    expandableText.getViewTreeObserver().removeOnPreDrawListener(this);
//          holder.content.getViewTreeObserver().addOnPreDrawListener(null);
//          如果内容显示的行数大于限定显示行数
                    if (expandableText.getLineCount() > MAX_LINE_COUNT) {
                        expandableText.setMaxLines(MAX_LINE_COUNT);//设置最大显示行数
                        tv_expand_or_collapse.setVisibility(View.VISIBLE);//让其显示全文的文本框状态为显示
                        tv_expand_or_collapse.setImageResource(R.mipmap.game_detail_expand);
                        expandStatus = STATE_COLLAPSED;
                    } else {
                        tv_expand_or_collapse.setVisibility(View.GONE);//显示全文隐藏
                        expandStatus = STATE_NOT_OVERFLOW;
                    }
                    return true;
                }
            });

            expandableText.setMaxLines(Integer.MAX_VALUE);//设置文本的最大行数，为整数的最大数值
            expandableText.setText(strDescribe);//用Util中的getContent方法获取内容
        } else {
            //      如果之前已经初始化过了，则使用保存的状态，无需在获取一次
            switch (expandStatus) {
                case STATE_NOT_OVERFLOW:
                    tv_expand_or_collapse.setVisibility(View.GONE);
                    break;
                case STATE_COLLAPSED:
                    expandableText.setMaxLines(MAX_LINE_COUNT);
                    tv_expand_or_collapse.setVisibility(View.VISIBLE);
                    tv_expand_or_collapse.setImageResource(R.mipmap.game_detail_expand);
                    break;
                case STATE_EXPANDED:
                    expandableText.setMaxLines(Integer.MAX_VALUE);
                    tv_expand_or_collapse.setVisibility(View.VISIBLE);
                    tv_expand_or_collapse.setImageResource(R.mipmap.game_detail_collape);
                    break;
            }
            expandableText.setText(strDescribe);
        }

        tv_expand_or_collapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandStatus == STATE_COLLAPSED) {
                    expandableText.setMaxLines(Integer.MAX_VALUE);
                    tv_expand_or_collapse.setImageResource(R.mipmap.game_detail_collape);
                    expandStatus = STATE_EXPANDED;
                } else if (expandStatus == STATE_EXPANDED) {
                    expandableText.setMaxLines(MAX_LINE_COUNT);
                    tv_expand_or_collapse.setImageResource(R.mipmap.game_detail_expand);
                    expandStatus = STATE_COLLAPSED;
                }
            }
        });


    }


    /**
     * 获取评论列表
     *
     * @param gameId UID
     */
    public void getCommentList(String gameId, final int page) {
        Context ctx = getContext();
        FindApiUtil.getGameCommentList(ctx, Integer.parseInt(gameId), page, 5, new HttpCallbackDecode<List<Comment>>(ctx, null, new TypeToken<List<Comment>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<Comment> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<Comment> arr = gson.fromJson(str, new TypeToken<List<Comment>>() {
                    }.getType());
                    if (page == 1) {
                        setData(true, arr);
                    } else {
                        setData(false, arr);
                    }
                } else {
                    mAdapter.loadMoreEnd(true);
                }
                if (mAdapter.getData().size() == 0) {
                    tv_no_comment.setVisibility(View.VISIBLE);
                } else {
                    tv_no_comment.setVisibility(View.GONE);
                }
                recyclerView.requestLayout();
                ((GameDetailActivity)getActivity()).resetHeight(0);
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


    //点赞 type ==0 ; 取消点赞==1
    private void like(int position, final int commentId, final int type) {
        final Context ctx = getContext();
        FindApiUtil.likeGameComment(ctx, commentId, type, new HttpCallbackDecode<RewardResultBean>(ctx, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {

                //updateItemLike(type, position);
                EventBus.getDefault().post(new LikeEvent(commentId, type == 0 ? 1 : 0));
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
        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
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
            //mNextRequestPage = 1;
            //loadMore();
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
