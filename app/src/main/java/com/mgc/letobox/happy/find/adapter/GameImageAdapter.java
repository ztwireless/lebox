package com.mgc.letobox.happy.find.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.ui.ShowPicVPActivity;

import java.util.ArrayList;

/**
 * Created by liu hong liang on 2016/10/11.
 */

public class GameImageAdapter extends RecyclerView.Adapter<GameImageAdapter.ViewHolder> {
    private ArrayList<String> data=new ArrayList();
    public GameImageAdapter(ArrayList<String> data) {
        this.data = data;
    }
    @Override
    public GameImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_game_image, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(GameImageAdapter.ViewHolder holder, final int position) {
        Glide.with(holder.ivGameImg.getContext()).load(data.get(position)).into(holder.ivGameImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPicVPActivity.start(v.getContext(),data,position,false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivGameImg;

        ViewHolder(View view) {
            super(view);

            // find view
            ivGameImg = view.findViewById(R.id.iv_game_img);
        }
    }
}
