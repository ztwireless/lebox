package com.mgc.letobox.happy.circle.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.StarBar;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.CircleConst;
import com.mgc.letobox.happy.circle.bean.CircleTieZiListResponse;
import com.mgc.letobox.happy.circle.bean.CreateTieZiReponse;
import com.mgc.letobox.happy.circle.bean.PostIdResponse;
import com.mgc.letobox.happy.circle.bean.ReplyCountBean;
import com.mgc.letobox.happy.circle.dialog.CommonDialog;
import com.mgc.letobox.happy.circle.util.CircleDialogUtils;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.adapter.ArticleCommentListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleCommentListResultBean;
import com.mgc.letobox.happy.find.bean.BaseResultBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.dialog.RichEditDialog;
import com.mgc.letobox.happy.find.dialog.SharePlatformDialog;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.ui.KOLActivitiy;
import com.mgc.letobox.happy.find.util.FileUtils;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.MgctUtil;
import com.mgc.letobox.happy.find.util.ShareUtil;
import com.mgc.letobox.happy.find.util.Util;
import com.mgc.letobox.happy.find.view.FlowLikeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 贴子详情
 */
public class ArticleDetailsActivity extends AppCompatActivity {

    View in_title;
	RecyclerView recyclerView;
    TextView tv_title;
    ImageView iv_right;
    ImageView iv_comment;
    ImageView iv_circle_join;
	SmartRefreshLayout refreshLayout;
    View in_no_network;
    Button btn_retry;
    View in_loading;
    ImageView iv_loading;

    //    @BindView(R.id.tv_article_title)
    TextView tv_article_title, tv_name, tv_time, tv_follow_num, tv_no_comment, tv_comment_lab;

    ImageView iv_avatar;
    ImageView iv_grade;

    CheckBox cb_follow;

    RelativeLayout rl_left;

    //@BindView(R.id.webView)
    WebView webView;

    FlowLikeView likeViewLayout;

    Dialog commentDialog;
    RichEditDialog mRichEditDialog;

    private int mPage = 1;
    private int PAGE_SIZE = 10;

    private PostIdResponse mArticleDetail;
    private int articleId = -1;

    private boolean isRefresh = true;
    private LinearLayoutManager linearLayoutManager;
    private boolean isTrue = false; // true:评论成功
    private int replyCount = 0; // 评论数量
    private RelativeLayout rl_like;
    private TextView tv_like, textView_like;

    private ArticleCommentListAdapter mAdapter;
    private ArrayList<ArticleCommentListResultBean> arrayList = new ArrayList<>();


    private String mImagePath;

