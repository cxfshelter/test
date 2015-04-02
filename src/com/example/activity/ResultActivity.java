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
        if (bestScore < TimeMgr.getTime()) {
            RecordUtil.setBestScore(this, TimeMgr.getTime());
        }
        RecordUtil.setLastScore(this, TimeMgr.getTime());
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
		// TODO Auto-generated method stub
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
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();
		// ����ʱNotification��ͼ�������
//		oks.setNotification(R.drawable.ic_launcher,
//				this.getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle("����holdס��");
		// titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
//		oks.setTitleUrl("http://10.163.7.91:8080/scrawl");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		oks.setText("�����ڡ�Զ���ֻ�APP�����"+shareTime+"������holdס��");
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//		oks.setUrl("http://10.163.7.91:8080/scrawl/publish/10.jpg");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("�����ڡ�Զ���ֻ�APP�����"+shareTime+"������holdס��");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite(this.getString(R.string.app_name));
		// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
//		oks.setSiteUrl("http://10.163.7.91:8080/scrawl");
//		oks.setImagePath(imagePath);
		// ��������GUI
		oks.show(this);
	}
	
}
