package com.usermanage.viewModel.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.usermanage.view.login.LoginActivity;
import com.usermanage.view.ui.profile.ProfileFragment;
import com.usermanage.viewModel.dataUser.GetUid;
import com.usermanage.viewModel.dataUser.SaveUid;

public class LogOutAccount {
    public static LogOutAccount instance;

    public static LogOutAccount getInstance() {
        if (instance == null)
            instance = new LogOutAccount();
        return instance;
    }

    public void checkAccountForLogout(Activity activity) {
        String uid = GetUid.getInstance().get(activity);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Profile.getCurrentProfile() != null) {
                    if (uid.equals(Profile.getCurrentProfile().getId())) {
                        SavaDateSession.getInstance().saved(activity, (long) 1);
                        SaveUid.getInstance().saved(activity, null);
                        LoginManager.getInstance().logOut();
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                }
                if (GoogleSignIn.getLastSignedInAccount(activity) != null) {
                    if (uid.equals(GoogleSignIn.getLastSignedInAccount(activity).getId())) {
                        ProfileFragment profileFragmentViewModel = new ProfileFragment();
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, profileFragmentViewModel.gso);
                        mGoogleSignInClient.signOut().isSuccessful();
                        SavaDateSession.getInstance().saved(activity, (long) 1);
                        SaveUid.getInstance().saved(activity, null);
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                }
                if (user != null) {
                    if (uid.equals(user.getUid())) {
                        SavaDateSession.getInstance().saved(activity, (long) 1);
                        SaveUid.getInstance().saved(activity, null);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                }
            }
        }, 1000);

    }
}
