package com.example.administrator.musicplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.page.AlbumAdapter;
import com.example.administrator.musicplayer.domain.Album;
import com.example.administrator.musicplayer.domain.AlbumLab;

import java.util.ArrayList;
import java.util.List;


public class AlbumFragment extends Fragment {

    private AlbumInteractionListener mListener;
    private RecyclerView albumRecycler;
    private View view;
    private AlbumAdapter adapter;
    private List<Album> albumList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        initView();
        setAlbumRecycler();
        return view;
    }

    private void initView() {
        albumRecycler = (RecyclerView) view.findViewById(R.id.albumRecycler);
    }

    private void setAlbumRecycler(){
        albumList = AlbumLab.getInstance().getAlbumList();
        adapter = new AlbumAdapter();
        adapter.setData(albumList);
        albumRecycler.setAdapter(adapter);
        albumRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }


    public interface AlbumInteractionListener {
        // TODO: Update argument type and name
        void onAlbumInteraction();
    }
}
