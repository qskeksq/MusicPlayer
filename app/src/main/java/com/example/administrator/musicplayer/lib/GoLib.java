package com.example.administrator.musicplayer.lib;

import android.content.Context;
import android.content.Intent;

import com.example.administrator.musicplayer.player.PlayActivity;

/**
 * Created by Administrator on 2017-10-11.
 */

public class GoLib {

    private static GoLib sGoLib;

    private GoLib() {

    }

    public static GoLib getInstance(){
        if(sGoLib == null){
            sGoLib = new GoLib();
        }
        return sGoLib;
    }

    public void goPlayerActivity(Context context,int position){
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(Const.PLAY_POSITION_KEY, position);
        context.startActivity(intent);
    }

}
