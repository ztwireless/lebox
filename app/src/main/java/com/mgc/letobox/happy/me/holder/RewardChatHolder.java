package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.leto.reward.LetoRewardManager;
import com.leto.reward.util.RewardApiUtil;
import com.mgc.leto.game.base.http.HttpCallbackDecode;
import com.mgc.leto.game.base.http.OkHttpCallbackDecode;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.bean.BenefitSettings_chat;
import com.mgc.leto.game.base.mgc.bean.GetBenefitsSettingResultBean;
import com.mgc.leto.game.base.mgc.model.MGCSharedModel;
import com.mgc.leto.game.base.mgc.util.MGCApiUtil;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.BaseAppUtil;
import com.mgc.leto.game.base.utils.DensityUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.me.adapter.RewardChatRedpacketAdapter;
import com.mgc.letobox.happy.me.bean.MeModuleBean;
import com.mgc.letobox.happy.me.bean.RewardChatRedpacketBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RewardChatHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    RecyclerView _recyclerView;

    Context _context;

    ImageView leto_chat_banner;
    TextView leto_chat_tip;
    ProgressBar _leto_chat_pregress;

    RewardChatRedpacketAdapter _adapter;

    List<RewardChatRedpacketBean> _rewardButtonList = new ArrayList<>();


    public static RewardChatHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_reward_chat"), parent, false);
        return new RewardChatHolder(ctx, view);
    }

    public RewardChatHolder(Context context, View view) {
        super(view);

        _context = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));
        leto_chat_banner = itemView.findViewById(MResource.getIdByName(context, "R.id.leto_chat_banner"));
        _recyclerView = itemView.findViewById(MResource.getIdByName(context, "R.id.recyclerView"));
        _leto_chat_pregress = itemView.findViewById(MResource.getIdByName(context, "R.id.leto_chat_progress"));
        leto_chat_tip = itemView.findViewById(MResource.getIdByName(context, "R.id.leto_chat_tip"));

