package com.android.mh.yasma.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.PostComments;
import com.android.mh.yasma.model.PostDetails;
import com.android.mh.yasma.model.UserDetail;
import com.android.mh.yasma.ui.adapter.CommentsAdapter;
import com.android.mh.yasma.ui.viewmodel.PostDetailsViewModel;

import java.util.List;

/**
 * Created by @author Mubarak Hussain.
 */
public class PostDetailsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayout(R.layout.activity_post_detals);
        setHomeButtonEnabled(false);
        int postId = 0;
        UserDetail userDetail;
        Intent intent = getIntent();

        postId = intent.getIntExtra("POST_ID", 0);
        userDetail = (UserDetail) intent.getSerializableExtra("USER_DETAILS");

        TextView txtUser = findViewById(R.id.textUser);
        txtUser.setText(getString(R.string.user, userDetail.getName()));

        initRecyclerView();

        PostDetailsViewModel postDetailsViewModel = ViewModelProviders.of(this).get(PostDetailsViewModel.class);

        postDetailsViewModel.getPostDetails(String.valueOf(postId)).observe(this, new Observer<PostDetails>() {
            @Override
            public void onChanged(@Nullable PostDetails postDetails) {

                TextView txtTitle = findViewById(R.id.txtPostTitle);
                txtTitle.setText(getString(R.string.post_title, postDetails.getTitle()));

                TextView txtBody = findViewById(R.id.txtPostBody);
                txtBody.setText(postDetails.getBody());
            }
        });

        postDetailsViewModel.getPostComments(String.valueOf(postId)).observe(this, new Observer<List<PostComments>>() {
            @Override
            public void onChanged(@Nullable List<PostComments> postComments) {
                mRecyclerView.setAdapter(new CommentsAdapter(postComments, PostDetailsActivity.this));
            }
        });

    }

    private RecyclerView mRecyclerView;

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }
}
