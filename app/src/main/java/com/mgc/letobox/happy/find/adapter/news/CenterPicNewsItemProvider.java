package com.mgc.letobox.happy.find.adapter.news;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.adapter.WeiboListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;

/**
 * @author ChayChan
 * @description: 居中大图布局(1.单图文章 ； 2.单图广告 ； 3.视频 ， 中间显示播放图标 ， 右侧显示时长)
 * @date 2018/3/22  14:36
 */
public class CenterPicNewsItemProvider extends BaseNewsItemProvider {


    public CenterPicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return WeiboListAdapter.CENTER_SINGLE_PIC_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_weibo_center_pic;
    }

    @Override
    protected void setData(BaseViewHolder helper, ArticleResultBean news) {
        //中间大图布局，判断是否有视频
        TextView tvBottomRight = helper.getView(R.id.tv_bottom_right);

        helper.setVisible(R.id.iv_play, false);//隐藏播放按钮
        helper.setVisible(R.id.ll_bottom_right, false);  //隐藏时长
        tvBottomRight.setCompoundDrawables(null, null, null, null);//去除TextView左侧图标
        if (null != news.getPics() && news.getPics().size() > 0) {

            int screen_width = BaseAppUtil.getDeviceWidth(helper.itemView.getContext());
            int image_width = screen_width - 2 * DensityUtil.dip2px(helper.itemView.getContext(), 10);
            int image_height = image_width * 3 / 4;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) helper.getView(R.id.iv_img).getLayoutParams();
            params.height = image_height;
            params.width = image_width;

            helper.getView(R.id.iv_img).setVisibility(View.VISIBLE);
            ((ImageView) helper.getView(R.id.iv_img)).setLayoutParams(params);

            //中间图片使用视频大图
            GlideUtil.loadRoundedCorner(helper.getView(R.id.iv_img).getContext(),
                news.getPics().get(0),
                (ImageView)helper.getView(R.id.iv_img),
                4,
                R.mipmap.default_image_2);
        }
    }
}
