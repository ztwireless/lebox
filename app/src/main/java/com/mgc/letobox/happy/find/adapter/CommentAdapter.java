package com.mgc.letobox.happy.find.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.leto.game.base.util.BaseAppUtil;
import com.leto.game.base.util.DensityUtil;
import com.leto.game.base.util.GlideUtil;
import com.leto.game.base.view.StarBar;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.bean.CommentBean;
import com.mgc.letobox.happy.find.model.Comment;
import com.mgc.letobox.happy.find.util.MgcFormatParse;
import com.mgc.letobox.happy.find.view.richedittext.utils.BitmapUtils;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<Comment, BaseViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context, List data) {
        super(R.layout.item_game_comment, data);
        mContext =context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Comment item) {
        TextView username = helper.getView(R.id.tv_name);
        ((TextView)helper.getView(R.id.tv_time)).setText(item.getCreate_time());
        username.setText(item.getUser().getNickname());

        GlideUtil.loadRoundedCorner(mContext,
            item.getUser().getAvatar128(),
            (ImageView) helper.getView(R.id.iv_avatar),
            20,
            R.mipmap.default_avatar);
        if(!TextUtils.isEmpty(item.getUser().getLevel_pic())) {
            Glide.with(mContext).load(item.getUser().getLevel_pic()).into((ImageView) helper.getView(R.id.iv_grade));
        }

        ((TextView)helper.getView(R.id.tv_comment_content)).setText(item.getContent());
        ((TextView)helper.getView(R.id.tv_like)).setText(""+item.getParse());
        ImageView iv_like = (ImageView) helper.getView(R.id.iv_like);
        if(item.getIs_support()==1){
            iv_like.setImageResource(R.mipmap.favorite_selected);
            ((TextView)helper.getView(R.id.tv_like)).setTextColor(mContext.getResources().getColor(R.color.text_blue));
        }else {
            iv_like.setImageResource(R.mipmap.favorite_unselect);
            ((TextView)helper.getView(R.id.tv_like)).setTextColor(mContext.getResources().getColor(R.color.text_lowgray));
        }
        helper.addOnClickListener(R.id.rl_like);
        helper.addOnClickListener(R.id.iv_avatar);

        StarBar starBar = helper.getView(R.id.rbar_score);


        starBar.setIntegerMark(true);
        starBar.setMarkCount(item.getUser().getGame_star()/2);
        starBar.setMark(item.getUser().getGame_star());

        LinearLayout ll_conntent = helper.getView(R.id.ll_content);
        ll_conntent.removeAllViews();

        if(null==item.getContent_new()) return;
        for (CommentBean comment:item.getContent_new()){
            if(comment.getDisplayType().equalsIgnoreCase(MgcFormatParse.TYPE_TEXT)){
                TextView textView = new TextView(mContext);
                textView.setTextSize(15);
                textView.setTextColor(mContext.getResources().getColor(R.color.text_black));
                textView.setText(comment.getEditorialCopy());
                ll_conntent.addView(textView);
            }else if(comment.getDisplayType().equalsIgnoreCase(MgcFormatParse.TYPE_INLINE_IMAGE)){
                final ImageView imageView = new ImageView(mContext);
                final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //imageView.setScaleType(ImageView.ScaleType.FIT_START);
                imageView.setAdjustViewBounds(true);

                Glide.with(mContext).asBitmap().load(comment.getArtwork().getUrl()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {

                        int width = bitmap.getWidth();
                        int sceen_width = BaseAppUtil.getDeviceWidth(mContext) - 2 * DensityUtil.dip2px(mContext, 10);
                        if(width>sceen_width){
                            params.width = sceen_width;
                        }else{
                            params.width = width;
                        }
                        //imageView.setLayoutParams(params);
                        Bitmap scaleBitmap = BitmapUtils.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), sceen_width, sceen_width *  bitmap.getHeight()/bitmap.getWidth());


                        imageView.setImageBitmap(scaleBitmap);
                    }
                });


                ll_conntent.addView(imageView);
            }
        }

    }
}