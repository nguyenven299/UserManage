package com.usermanage.view.changePassword;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityChangePasswordBinding;
import com.usermanage.TransparentStatusBar;
import com.usermanage.viewModel.dataUser.GetUid;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding mDatabinding;
    private ChangePasswordActivityViewModel mViewModel;
    private TransparentStatusBar transparentStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mViewModel = new ViewModelProvider(this).get(ChangePasswordActivityViewModel.class);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        mViewModel.uid.setValue(GetUid.getInstance().get(this));
        mViewModel.contextMutableLiveData.setValue(this);
        transparentStatusBar = new TransparentStatusBar(this);
        setSupportActionBar(mDatabinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        ImageView imageView = findViewById(R.id.im_change_password);
        Glide.with(this).load(R.drawable.ic_change_password).into(imageView);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}