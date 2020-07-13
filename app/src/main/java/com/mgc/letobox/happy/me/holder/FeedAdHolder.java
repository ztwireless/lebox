package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mgc.leto.game.base.api.adext.FeedAd;
import com.mgc.leto.game.base.api.adext.FeedAdView;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.me.bean.MeFeedAdModuleBean;
import com.mgc.letobox.happy.me.bean.MeModuleBean;

public class FeedAdHolder extends CommonViewHolder<MeModuleBean> {
    // views
    private FrameLayout _extraContainer;

    public static FeedAdHolder create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_feed_ad"), parent, false);
        return new FeedAdHolder(ctx, convertView);
    }

    public FeedAdHolder(Context context, View itemView) {
        super(itemView);

        // find views
        _extraContainer = itemView.findViewById(R.id.extra_container);
    }

    @Override
    public void onBind(final MeModuleBean model, int position) {
        if(model instanceof MeFeedAdModuleBean) {
            MeFeedAdModuleBean bean = (MeFeedAdModuleBean)model;
            FeedAd extAd = bean.getFeedAd();
            FeedAdView view = extAd.getView();
            if(view != null) {
                view.removeFromSuperview();
                _extraContainer.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }
}
