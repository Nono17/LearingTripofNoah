package com.example.zhangjack.wanzhongyixin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import static java.lang.Boolean.getBoolean;

public class WelcomeActivity extends AppCompatActivity {

    private boolean isFirstIn = false;
    private static final int TIME = 2000;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;

    private Handler welcomeHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            goGuide();//调试结束后去掉
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏显示且去掉虚拟按钮
        hideBottomUIMenu();

        setContentView(R.layout.activity_welcome);
        init();
    }

    private void init(){
        SharedPreferences perPreferences = getSharedPreferences("startMode",MODE_PRIVATE);
        isFirstIn = perPreferences.getBoolean("isFirstIn",true);
        if(!isFirstIn){
            welcomeHandler.sendEmptyMessageDelayed(GO_HOME,TIME);
        }else {
            welcomeHandler.sendEmptyMessageDelayed(GO_GUIDE,TIME);
            SharedPreferences.Editor editor = perPreferences.edit();
            editor.putBoolean("isFirstIn",false);
            editor.commit();
        }
    }

    private void goHome(){
        Intent i = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goGuide(){
        Intent i = new Intent(WelcomeActivity.this,Guide.class);
        startActivity(i);
        finish();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
