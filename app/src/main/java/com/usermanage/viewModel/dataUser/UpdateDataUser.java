package com.usermanage.viewModel.dataUser;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.model.UserModel;

import java.util.HashMap;
import java.util.Map;

public class UpdateDataUser {
    public static UpdateDataUser instace;

    public static UpdateDataUser getInstance() {
        if (instace == null)
            instace = new UpdateDataUser();
        return instace;
    }

    public interface IInsertDataUser {
        void onSuccess(String success);

        void onFail(String fail);
    }

    public void update(FirebaseFirestore mFirestore, UserModel mUserModel, IInsertDataUser iInsertDataUser) {
        Map<String, Object> user = new HashMap<>();
        user.put("Email", mUserModel.getEmail());
        user.put("Name", mUserModel.getName());
        user.put("Uid", mUserModel.getUid());
        user.put("Birthday", mUserModel.getBirthday());
        user.put("PhoneNumber", mUserModel.getPhoneNumber());
        mFirestore.collection("Users").document(mUserModel.getUid()).update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        iInsertDataUser.onSuccess("Insert Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iInsertDataUser.onFail(e.getMessage());
                    }
                });


    }
}
