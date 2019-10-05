package com.mgc.letobox.happy.circle.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.leto.game.base.http.HttpCallbackDecode;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.circle.bean.KOLGameListResponse;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.ui.GameDetailActivity;
import com.mgc.letobox.happy.find.ui.HeaderViewPagerFragment;
import com.mgc.letobox.happy.find.util.FindApiUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;


/**
 * Created by DELL on 2018/7/7.
 */

public class CircleGameFragment extends HeaderViewPagerFragment implements BaseQuickAdapter.RequestLoadMoreListener{

    private RecyclerView recyclerView;
    private CircleAdapter mAdapter;
    private View mView;
    private int userId;
    private int mPage = 1;

    public static CircleGameFragment newInstance() {
        return new CircleGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.circle_game_fragment, null);
        userId =  getArguments().getInt(FindConst.EXTRA_ARTICLE_TYPE);
        recyclerView =  mView.findViewById(R.id.recyclerView);

        SmartRefreshLayout smartRefreshLayout = mView.findViewById(R.id.refreshLayout);

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.setNoMoreData(true);
                refreshLayout.finishLoadMore();
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false));
        mAdapter = new CircleAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, recyclerView);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.center_no_data, null);
        ((TextView)view.findViewById(R.id.tv_no_comment)).setText("暂无游戏");
        mAdapter.setEmptyView(view);

        initNet();
        return mView;
    }

    private void initNet() {
        Context ctx = getContext();
        FindApiUtil.getUserGame(ctx, userId, mPage, new HttpCallbackDecode<List<KOLGameListResponse>>(ctx, null, new TypeToken<List<KOLGameListResponse>>(){}.getType()) {
            @Override
            public void onDataSuccess(List<KOLGameListResponse> data) {
                if (data != null && !data.isEmpty()) {
                    mPage ++;
                    mAdapter.addData(data);
                    mAdapter.loadMoreComplete();
                    if (data.size() < 10) {
                        mAdapter.loadMoreEnd(true);
                    }
                } else {
                    mAdapter.loadMoreEnd(true);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onFailure(String code, String msg) {
                super.onFailure(code, msg);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        initNet();
    }

    @Override
    public View getScrollableView() {
        return recyclerView;
    }

    public class CircleAdapter extends BaseQuickAdapter<KOLGameListResponse,BaseViewHolder> {
        private Context mContext;

        public CircleAdapter(Context mContext) {
            super(R.layout.circle_game_fragment_layout);
            this.mContext = mContext;
        }

        @Override
        protected void convert(BaseViewHolder helper, final KOLGameListResponse item) {
            LinearLayout linearClick = helper.getView(R.id.linearClick);
            TextView textView_game_name = helper.getView(R.id.textView_game_name);
            textView_game_name.setText(item.getGame_name());
            TextView textView_game_Fraction = helper.getView(R.id.textView_game_Fraction);
            textView_game_Fraction.setText(item.getStar_cnt() + "");
            TextView textView_game_describe = helper.getView(R.id.textView_game_describe);
            textView_game_describe.setText(item.getPublicity());
            ImageView imageView = helper.getView(R.id.imageView);
            GlideUtil.loadRoundedCorner(mContext,
                item.getGame_icon(),
                imageView,
                13,
                R.mipmap.circle_def);

            linearClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GameDetailActivity.start(mContext, String.valueOf(item.getGame_id()));
                }
            });
        }
    }
}
