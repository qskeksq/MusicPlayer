package com.example.administrator.musicplayer.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017-10-11.
 */

public abstract class BaseActivity extends AppCompatActivity {

    // 체크할 퍼미션
    private static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    public static int REQ_CODE = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkVersion();

    }

    abstract void init();

    private void checkVersion(){
        // 버전이 마시멜로 미만인 경우는 패스
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            init();
            // 이상인 경우는 일단 허용이 된 퍼미션이 무엇인지 체크한다.
        } else {
            checkAlreadyGrantedPermission();
        }
    }

    /**
     * 이미 체크된 퍼미션이 있는지 확인하고, 체크되지 않았다면 시스템에 onRequestPermission()으로 권한을 요청한다.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAlreadyGrantedPermission() {
        boolean isAllGranted = true;
        for(String perm : permissions){
            // 만약 원하는 퍼미션이 하나라도 허용이 안 되었다면 false로 전환
            if(checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                isAllGranted = false;
            }
        }
        // 만약 전부 허용이 되었다면 다음 액티비티로 넘어간다.
        if(isAllGranted){
            init();
            // 허용되지 않는 것이 있다면 시스템에 권한신청한다.
        } else {
            requestPermissions(permissions, REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onResult(grantResults);
    }

    /**
     * 시스템 권한체크가 끝난 후 호출
     */
    private void onResult(int[] grantResults){
        boolean isAllGranted = true;
        for(int granted : grantResults){
            if(granted != PackageManager.PERMISSION_GRANTED){
                isAllGranted = false;
            }
        }
        // 허용되면 init()으로 원하는 함수를 실행하고
        if(isAllGranted){
            init();
            // 허용되지 않는 것이 있다면 시스템에 권한신청한다.
        } else {
            finish();
        }
    }
}
