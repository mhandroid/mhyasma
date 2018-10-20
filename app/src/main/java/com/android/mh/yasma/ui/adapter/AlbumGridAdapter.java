package com.android.mh.yasma.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.mh.yasma.R;
import com.android.mh.yasma.model.AlbumPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter to show photo in grid
 * Created by @author Mubarak Hussain.
 */
public class AlbumGridAdapter extends BaseAdapter {
    private List<AlbumPhoto> albumPhotos;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public AlbumGridAdapter(Context context, List<AlbumPhoto> list) {

        albumPhotos = list;
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return albumPhotos.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_grid_view, viewGroup, false);
            imageView = view.findViewById(R.id.gridImg);
        } else {
            imageView = (ImageView) view;
        }
        Picasso.with(mContext).load(albumPhotos.get(i).getThumbnailUrl()).into(imageView);
        return view;
    }
}
