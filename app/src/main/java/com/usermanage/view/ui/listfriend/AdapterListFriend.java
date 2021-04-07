package com.usermanage.view.ui.listfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.usermanage.R;
import com.usermanage.dao.userList.UserList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListFriend extends RecyclerView.Adapter<AdapterListFriend.ViewHolder> implements Filterable {
    private List<UserList> mUserListList;
    private List<UserList> mUserListListFilter;
    private Context mContext;

    public AdapterListFriend(Context context, List<UserList> userModelList) {
        this.mUserListList = userModelList;
        this.mUserListListFilter = new ArrayList<>(mUserListList);
        this.mContext = context;
    }

    @NonNull
    @Override
    public AdapterListFriend.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new AdapterListFriend.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterListFriend.ViewHolder holder, int position) {
        UserList userModel = mUserListList.get(position);
        if (userModel.getAvatar() != null && !userModel.getAvatar().isEmpty()) {
            Glide.with(mContext).load(userModel.getAvatar()).into(holder.im_Avatar);
        } else {
            Glide.with(mContext).load(R.drawable.ic_launcher_foreground).into(holder.im_Avatar);
        }
        holder.tv_Name.setText(userModel.getName());
    }

    @Override
    public int getItemCount() {
        return mUserListList.size();
    }

    @Override
    public Filter getFilter() {
        return mfilter;
    }

    Filter mfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<UserList> userListList = new ArrayList<>();
            if (constraint.toString().isEmpty() || constraint.toString() == null) {
                userListList.addAll(mUserListListFilter);
            } else {
                for (UserList userList : mUserListListFilter) {
                    if (userList.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        userListList.add(userList);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = userListList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUserListList.clear();
            mUserListList.addAll((Collection<? extends UserList>) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView im_Avatar;
        public TextView tv_Name;

        public ViewHolder(View view) {
            super(view);
            tv_Name = view.findViewById(R.id.tv_username);
            im_Avatar = view.findViewById(R.id.iv_avatar);
        }
    }
}
