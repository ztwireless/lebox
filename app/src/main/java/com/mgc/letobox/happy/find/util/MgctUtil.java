package com.mgc.letobox.happy.find.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgc.letobox.happy.find.model.NewsCategory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MgctUtil {

    public static final String TOP_ISSUE_FILE_NAME = "top_game_issue";
    public static final String PROFILE_LEVEL = "profile_level";

    public static final String TRADE_BANNER = "trad_banner";
    public static final String NEWS_CATEGORY = "news_category";

    public static boolean haveSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String timesTwo(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static void  saveJson(Context context, String jsonString, String filename)  {

        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(filename,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonString);
        }catch (IOException e){
           e.printStackTrace();
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String loadStringFromFile(Context context, String fileName) {
        BufferedReader reader = null;
        String content = null;
        try {
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            content = jsonString.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return  content;
    }

    public static boolean hasLocalNewsCategory(Context context) {
        String content = loadStringFromFile (context, NEWS_CATEGORY);
        return !TextUtils.isEmpty(content);
    }

    public static ArrayList<NewsCategory> loadNewsCategory(Context context){
        ArrayList<NewsCategory> categories = new ArrayList<NewsCategory>();

        String content = loadStringFromFile (context, NEWS_CATEGORY);
        if(!TextUtils.isEmpty(content)){
            categories = new Gson().fromJson(content, new TypeToken<List<NewsCategory>>() {
            }.getType());
        }
        return categories;
    }

    /**
     * 旋转动画
     *
     * @return
     */
    public static Animation rotaAnimation() {
        RotateAnimation ra = new RotateAnimation(0, 355,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setDuration(888);
        ra.setRepeatCount(-1);
        ra.setStartOffset(0);
        ra.setRepeatMode(Animation.RESTART);
        return ra;
    }
}
