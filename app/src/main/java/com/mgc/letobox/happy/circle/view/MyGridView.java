package com.mgc.letobox.happy.circle.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leto.game.base.util.GlideUtil;
import com.mgc.letobox.happy.R;

/**
 * Created by DELL on 2018/6/28.
 */

public class MyGridView extends LinearLayout {

    private Context context;
    private View title_line,foot_line;
    private ImageView imageView;
    private TextView textView_game_name,textView_koL_name;

    public MyGridView(Context context) {
        super(context);
        init(context);
    }

    public MyGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        this.setOrientation(VERTICAL);
        this.context = context;
        View.inflate(context, R.layout.circle_grid_view,this);
        title_line = findViewById(R.id.title_line);
        foot_line = findViewById(R.id.foot_line);
        textView_game_name = findViewById(R.id.textView_game_name);
        textView_koL_name = findViewById(R.id.textView_koL_name);
        imageView = findViewById(R.id.imageView);
    }

    public void setData(String text,String content,String url) {
        textView_game_name.setText(text);
        textView_koL_name.setText(content);
        GlideUtil.loadRoundedCorner(context,
            url,
            imageView,
            5,
            R.mipmap.circle_def);
    }
}
