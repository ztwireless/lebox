package com.mgc.letobox.happy.find.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.mgc.letobox.happy.find.adapter.news.CenterPicNewsItemProvider;
import com.mgc.letobox.happy.find.adapter.news.CenterVideoNewsItemProvider;
import com.mgc.letobox.happy.find.adapter.news.RightPicNewsItemProvider;
import com.mgc.letobox.happy.find.adapter.news.RightVideoNewsItemProvider;
import com.mgc.letobox.happy.find.adapter.news.TextNewsItemProvider;
import com.mgc.letobox.happy.find.adapter.news.ThreePicNewsItemProvider;
import com.mgc.letobox.happy.find.model.Weibo;

import java.util.List;

/**
 * @author ChayChan
 * @description: 新闻列表的适配器
 * @date 2018/3/22  11
 */

public class WeiboListAdapter extends MultipleItemRvAdapter<Weibo, BaseViewHolder> {

    /**
     * 纯文字布局(文章、广告)
     */
    public static final int TEXT_NEWS = 1;
    /**
     * 三张图片布局(文章、广告)
     */
    public static final int THREE_PICS_NEWS = 2;
    /**
     * 右侧小图布局(1.小图新闻)
     */
    public static final int RIGHT_PIC_NEWS = 3;
    /**
     * 居中大图布局(1.单图文章；2.单图广告；3.视频，中间显示播放图标，右侧显示时长)
     */
    public static final int CENTER_SINGLE_PIC_NEWS = 4;
    /**
     * 居中大图布局(4.组图，右侧显示组图数目)
     */
    public static final int CENTER_PICS = 5;
    /**
     * 居中大图布局(3.视频，中间显示播放图标，右侧显示时长)
     */
    public static final int CENTER_SINGLE_VIDEO_NEWS = 6;
    /**
     * 右侧小图布局(2.视频类型，右下角显示视频时长)
     */
    public static final int RIGHT_VIDEO_NEWS = 7;



    private String mChannelCode;


    public WeiboListAdapter(/*String channelCode, */@Nullable List<Weibo> data) {
        super(data);
//        mChannelCode = channelCode;
        finishInitialize();
    }

    @Override
    protected int getViewType(Weibo news) {
        return news.getContent_type();
//        if (news.has_video) {
//            //如果有视频
//            if (news.video_style == 0) {
//                //右侧视频
//                if (news.middle_image == null || TextUtils.isEmpty(news.middle_image.url)) {
//                    return TEXT_NEWS;
//                }
//                return RIGHT_PIC_VIDEO_NEWS;
//            } else if (news.video_style == 2) {
//                //居中视频
//                return CENTER_SINGLE_PIC_NEWS;
//            }
//        } else {
//            //非视频新闻
//            if (!news.has_image) {
//                //纯文字新闻
//                return TEXT_NEWS;
//            } else {
//                if (news.image_list==null||news.image_list.size()==0) {
//                    //图片列表为空，则是右侧图片
//                    return RIGHT_PIC_VIDEO_NEWS;
//                }
//
//                if (news.gallary_image_count == 3) {
//                    //图片数为3，则为三图
//                    return THREE_PICS_NEWS;
//                }
//
//                //中间大图，右下角显示图数
//                return CENTER_SINGLE_PIC_NEWS;
//            }
//        }
//
//        return TEXT_NEWS;
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new TextNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new CenterPicNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new CenterVideoNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new RightPicNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new RightVideoNewsItemProvider(mChannelCode));
        mProviderDelegate.registerProvider(new ThreePicNewsItemProvider(mChannelCode));
    }
}
