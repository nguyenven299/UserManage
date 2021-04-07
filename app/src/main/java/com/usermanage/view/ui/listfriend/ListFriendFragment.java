package com.usermanage.view.ui.listfriend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanage.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.usermanage.base.BaseFragment;
import com.usermanage.dao.userList.UserList;
import com.usermanage.model.UserModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListFriendFragment extends BaseFragment {

    private AdapterListFriend mAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private UserModel mUserModel = new UserModel();
    private ListFriendFragmentViewModel mViewModel;
    private List<UserModel> userModelList = new ArrayList<>();
    private Set<UserModel> setA = new HashSet<UserModel>();
    private View view;
    private Snackbar snackbar;
    private EditText mETSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(getActivity()).get(ListFriendFragmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_friend, container, false);
        recyclerView = root.findViewById(R.id.rv_listfriend);
        toolbar = root.findViewById(R.id.toolbar);
        view = root.findViewById(R.id.layoutMain);
        mETSearch = root.findViewById(R.id.et_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setActionBar(toolbar);
        getActivity().getActionBar().setTitle("Danh sách bạn bè");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        mViewModel.contextMutableLiveData.setValue(getContext());
        mViewModel.activityMutableLiveData.setValue(getActivity());
        mViewModel.getDBOnline();
        mViewModel.userMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<UserList>>() {
            @Override
            public void onChanged(List<UserList> userLists) {
                mAdapter = new AdapterListFriend(getActivity(), userLists);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
        mETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return root;
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        snackbar = Snackbar.make(view, "Mất kết nối Internet", Snackbar.LENGTH_LONG);
        if (b) {
            snackbar.dismiss();
        } else {
            snackbar.show();
        }
    }
}