package com.mgc.letobox.happy.floattools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mgc.letobox.happy.R;

public class RedPacketSeaActivity extends Activity {
    public static final String KEY_GAME_ID = "game_id";
    public static final String KEY_COIN_COUNT = "coin_count";

    public static void start(Activity activity, String gameId, int coinCount) {
        Intent intent = new Intent(activity, RedPacketSeaActivity.class);
        intent.putExtra(KEY_GAME_ID, gameId);
        intent.putExtra(KEY_COIN_COUNT, coinCount);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpacket_sea);

        String gameId = getIntent().getStringExtra(KEY_GAME_ID);
        int coinCount = getIntent().getIntExtra(KEY_COIN_COUNT, 0);
        RedPacketSeaFragment fragment = new RedPacketSeaFragment();
        Bundle args = new Bundle();
        args.putString(RedPacketSeaFragment.KEY_GAME_ID, gameId);
        args.putInt(RedPacketSeaFragment.KEY_COIN_COUNT, coinCount);
        fragment.setArguments(args);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }
}
