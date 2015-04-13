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
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import com.example.util.Config;
import com.example.util.RecordUtil;
import com.example.util.TextEftUtil;
import com.example.util.TimeMgr;
import com.example.util.TimeMgr.TimeState;

public class MainActivity extends Activity {

    private TextView tvBestScore;
    private TextView tvLastScore;
	private Button mStartBtn;
	private TextView mTimeText;
	// private boolean mIsStop = false;		// 是否处于倒计时状态
	private TextEftUtil txtEftUtil;

	private Timer mTimer = new Timer();
	private TimerTask mTask;
	
	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					TimeMgr.setTime();
					// mTimeText.setText(TimeMgr.getTime() + "s");
					mTimeText.setText(RecordUtil.getDisplayFormatScoreByS(TimeMgr.getTime()));
				}
			});
		}
	}
	
	private void initResorces() {
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		txtEftUtil = TextEftUtil.createTextEftUtil(
				MainActivity.this, 
				MainActivity.this.findViewById(R.id.mainAct), 
				Config.MAIN_EFCTXT_NUM);
		
		mStartBtn = (Button) findViewById(R.id.btnStart);
    	mTimeText = (TextView) findViewById(R.id.textTime);
        tvLastScore = (TextView) findViewById(R.id.tvLastScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);
        
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TimeMgr.getState() == TimeState.TIME_NONE) {
                	if (mTask != null) {
                        return;
                    }
                    TimeMgr.resetTime();
                    mTask = new MyTimerTask();
                    mTimer.schedule(mTask, 500, 1000);
                    TimeMgr.setState(TimeState.TIME_ING);
                    
                    displayBtnType();
                    displayTxtEft();
                }
                else {
                	if(TimeMgr.getState() == TimeMgr.TimeState.TIME_ING) {
             			jumpToResultActivity();
             		}
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
    
    private void displayBtnType() {
    	if(TimeMgr.getState() == TimeState.TIME_NONE) {
    		mStartBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.start_btn));
            mStartBtn.setText(getResources().getString(R.string.main_startBtn));
    	}
    	else if(TimeMgr.getState() == TimeState.TIME_ING) {
    		mStartBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.end_btn));
            mStartBtn.setText(getResources().getString(R.string.main_endBtn));
    	}
    }
    
    private void displayTxtEft() {
    	txtEftUtil.startTextEft();
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
		txtEftUtil.removeTxtFromLayout();
		
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		startActivity(intent);
	}
	
	// --------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        initResorces();
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
        displayScore();
        displayBtnType();
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
