package com.mgc.letobox.happy.circle.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mgc.letobox.happy.circle.adapter.CircleDetailsAdapter;
import com.mgc.letobox.happy.circle.adapter.CircleListAdapter;
import com.mgc.letobox.happy.circle.bean.CircleBean;
import com.mgc.letobox.happy.circle.bean.CircleGroupsResponse;
import com.mgc.letobox.happy.circle.bean.CircleTieZiListResponse;
import com.mgc.letobox.happy.circle.bean.CreateTieZiReponse;
import com.mgc.letobox.happy.circle.bean.ReplyCountBean;
import com.mgc.letobox.happy.circle.dialog.CommonDialog;
import com.mgc.letobox.happy.circle.util.CircleDialogUtils;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.bean.BaseResultBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.dialog.RichEditDialog;
import com.mgc.letobox.happy.find.dialog.SharePlatformDialog;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.ui.GameDetailActivity;
import com.mgc.letobox.happy.find.ui.KOLActivitiy;
import com.mgc.letobox.happy.find.util.FileUtils;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.find.util.ShareUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CircleDetailsActivity extends AppCompatActivity implements CircleDetailsAdapter.CircleDetailsClickListener {

    ImageView iv_game_cover;
    CheckBox cb_follow;
    TextView tv_follow_num;
    ImageView iv_avatar, iv_grade;
    TextView tv_name;
    TextView tv_time;
    RelativeLayout lay_header;
    RecyclerView topRecyclerView;
    RecyclerView mRecyclerView;
    ImageView header_imageView;
    ImageView imageView_send;
    LinearLayout linear_in_circle;
    LinearLayout linear_add_circle;
    ImageView imageView_circle_add;
    TextView textViewDetails;
    TextView textViewName;
    TextView textViewMenNub;
    TextView textViewSendTieZi;
    ImageView imageView_quanZi;
    ImageView imageView_quanZhu;
    TextView textView_tuiJian, textView_title;
    View textView_tuiJian_line;
    View textView_wanGuo_line;
    TextView textView_wanGuo;
    RelativeLayout lay_actionbar_left, lay_actionbar_right;

	SmartRefreshLayout smartRefreshLayout;
	RecyclerView recyclerView;
    View include_title;
    ImageView left_finsh;
    ImageView imageView_shar;
    ImageView iv_comment;
    ImageView iv_circle_join;

    View in_no_network;
    Button btn_retry;

    View in_loading;
    ImageView iv_loading;

    public final static String GROUPID = "GROUPID";
    private LinearLayoutManager linearLayoutManager;
    private CircleListAdapter mAdapter;
    private HeaderCircleListAdapter headerAdapter;
    private HeaderTopAdapter headerAdapterTop;
    private List<CircleTieZiListResponse> mNewsList = new ArrayList<>();
    private int groupId = 0;
    private int mPage = 1;
    private int isAdd = 0; // 1:已经入圈子 0:未加入圈子
    private CircleGroupsResponse dataDetails;
    private int numbCount = 0;
    private int mDistance = 0;
    private int maxDistance = 250*3;//当距离在[0,255]变化时，透明度在[0,255之间变化]

    Dialog mDialog;
    RichEditDialog mRichEditDialog;

    public static void startActivity(Context mContext, int groupId) {
        Intent mIntent = new Intent(mContext, CircleDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(GROUPID, groupId);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_details);

        // find views
        smartRefreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        include_title = findViewById(R.id.include_title);
        left_finsh = findViewById(R.id.left_finsh);
        imageView_shar = findViewById(R.id.imageView_shar);
        iv_comment = findViewById(R.id.iv_comment);
        iv_circle_join = findViewById(R.id.iv_circle_join);
        in_no_network = findViewById(R.id.in_no_network);
        btn_retry = findViewById(R.id.btn_retry);
        in_loading = findViewById(R.id.in_loading);
        iv_loading = findViewById(R.id.iv_loading);

        StatusBarUtil.setTransparentForImageView(this, null);

        EventBus.getDefault().register(this);

        maxDistance = DensityUtil.dip2px(this,250);

        DialogUtil.showDialog(CircleDetailsActivity.this, "正在加载中...");

        View emtiyView = LayoutInflater.from(this).inflate(R.layout.no_data_def, null);
        ((TextView) emtiyView.findViewById(R.id.tv_no_comment)).setText("暂无帖子");

        View mView = LayoutInflater.from(this).inflate(R.layout.circle_header, null);
        iv_game_cover = mView.findViewById(R.id.iv_game_cover);
        iv_grade = mView.findViewById(R.id.iv_grade);
        cb_follow = mView.findViewById(R.id.cb_follow);
        tv_follow_num = mView.findViewById(R.id.tv_follow_num);
        iv_avatar = mView.findViewById(R.id.iv_avatar);
        tv_name = mView.findViewById(R.id.tv_name);
        tv_time = mView.findViewById(R.id.tv_time);
        lay_header = mView.findViewById(R.id.lay_header);
        topRecyclerView = mView.findViewById(R.id.topRecyclerView);
        mRecyclerView = mView.findViewById(R.id.mRecyclerView);
        header_imageView = mView.findViewById(R.id.header_imageView);
        imageView_send = mView.findViewById(R.id.imageView_send);
        linear_in_circle = mView.findViewById(R.id.linear_in_circle);
        linear_add_circle = mView.findViewById(R.id.linear_add_circle);
        imageView_circle_add = mView.findViewById(R.id.imageView_circle_add);
        textViewDetails = mView.findViewById(R.id.textViewDetails);
        textViewName = mView.findViewById(R.id.textViewName);
        textViewMenNub = mView.findViewById(R.id.textViewMenNub);
        textViewSendTieZi = mView.findViewById(R.id.textViewSendTieZi);
        imageView_quanZi = mView.findViewById(R.id.imageView_quanZi);
        imageView_quanZhu = mView.findViewById(R.id.imageView_quanZhu);
        textView_tuiJian = mView.findViewById(R.id.textView_tuiJian);
        textView_tuiJian_line = mView.findViewById(R.id.textView_tuiJian_line);
        textView_wanGuo_line = mView.findViewById(R.id.textView_wanGuo_line);
        textView_wanGuo = mView.findViewById(R.id.textView_wanGuo);

        linear_in_circle.setVisibility(View.GONE);
        linear_add_circle.setVisibility(View.INVISIBLE);

        LinearLayoutManager linearLayoutManagerTop = new LinearLayoutManager(this);
        linearLayoutManagerTop.setOrientation(LinearLayoutManager.HORIZONTAL);
        topRecyclerView.setLayoutManager(linearLayoutManagerTop);
        headerAdapterTop = new HeaderTopAdapter(this);
        topRecyclerView.setAdapter(headerAdapterTop);
        headerAdapterTop.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        headerAdapter = new HeaderCircleListAdapter(this);
        mRecyclerView.setAdapter(headerAdapter);
        headerAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CircleListAdapter(mNewsList, this);
        mAdapter.setEmptyView(emtiyView);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mView);
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        lay_actionbar_left = include_title.findViewById(R.id.lay_actionbar_left);
        lay_actionbar_right = include_title.findViewById(R.id.lay_actionbar_right);
        textView_title = include_title.findViewById(R.id.textView_title);
        setSystemBarAlpha(0);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            groupId = bundle.getInt(GROUPID);
            initDetails();
        }
        tv_follow_num.setVisibility(View.GONE);
        initOnClick();
        initRefresh();

        //initCircleList();

    }

    private void initRefresh() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                initCircleList();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                initCircleList();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    boolean isTop = recyclerView.canScrollVertically(-1);
                    mDistance += dy;
                    float percent = mDistance * 1f / maxDistance;
                    int alpha = (int) (percent * 255);
                    if (!isTop) {
                        alpha = 0;
                    }
                    setSystemBarAlpha(alpha);
                    //Log.i(TAG, "distance:" + mDistance);
                    if (isAdd == 0) {
                        if (mDistance > maxDistance) {
                            iv_circle_join.setVisibility(View.VISIBLE);
                        } else {
                            iv_circle_join.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                }
            }
        });
    }

    /**
     * 设置标题栏背景透明度
     *
     * @param alpha 透明度
     */
    private void setSystemBarAlpha(int alpha) {
        if (alpha >= 250) {
            include_title.getBackground().setAlpha(255);
            if (textView_title != null) {
                textView_title.setAlpha(1);
            }
        } else {
            include_title.getBackground().setAlpha(alpha);
            if (textView_title != null) {
                textView_title.setAlpha(alpha);
            }

        }
    }

    /**
     * 编辑帖子返回刷新
     * 删除帖子返回刷新
     * isEdtextAndDel  true:编辑帖子刷新 false:删除帖子刷新
     *
     * @param response
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshEditAndDel(CircleTieZiListResponse response) {
        if (mAdapter != null && mAdapter.getData().size() != 0 && response != null) {
            if (response.isEdtextAndDel()) {
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getPost_id() == response.getPost_id()) {
                        mAdapter.getData().get(i).setTitle(response.getTitle());
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            } else {
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getPost_id() == response.getPost_id()) {
                        mAdapter.remove(i);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    /**
     * 发帖返回刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(CreateTieZiReponse data) {
        if (CircleDetailsActivity.this != null) {
            if (mAdapter != null) {
                if (mNewsList != null && mNewsList.size() != 0) {
                    mNewsList.clear();
                }
                if (textViewSendTieZi != null && dataDetails != null) {
                    numbCount = numbCount + 1;
                    textViewSendTieZi.setText("发帖 " + numbCount + "");
                }
//            if (data != null) {
//                if (data.getAmount() != 0 && data.getSymbol() != null) {
//                    new CustomDialog().showSweetDialog(CircleDetailsActivity.this, "发帖成功", "", data.getAmount(), data.getSymbol(), null);
//                }
//            }
                mPage = 1;
                initCircleList();
            }
        }
    }

    /**
     * 发表评论返回刷新
     *
     * @param replyCountBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReplyCount(ReplyCountBean replyCountBean) {
        if (mAdapter != null && mAdapter.getData().size() != 0) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).getPost_id() == replyCountBean.getPostId()) {
                    mAdapter.getData().get(i).setComment(replyCountBean.getReplyCount() + "");
                    break;
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 关注返回刷新
     *
     * @param change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowChange(FollowEvent change) {
        if (LoginManager.getMemId(this) != null) {
            if (cb_follow != null) {
                if (dataDetails != null) {
                    if (change.getUid() == Integer.valueOf(dataDetails.getUser().getUid())) {
                        cb_follow.setVisibility(View.VISIBLE);
                        cb_follow.setChecked(change.isFollow());
                    }
                }
            }
        }
        if (mAdapter != null && mAdapter.getData().size() != 0) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).getKol().getId() == change.getUid()) {
                    mAdapter.getData().get(i).getKol().setIsfollow(change.isFollow() ? 1 : 0);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initOnClick() {
        left_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView_shar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightClick();
            }
        });

        lay_actionbar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView_shar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightClick();
            }
        });

        cb_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataDetails != null) {
                    userFollow(Integer.valueOf(dataDetails.getUser().getUid()));
                }
            }
        });

        imageView_quanZi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataDetails != null) {
                    KOLActivitiy.startActivity(CircleDetailsActivity.this, dataDetails.getUid());
                }
            }
        });

        imageView_quanZhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataDetails != null) {
                    KOLActivitiy.startActivity(CircleDetailsActivity.this, dataDetails.getUid());
                }
            }
        });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataDetails != null) {
                    KOLActivitiy.startActivity(CircleDetailsActivity.this, dataDetails.getUid());
                }
            }
        });
        imageView_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateTieZiActivtiy.start(CircleDetailsActivity.this, groupId);
                if (mRichEditDialog == null) {
                    mRichEditDialog = new RichEditDialog();
                }
                mRichEditDialog.fillDialog(CircleDetailsActivity.this, 3, new RichEditDialog.FillDialogCallBack() {
                    @Override
                    public void textViewCreate(Dialog dialog, StarBar starBar, EditText editText, EditText title) {
                        mDialog = dialog;
                        uploadContent(title.getText().toString(), mRichEditDialog.getContent(), groupId, "");
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
        imageView_circle_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJoin();
            }
        });
        iv_circle_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJoin();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleDetailsActivity.start(CircleDetailsActivity.this, ((CircleTieZiListResponse) adapter.getData().get(position)).getPost_id());
            }
        });

        iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRichEditDialog == null) {
                    mRichEditDialog = new RichEditDialog();
                }
                mRichEditDialog.fillDialog(CircleDetailsActivity.this, 3, new RichEditDialog.FillDialogCallBack() {
                    @Override
                    public void textViewCreate(Dialog dialog, StarBar starBar, EditText editText, EditText title) {
                        mDialog = dialog;
                        uploadContent(title.getText().toString(), mRichEditDialog.getContent(), groupId, "");
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
                initDetails();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 关注|取消关注
     *
     * @param userId UID
     *               1 关注 2取消关注
     */
    public void userFollow(final int userId) {
        final int type = cb_follow.isChecked() ? 1 : 2;
        FindApiUtil.followUser(this, userId, type, new HttpCallbackDecode<RewardResultBean>(this, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                if (mAdapter != null) {
                    if (cb_follow.isChecked()) {
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).getKol().getId() == userId) {
                                mAdapter.getData().get(i).getKol().setIsfollow(1);
                            }
                        }
                    } else {
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).getKol().getId() == userId) {
                                mAdapter.getData().get(i).getKol().setIsfollow(0);
                            }
                        }
                    }
                    EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));
                    mAdapter.notifyDataSetChanged();
                }
                if (data != null) {
                    ToastUtil.s(CircleDetailsActivity.this, "关注成功");
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
//                if (cb_follow.isChecked()) {
//                    cb_follow.setChecked(!cb_follow.isChecked());
//                } else {
//                    cb_follow.setChecked(cb_follow.isChecked());
//                }
            }
        });
    }

    /**
     * 圈子帖子列表
     */
    private void initCircleList() {
        FindApiUtil.getCirclePostList(this, groupId, mPage, new HttpCallbackDecode<List<CircleTieZiListResponse>>(this, null, new TypeToken<List<CircleTieZiListResponse>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<CircleTieZiListResponse> data) {
                if (data != null && !data.isEmpty()) {
                    mNewsList.addAll(data);
                    if (mPage == 1) {
                        mAdapter.setNewData(data);
                        smartRefreshLayout.setNoMoreData(false);
                    } else {
                        mAdapter.addData(data);
                        smartRefreshLayout.finishLoadMore();
                        if (data.size() < 10) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else {
                    if (mPage > 1) {
                        mPage--;
                    }
                    if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                        smartRefreshLayout.setNoMoreData(false);
                    }
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        });
    }

    /**
     * 圈子详情
     */
    private void initDetails() {

        in_loading.setVisibility(View.VISIBLE);
        iv_loading.startAnimation(DialogUtil.rotaAnimation());

        FindApiUtil.getCircleDetail(this, groupId, new HttpCallbackDecode<CircleGroupsResponse>(this, null) {
            @Override
            public void onDataSuccess(CircleGroupsResponse data) {
                if (data != null) {
                    dataDetails = data;
                    textView_title.setText(data.getTitle());
                    textViewName.setText(data.getTitle());
                    textViewMenNub.setText("成员 " + data.getMember_count());
                    textViewSendTieZi.setText("发帖 " + data.getPost_count());
                    numbCount = Integer.valueOf(dataDetails.getPost_count());
                    textViewDetails.setText(data.getDetail());
					GlideUtil.loadRoundedCorner(CircleDetailsActivity.this,
						data.getLogo(),
						header_imageView,
						30,
						R.mipmap.user_def);
                    tv_name.setText(data.getUser().getNickname());
                    tv_time.setText(data.getCreate_time());
                    iv_avatar.setBackgroundResource(R.mipmap.default_avatar);
                    GlideUtil.loadRoundedCorner(CircleDetailsActivity.this,
						data.getUser().getPortrait(),
						iv_avatar,
						20,
						R.mipmap.default_avatar);
                    GlideUtil.load(CircleDetailsActivity.this,
						data.getUser().getLevel_pic(),
						iv_grade);
                    GlideUtil.load(CircleDetailsActivity.this,
						data.getBackground(),
						iv_game_cover);
                    iv_game_cover.setAlpha(0.6f);

                    if (data.getRecommend_game_list() != null && data.getRecommend_game_list().size() != 0) {
                        textView_tuiJian.setVisibility(View.VISIBLE);
                        textView_tuiJian_line.setVisibility(View.VISIBLE);
                        headerAdapterTop.setNewData(data.getRecommend_game_list());
                    } else {
                        textView_tuiJian.setVisibility(View.GONE);
                        textView_tuiJian_line.setVisibility(View.GONE);
                    }
                    if (data.getPlayed_game_list() != null && data.getPlayed_game_list().size() != 0) {
                        headerAdapter.setNewData(data.getPlayed_game_list());
                        textView_wanGuo.setVisibility(View.VISIBLE);
                        textView_wanGuo_line.setVisibility(View.VISIBLE);
                    } else {
                        textView_wanGuo.setVisibility(View.GONE);
                        textView_wanGuo_line.setVisibility(View.GONE);
                    }

                    if (LoginManager.getMemId(CircleDetailsActivity.this) != null) {
                        if (data.getUid() == Integer.valueOf(LoginManager.getMemId(CircleDetailsActivity.this))) {
                            cb_follow.setVisibility(View.GONE);
                        } else {
                            if (data.getUser().getIs_follow() == 0) { // 未关注
                                cb_follow.setChecked(false);
                            } else { // 关注
                                cb_follow.setChecked(true);
                            }
                            cb_follow.setVisibility(View.VISIBLE);
                        }
                    }

                    if (data.getIs_join() == 1) { // 已加入该圈子
                        isAdd = 1;
                        linear_add_circle.setVisibility(View.GONE);
                        linear_in_circle.setVisibility(View.VISIBLE);
                        if (LoginManager.getMemId(CircleDetailsActivity.this) != null) {
                            if (data.getUid() == Integer.valueOf(LoginManager.getMemId(CircleDetailsActivity.this))) {
                                // 如果该用户是圈,不允许他退出圈子。
                                isAdd = 0;
                            }
                        }
                        iv_comment.setVisibility(View.VISIBLE);
                    } else { // 未加入该圈子
                        isAdd = 0;
                        linear_add_circle.setVisibility(View.VISIBLE);
                        linear_in_circle.setVisibility(View.GONE);
                        iv_comment.setVisibility(View.GONE);
                    }

                    if (in_no_network.getVisibility() == View.VISIBLE) {
                        in_no_network.setVisibility(View.GONE);
                        setSystemBarAlpha(0);
                    }

                    smartRefreshLayout.autoRefresh();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (in_loading.getVisibility() == View.VISIBLE) {
                    in_loading.setVisibility(View.GONE);
                    iv_loading.clearAnimation();
                }
                DialogUtil.dismissDialog();
            }

            @Override
            public void onFailure(String code, String msg) {
                //super.onFailure(code, msg);
                if (msg.equals("无法连接网络~")) {
                    if (in_no_network.getVisibility() == View.GONE) {
                        in_no_network.setVisibility(View.VISIBLE);
                        setSystemBarAlpha(255);
                    }
                } else {
                    ToastUtil.s(CircleDetailsActivity.this, msg);
                }
            }
        });
    }

    /**
     * 举报圈子
     */
    private void initInFormCircle() {
    	DialogUtil.showDialog(this, "举报中...");
    	FindApiUtil.getInformCircle(this, groupId, new HttpCallbackDecode<CircleBean>(this, null) {
			@Override
			public void onDataSuccess(CircleBean data) {
				if (data != null) {
					if (CircleDetailsActivity.this != null) {
						ToastUtil.s(CircleDetailsActivity.this, "举报成功。");
					}
				} else {
					if (data.getMsg() != null) {
						ToastUtil.s(CircleDetailsActivity.this, data.getMsg());
					}
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (CircleDetailsActivity.this != null) {
					ToastUtil.s(CircleDetailsActivity.this, msg);
				}
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
					recyclerView.requestLayout();
					if (CircleDetailsActivity.this != null) {
						ToastUtil.s(CircleDetailsActivity.this, "删除成功。");
					}
				} else {
					if (data.getMsg() != null) {
						ToastUtil.s(CircleDetailsActivity.this, data.getMsg());
					}
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (CircleDetailsActivity.this != null) {
					ToastUtil.s(CircleDetailsActivity.this, msg);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				DialogUtil.dismissDialog();
			}
		});
    }

    /**
     * 加入圈子
     */
    private void initJoin() {
		DialogUtil.showDialog(this, "加入中...");
		FindApiUtil.joinCircle(this, groupId, new HttpCallbackDecode<BaseResultBean>(this, null) {
			@Override
			public void onDataSuccess(BaseResultBean data) {
				initDetails();
				EventBus.getDefault().post(CircleConst.ADD_CIRCLE);
				if (CircleDetailsActivity.this != null) {
					ToastUtil.s(CircleDetailsActivity.this, "加入成功");
				}
				isAdd = 1;
				iv_circle_join.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (CircleDetailsActivity.this != null) {
					ToastUtil.s(CircleDetailsActivity.this, msg);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				DialogUtil.dismissDialog();
			}
		});
    }

    /**
     * 退出圈子
     */
    private void initQuit() {
		DialogUtil.showDialog(this, "加入中...");
		FindApiUtil.quitCircle(this, groupId, new HttpCallbackDecode<BaseResultBean>(this, null) {
			@Override
			public void onDataSuccess(BaseResultBean data) {
				initDetails();
				EventBus.getDefault().post(CircleConst.QUIT_CIRCLE);
				if (CircleDetailsActivity.this != null) {
					ToastUtil.s(CircleDetailsActivity.this, "退出成功");
				}
				isAdd = 0;
				if (mDistance > 540) {
					iv_circle_join.setVisibility(View.VISIBLE);
				} else {
					iv_circle_join.setVisibility(View.GONE);
				}

			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (CircleDetailsActivity.this != null) {
					ToastUtil.s(CircleDetailsActivity.this, msg);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				DialogUtil.dismissDialog();
			}
		});
    }

    /**
     * 分享,退出,举报圈子
     */
    public void onRightClick() {
        CircleDialogUtils.showPopWindow(CircleDetailsActivity.this, isAdd, include_title, getWindowManager().getDefaultDisplay().getWidth(), new CircleDialogUtils.PopWindowCallBack() {
            @Override
            public void popWindow1(PopupWindow popupWindow) {
                popupWindow.dismiss();
                new SharePlatformDialog().showDialog(CircleDetailsActivity.this, dataDetails.getShare(), new SharePlatformDialog.ConfirmDialogListener() {
                    @Override
                    public void setPlatform(SHARE_MEDIA platform) {
                        String shareImage = null;
                        String shareTitle = dataDetails.getTitle() + "-" + BaseAppUtil.getAppName(CircleDetailsActivity.this, CircleDetailsActivity.this.getPackageName());
                        // TODO 使用什么url
                        String shareUrl = dataDetails.getShare_url();
                        String shareContent = dataDetails.getDetail();
                        if (!TextUtils.isEmpty(dataDetails.getLogo())) {
                            shareImage = dataDetails.getLogo();
                        }

                        ShareUtil.shareToPlatform(CircleDetailsActivity.this, platform, new UMShareListener() {
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
            public void popWindow2(PopupWindow popupWindow) {
                popupWindow.dismiss();
                initInFormCircle();
            }

            @Override
            public void popWindow3(PopupWindow popupWindow) {
                popupWindow.dismiss();
                CommonDialog commonDialog = new CommonDialog();
                commonDialog.showDialog(CircleDetailsActivity.this, true, null, "确定退出该圈子？", new CommonDialog.ConfirmDialogListener() {
                    @Override
                    public void ok() {
                        initQuit();
                    }

                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void dismiss() {
                    }
                });
            }
        });
    }


    @Override
    public void onClick(int pos, CircleTieZiListResponse news) {
        ArticleDetailsActivity.start(this, news.getPost_id());
    }

    @Override
    public void onShowCircle(ImageView imageView, int pos, int editext, int detele, final CircleTieZiListResponse response) {
//        if (mAdapter != null && mAdapter.getData().size() != 0) {
//            int height = recyclerView.getLayoutManager().getChildAt(0).getHeight();
//
//        }
        CircleDialogUtils.showCircle(CircleDetailsActivity.this, imageView, editext, detele, new CircleDialogUtils.ShowCircleCallBack() {
            @Override
            public void BianJi() {
                // CircleEditActivity.start(CircleDetailsActivity.this, groupId, response);

                if (mRichEditDialog == null) {
                    mRichEditDialog = new RichEditDialog();
                }

                mRichEditDialog.fillDialog(CircleDetailsActivity.this, 3, new RichEditDialog.FillDialogCallBack() {
                    @Override
                    public void textViewCreate(Dialog dialog, StarBar starBar, EditText contentEditer, EditText titleEditer) {
                        mDialog = dialog;
                        uploadContent(response.getPost_id(), titleEditer.getText().toString(), mRichEditDialog.getContent(), groupId);

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
                mRichEditDialog.setTitle(response.getTitle());
                mRichEditDialog.setContent(response.getContent_new());
            }

            @Override
            public void Detele() {
                CommonDialog commonDialog = new CommonDialog();
                commonDialog.showDialog(CircleDetailsActivity.this, true, null, "确定要删除该帖子？", new CommonDialog.ConfirmDialogListener() {
                    @Override
                    public void ok() {
                        postDel(response.getPost_id());
                    }

                    @Override
                    public void cancel() {
                    }

                    @Override
                    public void dismiss() {
                    }
                });
            }
        });
    }

    /**
     * 列表关注取消关注刷新
     *
     * @param isCheck
     * @param cb_follow
     * @param userId
     * @param pos
     */
    @Override
    public void onCheckBox(final boolean isCheck, CheckBox cb_follow, final int userId, int pos) {
        final int type = isCheck ? 1 : 2;
        FindApiUtil.followUser(this, userId, type, new HttpCallbackDecode<Object>(this, null) {
            @Override
            public void onDataSuccess(Object data) {
                if (mAdapter != null) {
                    if (isCheck) {
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).getKol().getId() == userId) {
                                mAdapter.getData().get(i).getKol().setIsfollow(1);
                            }
                        }
                    } else {
                        for (int i = 0; i < mAdapter.getData().size(); i++) {
                            if (mAdapter.getData().get(i).getKol().getId() == userId) {
                                mAdapter.getData().get(i).getKol().setIsfollow(0);
                            }
                        }
                    }
                    EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));
                    mAdapter.notifyDataSetChanged();
                }
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


    private class HeaderCircleListAdapter extends BaseQuickAdapter<CircleGroupsResponse.Played_game_list, BaseViewHolder> {
        private Context mContext;

        public HeaderCircleListAdapter(Context mContext) {
            super(R.layout.circle_details_adapter);
            this.mContext = mContext;
        }

        @Override
        protected void convert(BaseViewHolder helper, final CircleGroupsResponse.Played_game_list item) {
            ImageView imageView = helper.getView(R.id.imageView);

            GlideUtil.loadRoundedCorner(mContext,
                item.getGame_icon(),
                imageView,
                10);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameDetailActivity.start(mContext, String.valueOf(item.getGame_id()));
                }
            });
        }
    }

    private class HeaderTopAdapter extends BaseQuickAdapter<CircleGroupsResponse.Recommend_game_list, BaseViewHolder> {
        private Context mContext;

        public HeaderTopAdapter(Context mContext) {
            super(R.layout.circle_details_adapter);
            this.mContext = mContext;
        }

        @Override
        protected void convert(BaseViewHolder helper, final CircleGroupsResponse.Recommend_game_list item) {
            ImageView imageView = helper.getView(R.id.imageView);

            GlideUtil.loadRoundedCorner(mContext,
                item.getGame_icon(),
                imageView,
                10,
                R.mipmap.circle_def);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameDetailActivity.start(mContext, String.valueOf(item.getGame_id()));
                }
            });
        }
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

    //发布贴子
    public void uploadContent(String title, String content, int groupId, String attach_id) {
        DialogUtil.showDialog(this, "发布中...");
        FindApiUtil.putRichPost(this, title, content, groupId, new HttpCallbackDecode<CreateTieZiReponse>(this, null) {
            @Override
            public void onDataSuccess(CreateTieZiReponse data) {
                if (mDialog != null) {
                    mRichEditDialog.clear();
                    mDialog.dismiss();
                }
                if (data != null) {
                    ArticleDetailsActivity.startActivity(CircleDetailsActivity.this, data);
                }
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

    //编辑贴子-图文混排
    public void uploadContent(final int id, final String title, String content, int groupId) {
        DialogUtil.showDialog(this, "更新中...");
        FindApiUtil.editPost(this, id, title, content, groupId, new HttpCallbackDecode<CircleTieZiListResponse>(this, null) {
            @Override
            public void onDataSuccess(CircleTieZiListResponse data) {
                if (mDialog != null) {
                    mRichEditDialog.clear();
                    mDialog.dismiss();
                }
                CircleTieZiListResponse circleTieZiListResponse = new CircleTieZiListResponse();
                circleTieZiListResponse.setTitle(title);
                circleTieZiListResponse.setPost_id(id);
                circleTieZiListResponse.setEdtextAndDel(true);
                EventBus.getDefault().post(circleTieZiListResponse);
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
}