    public static void start(Context context, int news) {
        Intent intent = new Intent(context, ArticleDetailsActivity.class);
        intent.putExtra(FindConst.EXTRA_BALANCE, news);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, CreateTieZiReponse createTieZiReponse) {
        Intent intent = new Intent(context, ArticleDetailsActivity.class);
        intent.putExtra(FindConst.EXTRA_AMOUNT, createTieZiReponse);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        StatusBarUtil.setTransparentForImageView(this, null);

        // find views
        in_title = findViewById(R.id.in_title);
        recyclerView = findViewById(R.id.recyclerView);
        tv_title = findViewById(R.id.tv_title);
        iv_right = findViewById(R.id.iv_right);
        iv_comment = findViewById(R.id.iv_comment);
        iv_circle_join = findViewById(R.id.iv_circle_join);
        refreshLayout = findViewById(R.id.refreshLayout);
        in_no_network = findViewById(R.id.in_no_network);
        btn_retry = findViewById(R.id.btn_retry);
        in_loading = findViewById(R.id.in_loading);
        iv_loading = findViewById(R.id.iv_loading);
        rl_left = findViewById(R.id.rl_left);

        if (getIntent() != null) {
            articleId = getIntent().getIntExtra(FindConst.EXTRA_BALANCE, -1);
            CreateTieZiReponse createTieZiReponse = (CreateTieZiReponse) getIntent().getSerializableExtra(FindConst.EXTRA_AMOUNT);
            if (createTieZiReponse != null) {
                articleId = createTieZiReponse.getPost_id();
                if (CreateTieZiActivtiy.isReWardOk && createTieZiReponse.getSymbol() != null && createTieZiReponse.getAmount() != 0) {
                    CreateTieZiActivtiy.isReWardOk = false;
                    ToastUtil.s(this, "发布成功");
                }
            }
        } else {
            finish();
        }

        tv_title.setText("帖子详情");

        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.drawer_open);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleCommentListAdapter(this, arrayList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.getItemAnimator().setChangeDuration(0);
        //mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        //recyclerView.addItemDecoration(new com.mgct.wallet.ui.RecycleViewDivider(this,LinearLayoutManager.VERTICAL, this.getResources().getColor(R.color.bg_common) ));

        View mView = LayoutInflater.from(this).inflate(R.layout.article_detail_header, null);

        tv_article_title = mView.findViewById(R.id.tv_article_title);
        iv_avatar = mView.findViewById(R.id.iv_avatar);
        iv_grade = mView.findViewById(R.id.iv_grade);
        cb_follow = mView.findViewById(R.id.cb_follow);
        tv_name = mView.findViewById(R.id.tv_name);
        tv_time = mView.findViewById(R.id.tv_time);
        tv_follow_num = mView.findViewById(R.id.tv_follow_num);
        tv_no_comment = mView.findViewById(R.id.tv_no_comment);
        tv_comment_lab = mView.findViewById(R.id.tv_comment_lab);
        tv_comment_lab.setVisibility(View.VISIBLE);
        rl_like = mView.findViewById(R.id.rl_like);
        tv_like = mView.findViewById(R.id.tv_like);

        likeViewLayout = (FlowLikeView) mView.findViewById(R.id.flowLikeView);

        cb_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArticleDetail != null) {
                    if (mArticleDetail.getUser() != null) {
                        if (LoginManager.getMemId(ArticleDetailsActivity.this) != null) {
                            if (!LoginManager.getMemId(ArticleDetailsActivity.this).equalsIgnoreCase("" + mArticleDetail.getUser().getUid())) {
                                userFollow(mArticleDetail.getUser().getUid(), mArticleDetail.getUser().getIs_follow() == 1 ? 2 : 1);
                            }
                        }
                    }
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {
                ArticleCommentListResultBean comment = (ArticleCommentListResultBean) adapter.getData().get(position);
                if (mArticleDetail != null) {
                    if (view.getId() == R.id.rl_like) {
                        if (Util.isFastClick()) {
                            textView_like = (TextView) view.findViewById(R.id.tv_like);

                            if (mArticleDetail.getIs_join() == 0) {
                                ToastUtil.s(ArticleDetailsActivity.this, "请您先加入该圈子.....");
                                return;
                            }


                            like(position, Integer.parseInt(comment.getId()), comment.getIs_support());

                            if (1 == comment.getIs_support()) {
                                //ImageView iv_like = view.findViewById(R.id.iv_like);
                                //ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(ArticleDetailsActivity.this, R.anim.scale);
                                //iv_like.startAnimation(scaleAnimation);
                                AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(ArticleDetailsActivity.this, R.anim.like_show);
                                final ImageView anima = (ImageView) LayoutInflater.from(ArticleDetailsActivity.this).inflate(R.layout.layout_like, (RelativeLayout) view, false);
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) anima.getLayoutParams();
                                params.leftMargin = DensityUtil.dip2px(ArticleDetailsActivity.this, 10);
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
                        KOLActivitiy.startActivity(ArticleDetailsActivity.this, Integer.valueOf(comment.getUser().getUid()));
                    }
                }
            }
        });

        initOnClick();

        mAdapter.addHeaderView(mView);
        webView = mView.findViewById(R.id.webView);
        initWebView(webView);

