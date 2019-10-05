package com.mgc.letobox.happy.find.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.mgc.letobox.happy.find.adapter.ArticleListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;
import com.mgc.letobox.happy.find.bean.RewardResultBean;
import com.mgc.letobox.happy.find.event.FollowEvent;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ArticleListFragment extends Fragment {
	// extra
	public final static String TYPE = "type";
	public final static String CATEGORY = "category";

	// type constant
	public final static int TYPE_TOP = 1;
	public final static int TYPE_FOLLOW = 2;

	// views
	private RecyclerView recyclerView;
	private SmartRefreshLayout refreshLayout;
	private TextView tv_no_data;

	// model
	private int type;
	private int categoryId;
	private List<ArticleResultBean> mNewsList = new ArrayList<>();
	private static final int PAGE_SIZE = 10;
	private int mNextRequestPage = 1;
	private ArticleListAdapter mAdapter;

	public static Fragment getInstance(int type) {
		ArticleListFragment gameListFragment = new ArticleListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(TYPE, type);
		gameListFragment.setArguments(bundle);
		return gameListFragment;
	}

	public static Fragment getInstance(int type, int category) {
		ArticleListFragment gameListFragment = new ArticleListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(TYPE, type);
		bundle.putInt(CATEGORY, category);
		gameListFragment.setArguments(bundle);
		return gameListFragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// register event bus
		if(!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// unregister event bus
		if (EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_article_list, container, false);

		// find views
		Context ctx = getContext();
		recyclerView = view.findViewById(R.id.recyclerView);
		refreshLayout = view.findViewById(R.id.refreshLayout);
		tv_no_data = view.findViewById(R.id.tv_no_data);

		// get arguments
		Bundle arguments = getArguments();
		if (arguments != null) {
			type = arguments.getInt(TYPE, TYPE_TOP);
			categoryId = arguments.getInt(CATEGORY, 0);
		}

		mAdapter = new ArticleListAdapter(mNewsList);

		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				if (view.getId() == R.id.cb_follow) {
					userFollow(mAdapter.getData().get(position).getKol().id, mAdapter.getData().get(position).getKol().isfollow == 1 ? 2 : 1, position);
				} else if (view.getId() == R.id.ll_weibo) {
					ArticleDetailActivity.start(getActivity(), mAdapter.getData().get(position));
				} else if (view.getId() == R.id.iv_avatar) {
					KOLActivitiy.startActivity(getActivity(), mAdapter.getData().get(position).getKol().id);
				}
			}
		});

		mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);

		recyclerView.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, getActivity().getResources().getColor(R.color.bg_common)));

		recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
		// 设置适配器
		recyclerView.setAdapter(mAdapter);

		refreshLayout.autoRefresh();
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				mNextRequestPage = 1;
				getPageData(mNextRequestPage, true);
			}
		});
		refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				getPageData(mNextRequestPage, false);
			}
		});

		// return
		return view;
	}

	public void getPageData(final int requestPageNo, final boolean isRefresh) {
		Context ctx = getContext();
		FindApiUtil.getArticleList(ctx, type, requestPageNo, categoryId, new HttpCallbackDecode<List<ArticleResultBean>>(getActivity(), null, new TypeToken<List<ArticleResultBean>>(){}.getType()) {
			@Override
			public void onDataSuccess(List<ArticleResultBean> data) {
				setData(isRefresh, data);

				if (mAdapter.getData().size() == 0) {
					tv_no_data.setVisibility(View.VISIBLE);
				} else {
					tv_no_data.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				refreshLayout.finishRefresh();
				refreshLayout.finishLoadMore();
				refreshLayout.finishLoadMoreWithNoMoreData();
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
		FindApiUtil.followUser(ctx, userId, type, new HttpCallbackDecode<RewardResultBean>(getActivity(), null) {
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
					ToastUtil.s(getContext(), "关注成功");
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				ToastUtil.s(getContext(), msg);
			}
		});
	}

	private void setData(boolean isRefresh, List data) {
		mNextRequestPage++;
		final int size = data == null ? 0 : data.size();
		if (isRefresh) {
			mAdapter.setNewData(data);
			refreshLayout.finishRefresh();
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
			refreshLayout.finishLoadMore();
		}
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFollowChange(FollowEvent change) {

		for (int i = 0; i < mAdapter.getData().size(); i++) {
			if (mAdapter.getData().get(i).getKol().getId() == change.getUid()) {
				mAdapter.getData().get(i).getKol().setIsfollow(change.isFollow() ? 1 : 0);
			}
		}
		mAdapter.notifyDataSetChanged();
	}
}
