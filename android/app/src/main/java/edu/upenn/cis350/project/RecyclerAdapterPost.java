package edu.upenn.cis350.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapterPost
        extends RecyclerView.Adapter<RecyclerAdapterPost.PostHolder> {

    private List<PostData> posts;
    private static ClickListener clickListener;

    public static class PostHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView text;
        TextView user;
        public PostHolder(View v) {

            super(v);
            image = (ImageView) v.findViewById(R.id.viewImagePost);
            text = (TextView) v.findViewById(R.id.viewTextPost);
            user = (TextView) v.findViewById(R.id.viewUserPost);
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

    public RecyclerAdapterPost(List<PostData> posts) {
        this.posts = posts;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_card_post, parent, false);

        PostHolder holder = new PostHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {

        holder.text.setText(posts.get(position).getText());
        String tempPic = posts.get(position).getImage();
        holder.user.setText(posts.get(position).getUser());
        if(tempPic ==null){
            return;
        }
        if(tempPic.equals("")){
            return;
        }
        holder.image.setImageBitmap(StringToBitMap(tempPic));

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public interface ClickListener {
        public void onItemClick(int position, View v);
    }

  public Bitmap StringToBitMap(String encodedString){
    try {
        byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
      } catch(Exception e) {
        e.getMessage();
        return null;
      }
    }

}
