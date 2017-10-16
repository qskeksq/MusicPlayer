package com.example.administrator.musicplayer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.domain.Song;
import com.example.administrator.musicplayer.domain.SongLab;

import java.util.List;

/**
 * Created by Administrator on 2017-10-11.
 */

public class PlayPagerAdapter extends PagerAdapter {

    Context context;
    List<Song> songList;

    private TextView pagerItemName;
    private TextView pagerItemArtist;
    private ImageView pageItemImage;

    public PlayPagerAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        List<Song> songList = SongLab.getInstance().getSongList();
        Song song = songList.get(position);

        View view = LayoutInflater.from(context).inflate(R.layout.item_pager, container, false);
        pageItemImage = (ImageView) view.findViewById(R.id.pageItemImage);
        pageItemImage.setImageURI(song.albumUri);
        if(pageItemImage.getDrawable() == null){
            pageItemImage.setImageResource(R.mipmap.ic_launcher_round);
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
