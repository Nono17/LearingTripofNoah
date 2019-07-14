package com.example.zhangjack.wanzhongyixin;

import android.content.Intent;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TreatmentExperienceActivity extends AppCompatActivity implements View.OnClickListener {

    private View statusBarView;
    TextView tvTreatmentExperienceNext,tvTreatmentExperienceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_experience);

        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_treatment_experience);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        tvTreatmentExperienceNext = (TextView)findViewById(R.id.toolbar_treatment_experience_next);
        tvTreatmentExperienceNext.setOnClickListener(this);

        tvTreatmentExperienceBack = (TextView)findViewById(R.id.toolbar_treatment_experience_back);
        tvTreatmentExperienceBack.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_treatment_experience_back:
                TreatmentExperienceActivity.this.finish();

                break;
            case R.id.toolbar_treatment_experience_next:
                Intent i=new Intent(TreatmentExperienceActivity.this,ReceiveDividentsActivity.class);
                startActivity(i);
                break;

        }
    }
}
