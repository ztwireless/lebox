package com.mgc.letobox.happy.me.holder;

import android.support.annotation.Keep;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * view holder的通用基类, 封装了一些通用字段和功能
 */
@Keep
public abstract class CommonViewHolder<T> extends RecyclerView.ViewHolder {
	public CommonViewHolder(View itemView) {
		super(itemView);
	}

	public abstract void onBind(T model, int position);

	public View getItemView() {
		return itemView;
	}

	public ViewGroup _adContainer;
	public void  setAdContainer(ViewGroup adContainer){
		_adContainer =  adContainer;
	}
}
