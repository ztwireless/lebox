package com.mgc.letobox.happy.me.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.leto.game.base.bean.GameExtendInfo;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.me.holder.CoinHolder;
import com.mgc.letobox.happy.me.holder.CommonViewHolder;
import com.mgc.letobox.happy.me.holder.DailyTaskHolder;
import com.mgc.letobox.happy.me.holder.GamesHolder;
import com.mgc.letobox.happy.me.holder.HighCoinHolder;
import com.mgc.letobox.happy.me.holder.MeSigninHolder;
import com.mgc.letobox.happy.me.holder.NewerTaskHolder;
import com.mgc.letobox.happy.me.holder.OtherHolder;
import com.mgc.letobox.happy.util.LeBoxConstant;

import java.util.ArrayList;
import java.util.List;

public class MeHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MeModuleBean> _models;
    private List<Integer> _itemTypes;
    private Context mContext;


    private String mOrientation;
    private String mSrcAppId;
    private String mSrcAppPath;

    private int gc_id;
    private String gc_source;


    ViewGroup _adContainer;

    GameExtendInfo gameExtendInfo;

    Fragment _fragment;

    public MeHomeAdapter(Context ctx) {
        mContext = ctx;
        _models = new ArrayList<>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return _models==null?0:_models.size();
    }

    @Override
    public int getItemViewType(int position) {
        return _models.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LeBoxConstant.LETO_ME_MODULE_COIN:
                return CoinHolder.create(mContext, parent);
            case LeBoxConstant.LETO_ME_MODULE_SIGININ:
                return MeSigninHolder.create(mContext, parent);
            case LeBoxConstant.LETO_ME_MODULE_MYGAMES:
                return GamesHolder.create(mContext, parent);

            case LeBoxConstant.LETO_ME_MODULE_NEWER_TASK:
                return NewerTaskHolder.create(mContext, parent);
            case LeBoxConstant.LETO_ME_MODULE_DAILY_TASK:
                return DailyTaskHolder.create(mContext, parent);
            case LeBoxConstant.LETO_ME_MODULE_HIGH_COIN_TASK:
                return HighCoinHolder.create(mContext, parent);
            default:
                return OtherHolder.create(mContext, parent);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int pos) {
        CommonViewHolder vh = (CommonViewHolder) holder;
        //在bind之前配置
        vh.setAdContainer(_adContainer);

        vh.onBind(_models.get(pos), pos);
    }

    public void setModels(List<MeModuleBean> moduleList) {

        if (moduleList == null || moduleList.size() == 0) {
            return;
        }

        _models.clear();

        _models.addAll(moduleList);

    }

    public void setAdContainer(ViewGroup adContainer) {
        _adContainer = adContainer;
    }

    public void setFragment(Fragment fragment){
        _fragment = fragment;
    }
}
