package com.usermanage.viewModel.authentication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.usermanage.model.AccountModel;

public class CreateUserEmail {
    public static CreateUserEmail instace;

    public static CreateUserEmail getInstance() {
        if (instace == null)
            instace = new CreateUserEmail();
        return instace;
    }

    public interface ICreateUserEmail {
        void onSuccess(String uid);

        void onFail(String fail);
    }

    public void createUser(FirebaseAuth mAuth, AccountModel mAccountModel, ICreateUserEmail iCreateUserEmail) {
        mAuth.createUserWithEmailAndPassword(mAccountModel.getAccount(), mAccountModel.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        iCreateUserEmail.onSuccess(authResult.getUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iCreateUserEmail.onFail(e.getMessage());
                    }
                });
    }
}
