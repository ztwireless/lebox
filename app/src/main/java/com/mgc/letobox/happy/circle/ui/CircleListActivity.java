package com.mgc.letobox.happy.circle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.CircleConst;
import com.mgc.letobox.happy.circle.bean.CircleGroupsResponse;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by DELL on 2018/6/30.
 */

public class CircleListActivity extends AppCompatActivity {

	RecyclerView recyclerView;
    ImageView imageView_back;
    TextView circle_name;
    TextView ceate_circle;
	SmartRefreshLayout smartRefreshLayout;

    private CircleListAdapter mAdapter;
    private int mPage = 1;
    private int mType;

    public static void startActivity(Context mContext, int type) {
        Intent mIntent = new Intent(mContext, CircleListActivity.class);
        mIntent.putExtra(FindConst.EXTRA_ARTICLE_TYPE, type);
        mContext.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_list_activity);

        // find views
        recyclerView = findViewById(R.id.recyclerView);
        imageView_back = findViewById(R.id.imageView_back);
        circle_name = findViewById(R.id.circle_name);
        ceate_circle = findViewById(R.id.ceate_circle);
        smartRefreshLayout = findViewById(R.id.refreshLayout);

        EventBus.getDefault().register(this);
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));

        if (null != getIntent()) {
            mType = getIntent().getIntExtra(FindConst.EXTRA_ARTICLE_TYPE, 0);
            if (mType == 1) {
                circle_name.setText("热门圈子");
            } else if (mType == 2) {
                circle_name.setText("我的圈子");
            }
        }

        ceate_circle.setVisibility(View.GONE);
        if (/* TODO LoginControl.getSnsAgentId() != 0 && */LoginManager.getMemId(this) != null) {
            // LoginControl.getSnsAgentId() == 1是KOL
            if (false /* TODO LoginControl.getSnsAgentId() == 1*/) {
                ceate_circle.setVisibility(View.VISIBLE);
            }
        }
        ceate_circle.setText("创建圈子");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CircleListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        View mView = LayoutInflater.from(this).inflate(R.layout.center_no_data, null);
        ((TextView) mView.findViewById(R.id.tv_no_comment)).setText("暂无圈子");
        mAdapter.setEmptyView(mView);

        initOnClick();

        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mPage = 1;
                if (mType == 1) {
                    perpareView();
                } else if (mType == 2) {
                    myGroups();
                }
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                mPage++;
                if (mType == 1) {
                    perpareView();
                } else if (mType == 2) {
                    myGroups();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshList(String string) {
        if (string.equals(CircleConst.CREATE_CIRCLE)) {
            if (CircleListActivity.this != null) {
                mPage = 1;
                if (mType == 1) {
                    perpareView();
                } else if (mType == 2) {
                    myGroups();
                }
            }
        } else if (string.equals(CircleConst.QUIT_CIRCLE)) {
            if (CircleListActivity.this != null) {
                mPage = 1;
                if (mType == 1) {
                    perpareView();
                } else if (mType == 2) {
                    myGroups();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initOnClick() {
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CircleDetailsActivity.startActivity(CircleListActivity.this, ((CircleGroupsResponse) adapter.getData().get(position)).getId());
            }
        });

        ceate_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateCircleActivity.startActivity(CircleListActivity.this);
            }
        });
    }

    /**
     * 我的圈子列表
     */
    private void myGroups() {
        FindApiUtil.getMyGuoups(this, mPage, new HttpCallbackDecode<List<CircleGroupsResponse>>(this, null, new TypeToken<List<CircleGroupsResponse>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<CircleGroupsResponse> data) {
                if (data != null) {
                    if (!data.isEmpty()) {
                        if (mPage == 1) {
                            mAdapter.setNewData(data);
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.setNoMoreData(false);
                        } else {
                            mAdapter.addData(data);
                            smartRefreshLayout.finishLoadMore();
                            if (data.size() < 10) {
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    } else {
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                        smartRefreshLayout.setNoMoreData(true);
                    }
                    smartRefreshLayout.finishRefresh();
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
     * 热门圈子列表
     */
    private void perpareView() {
        FindApiUtil.getCircleGroups(this, mPage, new HttpCallbackDecode<List<CircleGroupsResponse>>(this, null, new TypeToken<List<CircleGroupsResponse>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<CircleGroupsResponse> data) {
                if (data != null) {
                    Gson gson = new Gson();
                    String str = gson.toJson(data);
                    List<CircleGroupsResponse> responses = gson.fromJson(str, new TypeToken<List<CircleGroupsResponse>>() {
                    }.getType());
                    if (responses != null && responses.size() != 0) {
                        if (mPage == 1) {
                            mAdapter.setNewData(responses);
                            smartRefreshLayout.finishRefresh();
                            smartRefreshLayout.setNoMoreData(false);
                        } else {
                            mAdapter.addData(responses);
                            smartRefreshLayout.finishLoadMore();
                            if (responses.size() < 10) {
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }
                        }
                    } else {
                        smartRefreshLayout.finishLoadMore();
                        smartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    if (mAdapter.getData() != null && mAdapter.getData().size() == 0) {
                        smartRefreshLayout.setNoMoreData(true);
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

    private class CircleListAdapter extends BaseQuickAdapter<CircleGroupsResponse, BaseViewHolder> {
        private Context mContext;

        public CircleListAdapter(Context mContext) {
            super(R.layout.circle_list);
            this.mContext = mContext;
        }

        @Override
        protected void convert(BaseViewHolder helper, CircleGroupsResponse item) {
            ImageView imageView = helper.getView(R.id.imageView);
            TextView textView_top = helper.getView(R.id.textView_top);
            TextView textView_down = helper.getView(R.id.textView_down);
            textView_top.setText(item.getTitle());
            textView_down.setText(item.getDetail());
            GlideUtil.loadRoundedCorner(mContext,
                item.getLogo(),
                imageView,
                6,
                R.mipmap.circle_def);
        }
    }
}
