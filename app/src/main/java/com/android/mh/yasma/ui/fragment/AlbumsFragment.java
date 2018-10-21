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

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.Album;
import com.android.mh.yasma.model.Resource;
import com.android.mh.yasma.ui.AlbumsDetailActivity;
import com.android.mh.yasma.ui.HomeActivity;
import com.android.mh.yasma.ui.adapter.AlbumsAdapter;
import com.android.mh.yasma.ui.viewmodel.AlbumsViewModel;
import com.android.mh.yasma.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to show list of Albums
 * Created by @author Mubarak Hussain.
 */
public class AlbumsFragment extends Fragment implements AlbumsAdapter.AlbumClickListener {
    private final String TAG = AlbumsFragment.class.getSimpleName();
    private AlbumsAdapter albumsAdapter;
    private AlbumsViewModel albumsViewModel;
    final List<Album> albumsList = new ArrayList<>();
    private RecyclerView recyclerView;

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        albumsAdapter = new AlbumsAdapter(getActivity(), albumsList, this);
        recyclerView.setAdapter(albumsAdapter);

        albumsViewModel = ViewModelProviders.of(this).get(AlbumsViewModel.class);
        if (Utils.isNetworkAvailable(getActivity())) {
            loadAlbums();
        } else {
            Utils.showToastMsg(getActivity(), getString(R.string.no_iternet));
        }
    }


    /**
     * Method to load albums
     */
    private void loadAlbums() {
        albumsViewModel.getListAlbums().observe(this, new Observer<Resource<List<Album>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Album>> albums) {
                Log.d(TAG, "albums list onChanged" + albums);
                ((HomeActivity) getActivity()).hideProgress();
                if (Resource.Status.SUCCESS == albums.status && albums.data != null) {
                    albumsList.clear();
                    albumsList.addAll(albums.data);
                    albumsAdapter.notifyDataSetChanged();
                } else if (Resource.Status.NO_INTERNET == albums.status) {
                    Utils.showToastMsg(getActivity(), getString(R.string.no_iternet));
                } else {
                    Utils.showToastMsg(getActivity(), getString(R.string.went_wrong));
                }
            }
        });
    }

    @Override
    public void onItemClick(Album album) {
        if (Utils.isNetworkAvailable(getActivity())) {
            Intent intent = new Intent(getActivity(), AlbumsDetailActivity.class);
            intent.putExtra(Utils.ALBUM_ID, String.valueOf(album.getId()));
            intent.putExtra(Utils.ALBUM_TITLE, album.getTitle());
            startActivity(intent);
        }else {
            Utils.showToastMsg(getActivity(), getString(R.string.no_iternet));
        }
    }
}
