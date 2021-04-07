package com.usermanage.viewModel.dataUser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GetUid {
    public static GetUid instance;

    public static GetUid getInstance() {
        if (instance == null)
            instance = new GetUid();
        return instance;
    }

    public String get(Activity mContext) {
        SharedPreferences sharedPref = mContext.getSharedPreferences("UID", Context.MODE_PRIVATE);
        String uid = sharedPref.getString("UID", null);
        return uid;
    }
}