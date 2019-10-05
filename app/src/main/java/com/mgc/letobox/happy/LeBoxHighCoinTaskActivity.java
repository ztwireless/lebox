package com.mgc.letobox.happy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.ad.AdManager;
import com.leto.game.base.ad.bean.mgc.MgcAdBean;
import com.leto.game.base.ad.net.IAdCallback;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.MResource;
import com.leto.game.base.util.StatusBarUtil;
import com.mgc.letobox.happy.me.holder.CommonViewHolder;
import com.mgc.letobox.happy.me.holder.DownloadTaskHolder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeBoxHighCoinTaskActivity extends BaseActivity {

    // views
    private ImageView _backBtn;
    private TextView _titleLabel;
    private RecyclerView _listView;
    private View _signOutBtn;


    List<MgcAdBean> _taskList;
    IAdCallback _loadTaskListener;

    public static void start(Context context) {
        if (null != context) {
            Intent intent = new Intent(context, LeBoxHighCoinTaskActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtil.setStatusBarColor(this, ColorUtil.parseColor("#ffffff"));
        }

        // set content view
        setContentView(MResource.getIdByName(this, "R.layout.activity_high_coin_task"));

        // find views
        _backBtn = findViewById(MResource.getIdByName(this, "R.id.iv_back"));
        _titleLabel = findViewById(MResource.getIdByName(this, "R.id.tv_title"));
        _listView = findViewById(MResource.getIdByName(this, "R.id.recyclerView"));

        // back click
        _backBtn.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                finish();
                return true;
            }
        });


        // title
        _titleLabel.setText("高佣任务");

        // setup list
        _listView.setLayoutManager(new LinearLayoutManager(this));
        _listView.setAdapter(new DownloadTaskAdapter());

        // register context menu
        registerForContextMenu(_listView);

        _taskList = new ArrayList<>();
        _loadTaskListener = new IAdCallback() {
            @Override
            public void onSuccess(final List<MgcAdBean> list) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (_taskList == null) {
                            _taskList = new ArrayList<>();
                        }
                        _taskList.clear();

                        _taskList.addAll(list);

                        _listView.getAdapter().notifyDataSetChanged();

                    }
                });
            }

            @Override
            public void onFail(int code, String message) {

            }
        };


        AdManager.getInstance().loadTmDownloadAppList(this, _loadTaskListener);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }


    private class DownloadTaskAdapter extends RecyclerView.Adapter<CommonViewHolder<MgcAdBean>> {
        @NonNull
        @Override
        public CommonViewHolder<MgcAdBean> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            return DownloadTaskHolder.create(LeBoxHighCoinTaskActivity.this, parent);

        }

        @Override
        public void onBindViewHolder(@NonNull CommonViewHolder<MgcAdBean> holder, final int position) {
            holder.onBind(_taskList.get(position), position);

        }

        @Override
        public int getItemCount() {
            return null == _taskList ? 0 : _taskList.size();
        }
    }
}
