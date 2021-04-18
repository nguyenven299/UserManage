package com.usermanage.view.insertData;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.model.UserModel;
import com.usermanage.viewModel.dataUser.InsertDataUser;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsertDataActivityViewModel extends ViewModel {
    public MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> uid = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> birthdayTemp = new MutableLiveData<>();
    public MutableLiveData<String> birthday = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> errorName = new MutableLiveData<>();
    public MutableLiveData<String> errorBirthday = new MutableLiveData<>();
    public MutableLiveData<String> errorPhoneNumber = new MutableLiveData<>();
    public MutableLiveData<Boolean> insertSuccess = new MutableLiveData<>(false);
    public MutableLiveData<String> successString = new MutableLiveData<>();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private UserModel mUserModel = new UserModel();
    private long mDateTime = new Date().getTime();
    private Long mDayCurrent = new Timestamp(mDateTime).getTime();
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener day = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Date day = new Date(myCalendar.getTimeInMillis());
            String dayTime = String.valueOf(day.getTime());
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            birthday.setValue(sdf.format(myCalendar.getTime()));
            birthdayTemp.setValue(dayTime);

        }
    };

    private MutableLiveData<UserModel> getUserModelMutableLiveData() {
        return userModelMutableLiveData;
    }

    public void showEmail(String Email, String Uid) {
        email.setValue(Email);
        uid.setValue(Uid);
        mUserModel.setEmail(Email);
    }

    public void selectBirthday(Context context) {
        new DatePickerDialog(context, day, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void onClicked() {
        String mName = name.getValue();
        String mBirthday = birthday.getValue();
        String mPhoneNumber = phoneNumber.getValue();
        String Empty = "Trường này không được trống";
        if (mName == null || mName.isEmpty() || mName.equals("defined")) {
            errorName.setValue(Empty);
        } else {
            mUserModel.setName(mName.trim());
        }
        if (mBirthday == null || mBirthday.isEmpty() || mBirthday.equals("defined")) {
            errorBirthday.setValue(Empty);
        } else if (mDayCurrent < Long.parseLong(birthdayTemp.getValue())) {
            errorBirthday.setValue("Ngày sinh không hợp lệ");
        } else {
            mUserModel.setBirthday(birthdayTemp.getValue());
        }
        if (mPhoneNumber == null || mPhoneNumber.isEmpty() || mPhoneNumber.equals("defined")) {
            errorPhoneNumber.setValue(Empty);
        } else {
            mUserModel.setPhoneNumber(mPhoneNumber.trim());
        }
        if (mUserModel.getName() != null && !mUserModel.getName().isEmpty()
                && mUserModel.getBirthday() != null && !mUserModel.getBirthday().isEmpty()
                && mUserModel.getPhoneNumber() != null && !mUserModel.getPhoneNumber().isEmpty()
        ) {
            mUserModel.setEmail(email.getValue());
            mUserModel.setUid(uid.getValue());
            InsertDataUser.getInstance().insertData(mFirestore, mUserModel, new InsertDataUser.IInsertDataUser() {
                @Override
                public void onSuccess(String success) {
                    insertSuccess.setValue(true);
                    successString.setValue(success);
                }

                @Override
                public void onFail(String fail) {
                    Log.d("errorInsertDataUser", "onFail: " + fail);
                }
            });
        }
    }
}
