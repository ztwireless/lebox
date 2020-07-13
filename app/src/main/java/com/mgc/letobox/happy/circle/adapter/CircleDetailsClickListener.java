package com.mgc.letobox.happy.circle.adapter;

import android.widget.CheckBox;
import android.widget.ImageView;

import com.mgc.letobox.happy.circle.bean.CircleTieZiListResponse;

public interface CircleDetailsClickListener {
	void onClick(int pos, CircleTieZiListResponse news);

	void onShowCircle(ImageView imageView, int pos, int editext, int detele, CircleTieZiListResponse response);

	void onCheckBox(boolean isCheck, CheckBox cb_follow, int userId, int pos);
}