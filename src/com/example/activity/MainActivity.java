package com.example.activity;

import com.example.activity.ResultActivity;
import com.example.test2.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.text.TextUtils;

import java.util.Timer;
import java.util.TimerTask;

import com.example.util.TimeMgr;
import com.example.util.Config;

public class MainActivity extends Activity {
	
	private Button mStartBtn;
	private TextView mTimeText;
	private Timer mTimer = new Timer();
	private TimerTask mTask;
	private HomeBtnReceiver mHomeBtnReceiver;
	
	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					TimeMgr.setTime();
					mTimeText.setText(TimeMgr.getTime() + "s");
				}
			});
		}
	}
	
	// 监听按下home键
	// 接听处理后立即取消监听
	private class HomeBtnReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
				boolean isStop = false;
				String reason = intent.getStringExtra(Config.SYSTEM_REASON);
				
				if(TextUtils.equals(reason, Config.SYSTEM_HOME_KEY)) {
					isStop = true;
				}
				else if(TextUtils.equals(reason, Config.SYSTEM_HOME_KEY_LONG)) {
					isStop = true;
				}
				if(isStop) {
					jumpToResultActivity();
				}
			}
			
			unregisterReceiver(mHomeBtnReceiver);
		}
	}
	
	// 注册：监听相关广播
	private void initReceiver() {
		mHomeBtnReceiver = new HomeBtnReceiver();
	    IntentFilter filter = new IntentFilter();
	    filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
	    filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	    registerReceiver(mHomeBtnReceiver, filter);
	}
	
	private void initResorces() {
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		
		mStartBtn = (Button) findViewById(R.id.btnStart);
    	mTimeText = (TextView) findViewById(R.id.textTime);
    	
    	mStartBtn.setOnClickListener(new View.OnClickListener() {
    		@Override
			public void onClick(View v) {
    			if(mTask != null) {
    				return ;
    			}
    			TimeMgr.resetTime();
    			mTask = new MyTimerTask();
    			mTimer.schedule(mTask, 500, 1000);
    			TimeMgr.setState(TimeMgr.TimeState.TIME_ING);
    		}
    	});
    	
    	initReceiver();
	}
	
	private void shutDownTimer() {
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		if(null != mTimer && null != mTask) {
			mTask.cancel();
			mTimer.purge();
			mTask = null;
		}
	}
	
	private void jumpToResultActivity() {
		shutDownTimer();
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		startActivity(intent);
	}
	
	// ---------  下面为activity执行开始   ---------
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
    @Override
    protected void onResume() {
    	initResorces();
    	super.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }

    // 监听back返回键
 	@Override
     public void onBackPressed() {
 		if(TimeMgr.getState() == TimeMgr.TimeState.TIME_ING) {
 			jumpToResultActivity();
 			return;
 		}
 		super.onBackPressed();
 	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
