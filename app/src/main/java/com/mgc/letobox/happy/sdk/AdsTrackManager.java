package com.mgc.letobox.happy.sdk;

import android.content.Context;

import java.lang.reflect.Method;

public class AdsTrackManager {
	private static final String CLASSNAME = "com.mobgi.tool.adtrack.AdsTrackManager";
	private static Class _klass;
	private static Object _inst;

	private static void lazyInit() {
		try {
			if(_klass == null) {
				_klass = Class.forName(CLASSNAME);
			}
			if(_inst == null) {
				Method m = _klass.getDeclaredMethod("getInstance");
				_inst = m.invoke(_klass);
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void init(Context context, String appkey) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("init", Context.class, String.class);
			m.invoke(_inst, context, appkey);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void login(String playid) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("login", String.class);
			m.invoke(_inst, playid);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
