package com.usermanage.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class BaseFragment extends Fragment {
    private BroadcastReceiver mInternetBroadcastReceiver;
    private IntentFilter mInternetIntentFilter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInternetIntentFilter = new IntentFilter();
        mInternetIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mInternetBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onInternetStatusChange(isNetworkAvailable(context));
            }
        };
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected abstract void onInternetStatusChange(boolean b);

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mInternetBroadcastReceiver, mInternetIntentFilter);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mInternetBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mInternetBroadcastReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mInternetBroadcastReceiver != null) {
            mInternetBroadcastReceiver = null;
        }
    }
}
