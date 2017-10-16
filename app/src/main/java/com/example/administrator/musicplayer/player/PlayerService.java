package com.example.administrator.musicplayer.player;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.example.administrator.musicplayer.domain.Song;
import com.example.administrator.musicplayer.domain.SongLab;
import com.example.administrator.musicplayer.lib.Const;

import java.util.List;

/**
 * 음원 관련 스트리밍의 경우 서브스레드에서 할 경우 일정 시간이 지나면
 * 시스템에서 해제를 시켜버린다. 따라서 foreground 서비스에서 따로 관리를 해 준다.
 */
public class PlayerService extends Service {

    private Player player;
    private List<Song> songList;
    private int itemPosition;
    private String title, artist;

    public PlayerService() {

    }

    /**
     * 서비스는 생명주기가 먼저 있고 생성자에 값을 채워넣는 형식이므로 생명주기에서 자원을 초기화 해준다.
     *
     * 앱을 강제 종료할 경우 '서비스만' 다시 호출된다. 즉, 다른 것은 다 죽고, 서비스만 살아 있는 상황에서
     * player, songlist 를 초기화 할 경우 null 값으로 설정이 되어 있고, 다시 앱을 켰을 때는 이미 서비스가
     * 생성이 되어 있기 때문에 따로 onCreate()가 호출되지 않는다. 따라서 NullPointerException 이 생기는 것.
     * 서비스는 강제 종료될 경우에 대비해서 예외처리를 해 줘야 한다.
     */
    @Override
    public void onCreate() {
        super.onCreate();
//        player = Player.getInstance(getBaseContext());
//        songList = SongLab.getInstance().getSongList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 계속해서 호출하기 때문에 서비스를 호출할 때마다 호출되는 이곳에서 메소드를 관리해준다.
     * @param intent 액션과 데이터 값 넘어옴
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            switch (intent.getAction()) {
                case Const.ACTION_SET:
                    itemPosition = intent.getIntExtra(Const.CURRENT_POS, 0);
                    title = intent.getStringExtra(Const.CURRENT_TITLE);
                    artist = intent.getStringExtra(Const.CURRENT_ARTIST);
                    setPlayer(itemPosition);
                    break;
                case Const.ACTION_START:
                    startPlayer();
                    break;
                case Const.ACTION_PAUSE:
                    pausePlayer();
                    break;
                case Const.ACTION_STOP:
                    stopPlayer();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setPlayer(int position){
        if(player == null){
            Log.e("호출 확인", "==========player");
            player = Player.getInstance(getBaseContext());
        }
        if(songList == null){
            Log.e("호출 확인", "==========songList");
            songList = SongLab.getInstance().getSongList();

        }
        Uri musicUri = songList.get(position).musicUri;
        player.setPlayer(musicUri);
        player.setTitle(title);
        player.setArtist(artist);
        player.setCurrentPosition(itemPosition);
    }

    private void startPlayer(){
        player.start();
    }

    private void pausePlayer(){
        player.pause();
    }

    private void stopPlayer(){
        player.stop();
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return artist;
    }

}
