package com.example.util;

import android.content.Context;

public class Config {
	
	public static final String SYSTEM_REASON = "reason";
	public static final String SYSTEM_HOME_KEY = "homekey";
	public static final String SYSTEM_HOME_KEY_LONG = "recentapps";
	
	// 改：这里将更改为runtime中获取更为稳妥
	public static final String LAUNCHER_ACT = "com.example.activity.MainActivity";
	
	public static final int MAIN_EFCTXT_NUM = 4;
	
}

class StrUtil {
	
	public static String getCorStr(Context context, int resID) {
		String str = null;
		String[] mLeastStrs = context.getResources().getStringArray(resID);
		// 产生0 ~ .length-1的随机数
		int randNum = (int) (Math.random() * mLeastStrs.length);
		str = mLeastStrs[randNum];
		return str;
	}
	
}