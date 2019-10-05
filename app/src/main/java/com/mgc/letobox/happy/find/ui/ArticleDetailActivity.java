package com.mgc.letobox.happy.find.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.ToastUtil;
import com.leto.game.base.view.StarBar;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.adapter.ArticleCommentListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleCommentListResultBean;
import com.mgc.letobox.happy.find.bean.ArticleDetailResultBean;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.dialog.RichEditDialog;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.util.FileUtils;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.MgctUtil;
import com.mgc.letobox.happy.find.util.Util;
import com.mgc.letobox.happy.find.view.FlowLikeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends AppCompatActivity {
    // Views
	RecyclerView recyclerView;
    TextView tv_title;
    ImageView iv_right;
    ImageView iv_comment;
	SmartRefreshLayout refreshLayout;
    View in_no_network;
    Button btn_retry;
    RichEditDialog mRichEditDialog;
    View in_kol;
    TextView tv_article_title, tv_name, tv_time, tv_follow_num, tv_comment_lab, tv_no_comment, tv_like;
    RelativeLayout rl_like;
    ImageView iv_grade;
    ImageView iv_avatar;
    CheckBox cb_follow;
    RelativeLayout rl_left;
    View in_loading;
    ImageView iv_loading;
    WebView webView;

    Dialog commentDialog;


    private int mPage = 1;
    private int PAGE_SIZE = 10;
    private int mType = 0;

    ArticleResultBean mArticle;

    ArticleDetailResultBean mArticleDetail;

    private ArticleCommentListAdapter mAdapter;
    private ArrayList<ArticleCommentListResultBean> arrayList = new ArrayList<>();

    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        setStatusBar();

        // find views
        recyclerView = findViewById(R.id.recyclerView);
        tv_title = findViewById(R.id.tv_title);
        iv_right = findViewById(R.id.iv_right);
        iv_comment = findViewById(R.id.iv_comment);
        refreshLayout = findViewById(R.id.refreshLayout);
        in_no_network = findViewById(R.id.in_no_network);
        btn_retry = findViewById(R.id.btn_retry);
        rl_left = findViewById(R.id.rl_left);
        in_loading = findViewById(R.id.in_loading);
        iv_loading = findViewById(R.id.iv_loading);

        initView();

        if (getIntent() != null) {

            mArticle = (ArticleResultBean) getIntent().getSerializableExtra(FindConst.EXTRA_ARTICLE);

            initData();
            in_loading.setVisibility(View.VISIBLE);
            iv_loading.startAnimation(MgctUtil.rotaAnimation());
            loadArticleData(mArticle.getNews_id());
        } else {
            finish();
        }
    }

    private void initView() {
        tv_title.setText("文章详情");

        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(R.mipmap.share);

        initOnClick();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleCommentListAdapter(this, arrayList);
        recyclerView.setAdapter(mAdapter);
        //mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        //recyclerView.addItemDecoration(new com.mgct.wallet.ui.RecycleViewDivider(this,LinearLayoutManager.VERTICAL, this.getResources().getColor(R.color.bg_common) ));


        View mView = LayoutInflater.from(this).inflate(R.layout.article_detail_header, null);

        in_kol = mView.findViewById(R.id.in_kol);
        iv_grade = mView.findViewById(R.id.iv_grade);
        tv_article_title = mView.findViewById(R.id.tv_article_title);
        iv_avatar = mView.findViewById(R.id.iv_avatar);
        cb_follow = mView.findViewById(R.id.cb_follow);
        tv_name = mView.findViewById(R.id.tv_name);
        tv_time = mView.findViewById(R.id.tv_time);
        tv_follow_num = mView.findViewById(R.id.tv_follow_num);
        tv_comment_lab = mView.findViewById(R.id.tv_comment_lab);
        tv_no_comment = mView.findViewById(R.id.tv_no_comment);
        tv_like = mView.findViewById(R.id.tv_like);
        rl_like = mView.findViewById(R.id.rl_like);

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KOLActivitiy.startActivity(ArticleDetailActivity.this, mArticle.getKol().id);
            }
        });

        if (null != mArticle && LoginManager.getMemId(this).equalsIgnoreCase("" + mArticle.getKol().id)) {
            cb_follow.setVisibility(View.GONE);
        } else {
            cb_follow.setVisibility(View.VISIBLE);
        }
        final FlowLikeView likeViewLayout = (FlowLikeView) mView.findViewById(R.id.flowLikeView);
        rl_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mArticleDetail != null) {
                    if (Util.isFastClick()) {
                        if (mArticleDetail.getIs_support() == 0) {
                            likeViewLayout.addLikeView();
                        }
                        // 进行点击事件后的逻辑操作
                        article_like(mArticle.getNews_id(), mArticleDetail.getIs_support());

                    } else {
                        if (mArticleDetail.getIs_support() == 1) {
                            likeViewLayout.addLikeView();
                        }
                    }
                }
            }
        });

        cb_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mArticleDetail && null != mArticleDetail.getKol()) {
                    if (!LoginManager.getMemId(ArticleDetailActivity.this).equalsIgnoreCase("" + mArticleDetail.getKol().id))
                        userFollow(mArticleDetail.getKol().id, mArticleDetail.getKol().isfollow == 1 ? 2 : 1);
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {
                if (Util.isFastClick()) {

                    ArticleCommentListResultBean comment = (ArticleCommentListResultBean) adapter.getData().get(position);
                    if (view.getId() == R.id.rl_like) {
                        int support = comment.getIs_support();
                        comment_like(position, Integer.parseInt(comment.getId()), comment.getIs_support());
                        updateItemLike(comment.getIs_support(), position);

                        if (support == 0) {
//                        ImageView iv_like = view.findViewById(R.id.iv_like);
//                        ScaleAnimation scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(ArticleDetailActivity.this, R.anim.scale);
                            AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(ArticleDetailActivity.this, R.anim.like_show);
                            //iv_like.startAnimation(animationSet);
                            final ImageView anima = (ImageView) LayoutInflater.from(ArticleDetailActivity.this).inflate(R.layout.layout_like, (RelativeLayout) view, false);
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) anima.getLayoutParams();
                            params.leftMargin = DensityUtil.dip2px(ArticleDetailActivity.this, 10);
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
                        KOLActivitiy.startActivity(ArticleDetailActivity.this, Integer.parseInt(comment.getUser().getUid()));
                    }
                }
            }
        });

        mAdapter.addHeaderView(mView);
        webView = mView.findViewById(R.id.webView);

        initWebView(webView);

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                loadCommentData(false, mPage);
            }
        });
    }

    private void initData() {
        if (null == mArticle) return;

        tv_article_title.setText(mArticle.getTitle());

        if (mArticle.getKol() != null) {
            GlideUtil.loadRoundedCorner(this,
                mArticle.getKol().getCover_pic(),
                iv_avatar,
                20,
                R.mipmap.default_avatar);
            Glide.with(this).load(mArticle.getKol().getLevel_pic()).into(iv_grade);

            if (LoginManager.getMemId(this).equalsIgnoreCase("" + mArticle.getKol().id)) {
                cb_follow.setVisibility(View.GONE);
            } else {
                cb_follow.setVisibility(View.VISIBLE);
            }

            cb_follow.setChecked(mArticle.getKol().isfollow == 0 ? false : true);
            tv_name.setText(mArticle.getKol().getNickname());


        }
        tv_time.setText(mArticle.getDate());
        //tv_follow_num.setText(""+mArticle.getView());

    }

    protected void setStatusBar() {
        StatusBarUtil.setTransparentForImageView(this, null);
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
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    private void initOnClick() {
        rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareView();
            }
        });

        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mRichEditDialog == null) {
                    mRichEditDialog = new RichEditDialog();
                }

                mRichEditDialog.fillDialog(ArticleDetailActivity.this, 1, new RichEditDialog.FillDialogCallBack() {
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
        });

        btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadArticleData(mArticle.getNews_id());
            }
        });
    }

    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, FindConst.IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImagePath)));
        startActivityForResult(intent, FindConst.REQUEST_CROP);
    }


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
                Uri uri = data.getData();
                if (uri != null) {
                    Log.d("MgcUpload","ready to upload");
                    mRichEditDialog.bindImage(FileUtils.getRealFilePath(this, uri));
                    //cropImage(uri);
                }
                break;

            case FindConst.REQUEST_CROP:
                Uri uri2 = data.getData();
                if (uri2 != null) {
                    mRichEditDialog.bindImage(FileUtils.getRealFilePath(this, uri2));
                }
