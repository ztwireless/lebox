package com.mgc.letobox.happy.circle.adapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.mgc.letobox.happy.circle.bean.CircleTieZiListResponse;

import java.util.List;

/**
 * Created by DELL on 2018/8/11.
 */

public class CircleListAdapter extends MultipleItemRvAdapter<CircleTieZiListResponse, BaseViewHolder> {

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


    private CircleDetailsAdapter.CircleDetailsClickListener listener;

    public CircleListAdapter(List<CircleTieZiListResponse> data,CircleDetailsAdapter.CircleDetailsClickListener listener) {
        super(data);
        this.listener = listener;
        finishInitialize();
    }

    @Override
    protected int getViewType(CircleTieZiListResponse news) {
        return news.getTemplate_id();
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new TextItemProvider(listener));
        mProviderDelegate.registerProvider(new CenterPicItemProvider(listener));
        mProviderDelegate.registerProvider(new CenterVideoItemProvider(listener));
        mProviderDelegate.registerProvider(new RightPicItemProvider(listener));
        mProviderDelegate.registerProvider(new RightVideoItemProvider(listener));
        mProviderDelegate.registerProvider(new ThreePicItemProvider(listener));
    }

}
