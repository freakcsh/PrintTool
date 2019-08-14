package com.freak.printtool.hardware.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * @author Administrator
 * @date 2018/4/11
 */

public class SharedPreferencesUtils {

    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String USER_CODE = "USER_CODE";
    public static final String KEY = "KEY";
    public static final String USER_MOBIL = "USER_MOBIL";
    public static final String QCWD_MCHT = "QCWD_MCHT";
    public static final String IDENTIFY_STATUS = "IDENTIFY_STATUS";
    public static final String USER_ROLE = "USER_ROLE";

    public static final String JPush_Alias = "JPush_Alias";
    public static final String YiBao_Open_Status = "YiBao_Open_Status";

    public static final String Printer_Open_Status = "Printer_Open_Status";
    public static final String Printer_IP_Address = "Printer_IP_Address";
    public static final String Voice_Open_Status = "Voice_Open_Status";




    public static void save(Context context, String key, Long value) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static Long getLong(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    public static void save(Context context, String key, float value) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        return preferences.getFloat(key, 0);
    }

    public static void save(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static void save(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public static void save(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("yunkahui", Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

}