package com.android.mh.yasma.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.PostComments;

import java.util.List;

/**
 * Created by @author Mubarak Hussain.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {

    private List<PostComments> mList;
    private Context mContext;

    public CommentsAdapter(List<PostComments> list, Context context) {
        mList = list;
        mContext = context;
    }


    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_item_layout, viewGroup, false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder commenViewHolder, int i) {
        final PostComments comments = mList.get(i);
        if (comments != null) {
            commenViewHolder.txtName.setText(comments.getName());
            commenViewHolder.txtBody.setText(comments.getBody());
            commenViewHolder.txtEmail.setText(comments.getEmail());

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        final TextView txtName;
        final TextView txtBody;
        final TextView txtEmail;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtemail);
            txtBody = itemView.findViewById(R.id.txtbody);
        }
    }

}
