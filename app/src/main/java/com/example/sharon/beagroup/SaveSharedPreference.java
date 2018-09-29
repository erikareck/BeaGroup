package com.example.sharon.beagroup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setID(Context ctx, String user_id)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString("USER", user_id).apply();
    }

    public static String getID(Context ctx)
    {
        return getSharedPreferences(ctx).getString("USER", "null");
    }

    public static void setLog(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean("logged", status).apply();
    }

    public static boolean getLog(Context ctx)
    {
        return getSharedPreferences(ctx).getBoolean("logged", false);
    }
}
