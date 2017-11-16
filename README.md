# MusicPlayer

- MediaStore - 음악, 앨범, 아티스트 분류
- MediaPlayer
- Observer Design Pattern
- Service, Foreground Service

![](https://github.com/qskeksq/MusicPlayer/blob/master/pict/20171116_144506_360x640.jpg)
![](https://github.com/qskeksq/MusicPlayer/blob/master/pict/20171116_144403_360x640.jpg)
![](https://github.com/qskeksq/MusicPlayer/blob/master/pict/20171116_144410_360x640.jpg)
![](https://github.com/qskeksq/MusicPlayer/blob/master/pict/20171116_144531_360x640.jpg)
![](https://github.com/qskeksq/MusicPlayer/blob/master/pict/20171116_151216_360x640.jpg)

## 플레이 과정

- 1. PlayActivity, SongList, Notification에서 재생, 다음, 일시정지
- 2. PlayService로 인텐트 전달
- 3. PlayService에서 MediaPlayer가 있는 Player 객체의 해당 메소드 호출
- 4. Player에서 Controller에 등록된 Observer호출, MediaPlayer 해당 메소드 호출
- 5. Observer로 등록된 PlayActivity, SongList, Notification에 각 동작 전달  

## MediaStore를 통해 음악 가져오기

#### 음악

```java
// 테이블 가져오기
Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
// 칼럼명 정하기
String[] projection = {
    MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID
    , MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST
};
```

#### 앨범

```java
 // 2. 테이블 가져오기
Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

// 3. 칼럼명 정하기
String[] projection = {
    MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums._ID,
    MediaStore.Audio.Albums.NUMBER_OF_SONGS, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ARTIST };
```

#### 아티스트

```java
// 테이블 가져오기
Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
// 칼럼명 정하기
String[] projection = {MediaStore.Audio.Artists.ARTIST, MediaStore.Audio.Artists.NUMBER_OF_ALBUMS };
```

#### ContentResolver 쿼리

```java
public void load(Context context) {
    List<Song> songList = new ArrayList<>();
    // 1. 데이터베이스 열어주기
    ContentResolver resolver = context.getContentResolver();
    // 2. 테이블 가져오기
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    // 3. 칼럼명 정하기
    String[] projection = {
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ALBUM_ID
            , MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST
    };
    // 4. 쿼리
    Cursor cursor = resolver.query(uri, projection, null, null, projection[2] + " ASC");
    while (cursor.moveToNext()) {
        Song song = new Song();
        song.id = getValue(cursor, projection[0]);
        song.albumId = getValue(cursor, projection[1]);
        song.title = getValue(cursor, projection[2]);
        song.artist = getValue(cursor, projection[3]);
        song.musicUri = makeMusicUri(song.id);
        song.albumUri = makeAlbumUri(song.albumId);
        songList.add(song);
    }
    cursor.close();
    this.songList = songList;
}
```

#### 앨범자켓 Uri

> method1

```java
 private Uri makeMusicUri(String musicId){
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Uri musicUri = Uri.withAppendedPath(uri, musicId);
    return musicUri;
}
```

> method2

```java
private Uri makeAlbumUri(String albumId){
    String contentUri = "content://media/external/audio/albumart/";
    return Uri.parse(contentUri + albumId);
}
```

## PlayerService

#### 1. 서비스 관리

- 계속해서 호출하기 때문에 서비스를 호출할 때마다 호출되는 이곳에서 메소드를 관리해준다.
- intent 액션과 데이터 값 넘어옴

```java
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
```

#### 2. Player의 해당 메소드 호출

```java
private void setPlayer(int position){
    if(player == null){
        player = Player.getInstance(getBaseContext());
    }
    if(songList == null){
        songList = SongLab.getInstance().getSongList();
    }
    Uri musicUri = songList.get(position).musicUri;
    player.setPlayer(musicUri);
    player.setTitle(title);
    player.setArtist(artist);
    player.setCurrentPosition(itemPosition);
}
```

## Player

- MediaPlayer 관리
- Controller에 등록된 Observer 호출

```java
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
```

## Observer

#### 1. 옵저버 인터페이스
```java
public interface IController {
    void setProgress();
    void setTime();
    void setPlayButton();
    void setPauseButton();
    void setTitle();
    void setArtist();
}
```

#### 2. 옵저버 등록

```java
public void addObserver(IController controller){
    clients.add(controller);
}
```

#### 3. 옵저버 호출 

```java
// 프로그래스바 등록된 옵저버 호출
public void setProgress(){
    runFlag = true;
    new Thread(){
        @Override
        public void run() {
            while(runFlag){
                for(IController client : clients){
                    client.setProgress();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }.start();
}

// 시간이 필요한 옵저버 호출
public void setTime(){
    for(IController client : clients){
        client.setTime();
    }
}

// 플레이, 일시정지 버튼 필요한 옵저버 호출
public void setPlayButton(){
    for(IController client : clients){
        client.setPlayButton();
    }
}

public void setPauseButton(){
    for(IController client : clients){
        client.setPauseButton();
    }
}

// 음악제목이 필요한 옵저버 호출
public void setTitle(){
    for(IController client : clients){
        client.setTitle();
    }
}
```