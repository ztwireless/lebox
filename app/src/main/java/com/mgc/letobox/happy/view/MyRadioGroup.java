package com.mgc.letobox.happy.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MyRadioGroup extends LinearLayout {
	public interface OnCheckedChangeListener {
		/**
		 * <p>Called when the checked radio button has changed. When the
		 * selection is cleared, checkedId is -1.</p>
		 *
		 * @param group the group in which the checked radio button has changed
		 * @param checkedId the unique identifier of the newly checked radio button
		 */
		void onCheckedChanged(MyRadioGroup group, @IdRes int checkedId);
	}

	private ArrayList<View> mCheckables = new ArrayList<>();
	private OnCheckedChangeListener _listener = null;

	public MyRadioGroup(Context context) {
		super(context);
	}

	public MyRadioGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyRadioGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
		_listener = l;
	}

	@Override
	public void addView(View child, int index,
						android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		parseChild(child);
	}

	public void check(@IdRes int id) {
		for(View v : mCheckables) {
			if(v.getId() == id) {
				((Checkable)v).setChecked(true);
				if(_listener != null) {
					_listener.onCheckedChanged(this, id);
				}
			} else {
				((Checkable)v).setChecked(false);
			}
		}
	}

	public void parseChild(final View child) {
		if(child instanceof Checkable) {
			mCheckables.add(child);
			child.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					for(int i = 0; i < mCheckables.size(); i++) {
						Checkable view = (Checkable) mCheckables.get(i);
						if(view == v) {
							((Checkable) view).setChecked(true);
							if(_listener != null) {
								_listener.onCheckedChanged(MyRadioGroup.this, v.getId());
							}
						} else {
							((Checkable) view).setChecked(false);
						}
					}
				}
			});
		} else if(child instanceof ViewGroup) {
			parseChildren((ViewGroup) child);
		}
	}

	public void parseChildren(final ViewGroup child) {
		for(int i = 0; i < child.getChildCount(); i++) {
			parseChild(child.getChildAt(i));
		}
	}
}