        in_loading.setVisibility(View.VISIBLE);
        iv_loading.startAnimation(MgctUtil.rotaAnimation());
        loadPostData(articleId);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                isRefresh = false;
                mPage++;
                loadCommentData(mPage);
            }
        });
    }

    private void initWebView(WebView mWebView) {
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setSupportZoom(true);
        //webSettings.setDefaultFontSize(48);
        webSettings.setTextSize(WebSettings.TextSize.SMALLER);

        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        //webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(false);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(false);
    }

    private void initOnClick() {
        rl_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mArticleDetail != null) {
                    if (mArticleDetail.getIs_join() == 0) {
                        ToastUtil.s(ArticleDetailsActivity.this, "请您先加入该圈子.....");
                        return;
                    }
                    if (Util.isFastClick()) {
                        if (Integer.parseInt(mArticleDetail.getIs_support()) == 0) {
                            likeViewLayout.addLikeView();
                        }
                        article_like(Integer.valueOf(mArticleDetail.getId()), Integer.valueOf(mArticleDetail.getIs_support()) == 0 ? 0 : 1);
                    } else {
                        if (Integer.parseInt(mArticleDetail.getIs_support()) == 1) {
                            likeViewLayout.addLikeView();
                        }
                    }
                }
            }
        });
        rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightClick();
            }
        });

        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mArticleDetail != null) {
                    if (mArticleDetail.getIs_join() == 0) {
                        ToastUtil.s(ArticleDetailsActivity.this, "请您先加入该圈子.....");
                        return;
                    }
                    if (mRichEditDialog == null) {
                        mRichEditDialog = new RichEditDialog();
                    }
                    mRichEditDialog.clear();
                    mRichEditDialog.fillDialog(ArticleDetailsActivity.this, 1, new RichEditDialog.FillDialogCallBack() {
                        @Override
                        public void textViewCreate(Dialog dialog, StarBar starBar, EditText comment, EditText titleEdit) {
                            commentDialog = dialog;
                            comment(mRichEditDialog.getContent());
                        }

                        @Override
                        public void selectPicture() {
                            Intent albumIntent = new Intent(Intent.ACTION_PICK);
                            albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FindConst.IMAGE_UNSPECIFIED);
                            startActivityForResult(albumIntent, FindConst.REQUEST_ALBUM);
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            }
        });

        iv_circle_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initJoin();
            }
        });
    }

    /**
     * 文章点赞 type ==0 ; 取消点赞==1
     *
     * @param articleId
     * @param type
     */
    private void article_like(int articleId, int type) {
        if (mArticleDetail != null) {
            if (type == 0) {
                mArticleDetail.setIs_support("1");
                rl_like.setBackground(getResources().getDrawable(R.mipmap.article_like_selected));
                tv_like.setTextColor(getResources().getColor(R.color.white));
                Integer integer = Integer.valueOf(mArticleDetail.getSupportCount()) + 1;
                tv_like.setText(integer + "");
                mArticleDetail.setSupportCount(integer + "");
            } else {
                mArticleDetail.setIs_support("0");
                rl_like.setBackground(getResources().getDrawable(R.mipmap.article_like_unselect));
                tv_like.setTextColor(getResources().getColor(R.color.text_lowgray));
                Integer integer = Integer.valueOf(mArticleDetail.getSupportCount()) - 1;
                tv_like.setText(integer + "");
                mArticleDetail.setSupportCount(integer + "");
            }

//            rl_like.setBackground(getResources().getDrawable(R.mipmap.article_like_unselect));
//            tv_like.setTextColor(getResources().getColor(R.color.text_lowgray));
//            tv_like.setText(String.valueOf(Integer.valueOf(mArticleDetail.getSupportCount()) - 1));
//            mArticleDetail.setSupportCount(String.valueOf((Integer.valueOf(mArticleDetail.getSupportCount()) - 1)));
        }

        FindApiUtil.likeCircleArticle(this, articleId, type, new HttpCallbackDecode<RewardResultBean>(this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                if (data != null) {
                    ToastUtil.s(ArticleDetailsActivity.this, "点赞成功");
                }
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        });
    }

    /**
     * 帖子详情
     *
     * @param id
     */
    private void loadPostData(int id) {
        FindApiUtil.getPostDetail(this, id, new HttpCallbackDecode<PostIdResponse>(this, null) {
            @Override
            public void onDataSuccess(PostIdResponse data) {
                if (data != null) {
                    mArticleDetail = data;
                    iv_avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KOLActivitiy.startActivity(ArticleDetailsActivity.this, mArticleDetail.getUser().getUid());
                        }
                    });
                    tv_article_title.setText(data.getTitle());
                    iv_avatar.setBackgroundResource(R.mipmap.default_avatar);

                    GlideUtil.loadRoundedCorner(ArticleDetailsActivity.this,
                        data.getUser().getPortrait(),
                        iv_avatar,
                        20,
                        R.mipmap.default_avatar);
                    GlideUtil.load(ArticleDetailsActivity.this,
                        data.getUser().getLevel_pic(),
                        iv_grade);

                    cb_follow.setChecked(data.getUser().getIs_follow() == 0 ? false : true);
                    tv_name.setText(data.getUser().getNickname());
                    tv_time.setText(data.getCreate_time());
                    tv_follow_num.setText("" + data.getView_count());
                    tv_like.setText(data.getSupportCount() + "");
                    rl_like.setBackground(Integer.valueOf(data.getIs_support()) == 1 ? getResources().getDrawable(R.mipmap.article_like_selected) : getResources().getDrawable(R.mipmap.article_like_unselect));
                    tv_like.setTextColor(Integer.valueOf(data.getIs_support()) == 1 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.text_lowgray));
                    webView.loadData(data.getContent(), "text/html; charset=UTF-8", null);

                    if (LoginManager.getMemId(ArticleDetailsActivity.this) != null) {
                        if (LoginManager.getMemId(ArticleDetailsActivity.this).equalsIgnoreCase("" + mArticleDetail.getUser().getUid())) {
                            cb_follow.setVisibility(View.GONE);
                        } else {
                            cb_follow.setVisibility(View.VISIBLE);
                        }
                    }
                    if (mArticleDetail.getIs_join() == 0) {
                        iv_comment.setVisibility(View.GONE);
                        iv_circle_join.setVisibility(View.VISIBLE);
                    } else {
                        iv_comment.setVisibility(View.VISIBLE);
                        iv_circle_join.setVisibility(View.GONE);
                    }
                    loadCommentData(mPage);
                    if (in_no_network.getVisibility() == View.VISIBLE) {
                        in_no_network.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                //T.s(ArticleDetailsActivity.this, msg);
                if (msg.equals("无法连接网络~")) {
                    if (in_no_network.getVisibility() == View.GONE) {
                        in_no_network.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.s(ArticleDetailsActivity.this, msg);
                }
            }
            @Override
            public void onFinish() {
                if(in_loading.getVisibility()==View.VISIBLE){
                    in_loading.setVisibility(View.GONE);
                    iv_loading.clearAnimation();
                }
            }
        });
    }

    /**
     * 帖子详情评论列表
     */
    private void loadCommentData(int page) {
        FindApiUtil.getPostCommentList(this, articleId, page, new HttpCallbackDecode<List<ArticleCommentListResultBean>>(this, null, new TypeToken<List<ArticleCommentListResultBean>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<ArticleCommentListResultBean> data) {
                if (data != null) {
                    tv_no_comment.setVisibility(View.GONE);
                    setData(data);
                } else {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMore();
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
                if (mAdapter.getData().size() == 0) {
                    tv_no_comment.setVisibility(View.VISIBLE);
                } else {
                    tv_no_comment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                ToastUtil.s(ArticleDetailsActivity.this, msg);
            }
        });
    }

    /**
     * 发布评论
     *
     * @param content
     */
    private void comment(String content) {
        FindApiUtil.circleComment(this, articleId, content, new HttpCallbackDecode<RewardResultBean>(this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                refreshLayout.setNoMoreData(false);
                mPage = 1;
                isRefresh = true;
                isTrue = true;
                if (mArticleDetail != null) {
                    replyCount = replyCount + 1;
                    EventBus.getDefault().post(new ReplyCountBean(Integer.valueOf(mArticleDetail.getId()), mArticleDetail.getReply_count() + replyCount));
                }
                loadCommentData(mPage);

                if (data != null) {
                    ToastUtil.s(ArticleDetailsActivity.this, "评论成功");
                }
                if (commentDialog != null) {
                    mRichEditDialog.clear();
                    commentDialog.dismiss();
                }
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        });
    }

    /**
     * 评论列表点赞 type ==0 ; 取消点赞==1
     *
     * @param position
     * @param commentId
     * @param type
     */
    private void like(int position, int commentId, int type) {
        if (textView_like != null) {
            BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position + 1);  //有头部
            TextView tv_like = (TextView) viewHolder.getView(R.id.tv_like);
            ImageView iv_like = (ImageView) viewHolder.getView(R.id.iv_like);

            if (type == 0) {

                int parse = Integer.parseInt(mAdapter.getData().get(position).getParse());
                mAdapter.getData().get(position).setParse(String.valueOf(type == 0 ? parse + 1 : parse - 1));
                mAdapter.getData().get(position).setIs_support(1);

                iv_like.setImageResource(R.mipmap.favorite_selected);
                //textView_like.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.favorite_selected), null, null, null);
                textView_like.setText((type == 0 ? parse + 1 : parse - 1) + "");
                textView_like.setTextColor(getResources().getColor(R.color.text_blue));
            } else {
                int parse = Integer.parseInt(mAdapter.getData().get(position).getParse());
                mAdapter.getData().get(position).setParse(String.valueOf(type == 0 ? parse + 1 : parse - 1));
                mAdapter.getData().get(position).setIs_support(0);
                iv_like.setImageResource(R.mipmap.favorite_unselect);
                //textView_like.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.favorite_unselect), null, null, null);
                textView_like.setText((type == 0 ? parse + 1 : parse - 1) + "");
                textView_like.setTextColor(getResources().getColor(R.color.text_lowgray));
            }

            // 取消点赞逻辑代码
//            int parse = Integer.parseInt(mAdapter.getData().get(position).getParse());
//            mAdapter.getData().get(position).setParse(String.valueOf(type == 0 ? parse + 1 : parse - 1));
//            mAdapter.getData().get(position).setIs_support(0);
//            textView_like.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.favorite_unselect), null, null, null);
//            textView_like.setText((type == 0 ? parse + 1 : parse - 1) + "");
//            textView_like.setTextColor(getResources().getColor(R.color.text_lowgray));

            FindApiUtil.likeCircleComment(this, commentId, type, new HttpCallbackDecode<RewardResultBean>(this, null) {
                @Override
                public void onDataSuccess(RewardResultBean data) {
                    if (data != null) {
                        ToastUtil.s(ArticleDetailsActivity.this, "点赞成功");
                    }
                }

                @Override
                public void onFailure(String code, String msg) {
                }
            });
        }
    }


    private void setData(List data) {
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
            refreshLayout.setNoMoreData(false);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
                refreshLayout.finishLoadMore();
            } else {
                refreshLayout.finishLoadMore();
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }

        if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
            refreshLayout.setNoMoreData(true);
            refreshLayout.finishLoadMore();
            refreshLayout.finishLoadMoreWithNoMoreData();
        }

        if (size < PAGE_SIZE) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.finishRefresh();
        }

        if (isTrue) {
            recyclerView.smoothScrollToPosition(1);
            isTrue = false;
        }
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
                mArticleDetail.getUser().is_follow = type == 1 ? 1 : 0;
