package com.usermanage.viewModel.dataUser;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.usermanage.model.AccountModel;
import com.usermanage.model.UserModel;

public class GetDataUser {
    public static GetDataUser instace;

    public static GetDataUser getInstance() {
        if (instace == null)
            instace = new GetDataUser();
        return instace;
    }

    public interface IFindDataUser {
        void onSuccess(UserModel userModel);

        void onSuccessAccount(AccountModel accountModel);

        void onExist();

        void onEmpty();
    }

    public void getData(FirebaseFirestore mFirestore, String uid, IFindDataUser iFindDataUser) {
        DocumentReference documentReference = mFirestore.collection("Users").document(uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {

                    if (value.exists()) {
                        iFindDataUser.onExist();
                        iFindDataUser.onSuccess(value.toObject(UserModel.class));
                        iFindDataUser.onSuccessAccount(value.toObject(AccountModel.class));
                    } else {
                        iFindDataUser.onEmpty();
                    }
                }
            }
        });
    }
}
