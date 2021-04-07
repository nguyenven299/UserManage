package com.usermanage.view.register;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityRegisterBinding;
import com.google.android.material.snackbar.Snackbar;
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
        mDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mViewModel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = mDatabinding.layoutMain;
        new TransparentStatusBar(this);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        mViewModel.contextMutableLiveData.setValue(this);
        this.setSupportActionBar(mDatabinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        Glide.with(RegisterActivity.this).load(R.drawable.ic_launcher_foreground).into(mDatabinding.imIconLogo);
        mViewModel.activityMutableLiveData.setValue(this);
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