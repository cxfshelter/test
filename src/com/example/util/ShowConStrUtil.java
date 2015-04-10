package com.example.util;

import com.example.test2.R;

import android.content.Context;

/**
 * 
 * @author ZZ
 *
 */
public class ShowConStrUtil {
	
	/** 
	 * 挑逗语句的几种状态
	 * 与历史最好成绩相比：小于、等于（前后相差小于5%）、大于
	 * 特殊情况：小于1分钟
	 */
	public enum TeaseState {
		LEAST_ONE_MIN,		// 小于1分钟
		LESS_THAN_BEST,		// 大于1分钟小于最好成绩
		EQUAL_THAN_BEST,	// 等于最好成绩（前后相差小于5%）
		LARGER_THAN_BEST,	// 大于最好成绩（相差大于5%）
	};
	
	private Context mContext;
	
	private TeaseState mTeaseState;
	
	public ShowConStrUtil(Context context) {
		mContext = context;
	}
	
	public static ShowConStrUtil createEmptyWorkingUtil(Context context, int time) {
		ShowConStrUtil work = new ShowConStrUtil(context);
		work.setTeaseState(time);
		return work;
	}
	
	/**
	 * 	成绩出来三种情况（与历史最好成绩对比）：小于、等于、大于；
	 *  暂不对最终成绩进行筛选
	 */
	public void setTeaseState(int time) {
		mTeaseState = TeaseState.LEAST_ONE_MIN;
	}
	
	public TeaseState getTeaseState() {
		return mTeaseState;
	}
	
	public String getTeaseStr() {
		String str = null;
		if(mTeaseState == TeaseState.LEAST_ONE_MIN) {
			String[] mLeastStrs = mContext.getResources().getStringArray(R.array.least_than_oneMin);
			// 产生0 ~ .length-1的随机数
			int randNum = (int) (Math.random() * mLeastStrs.length);
			str = mLeastStrs[randNum];
		}
		return str;
	}
	
}
