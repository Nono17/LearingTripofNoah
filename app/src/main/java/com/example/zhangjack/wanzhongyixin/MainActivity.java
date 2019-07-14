package com.example.zhangjack.wanzhongyixin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.haha.perflib.Main;

public class MainActivity extends AppCompatActivity {
    private View statusBarView;


    private FrameLayout frg_content;
    private RadioButton rbtn_1;
    private RadioButton rbtn_2;
    private RadioButton rbtn_3;
    private RadioButton rbtn_4;
    private Fragment currentFragment;
    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;
    private CommunityFragment mCommunityFragment;
    private RadioGroup rgroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
*/
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
        }*/
        statusbar();
        initView();
        mHomeFragment = HomeFragment.newInstance();
        mMineFragment = MineFragment.newInstance();
        mCommunityFragment = CommunityFragment.newInstance();

        switchFragment(mHomeFragment);




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

    private void statusbar(){
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
    }

    private void initView() {
        frg_content = (FrameLayout) findViewById(R.id.frg_content);
        rbtn_1 = (RadioButton) findViewById(R.id.rbtn_1);
        rbtn_2 = (RadioButton) findViewById(R.id.rbtn_2);
        rbtn_3 = (RadioButton) findViewById(R.id.rbtn_3);
        rgroup = (RadioGroup) findViewById(R.id.rgroup);

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.rbtn_1:
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.rbtn_2:
                        switchFragment(mCommunityFragment);
                        break;
                    case R.id.rbtn_3:
                        switchFragment(mMineFragment);
                        break;
                }
            }
        });
    }

    private void switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (!targetFragment.isAdded()) {

            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frg_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;

        transaction.commit();

    }


}
