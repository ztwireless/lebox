package com.mgc.letobox.happy.find.view.richedittext.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mgc.letobox.happy.find.bean.CommentBean;
import com.mgc.letobox.happy.find.util.MgcFormatParse;
import com.mgc.letobox.happy.find.view.richedittext.IEmojiFactory;
import com.mgc.letobox.happy.find.view.richedittext.span.BoldSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.EmojiSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.FakeImageSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.ImageSpan;
import com.mgc.letobox.happy.find.view.richedittext.span.UrlSpan;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author liye
 * @version 4.1.0
 * @since: 15/12/28 下午2:27
 */
public class RichTextUtils {
    public static Spanned convertRichTextToSpanned(Context ctx, String richText, IEmojiFactory iEmojiFactory) {
        return RichTextConverter.fromRichText(ctx, richText, iEmojiFactory);
    }

    public static String convertSpannedToRichText(Spanned spanned) {
        int length = spanned.length();
        List<CharacterStyle> spanList =
                Arrays.asList(spanned.getSpans(0, spanned.length(), CharacterStyle.class));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spanned);
        int mStart =0;
        StringBuffer richTextContent = new StringBuffer();
        JsonArray richTextJsonArray = new JsonArray();
        for (CharacterStyle characterStyle : spanList) {
            int start = stringBuilder.getSpanStart(characterStyle);
            int end = stringBuilder.getSpanEnd(characterStyle);
            if (characterStyle instanceof ImageSpan){
                if (start >= 0) {
                   JsonObject imageJson =  MgcFormatParse.convertImage(((ImageSpan) characterStyle).getFilePath());
                    if(start>mStart){
                        JsonObject textJson =  MgcFormatParse.convertText(stringBuilder.subSequence(mStart, start).toString());
                        richTextJsonArray.add(textJson);
                    }
                    richTextJsonArray.add(imageJson);
                }
            }else {
                if (start >= 0) {
                    String htmlStyle = handleCharacterStyle(characterStyle,
                            stringBuilder.subSequence(start, end).toString());
                    if (htmlStyle != null) {
                        stringBuilder.replace(start, end, htmlStyle);
                    }
                }
            }
            mStart = end;

//            int start = stringBuilder.getSpanStart(characterStyle);
//            int end = stringBuilder.getSpanEnd(characterStyle);
//            if (start >= 0) {
//                String htmlStyle = handleCharacterStyle(characterStyle,
//                        stringBuilder.subSequence(start, end).toString());
//                if (htmlStyle != null) {
//                    stringBuilder.replace(start, end, htmlStyle);
//                }
//            }
        }
        if(mStart<spanned.length()){
            JsonObject textJson =  MgcFormatParse.convertText(stringBuilder.subSequence(mStart, spanned.length()).toString());
            richTextJsonArray.add(textJson);
        }
        return richTextJsonArray.toString();
    }

    private static String handleCharacterStyle(CharacterStyle characterStyle, String text) {
        if (characterStyle instanceof BoldSpan) {
            return String.format("<b>%s</b>", text);
        } else if (characterStyle instanceof UrlSpan) {
            UrlSpan span = (UrlSpan) characterStyle;
            return String.format("<a href=\"%s\">%s</a>", span.getValue(), text);
        } else if (characterStyle instanceof EmojiSpan) {
            EmojiSpan span = (EmojiSpan) characterStyle;
            return String.format("<img src=\"%s\" alt=\"[%s]\" class=\"yiqiFace\"/>",
                    span.getUrl(), span.getName());
        } else if (characterStyle instanceof FakeImageSpan) {
            FakeImageSpan span = (FakeImageSpan) characterStyle;
            return String.format("<img src=\"%s\" />", span.getValue());
        } if (characterStyle instanceof ImageSpan) {
            ImageSpan span = (ImageSpan) characterStyle;
            return String.format("<img src=\"%s\" />", TextUtils.isEmpty(span.getUrl()) ?
                    span.getFilePath() : span.getUrl());
        }
        return null;
    }


    public static String convertSpannedToRichText(Spanned spanned, Map<String, String> filepaths) {
        int length = spanned.length();
        List<CharacterStyle> spanList =
                Arrays.asList(spanned.getSpans(0, spanned.length(), CharacterStyle.class));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spanned);
        int mStart =0;
        StringBuffer richTextContent = new StringBuffer();
        JsonArray richTextJsonArray = new JsonArray();
        for (CharacterStyle characterStyle : spanList) {
            int start = stringBuilder.getSpanStart(characterStyle);
            int end = stringBuilder.getSpanEnd(characterStyle);
            if (characterStyle instanceof  ImageSpan){
                if (start >= 0) {
                    String localpath =((ImageSpan) characterStyle).getFilePath();
                    String filepath = filepaths.get(localpath);
                    if(TextUtils.isEmpty(filepath)){
                        filepath = localpath;
                    }
                    JsonObject imageJson =  MgcFormatParse.convertImage(filepath);
                    if(start>mStart){
                        JsonObject textJson =  MgcFormatParse.convertText(stringBuilder.subSequence(mStart, start).toString());
                        richTextJsonArray.add(textJson);
                    }
                    richTextJsonArray.add(imageJson);
                }
            }else if (characterStyle instanceof  FakeImageSpan){
                if (start >= 0) {
                    String localpath =((FakeImageSpan) characterStyle).getValue();
                    String filepath = filepaths.get(localpath);
                    if(TextUtils.isEmpty(filepath)){
                        filepath = localpath;
                    }
                    JsonObject imageJson =  MgcFormatParse.convertImage(filepath);
                    if(start>mStart){
                        JsonObject textJson =  MgcFormatParse.convertText(stringBuilder.subSequence(mStart, start).toString());
                        richTextJsonArray.add(textJson);
                    }
                    richTextJsonArray.add(imageJson);
                }
            }else {
                if (start >= 0) {
                    String htmlStyle = handleCharacterStyle(characterStyle,
                            stringBuilder.subSequence(start, end).toString());
                    if (htmlStyle != null) {
                        stringBuilder.replace(start, end, htmlStyle);
                    }
                }
            }
            mStart = end;
        }
        if(mStart<spanned.length()){
            JsonObject textJson =  MgcFormatParse.convertText(stringBuilder.subSequence(mStart, spanned.length()).toString());
            richTextJsonArray.add(textJson);
        }
        return richTextJsonArray.toString();
    }

    public static String convertMgcToRichText(List<CommentBean> comments) {
        if(comments==null) return null;

        StringBuffer content = new StringBuffer();
        for (int i=0; i<comments.size(); i++){
            if(comments.get(i).getDisplayType().equalsIgnoreCase(MgcFormatParse.TYPE_TEXT)){
                content.append(comments.get(i).getEditorialCopy());
            }else if(comments.get(i).getDisplayType().equalsIgnoreCase(MgcFormatParse.TYPE_INLINE_IMAGE)){
                SpannableString spannable = new SpannableString("\n<img src=\"" + comments.get(i).getArtwork().getUrl() + "\"/>");
                content.append(spannable);
            }
        }

        return content.toString();
    }
}
