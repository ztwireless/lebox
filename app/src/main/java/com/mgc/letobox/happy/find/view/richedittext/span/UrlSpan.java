package com.mgc.letobox.happy.find.view.richedittext.span;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;


/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/28 下午4:00
 */
public class UrlSpan extends ForegroundColorSpan implements Span<String> {
    private String mUrl;

    public UrlSpan(String url) {
        super(Color.BLUE);
        mUrl = url;
    }

    @Override
    public String getValue() {
        return mUrl;
    }
}
