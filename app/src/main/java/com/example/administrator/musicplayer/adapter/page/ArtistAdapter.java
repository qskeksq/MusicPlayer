package com.example.administrator.musicplayer.adapter.page;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.domain.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-10-11.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Holder> {

    List<Artist> artistList = new ArrayList<>();

    public void setData(List<Artist> artistList){
        this.artistList = artistList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.artist.setText(artistList.get(position).artist);
        holder.count.setText("앨범 "+artistList.get(position).trackNo+"개");
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView artist;
        TextView count;

        public Holder(View itemView) {
            super(itemView);
            artist = (TextView) itemView.findViewById(R.id.artistItemTitle);
            count = (TextView) itemView.findViewById(R.id.artistItemCount);
        }
    }

}
