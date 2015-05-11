package com.example.activity;

import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

import com.example.activity.ResultActivity;

import com.example.service.MonitorService;
import com.gdhysz.savehumen.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.example.util.BackUpUtils;
import com.example.util.Config;
import com.example.util.RecordUtil;
import com.example.util.TextEftUtil;
import com.example.util.TimeMgr;
import com.example.util.TimeMgr.TimeState;

public class MainActivity extends Activity
{

	private TextView tvBestScore;
	private TextView tvLastScore;
	private Button mStartBtn, mUpdateBtn;
	private TextView mTimeText;
	// private boolean mIsStop = false; // 是否处于倒计时状态
	private TextEftUtil txtEftUtil;
	private UpdateResponse ur;
	private boolean isNeedUpdate = false;
	private static final String BMOB_TAG="Bmob Log";
	private Timer mTimer = new Timer();
	private TimerTask mTask;

	private class MyTimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					TimeMgr.setTime();

					// 挑战成功
					if (TimeMgr.checkTime(BackUpUtils.getInstance(
							MainActivity.this).getSelectInt(
							TimeMgr.getSelState().ordinal())))
					{
						displaySuc(true);
					}

					// mTimeText.setText(TimeMgr.getTime() + "s");
					mTimeText.setText(RecordUtil
							.getDisplayFormatScoreByS(TimeMgr.getTime()));
				}
			});
		}
	}

	private void initResorces()
	{
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		txtEftUtil = TextEftUtil.createTextEftUtil(MainActivity.this,
				MainActivity.this.findViewById(R.id.mainAct),
				Config.MAIN_EFCTXT_NUM);

		mStartBtn = (Button) findViewById(R.id.btnStart);
		mUpdateBtn = (Button) findViewById(R.id.btnUpdate);
		mTimeText = (TextView) findViewById(R.id.textTime);
		tvLastScore = (TextView) findViewById(R.id.tvLastScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);

		mStartBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				if (TimeMgr.getState() == TimeState.TIME_NONE)
				{
					displaySelList();
				} else
				{
					if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
					{
						jumpToResultActivity();
					}
				}
			}
		});
		// 点击按钮进行手动更新操作
		mUpdateBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 手动更新
				BmobUpdateAgent.forceUpdate(MainActivity.this);
			}
		});
	}

	private void displayScore()
	{
		tvLastScore.setText("Last: "
				+ RecordUtil.getDisplayFormatScore(RecordUtil
						.getLastScore(this)));
		tvBestScore.setText("Best: "
				+ RecordUtil.getDisplayFormatScore(RecordUtil
						.getBestScore(this)));
	}

	private void displayBtnType()
	{
		if (TimeMgr.getState() == TimeState.TIME_NONE)
		{
			mStartBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.start_btn));
			mStartBtn.setText(getResources().getString(R.string.main_startBtn));
		} else if (TimeMgr.getState() == TimeState.TIME_ING)
		{
			mStartBtn.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.end_btn));
			mStartBtn.setText(getResources().getString(R.string.main_endBtn));
		}
	}

	private void displayTxtEft()
	{
		txtEftUtil.startTextEft();
	}

	/*
	 * 开始倒计时后，弹出选择框，并在点选任一选项后开始倒计时
	 */
	private void displaySelList()
	{
		new AlertDialog.Builder(MainActivity.this)
				.setTitle(getResources().getString(R.string.select_list_title))
				.setItems(
						getResources()
								.getStringArray(R.array.select_list_array),
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								mUpdateBtn.setEnabled(false);
								Toast.makeText(MainActivity.this,getResources().getString(R.string.select_list_toast)+ getResources().getStringArray(R.array.select_list_array)[which],
										Toast.LENGTH_LONG).show();

								TimeMgr.setSelState(which);

								startTimer();
							}
						}).show();
	}

	/*
	 * 挑战成功后展示toast 后期将改变背景颜色（成功界面）
	 */
	private void displaySuc(boolean isSet)
	{
		if (TimeMgr.getIsShowSuc())
		{
			return;
		}

		if (isSet)
		{
			Toast.makeText(MainActivity.this, "挑战成功", Toast.LENGTH_LONG).show();
			MainActivity.this.findViewById(R.id.main_imgBg).setBackgroundColor(
					getResources().getColor(Config.SUC_COLOR_ID));
			TimeMgr.setIsShowSuc(true);
		} else
		{
			// MainActivity.this.findViewById(R.id.main_imgBg).setBackgroundDrawable(getResources().getDrawable(R.drawable.list_background));
			MainActivity.this.findViewById(R.id.main_imgBg).setBackgroundColor(
					getResources().getColor(Config.FAL_COLOR_ID));
		}
	}

	/*
	 * 开始倒计时
	 */
	private void startTimer()
	{
		if (mTask != null)
		{
			return;
		}
		TimeMgr.resetTime();
		mTask = new MyTimerTask();
		mTimer.schedule(mTask, 500, 1000);
		TimeMgr.setState(TimeState.TIME_ING);

		displayBtnType();
		displayTxtEft();
	}

	/*
	 * 结束倒计时
	 */
	private void shutDownTimer()
	{
		TimeMgr.setState(TimeMgr.TimeState.TIME_NONE);
		if (null != mTimer && null != mTask)
		{
			mTask.cancel();
			mTimer.purge();
			mTask = null;
		}
	}

	/*
	 * 结束时跳转界面
	 */
	private void jumpToResultActivity()
	{
		shutDownTimer();
		MonitorService.stopMonitor(MainActivity.this);
		txtEftUtil.removeTxtFromLayout();

		Intent intent = new Intent();
		intent.setClass(MainActivity.this, ResultActivity.class);
		if (intent.resolveActivity(getPackageManager()) != null)
		{
			startActivity(intent);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		BmobUpdateAgent.setUpdateCheckConfig(true);
		initResorces();

		// 设置在非wifi环境下不更新app
		BmobUpdateAgent.setUpdateOnlyWifi(false);
		BmobUpdateAgent.update(MainActivity.this);
		BmobUpdateAgent.setUpdateListener(new BmobUpdateListener()
		{
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo)
			{
				Log.i(BMOB_TAG, updateStatus + "");
				if (updateStatus == UpdateStatus.Yes)
				{
					isNeedUpdate = true;
					ur = updateInfo;
					File file = new File(Environment
							.getExternalStorageDirectory(), "yuanlishouji" + ".apk");
					Log.i(BMOB_TAG, file.getAbsolutePath());
				} else if (updateStatus == UpdateStatus.No)
				{
					Toast.makeText(MainActivity.this, "版本无更新",
							Toast.LENGTH_SHORT).show();
				} else if (updateStatus == UpdateStatus.IGNORED)
				{
					Toast.makeText(MainActivity.this, "该版本已被忽略更新",
							Toast.LENGTH_SHORT).show();
					mUpdateBtn.setVisibility(View.VISIBLE);
	            }else if (updateStatus == UpdateStatus.TimeOut)
				{
					Toast.makeText(MainActivity.this, "查询出错或查询超时",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		
		
		//设置对对话框按钮的点击事件的监听
		BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {
			
			@Override
			public void onClick(int status) {
				// TODO Auto-generated method stub
				switch (status) {
		        case UpdateStatus.Update:
		        	
		            break;
		        case UpdateStatus.NotNow:
		        	
		        	mUpdateBtn.setVisibility(View.VISIBLE);
		        	break;
		        case UpdateStatus.Close:
		        	//只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，直接退出应用
		            MainActivity.this.finish();
		            break;
		        }
			}
		});

	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		displayScore();
		displayBtnType();
		displaySuc(TimeMgr.checkTime(BackUpUtils.getInstance(MainActivity.this)
				.getSelectInt(TimeMgr.getSelState().ordinal())));
		
		mUpdateBtn.setEnabled(true);
		if(isNeedUpdate)
		{
			mUpdateBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onPause()
	{
		if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
		{
			MonitorService.startMonitor(this);
		}
		super.onPause();
	}

	@Override
	public void onBackPressed()
	{
		if (TimeMgr.getState() == TimeMgr.TimeState.TIME_ING)
		{
			jumpToResultActivity();
			return;
		}
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
