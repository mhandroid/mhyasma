package com.android.mh.yasma.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.Post;
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.ui.HomeActivity;
import com.android.mh.yasma.ui.PostDetailsActivity;
import com.android.mh.yasma.ui.adapter.PostsAdapter;
import com.android.mh.yasma.ui.viewmodel.HomeViewModel;
import com.android.mh.yasma.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display list of post
 * Created by @author Mubarak Hussain.
 */
public class PostFragment extends Fragment implements PostsAdapter.PostItemCLickListener {
    private RecyclerView recyclerView;
    private List<Post> list = new ArrayList<>();

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false
        );
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final PostsAdapter postsAdapter = new PostsAdapter(list, getActivity(), PostFragment.this);
        recyclerView.setAdapter(postsAdapter);

        if (Utils.isNetworkAvailable(getActivity())) {
            homeViewModel.getListOfPost().observe(this, new Observer<Resource<Post>>() {
                @Override
                public void onChanged(@Nullable Resource<Post> posts) {
                    ((HomeActivity) getActivity()).hideProgress();
                    Log.d("MUB", posts + "");
                    if (Resource.Status.SUCCESS == posts.status) {
                        list.add(posts.data);
                        postsAdapter.notifyDataSetChanged();
                    } else if (Resource.Status.NO_INTERNET == posts.status) {
                        Toast.makeText(getActivity(), getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Post post) {
        if (Utils.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), PostDetailsActivity.class);
            intent.putExtra(Utils.POST_ID, post.getId());
            intent.putExtra(Utils.USER_DETAILS, post.getUserDetail());
            startActivity(intent);
        } else
            Toast.makeText(getActivity(), getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
    }
}
