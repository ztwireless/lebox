package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leto.reward.OpenRedpacketView;
import com.leto.reward.adapter.HomeRedPackageAdapter;
import com.leto.reward.constant.RewardConst;
import com.leto.reward.model.HongbaoBean;
import com.leto.reward.util.RewardApiUtil;
import com.leto.reward.widget.GridDividerItemDecoration;
import com.mgc.leto.game.base.be.bean.mgc.MgcAdBean;
import com.mgc.leto.game.base.http.OkHttpCallbackDecode;
import com.mgc.leto.game.base.main.IntegralDownloadTaskActivity;
import com.mgc.leto.game.base.mgc.bean.BaseUserRequestBean;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.DensityUtil;
import com.mgc.leto.game.base.utils.DeviceInfo;
import com.mgc.leto.game.base.utils.GlideUtil;
import com.mgc.leto.game.base.utils.LetoFileUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.OkHttpUtil;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.bean.MeModuleBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OpenRedpacketHolder extends CommonViewHolder<MeModuleBean> {

    private static final String TAG = OpenRedpacketHolder.class.getSimpleName();

    // views
    private FrameLayout _openRedpacketLayout;


    Context _ctx;
    RecyclerView mRecyclerView;

    HomeRedPackageAdapter _mRedPackageAdapter;

    List<HongbaoBean> hongbaoList = new ArrayList<>();

    public static CommonViewHolder<MeModuleBean> create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_reward_open_redpacket"), parent, false);
        return new OpenRedpacketHolder(convertView);
    }

    public OpenRedpacketHolder(View itemView) {
        super(itemView);

        // find views
        _ctx = itemView.getContext();


        mRecyclerView = itemView.findViewById(R.id.redpackage_rv);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(_ctx, 5));
        mRecyclerView.addItemDecoration(new GridDividerItemDecoration(7, DensityUtil.dip2px(_ctx, 10)));

        //从服务器获取数据
        initData();
    }

    @Override
    public void onBind(final MeModuleBean model, final int position) {
        //从服务器获取数据
        getOpenRedPacketConfig();
    }


    private void initData() {

//        //异步获取本地配置
//        LocalConfigTask dTask = new LocalConfigTask();
//        dTask.execute(mContext);


        List<HongbaoBean> hongbaoBeanList = HongbaoBean.buildFakeData();
        hongbaoList.clear();
        hongbaoList.addAll(hongbaoBeanList);
        if (_mRedPackageAdapter == null) {
            _mRedPackageAdapter = new HomeRedPackageAdapter(_ctx, hongbaoList, 0);
            mRecyclerView.setAdapter(_mRedPackageAdapter);
        }
        _mRedPackageAdapter.notifyDataSetChanged();

        //从服务器获取数据
        getOpenRedPacketConfig();

    }

    private void updateList() {
        if (_ctx != null && mRecyclerView != null) {
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    if (_mRedPackageAdapter == null) {
                        _mRedPackageAdapter = new HomeRedPackageAdapter(_ctx, hongbaoList, 0);
                        mRecyclerView.setAdapter(_mRedPackageAdapter);
                    }
                    _mRedPackageAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void loadConfig(Context context) {
        String config = LetoFileUtil.loadStringFromFile(context, RewardConst.CACHE_FILE_REDPACKET);
        if (TextUtils.isEmpty(config)) {
            return;
        }
        List<HongbaoBean> hongbaoBeanList = new Gson().fromJson(config, new TypeToken<List<HongbaoBean>>() {
        }.getType());
        if (hongbaoBeanList != null && hongbaoBeanList.size() > 0) {
            hongbaoList.clear();
            if (mRecyclerView != null)
                mRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (_mRedPackageAdapter != null) {
                            _mRedPackageAdapter.notifyDataSetChanged();
                        }
                    }
                });

            hongbaoList.addAll(hongbaoBeanList);
        }
    }


    private void saveConfig(Context context) {
        String config = new Gson().toJson(hongbaoList);
        LetoFileUtil.saveString(context, config, RewardConst.CACHE_FILE_REDPACKET);
    }

    private void getOpenRedPacketConfig() {

        BaseUserRequestBean requestBean = new BaseUserRequestBean(_ctx);
        OkHttpUtil.postMgcEncodeData(RewardApiUtil.URL_OPEN_RED_PACKET_CONFIG, new Gson().toJson(requestBean), new OkHttpCallbackDecode<List<HongbaoBean>>(new TypeToken<List<HongbaoBean>>() {
        }.getType()) {
            @Override
            public void onFailure(String code, final String msg) {
                LetoTrace.d(msg);
                if (_ctx != null && mRecyclerView != null) {
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.s(_ctx, TextUtils.isEmpty(msg) ? "获取配置失败" : msg);
                        }
                    });
                }
            }

            @Override
            public void onDataSuccess(List<HongbaoBean> data) {
                try {
                    if (data != null && data.size() > 0) {
                        hongbaoList.clear();
                        hongbaoList.addAll(data);

                        updateList();

                        saveConfig(_ctx);
                    }
                } catch (Throwable e) {

                }
            }
        });
    }
}
