package com.usermanage.view.start;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.usermanage.TransparentStatusBar;
import com.usermanage.dao.user.User;
import com.usermanage.dao.user.UserDatabase;
import com.usermanage.view.login.LoginActivity;
import com.usermanage.view.main.MainActivity;

public class StartActivity extends AppCompatActivity {
    private StartActivityViewModel mViewModel;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mViewModel = new ViewModelProvider(this).get(StartActivityViewModel.class);
        new TransparentStatusBar(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewModel.activityMutableLiveData.setValue(StartActivity.this);
                mViewModel.checkUserCurrent();
            }
        }, 1500);
        ImageView im_start = findViewById(R.id.im);
        Glide.with(this).load(R.drawable.ic_start).into(im_start);
        mViewModel.successLogin.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
            }
        });
        mViewModel.successSession.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }
}