package com.example.administrator.musicplayer.adapter.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.domain.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-10-11.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {

    List<Album> albumList = new ArrayList<>();
    Context context;

    public void setData(List<Album> albumList){
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.title.setText(albumList.get(position).albumName);
        holder.artist.setText(albumList.get(position).albumArtist);
        Glide.with(context).load(albumList.get(position).albumUri).placeholder(R.mipmap.ic_launcher_round).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView cover;
        TextView title;
        TextView artist;

        public Holder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.albumItemImage);
            title = (TextView) itemView.findViewById(R.id.albumItemTitle);
            artist = (TextView) itemView.findViewById(R.id.albumItemArtist);
        }
    }

}
