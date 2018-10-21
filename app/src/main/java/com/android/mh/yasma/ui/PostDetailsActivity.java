package com.android.mh.yasma.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.PostComments;
import com.android.mh.yasma.model.PostDetails;
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.model.UserDetail;
import com.android.mh.yasma.ui.adapter.CommentsAdapter;
import com.android.mh.yasma.ui.viewmodel.PostDetailsViewModel;
import com.android.mh.yasma.utils.Utils;

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

        postId = intent.getIntExtra(Utils.POST_ID, 0);
        userDetail = (UserDetail) intent.getSerializableExtra(Utils.USER_DETAILS);

        TextView txtUser = findViewById(R.id.textUser);
        txtUser.setText(getString(R.string.user, userDetail.getName()));

        initRecyclerView();

        PostDetailsViewModel postDetailsViewModel = ViewModelProviders.of(this).get(PostDetailsViewModel.class);
        showProgressDialog(userDetail.getName(), getString(R.string.loding));
        if (Utils.isNetworkAvailable(this)) {

            postDetailsViewModel.getPostDetails(String.valueOf(postId)).observe(this, new Observer<Resource<PostDetails>>() {
                @Override
                public void onChanged(@Nullable Resource<PostDetails> postDetails) {
                    hideProgressDialog();
                    if (Resource.Status.SUCCESS == postDetails.status) {
                        TextView txtTitle = findViewById(R.id.txtPostTitle);
                        txtTitle.setText(getString(R.string.post_title, postDetails.data.getTitle()));

                        TextView txtBody = findViewById(R.id.txtPostBody);
                        txtBody.setText(postDetails.data.getBody());
                    } else if (Resource.Status.NO_INTERNET == postDetails.status) {
                        Toast.makeText(PostDetailsActivity.this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PostDetailsActivity.this, getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            postDetailsViewModel.getPostComments(String.valueOf(postId)).observe(this, new Observer<Resource<List<PostComments>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<PostComments>> postComments) {
                    if (Resource.Status.SUCCESS == postComments.status) {
                        mRecyclerView.setAdapter(new CommentsAdapter(postComments.data, PostDetailsActivity.this));
                    } else if (Resource.Status.NO_INTERNET == postComments.status) {
                        Toast.makeText(PostDetailsActivity.this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PostDetailsActivity.this, getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private RecyclerView mRecyclerView;

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
