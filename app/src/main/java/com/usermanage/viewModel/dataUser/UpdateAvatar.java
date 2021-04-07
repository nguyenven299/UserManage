package com.usermanage.viewModel.dataUser;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.usermanage.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateAvatar {
    public static UpdateAvatar instance;

    public static UpdateAvatar getInstance() {
        if (instance == null)
            instance = new UpdateAvatar();
        return instance;
    }

    public interface IUpdateAvatar {
        void onSuccess(String success);

        void onFail(String fail);
    }

    public void updateAvatarUser(FirebaseStorage mStorage, Bitmap bitmap, FirebaseFirestore mFirestore,String uid, IUpdateAvatar iUpdateAvatar) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        mStorage.getReference().child("Users").child(uid).putBytes(data)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Avatar", uri.toString());
                                    mFirestore.collection("Users").document(uid).update(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    iUpdateAvatar.onSuccess("Insert Success");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    iUpdateAvatar.onFail(e.getMessage());
                                                }
                                            });
                                }
                            });

                        }
                    }
                });

    }
}
