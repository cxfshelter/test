package com.example.util;

public class TimeMgr {
	
	public enum TimeState {
		TIME_NONE,	// 非挑战
		TIME_ING,	// 挑战中
		// TIME_END,	// 挑战结束
	};
	
	private static TimeState mState = TimeState.TIME_NONE;
	
	// static int = sn_
	private static int n_time = 0;
		
	public static void resetTime() {
		n_time = 0;
	}
		
	public static void setTime() {
		if(n_time < 0) {
			n_time = 0;
		}
		else {
			++n_time;
		}
	}
		
	public static int getTime() {
		if(n_time < 0) {
			n_time = 0;
		}
		
		return n_time;
	}
		
	public static void setState(TimeState state) {
		mState = state;
	}
		
	public static TimeState getState() {
		return mState;
	}
	
}
