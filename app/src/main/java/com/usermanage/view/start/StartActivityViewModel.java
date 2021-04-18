package com.usermanage.view.start;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usermanage.dao.user.UserDatabase;
import com.usermanage.viewModel.authentication.GetDateSession;
import com.usermanage.viewModel.authentication.LogOutAccount;
import com.usermanage.viewModel.dataUser.GetUid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StartActivityViewModel extends ViewModel {
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    private String mCurrentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    private Calendar mCalendar = Calendar.getInstance();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public MutableLiveData<Boolean> successLogin = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> successSession = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> sessionResult = new MutableLiveData<>();

    public void checkUserCurrent() {
        String Uid = GetUid.getInstance().get(activityMutableLiveData.getValue());
        if (Uid == null || Uid.isEmpty()) {
            successLogin.setValue(true);
        } else {
            checkSession();
        }
    }

    public void checkSession() {
        Long date = GetDateSession.getInstance().get(activityMutableLiveData.getValue());
        try {
            mCalendar.setTime(mDateFormat.parse(mCurrentDate));
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        if (mCalendar.getTime().getTime() - date > 604800000) {
            sessionResult.setValue(true);
            LogOutAccount.getInstance().checkAccountForLogout(activityMutableLiveData.getValue());
            successLogin.setValue(true);
            UserDatabase userDatabase = UserDatabase.getInsance(activityMutableLiveData.getValue());
            userDatabase.daoQuery().deleteUserById(1);

        } else {
            successSession.setValue(true);
        }
    }
}
