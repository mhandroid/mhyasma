package com.android.mh.yasma.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.Album;

import java.util.List;

/**
 * Adapter to create albums list
 * Created by @author Mubarak Hussain.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    private List<Album> albumsList;
    private LayoutInflater mLayoutInflater;
    private AlbumClickListener albumClickListener;

    public AlbumsAdapter(Context context, List<Album> list, AlbumClickListener albumClickListener) {
        albumsList = list;
        mLayoutInflater = LayoutInflater.from(context);
        this.albumClickListener = albumClickListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.album_item_layout, viewGroup, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder albumViewHolder, int i) {
        final Album album = albumsList.get(i);
        albumViewHolder.textViewTitle.setText(album.getTitle());

        albumViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (albumClickListener != null) {
                    albumClickListener.onItemClick(album);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView textViewTitle;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.txtTitle);
            view = itemView;
        }
    }


    /**
     * Interface to handle albums list item click
     */
    public interface AlbumClickListener {
        void onItemClick(Album album);
    }
}
