package com.mgc.letobox.happy.find.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mgc.letobox.happy.find.intf.OnItemClickListener;

/**
 * Created by lixb on 2017/4/5.
 */

public abstract class BaseRecyclerAdapter<VH extends BaseRecyclerAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {


    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(VH holder, int position);

    @Override
    public abstract int getItemCount();

    public class BaseViewHolder extends RecyclerView.ViewHolder{

        public BaseViewHolder(final View itemView) {
            super(itemView);
            if (mOnItemClickListener != null)
            {
                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        int pos = getLayoutPosition();
                        mOnItemClickListener.onItemClick(itemView, pos);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View v)
                    {
                        int pos = getLayoutPosition();
                        mOnItemClickListener.onItemLongClick(itemView, pos);
                        return false;
                    }
                });
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
