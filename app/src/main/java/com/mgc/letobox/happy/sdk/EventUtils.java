package com.mgc.letobox.happy.sdk;

import java.lang.reflect.Method;

public class EventUtils {
	private static final String CLASSNAME = "com.ss.android.common.lib.EventUtils";
	private static Class _klass;

	private static void lazyInit() {
		try {
			if(_klass == null) {
				_klass = Class.forName(CLASSNAME);
			}
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void setLogin(String var0, boolean var1) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("setLogin", String.class, boolean.class);
			m.invoke(_klass, var0, var1);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static void setRegister(String var0, boolean var1) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("setRegister", String.class, boolean.class);
			m.invoke(_klass, var0, var1);
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
