package com.usermanage.view.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usermanage.model.AccountModel;
import com.usermanage.viewModel.dataUser.SaveUid;
import com.usermanage.view.insertData.InsertDataActivity;
import com.usermanage.viewModel.authentication.CreateUserEmail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivityViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> repassword = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorRepassword = new MutableLiveData<>();
    public MutableLiveData<Boolean> m_ShowPassword = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> m_ShowRePassword = new MutableLiveData<>(false);
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    private AccountModel mAccountModel = new AccountModel();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>(false);

    public void onClick() {
        String Empty = "Trường này không được trống";
        String mEmail = email.getValue();
        String mPassword = password.getValue();
        String mRePassword = repassword.getValue();
        if (mEmail == null || mEmail.isEmpty() || mEmail.equals("defined")) {
            errorEmail.setValue(Empty);
        } else {
            mAccountModel.setAccount(mEmail);
        }
        if (mPassword == null || mPassword.isEmpty() || mPassword.equals("defined")) {
            errorPassword.setValue(Empty);
        } else {
            mAccountModel.setPassword(mPassword);
        }
        if (mRePassword == null || mRePassword.isEmpty() || mRePassword.equals("defined")) {
            errorRepassword.setValue(Empty);
        } else {
            mAccountModel.setRePassword(mRePassword);
        }
        if (mRePassword != null && mPassword != null && mRePassword.equals(mPassword) && !mRePassword.isEmpty() && !mPassword.isEmpty()) {
            CreateUserEmail.getInstance().createUser(mAuth, mAccountModel, new CreateUserEmail.ICreateUserEmail() {
                @Override
                public void onSuccess(String uid) {
                    SaveUid.getInstance().saved((Activity) contextMutableLiveData.getValue(), uid);
                    SaveUid.getInstance().saved(activityMutableLiveData.getValue(), uid);
                    registerSuccess.setValue(true);

                }

                @Override
                public void onFail(String fail) {
                    Toast.makeText(contextMutableLiveData.getValue(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    Log.d("errorRegister", "onFail: " + fail);
                }
            });
        } else if (!mRePassword.equals(mPassword)) {
            errorRepassword.setValue("Mật khẩu không trùng khớp");
        }
    }


    public void onClickShowPassword() {
        m_ShowPassword.setValue(!m_ShowPassword.getValue());
    }

    public void onClickShowRePassword() {
        m_ShowRePassword.setValue(!m_ShowRePassword.getValue());

    }
}
