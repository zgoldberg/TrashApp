package edu.upenn.cis350.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapterUser
        extends RecyclerView.Adapter<RecyclerAdapterUser.UserHolder> {

    private List<UserData> users;
    private static ClickListener clickListener;

    public static class UserHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView username;
        TextView fullname;

        public UserHolder(View v) {

            super(v);
            image = (ImageView) v.findViewById(R.id.viewImageUser);
            username = (TextView) v.findViewById(R.id.viewNameUser);
            fullname = (TextView) v.findViewById(R.id.viewFullNameUser);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(getAdapterPosition(), v);

        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RecyclerAdapterUser(List<UserData> posts) {
        this.users = posts;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_card_user, parent, false);

        UserHolder holder = new UserHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {

        holder.username.setText(users.get(position).getUsername());
        holder.fullname.setText(users.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface ClickListener {
        public void onItemClick(int position, View v);
    }

}