package com.usermanage.view.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.usermanage.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.usermanage.model.UserViewModel;
import com.usermanage.view.ui.listfriend.ListFriendFragment;
import com.usermanage.view.ui.location.LocationFragment;
import com.usermanage.view.ui.profile.ProfileFragment;
import com.usermanage.viewModel.dataUser.GetUid;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    private MainActivityViewModel mViewModel;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            Fragment fragment = new LocationFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
        }
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(naListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.activityMutableLiveData.setValue(this);
        mViewModel.saveSession();
        mViewModel.checkDBUser(GetUid.getInstance().get(MainActivity.this));
        permission();
    }


    private void permission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.isAnyPermissionPermanentlyDenied()) {
                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Cần cung cấp quyền");
        builder.setMessage("Để cung cấp quyền cho ứng dụng trong tương lai. \nBạn có thể cung cấp trong cài đặt.");
        builder.setPositiveButton("Đi đến cài đặt", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Trở về", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener naListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    fragment = new LocationFragment();
                    break;
                case R.id.navigation_list_friend:
                    fragment = new ListFriendFragment();
                    break;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}