//                cb_follow.setChecked(type == 1 ? true : false);
                EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));
                if (data != null) {
                    ToastUtil.s(ArticleDetailsActivity.this, "关注成功");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
//                cb_follow.setChecked(type == 1 ? false : true);
            }
        });
    }

    private void showShareView() {
        if (mArticleDetail == null) {
            ToastUtil.s(ArticleDetailsActivity.this, "数据异常，无法分享");
            return;
        }
        new SharePlatformDialog().showDialog(ArticleDetailsActivity.this, mArticleDetail.getShare(), new SharePlatformDialog.ConfirmDialogListener() {
            @Override
            public void setPlatform(SHARE_MEDIA platform) {
                String shareImage = null;
                String shareTitle = mArticleDetail.getTitle() + "-" + BaseAppUtil.getAppName(ArticleDetailsActivity.this, ArticleDetailsActivity.this.getPackageName());
                // TODO 分享什么url?
                String shareUrl = mArticleDetail.getShare_url();
                String shareContent = mArticleDetail.getSummary();
                String share_img = mArticleDetail.getShare_img();

                if (share_img != null && !share_img.isEmpty()) {
                    shareImage = share_img;
                }

                ShareUtil.shareToPlatform(ArticleDetailsActivity.this, platform, new UMShareListener() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String content = data.getStringExtra("content");
            comment(content);
        }
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
                    //cropImage(uri);
                }
                break;

            case FindConst.REQUEST_CROP:
//                avatarView.setImageURI(Uri.fromFile(new File(tempfilePath)));
//                uploadAvatar();

                break;
        }

    }

    /**
     * 加入圈子
     */
    private void initJoin() {
        FindApiUtil.joinCircle(this, Integer.parseInt(mArticleDetail.getGroup_id()), new HttpCallbackDecode<BaseResultBean>(this, null) {
            @Override
            public void onDataSuccess(BaseResultBean data) {
                EventBus.getDefault().post(CircleConst.ADD_CIRCLE);
                if (ArticleDetailsActivity.this != null) {
                    ToastUtil.s(ArticleDetailsActivity.this, "加入成功");
                }
                iv_circle_join.setVisibility(View.GONE);
                iv_comment.setVisibility(View.VISIBLE);
                mArticleDetail.setIs_join(1);
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (ArticleDetailsActivity.this != null) {
                    ToastUtil.s(ArticleDetailsActivity.this, msg);
                }
            }
        });
    }


    /**
     * 分享,编辑,删除
     */
    public void onRightClick() {

        boolean isSelf = LoginManager.getMemId(this).equals(mArticleDetail.getUid()) ? true : false;

        CircleDialogUtils.showArticlePopWindow(ArticleDetailsActivity.this, isSelf, in_title, getWindowManager().getDefaultDisplay().getWidth(), new CircleDialogUtils.PopWindowCallBack() {
            @Override
            public void popWindow1(PopupWindow popupWindow) {
                popupWindow.dismiss();
                showShareView();
            }

            @Override
            public void popWindow2(PopupWindow popupWindow) {
                popupWindow.dismiss();
                edit();
                //T.s(ArticleDetailsActivity.this, "暂未实现");
            }

            @Override
            public void popWindow3(PopupWindow popupWindow) {
                popupWindow.dismiss();

                delete();

            }
        });
    }

    private void edit() {
        if (mRichEditDialog == null) {
            mRichEditDialog = new RichEditDialog();
        }

        mRichEditDialog.fillDialog(ArticleDetailsActivity.this, 3, new RichEditDialog.FillDialogCallBack() {
            @Override
            public void textViewCreate(Dialog dialog, StarBar starBar, EditText contentEditer, EditText titleEditer) {
                commentDialog = dialog;
                uploadContent(articleId, titleEditer.getText().toString(), mRichEditDialog.getContent(), Integer.parseInt(mArticleDetail.getGroup_id()));
            }

            @Override
            public void selectPicture() {
                Intent albumIntent = new Intent(Intent.ACTION_PICK);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, FindConst.IMAGE_UNSPECIFIED);
                startActivityForResult(albumIntent, FindConst.REQUEST_ALBUM);
            }

            @Override
            public void cancel() {

            }
        });
        mRichEditDialog.setTitle(mArticleDetail.getTitle());
        mRichEditDialog.setContent(mArticleDetail.getContent_new());
    }

    private void delete() {
        CommonDialog commonDialog = new CommonDialog();
        commonDialog.showDialog(ArticleDetailsActivity.this, true, null, "确定要删除该帖子？", new CommonDialog.ConfirmDialogListener() {
            @Override
            public void ok() {
                postDel(articleId);
            }

            @Override
            public void cancel() {
            }

            @Override
            public void dismiss() {
            }
        });
    }

    //编辑贴子-图文混排
    public void uploadContent(int id, final String title, String content, int groupId) {
        DialogUtil.showDialog(this, "更新中...");
        FindApiUtil.editPost(this, id, title, content, groupId, new HttpCallbackDecode<CircleTieZiListResponse>(this, null) {
            @Override
            public void onDataSuccess(CircleTieZiListResponse data) {
                if (commentDialog != null) {
                    mRichEditDialog.clear();
                    commentDialog.dismiss();
                    commentDialog = null;
                }
                //loadPostData(articleId);
                CircleTieZiListResponse circleTieZiListResponse = new CircleTieZiListResponse();
                circleTieZiListResponse.setTitle(title);
                circleTieZiListResponse.setPost_id(articleId);
                circleTieZiListResponse.setEdtextAndDel(true);
                EventBus.getDefault().post(circleTieZiListResponse);
                ArticleDetailsActivity.start(ArticleDetailsActivity.this, articleId);
                finish();
            }

            @Override
            public void onFailure(String code, String msg) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUtil.dismissDialog();
            }
        });
    }

    /**
     * 删除帖子
     */
    private void postDel(final int postId) {
        DialogUtil.showDialog(this, "删除中...");
        FindApiUtil.deletePost(this, postId, new HttpCallbackDecode<BaseResultBean>(this, null) {
            @Override
            public void onDataSuccess(BaseResultBean data) {
                if (data != null) {
                    CircleTieZiListResponse circleTieZiListResponse = new CircleTieZiListResponse();
                    circleTieZiListResponse.setPost_id(postId);
                    circleTieZiListResponse.setEdtextAndDel(false);
                    EventBus.getDefault().post(circleTieZiListResponse);
                    finish();
                } else {
                    if (data.getMsg() != null) {
                        ToastUtil.s(ArticleDetailsActivity.this, data.getMsg());
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (ArticleDetailsActivity.this != null) {
                    ToastUtil.s(ArticleDetailsActivity.this, msg);
                }
            }
        });
    }
}
