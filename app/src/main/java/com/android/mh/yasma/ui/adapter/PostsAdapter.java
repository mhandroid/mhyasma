package com.android.mh.yasma.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.Post;

import java.util.List;

/**
 * Created by @author Mubarak Hussain.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> mList;
    private Context mContext;
    private PostItemCLickListener mPostItemCLickListener;

    public PostsAdapter(List<Post> list, Context context, PostItemCLickListener postItemCLickListener) {
        mList = list;
        mContext = context;
        this.mPostItemCLickListener = postItemCLickListener;
    }


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item_layout, viewGroup, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        final Post post = mList.get(i);
        if (post != null) {
            postViewHolder.txtTitle.setText(post.getTitle());
            postViewHolder.txtBody.setText(post.getBody());
            if (post.getUserDetail() != null) {
                postViewHolder.txtUser.setText(post.getUserDetail().getName());
            } else {
                postViewHolder.txtUser.setText("Loading...");
            }
            postViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPostItemCLickListener != null)
                        mPostItemCLickListener.onItemClick(post);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        final TextView txtTitle;
        final TextView txtBody;
        final TextView txtUser;
        final View view;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            txtBody = itemView.findViewById(R.id.txtbody);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtUser = itemView.findViewById(R.id.txtUser);
        }
    }

    public interface PostItemCLickListener {
        void onItemClick(Post post);
    }
}
