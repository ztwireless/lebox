package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ledong.lib.leto.mgc.holder.CommonViewHolder;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.event.TabSwitchEvent;
import com.mgc.letobox.happy.me.JoinWeChatActivity;
import com.mgc.letobox.happy.me.bean.TaskResultBean;
import com.mgc.letobox.happy.util.LeBoxConstant;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

public class TaskHolder extends CommonViewHolder<TaskResultBean> {
    // views
    private TextView _label;
    private TextView _titlelabel;
    private TextView _coinlabel;
    private TextView _desclabel;
    private TextView _playlabel;

    TaskResultBean taskResultBean;

    Context _ctx;


    public static TaskHolder create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_task_item"), parent, false);
        return new TaskHolder(convertView);
    }

    public TaskHolder(View itemView) {
        super(itemView);

        // find views
        _ctx = itemView.getContext();
        _playlabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.btn_play"));
        _desclabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_desc"));
        _coinlabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.coin"));
        _titlelabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_title"));
    }

    @Override
    public void onBind(final TaskResultBean model, int position) {
        // label

        if (model.getFinish_type() == 4) {
            _titlelabel.setText(String.format("%s(已完成%d/%d)", model.getTask_title(), model.getProcess(), model.getFinish_level()));
        } else if (model.getFinish_type() == 2) {
            String progress = convertTimeFormat(model.getProcess());
            if (model.getProcess() > model.getFinish_level()) {
                progress = convertTimeFormat(model.getFinish_level());
            }
            _titlelabel.setText(String.format("%s(已玩%s分钟)", model.getTask_title(), progress));
        } else {
            _titlelabel.setText(model.getTask_title());
        }
        _desclabel.setText(model.getTask_desc());
        _coinlabel.setText("+" + model.getAward_coins());

        // set tag
        itemView.setTag(position);
        if (model.getClassify() == LeBoxConstant.LETO_TASK_DAILY) {
            _playlabel.setText("马上玩");
            _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
            _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);
            _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {

                    if (model.getClassify() == LeBoxConstant.LETO_TASK_DAILY) {

                        if (model.getChannel_task_id() == 1) {
                            EventBus.getDefault().post(new TabSwitchEvent(0));
                        } else {
                            EventBus.getDefault().post(new TabSwitchEvent(2));
                        }
                    } else if (model.getClassify() == LeBoxConstant.LETO_TASK_NEWER) {

                        Log.i("task", "asdfghjk");

                        if (model.getFinish_type() == 4) {
                            EventBus.getDefault().post(new TabSwitchEvent(0));
                        }
                    }

                    return true;
                }
            });
        } else {

            if (model.getFinish_type() == LeBoxConstant.LETO_TASK_TYP_JOIN_WECHAT_GROUP) {
                _playlabel.setText("立即查看");
                _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
                _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);
                _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                    @Override
                    public boolean onClicked() {

                        JoinWeChatActivity.start(_ctx, model.getChannel_task_id());
                        return true;
                    }
                });

            } else {
                int status = model.getProcess() >= model.getFinish_level() ? 1 : 0;
                if (status == 0) {
                    _playlabel.setText("马上玩");
                    _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
                    _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);
                    _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                        @Override
                        public boolean onClicked() {

                            if (model.getClassify() == LeBoxConstant.LETO_TASK_DAILY) {

                                if (model.getChannel_task_id() == 1) {
                                    EventBus.getDefault().post(new TabSwitchEvent(0));
                                } else {
                                    EventBus.getDefault().post(new TabSwitchEvent(2));
                                }
                            } else if (model.getClassify() == LeBoxConstant.LETO_TASK_NEWER) {

                                Log.i("task", "asdfghjk");
                                EventBus.getDefault().post(new TabSwitchEvent(0));
                            }
                            return true;
                        }
                    });
                } else {
//                    if(model.getFinish_type() == 2) {
//                        _playlabel.setText("马上玩");
//                        _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
//                        _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);
//                        _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
//                            @Override
//                            public boolean onClicked() {
//
//                                if (model.getClassify() == LeBoxConstant.LETO_TASK_DAILY) {
//
//                                    if (model.getChannel_task_id() == 1) {
//                                        EventBus.getDefault().post(new TabSwitchEvent(0));
//                                    } else {
//                                        EventBus.getDefault().post(new TabSwitchEvent(2));
//                                    }
//                                } else if (model.getClassify() == LeBoxConstant.LETO_TASK_NEWER) {
//
//                                    Log.i("task", "asdfghjk");
//                                    EventBus.getDefault().post(new TabSwitchEvent(0));
//                                }
//                                return true;
//                            }
//                        });
//
//
//                    }else{
                    _playlabel.setText("已领取");
                    _playlabel.setTextColor(ColorUtil.parseColor("#CCCCCC"));
                    _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg_gray);
                    _playlabel.setOnClickListener(null);
//                    }

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
