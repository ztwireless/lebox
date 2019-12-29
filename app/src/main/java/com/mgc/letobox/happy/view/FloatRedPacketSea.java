package com.mgc.letobox.happy.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.util.LeBoxSpUtil;

import java.util.Timer;
import java.util.TimerTask;

public class FloatRedPacketSea extends BaseFloatToolView {
    @Override
    protected int getLayoutId() {
        return R.layout.layout_red_packet_sea;
    }

    private AnimationDrawable mAnimationDrawable;
    final TextView itemText;
    public FloatRedPacketSea(@NonNull Context context) {
        super(context);
        itemText = findViewById(R.id.itemText);
        ImageView itemImage = findViewById(R.id.itemImage);
        mAnimationDrawable = (AnimationDrawable) itemImage.getDrawable();
        mAnimationDrawable.start();
    }
    public void updateText(String text) {
        itemText.setText(text);
    }
}
