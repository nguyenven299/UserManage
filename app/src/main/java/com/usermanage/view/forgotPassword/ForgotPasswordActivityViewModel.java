package com.usermanage.view.forgotPassword;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivityViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public MutableLiveData<Boolean> sendEmailResetPassword = new MutableLiveData<>();

    public void onClick() {
        String mEmail = email.getValue();
        if (mEmail == null || mEmail.isEmpty()) {
            errorEmail.setValue("Vui lòng nhập Email");
        } else {
            firebaseAuth.sendPasswordResetEmail(mEmail.trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                sendEmailResetPassword.setValue(true);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            sendEmailResetPassword.setValue(false);
                            Log.d("errorResetPassword", "onFailure: " + e.getMessage());
                        }
                    });
        }
    }
}
