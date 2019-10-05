package com.mgc.letobox.happy.circle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.ToastUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.adapter.ArticleListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.ui.ArticleDetailActivity;
import com.mgc.letobox.happy.find.ui.HeaderViewPagerFragment;
import com.mgc.letobox.happy.find.ui.RecycleViewDivider;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by DELL on 2018/7/7.
 */

public class CircleArticleFragment extends HeaderViewPagerFragment implements BaseQuickAdapter.RequestLoadMoreListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArticleListAdapter mAdapter;
    private View mView;
    private int userId;
    private int mPage = 1;
    private List<ArticleResultBean> mNewsList = new ArrayList<>();

    public static CircleArticleFragment newInstance() {
        return new CircleArticleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.circle_article_fragment, null);
        EventBus.getDefault().register(this);
        recyclerView = mView.findViewById(R.id.recyclerView);
        SmartRefreshLayout smartRefreshLayout = mView.findViewById(R.id.refreshLayout);

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.setNoMoreData(true);
                refreshLayout.finishLoadMore();
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        });

        userId = getArguments().getInt(FindConst.EXTRA_ARTICLE_TYPE);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, this.getResources().getColor(R.color.bg_common)));
        mAdapter = new ArticleListAdapter(mNewsList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.center_no_data, null);
        ((TextView) view.findViewById(R.id.tv_no_comment)).setText("暂无文章");
        mAdapter.setEmptyView(view);

        initNet();
        initView();

        return mView;
    }

    private void initNet() {
        Context ctx = getContext();
        FindApiUtil.getNewsList(ctx, userId, mPage, new HttpCallbackDecode<List<ArticleResultBean>>(ctx, null, new TypeToken<List<ArticleResultBean>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<ArticleResultBean> data) {
                if (data != null && !data.isEmpty()) {
                    mPage++;
                    mAdapter.addData(data);
                    mAdapter.loadMoreComplete();
                    if (data.size() < 10) {
                        mAdapter.loadMoreEnd(true);
                    }
                } else {
                    mAdapter.loadMoreEnd(true);
                }
            }
        });
    }

    private void initView() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.cb_follow) {
                    userFollow(mAdapter.getData().get(position).getKol().id, mAdapter.getData().get(position).getKol().isfollow == 1 ? 2 : 1, position);
                } else if (view.getId() == R.id.ll_weibo) {
                    ArticleDetailActivity.start(getActivity(), mAdapter.getData().get(position));
                }
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }

    /**
     * 关注|取消关注
     *
     * @param userId UID
     * @param type   1 关注 2取消关注
     */
    public void userFollow(final int userId, final int type, int position) {
        final Context ctx = getContext();
        FindApiUtil.followUser(ctx, userId, type, new HttpCallbackDecode<RewardResultBean>(ctx, null) {
            @Override
            public void onDataSuccess(RewardResultBean data) {
                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    if (mAdapter.getData().get(i).getKol().getId() == userId) {
                        mAdapter.getData().get(i).getKol().setIsfollow(type == 1 ? 1 : 0);
                    }
                }
                mAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new FollowEvent(userId, type == 1 ? true : false));

                if (data != null) {
                    ToastUtil.s(ctx, "关注成功");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 关注返回刷新
     *
     * @param change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowChange(FollowEvent change) {
        if (mAdapter != null && mAdapter.getData().size() != 0) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (mAdapter.getData().get(i).getKol().getId() == change.getUid()) {
                    mAdapter.getData().get(i).getKol().setIsfollow(change.isFollow() ? 1 : 0);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        initNet();
    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }
}
