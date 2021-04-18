package com.usermanage.view.changePassword;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityChangePasswordBinding;
import com.usermanage.CloseKeyboardClickOutside;
import com.usermanage.TransparentStatusBar;
import com.usermanage.viewModel.dataUser.GetUid;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding mDatabinding;
    private ChangePasswordActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initData();
    }

    private void initData() {
        mViewModel.showOldPassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    mDatabinding.etOldPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    mDatabinding.etOldPassword.setTransformationMethod(null);
                }
            }
        });
        mViewModel.showNewPassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    mDatabinding.etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    mDatabinding.etNewPassword.setTransformationMethod(null);
                }
            }
        });
        mViewModel.showReNewPassword.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) {
                    mDatabinding.etReNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    mDatabinding.etReNewPassword.setTransformationMethod(null);
                }
            }
        });
        mViewModel.changePasswordResult.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("Change Success")) {
                    Toast.makeText(ChangePasswordActivity.this, "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                }
                if (s.equals("This operation is sensitive and requires recent authentication. Log in again before retrying this request.")) {
                    Toast.makeText(ChangePasswordActivity.this, "Vui lòng đăng nhập lại trước khi thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initUi() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new TransparentStatusBar(this);
        mDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mViewModel = new ViewModelProvider(this).get(ChangePasswordActivityViewModel.class);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        mViewModel.uid.setValue(GetUid.getInstance().get(this));
        mViewModel.contextMutableLiveData.setValue(this);
        mDatabinding.layoutMain.setOnClickListener(new CloseKeyboardClickOutside(this));
        setSupportActionBar(mDatabinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        ImageView imageView = findViewById(R.id.im_change_password);
        Glide.with(this).load(R.drawable.ic_change_password).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}