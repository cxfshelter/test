package com.example.util;

import java.util.ArrayList;

import com.example.test2.R;
import com.example.util.TimeMgr.TimeState;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextEftUtil {
	
	private static Context mContext;
	private static View mView;
	private static TextEftUtil mInstance;
	// anim：在res/anim里设置即可
	private static final int[] mAnimIDs;
	private static final int[] mTxtColors;
	private ArrayList<TextView> mViews;

	
	static {
		mAnimIDs = new int[] {
			R.anim.alpha_scale,
			R.anim.alpha_scale2,
			R.anim.alpha_scale3,
			R.anim.alpha_scale4,
			R.anim.alpha_scale5,
            R.anim.translate
		};
		mTxtColors = new int[] {
			Color.BLACK,
			Color.GREEN,
			Color.RED,
			Color.WHITE,
			Color.YELLOW,
            Color.YELLOW
		};
	}
	
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
		for(TextView txtView : mInstance.mViews) {
			mInstance.addTxtViewToLayout(mView, txtView);
		}
	}
	
	public int[] getPos() {
		if(mView == null || !(mView instanceof View)) {
			return new int[] {};
		}
		
		int viewWidth = mView.getWidth();
		int viewHeight = mView.getHeight();
		int x = 0 + (int) (Math.random() * viewWidth/3);
		int y = viewHeight/5 + (int) (Math.random() * viewHeight/3);
		
		return new int[] {x, y};
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
		margin.setMargins(posX, posY, 0, 0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		if(txtView.getParent() != null) {
			txtView.setLayoutParams(layoutParams);
			txtView.setVisibility(View.VISIBLE);
		}
		else {
			relView.addView(txtView, layoutParams);
		}
		
		mInstance.loadAnimation(txtView);
		txtView.setText(StrUtil.getCorStr(mContext, R.array.remind_str));
		txtView.setTextSize(25);
	}
	
	public void removeTxtFromLayout() {
		for(TextView txtView : mInstance.mViews) {
			txtView.clearAnimation();
			txtView.setVisibility(View.GONE);
		}
	}
	
	private void loadAnimation(TextView txtView) {
		int range = (int) (Math.random() * mTxtColors.length);
//		int animID = mAnimIDs[range];
		Animation animation = AnimationUtils.loadAnimation(mContext, mAnimIDs[0]);
        ShakeScaleAnimation shakeScaleAnimation = new ShakeScaleAnimation();
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(shakeScaleAnimation);
        animationSet.setAnimationListener(mAsListener);
//        txtView.clearAnimation();
        txtView.setAnimation(animationSet);
		txtView.setTextColor(mTxtColors[range]);
        animationSet.startNow();
	}
	
}
