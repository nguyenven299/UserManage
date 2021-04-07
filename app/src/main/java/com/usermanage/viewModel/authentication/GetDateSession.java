package com.usermanage.viewModel.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GetDateSession {
    public static GetDateSession instance;

    public static GetDateSession getInstance() {
        if (instance == null)
            instance = new GetDateSession();
        return instance;
    }

    public Long get(Activity context) {
        SharedPreferences sharedPref = context.getSharedPreferences("Date", Context.MODE_PRIVATE);
        Long Date = new Long(1);
        Long date = sharedPref.getLong("Date", Date);
        return date;
    }
}
