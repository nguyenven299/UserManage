package com.usermanage.view.ui.listfriend;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.dao.userList.UserListDatabase;
import com.usermanage.dao.userList.UserList;
import com.usermanage.model.UserModel;
import com.usermanage.viewModel.dataUser.GetDataAllUser;

import java.util.List;

public class ListFriendFragmentViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> birthday = new MutableLiveData<>();
    public MutableLiveData<String> avatar = new MutableLiveData<>();
    public MutableLiveData<UserModel> userModelMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Context> contextMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<UserList>> userMutableLiveData = new MutableLiveData<>();
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public MutableLiveData<String> getAvatar() {
        return avatar;
    }

    private MutableLiveData<UserModel> getUserModel() {
        return userModelMutableLiveData;
    }

    private MutableLiveData<List<UserList>> getUserList() {
        return userMutableLiveData;
    }

    public void getDBOnline() {
        userModelMutableLiveData = new MutableLiveData<>();
        UserListDatabase userDatabase = UserListDatabase.getInsance(activityMutableLiveData.getValue());
        if (userDatabase.daoQueryList().getAll() == null || userDatabase.daoQueryList().getUser() == null) {
            GetDataAllUser.getInstance().getData(mFirestore, new GetDataAllUser.IFindDataUser() {
                @Override
                public void onSuccess(List<UserModel> userModelList) {
                    UserList userList = new UserList();
                    for (UserModel userModel : userModelList) {
                        userList.setUid(userModel.getUid());
                        userList.setName(userModel.getName());
                        userList.setEmail(userModel.getEmail());
                        userList.setBirthday(userModel.getBirthday());
                        userList.setAvatar(userModel.getAvatar());
                        userList.setPhoneNumber(userModel.getPhoneNumber());
                        userDatabase.daoQueryList().insertData(userList);
                    }
                    getDB();
                }

                @Override
                public void onFail(String fail) {

                }
            });
        } else {
            GetDataAllUser.getInstance().getData(mFirestore, new GetDataAllUser.IFindDataUser() {
                @Override
                public void onSuccess(List<UserModel> userModelList) {
                    UserList userList = new UserList();
                    userDatabase.daoQueryList().deleteUserListById();
                    for (UserModel userModel : userModelList) {
                        userList.setUid(userModel.getUid());
                        userList.setName(userModel.getName());
                        userList.setEmail(userModel.getEmail());
                        userList.setBirthday(userModel.getBirthday());
                        userList.setAvatar(userModel.getAvatar());
                        userList.setPhoneNumber(userModel.getPhoneNumber());
                        userDatabase.daoQueryList().insertData(userList);
                    }
                    getDB();
                }

                @Override
                public void onFail(String fail) {

                }
            });
        }
    }

    public void getDB() {
        UserListDatabase userDatabase = UserListDatabase.getInsance(activityMutableLiveData.getValue());
        userMutableLiveData.setValue(userDatabase.daoQueryList().getAll());
    }
}
