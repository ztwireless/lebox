package com.mgc.letobox.happy.find.adapter.news;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.FindConst;
import com.mgc.letobox.happy.find.adapter.WeiboListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;


/**
 * @author ChayChan
 * @description: 右侧小图布局(1.小图新闻；2.视频类型，右下角显示视频时长)
 * @date 2018/3/22  14:36
 */
public class RightVideoNewsItemProvider extends BaseNewsItemProvider {


    public RightVideoNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return WeiboListAdapter.RIGHT_VIDEO_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_weibo_pic_video;
    }


    @Override
    protected void setData(BaseViewHolder helper, ArticleResultBean news) {
//        //右侧小图布局，判断是否有视频
//        if (news.has_video) {
//            helper.setVisible(R.id.ll_duration, true);//显示时长
//            helper.setText(R.id.tv_duration, TimeUtil.secToTime(news.video_duration));//设置时长
//        } else {
//            helper.setVisible(R.id.ll_duration, false);//隐藏时长
//        }
//        GlideUtils.load(mContext, news.middle_image.url, helper.getView(R.id.iv_img));//右侧图片或视频的图片使用middle_image
//
        helper.setVisible(R.id.ll_duration, true);//显示时长
        //helper.setText(R.id.tv_duration, TimeUtil.secToTime(news.video_duration));//设置时长

        if(null!=news.getPics()&& news.getPics().size()>0) {


            int screen_width = BaseAppUtil.getDeviceWidth(helper.itemView.getContext());
            int image_width = (screen_width - 2 * DensityUtil.dip2px(helper.itemView.getContext(), 10)) / 3;
            int image_height = (screen_width - 2 * DensityUtil.dip2px(helper.itemView.getContext(), 10)) / 4;

            RelativeLayout rl_right = helper.getView(R.id.rl_right);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_right.getLayoutParams();
            params.height = image_height;
            params.width = image_width;
            helper.getView(R.id.rl_right).setLayoutParams(params);

            GlideUtil.loadRoundedCorner(helper.getView(R.id.iv_img).getContext(),
                news.getPics().get(0)+ String.format(FindConst.image_url_with_size,640,420),
                (ImageView)helper.getView(R.id.iv_img),
                4,
                R.mipmap.default_image_2);
        }
    }

}
