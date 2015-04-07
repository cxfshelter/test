package com.example.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class ScreenShotUtils
{

	public static Bitmap getScreenShotBitmap(Activity activity,View v) {
		// 需要截图的view
		v.setVisibility(View.INVISIBLE);
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		// 获取状态栏的高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕的宽和高
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 生成图片
		Bitmap bp = null;
//		if (statusBarHeight + height <= bmp.getHeight()) {
			bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
					- statusBarHeight);
			String savePath = getSDCardPath()+"/Demo/ScreenImages";
			File path = new File(savePath);
			String filepath = savePath+"/screenshot.png";
			File file = new File(filepath);
		try
		{
			if (!path.exists())
			{
				path.mkdirs();
			}
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileOutputStream fos = null;
			fos = new FileOutputStream(file);
			if (null != fos)
			{
				bp.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			view.destroyDrawingCache();
//		}
		return bp;

	}

	/**
	 * 获取SDCard的目录路径功能
	 * 
	 * @return
	 */
	public static String getSDCardPath()
	{
		File sdcardDir = null;
		// 判断SDCard是否存在
		boolean sdcardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdcardExist)
		{
			sdcardDir = Environment.getExternalStorageDirectory();
		}
		return sdcardDir.toString();
	}
}
