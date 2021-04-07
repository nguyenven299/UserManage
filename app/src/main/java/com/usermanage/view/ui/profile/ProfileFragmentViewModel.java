package com.usermanage.view.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.usermanage.dao.user.User;
import com.usermanage.dao.user.UserDatabase;
import com.usermanage.model.UserModel;
import com.usermanage.viewModel.dataUser.GetDataUser;
import com.usermanage.viewModel.dataUser.UpdateAvatar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.usermanage.GetPathUri.getPath;

public class ProfileFragmentViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> birthday = new MutableLiveData<>();
    public MutableLiveData<String> avatar = new MutableLiveData<>();
    public MutableLiveData<User> userModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    public MutableLiveData<String> changeImage = new MutableLiveData<>();
    private MutableLiveData<String> userId = new MutableLiveData<>();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public MutableLiveData<String> getAvatar() {
        return avatar;
    }

    public void getDBOnline(String uid) {
        UserDatabase userDatabase = UserDatabase.getInsance(activityMutableLiveData.getValue());
        userId.setValue(uid);
        userModelMutableLiveData = new MutableLiveData<>();
        User user = new User();
        if (userDatabase.daoQuery().getUser() == null) {
            GetDataUser.getInstance().getData(mFirestore, uid, new GetDataUser.IFindDataUser() {
                @Override
                public void onSuccess(UserModel userModel) {
                    user.setName(userModel.getName());
                    user.setUid(userModel.getUid());
                    user.setEmail(userModel.getEmail());
                    user.setBirthday(userModel.getBirthday());
                    user.setPhoneNumber(userModel.getPhoneNumber());
                    user.setAvatar(userModel.getAvatar());
                    user.setId(1);
                    userDatabase.daoQuery().insertData(user);
                    getDBLocal();
                }

                @Override
                public void onExist() {

                }

                @Override
                public void onEmpty() {

                }
            });
        } else {
            getDBLocal();
            GetDataUser.getInstance().getData(mFirestore, uid, new GetDataUser.IFindDataUser() {
                @Override
                public void onSuccess(UserModel userModel) {
                    user.setName(userModel.getName());
                    user.setUid(userModel.getUid());
                    user.setEmail(userModel.getEmail());
                    user.setBirthday(userModel.getBirthday());
                    user.setPhoneNumber(userModel.getPhoneNumber());
                    user.setAvatar(userModel.getAvatar());
                    userDatabase.daoQuery().update(user);
                    getDBLocal();
                }

                @Override
                public void onExist() {

                }

                @Override
                public void onEmpty() {

                }
            });
        }
    }

    public void getDBLocal() {
        UserDatabase userDatabase = UserDatabase.getInsance(activityMutableLiveData.getValue());
        setUser(userDatabase.daoQuery().getUser());
    }

    public void setUser(User user) {
        email.setValue(user.getEmail() == null ? "" : user.getEmail());
        name.setValue(user.getName() == null ? "" : user.getName());
        phoneNumber.setValue(user.getPhoneNumber());
        avatar.setValue(user.getAvatar());
        if (user.getBirthday() != null && !user.getBirthday().isEmpty()) {
            Calendar cal = Calendar.getInstance();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            cal.setTimeInMillis(Long.parseLong(user.getBirthday()));
            birthday.setValue(df.format(cal.getTime()));
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {
        Bitmap bitmapRotated = null;
        try {
            ExifInterface exif = new ExifInterface(getPath(contextMutableLiveData.getValue(), uri));
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotationInDegrees != 0) {
                matrix.postRotate(rotationInDegrees);
            }
            Bitmap bitmap = BitmapFactory.decodeFile(getPath(contextMutableLiveData.getValue(), uri));
            bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } catch (IOException e) {
            Log.e("WallOfLightApp", e.getMessage());
        }
        Bitmap finalBitmapRotated = bitmapRotated;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                updateImageDB(finalBitmapRotated);
            }
        });
        thread.start();
        return bitmapRotated;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private void updateImageDB(Bitmap bitmap) {
        UpdateAvatar.getInstance().updateAvatarUser(firebaseStorage, bitmap, mFirestore, userId.getValue(), new UpdateAvatar.IUpdateAvatar() {
            @Override
            public void onSuccess(String success) {
                changeImage.setValue(success);
            }

            @Override
            public void onFail(String fail) {
                Log.d("failChangeImage", "onFail: " + fail);
            }
        });
    }
}