//        initRedPacketData();
        int width = BaseAppUtil.getDeviceWidth(context);
        int count = _rewardButtonList == null || _rewardButtonList.size() == 0 ? 4 : _rewardButtonList.size();
        int margin = (width - DensityUtil.dip2px(context, 48)) / (2 * count);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) _leto_chat_pregress.getLayoutParams();
        layoutParams.leftMargin = margin;
        layoutParams.rightMargin = margin;
        _leto_chat_pregress.setLayoutParams(layoutParams);

        leto_chat_banner.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                LetoRewardManager.startChat(context);
                return true;
            }
        });
    }

    @Override
    public void onBind(final MeModuleBean moduleBean, final int position) {
        // name & desc
//        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        _splitSpace.setVisibility(View.GONE);


        long curChatDuration = LetoRewardManager.getChatGameProgress(_context);
        int progress = (int) curChatDuration / 60000; //转化成分钟
        _leto_chat_pregress.setProgress(progress);

        initRedPacketData();
    }

    private void initRedPacketData() {


        String rewardList = LetoRewardManager.loadUserChatProgress(_context, LoginManager.getMobile(_context));
        if (!TextUtils.isEmpty(rewardList)) {
            List<RewardChatRedpacketBean> chatList = new Gson().fromJson(rewardList, new TypeToken<List<RewardChatRedpacketBean>>() {
            }.getType());
            _rewardButtonList.clear();
            _rewardButtonList.addAll(chatList);
            long maxTime = 0;
            int size = _rewardButtonList.size();
            for (int i = 0; i < size; i++) {
                long min = _rewardButtonList.get(i).getChatTime();
                if (min >= maxTime) {
                    maxTime = min;
                }
            }
            if (_leto_chat_pregress != null) {
                _leto_chat_pregress.setMax((int) maxTime);
            }
        }

        if (MGCSharedModel.isBenefitSettingsInited()) {
            initList();
        } else {
            MGCApiUtil.getBenefitSettings(_context, new HttpCallbackDecode<GetBenefitsSettingResultBean>(_context, null) {
                @Override
                public void onDataSuccess(GetBenefitsSettingResultBean data) {

                    initList();
                }
            });
        }

    }

    private void initList() {

        if (MGCSharedModel.benefitSettings != null) {
            BenefitSettings_chat chatConfig = MGCSharedModel.benefitSettings.getYuliao();
            if (chatConfig.getYuliao() != null) {
                if (_rewardButtonList == null) {
                    _rewardButtonList = new ArrayList<>();
                }
                _rewardButtonList.clear();
                int size = chatConfig.getYuliao().size();
                double totalCoins = 0;
                long maxTime = 0;
                for (int i = 0; i < size; i++) {
                    long min = chatConfig.getYuliao().get(i).getChat_time() / 60;
                    RewardChatRedpacketBean redpacketBean = new RewardChatRedpacketBean();
                    totalCoins += chatConfig.getYuliao().get(i).getCoin_num();
                    redpacketBean.amount = chatConfig.getYuliao().get(i).getCoin_num();
                    redpacketBean.chatTime = min;
                    redpacketBean.status = 0;
                    _rewardButtonList.add(redpacketBean);
                    if (min >= maxTime) {
                        maxTime = min;
                    }
                }
                int radio = MGCSharedModel.coinRmbRatio == 0 ? 10000 : MGCSharedModel.coinRmbRatio;
                leto_chat_tip.setText(String.format("美女陪聊最高可获得%.02f元红包", totalCoins / radio));

                _leto_chat_pregress.setMax((int) maxTime);
            }
        }
        getUserStatus();
    }

    private void getUserStatus() {
        RewardApiUtil.getUserChatStatus(_context, new OkHttpCallbackDecode<List<Integer>>(new TypeToken<List<Integer>>() {
        }.getType()) {
            @Override
            public void onFailure(String code, final String msg) {
                super.onFailure(code, msg);
                LetoTrace.d(msg);
                try {
                    if (_recyclerView != null) {
                        _recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                if (_adapter == null) {
                                    _adapter = new RewardChatRedpacketAdapter(_context, _rewardButtonList, getRewardAdRequest());

                                    _recyclerView.setLayoutManager(new GridLayoutManager(_context, 4));
                                    _recyclerView.setAdapter(_adapter);
                                } else {
                                    _adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataSuccess(List<Integer> data) {
                LetoTrace.d("get user chat status....");
                try {
                    if (_recyclerView != null) {
                        _recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                //因为聊天时长在本地存储，当用户删掉缓存或换设备登陆，此时本地无记录。目前采用通过用户状态记录对应的时长，更新到本地
                                //因为状态取决用户是否点击加金币，如果用户完成后并没有领取金币，记录也会无法保存
                                long serverChatTime = 0;

                                //本地时长，单位毫秒
                                long curChatDuration = LetoRewardManager.getChatGameProgress(_context);
                                //转化成分钟
                                long curChatTime = (curChatDuration / 60000);

                                for (int i = 0; i < _rewardButtonList.size(); i++) {
                                    RewardChatRedpacketBean redpacketBean = _rewardButtonList.get(i);
                                    if (data.get(i) == 2) {
                                        redpacketBean.status = 2;
                                        serverChatTime = redpacketBean.chatTime * 60000;
                                    } else {
                                        if (curChatTime >= redpacketBean.chatTime) {
                                            _rewardButtonList.get(i).status = 1;
                                        } else {
                                            _rewardButtonList.get(i).status = 0;
                                        }
                                    }
                                }
                                //如果需要更新到本地
                                if (curChatDuration == 0 && serverChatTime > 0) {
                                    LetoTrace.d("update chat time to loacal: " + serverChatTime);
                                    LetoRewardManager.updateChatGameProgress(_context, serverChatTime);
                                }

                                //保存数据更新到本地
                                LetoRewardManager.saveUserChatProgress(_context, LoginManager.getMobile(_context), new Gson().toJson(_rewardButtonList));

                                if (_adapter == null) {
                                    _adapter = new RewardChatRedpacketAdapter(_context, _rewardButtonList, getRewardAdRequest());

                                    _recyclerView.setLayoutManager(new GridLayoutManager(_context, 4));
                                    _recyclerView.setAdapter(_adapter);
                                    LetoTrace.d("new adapter: notifyDataSetChanged");
                                } else {
                                    LetoTrace.d("getUserStatus notifyDataSetChanged");
                                    _adapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        });
    }


}