package com.android.mh.yasma.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.ui.adapter.PostsAdapter;
import com.android.mh.yasma.ui.viewmodel.HomeViewModel;

import java.util.List;

/**
 * Created by @author Mubarak Hussain.
 */
public class HomeActivity extends BaseActivity implements PostsAdapter.PostItemCLickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayout(R.layout.activity_home);
        setHomeButtonEnabled(true);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getListOfPost().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {
                Log.d("MUB", posts.size() + "");

                recyclerView.setAdapter(new PostsAdapter(posts, HomeActivity.this, HomeActivity.this));
            }
        });
    }

    @Override
    public void onItemClick(Post post) {
        Intent intent = new Intent(this,PostDetailsActivity.class);
        intent.putExtra("POST_ID", post.getId());
        intent.putExtra("USER_DETAILS", post.getUserDetail());
        startActivity(intent);
    }
}
