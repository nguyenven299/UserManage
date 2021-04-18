package com.usermanage.view.main;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.model.AccountModel;
import com.usermanage.model.UserModel;
import com.usermanage.viewModel.authentication.SavaDateSession;
import com.usermanage.viewModel.dataUser.GetDataUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivityViewModel extends ViewModel {
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private String mCurrentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    private Calendar mCalendar = Calendar.getInstance();
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private MutableLiveData<Boolean> dbUser = new MutableLiveData<>(false);

    public void saveSession() {
        try {
            mCalendar.setTime(mDateFormat.parse(mCurrentDate));
        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        SavaDateSession.getInstance().saved(activityMutableLiveData.getValue(), mCalendar.getTime().getTime());
    }

    public void checkDBUser(String uid) {
        GetDataUser.getInstance().getData(firebaseFirestore, uid, new GetDataUser.IFindDataUser() {
            @Override
            public void onSuccess(UserModel userModel) {

            }

            @Override
            public void onExist() {
                dbUser.setValue(true);
            }

            @Override
            public void onEmpty() {

            }
            @Override
            public void onSuccessAccount(AccountModel accountModel) {

            }
        });
    }
}
