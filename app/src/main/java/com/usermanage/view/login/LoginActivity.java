package com.usermanage.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.usermanage.R;
import com.google.android.material.snackbar.Snackbar;
import com.usermanage.TransparentStatusBar;
import com.example.usermanage.databinding.ActivityLoginBinding;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.usermanage.base.BaseActivity;
import com.usermanage.dao.userList.UserListDatabase;
import com.usermanage.dao.userList.UserList;
import com.usermanage.dao.user.DaoQuery;
import com.usermanage.dao.user.User;
import com.usermanage.dao.user.UserDatabase;
import com.usermanage.view.forgotPassword.ForgotPasswordActivity;
import com.usermanage.view.main.MainActivity;
import com.usermanage.view.register.RegisterActivity;

import java.util.List;

public class LoginActivity extends BaseActivity implements LifecycleOwner {
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    private LoginActivityViewModel mViewModel;
    private ActivityLoginBinding mDatabinding;
    private View view;
    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new TransparentStatusBar(this);
        mDatabinding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);
        mViewModel = new ViewModelProvider(LoginActivity.this).get(LoginActivityViewModel.class);
        view = mDatabinding.layoutMain;
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        mViewModel.contextMutableLiveData.setValue(this);
        mViewModel.activityMutableLiveData.setValue(LoginActivity.this);
        mViewModel.showPassword.observe(LoginActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    mDatabinding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    mDatabinding.etPassword.setTransformationMethod(null);
                }
            }
        });
        mDatabinding.btLoginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });
        mDatabinding.btLoginFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mViewModel.onClickLoginFB();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("errorFb", "onError: " + error.getMessage());
            }
        });
        mViewModel.loginSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

            }
        });
        mDatabinding.btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        mDatabinding.btForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        UserDatabase userDatabase = UserDatabase.getInsance(this);
        DaoQuery userDao = userDatabase.daoQuery();
        List<User> users = userDao.getAll();
        for (User user : users) {
        }
        UserListDatabase userListDatabase = UserListDatabase.getInsance(this);
        List<UserList> userLists = userListDatabase.daoQueryList().getAll();
        for (UserList userList : userLists) {

        }
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        snackbar = Snackbar.make(view, "Không có kết nối Internet\nKhông thể đăng nhập", Snackbar.LENGTH_INDEFINITE).setAction("Đồng ý", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        if (b) {
            snackbar.dismiss();
        } else {
            snackbar.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            mViewModel.onClickLoginGG(data);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}