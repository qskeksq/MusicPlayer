package com.example.administrator.musicplayer.lib;

/**
 * Created by Administrator on 2017-10-11.
 */

public class NumberLib {

    private static NumberLib sNumberLib;

    private NumberLib() {

    }

    public static NumberLib getInstance(){
        if(sNumberLib == null){
            sNumberLib = new NumberLib();
        }
        return sNumberLib;
    }

    public String miliToSec(int mili) {
        int sec = mili / 1000;
        int min = sec / 60;
        sec = sec % 60;
        return String.format("%02d", min) + ":" + String.format("%02d", sec);
    }

}
