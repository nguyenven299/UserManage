package com.usermanage.view.insertData;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityInsertDataBinding;
import com.google.android.material.snackbar.Snackbar;
import com.usermanage.CloseKeyboardClickOutside;
import com.usermanage.TransparentStatusBar;
import com.usermanage.base.BaseActivity;
import com.usermanage.view.main.MainActivity;
import com.usermanage.viewModel.dataUser.GetUid;

public class InsertDataActivity extends BaseActivity {
    private ActivityInsertDataBinding mDatabinding;
    private InsertDataActivityViewModel mViewModel;
    private View view;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUi();
        initData();
    }

    private void initData() {
        String email = getIntent().getStringExtra("email");
        mViewModel.showEmail(email, GetUid.getInstance().get(this));
        mDatabinding.dpBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.selectBirthday(InsertDataActivity.this);
            }
        });
    }

    private void initUi() {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new TransparentStatusBar(this);
        view = findViewById(R.id.layoutMain);
        mViewModel = new ViewModelProvider(InsertDataActivity.this).get(InsertDataActivityViewModel.class);
        mDatabinding = DataBindingUtil.setContentView(InsertDataActivity.this, R.layout.activity_insert_data);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        mDatabinding.layoutMain.setOnClickListener(new CloseKeyboardClickOutside(this));
        mViewModel.insertSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(InsertDataActivity.this, mViewModel.successString.getValue(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InsertDataActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        snackbar = Snackbar.make(view, "Không có kết nối Internet\nKhông thể thêm thông tin", Snackbar.LENGTH_INDEFINITE).setAction("Đồng ý", new View.OnClickListener() {
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