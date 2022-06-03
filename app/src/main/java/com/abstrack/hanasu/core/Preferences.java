package com.abstrack.hanasu.core;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String PREFERENCES_ID = "Hanasu";

    public static String getValue(String key, Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static void setStringValue(String key, String value, Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
