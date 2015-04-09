package com.example.activity;

import com.example.activity.ResultActivity;
import com.example.service.MonitorService;
import com.example.test2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.example.util.RecordUtil;
import com.example.util.TimeMgr;

public class MainActivity extends Activity {

    private TextView tvBestScore;
    private TextView tvLastScore;
	private Button mStartBtn;
	private TextView mTimeText;
	private boolean mIsStop;

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
		mIsStop = false;
		
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		
		mStartBtn = (Button) findViewById(R.id.btnStart);
    	mTimeText = (TextView) findViewById(R.id.textTime);
        tvLastScore = (TextView) findViewById(R.id.tvLastScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        
        mStartBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.start_btn));
        mStartBtn.setText(getResources().getString(R.string.main_startBtn));
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsStop) {
                	if (mTask != null) {
                        return;
                    }
                    TimeMgr.resetTime();
                    mTask = new MyTimerTask();
                    mTimer.schedule(mTask, 500, 1000);
                    TimeMgr.setState(TimeMgr.TimeState.TIME_ING);
                    
                    mStartBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.end_btn));
                    mStartBtn.setText(getResources().getString(R.string.main_endBtn));
                    
                    mIsStop = true;
                }
                else {
                	if(TimeMgr.getState() == TimeMgr.TimeState.TIME_ING) {
             			jumpToResultActivity();
             		}
                	
                	mIsStop = false;
                }
            }
        });
    	
	}

    private void displayScore() {
        tvLastScore.setText("Last: " + RecordUtil.getDisplayFormatScore(RecordUtil
                .getLastScore(this)));
        tvBestScore.setText("Best: " + RecordUtil.getDisplayFormatScore(RecordUtil
                .getBestScore(this)));
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
		MonitorService.stopMonitor(MainActivity.this);
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		startActivity(intent);
	}
	

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
    	super.onResume();
    	initResorces();
        displayScore();
    }
    
    @Override
    protected void onPause() {
    	if(TimeMgr.getState() == TimeMgr.TimeState.TIME_ING) {
    		MonitorService.startMonitor(this);
    	}
    	super.onPause();
    }


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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
