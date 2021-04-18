package com.usermanage.view.changePassword;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.model.AccountModel;
import com.usermanage.model.UserModel;
import com.usermanage.viewModel.authentication.ChangePasswordAccount;
import com.usermanage.viewModel.dataUser.GetDataUser;

public class ChangePasswordActivityViewModel extends ViewModel {
    public MutableLiveData<String> newPassword = new MutableLiveData<>();
    public MutableLiveData<String> reNewPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorNewPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorReNewPassword = new MutableLiveData<>();
    public MutableLiveData<String> uid = new MutableLiveData<>();
    public MutableLiveData<Boolean> showOldPassword = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> showNewPassword = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> showReNewPassword = new MutableLiveData<>(false);
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> oldPassword = new MutableLiveData<>();
    public MutableLiveData<String> errorOldPassword = new MutableLiveData<>();
    private AccountModel mAccountModel = new AccountModel();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public MutableLiveData<String> changePasswordResult = new MutableLiveData<>();

    public void onClick() {
        String Empty = "Trường này không được trống";
        String mNewPassword = newPassword.getValue();
        String mReNewPassword = reNewPassword.getValue();
        String mOldPassword = oldPassword.getValue();
        if (mOldPassword == null || mOldPassword.isEmpty() || mOldPassword.equals("defined")) {
            errorOldPassword.setValue(Empty);
        }
        if (mNewPassword == null || mNewPassword.isEmpty() || mNewPassword.equals("defined")) {
            errorNewPassword.setValue(Empty);
        }
        if (mReNewPassword == null || mReNewPassword.isEmpty() || mReNewPassword.equals("defined")) {
            errorReNewPassword.setValue(Empty);
        }
        if (mNewPassword != null && !mNewPassword.isEmpty() && !mNewPassword.equals("defined")
                && mReNewPassword != null && !mReNewPassword.isEmpty() && !mReNewPassword.equals("defined")
                && mOldPassword != null && !mOldPassword.isEmpty() && !mOldPassword.equals("defined")) {
            if (mNewPassword.length() < 6 || mReNewPassword.length() < 6) {
                errorNewPassword.setValue("Mật khẩu phải hơn 6 ký tự");
                errorReNewPassword.setValue("Mật khẩu phải hơn 6 ký tự");
                errorOldPassword.setValue("Mật khẩu phải hơn 6 ký tự");
            } else if (mNewPassword.equals(mReNewPassword)) {
                GetDataUser.getInstance().getData(mFirestore, uid.getValue(), new GetDataUser.IFindDataUser() {
                    @Override
                    public void onSuccessAccount(AccountModel accountModel) {
                        if (mOldPassword.equals(accountModel.getPassword()) && !mOldPassword.isEmpty()) {
                            mAccountModel.setNewPassword(mReNewPassword.trim());
                            mAccountModel.setUid(uid.getValue());
                            updatePassword(mAccountModel);
                        } else {
                            errorOldPassword.setValue("Mật khẩu không chính xác");
                        }
                    }

                    @Override
                    public void onSuccess(UserModel userModel) {
                    }

                    @Override
                    public void onExist() {

                    }

                    @Override
                    public void onEmpty() {

                    }
                });


            } else {
                errorReNewPassword.setValue("Mật khẩu không trùng khớp");
            }
        }

    }

    private void updatePassword(AccountModel mAccountModel) {
        ChangePasswordAccount.getInstance().changePassword(mFirestore, mAuth, mAccountModel, new ChangePasswordAccount.IChangePasswordAccount() {
            @Override
            public void onSuccess(String success) {
                changePasswordResult.setValue(success);
            }

            @Override
            public void onFail(String fail) {
                changePasswordResult.setValue(fail);
            }

        });
    }

    public void onShowNewPassword() {
        showNewPassword.setValue(!showNewPassword.getValue());
    }

    public void onShowOldPassword() {
        showOldPassword.setValue(!showOldPassword.getValue());
    }

    public void onShowReNewPassword() {
        showReNewPassword.setValue(!showReNewPassword.getValue());
    }
}