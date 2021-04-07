package com.usermanage.viewModel.authentication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.usermanage.model.AccountModel;

public class LoginUserEmail {
    public static LoginUserEmail instance;


    public static LoginUserEmail getInstance() {
        if (instance == null)
            instance = new LoginUserEmail();
        return instance;
    }

    public interface ILoginUserEmail {
        void onSuccess(String success);

        void onFail(String fail);

        void onUid(String uid);
    }

    public void loginUser(FirebaseAuth mAuth, AccountModel mAccountModel, ILoginUserEmail iLoginUserEmail) {
        String acc = mAccountModel.getAccount();
        String pass = mAccountModel.getPassword();
        mAuth.signInWithEmailAndPassword(acc, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    iLoginUserEmail.onSuccess("Connect Success");
                    FirebaseUser user = mAuth.getInstance().getCurrentUser();
                    iLoginUserEmail.onUid(user.getUid());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLoginUserEmail.onFail(e.getMessage());
            }
        });
    }
}
