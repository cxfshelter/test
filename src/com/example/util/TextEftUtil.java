package com.example.util;

import java.util.ArrayList;

import com.example.test2.R;
import com.example.util.TimeMgr.TimeState;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextEftUtil {
	
	private static Context mContext;
	private static View mView;
	private static TextEftUtil mInstance;
	
	private ArrayList<TextView> mViews;
	// pos：x，y取范围值即可（暂取均小于360dp）
	// anim：在res/anim里设置即可
	private int[] mAnimIDs;
	private Animation mAnimation;
	
	// 每个 view 动画的监听事件
	AnimationListener mAsListener = new AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			if(TimeMgr.getState() == TimeState.TIME_NONE) {
				return;
			}
			
			for(TextView txtView : mViews) {
				Animation anim = txtView.getAnimation();
				if(anim != null) {
					if(anim.hasEnded()) {
						mInstance.addTxtViewToLayout(mView, txtView);
					}
				}
				else {
					mInstance.addTxtViewToLayout(mView, txtView);
				}
			}
		}
	};
	
	public TextEftUtil() {
		mViews = new ArrayList<TextView>();
		mAnimIDs = new int[] {
				R.anim.alpha_scale,
				R.anim.alpha_scale2,
				R.anim.alpha_scale3,
				R.anim.alpha_scale4,
				R.anim.alpha_scale5
		};
	}
	
	public static synchronized void setInstance(Context context, View view) {
		if(mInstance == null) {
			mInstance = new TextEftUtil();
		}
		mContext = context;
		mView = view;
	}
	
	public static TextEftUtil createTextEftUtil(Context context, View view, int num) {
		TextEftUtil.setInstance(context, view);
		TextView txtView; 
		for(int i=0; i<num; i++) {
			txtView = new TextView(context);
			mInstance.mViews.add(txtView);
		}
		return mInstance;
	}
	
	public void startTextEft() {
		int i = 0;
		for(TextView txtView : mInstance.mViews) {
			txtView.setText("就试试显示位置" + i++);
			mInstance.addTxtViewToLayout(mView, txtView);
		}
	}
	
	public int[] getPos() {
		if(mView == null && mView instanceof View) {
			return new int[] {};
		}
		
		int[] pos;
		int screenWidth = mView.getWidth();
		int screenHeight = mView.getHeight();
		int x = screenWidth/5 + (int) (Math.random() * screenWidth/3);
		int y = screenHeight/5 + (int) (Math.random() * screenHeight/3);
		pos = new int[] {x, y};
		return pos;
	}
	
	/*
	 * 	在view里添加text
	 *  设置控件所在的位置posX, posY，并且不改变宽高
	 *  绝对位置
	 */
	public void addTxtViewToLayout(View view, TextView txtView) {
		int[] pos = getPos();
		int posX = pos[0];
		int posY = pos[1];
		
		RelativeLayout relView = (RelativeLayout) view;
		MarginLayoutParams margin = new MarginLayoutParams(relView.getLayoutParams());
		margin.setMargins(posX, posY, posX+margin.width, posY+margin.height);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		if(txtView.getParent() != null) {
			txtView.setLayoutParams(layoutParams);
			txtView.setVisibility(View.VISIBLE);
		}
		else {
			relView.addView(txtView, layoutParams);
		}
		
		mInstance.loadAnimation(txtView);
	}
	
	public void removeTxtFromLayout() {
		for(TextView txtView : mInstance.mViews) {
			txtView.clearAnimation();
			txtView.setVisibility(View.GONE);
		}
	}
	
	private void loadAnimation(TextView txtView) {
		int range = (int) (Math.random() * mAnimIDs.length);
		int animID = mAnimIDs[range];
		mAnimation = AnimationUtils.loadAnimation(mContext, animID);
		mAnimation.setAnimationListener(mAsListener);
		txtView.startAnimation(mAnimation);
	}
	
}
