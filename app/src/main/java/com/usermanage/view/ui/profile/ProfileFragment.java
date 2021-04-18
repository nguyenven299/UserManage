package com.usermanage.view.ui.profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.example.usermanage.databinding.FragmentProfileBinding;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.usermanage.base.BaseFragment;
import com.usermanage.dao.user.UserDatabase;
import com.usermanage.dao.userList.UserListDatabase;
import com.usermanage.model.UserViewModel;
import com.usermanage.view.camera.CameraXActivity;
import com.usermanage.view.changePassword.ChangePasswordActivity;
import com.usermanage.view.updateDataUser.UpdateDataUserActivity;
import com.usermanage.viewModel.authentication.LogOutAccount;
import com.usermanage.viewModel.dataUser.GetUid;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding mDatabinding;
    private ProfileFragmentViewModel mViewModel;
    public GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();
    private UserViewModel mUserViewModel = new UserViewModel();
    private int REQUEST_TAKE_PHOTO = 0;
    private int REQUEST_SELECT_IMAGE_IN_ALBUM = 1;
    private View view;
    private Snackbar mSnackbar;
    private MutableLiveData<Boolean> mCheckConnectInternet = new MutableLiveData<>();
    private UserDatabase mUserDatabase;
    private UserListDatabase mUserListDatabase;
    private int WRITE_EXTERNAL_STORAGE = 1;
    private int READ_EXTERNAL_STORAGE = 2;
    private int CAMERA = 3;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mDatabinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initUi();
        initData();
        return mDatabinding.getRoot();
    }

    private void initUi() {
        view = mDatabinding.layoutMain;
        mViewModel = new ViewModelProvider(ProfileFragment.this).get(ProfileFragmentViewModel.class);
        mViewModel.contextMutableLiveData.setValue(getContext());
        mViewModel.activityMutableLiveData.setValue(getActivity());
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mDatabinding.setLifecycleOwner(this);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.toolbar.inflateMenu(R.menu.menu_profile);
        mUserDatabase = UserDatabase.getInsance(getActivity());
        mUserListDatabase = UserListDatabase.getInsance(getActivity());
    }

    private void initData() {
        mViewModel.getAvatar().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null && !s.isEmpty()) {
                    Glide.with(ProfileFragment.this).load(s).into(mDatabinding.hinhDaiDien);
                } else {
                    Glide.with(ProfileFragment.this).load(R.drawable.ic_launcher_foreground).into(mDatabinding.hinhDaiDien);
                }
            }
        });
        mDatabinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.change_profile:
                        getActivity().startActivity(new Intent(getContext(), UpdateDataUserActivity.class));
                        break;
                    case R.id.chane_password:
                        String uid = GetUid.getInstance().get(getActivity());
                        if (Profile.getCurrentProfile() != null) {
                            if (uid.equals(Profile.getCurrentProfile().getId())) {
                                Toast.makeText(getActivity(), "Bạn không thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (GoogleSignIn.getLastSignedInAccount(getActivity()) != null) {
                            if (uid.equals(GoogleSignIn.getLastSignedInAccount(getActivity()).getId())) {
                                Toast.makeText(getActivity(), "Bạn không thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            getActivity().startActivity(new Intent(getContext(), ChangePasswordActivity.class));
                        }
                        break;
                    case R.id.log_out:
                        LogOutAccount.getInstance().checkAccountForLogout(getActivity());
                        mUserDatabase.daoQuery().deleteUserById(1);
                        mUserListDatabase.daoQueryList().deleteUserListById();
                        break;
                }
                return false;
            }
        });
        mDatabinding.hinhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChosse();
                Log.d("TAG", "onClick: ahiahiahihia");
            }
        });
        mViewModel.changeImage.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null && !s.isEmpty()) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thay đổi ảnh thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mViewModel.getDBOnline(GetUid.getInstance().get(getActivity()));
    }


    private void dialogChosse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chọn chế độ hình");
        builder.setMessage("Bạn muốn đăng hình từ thư viện hay máy ảnh?");
        builder.setCancelable(false);
        builder.setPositiveButton("Thư viện", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                } else {
                    imageLibrary();
                }
            }
        });
        builder.setNegativeButton("Máy ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA);
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);

                } else {
                    imageCapture();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE) {
            imageLibrary();
        }
        if (requestCode == CAMERA) {
            imageCapture();
        }
    }

    private void imageLibrary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM);
    }

    private void imageCapture() {
        startActivity(new Intent(getContext(), CameraXActivity.class));
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == RESULT_OK) {
            Glide.with(getContext()).load(mViewModel.getBitmapFromUri(data.getData())).into(mDatabinding.hinhDaiDien);
        }
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        mSnackbar = Snackbar.make(view, "Mất kết nối Internet", Snackbar.LENGTH_LONG);
        if (b) {
            mCheckConnectInternet.setValue(b);
            mSnackbar.dismiss();
        } else {
            mCheckConnectInternet.setValue(b);
            mSnackbar.show();
        }
    }
}