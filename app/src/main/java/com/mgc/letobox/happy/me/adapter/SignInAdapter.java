package com.mgc.letobox.happy.me.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ledong.lib.minigame.bean.GameCenterData_Signin;
import com.mgc.letobox.happy.me.holder.SigninHolder;

import java.util.List;

/**
 * Create by zhaozhihui on 2019-09-10
 **/
public class SignInAdapter extends RecyclerView.Adapter<SigninHolder> {

    Context _context;

    List<GameCenterData_Signin> _list;

    ViewGroup _adContainer;

    public SignInAdapter(Context context, List<GameCenterData_Signin> dataList) {
        _context = context;

        _list = dataList;
    }

    @NonNull
    @Override
    public SigninHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return SigninHolder.create(_context, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SigninHolder holder, int position) {
        holder.onBind(_list.get(position), position);

        holder.setAdContainer(_adContainer);
    }

    @Override
    public int getItemCount() {
        return _list == null ? 0 : _list.size();
    }


    public void setAdContainer(ViewGroup adContainer){
        _adContainer = adContainer;
    }
}