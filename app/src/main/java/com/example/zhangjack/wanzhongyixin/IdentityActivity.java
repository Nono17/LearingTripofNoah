package com.example.zhangjack.wanzhongyixin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IdentityActivity extends AppCompatActivity {

    private int identityFlag = 0;
    private View statusBarView;

    //身份
    private static final int IDENTITY_PATIENT = 1000;
    private static final int IDENTITY_DOCTOR = 1001;
    private static final int IDENTITY_DEF = 0;
    private int myIdentity = IDENTITY_DEF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity);



        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_identity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IdentityActivity.this,Guide.class);
                startActivity(i);
                finish();
            }
        });
/*
        //去掉状态栏和屏幕底部虚拟按钮
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();

            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //getWindow().setNavigationBarColor(Color.TRANSPARENT);
            //getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
*/
        //设置渐变色状态栏
        //延时加载数据.
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                if (isStatusBar()) {
                    initStatusBar();
                    getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            initStatusBar();
                        }
                    });
                }
                //只走一次
                return false;
            }
        });


        final ImageButton patientButton=(ImageButton) findViewById(R.id.identity_patient_ib);
        final ImageButton doctorButton=(ImageButton) findViewById(R.id.identity_doctor_ib);
        Button button=(Button)findViewById(R.id.identity_btn);

        final ImageView patientImage=(ImageView) findViewById(R.id.identity_patient_iv);
        final ImageView doctorImage=(ImageView) findViewById(R.id.identity_doctor_iv);



        patientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identityFlag==0 || identityFlag==2){
                    patientButton.setBackgroundResource(R.drawable.identity_shape_select);
                    doctorButton.setBackgroundResource(R.drawable.identity_shape_unselect);
                    patientImage.setImageResource(R.mipmap.radio_group_select);
                    doctorImage.setImageResource(R.mipmap.radio_group_unselect);
                    identityFlag=1;
                }
            }
        });

        doctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identityFlag==0 || identityFlag==1){
                    patientButton.setBackgroundResource(R.drawable.identity_shape_unselect);
                    doctorButton.setBackgroundResource(R.drawable.identity_shape_select);
                    patientImage.setImageResource(R.mipmap.radio_group_unselect);
                    doctorImage.setImageResource(R.mipmap.radio_group_select);
                    identityFlag=2;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(identityFlag==1 || identityFlag==2){
                    SharedPreferences perPreferences = getSharedPreferences("identity",MODE_PRIVATE);
                    myIdentity = perPreferences.getInt("myIdentity",IDENTITY_DEF);
                    if (identityFlag ==1){
                        SharedPreferences.Editor editor = perPreferences.edit();
                        editor.putInt("myIdentity",IDENTITY_PATIENT);
                        editor.commit();
                    }else {
                        SharedPreferences.Editor editor = perPreferences.edit();
                        editor.putInt("myIdentity",IDENTITY_DOCTOR);
                        editor.commit();
                    }

                    Intent i=new Intent(IdentityActivity.this,DiseaseSelectActivity.class);
                    startActivity(i);
                    //finish();
                    //Toast.makeText(IdentityActivity.this,"选择身份" + myIdentity,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(IdentityActivity.this,"请先选择身份",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //hideBottomUIMenu();

    }

    private void initStatusBar() {
        if (statusBarView == null) {
            //利用反射机制修改状态栏背景
            int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
            statusBarView = getWindow().findViewById(identifier);
        }
        if (statusBarView != null) {
            statusBarView.setBackgroundResource(R.drawable.toolbar_bg);
        }
    }

    protected boolean isStatusBar() {
        return true;
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
