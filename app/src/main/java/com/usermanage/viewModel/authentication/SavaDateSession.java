package com.usermanage.viewModel.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SavaDateSession {
    public static SavaDateSession instance;

    public static SavaDateSession getInstance() {
        if (instance == null)
            instance = new SavaDateSession();
        return instance;
    }

    public void saved(Activity mAontext, Long date) {
        SharedPreferences sharedPref = mAontext.getSharedPreferences("Date", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("Date", date);
        editor.apply();
    }
}
