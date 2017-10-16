package com.example.administrator.musicplayer.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-10-11.
 */

public class AlbumLab {

    public static AlbumLab lab;
    private List<Album> albumList;

    private AlbumLab(){

    }

    public static AlbumLab getInstance(){
        if(lab == null){
            lab = new AlbumLab();
        }
        return lab;
    }

    public void load(Context context) {
//        setAlbumArt(context);

        List<Album> albumList = new ArrayList<>();

        // 1. 데이터베이스 열어주기
        ContentResolver resolver = context.getContentResolver();

        // 2. 테이블 가져오기
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        // 3. 칼럼명 정하기
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ARTIST };

        // 4. 쿼리
        Cursor cursor = resolver.query(uri, projection, null, null, projection[0] + " ASC");
        while (cursor.moveToNext()) {
            Album album = new Album();
            album.albumName = getValue(cursor, projection[0]);
            album.songCount = getValue(cursor, projection[2]);
            album.albumUri = getValue(cursor, projection[3]);
            album.albumArtist = getValue(cursor, projection[4]);
            albumList.add(album);
        }
        cursor.close();
        this.albumList = albumList;
    }

    private String getValue(Cursor cursor, String projection){
        return cursor.getString(cursor.getColumnIndex(projection));
    }


    private Uri makeMusicUri(String musicId){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri musicUri = Uri.withAppendedPath(uri, musicId);
        return musicUri;
    }

    private Uri makeAlbumUri(String albumId){
//        Uri uri = MediaStore.Audio.Albums.ALBUM_ART
        String contentUri = "content://media/external/audio/albumart/";
        Log.e("앨범아트", contentUri+albumId);
        return Uri.parse(contentUri + albumId);
    }

    public List<Album> getAlbumList(){
        return albumList;
    }


//    // 앨범아트 데이터만 따로 저장
//    private HashMap<Integer, String> albumMap = new HashMap<>(); //앨범아이디와 썸네일 경로 저장
//    private void setAlbumArt(Context context) {
//
//        String[] Album_cursorColumns = new String[]{MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums._ID};
//
//        Cursor Album_cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, Album_cursorColumns, null, null, null);
//        if (Album_cursor != null) { //커서가 널값이 아니면
//            if (Album_cursor.moveToFirst()) { //처음참조
//                int albumArt = Album_cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
//                int albumId = Album_cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
//                do {
//                    if (!albumMap.containsKey(Integer.parseInt(Album_cursor.getString(albumId)))) { //맵에 앨범아이디가 없으면
//                        albumMap.put(Integer.parseInt(Album_cursor.getString(albumId)), Album_cursor.getString(albumArt)); //집어넣는다
//                        Log.e("확인", Album_cursor.getString(albumArt));
//                    }
//                } while (Album_cursor.moveToNext());
//            }
//        }
//        Album_cursor.close();
//    }

}
