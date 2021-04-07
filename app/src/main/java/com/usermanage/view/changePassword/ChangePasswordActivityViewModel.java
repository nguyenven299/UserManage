package com.usermanage.view.changePassword;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.usermanage.model.AccountModel;
import com.usermanage.viewModel.authentication.ChangePasswordAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivityViewModel extends ViewModel {
    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> reNewPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorNewPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorReNewPassword = new MutableLiveData<>();
    public MutableLiveData<String> uid = new MutableLiveData<>();
    public MutableLiveData<Boolean> showPassword = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> showNewPassword = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> showReNewPassword = new MutableLiveData<>(false);
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    private AccountModel mAccountModel = new AccountModel();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public void onClick() {
        String Empty = "Trường này không được trống";
        String mNewPassword = newPassword.getValue();
        String mReNewPassword = reNewPassword.getValue();
        if (mNewPassword == null || mNewPassword.isEmpty() || mNewPassword.equals("defined")) {
            errorNewPassword.setValue(Empty);
        }
        if (mReNewPassword == null || mReNewPassword.isEmpty() || mReNewPassword.equals("defined")) {
            errorReNewPassword.setValue(Empty);
        }
        if (mNewPassword != null && !mNewPassword.isEmpty() && !mNewPassword.equals("defined")
                && mReNewPassword != null && !mReNewPassword.isEmpty() && !mReNewPassword.equals("defined")) {
            if (mNewPassword.length() < 6 || mReNewPassword.length() < 6) {
                errorNewPassword.setValue("Mật khẩu phải hơn 6 ký tự");
                errorReNewPassword.setValue("Mật khẩu phải hơn 6 ký tự");
            } else if (mNewPassword.equals(mReNewPassword)) {
                mAccountModel.setNewPassword(mReNewPassword);
                mAccountModel.setUid(uid.getValue());
                ChangePasswordAccount.getInstance().changePassword(mFirestore, mAuth, mAccountModel, new ChangePasswordAccount.IChangePasswordAccount() {
                    @Override
                    public void onSuccess(String success) {
                        Toast.makeText(contextMutableLiveData.getValue(), success, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String fail) {
                        Toast.makeText(contextMutableLiveData.getValue(), fail, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFalse(String onfalse) {
                        Toast.makeText(contextMutableLiveData.getValue(), onfalse, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                errorReNewPassword.setValue("Mật khẩu không trùng khớp");
            }
        }

    }


    public void onShowNewPassword() {
        showNewPassword.setValue(!showNewPassword.getValue());
    }

    public void onShowReNewPassword() {
        showReNewPassword.setValue(!showReNewPassword.getValue());
    }
}