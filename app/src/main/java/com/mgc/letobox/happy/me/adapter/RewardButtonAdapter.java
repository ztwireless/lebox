package com.mgc.letobox.happy.me.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leto.reward.LetoRewardManager;
import com.leto.reward.constant.RewardType;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.me.bean.RewardButtonBean;
import com.mgc.letobox.happy.me.holder.CommonViewHolder;

import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class RewardButtonAdapter extends RecyclerView.Adapter<RewardButtonAdapter.RewardButtonHolder> {

    Context _context;

    List<RewardButtonBean> _list;

    public RewardButtonAdapter(Context context, List<RewardButtonBean> dataList) {
        _context = context;

        _list = dataList;
    }

    @NonNull
    @Override
    public RewardButtonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(_context).inflate(MResource.getIdByName(_context, "R.layout.leto_list_item_reward_button"), parent, false);
        return new RewardButtonHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardButtonHolder holder, int position) {
        holder.onBind(_list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return _list == null ? 0 : _list.size();
    }

    public class RewardButtonHolder extends CommonViewHolder<RewardButtonBean> {

        public ImageView _buttonIcon;
        public TextView _buttonName;
        public View _buttonView;

        public RewardButtonHolder(View itemView) {
            super(itemView);

            // find views
            Context _ctx = itemView.getContext();
            _buttonView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.button_view"));
            _buttonIcon = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_icon"));
            _buttonName = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_name"));
        }

        @Override
        public void onBind(RewardButtonBean model, int position) {
            _buttonIcon.setImageResource(model.resId);
            _buttonName.setText(model.name);
            _buttonView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {



                    if(model.type == RewardType.REWARD_TYPE_FOOD){
                        ToastUtil.s(_context, "敬请期待..." );
                    }else if(model.type == RewardType.REWARD_TYPE_DRINKING){
//                        ToastUtil.s(_context, "敬请期待..." );
                        LetoRewardManager.startStep(_context);
                    }else if(model.type == RewardType.REWARD_TYPE_SLEEPING){
                        LetoRewardManager.startCharge(_context);
                    }else if(model.type == RewardType.REWARD_TYPE_TASK_NEWER){
                        ToastUtil.s(_context, "敬请期待..." );
                    }else if(model.type == RewardType.REWARD_TYPE_TASK_DAILY){
                        ToastUtil.s(_context, "敬请期待..." );
                    }

                    return true;
                }
            });
        }
    }
}
