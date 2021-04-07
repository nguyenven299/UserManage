package com.usermanage.view.updateDataUser;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usermanage.dao.user.User;
import com.usermanage.dao.user.UserDatabase;
import com.usermanage.model.UserModel;
import com.usermanage.viewModel.dataUser.UpdateDataUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateDataUserActivityViewModel extends ViewModel {
    public MutableLiveData<Long> birthdayTemp = new MutableLiveData<>();
    public MutableLiveData<String> errorName = new MutableLiveData<>();
    public MutableLiveData<String> errorPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> errorBirthday = new MutableLiveData<>();
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> uid = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> birthday = new MutableLiveData<>();
    public MutableLiveData<Boolean> insertSuccess = new MutableLiveData<>(false);
    final Calendar myCalendar = Calendar.getInstance();
    private UserModel mUserModel = new UserModel();
    private long mDateTime = new Date().getTime();
    private Long mCurrentDay = new Timestamp(mDateTime).getTime();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    DatePickerDialog.OnDateSetListener day = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date day = new Date(myCalendar.getTimeInMillis());
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            birthday.setValue(sdf.format(myCalendar.getTime()));
            birthdayTemp.setValue(day.getTime());
        }
    };

    public void setUser(String userID) {
        User user = new User();
        UserDatabase userDatabase = UserDatabase.getInsance(activityMutableLiveData.getValue());
        user = userDatabase.daoQuery().getUser();
        uid.setValue(userID);
        email.setValue(user.getEmail());
        name.setValue(user.getName());
        phoneNumber.setValue(user.getPhoneNumber());
        if (user.getBirthday() != null && !user.getBirthday().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            birthdayTemp.setValue(Long.valueOf(user.getBirthday()));
            cal.setTimeInMillis(birthdayTemp.getValue());
            birthday.setValue(df.format(cal.getTime()));
        }
        Log.d("idididid", "setUser: " + mCurrentDay);
    }

    public void selectBirthday(Context context) {
        new DatePickerDialog(context, day, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void onClick() {
        String mEmail = email.getValue();
        String mName = name.getValue();
        String mPhoneNumber = phoneNumber.getValue();
        Long mBirthday = birthdayTemp.getValue();
        String Empty = "Trường này không được trống";
        Log.d("tttttt", "OnClick: " + uid.getValue());
        mUserModel.setEmail(mEmail);
        mUserModel.setUid(uid.getValue());
        if (mName == null || mName.isEmpty() || mName.equals("defined")) {
            errorName.setValue(Empty);
        } else {
            mUserModel.setName(mName);
        }
        if (mBirthday == null || mBirthday.equals("defined")) {
            errorBirthday.setValue(Empty);
            Log.d("ttrweerw", "OnClick: null");
        } else if (mCurrentDay < mBirthday) {
            Log.d("utututut", "OnClick: " + mCurrentDay + " / " + mBirthday);
            errorBirthday.setValue("Ngày sinh không hợp lệ");
        } else {
            mUserModel.setBirthday(mBirthday.toString());
        }
        if (mPhoneNumber == null || mPhoneNumber.isEmpty() || mPhoneNumber.equals("defined")) {
            errorPhoneNumber.setValue(Empty);
        } else {
            mUserModel.setPhoneNumber(mPhoneNumber);
        }
        if (mUserModel.getName() != null && !mUserModel.getName().isEmpty()
                && mUserModel.getBirthday() != null && !mUserModel.getBirthday().isEmpty()
                && mUserModel.getPhoneNumber() != null && !mUserModel.getPhoneNumber().isEmpty()
        ) {
            UpdateDataUser.getInstance().update(mFirestore, mUserModel, new UpdateDataUser.IInsertDataUser() {
                @Override
                public void onSuccess(String success) {
                    Toast.makeText(contextMutableLiveData.getValue(), success, Toast.LENGTH_SHORT).show();
                    insertSuccess.setValue(true);
                }

                @Override
                public void onFail(String fail) {
                    Log.d("TAG", "onFail: " + fail);
                }
            });
        }
    }
}