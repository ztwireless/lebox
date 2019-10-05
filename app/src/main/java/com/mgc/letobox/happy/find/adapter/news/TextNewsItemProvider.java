package com.mgc.letobox.happy.find.adapter.news;

import com.chad.library.adapter.base.BaseViewHolder;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.adapter.WeiboListAdapter;
import com.mgc.letobox.happy.find.bean.ArticleResultBean;


/**
 * @author ChayChan
 * @description: 纯文本新闻
 * @date 2018/3/22  14:36
 */
public class TextNewsItemProvider extends BaseNewsItemProvider {

    public TextNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    public int viewType() {
        return WeiboListAdapter.TEXT_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_weibo_text_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, ArticleResultBean news) {
         //由于文本消息的逻辑目前已经在基类中封装，所以此处无须写
        //定义此类是提供文本消息的ItemProvider
    }
}
