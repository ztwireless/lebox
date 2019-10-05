package com.mgc.letobox.happy.find.adapter.news;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.leto.game.base.login.LoginManager;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;


/**
 * @author ChayChan
 * @description: 将新闻中设置数据公共部分抽取
 * @date 2018/3/22  14:48
 */

public abstract class BaseNewsItemProvider extends BaseItemProvider<ArticleResultBean, BaseViewHolder> {

    private String mChannelCode;

    public BaseNewsItemProvider(String channelCode) {
        mChannelCode = channelCode;
    }

    @Override
    public void convert(BaseViewHolder helper, ArticleResultBean news, int i) {
        if (TextUtils.isEmpty(news.getTitle())) {
            //如果没有标题，则直接跳过
            return;
        }

        Context ctx = helper.getView(R.id.iv_grade).getContext();

        helper.setText(R.id.tv_title, news.getTitle())
                .setText(R.id.tv_comment_num, "" + news.getComment());

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(news.getKol().getNickname());
        helper.setText(R.id.tv_time, news.getDate());

        CheckBox cb_follow = helper.getView(R.id.cb_follow);

        if (null != LoginManager.getMemId(ctx) && LoginManager.getMemId(ctx).equalsIgnoreCase(news.getKol().getId() + "")) {
            cb_follow.setVisibility(View.GONE);
        }else{
            cb_follow.setVisibility(View.VISIBLE);
        }
        helper.addOnClickListener(R.id.cb_follow);
        helper.addOnClickListener(R.id.ll_weibo);
        helper.addOnClickListener(R.id.iv_avatar);

        cb_follow.setChecked(news.getKol().getIsfollow()==1?true:false);

        GlideUtil.loadRoundedCorner(ctx,
            news.getKol().getCover_pic(),
            (ImageView) helper.getView(R.id.iv_avatar),
            20,
            R.mipmap.default_avatar);
        Glide.with(ctx).load(news.getKol().getLevel_pic()).into((ImageView) helper.getView(R.id.iv_grade));

        setData(helper, news);
    }

    protected abstract void setData(BaseViewHolder helper, ArticleResultBean news);
}
