package com.android.mh.yasma.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.AlbumPhoto;
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.ui.adapter.AlbumGridAdapter;
import com.android.mh.yasma.ui.viewmodel.AlbumsDataViewModel;
import com.android.mh.yasma.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to show album details
 * Created by @author Mubarak Hussain.
 */
public class AlbumsDetailActivity extends BaseActivity {

    private List<AlbumPhoto> mListAlbum = new ArrayList<>();
    private AlbumGridAdapter albumGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLayout(R.layout.activity_album_detals);

        setHomeButtonEnabled(false);

        Intent intent = getIntent();
        String id = intent.getStringExtra(Utils.ALBUM_ID);
        String title = intent.getStringExtra(Utils.ALBUM_TITLE);

        setToolbarTitle(title);
        final GridView gridView = findViewById(R.id.grid_view);
        TextView txtTitle = findViewById(R.id.textAlbumTitle);
        txtTitle.setText(title);

        showProgressDialog(getString(R.string.album_detail), getString(R.string.loding));

        albumGridAdapter = new AlbumGridAdapter(AlbumsDetailActivity.this, mListAlbum);
        gridView.setAdapter(albumGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlbumPhoto photo = mListAlbum.get(i);
                if (photo != null && photo.getUrl() != null) {
                    Intent intent = new Intent(AlbumsDetailActivity.this, FullScreenActivity.class);
                    intent.putExtra("IMAGE_URL", photo.getUrl());
                    startActivity(intent);
                }
            }
        });

        AlbumsDataViewModel albumsDetalViewModel = ViewModelProviders.of(this).get(AlbumsDataViewModel.class);

        if (Utils.isNetworkAvailable(this)) {

            albumsDetalViewModel.getAlbumsOfPhotos(id).observe(this, new Observer<Resource<List<AlbumPhoto>>>() {
                @Override
                public void onChanged(@Nullable Resource<List<AlbumPhoto>> listResource) {
                    if (Resource.Status.SUCCESS == listResource.status) {
                        hideProgressDialog();
                        mListAlbum.clear();
                        mListAlbum.addAll(listResource.data);
                        albumGridAdapter.notifyDataSetChanged();
                    } else if (Resource.Status.NO_INTERNET == listResource.status) {
                        Toast.makeText(AlbumsDetailActivity.this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AlbumsDetailActivity.this, getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            Toast.makeText(this, getString(R.string.no_iternet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
