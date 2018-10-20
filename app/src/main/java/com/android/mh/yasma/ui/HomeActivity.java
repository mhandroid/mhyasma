package com.android.mh.yasma.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.ui.adapter.PostsAdapter;
import com.android.mh.yasma.ui.viewmodel.HomeViewModel;
import com.android.mh.yasma.utils.Utils;

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

        showProgressDialog(getString(R.string.app_name), getString(R.string.loding));

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        if (Utils.isNetworkAvailable(this)) {
            homeViewModel.getListOfPost().observe(this, new Observer<Resource<List<Post>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<Post>> posts) {
                    hideProgressDialog();
                    Log.d("MUB", posts + "");
                    if (Resource.Status.SUCCESS == posts.status) {
                        recyclerView.setAdapter(new PostsAdapter(posts.data, HomeActivity.this, HomeActivity.this));
                    } else if (Resource.Status.NO_INTERNET == posts.status) {
                        Toast.makeText(HomeActivity.this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Toast.makeText(this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
            finish();
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("MUB",  " onScrollStateChanged() ");
                if(RecyclerView.SCROLL_STATE_IDLE==newState){
                    LinearLayoutManager layoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if(layoutManager1!=null)
                    Log.d("MUB", "first:"+ layoutManager1.findFirstVisibleItemPosition() +"last: " +layoutManager1.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("MUB",  " onScrolled() ");
            }
        });


    }

    @Override
    public void onItemClick(Post post) {
        Intent intent = new Intent(this, PostDetailsActivity.class);
        intent.putExtra("POST_ID", post.getId());
        intent.putExtra("USER_DETAILS", post.getUserDetail());
        startActivity(intent);
    }
}
