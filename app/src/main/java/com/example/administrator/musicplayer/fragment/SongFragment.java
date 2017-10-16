package com.example.administrator.musicplayer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.page.SongAdapter;
import com.example.administrator.musicplayer.domain.Song;
import com.example.administrator.musicplayer.domain.SongLab;

import java.util.List;

public class SongFragment extends Fragment {

    private SongInteractionListener mListener;
    private RecyclerView songRecycler;
    private View view;

    public static SongFragment newInstance() {
        SongFragment fragment = new SongFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_song, container, false);
        initView();
        setSongRecycler();
        return view;
    }

    private void initView() {
        songRecycler = (RecyclerView) view.findViewById(R.id.songRecycler);
    }

    private void setSongRecycler(){
        List<Song> songList = SongLab.getInstance().getSongList();
        SongAdapter adapter = new SongAdapter(songList, getContext());
        songRecycler.setAdapter(adapter);
        songRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SongInteractionListener) {
            mListener = (SongInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SongInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SongInteractionListener {
        // TODO: Update argument type and name
        void onSongInteraction();
    }

}
