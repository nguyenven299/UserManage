package com.usermanage.viewModel.dataUser;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.usermanage.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class GetDataAllUser {
    public static GetDataAllUser instace;

    public static GetDataAllUser getInstance() {
        if (instace == null)
            instace = new GetDataAllUser();
        return instace;
    }

    public interface IFindDataUser {
        void onSuccess(List<UserModel> userModelList);

        void onFail(String fail);
    }

    public void getData(FirebaseFirestore db, IFindDataUser iFindDataUser) {
        List<UserModel> userModelList = new ArrayList<>();
        db.collection("Users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            userModelList.add(documentSnapshot.toObject(UserModel.class));
                        }
                        iFindDataUser.onSuccess(userModelList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
