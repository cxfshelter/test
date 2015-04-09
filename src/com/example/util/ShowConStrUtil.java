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
	 * �������ļ���״̬
	 * ����ʷ��óɼ���ȣ�С�ڡ����ڣ�ǰ�����С��5%��������
	 * ���������С��1����
	 */
	public enum TeaseState {
		LEAST_ONE_MIN,		// С��1����
		LESS_THAN_BEST,		// ����1����С����óɼ�
		EQUAL_THAN_BEST,	// ������óɼ���ǰ�����С��5%��
		LARGER_THAN_BEST,	// ������óɼ���������5%��
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
	 * 	�ɼ������������������ʷ��óɼ��Աȣ���С�ڡ����ڡ����ڣ�
	 *  �ݲ������ճɼ�����ɸѡ
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
			// ����0 ~ .length-1�������
			int randNum = (int) (Math.random() * mLeastStrs.length);
			str = mLeastStrs[randNum];
		}
		return str;
	}
	
}
