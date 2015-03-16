package com.example.activity;

import com.example.test2.R;

import com.example.util.TimeMgr;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	private TextView mTextTime;
	
	private void initResources() {
		mTextTime = (TextView) findViewById(R.id.textFinalTime);
		mTextTime.setText(TimeMgr.getTime() + "s");
		
		TimeMgr.resetTime();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		initResources();
	}
	
}
