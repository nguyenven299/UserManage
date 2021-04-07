package com.usermanage.view.forgotPassword;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityForgotPasswordBinding;
import com.usermanage.TransparentStatusBar;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ActivityForgotPasswordBinding mDatabiding;
    private ForgotPasswordActivityViewModel mViewModel;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabiding = DataBindingUtil.setContentView(ForgotPasswordActivity.this, R.layout.activity_forgot_password);
        mViewModel = new ViewModelProvider(this).get(ForgotPasswordActivityViewModel.class);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDatabiding.setViewmodel(mViewModel);
        mDatabiding.setLifecycleOwner(this);
        new TransparentStatusBar(this);
        Glide.with(this).load(R.drawable.ic_launcher_foreground).into(mDatabiding.imIconLogo);
        mViewModel.sendEmailResetPassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng kiểm tra hòm thư Email.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();

                }
            }
        });
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}