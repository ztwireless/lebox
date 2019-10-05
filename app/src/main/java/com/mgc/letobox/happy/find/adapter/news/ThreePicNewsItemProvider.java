package com.mgc.letobox.happy.find.adapter.news;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.adapter.WeiboListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;


/**
 * @author ChayChan
 * @description: 三张图片布局(文章 、 广告)
 * @date 2018/3/22  14:36
 */
public class ThreePicNewsItemProvider extends BaseNewsItemProvider {

    public ThreePicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return WeiboListAdapter.THREE_PICS_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_weibo_three_pics;
    }

    @Override
    protected void setData(BaseViewHolder helper, ArticleResultBean news) {
        //三张图片的新闻
        if (null != news.getPics() && news.getPics().size() > 0) {
            int screen_width = BaseAppUtil.getDeviceWidth(helper.itemView.getContext());
            int image_width = (screen_width - 2 * DensityUtil.dip2px(helper.itemView.getContext(), 10)) / 3;
            int image_height = (screen_width - 2 * DensityUtil.dip2px(helper.itemView.getContext(), 10)) / 4;

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.iv_img1).getLayoutParams();
            params.height = image_height;
            params.width = image_width;

            ImageView imgView1 = helper.getView(R.id.iv_img1);
            imgView1.setLayoutParams(params);

            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) helper.getView(R.id.iv_img1).getLayoutParams();
            params2.height = image_height;
            params2.width = image_width;
            params2.leftMargin = 5;
            params2.rightMargin = 5;
            ((ImageView) helper.getView(R.id.iv_img2)).setLayoutParams(params2);

            ((ImageView) helper.getView(R.id.iv_img3)).setLayoutParams(params);

            helper.getView(R.id.iv_img1).setVisibility(View.GONE);
            helper.getView(R.id.iv_img2).setVisibility(View.GONE);
            helper.getView(R.id.iv_img3).setVisibility(View.GONE);
            if(news.getPics().size() > 0) {
                helper.getView(R.id.iv_img1).setVisibility(View.VISIBLE);
                GlideUtil.loadRoundedCorner(helper.getView(R.id.iv_img1).getContext(),
                    news.getPics().get(0),
                    (ImageView)helper.getView(R.id.iv_img1),
                    4,
                    R.mipmap.default_image_2);
            }
            if (news.getPics().size() > 1) {
                helper.getView(R.id.iv_img2).setVisibility(View.VISIBLE);
                GlideUtil.loadRoundedCorner(helper.getView(R.id.iv_img2).getContext(),
                    news.getPics().get(1),
                    (ImageView)helper.getView(R.id.iv_img2),
                    4,
                    R.mipmap.default_image_2);
            }
            if (news.getPics().size() > 2) {
                helper.getView(R.id.iv_img3).setVisibility(View.VISIBLE);
                GlideUtil.loadRoundedCorner(helper.getView(R.id.iv_img3).getContext(),
                    news.getPics().get(2),
                    (ImageView)helper.getView(R.id.iv_img3),
                    4,
                    R.mipmap.default_image_2);
            }
        }
    }

}
