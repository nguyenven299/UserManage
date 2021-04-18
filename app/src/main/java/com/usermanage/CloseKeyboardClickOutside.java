package com.usermanage;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class CloseKeyboardClickOutside implements View.OnClickListener {
    Activity mAct;

    public CloseKeyboardClickOutside(Activity act) {
        this.mAct = act;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ViewGroup) {
            hideSoftKeyboard(this.mAct);
        }

    }
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager)
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
