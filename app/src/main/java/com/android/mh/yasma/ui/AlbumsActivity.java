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
import com.android.mh.yasma.model.Album;
import com.android.mh.yasma.ui.adapter.AlbumsAdapter;
import com.android.mh.yasma.ui.viewmodel.AlbumsViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author Mubarak Hussain.
 */
public class AlbumsActivity extends BaseActivity implements AlbumsAdapter.AlbumClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayout(R.layout.activity_home);
        setHomeButtonEnabled(true);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final List<Album> albumsList = new ArrayList<>();
        final AlbumsAdapter albumsAdapter = new AlbumsAdapter(this, albumsList, this);
        recyclerView.setAdapter(albumsAdapter);

        AlbumsViewModel albumsViewModel = ViewModelProviders.of(this).get(AlbumsViewModel.class);
        albumsViewModel.getListAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(@Nullable List<Album> albums) {
                Log.d("MUB", "albums list onChanged" + albums);
                if (albums != null) {
                    albumsList.addAll(albums);
                    albumsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(Album album) {
        Intent intent = new Intent(this, AlbumsDetailActivity.class);
        intent.putExtra("ALBUM_ID", String.valueOf(album.getId()));
        intent.putExtra("ALBUM_TITLE",album.getTitle());
        startActivity(intent);
    }
}
