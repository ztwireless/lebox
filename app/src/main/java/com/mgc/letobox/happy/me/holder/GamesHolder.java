package com.mgc.letobox.happy.me.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ledong.lib.leto.mgc.holder.MyGameHolder;
import com.mgc.leto.game.base.bean.GameModel;
import com.mgc.leto.game.base.login.LoginManager;
import com.mgc.leto.game.base.utils.GameUtil;
import com.mgc.leto.game.base.utils.MResource;
import com.mgc.letobox.happy.me.bean.MeModuleBean;

import java.util.List;

public class GamesHolder extends CommonViewHolder<MeModuleBean> {
    View _splitSpace;
    // views
    RecyclerView _listView;
    LinearLayout _gameView;

    Context _ctx;
    private List<GameModel> _myGames;

    View rootView;

    RecentAdapter mAdapter;

    public static GamesHolder create(Context ctx, ViewGroup parent) {
        View convertView = LayoutInflater.from(ctx).inflate(MResource.getIdByName(ctx, "R.layout.leto_mgc_me_game"), parent, false);
        return new GamesHolder(ctx, convertView);
    }

    public GamesHolder(Context context, View itemView) {
        super(itemView);

        rootView = itemView;

        // find views
        _ctx = context;
        _splitSpace = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.split_space"));
        _listView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.list"));
        _gameView = itemView.findViewById(MResource.getIdByName(_ctx, "R.id.my_games_panel"));

        _listView.setLayoutManager(new GridLayoutManager(_ctx, 1, RecyclerView.HORIZONTAL, false));

        mAdapter = new RecentAdapter();
        _listView.setAdapter(mAdapter);


    }

    @Override
    public void onBind(final MeModuleBean model, int position) {

        _splitSpace.setVisibility(position == 0 ? View.GONE : View.VISIBLE);

        // label
        _myGames = GameUtil.loadGameList(_ctx, LoginManager.getUserId(_ctx), GameUtil.USER_GAME_TYPE_PLAY);
        mAdapter.notifyDataSetChanged();

        // hide my games or setup my game list
        if(_myGames == null || _myGames.isEmpty()) {
            _gameView.setVisibility(View.GONE);
            rootView.setVisibility(View.GONE);
        }else{
            _gameView.setVisibility(View.VISIBLE);
            rootView.setVisibility(View.VISIBLE);
        }
    }



    private class RecentAdapter extends RecyclerView.Adapter<MyGameHolder> {
        @NonNull
        @Override
        public MyGameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return MyGameHolder.create(_ctx);
        }

        @Override
        public void onBindViewHolder(@NonNull MyGameHolder holder, int position) {
            holder.onBind(_myGames.get(position), position);
        }

        @Override
        public int getItemCount() {
            return _myGames == null ? 0 : _myGames.size();
        }
    }



//    public void loadRecentGame(){
//        _myGames = GameUtil.loadGameList(_ctx, LoginManager.getUserId(_ctx), GameUtil.USER_GAME_TYPE_PLAY);
//        if(_myGames == null || _myGames.isEmpty()) {
//            _myGamesPanel.setVisibility(View.GONE);
//        } else {
//            _myGamesPanel.setVisibility(View.VISIBLE);
//            _listView.getAdapter().notifyDataSetChanged();
//        }
//
//    }
}
