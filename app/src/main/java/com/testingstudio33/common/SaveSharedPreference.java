package com.testingstudio33.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.testingstudio33.common.Constant.PreferencesUtility.LOGGED_IN_PREF;

public class SaveSharedPreference {

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private Context context;

    public SaveSharedPreference(Context context) {
        this.context = context;
    }

    /**
     * Set the Login Status
     *
     * @param context
     * @param loggedIn
     */
    public static void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    /**
     * Get the Login Status
     *
     * @param context
     * @return boolean: login status
     */
    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static void logoff(Context context) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    public  void saveuserinfo(String info) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("info", info);
        editor.apply();
    }

    public String getuserinfo(Context context) {
        return getPreferences(context).getString("info", null);
    }

    public void saveLoginDetails(String name, String email, String phone) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString("Name", name);
        editor.putString("Email", email);
        editor.putString("Phone", phone);
        editor.apply();
    }

    public static String getname(Context context) {
        return getPreferences(context).getString("Name", null);
    }

    public String getemail(Context context) {
        return getPreferences(context).getString("Email", null);
    }

    public String getphone(Context context) {
        return getPreferences(context).getString("Phone", null);
    }
}
