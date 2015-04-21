package com.example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.gdhysz.savehumen.R;
import com.example.util.RecordUtil;
import com.example.util.ScreenShotUtils;
import com.example.util.ShowConStrUtil;
import com.example.util.TimeMgr;

public class ResultActivity extends Activity implements OnClickListener {
	
	private ShowConStrUtil mTeaseUtil;
	private TextView mTextTime;
	private TextView mConStr;
	private Button mShareBtn;
	private static final String TAG="ResultActivity";
	
	private void initResources() {
		mTeaseUtil = ShowConStrUtil.createEmptyWorkingUtil(this, TimeMgr.getTime());
		
		mTextTime = (TextView) findViewById(R.id.textFinalTime);
		mConStr = (TextView) findViewById(R.id.textConStr);
		mShareBtn = (Button)findViewById(R.id.shareBtn);

        int bestScore = RecordUtil.getBestScore(this);
        int lastScore = RecordUtil.getLastScore(this);
        if (bestScore < TimeMgr.getTime()) {
            RecordUtil.setBestScore(this, TimeMgr.getTime());
        }
        if (lastScore != TimeMgr.getTime()) {
        	RecordUtil.setLastScore(this, TimeMgr.getTime());
        }
		
        // mTextTime.setText(TimeMgr.getTime() + "s");
        mTextTime.setText(RecordUtil.getDisplayFormatScore(TimeMgr.getTime()));
		mConStr.setText(mTeaseUtil.getTeaseStr());
		mShareBtn.setOnClickListener(this);
		TimeMgr.resetTime();
	}
	
	private void resetConfig() {
		TimeMgr.setIsSuccess(false);
		
	}
	
	private void displayEnd() {
		View bgView = ResultActivity.this.findViewById(R.id.result_imgBg);
		if(TimeMgr.getIsSuccess()) {
			bgView.setBackgroundColor(getResources().getColor(R.color.lemonchiffon));
		}
		else {
			bgView.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_background));
		}
	}
	
	
	///////////////////////////////////////
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		initResources();
		displayEnd();
	}
	
	@Override
	protected void onPause() {
		resetConfig();
		super.onPause();
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
		case R.id.shareBtn:
			String shareTime = mTextTime.getText().toString();

			showShare(shareTime,view);
			break;

		default:
			break;
		}
	}
	

	private void showShare(String shareTime,final View view)
	{
		//截取屏幕并存在sd卡
		ScreenShotUtils.getScreenShotBitmap(this, view);
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("你能hold住吗？");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("https://github.com/cxfshelter/test");
		// text是分享文本，所有平台都需要这个字段
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl("http://10.163.7.91:8080/scrawl/publish/10.jpg");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("哥能在【远离手机APP】坚持"+shareTime+"，你能hold住吗？");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(this.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://10.163.7.91:8080/scrawl");
		oks.setText("哥能在【远离手机APP】坚持"+shareTime+"，你能hold住吗？");
		oks.setImagePath(ScreenShotUtils.getSDCardPath()+"/Demo/ScreenImages"+"/screenshot.png");
//		oks.setImageUrl("http://img2.cache.netease.com/tech/2015/4/7/2015040709103419de7.jpg");
		Log.i(TAG, ScreenShotUtils.getSDCardPath()+"/Demo/ScreenImages"+"/screenshot.png");
		
		// 启动分享GUI
		oks.show(this);
	}
	
}
