package com.usermanage.view.register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;
import com.usermanage.CloseKeyboardClickOutside;
import com.usermanage.TransparentStatusBar;
import com.usermanage.base.BaseActivity;
import com.usermanage.view.insertData.InsertDataActivity;

public class RegisterActivity extends BaseActivity {
    private RegisterActivityViewModel mViewModel;
    private ActivityRegisterBinding mDatabinding;
    private View view;
    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUi();
        initData();
    }

    private void initData() {
        mViewModel.m_ShowPassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    mDatabinding.etPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    mDatabinding.etPassword.setTransformationMethod(null);
                }
            }
        });
        mViewModel.m_ShowRePassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    mDatabinding.etRePassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    mDatabinding.etRePassword.setTransformationMethod(null);
                }
            }
        });
        mViewModel.registerSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(RegisterActivity.this, InsertDataActivity.class);
                    intent.putExtra("email", mViewModel.email.getValue());
                    startActivity(intent);
                    finish();
                }
            }
        });
        mViewModel.registerResultFail.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s == "The email address is already in use by another account.") {
                    Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Có lỗi xảy ra, hãy thử lại sau", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void initUi() {
        new TransparentStatusBar(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mViewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);
        view = mDatabinding.layoutMain;
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        mDatabinding.layoutMain.setOnClickListener(new CloseKeyboardClickOutside(this));
        mViewModel.contextMutableLiveData.setValue(this);
        this.setSupportActionBar(mDatabinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        Glide.with(RegisterActivity.this).load(R.drawable.ic_launcher_foreground).into(mDatabinding.imIconLogo);
        mViewModel.activityMutableLiveData.setValue(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        snackbar = Snackbar.make(view, "Không có kết nối Internet\nKhông thể đăng ký", Snackbar.LENGTH_INDEFINITE).setAction("Đồng ý", new View.OnClickListener() {
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
}