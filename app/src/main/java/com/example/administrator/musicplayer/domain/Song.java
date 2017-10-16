package com.example.administrator.musicplayer.domain;

import android.net.Uri;

/**
 * Created by Administrator on 2017-10-11.
 */

public class Song {

    public String id;      // 음악 id
    public String albumId; // 앨범 id
    public String artist;   // 아티스트
    public String title;    // 제목
    public String genre;    // 장르

    public Uri musicUri;    // 음악 주소
    public Uri albumUri;    // 앨범자켓 주소

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Song){
            return id.equals(((Song)obj).id);
        }
        return super.equals(obj);
    }



}
