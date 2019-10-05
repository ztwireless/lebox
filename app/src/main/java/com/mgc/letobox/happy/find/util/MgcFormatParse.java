package com.mgc.letobox.happy.find.util;

import com.google.gson.JsonObject;

/**
 * Create by zhaozhihui on 2018/9/6
 **/
public class MgcFormatParse {
    private static String TAG_TYPE =  "displayType";
    private static String TAG_TEXT =  "editorialCopy";
    private static String TAG_ARTWORK =  "artwork";
    private static String TAG_URL =  "url";

    public static String TYPE_TEXT =  "TextBlock";
    public static String TYPE_INLINE_IMAGE =  "InlineImage";

    public static JsonObject convertText(String text){
        JsonObject json = new JsonObject();
        json.addProperty(TAG_TYPE, TYPE_TEXT);
        json.addProperty(TAG_TEXT, text.trim());
        return json;
    }

    public static JsonObject convertImage(String path){
        JsonObject json = new JsonObject();
        json.addProperty(TAG_TYPE, TYPE_INLINE_IMAGE);
        JsonObject art =new JsonObject();
        art.addProperty(TAG_URL, path);
        json.add(TAG_ARTWORK, art);
        return json;
    }
}
