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
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.ui.adapter.AlbumsAdapter;
import com.android.mh.yasma.ui.viewmodel.AlbumsViewModel;
import com.android.mh.yasma.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @author Mubarak Hussain.
 */
public class AlbumsActivity extends BaseActivity implements AlbumsAdapter.AlbumClickListener {
    private final String TAG = AlbumsActivity.class.getSimpleName();
    private AlbumsAdapter albumsAdapter;
    private AlbumsViewModel albumsViewModel;
    final List<Album> albumsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayout(R.layout.activity_home);
        setHomeButtonEnabled(true);
        setToolbarTitle(getString(R.string.album));
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        albumsAdapter = new AlbumsAdapter(this, albumsList, this);
        recyclerView.setAdapter(albumsAdapter);

        albumsViewModel = ViewModelProviders.of(this).get(AlbumsViewModel.class);
        showProgressDialog(getString(R.string.album), getString(R.string.loding));
        if (Utils.isNetworkAvailable(this)) {
            loadAlbums();
        } else {
            Utils.showToastMsg(AlbumsActivity.this, getString(R.string.no_iternet));
        }
    }

    private void loadAlbums() {
        albumsViewModel.getListAlbums().observe(this, new Observer<Resource<List<Album>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Album>> albums) {
                Log.d(TAG, "albums list onChanged" + albums);
                hideProgressDialog();
                if (Resource.Status.SUCCESS == albums.status && albums.data != null) {
                    albumsList.clear();
                    albumsList.addAll(albums.data);
                    albumsAdapter.notifyDataSetChanged();
                } else if (Resource.Status.NO_INTERNET == albums.status) {
                    Utils.showToastMsg(AlbumsActivity.this, getString(R.string.no_iternet));
                } else {
                    Utils.showToastMsg(AlbumsActivity.this, getString(R.string.went_wrong));
                }
            }
        });
    }

    @Override
    public void onItemClick(Album album) {
        Intent intent = new Intent(this, AlbumsDetailActivity.class);
        intent.putExtra(Utils.ALBUM_ID, String.valueOf(album.getId()));
        intent.putExtra(Utils.ALBUM_TITLE, album.getTitle());
        startActivity(intent);
    }
}
