package com.mgc.letobox.happy.find.view.richedittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatEditText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import com.mgc.letobox.happy.find.view.richedittext.effects.Effects;
import com.mgc.letobox.happy.find.view.richedittext.span.EmojiSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.FakeImageSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.ImageSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.UrlSpan;
import com.mgc.letobox.happy.find.view.richedittext.utils.BitmapUtils;
import com.mgc.letobox.happy.find.view.richedittext.utils.RichTextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/24 下午3:56
 */
public class RichEditText extends AppCompatEditText {

//    private static final int IMAGE_MAX_WIDTH = DisplayUtils.getWidthPixels() - 200;
//    private static final int IMAGE_MAX_HEIGHT = DisplayUtils.getWidthPixels();

    private static final int IMAGE_MAX_WIDTH = 200;
    private static final int IMAGE_MAX_HEIGHT = 150;

    private Context mContext;

    private RichEditTextListener mListener;

    private IEmojiFactory mIEmojiFactory;

    public RichEditText(Context context) {
        super(context);
        mContext = context;
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setRichEditTextListener(RichEditTextListener listener) {
        mListener = listener;
    }

    public void setEmojiFactory(IEmojiFactory iEmojiFactory) {
        mIEmojiFactory = iEmojiFactory;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mListener != null) {
            mListener.onSelectionChanged(selStart, selEnd);
        }
    }
    public void addText(String text) {
        getText().insert(getSelectionStart(), text);
    }

    public void addImage(String localPath, String filePath) {
        SpannableString spannable = new SpannableString("\n<img src=\"" + filePath + "\"/>");
        Bitmap bitmap = BitmapUtils.decodeScaleImage(localPath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
        if (bitmap == null) {
            return;
        }
        ImageSpan span = new ImageSpan(mContext, bitmap, filePath);
        spannable.setSpan(span, 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().insert(getSelectionStart(), spannable);
    }

    public void addImage(String filePath) {
        SpannableString spannable = new SpannableString("\n<img src=\"" + filePath + "\"/>");
        Bitmap bitmap = BitmapUtils.decodeScaleImage(filePath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
        if (bitmap == null) {
            return;
        }
        Log.d("Bind Image","decode end....");
        ImageSpan span = new ImageSpan(mContext, bitmap, filePath);
        spannable.setSpan(span, 1, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().insert(getSelectionStart(), spannable);
        Log.d("Bind Image","insert end....");
    }

    public void addEmoji(Emoji emoji) {
        SpannableString spannable = new SpannableString(String.format("[%s]", emoji.getName()));
        EmojiSpan span = new EmojiSpan(mContext, emoji);
        spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().insert(getSelectionStart(), spannable);
    }

    public void replaceDownloadedImage(FakeImageSpan span, Bitmap bitmap, String url) {
        Bitmap scaledBitmap = BitmapUtils.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);

       // SpannableString spannable = new SpannableString("<img />");
        SpannableString spannable = new SpannableString("<img src=\"" + url + "\"/>");
        ImageSpan imageSpan = new ImageSpan(mContext, scaledBitmap, null);
        imageSpan.setUrl(url);
        spannable.setSpan(imageSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().replace(getText().getSpanStart(span), getText().getSpanEnd(span), spannable);
    }

    public void replaceLocalImage(FakeImageSpan span, String filePath) {
        Bitmap bitmap = BitmapUtils.decodeScaleImage(filePath, IMAGE_MAX_WIDTH, IMAGE_MAX_HEIGHT);
        if (bitmap == null) {
            return;
        }
        SpannableString spannable = new SpannableString("<img src=\"" + filePath + "\"/>");
        ImageSpan imageSpan = new ImageSpan(mContext, bitmap, filePath);
        spannable.setSpan(imageSpan, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().replace(getText().getSpanStart(span), getText().getSpanEnd(span), spannable);
    }

    public void addLink(String linkName, String url) {
        SpannableString spannable = new SpannableString(linkName);
        spannable.setSpan(new UrlSpan(url), 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        getText().insert(getSelectionStart(), spannable);
    }

    public void addHref(int start, int end, String text) {
        UrlSpan[] spans = getText().getSpans(start, end, UrlSpan.class);
        if (spans.length > 0) {
            if (start == getText().getSpanStart(spans[0])
                    && end == getText().getSpanEnd(spans[0])) {
                return;
            }
            for (UrlSpan span : spans) {
                getText().removeSpan(span);
            }
        }
        //如果还没有添加span
        getText().setSpan(new UrlSpan(text),
                start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
    }

    public void setRichText(String richText) {
        setText(RichTextUtils.convertRichTextToSpanned(getContext(), richText, mIEmojiFactory));
    }

    public String getRichText() {
        return RichTextUtils.convertSpannedToRichText(getText());
    }
    public String getMgcRichText(Map<String, String> fileMap) {
        return RichTextUtils.convertSpannedToRichText(getText(),fileMap);
    }


    public FakeImageSpan[] getFakeImageSpans() {
        return getText().getSpans(0, getText().length(), FakeImageSpan.class);
    }

    public List<ImageSpan> getToUploadImageSpanList() {
        ImageSpan[] imageSpan = getText()
                .getSpans(0, getText().length(), ImageSpan.class);
        ArrayList<ImageSpan> spanList = new ArrayList<>();
        for (ImageSpan imgSpan : imageSpan) {
            if (TextUtils.isEmpty(imgSpan.getUrl())) {
                spanList.add(imgSpan);
            }
        }
        return spanList;
    }

    public void applyBoldEffect(int start, int end, boolean value) {
        Effects.BOLD.apply(this, start, end, value);
    }

    public void applyBoldEffect(boolean value) {
        Effects.BOLD.applyToSelection(this, value);
    }

    public boolean getBoldEffectValue() {
        return Effects.BOLD.existsInSelection(this, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
