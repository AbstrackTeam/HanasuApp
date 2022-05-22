package com.abstrack.hanasu.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String PREFERENCES_ID = "hanasu";

    public static String getIdentifier(Context ct) {
        SharedPreferences sharedPref = ct.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        return sharedPref.getString("identifier", "");
    }

    public static void setIdentifier(String value, Context ct){
        SharedPreferences sharedPreferences = ct.getSharedPreferences(PREFERENCES_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("identifier", value);
        editor.apply();
    }
}
