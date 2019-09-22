package com.mgc.letobox.happy;

import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment {
	private boolean _started;

	@Override
	public void onResume() {
		super.onResume();
		if(!_started) {
			_started = true;
			MobclickAgent.onPageStart(getClass().getSimpleName());
		}
	}

//	@Override
//	public void onPause() {
//		super.onPause();
//		MobclickAgent.onPageEnd(getClass().getName());
//	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(hidden) {
			if(_started) {
				_started = false;
				MobclickAgent.onPageEnd(getClass().getSimpleName());
			}
		} else {
			if(!_started) {
				_started = true;
				MobclickAgent.onPageStart(getClass().getSimpleName());
			}
		}
	}
}
