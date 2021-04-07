package com.usermanage.view.ui.profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.usermanage.base.BaseFragment;
import com.usermanage.dao.user.User;
import com.usermanage.dao.user.UserDatabase;
import com.usermanage.dao.userList.UserListDatabase;
import com.usermanage.model.UserViewModel;
import com.usermanage.view.changePassword.ChangePasswordActivity;
import com.usermanage.view.updateDataUser.UpdateDataUserActivity;
import com.usermanage.viewModel.authentication.LogOutAccount;
import com.usermanage.viewModel.dataUser.GetUid;

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
    private Snackbar snackbar;
    private MutableLiveData<Boolean> mCheckConnectInternet = new MutableLiveData<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mDatabinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        view = mDatabinding.layoutMain;
        mViewModel = new ViewModelProvider(ProfileFragment.this).get(ProfileFragmentViewModel.class);
        UserDatabase userDatabase = UserDatabase.getInsance(getActivity());
        UserListDatabase userListDatabase = UserListDatabase.getInsance(getActivity());
        mViewModel.contextMutableLiveData.setValue(getContext());
        mViewModel.activityMutableLiveData.setValue(getActivity());
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        mDatabinding.setLifecycleOwner(this);
        mDatabinding.setViewmodel(mViewModel);
        mDatabinding.toolbar.inflateMenu(R.menu.menu_profile);
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
                        User user1 = new User();
                        user1 = userDatabase.daoQuery().getUser();
                        userDatabase.daoQuery().deleteUserById(1);
                        userListDatabase.daoQueryList().deleteUserListById();
                        break;
                }
                return false;
            }
        });
        mDatabinding.hinhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getContext())
                        .withPermission(Manifest.permission.CAMERA)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                dialogChosse();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    openSettings();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();
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

        return mDatabinding.getRoot();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void dialogChosse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Chọn chế độ hình");
        builder.setMessage("Bạn muốn đăng hình từ thư viện hay máy ảnh?");
        builder.setCancelable(false);
        builder.setPositiveButton("Thư viện", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                imageLibrary();
            }
        });
        builder.setNegativeButton("Máy ảnh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                imageCapture();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void imageLibrary() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM);
    }

    private void imageCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } else {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Glide.with(getContext()).load(mViewModel.getBitmapFromUri(data.getData())).into(mDatabinding.hinhDaiDien);
        } else if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == RESULT_OK) {
            Glide.with(getContext()).load(mViewModel.getBitmapFromUri(data.getData())).into(mDatabinding.hinhDaiDien);
        }
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        snackbar = Snackbar.make(view, "Mất kết nối Internet", Snackbar.LENGTH_LONG);
        if (b) {
            mCheckConnectInternet.setValue(b);
            snackbar.dismiss();
        } else {
            mCheckConnectInternet.setValue(b);
            snackbar.show();
        }
    }
}