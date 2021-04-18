package com.usermanage.view.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.model.AccountModel;
import com.usermanage.model.UserModel;
import com.usermanage.view.main.MainActivity;
import com.usermanage.viewModel.authentication.LoginUserEmail;
import com.usermanage.viewModel.dataUser.GetDataUser;
import com.usermanage.viewModel.dataUser.InsertDataUser;
import com.usermanage.viewModel.dataUser.SaveUid;

public class LoginActivityViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<String> errorPassword = new MutableLiveData<>();
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> showPassword = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>(false);
    private MutableLiveData<String> mCheckDataUSer = new MutableLiveData<>();
    private AccountModel mAccountModel = new AccountModel();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private UserModel mUserModel;
    public MutableLiveData<String> loginResultFail = new MutableLiveData<>();
    public MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>(false);
    public MutableLiveData<String> insertDataUser = new MutableLiveData<>();

    public void onClickLogin() {
        String mEmail = email.getValue();
        String mPassword = password.getValue();
        String empty = "Trường này không được trống";
        if (mEmail == null || mEmail.isEmpty()) {
            errorEmail.setValue(empty);
        } else {
            mAccountModel.setAccount(mEmail.trim());
        }
        if (mPassword == null || mPassword.isEmpty()) {
            errorPassword.setValue(empty);
        } else {
            mAccountModel.setPassword(mPassword.trim());
        }
        if (mAccountModel.getAccount() != null && !mAccountModel.getAccount().isEmpty()
                && mAccountModel.getPassword() != null && !mAccountModel.getPassword().isEmpty()
        ) {
            showProgressBar.setValue(true);
            LoginUserEmail.getInstance().loginUser(mAuth, mAccountModel, new LoginUserEmail.ILoginUserEmail() {
                @Override
                public void onSuccess(String success) {
                    showProgressBar.setValue(false);

                }

                @Override
                public void onFail(String fail) {
                    Log.d("errorLogin", "onFail: " + fail);
                    loginResultFail.setValue(fail);
                    showProgressBar.setValue(false);
                }

                @Override
                public void onUid(String uid) {
                    checkDataUSerEmail(mFirestore, uid);
                    SaveUid.getInstance().saved(activityMutableLiveData.getValue(), uid);
                }
            });
        }
    }

    public void onClickLoginFB() {
        Profile profile = Profile.getCurrentProfile();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (profile.getProfilePictureUri(500, 500) != null) {
                    mUserModel = new UserModel();
                    mUserModel.setUid(profile.getId());
                    mUserModel.setName(profile.getName());
                    mUserModel.setAvatar(profile.getProfilePictureUri(500, 500).toString());
                } else {
                    mUserModel = new UserModel();
                    mUserModel.setUid(profile.getId());
                    mUserModel.setName(profile.getName());
                }
            }
        });
        thread.start();

        checkDataUSer(mFirestore, profile.getId());
        SaveUid.getInstance().saved(activityMutableLiveData.getValue(), profile.getId());

    }

    public void onClickLoginGG(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Uri personPhoto = account.getPhotoUrl();
                checkDataUSer(mFirestore, account.getId());
                SaveUid.getInstance().saved(activityMutableLiveData.getValue(), account.getId());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (personPhoto != null) {
                            mUserModel = new UserModel();
                            mUserModel.setUid(account.getId());
                            mUserModel.setEmail(account.getEmail());
                            mUserModel.setName(account.getDisplayName());
                            mUserModel.setAvatar(personPhoto.toString());
                        } else {
                            mUserModel = new UserModel();
                            mUserModel.setUid(account.getId());
                            mUserModel.setEmail(account.getEmail());
                            mUserModel.setName(account.getDisplayName());
                        }
                    }
                });
                thread.start();
            }
        } catch (ApiException e) {
            Log.w("errorGG", "Google sign in failed", e);
        }

    }

    public void onClickShowPassword() {
        showPassword.setValue(!showPassword.getValue());
    }

    private void checkDataUSer(FirebaseFirestore db, String uid) {
        showProgressBar.setValue(false);

        GetDataUser.getInstance().getData(db, uid, new GetDataUser.IFindDataUser() {
            @Override
            public void onSuccess(UserModel mUserModel1) {
            }

            @Override
            public void onSuccessAccount(AccountModel accountModel) {

            }

            @Override
            public void onExist() {
                mCheckDataUSer.setValue("Exist");
                loginSuccess.setValue(true);
            }

            @Override
            public void onEmpty() {
                insertInformationUser(db, mUserModel);

            }
        });
    }

    private void checkDataUSerEmail(FirebaseFirestore db, String uid) {
        insertDataUser = new MutableLiveData<>();
        showProgressBar.setValue(false);
        GetDataUser.getInstance().getData(db, uid, new GetDataUser.IFindDataUser() {
            @Override
            public void onSuccess(UserModel mUserModel) {
                if (mUserModel.getName() == null || mUserModel.getName().isEmpty()) {
                    insertDataUser.setValue(email.getValue());
                } else {
                    mCheckDataUSer.setValue("Exist");
                    loginSuccess.setValue(true);
                }
            }

            @Override
            public void onSuccessAccount(AccountModel accountModel) {

            }

            @Override
            public void onExist() {

            }

            @Override
            public void onEmpty() {
                insertDataUser.setValue(email.getValue());
            }
        });
    }

    private void insertInformationUser(FirebaseFirestore db, UserModel mUserModel) {
        InsertDataUser.getInstance().insertData(db, mUserModel, new InsertDataUser.IInsertDataUser() {
            @Override
            public void onSuccess(String success) {
                mCheckDataUSer.setValue(success);
                contextMutableLiveData.getValue().startActivity(new Intent(contextMutableLiveData.getValue(), MainActivity.class));
            }

            @Override
            public void onFail(String fail) {

            }
        });
    }

}
