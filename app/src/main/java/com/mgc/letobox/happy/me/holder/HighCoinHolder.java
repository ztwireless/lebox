package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ledong.lib.leto.widget.ClickGuard;
import com.leto.game.base.util.MResource;
import com.mgc.letobox.happy.LeBoxHighCoinTaskActivity;
import com.mgc.letobox.happy.me.bean.MeModuleBean;



public class HighCoinHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    Context _context;

    public static HighCoinHolder create(Context ctx, ViewGroup parent) {
        // load game row, and leave a gap so that next column can be seen
        View view = LayoutInflater.from(ctx)
                .inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_high_coin"), parent, false);
        return new HighCoinHolder(ctx, view);
    }

    public HighCoinHolder(Context context, View view) {
        super(view);

        _context = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(context, "R.id.split_space"));

        itemView.setOnClickListener(new ClickGuard.GuardedOnClickListener() {
            @Override
            public boolean onClicked() {
                LeBoxHighCoinTaskActivity.start(_context);
                return true;
            }
        });

    }

    @Override
    public void onBind(final MeModuleBean signin, final int position) {
        // name & desc
        final Context ctx = itemView.getContext();

        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

    }
}