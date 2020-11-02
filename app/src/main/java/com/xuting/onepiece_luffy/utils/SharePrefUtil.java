package com.xuting.onepiece_luffy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xuting.onepiece_luffy.MyApplication;

public class SharePrefUtil {

    public static final String FLUTTER_VERSION_SP_NAME = "FLUTTER_VERSION";
    public static final String FLUTTER_VERSION_SP_NAME_KEY = "FLUTTER_VERSION_KEY";

    public static void setFlutterVersion(String value) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(
                FLUTTER_VERSION_SP_NAME, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FLUTTER_VERSION_SP_NAME_KEY, value);
        editor.apply();
    }

    public static String getFlutterVersion() {
        return MyApplication.getContext().getSharedPreferences(
                FLUTTER_VERSION_SP_NAME, Context.MODE_PRIVATE
        ).getString(FLUTTER_VERSION_SP_NAME_KEY, "0.0.0");
    }
}
