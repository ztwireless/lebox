package com.mgc.letobox.happy.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgc.letobox.happy.R;
import com.mgc.letobox.happy.find.model.Image;

import java.util.List;


public class SelectedImageAdapter extends BaseRecyclerAdapter<SelectedImageAdapter.MyViewHolder> {
    private List<Image> selectImages;
    private View rlClickCreame;
    Context mContext;

    int mImageSize;

    public SelectedImageAdapter(Context context, List<Image> selectImages, View rlClickCreame, int maxSize) {
        this.mContext = context;
        this.selectImages = selectImages;
        this.rlClickCreame = rlClickCreame;
        this.mImageSize = maxSize;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.selected_image_item, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position < selectImages.size()) {
            Image image = selectImages.get(position);
            if (image.getType() == 0) {
                holder.imgDelete.setVisibility(View.VISIBLE);
                holder.tvDuration.setVisibility(View.GONE);
            } else if (image.getType() == 1) {
                holder.imgDelete.setVisibility(View.GONE);
                holder.tvDuration.setVisibility(View.VISIBLE);
                int duration = image.getDuration();
                int second = duration / 1000 % 60;
                String s = String.valueOf(second);
                if (second < 10) {
                    s = "0" + second;
                }
                int minute = duration / 1000 / 60;
                holder.tvDuration.setText(minute + ":" + s);
            }
            Glide.with(mContext).load(image.getPath()).into(holder.ivImage);
        } else {
            holder.tvDuration.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.GONE);
            Glide.with(mContext).load(R.mipmap.icon_add).into(holder.ivImage);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < selectImages.size()) {
                    selectImages.remove(position);
                    notifyDataSetChanged();
                }
                if (selectImages.size() <= 0) {
                    rlClickCreame.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (selectImages.size() > 0 && selectImages.size() < mImageSize && selectImages.get(0).getType() == 0) {
            return selectImages.size() + 1;
        }
        return selectImages.size();
    }

    public class MyViewHolder extends BaseRecyclerAdapter.BaseViewHolder {
        private ImageView ivImage, imgDelete;
        private TextView tvDuration;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
            tvDuration = (TextView) itemView.findViewById(R.id.tv_duration);
        }
    }
}
