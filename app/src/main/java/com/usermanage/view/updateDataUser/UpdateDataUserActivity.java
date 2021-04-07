package com.usermanage.view.updateDataUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.usermanage.R;
import com.example.usermanage.databinding.ActivityUpdateDataUserBinding;
import com.usermanage.TransparentStatusBar;
import com.usermanage.model.UserModel;
import com.usermanage.model.UserViewModel;
import com.usermanage.view.main.MainActivity;
import com.usermanage.viewModel.dataUser.GetUid;

public class UpdateDataUserActivity extends AppCompatActivity {
    private ActivityUpdateDataUserBinding mDatabinding;
    private UpdateDataUserActivityViewModel mViewModel;
    private UserViewModel mUserViewModel = new UserViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabinding = DataBindingUtil.setContentView(this, R.layout.activity_update_data_user);
        mViewModel = new ViewModelProvider(this).get(UpdateDataUserActivityViewModel.class);
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.setLifecycleOwner(this);
        setSupportActionBar(mDatabinding.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        new TransparentStatusBar(this);
        mViewModel.contextMutableLiveData.setValue(this);
        mViewModel.activityMutableLiveData.setValue(UpdateDataUserActivity.this);
        mDatabinding.dpBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.selectBirthday(UpdateDataUserActivity.this);
            }
        });
        mViewModel.setUser(GetUid.getInstance().get(UpdateDataUserActivity.this));
        mViewModel.insertSuccess.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
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
}