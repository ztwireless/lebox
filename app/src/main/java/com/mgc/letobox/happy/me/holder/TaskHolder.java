package com.mgc.letobox.happy.me.holder;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leto.reward.LetoRewardManager;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.mgc.bean.CoinDialogScene;
import com.mgc.leto.game.base.mgc.dialog.IMGCCoinDialogListener;
import com.mgc.leto.game.base.mgc.util.MGCDialogUtil;
import com.mgc.leto.game.base.trace.LetoTrace;
import com.mgc.leto.game.base.utils.ColorUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.leto.game.base.widget.ClickGuard;
import com.mgc.letobox.happy.LeBoxMobileLoginActivity;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.event.TabSwitchEvent;
import com.mgc.letobox.happy.follow.FollowInviteCodeActivity;
import com.mgc.letobox.happy.me.IRewardAdResult;
import com.mgc.letobox.happy.me.JoinWeChatActivity;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;
import com.mgc.letobox.happy.util.LetoBoxEvents;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

public class TaskHolder extends CommonViewHolder<TaskResultBean> {
    // views
    private TextView _label;
    private ImageView _taskIcon;
    private TextView _titlelabel;
    private TextView _coinlabel;
    private TextView _desclabel;
    private TextView _playlabel;

    private ProgressBar _progressBar;
    private TextView _curProgresslabel;
    private TextView _totalProgresslabel;
    private LinearLayout _progressLayout;

    Context _ctx;


