package com.usermanage.viewModel.dataUser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SaveUid {
    public static SaveUid instance;

    public static SaveUid getInstance() {
        if (instance == null)
            instance = new SaveUid();
        return instance;
    }

    public void saved(Activity mContext, String uid) {
        SharedPreferences sharedPref = mContext.getSharedPreferences("UID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UID", uid);
        editor.commit();
        editor.apply();
    }
}
