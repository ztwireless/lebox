package com.mgc.letobox.happy.circle.ui;

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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.DialogUtil;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.CircleConst;
import com.mgc.letobox.happy.circle.bean.CircleGroupsResponse;
import com.mgc.letobox.happy.circle.view.MyGridView;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.mgc.letobox.happy.circle.util.ViewCacheManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CircleFragment extends Fragment {

	SwipeRefreshLayout swipeRefreshLayout;
	LinearLayout circle_linearLayout;
	GridLayout gridLayoutBottom;
	TextView textView_up_circle;
	TextView textView_down_circle;
	TextView textView_down_refresh;
	TextView textView_down;
	RecyclerView recyclerView;
	LinearLayout circle_fragment_come_on;

	private List<CircleGroupsResponse> responseList = new ArrayList<>();
	private List<CircleGroupsResponse> mGroupsList = new ArrayList<>();
	private CircleListAdapter mAdapter;
	private int mPage = 1;

	public static Fragment newInstance() {
		CircleFragment gameListFragment = new CircleFragment();
		return gameListFragment;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_circle, container, false);

		// find views
		swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
		circle_linearLayout = view.findViewById(R.id.circle_linearLayout);
		gridLayoutBottom = view.findViewById(R.id.gridLayoutBottom);
		textView_up_circle = view.findViewById(R.id.textView_up_circle);
		textView_down_circle = view.findViewById(R.id.textView_down_circle);
		textView_down_refresh = view.findViewById(R.id.textView_down_refresh);
		textView_down = view.findViewById(R.id.textView_down);
		recyclerView = view.findViewById(R.id.recyclerView);
		circle_fragment_come_on = view.findViewById(R.id.circle_fragment_come_on);

		EventBus.getDefault().register(this);
		textView_up_circle.setText("我的圈子");
		textView_down_circle.setText("热门圈子");

		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		mAdapter = new CircleListAdapter(getActivity());
		mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
		recyclerView.setAdapter(mAdapter);
		swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.refresh_blue), getResources().getColor(R.color.refresh_blue), getResources().getColor(R.color.refresh_blue), getResources().getColor(R.color.refresh_blue), getResources().getColor(R.color.refresh_blue));

		setupUI();
		initOnClick();

		// return
		return view;
	}

	private void initOnClick() {
		// 热门圈子查看更多
		textView_down.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CircleListActivity.startActivity(getContext(), 1);
			}
		});

		// 热门圈子换一批
		textView_down_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DialogUtil.showDialog(getActivity(), "正在加载中....");
				mPage  ++;
				perpareView();
			}
		});

		// 进入我的圈子列表
		circle_fragment_come_on.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CircleListActivity.startActivity(getContext(), 2);
			}
		});

		mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				if (((CircleGroupsResponse) adapter.getData().get(position)).getItemType() == 1) {
					CircleListActivity.startActivity(getActivity(),1);
				} else {
					CircleDetailsActivity.startActivity(getActivity(),((CircleGroupsResponse) adapter.getData().get(position)).getId());
				}
			}
		});
	}

	private void setupUI() {
		myGroups();
		perpareView();
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				myGroups();
				perpareView();
			}
		});
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void refreshList(String string) {
		if (string.equals(CircleConst.CREATE_CIRCLE)) {
			if (getActivity() != null) {
				myGroups();
			}
		} else if (string.equals(CircleConst.ADD_CIRCLE)) {
			if (getActivity() != null) {
				myGroups();
			}
		} else if (string.equals(CircleConst.QUIT_CIRCLE)) {
			if (getActivity() != null) {
				myGroups();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 我的圈子列表
	 */
	private void myGroups() {
		Context ctx = getContext();
		FindApiUtil.getMyGuoups(ctx, 1, new HttpCallbackDecode<List<CircleGroupsResponse>>(ctx, null, new TypeToken<List<CircleGroupsResponse>>(){}.getType()) {
			@Override
			public void onDataSuccess(List<CircleGroupsResponse> data) {
				if (swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(false);
				}
				if (data != null) {
					mGroupsList.clear();
					CircleGroupsResponse circleGroupsResponse = new CircleGroupsResponse();
					circleGroupsResponse.setItemType(1);
					mGroupsList.add(circleGroupsResponse);
					if (!data.isEmpty()) {
						for (int i = 0; i < data.size(); i++) {
							mGroupsList.add(data.get(i));
						}
					}
					mAdapter.setNewData(mGroupsList);
				} else {
					mGroupsList.clear();
					CircleGroupsResponse circleGroupsResponse = new CircleGroupsResponse();
					circleGroupsResponse.setItemType(1);
					mGroupsList.add(circleGroupsResponse);
					mAdapter.setNewData(mGroupsList);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(false);
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(false);
				}
			}
		});
	}

	/**
	 * 热门圈子换一批
	 */
	private void perpareView() {
		Context ctx = getContext();
		FindApiUtil.getHotGroups(ctx, mPage, new HttpCallbackDecode<List<CircleGroupsResponse>>(ctx, null, new TypeToken<List<CircleGroupsResponse>>(){}.getType()) {
			@Override
			public void onDataSuccess(List<CircleGroupsResponse> data) {
				if (swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(false);
				}
				if (data != null) {
					responseList.clear();
					if (!data.isEmpty()) {
						textView_down.setVisibility(View.VISIBLE);
						if (data.size() >= 6) {
							for (int i = 0; i < 6; i++) {
								responseList.add(data.get(i));
							}
						} else {
							for (int i = 0; i < data.size(); i++) {
								responseList.add(data.get(i));
							}
						}
						initViewData(responseList, gridLayoutBottom);
					} else {
						textView_down.setVisibility(View.GONE);
					}
				} else {
					textView_down.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				DialogUtil.dismissDialog();
				if (swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(false);
				}
			}

			@Override
			public void onFailure(String code, String msg) {
				super.onFailure(code, msg);
				if (swipeRefreshLayout.isRefreshing()) {
					swipeRefreshLayout.setRefreshing(false);
				}
			}
		});
	}

	private void initViewData(final List<CircleGroupsResponse> mStrings, final GridLayout mRootFloorLayout) {
		if (mStrings == null || mStrings.size() == 0) {
			mRootFloorLayout.setVisibility(View.GONE);
			return;
		}
		mRootFloorLayout.setVisibility(View.VISIBLE);
		mRootFloorLayout.setColumnCount(2);
		final ViewCacheManager<GridLayout> mCacheManager = new ViewCacheManager<>();
		mCacheManager.setOnCacheListener(new ViewCacheManager.onCacheListener() {
			@Override
			public View onAddView(int position) {
				MyGridView mView = new MyGridView(getActivity());
				GridLayout.LayoutParams mLayoutParams = new GridLayout.LayoutParams();
				mLayoutParams.width = (mRootFloorLayout.getResources().getDisplayMetrics().widthPixels) / 2;
				mView.setLayoutParams(mLayoutParams);
				return mView;
			}

			@Override
			public void onDelete(int position) {
			}

			@Override
			public void onBindView(final int position, Object view) {
				((MyGridView) view).setData(mStrings.get(position).getTitle(), mStrings.get(position).getUser().getNickname(), mStrings.get(position).getLogo());
				((View) view).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						CircleDetailsActivity.startActivity(getActivity(), mStrings.get(position).getId());
					}
				});
			}
		});
		mCacheManager.onRefresh(mRootFloorLayout, mStrings.size());
	}

	private class CircleListAdapter extends BaseQuickAdapter<CircleGroupsResponse, BaseViewHolder> {
		private Context mContext;

		public CircleListAdapter(Context mContext) {
			super(R.layout.circle_fragment_adapter);
			this.mContext = mContext;
		}

		@Override
		protected void convert(BaseViewHolder helper, CircleGroupsResponse item) {
			TextView textView_game_name = helper.getView(R.id.textView_game_name);
			TextView textView_koL_name = helper.getView(R.id.textView_koL_name);
			ImageView imageView = helper.getView(R.id.imageView);

			LinearLayout linearLayout_bottom = helper.getView(R.id.linearLayout_bottom);
			LinearLayout linearLayout_top = helper.getView(R.id.linearLayout_top);
			TextView head_game_name = helper.getView(R.id.head_game_name);

			if (item.getItemType() == 1) {
				linearLayout_top.setVisibility(View.VISIBLE);
				linearLayout_bottom.setVisibility(View.GONE);
				head_game_name.setText("加入圈子");
			} else {
				linearLayout_top.setVisibility(View.GONE);
				linearLayout_bottom.setVisibility(View.VISIBLE);
				GlideUtil.loadRoundedCorner(mContext,
					item.getLogo(),
					imageView,
					5);

				textView_game_name.setText(item.getTitle());
				textView_koL_name.setText(item.getUser().getNickname());
			}
		}
	}
}
