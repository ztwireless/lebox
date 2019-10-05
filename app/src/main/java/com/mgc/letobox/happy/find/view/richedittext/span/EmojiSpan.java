package com.mgc.letobox.happy.find.view.richedittext.span;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.leto.game.base.util.DensityUtil;
import com.mgc.letobox.happy.find.view.richedittext.Emoji;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/1/12 下午3:28
 */
public class EmojiSpan extends ImageSpan {
    private static final int EMOJI_DISPLAY_SIZE = 22;

    private String mName;

    public EmojiSpan(Context context, Emoji emoji) {
        super(convertBitmapToSizedDrawable(context, emoji.getBitmap()));
        mName = emoji.getName();
        setUrl(emoji.getUrl());
    }

    public String getName() {
        return mName;
    }

    private static Drawable convertBitmapToSizedDrawable(Context context, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        drawable.setBounds(0, 0, DensityUtil.dip2px(context, EMOJI_DISPLAY_SIZE),
                DensityUtil.dip2px(context, EMOJI_DISPLAY_SIZE));
        return drawable;
    }
}
