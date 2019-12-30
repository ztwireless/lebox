package com.mgc.letobox.happy.floattools.components;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mgc.letobox.happy.R;

public class RedPacketSeaActivity extends Activity {
    public static final String KEY_GAME_ID = "game_id";
    public static final String KEY_COIN_COUNT = "coin_count";
    public static final String KEY_COIN_MULTIPLE = "coin_multiple";

    public static void start(Activity activity, String gameId, int coinCount,int coinMultiple) {
        Intent intent = new Intent(activity, RedPacketSeaActivity.class);
        intent.putExtra(KEY_GAME_ID, gameId);
        intent.putExtra(KEY_COIN_COUNT, coinCount);
        intent.putExtra(KEY_COIN_MULTIPLE, coinMultiple);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpacket_sea);

        String gameId = getIntent().getStringExtra(KEY_GAME_ID);
        int coinCount = getIntent().getIntExtra(KEY_COIN_COUNT, 0);
        int coinMultiple = getIntent().getIntExtra(KEY_COIN_MULTIPLE, 1);
        RedPacketSeaFragment fragment = new RedPacketSeaFragment();
        Bundle args = new Bundle();
        args.putString(RedPacketSeaFragment.KEY_GAME_ID, gameId);
        args.putInt(RedPacketSeaFragment.KEY_COIN_COUNT, coinCount);
        args.putInt(RedPacketSeaFragment.KEY_COIN_MULTIPLE, coinMultiple);
        fragment.setArguments(args);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }
}
