package com.usermanage.view.ui.listfriend;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usermanage.R;
import com.google.android.material.snackbar.Snackbar;
import com.usermanage.CloseKeyboardClickOutside;
import com.usermanage.base.BaseFragment;
import com.usermanage.dao.userList.UserList;

import java.util.List;

public class ListFriendFragment extends BaseFragment {

    private AdapterListFriend mAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private ListFriendFragmentViewModel mViewModel;
    private View mView;
    private Snackbar mSnackbar;
    private EditText mETSearch;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_friend, container, false);
        initUi(root);
        initData();
        return root;
    }

    private void initData() {
        mViewModel.getDBOnline();
        mViewModel.showProgressBar.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

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
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initUi(View root) {
        mViewModel = new ViewModelProvider(getActivity()).get(ListFriendFragmentViewModel.class);
        recyclerView = root.findViewById(R.id.rv_listfriend);
        toolbar = root.findViewById(R.id.toolbar);
        mView = root.findViewById(R.id.layoutMain);
        mView.setOnClickListener(new CloseKeyboardClickOutside(getActivity()));
        progressBar = root.findViewById(R.id.progress_bar);
        mETSearch = root.findViewById(R.id.et_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setActionBar(toolbar);
        getActivity().getActionBar().setTitle("Danh sách bạn bè");
        mViewModel.contextMutableLiveData.setValue(getContext());
        mViewModel.activityMutableLiveData.setValue(getActivity());
        new CloseKeyboardClickOutside(getActivity());
    }

    @Override
    protected void onInternetStatusChange(boolean b) {
        mSnackbar = Snackbar.make(mView, "Mất kết nối Internet", Snackbar.LENGTH_LONG);
        if (b) {
            mSnackbar.dismiss();
        } else {
            mSnackbar.show();
        }
    }
}