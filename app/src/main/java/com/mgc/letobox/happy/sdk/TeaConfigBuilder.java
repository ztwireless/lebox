package com.mgc.letobox.happy.sdk;

import android.content.Context;

import java.lang.reflect.Method;

public class TeaConfigBuilder {
	private final static String CLASSNAME = "com.ss.android.common.applog.TeaConfigBuilder";
	private static Class _klass;
	private Object _builder;

	private static void lazyInit() {
		try {
			if (_klass == null) {
				_klass = Class.forName(CLASSNAME);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private TeaConfigBuilder(Object builder) {
		_builder = builder;
	}

	public static TeaConfigBuilder create(Context ctx) {
		try {
			lazyInit();
			Method m = _klass.getDeclaredMethod("create", Context.class);
			return new TeaConfigBuilder(m.invoke(_klass, ctx));
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public TeaConfigBuilder setAppName(String var1) {
		try {
			Method m = _klass.getDeclaredMethod("setAppName", String.class);
			m.invoke(_builder, var1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public TeaConfigBuilder setChannel(String var1) {
		try {
			Method m = _klass.getDeclaredMethod("setChannel", String.class);
			m.invoke(_builder, var1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public TeaConfigBuilder setAid(int var1) {
		try {
			Method m = _klass.getDeclaredMethod("setAid", int.class);
			m.invoke(_builder, var1);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}

	public Object createTeaConfig() {
		try {
			Method m = _klass.getDeclaredMethod("createTeaConfig");
			return m.invoke(_builder);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}
