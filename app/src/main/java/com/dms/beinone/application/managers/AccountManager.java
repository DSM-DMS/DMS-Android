package com.dms.beinone.application.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;

import com.dms.beinone.application.models.Token;

/**
 * Created by BeINone on 2017-08-02.
 */

public class AccountManager {

    public static String TOKEN;

    private static String NAME="Perfs";
    private static String LOGIN_KEY="Logined";
    private static String TOKEN_KEY="Token";

    public static void setLogined(Context context, boolean mLogined) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(LOGIN_KEY,mLogined).apply();
    }

    public static void setToken(Context context,String mToken){
        SharedPreferences sharedPreferences=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(TOKEN_KEY,mToken).apply();
    }

    public static boolean isLogined(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(LOGIN_KEY,false);
    }

    public static String isToken(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_KEY,"JWT ");
    }
}
