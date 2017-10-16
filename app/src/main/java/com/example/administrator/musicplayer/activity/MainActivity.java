package com.example.administrator.musicplayer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.adapter.ListPagerAdapter;
import com.example.administrator.musicplayer.domain.AlbumLab;
import com.example.administrator.musicplayer.domain.ArtistLab;
import com.example.administrator.musicplayer.domain.Song;
import com.example.administrator.musicplayer.domain.SongLab;
import com.example.administrator.musicplayer.fragment.AlbumFragment;
import com.example.administrator.musicplayer.fragment.ArtistFragment;
import com.example.administrator.musicplayer.fragment.GenreFragment;
import com.example.administrator.musicplayer.fragment.SongFragment;
import com.example.administrator.musicplayer.lib.Const;
import com.example.administrator.musicplayer.lib.GoLib;
import com.example.administrator.musicplayer.player.Controller.Controller;
import com.example.administrator.musicplayer.player.Controller.IController;
import com.example.administrator.musicplayer.player.Player;
import com.example.administrator.musicplayer.player.PlayerService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AlbumFragment.AlbumInteractionListener,
                    GenreFragment.GenreInteractionListener, SongFragment.SongInteractionListener, IController, View.OnClickListener{


    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager tabPager;
    private FloatingActionButton fab;
    private NavigationView navView;
    private ProgressBar mainProgressBar;

    private ImageView mainPlay;

    private Intent service;
    AlbumFragment albumFragment;
    ArtistFragment artistFragment;
    GenreFragment genreFragment;
    SongFragment songFragment;
    Player player;
    private TextView mainTitle;
    private TextView mainArtist;
    private ConstraintLayout mainConstraintLayout;
    private ImageView mainPrev;
    private ImageView mainNext;

    int currentPosition;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void init(){
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.basic_background));
        load();
        initView();
        setToolbar();
        setListener();
        setActionBarToggle();
        setTabLayout();
        setTabPager();
        Controller.getInstance().addObserver(this);
        player = Player.getInstance(this);
    }

    /**
     * 뷰 설정
     */
    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabPager = (ViewPager) findViewById(R.id.tabPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        navView = (NavigationView) findViewById(R.id.nav_view);
        mainPlay = (ImageView) findViewById(R.id.mainPlay);
        mainProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        mainTitle = (TextView) findViewById(R.id.mainTitle);
        mainArtist = (TextView) findViewById(R.id.mainArtist);
        mainConstraintLayout = (ConstraintLayout) findViewById(R.id.mainConstraintLayout);
        mainPrev = (ImageView) findViewById(R.id.mainPrev);
        mainNext = (ImageView) findViewById(R.id.mainNext);
    }

    /**
     * 툴바
     */
    private void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.toolbar_title));
    }

    /**
     * 리스너
     */
    private void setListener(){
        navView.setNavigationItemSelectedListener(this);
        mainPlay.setOnClickListener(this);
        mainNext.setOnClickListener(this);
        mainPrev.setOnClickListener(this);
        mainConstraintLayout.setOnClickListener(this);
    }

    /**
     * 액션바 토글버튼 설정
     */
    private void setActionBarToggle(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 탭바 설정
     */
    private void setTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_songs)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_album)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_artist)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.tab_genre)));
    }

    /**
     * 뷰페이저 설정
     */
    private void setTabPager(){
        List<Fragment> fragmentList = new ArrayList<>();
        albumFragment = new AlbumFragment();
        artistFragment = new ArtistFragment();
        genreFragment = new GenreFragment();
        songFragment = new SongFragment();
        fragmentList.add(songFragment);
        fragmentList.add(albumFragment);
        fragmentList.add(artistFragment);
        fragmentList.add(genreFragment);
        ListPagerAdapter pagerAdapter = new ListPagerAdapter(getSupportFragmentManager(), fragmentList);
        tabPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(tabPager));
        tabPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    /**
     * 음악 데이터 가져오기
     */
    private void load(){
        service = new Intent(this, PlayerService.class);
        SongLab.getInstance().load(this);
        AlbumLab.getInstance().load(this);
        ArtistLab.getInstance().load(this);
    }

    /**
     * back 버튼
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 메뉴, 옵션 메뉴
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 프래그먼트 통신
     */
    @Override
    public void onAlbumInteraction() {

    }

    @Override
    public void onSongInteraction() {

    }

    @Override
    public void onGenreInteraction(Uri uri) {

    }


    /**
     * 미디어 플레이어 세팅
     */
    private void setPlayer() {
        Song song = SongLab.getInstance().getSongList().get(currentPosition);
        service.setAction(Const.ACTION_SET);
        service.putExtra(Const.CURRENT_POS, currentPosition);
        service.putExtra(Const.CURRENT_TITLE, song.title);
        service.putExtra(Const.CURRENT_ARTIST, song.artist);
        startService(service);
    }

    private void startPlayer(){
        service.setAction(Const.ACTION_START);
        startService(service);
    }

    private void pausePlayer(){
        service.setAction(Const.ACTION_PAUSE);
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
                mainProgressBar.setProgress(curPos);
            }
        });
    }

    @Override
    public void setTime() {
        mainProgressBar.setMax(player.getDuration());
    }

    @Override
    public void setPlayButton() {
        mainPlay.setImageResource(R.drawable.play_button_p);
    }

    @Override
    public void setPauseButton() {
        mainPlay.setImageResource(R.drawable.pause);
    }

    @Override
    public void setTitle() {
        mainTitle.setText(Player.getInstance(this).getTitle());
        mainArtist.setText(Player.getInstance(this).getArtist());
    }

    @Override
    public void setArtist() {

    }

    /**
     * 클릭 콜백 함수
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainPlay:
                if(Player.getInstance(this).getStatus() == Const.STAT_PLAY){
                    pausePlayer();
                } else if(Player.getInstance(this).getStatus() == Const.STAT_PAUSE){
                    startPlayer();
                } else {
                    Toast.makeText(this, "뭘 재생하라는거야", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.mainNext:
                currentPosition = player.getCurrentPosition();
                if(currentPosition == -1){
                    Toast.makeText(this, "갈 곳이 없잖아", Toast.LENGTH_LONG).show();
                    return;
                }
                currentPosition += 1;
                setPlayer();
                startPlayer();
                break;
            case R.id.mainPrev:
                currentPosition = player.getCurrentPosition();
                if(currentPosition == -1){
                    Toast.makeText(this, "갈 곳이 없잖아", Toast.LENGTH_LONG).show();
                    return;
                }
                if(currentPosition >= 0){
                    currentPosition -= 1;
                    setPlayer();
                    startPlayer();
                }
                break;
            case R.id.mainConstraintLayout:
                GoLib.getInstance().goPlayerActivity(this, Player.getInstance(this).getCurrentPosition());
                break;
        }
    }


}
