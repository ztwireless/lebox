package com.mgc.letobox.happy.sdk;

import android.content.Context;

import java.lang.reflect.Method;

public class TeaAgent {
	private final static String CLASSNAME = "com.ss.android.common.applog.TeaAgent";
	private final static String CONFIG_CLASSNAME = "com.ss.android.common.applog.TeaConfig";
	private static Class _klass;
	private static Class _configClass;

	private static void lazyInit() {
		try {
			if(_klass == null) {
				_klass = Class.forName(CLASSNAME);
			}
			if(_configClass == null) {
				_configClass = Class.forName(CONFIG_CLASSNAME);
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void onResume(Context ctx) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("onResume", Context.class);
			m.invoke(_klass, ctx);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void onPause(Context ctx) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("onPause", Context.class);
			m.invoke(_klass, ctx);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void init(Object var0) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("init", _configClass);
			m.invoke(_klass, var0);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
