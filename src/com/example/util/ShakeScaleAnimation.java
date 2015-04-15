package com.example.util;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * @Description:
 * @Author: WuRuiqiang
 * @CreateDate: 2015/4/14-0:00
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: [v1.0]
 */
public class ShakeScaleAnimation extends Animation {
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        setDuration(2000);
        setRepeatCount(1);
        setInterpolator(new CycleInterpolator(3));
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Matrix matrix = t.getMatrix();
        if (interpolatedTime <= 0.25f) {
            matrix.preTranslate(-interpolatedTime * 20, 0);
        } else if (interpolatedTime > 0.25f && interpolatedTime <= 0.5f){
            matrix.preTranslate(interpolatedTime * 40, 0);
            matrix.postTranslate(-30, 0);
        }
    }
}
