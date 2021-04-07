package com.usermanage.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserViewModel extends ViewModel {
    MutableLiveData<UserModel> userViewModelMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<UserModel>> listMutableLiveData = new MutableLiveData<>();

    public void setUserViewModel(UserModel userViewModel) {
        userViewModelMutableLiveData.setValue(userViewModel);
    }

    public MutableLiveData<UserModel> getUserViewModelMutableLiveData() {
        return userViewModelMutableLiveData;
    }

    public void setListUserViewModel(List<UserModel> listUserViewModel) {
        listMutableLiveData.setValue(listUserViewModel);
    }

    public MutableLiveData<List<UserModel>> getListUserViewModelMutableLiveData() {
        return listMutableLiveData;
    }
}
