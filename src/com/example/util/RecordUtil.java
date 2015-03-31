package com.example.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Description:
 * @Author: WuRuiqiang
 * @CreateDate: 2015/3/23-21:25
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @Version: [v1.0]
 */
public class RecordUtil {
    /**
     * 记录成绩信息的SharePreferences对应KEY值
     */
    final static String SCORE_SP_KEY = "score_key";
    /**
     * 上一次成绩
     */
    final static String LAST_SCORE_KEY = "last_score_key";
    /**
     * 最好成绩
     */
    final static String BEST_SCORE_KEY = "best_score_key";


    /**
     * 返回上一次成绩
     * @param context 上下文
     * @return 上一次成绩
     */
    public static long getLastScore(Context context) {
        return getScore(LAST_SCORE_KEY, context);
    }

    /**
     * 返回最好成绩
     * @param context 上下文
     * @return 最好成绩
     */
    public static long getBestScore(Context context) {
        return getScore(BEST_SCORE_KEY, context);
    }

    /**
     * 根据不同的KEY获取相应成绩
     * @param key 不同的成绩对应的KEY值
     * @param context 上下文
     * @return 成绩
     */
    public static long getScore(String key, Context context) {
        SharedPreferences sp = context.getSharedPreferences(SCORE_SP_KEY, Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }

    /**
     * 将不同的成绩存入SharePreferences中.
     * @param key 不同成绩对应的KEY
     * @param context 上下文
     * @param score 要存入的成绩
     */
    public static void setScore(String key, Context context, long score) {
        SharedPreferences sp = context.getSharedPreferences(SCORE_SP_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, score);
        editor.apply();
    }
}
