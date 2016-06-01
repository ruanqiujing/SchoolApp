package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.User;

import cn.bmob.v3.BmobUser;

public class SplashActivity extends AppCompatActivity {

    private static final int CODE_ENTER_HOME = 1;
    private static final int CODE_ENTER_LOGIN = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                    break;
                case 2:
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //  SystemClock.sleep(2000);
        //确认用户是否登录过，如果登录过则不需要再次登录
        isLogin();

    }

    private void isLogin() {
        final Message message = handler.obtainMessage();
        final long start = System.currentTimeMillis();
        User user = BmobUser.getCurrentUser(this, User.class);
        //判断是否联网
        boolean net = isNetConnected();
        if (net) {
            if (user != null) {
                //如果登录过则直接跳到主界面
                message.what = CODE_ENTER_HOME;
            } else {
                //如果没有登录过，则跳到登录界面
                message.what = CODE_ENTER_LOGIN;
            }
        } else {
            Toast.makeText(this, "好像没有网噢~", Toast.LENGTH_SHORT).show();
            //即使没有网，也让用户进入到主界面
            message.what = CODE_ENTER_HOME;
        }
        new Thread(){
            @Override
            public void run() {
                long end = System.currentTimeMillis();
                long time = end - start;
                if (time<2000){
                    try {
                        Thread.sleep(2000-time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendMessage(message);
            }
        }.start();

    }

    /**
     * 判断是否联网
     *
     * @return
     */
    private boolean isNetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            for (NetworkInfo info : infos) {
                if (info.isConnected()) {
                    return true;
                }
            }

        }
        return false;
    }
}
