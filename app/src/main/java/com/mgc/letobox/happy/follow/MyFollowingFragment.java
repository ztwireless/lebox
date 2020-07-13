package com.mgc.letobox.happy.follow;

import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.ui.HeaderViewPagerFragment;
import com.mgc.letobox.happy.find.ui.RecycleViewDivider;
import com.mgc.letobox.happy.follow.bean.FollowAwaken;
import com.mgc.letobox.happy.follow.bean.FollowingUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liu hong liang on 2016/9/26.
 */

public class MyFollowingFragment extends HeaderViewPagerFragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout;

    TextView tv_no_comment;
    private List<FollowAwaken> followingList = new ArrayList<>();
    FollowingAdapter mAdapter;

    private int mNextRequestPage = 1;
    private int PAGE_SIZE = 10;

    public static Fragment getInstance() {
        MyFollowingFragment gameListFragment = new MyFollowingFragment();
        return gameListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_follow_my_following, null);

//        // find views
        swipeLayout = mView.findViewById(R.id.refreshLayout);
        recyclerView = mView.findViewById(R.id.recyclerView);
        tv_no_comment = mView.findViewById(R.id.tv_no_comment);

        View headerView = inflater.inflate(R.layout.fragment_follow_my_following_header, null);
        ImageView iv_awake = headerView.findViewById(R.id.iv_awake);
        iv_awake.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {

                FollowUtil.startAwakeFriend(getActivity());
                return true;
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mNextRequestPage = 1;
                loadMore();
            }
        });

        setupUI(headerView);

        return mView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupUI(View headerView) {

        //评论相关
        mAdapter = new FollowingAdapter(getContext(), followingList);
        mAdapter.setShareListener((FollowInviteActivity)getActivity());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        });
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {

            }
        });

        mAdapter.setHeaderView(headerView);

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
        getFollowingList(mNextRequestPage);
    }


    /**
     * 获取我的徒弟列表
     */
    public void getFollowingList(final int page) {
        Context ctx = getContext();
        FollowUtil.getInviteApprentices(ctx, page, 10, new HttpCallbackDecode<List<FollowingUser>>(ctx, null, new TypeToken<List<FollowingUser>>() {
        }.getType()) {
            @Override
            public void onDataSuccess(List<FollowingUser> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<FollowingUser> arr = gson.fromJson(str, new TypeToken<List<FollowingUser>>() {
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
            }

            @Override
            public void onFinish() {
                super.onFinish();

                if (getActivity() != null && swipeLayout != null) {
                    swipeLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        });
    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

}
