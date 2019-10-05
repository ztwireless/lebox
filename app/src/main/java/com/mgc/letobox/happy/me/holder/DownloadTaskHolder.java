package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledong.lib.leto.main.IntegralDownloadTaskActivity;
import com.ledong.lib.leto.trace.LetoTrace;
import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.ad.bean.mgc.MgcAdBean;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.ColorUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.R;


import java.text.DecimalFormat;

public class DownloadTaskHolder extends CommonViewHolder<MgcAdBean> {

    private static final String TAG = DownloadTaskHolder.class.getSimpleName();

    // views
    private TextView _label;
    private TextView _titlelabel;
    private TextView _coinlabel;
    private TextView _desclabel;
    private TextView _playlabel;
    private ImageView _iconView;

    MgcAdBean taskResultBean;

    Context _ctx;


    public static CommonViewHolder<MgcAdBean> create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_high_coin_task_item"), parent, false);
        return new DownloadTaskHolder(convertView);
    }

    public DownloadTaskHolder(View itemView) {
        super(itemView);

        // find views
        _ctx = itemView.getContext();
        _playlabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.btn_play"));
        _desclabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_desc"));
        _coinlabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.coin"));
        _titlelabel = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.task_title"));
        _iconView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.app_icon"));
    }

    @Override
    public void onBind(final MgcAdBean model, final int position) {
        // label

        int status = BaseAppUtil.isInstallApp(_ctx, model.dappPkgName) ? 1 : 0;
        if (status == 0) {
            _playlabel.setText("去完成");
            _playlabel.setTextColor(ColorUtil.parseColor("#3D9AF0"));
            _playlabel.setBackgroundResource(R.drawable.leto_minigame_play_btn_bg);

            _playlabel.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
                @Override
                public boolean onClicked() {

                    LetoTrace.d(TAG, "do task: " + model.adSubtitle);

                    IntegralDownloadTaskActivity.start(_ctx, 2, "+0.4元", 0, model, null, 0);

                    return true;
                }
            });

        } else {
            _playlabel.setText("已完成");
            _playlabel.setTextColor(ColorUtil.parseColor("#FFFFFF"));
            _playlabel.setBackgroundResource(R.drawable.leto_task_play_btn_bg_gray);

            _playlabel.setOnClickListener(null);
        }
        _titlelabel.setText(model.adSubtitle);
        _coinlabel.setText("+" + 0.4 + "元/次");

        GlideUtil.loadRoundedCorner(_ctx, model.adIcon, _iconView, 11);

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
