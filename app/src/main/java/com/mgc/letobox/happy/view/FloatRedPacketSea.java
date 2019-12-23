package com.mgc.letobox.happy.view;

import android.content.Context;
import android.support.annotation.NonNull;
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

    final TextView itemText;
    public FloatRedPacketSea(@NonNull Context context) {
        super(context);
        itemText = findViewById(R.id.itemText);
    }
    public void updateText(String text) {
        itemText.setText(text);
    }
}
