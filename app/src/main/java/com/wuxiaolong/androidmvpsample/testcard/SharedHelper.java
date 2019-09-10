package com.wuxiaolong.androidmvpsample.testcard;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHelper {
    public static void putValue(Context context, String key, int value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("TvSetting", Context.MODE_PRIVATE)
                .edit();
        sp.putInt(key, value);
        sp.apply();
    }
    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences("TvSetting",
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void putStringValue(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences("TvSetting", Context.MODE_PRIVATE)
                .edit();
        sp.putString(key, value);
        sp.apply();
    }
    public static String getStringValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences("TvSetting",
                Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
}
