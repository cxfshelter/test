package com.example.service;

import com.example.util.Config;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * 用于检测当前应用退出后（并且还处于正在倒计时状态），检测用户有无开启新应用
 * 有则关闭开启应用并跳转到倒计时界面
 */
public class MonitorService extends Service{
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		ActivityManager activityMgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
	    String curPackName = activityMgr.getRunningTasks(1).get(0).topActivity.getPackageName();
	    String pjtPackName = intent.getComponent().getPackageName();
	    if(!curPackName.equals(pjtPackName)) {
	    	// activityMgr.killBackgroundProcesses(curPackName);
	    	ComponentName comName = new ComponentName(pjtPackName, Config.LAUNCHER_ACT);
	    	Intent newIntent = new Intent();
	    	newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	newIntent.setComponent(comName);
	    	startActivity(newIntent);
	    }
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
	}
	
	public static void startMonitor(Context context) {
		Intent intent = new Intent(context, MonitorService.class);
		context.startService(intent);
	}
	
}