    public static TaskHolder create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_task_item"), parent, false);
        return new TaskHolder(convertView);
    }

    public TaskHolder(View itemView) {
        super(itemView);

        // find views
        _ctx = itemView.getContext();
        _taskIcon = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_icon"));
        _playlabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.btn_play"));
        _desclabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_desc"));
        _coinlabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.coin"));
        _titlelabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_title"));
        _progressBar = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_progress"));
        _curProgresslabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_tv_progress"));
        _totalProgresslabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_tv_total_progress"));
        _progressLayout = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.leto_progress_layout"));
    }

    @Override
    public void onBind(final TaskResultBean model, int position) {
        // label

//        if (model.getFinish_type() == 4) {
//            _titlelabel.setText(String.format("%s(已完成%d/%d)", model.getTask_title(), model.getProcess(), model.getFinish_level()));
//        } else if (model.getFinish_type() == 2) {
//            String progress = convertTimeFormat(model.getProcess());
//            if (model.getProcess() > model.getFinish_level()) {
//                progress = convertTimeFormat(model.getFinish_level());
//            }
//            _titlelabel.setText(String.format("%s(已玩%s分钟)", model.getTask_title(), progress));
//        } else {
//            _titlelabel.setText(model.getTask_title());
//        }
        _progressLayout.setVisibility(View.VISIBLE);
        if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_SCRATCH_CARD) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_scrach_card);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_ANSWER) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_answer);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_IDIOM) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_idiom);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_TURNTABLE) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_turntable);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_BIND_PHONE) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_phone);
            _progressLayout.setVisibility(View.GONE);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_BIND_INVITE) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_invite_code);
            _progressLayout.setVisibility(View.GONE);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_DURATION) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_play_game);
        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_PLAY_GAME_TIME) {
            _taskIcon.setImageResource(R.mipmap.leto_reward_task_try_play_game);
        }


        _titlelabel.setText(model.getTask_title());

        _desclabel.setText(model.getTask_desc());
        _coinlabel.setText("+" + model.getAward_coins());

        long progress = model.getProcess();
        long totalProgress = model.getFinish_level();

        if (model.getFinish_type() == 2) { //毫秒转化成分钟
            progress = (int) model.getProcess() / 60000;
            totalProgress = (int) model.getFinish_level() / 60000;
        }
        _curProgresslabel.setText(String.valueOf(progress));
        _totalProgresslabel.setText(String.valueOf(totalProgress));
        _progressBar.setProgress((int) progress);
        _progressBar.setMax((int) totalProgress);

        // set tag
        itemView.setTag(position);
        int status = model.getProcess() >= model.getFinish_level() ? 1 : 0;
        if (model.getClassify() == LeBoxConstant.LETO_TASK_DAILY) {
            if (status == 0) {
                _playlabel.setText("去完成");
                _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
                _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);
                _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {
                        LetoTrace.d("click  task type :" + model.getFinish_type());
                        if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_SCRATCH_CARD) {
                            LetoRewardManager.startGuaGuaCard(_ctx);
                        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_ANSWER) {
                            LetoRewardManager.startDaTi(_ctx, true);
                        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_IDIOM) {
                            LetoRewardManager.startIdiom(_ctx);
                        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_REWARD_TURNTABLE) {
                            LetoRewardManager.startDanzhuanpan(_ctx);
                        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_VIEW_VIDEO) {
                            if (getRewardAdRequest() != null) {
                                getRewardAdRequest().requestRewardAd(_ctx, new IRewardAdResult() {
                                    @Override
                                    public void onSuccess() {
                                        if (LetoBoxEvents.getRewardedVideoListener() != null) {
                                            LetoBoxEvents.getRewardedVideoListener().showVideo();
                                        }

                                    }

                                    @Override
                                    public void onFail(String code, String message) {

                                    }
                                });
                            } else {
                                LetoTrace.d("request ad callback is null");
                            }

                        } else {
                            EventBus.getDefault().post(new TabSwitchEvent(1));
                        }
                        return true;
                    }
                });
            } else {
                if (model.getStatus() == 1) {
                    _playlabel.setText("待领取");
                    _playlabel.setTextColor(ColorUtil.parseColor("#FFFFFF"));
                    _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg_blue);
                    _playlabel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MGCDialogUtil.showMGCCoinDialogWithOrderId(_ctx, null, model.getAward_coins(), 1, CoinDialogScene.ROOKIE_TASK,
                                    model.getChannel_task_id(), new IMGCCoinDialogListener() {
                                        @Override
                                        public void onExit(boolean video, int coinGot) {

                                        }
                                    });
                        }
                    });
                } else {
                    _playlabel.setText("已领取");
                    _playlabel.setTextColor(ColorUtil.parseColor("#CCCCCC"));
                    _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg_gray);
                    _playlabel.setOnClickListener(null);
                }
            }

        } else if (model.getClassify() == LeBoxConstant.LETO_TASK_NEWER) {
            if (status == 0) {
                _playlabel.setText("去完成");
                _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
                _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);
                _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {

                        if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_JOIN_WECHAT_GROUP) {
                            JoinWeChatActivity.start(_ctx, model.getChannel_task_id());
                        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_BIND_PHONE) {
                            if (LoginManager.isTempAccount(LoginManager.getMobile(_ctx))) {
                                LeBoxMobileLoginActivity.startActivityByRequestCode((Activity) _ctx, LeBoxConstant.REQUEST_CODE_TASK_PHONE_LOGIN);
                            } else {
                                LetoTrace.d("The account was signed : " + LoginManager.getMobile(_ctx));
                            }
                        } else if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_BIND_INVITE) {
                            FollowInviteCodeActivity.startActivityByRequestCode((Activity) _ctx, LeBoxConstant.REQUEST_CODE_TASK_INVITE_CODE);
                        } else {

                            EventBus.getDefault().post(new TabSwitchEvent(1));

                        }
                        return true;
                    }
                });
            } else {
                if (model.getStatus() == 1) {
                    _playlabel.setText("待领取");
                    _playlabel.setTextColor(ColorUtil.parseColor("#FFFFFF"));
                    _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg_blue);
                    _playlabel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MGCDialogUtil.showMGCCoinDialogWithOrderId(_ctx, null, model.getAward_coins(), 1, CoinDialogScene.ROOKIE_TASK,
                                    model.getChannel_task_id(), new IMGCCoinDialogListener() {
                                        @Override
                                        public void onExit(boolean video, int coinGot) {

                                        }
                                    });
                        }
                    });
                } else {
                    _playlabel.setText("已领取");
                    _playlabel.setTextColor(ColorUtil.parseColor("#CCCCCC"));
                    _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg_gray);
                    _playlabel.setOnClickListener(null);
                }
            }
        }
    }


    /**
     * 毫秒转化成分钟， 保留1位小数
     *
     * @param progress
     * @return
     */
    private String convertTimeFormat(long progress) {
        String result;
        long seconds = progress / 1000;

        float num = (float) seconds / 60;
        DecimalFormat df = new DecimalFormat("0.0");

        result = df.format(num);

        return result;
    }
}
