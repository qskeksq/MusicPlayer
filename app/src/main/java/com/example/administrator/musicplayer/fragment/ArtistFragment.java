package com.example.administrator.musicplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.page.ArtistAdapter;
import com.example.administrator.musicplayer.domain.Artist;
import com.example.administrator.musicplayer.domain.ArtistLab;

import java.util.ArrayList;
import java.util.List;

public class ArtistFragment extends Fragment {

    private RecyclerView artistRecycler;
    private View view;
    private ArtistAdapter adapter;
    private List<Artist> artistList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist, container, false);
        initView();
        setArtistRecycler();
        return view;
    }

    private void initView() {
        artistRecycler = (RecyclerView) view.findViewById(R.id.artistRecycler);
    }

    private void setArtistRecycler(){
        artistList = ArtistLab.getInstance().getArtistList();
        adapter = new ArtistAdapter();
        adapter.setData(artistList);
        artistRecycler.setAdapter(adapter);
        artistRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public interface AlbumInteractionListener {
        // TODO: Update argument type and name
        void onAlbumInteraction();
    }
}
