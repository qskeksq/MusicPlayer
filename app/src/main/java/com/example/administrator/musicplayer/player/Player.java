package com.example.administrator.musicplayer.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.administrator.musicplayer.domain.Song;
import com.example.administrator.musicplayer.domain.SongLab;
import com.example.administrator.musicplayer.lib.Const;
import com.example.administrator.musicplayer.player.Controller.Controller;

import java.util.List;

/**
 * 음원 정보를 싱글턴으로 만들어 따로 관리해준다.
 */
public class Player {

    private MediaPlayer mediaPlayer;
    private Controller controller;
    private Context context;
    private String title, artist;
    private Song currentSong;
    private int currentPosition;
    private boolean loop = false;
    private int status = 0;

    private static List<Song> songList;
    private static Player sPlayer;

    public Player(Context context) {
        this.context = context;
        controller = Controller.getInstance();
    }

    public static Player getInstance(Context context){
        if(sPlayer == null){
            sPlayer = new Player(context);
        }
        if(songList == null){
            songList = SongLab.getInstance().getSongList();
        }
        return sPlayer;
    }

    /**
     * 각 음원마다 미디어 플레이어 설정해준다
     */
    public void setPlayer(Uri musicUri){
        // 기존 재생되고 있던 스트리밍 해제
        if(mediaPlayer != null){
            controller.stopProgress();
            // 스트리밍 해제
            mediaPlayer.release();
            // 참조 해제
            mediaPlayer = null;
        }
        mediaPlayer = MediaPlayer.create(context, musicUri);
        mediaPlayer.setLooping(loop);
    }

    /**
     * 재생 - 재생하기 전 반드시 set 해줘야 한다.
     */
    public void start(){
        if(mediaPlayer != null){
            mediaPlayer.start();
            controller.setPauseButton();
            controller.setTime();
            controller.setProgress();
            controller.setTitle();
            status = Const.STAT_PLAY;
        } else {
            Toast.makeText(context, "재생중인 곡이 없습니다", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 일시정지
     */
    public void pause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            controller.setPlayButton();
            status = Const.STAT_PAUSE;
        } else {
            Toast.makeText(context, "재생중인 곡이 없습니다", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 멈춤
     */
    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * 반복 설정
     */
    public void setLoop(){
        if(loop = false){
            loop = true;
        } else {
            loop = false;
        }
    }

    /**
     * 현재 재생 여부
     * @return
     */
    public boolean isPlaying(){
        if(mediaPlayer != null){
            return mediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public int getStatus(){
        return status;
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public int getCurPosition(){
        if(mediaPlayer != null){
            return mediaPlayer.getCurrentPosition();
        } else {
            return -1;
        }
    }

    /**
     * 서비스에서 제공받지 않고 바로 싱글턴 패턴으로 넘겨받는다
     * @param title
     */
    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    /**
     * 아티스트 정보
     * @param artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    /**
     * 현재 값 정보
     * @param currentPosition
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        if(songList != null){
            currentSong = songList.get(currentPosition);
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
