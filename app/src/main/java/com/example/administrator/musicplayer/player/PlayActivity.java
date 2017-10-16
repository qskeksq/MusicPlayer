package com.example.administrator.musicplayer.player;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.PlayPagerAdapter;
import com.example.administrator.musicplayer.domain.Song;
import com.example.administrator.musicplayer.domain.SongLab;
import com.example.administrator.musicplayer.lib.Const;
import com.example.administrator.musicplayer.lib.NumberLib;
import com.example.administrator.musicplayer.player.Controller.Controller;
import com.example.administrator.musicplayer.player.Controller.IController;

import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, IController {

    int currentPosition;

    private ViewPager viewPager;
    private ImageView play, next, prev;
    private TextView time, curTime;
    private TextView songTitle;
    private TextView songArtist;

    private List<Song> songList;
    private Song song;
    private ConstraintLayout playLayout;
    private ProgressBar progressBar;

    private Intent service;
    private ProgressBar playerProgressBar;
    Player player = Player.getInstance(PlayActivity.this);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        getWindow().setStatusBarColor(getResources().getColor(R.color.basic_background));

        // 음원 번호, 전체 음원
        initData();
        // 뷰 설정
        initView();
        // 리스너
        setListener();
        // 페이저 설정, currentPosition 설정, 화면값 설정
        initSongPager();
        // 기타 미디어 값 설정
        setMedia();
        Controller.getInstance().addObserver(this);
    }

    /**
     * 1.재생할 음악 번호
     * 2.전체 목록
     */
    private void initData() {
        // 재생할 음악 번호 받아오기
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra(Const.PLAY_POSITION_KEY, 0);

        // 전체 음악 데이터
        songList = SongLab.getInstance().getSongList();

        // 보낼 인텐트
        service = new Intent(this, PlayerService.class);
    }

    /**
     * 뷰 초기화
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
        prev = (ImageView) findViewById(R.id.prev);
        time = (TextView) findViewById(R.id.time);
        curTime = (TextView) findViewById(R.id.curTime);
        progressBar = (ProgressBar) findViewById(R.id.playerProgressBar);
        playLayout = (ConstraintLayout) findViewById(R.id.play_layout);
        songTitle = (TextView) findViewById(R.id.songTitle);
        songArtist = (TextView) findViewById(R.id.songArtist);
        playerProgressBar = (ProgressBar) findViewById(R.id.playerProgressBar);
    }

    /**
     * 리스너 설정
     */
    private void setListener() {
        play.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
    }

    /**
     * 미디어 설정
     */
    private void setMedia() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    /**
     * 뷰페이저 설정
     */
    private void initSongPager() {
        PlayPagerAdapter adapter = new PlayPagerAdapter(this, songList);
        viewPager.setAdapter(adapter);
        if(Player.getInstance(this).getStatus() == Const.STAT_PAUSE){
            viewPager.setCurrentItem(currentPosition);
            setCurrentSong();
            setPlayerWindow();
            setPlayButton();
            setTime();
        } else if(Player.getInstance(this).isPlaying()){
            // 여기서는 음원이 이미 설정되어있기 때문에 아래와 같이 음원을 설정하지 않아도 된다
            viewPager.setCurrentItem(currentPosition);
            // 현재 곡 설정
            setCurrentSong();
            // 제목 설정
            setPlayerWindow();
            // 버튼 설정
            setPauseButton();
            // 시간 설정
            setTime();
        } else if (currentPosition > -1) {
            viewPager.setCurrentItem(currentPosition);
            // 처음 호출될 때 onPageSelected 가 호출되지 않기 때문에 한번 호출해준다
            setCurrentSong();
            // 제목 설정
            setPlayerWindow();
            // 음원 설정
            setPlayer();
            // 재생
            startPlayer();
        }
        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    /**
     * 뷰페이저 리스너
     */
    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 현재 position 과 Song 객체 설정
            currentPosition = position;
            setCurrentSong();
            // 현재 페이지 화면 설정
            setPlayerWindow();
            // 페이지를 넘길 때마다 음원 세팅
            setPlayer();
            startPlayer();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 화면 설정
     */
    private void setPlayerWindow() {
        songTitle.setText(song.title);
        songArtist.setText(song.artist);
    }

    /**
     * 현재 곡 설정
     */
    private void setCurrentSong() {
        song = songList.get(currentPosition);
    }

    /**
     * 클릭 콜백 함수
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                if (Player.getInstance(this).getStatus() == Const.STAT_PLAY) {
                    pausePlayer();
                } else if (Player.getInstance(this).getStatus() == Const.STAT_PAUSE) {
                    startPlayer();
                }
                break;
            case R.id.next:
                if(currentPosition <= songList.size()){
                    currentPosition += 1;
                    viewPager.setCurrentItem(currentPosition);
                }
                break;
            case R.id.prev:
                if(currentPosition >= 0){
                    currentPosition -= 1;
                    viewPager.setCurrentItem(currentPosition);
                }
                break;
        }
    }

    /**
     * 미디어 플레이어 세팅
     */
    private void setPlayer() {
        service.setAction(Const.ACTION_SET);
        service.putExtra(Const.CURRENT_POS, currentPosition);
        service.putExtra(Const.CURRENT_TITLE, song.title);
        service.putExtra(Const.CURRENT_ARTIST, song.artist);
        startService(service);
    }

    /**
     * 미디어 플레이어 재생
     */
    private void startPlayer() {
        service.setAction(Const.ACTION_START);
        startService(service);
    }

    /**
     * 미디어 플레이어 일시정지
     */
    private void pausePlayer() {
        service.setAction(Const.ACTION_PAUSE);
        startService(service);
    }

    /**
     * 미디어 플레이어 멈춤
     */
    private void stopPlayer() {
        service.setAction(Const.ACTION_STOP);
        startService(service);
    }

    /**
     * 옵저버 패턴 구현
     */
    @Override
    public void setProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int curPos = player.getCurPosition();
                curTime.setText(NumberLib.getInstance().miliToSec(curPos)+"");
                progressBar.setProgress(curPos);
            }
        });
    }

    @Override
    public void setTime() {
        time.setText(NumberLib.getInstance().miliToSec(Player.getInstance(PlayActivity.this).getDuration()));
        progressBar.setMax(Player.getInstance(PlayActivity.this).getDuration());
    }

    @Override
    public void setPlayButton() {
        play.setImageResource(R.drawable.play_rounded_button);
    }

    @Override
    public void setPauseButton() {
        play.setImageResource(R.drawable.round_pause_button);
    }

    @Override
    public void setTitle() {

    }

    @Override
    public void setArtist() {

    }

}


