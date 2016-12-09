package com.fanavard.alisherafat.khatereha.app.preferences;

import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * User preferences for application
 */
public class UserPrefs {
    private Context context;
    private PreferenceHelper preferenceHelper;

    private UserPrefs(Context context) {
        this.context = context;
        preferenceHelper = PreferenceHelper.getInstance(context);
    }

    private static WeakReference<UserPrefs> instance;

    public static UserPrefs getInstance(Context context) {
        if (instance == null || instance.get() == null) {
            instance = new WeakReference<>(new UserPrefs(context));
        }
        return instance.get();
    }

}