//                avatarView.setImageURI(Uri.fromFile(new File(tempfilePath)));
//                uploadAvatar();

                break;
        }

    }

    private void loadArticleData(int id) {
        FindApiUtil.loadArticleData(this, id, new HttpCallbackDecode<ArticleDetailResultBean>(ArticleDetailActivity.this, null) {
            @Override
            public void onDataSuccess(ArticleDetailResultBean data) {
                // ToastUtil.s(ArticleDetailActivity.this, "发布成功，请等待审核！");
                mArticleDetail = data;
                updateUI(data);

                loadCommentData(true, mPage);
                if(in_no_network.getVisibility()==View.VISIBLE){
                    in_no_network.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(String code, String msg) {
                if(msg.equals("无法连接网络~")){
                    in_no_network.setVisibility(View.VISIBLE);
                }else{
                    ToastUtil.s(ArticleDetailActivity.this, msg);
                }
            }
            @Override
            public void onFinish() {
                //ToastUtil.s(ArticleDetailActivity.this, msg);
                if(in_loading.getVisibility()==View.VISIBLE){
                    in_loading.setVisibility(View.GONE);
                    iv_loading.clearAnimation();
                }
            }
        });
    }

    private void loadCommentData(final boolean isRefresh, int page) {
        FindApiUtil.loadCommentData(this, mArticle.getNews_id(), page, new HttpCallbackDecode<List<ArticleCommentListResultBean>>(ArticleDetailActivity.this, null) {
            @Override
            public void onDataSuccess(List<ArticleCommentListResultBean> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<ArticleCommentListResultBean> arr = gson.fromJson(str, new TypeToken<List<ArticleCommentListResultBean>>() {
                    }.getType());
                    setData(isRefresh, arr);
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
        });
    }

    //评论
    private void comment(String content) {
        FindApiUtil.kolComment(this, mArticle.getNews_id(), content, new HttpCallbackDecode<RewardResultBean>(ArticleDetailActivity.this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                refreshLayout.setNoMoreData(false);
                mPage = 1;
                loadCommentData(true, mPage);
                //recyclerView.smoothScrollToPosition(1);
                ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(1, 0);

                if (data != null) {
                    ToastUtil.s(ArticleDetailActivity.this, "评论成功");
                } else {
                    ToastUtil.s(ArticleDetailActivity.this, "评论成功");
                }
                if(commentDialog!=null) {
                    commentDialog.dismiss();
                    mRichEditDialog.clear();
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                ToastUtil.s(ArticleDetailActivity.this, msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                DialogUtil.dismissDialog();
            }
        });
    }

    //点赞 type ==0 ; 取消点赞==1
    private void article_like(int articleId, int type) {
        updateArticleLike(type);
        FindApiUtil.likeKOLArticle(this, articleId, type, new HttpCallbackDecode<RewardResultBean>(ArticleDetailActivity.this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {

                if (data != null) {
                    ToastUtil.s(ArticleDetailActivity.this, "点赞成功");
                }
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        });
    }

    //点赞 type ==0 ; 取消点赞==1
    private void comment_like(int position, int commentId, int type) {
        FindApiUtil.likeKOLComment(this, commentId, type, new HttpCallbackDecode<RewardResultBean>(ArticleDetailActivity.this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {

                if (data != null) {
                    ToastUtil.s(ArticleDetailActivity.this, "点赞成功");
                }
            }

            @Override
            public void onFailure(String code, String msg) {
            }
        });
    }

    private void updateArticleLike(int type) {
        if (type == 0) {
            rl_like.setBackground(getResources().getDrawable(R.mipmap.article_like_selected));
            tv_like.setTextColor(getResources().getColor(R.color.white));
            tv_like.setText(String.valueOf(mArticleDetail.getSupportCount() + 1));
            mArticleDetail.setSupportCount(mArticleDetail.getSupportCount() + 1);
            mArticleDetail.setIs_support(1);
        } else {
            rl_like.setBackground(getResources().getDrawable(R.mipmap.article_like_unselect));
            tv_like.setTextColor(getResources().getColor(R.color.text_lowgray));
            tv_like.setText(String.valueOf(mArticleDetail.getSupportCount() - 1));
            mArticleDetail.setSupportCount(mArticleDetail.getSupportCount() - 1);
            mArticleDetail.setIs_support(0);
        }
    }

    private void updateItemLike(int type, int position) {
        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position + 1);  //有头部
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


    private void setData(boolean isRefresh, List data) {
        mPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
            refreshLayout.finishLoadMore();
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
            //第一页如果不够一页就不显示没有更多数据布局
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            refreshLayout.finishRefresh();
        }
    }

    public static void start(Context context, int newsId) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(FindConst.EXTRA_ARTICLE_ID, newsId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, ArticleResultBean article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(FindConst.EXTRA_ARTICLE, article);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private void updateUI(ArticleDetailResultBean detail) {
        tv_article_title.setText(detail.getTitle());

        if (null != detail.getKol()) {
            in_kol.setVisibility(View.VISIBLE);
            GlideUtil.loadRoundedCorner(this,
                detail.getKol().getCover_pic(),
                iv_avatar,
                20,
                R.mipmap.default_avatar);
            if (LoginManager.getMemId(this).equalsIgnoreCase("" + detail.getKol().id)) {
                cb_follow.setVisibility(View.GONE);
            } else {
                cb_follow.setVisibility(View.VISIBLE);
            }

            cb_follow.setChecked(detail.getKol().isfollow == 0 ? false : true);
            tv_name.setText(detail.getKol().getNickname());

        } else {
            in_kol.setVisibility(View.GONE);
        }

        tv_time.setText(detail.getCreate_time());
        tv_follow_num.setText("" + detail.getView());

        tv_like.setText("" + detail.getSupportCount());
        rl_like.setBackground(detail.getIs_support() == 1 ? getResources().getDrawable(R.mipmap.article_like_selected) : getResources().getDrawable(R.mipmap.article_like_unselect));
        tv_like.setTextColor(detail.getIs_support() == 1 ? getResources().getColor(R.color.white) : getResources().getColor(R.color.text_lowgray));
        webView.loadData(detail.getContent(), "text/html; charset=UTF-8", null);
    }

    /**
     * 关注|取消关注
     *
     * @param userId UID
     * @param type   1 关注 2取消关注
     */
    public void userFollow(final int userId, final int type) {
        FindApiUtil.followUser(this, userId, type, new HttpCallbackDecode<RewardResultBean>(ArticleDetailActivity.this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                mArticleDetail.getKol().isfollow = type == 1 ? 1 : 0;
                cb_follow.setChecked(type == 1 ? true : false);
                EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));
                if (data != null) {
                    ToastUtil.s(ArticleDetailActivity.this, "关注成功");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                cb_follow.setChecked(type == 1 ? false : true);
            }
        });
    }

    private void showShareView() {
//        if (mArticleDetail == null) {
//            ToastUtil.s(ArticleDetailActivity.this, "数据异常，无法分享");
//            return;
//        }
//        new SharePlatformDialog().showDialog(ArticleDetailActivity.this, mArticleDetail.getShare(), new SharePlatformDialog.ConfirmDialogListener() {
//            @Override
//            public void setPlatform(SHARE_MEDIA platform) {
//                String shareImage = null;
//                String shareTitle = mArticleDetail.getTitle() + "-梦工厂游戏钱包";
//                String shareUrl = mArticleDetail.getShare_url();
//                String shareContent = mArticleDetail.getDescription();
//
//                if (null != mArticle && null != mArticle.getPics() && mArticle.getPics().size() > 0) {
//                    shareImage = mArticle.getPics().get(0);
//                } else if (!TextUtils.isEmpty(mArticleDetail.getCover_url())) {
//                    String[] url = mArticleDetail.getCover_url().split(",");
//                    if (url.length > 0) {
//                        shareImage = url[0];
//                    }
//                }
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
