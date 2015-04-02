package com.example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.test2.R;
import com.example.util.RecordUtil;
import com.example.util.TimeMgr;

public class ResultActivity extends Activity implements OnClickListener {
	
	private TextView mTextTime;
	private Button mShareBtn;
	
	private void initResources() {
		mTextTime = (TextView) findViewById(R.id.textFinalTime);
		mShareBtn = (Button)findViewById(R.id.shareBtn);

        int bestScore = RecordUtil.getBestScore(this);
        int lastScore = RecordUtil.getLastScore(this);
        if (bestScore < TimeMgr.getTime()) {
            RecordUtil.setBestScore(this, TimeMgr.getTime());
        }
        if (lastScore != TimeMgr.getTime()) {
        	RecordUtil.setLastScore(this, TimeMgr.getTime());
        }
		mTextTime.setText(TimeMgr.getTime() + "s");
		mShareBtn.setOnClickListener(this);
		TimeMgr.resetTime();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		initResources();
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.shareBtn:
			String shareTime = mTextTime.getText().toString();
			showShare(shareTime);
			break;

		default:
			break;
		}
	}
	
	private void showShare(String shareTime)
	{
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.disableSSOWhenAuthorize();
		oks.setTitle("����holdס��");
		oks.setText("�����ڡ�Զ���ֻ�APP�����"+shareTime+"������holdס��");
		oks.setComment("�����ڡ�Զ���ֻ�APP�����"+shareTime+"������holdס��");
		oks.setSite(this.getString(R.string.app_name));
		oks.show(this);
	}
	
}
