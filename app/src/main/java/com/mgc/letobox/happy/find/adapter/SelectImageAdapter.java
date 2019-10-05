package com.mgc.letobox.happy.find.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.model.Image;
import com.mgc.letobox.happy.find.ui.SelectImageActivity;

import java.util.ArrayList;

/**
 * Created by lixiaobin on 2017/4/5.
 */

public class SelectImageAdapter extends BaseRecyclerAdapter<SelectImageAdapter.MyViewHolder> {
    private Activity mActivity;
    private ArrayList<Image> images;
    public SelectImageAdapter(SelectImageActivity activity){
        this.mActivity = activity;
    }


    public SelectImageAdapter(Activity activity, ArrayList<Image> images){
        this.mActivity = activity;
        this.images = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.select_image_item,null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(position==0){
            Glide.with(mActivity).load(R.mipmap.camera).into(holder.ivImage);
            holder.imgSelect.setVisibility(View.GONE);
            holder.cbSelected.setVisibility(View.GONE);
            holder.tvDuration.setVisibility(View.GONE);
        }else{
            holder.cbSelected.setVisibility(View.VISIBLE);
            Image image = images.get(position-1);
            Glide.with(mActivity).load(image.getPath()).into(holder.ivImage);
            if(image.getType()==0){
                holder.tvDuration.setVisibility(View.GONE);
                if(image.isSelect()){
                    holder.imgSelect.setVisibility(View.VISIBLE);
                    holder.cbSelected.setSelected(true);
                }else{
                    holder.imgSelect.setVisibility(View.GONE);
                    holder.cbSelected.setSelected(false);
                }
            }else if(image.getType()==1){//视频
                holder.imgSelect.setVisibility(View.GONE);
                holder.cbSelected.setVisibility(View.GONE);
                holder.tvDuration.setVisibility(View.VISIBLE);
                int duration = image.getDuration();
                int second = duration/1000%60;
                String s = String.valueOf(second);
                if(second<10){
                    s = "0"+second;
                }
                int minute = duration/1000/60;
                holder.tvDuration.setText(minute+":"+s);
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size()+1;
    }

    public class MyViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        private ImageView ivImage,imgSelect,cbSelected;
        private TextView tvDuration;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            imgSelect = (ImageView) itemView.findViewById(R.id.img_select);
            cbSelected = (ImageView) itemView.findViewById(R.id.cb_selected);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }
    }

    public void setImages(ArrayList<Image> images){
        this.images = images;
    }
}
