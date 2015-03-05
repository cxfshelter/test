package com.example.activity;

import com.example.activity.ResultActivity;
import com.example.test2.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.example.util.TimeMgr;

public class MainActivity extends ActionBarActivity {
	
	private Button mStartBtn;
	private TextView mTimeText;
	private Timer mTimer = new Timer();
	private TimerTask mTask;
	
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
	
	private void initResorces() {
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		
		mStartBtn = (Button) findViewById(R.id.btnStart);
    	mTimeText = (TextView) findViewById(R.id.textTime);
    	
    	mStartBtn.setOnClickListener(new View.OnClickListener() {
    		@Override
			public void onClick(View v) {
    			TimeMgr.resetTime();
    			mTask = new MyTimerTask();
    			mTimer.schedule(mTask, 500, 1000);
    			TimeMgr.setState(TimeMgr.TimeState.TIME_ING);
    		}
    	});
    }
	
	private void jumpToResultActivity() {
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		startActivity(intent);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initResorces();
    }
    
    // ¼àÌýback·µ»Ø¼ü
 	@Override
     public void onBackPressed() {
 		if(TimeMgr.getState() == TimeMgr.TimeState.TIME_ING) {
 			if(mTimer != null) {
 				mTask.cancel();
 				mTimer.purge();
 				mTask = null;
 			}
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
