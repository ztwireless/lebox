package com.mgc.letobox.happy.find.view.richedittext.effects;


import com.mgc.letobox.happy.find.view.richedittext.span.BoldSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.Span;

/**
 * @author liye
 * @version 4.1.0
 * @since: 16/1/7 下午3:23
 */
public class BoldEffect extends Effect<Boolean> {
    @Override
    protected Class<? extends Span> getSpanClazz() {
        return BoldSpan.class;
    }

    @Override
    protected Span<Boolean> newSpan(Boolean value) {
        return value ? new BoldSpan() : null;
    }
}
