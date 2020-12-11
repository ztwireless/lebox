package com.mgc.letobox.happy.me.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.leto.reward.LetoRewardManager;
import com.leto.reward.constant.RewardConst;
import com.mgc.leto.game.base.api.ApiContainer;
import com.mgc.leto.game.base.api.adpush.PushAppManager;
import com.mgc.leto.game.base.be.AdConst;
import com.mgc.leto.game.base.be.net.IAdCallback;
import com.mgc.leto.game.base.event.DataRefreshEvent;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.interfaces.IApiCallback;
import com.mgc.leto.game.base.mgc.MGCConst;
import com.mgc.leto.game.base.mgc.bean.AddCoinResultBean;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.utils.ToastUtil;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.IRewardAdRequest;
import com.mgc.letobox.happy.me.IRewardAdResult;
import com.mgc.letobox.happy.me.bean.RewardChatRedpacketBean;
import com.mgc.letobox.happy.me.holder.CommonViewHolder;
import com.mgc.letobox.happy.view.CustomRotateAnim;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class RewardChatRedpacketAdapter extends RecyclerView.Adapter<RewardChatRedpacketAdapter.RewardChatRedpacketHolder> implements ApiContainer.IApiResultListener {

    Context _context;

    List<RewardChatRedpacketBean> _list;

    public IRewardAdRequest getRewardAdRequest() {
        return _rewardAdRequest;
    }

    public void setRewardAdRequest(IRewardAdRequest _rewardAdRequest) {
        this._rewardAdRequest = _rewardAdRequest;
    }

    private IRewardAdRequest _rewardAdRequest;

    public RewardChatRedpacketAdapter(Context context, List<RewardChatRedpacketBean> dataList, IRewardAdRequest rewardAdRequest) {
        _context = context;

        _list = dataList;

        _rewardAdRequest = rewardAdRequest;
    }

    @NonNull
    @Override
    public RewardChatRedpacketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(_context).inflate(MResource.getIdByName(_context, "R.layout.leto_list_item_reward_chat"), parent, false);
        return new RewardChatRedpacketHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardChatRedpacketHolder holder, int position) {
        holder.onBind(_list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return _list == null ? 0 : _list.size();
    }

    @Override
    public void onApiSuccess(ApiContainer.ApiName n, Object data) {

    }

    @Override
    public void onApiFailed(ApiContainer.ApiName n, Object data, boolean aborted) {

    }

    public class RewardChatRedpacketHolder extends CommonViewHolder<RewardChatRedpacketBean> {

        public TextView _amountTv;
        public View _rootView;
        public TextView _curTimeTv;
        public View _redpacketLayout;
        public TextView _splitTv;
        public TextView _totalTimeTv;
        public TextView _tagTv;
        public ImageView _redpacket;

        public RewardChatRedpacketHolder(View itemView) {
            super(itemView);

            // find views
            Context _ctx = itemView.getContext();
            _rootView = itemView;
            _amountTv = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_redpacket_amount"));
            _redpacketLayout = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_redpacket_layout"));
            _curTimeTv = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_time_long"));
            _splitTv = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_time_split"));
            _totalTimeTv = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_total_time_long"));
            _tagTv = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_chat_tag"));
            _redpacket = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_redpacket"));
        }

        @Override
        public void onBind(RewardChatRedpacketBean model, int position) {

            int radio = MGCSharedModel.coinRmbRatio == 0 ? 10000 : MGCSharedModel.coinRmbRatio;

            long curChatTime = (LetoRewardManager.getChatGameProgress(_context) / 60000);//转化成分钟

            _amountTv.setText(String.format("%.02f", (float) model.amount / radio));  //转化成现金
            _amountTv.setTextColor(ColorUtil.parseColor("#FFFFFF"));
            _totalTimeTv.setText(String.valueOf(model.chatTime));
            _totalTimeTv.setTextColor(ColorUtil.parseColor("#FF6771"));
            _tagTv.setVisibility(View.VISIBLE);
            _splitTv.setText("/");

            int status = model.status;
            if (status == 0) { //未完成
                _redpacket.setImageResource(R.mipmap.leto_reward_chat_redpacket);
                if (position == 0) {
                    if (curChatTime < model.chatTime) {
                        //设置红包
                        _curTimeTv.setText(String.valueOf(curChatTime));
                        setAnimation(_redpacketLayout);
                    } else {
                        _curTimeTv.setText("");
                        _splitTv.setText("");
                    }

                } else {
                    long preChatTime = _list.get(position - 1).chatTime;
                    if (preChatTime <= curChatTime && curChatTime < model.chatTime) {
                        //设置红包
                        _curTimeTv.setText(String.valueOf(curChatTime));
                        setAnimation(_redpacketLayout);
                    } else {
                        _curTimeTv.setText("");
                        _splitTv.setText("");
                    }
                }
                _rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LetoRewardManager.startChat(_context);
                    }
                });
            } else if (status == 1) {  //已完成，未领取
                _curTimeTv.setText("");
                _splitTv.setText("");
                _amountTv.setTextColor(ColorUtil.parseColor("#FFFFFF"));
                _totalTimeTv.setTextColor(ColorUtil.parseColor("#FF6771"));
                _tagTv.setVisibility(View.INVISIBLE);
                _redpacket.setImageResource(R.mipmap.leto_reward_chat_redpacket_open);
                setAnimation(_redpacketLayout);

                _rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_rewardAdRequest != null) {
                            _rewardAdRequest.requestRewardAd(_context, new IRewardAdResult() {
                                @Override
                                public void onSuccess() {
                                    MGCApiUtil.addCoin(_context, "", (int) model.amount, "", RewardConst.ADD_COIN_BY_CHAT, position, new HttpCallbackDecode<AddCoinResultBean>(_context, null) {
                                        @Override
                                        public void onDataSuccess(AddCoinResultBean data) {
                                            ToastUtil.s(_context, String.format("恭喜您获得%d金币",(int) model.amount));
                                            model.status = 2;
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(String code, String msg) {
                                            super.onFailure(code, msg);
                                        }
                                    });
                                }

                                @Override
                                public void onFail(String code, String message) {
                                    MGCApiUtil.addCoin(_context, "", (int) model.amount, "", RewardConst.ADD_COIN_BY_CHAT, position, new HttpCallbackDecode<AddCoinResultBean>(_context, null) {
                                        @Override
                                        public void onDataSuccess(AddCoinResultBean data) {
                                            ToastUtil.s(_context, String.format("恭喜您获得%d金币",(int) model.amount));
                                            model.status = 2;
                                            notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFailure(String code, String msg) {
                                            super.onFailure(code, msg);
                                        }
                                    });
                                }
                            });
                        }else{
                            MGCApiUtil.addCoin(_context, "", (int) model.amount, "", RewardConst.ADD_COIN_BY_CHAT, position, new HttpCallbackDecode<AddCoinResultBean>(_context, null) {
                                @Override
                                public void onDataSuccess(AddCoinResultBean data) {
                                    ToastUtil.s(_context, String.format("恭喜您获得%d金币",(int) model.amount));
                                    model.status = 2;
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(String code, String msg) {
                                    super.onFailure(code, msg);
                                }
                            });

                        }

                    }
                });

            } else if (status == 2) {  //已领取
                _amountTv.setTextColor(ColorUtil.parseColor("#666666"));
                _curTimeTv.setText("");
                _splitTv.setText("");
                _totalTimeTv.setTextColor(ColorUtil.parseColor("#999999"));
                _tagTv.setVisibility(View.INVISIBLE);
                _redpacket.setImageResource(R.mipmap.leto_reward_chat_redpacket_gray);
                _rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LetoRewardManager.startChat(_context);
                    }
                });
            }
        }

        private void setAnimation(View animationView) {
            // 获取自定义动画实例
            CustomRotateAnim rotateAnim = CustomRotateAnim.getCustomRotateAnim();
            // 一次动画执行1秒
            rotateAnim.setDuration(800);
            // 设置为循环播放
            rotateAnim.setRepeatCount(-1);
            // 设置为匀速
            rotateAnim.setInterpolator(new LinearInterpolator());
            animationView.startAnimation(rotateAnim);
        }
    }

//    private void addCoin(int coin, int position){
//        MGCApiUtil.addCoin(_context, "", coin, "", RewardConst.ADD_COIN_BY_CHAT, position, new HttpCallbackDecode<AddCoinResultBean>(_context, null) {
//            @Override
//            public void onDataSuccess(AddCoinResultBean data) {
//                ToastUtil.s(_context, "添加成功");
//                model.status = 2;
//                notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(String code, String msg) {
//                super.onFailure(code, msg);
//            }
//        });
//    }
}
