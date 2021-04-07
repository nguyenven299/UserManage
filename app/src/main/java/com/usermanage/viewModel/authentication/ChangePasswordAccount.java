package com.usermanage.viewModel.authentication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.model.AccountModel;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordAccount {
    public static ChangePasswordAccount instance;

    public static ChangePasswordAccount getInstance() {
        if (instance == null)
            instance = new ChangePasswordAccount();
        return instance;
    }

    public interface IChangePasswordAccount {
        void onSuccess(String success);

        void onFail(String fail);

        void onFalse(String onfalse);

    }

    public void changePassword(FirebaseFirestore mFirestore, FirebaseAuth mAuth, AccountModel mAccountModel, IChangePasswordAccount iChangePasswordAccount) {
        mAuth.getCurrentUser().updatePassword(mAccountModel.getNewPassword())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        iChangePasswordAccount.onSuccess("Thay đổi mật khẩu thành công");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("changePasswordAuth", "onFailure: " + e.getMessage());

                    }
                });
    }

}
