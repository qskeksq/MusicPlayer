package com.example.administrator.musicplayer.player.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-10-12.
 */

public class Controller {

    private static Controller sController;
    private boolean runFlag = true;

    List<IController> clients = new ArrayList<>();

    public Controller() {

    }

    public static Controller getInstance(){
        if(sController == null){
            sController = new Controller();
        }
        return sController;
    }

    public void addObserver(IController controller){
        clients.add(controller);
    }

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

    public void setTime(){
        for(IController client : clients){
            client.setTime();
        }
    }

    public void stopProgress(){
        runFlag = false;
    }

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

    public void setTitle(){
        for(IController client : clients){
            client.setTitle();
        }
    }

    public void setArtist(){

    }

}
