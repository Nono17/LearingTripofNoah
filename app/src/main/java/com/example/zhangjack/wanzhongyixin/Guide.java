package com.example.zhangjack.wanzhongyixin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zhangjack.wanzhongyixin.Adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Guide extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.iv1,R.id.iv2,R.id.iv3,R.id.iv4};
    private Button start_btn;

    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏显示且去掉虚拟按钮
        hideBottomUIMenu();

        setContentView(R.layout.guide);
        initViews();
        initDots();
    }

    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);



        views=new ArrayList<View>();
        views.add(inflater.inflate(R.layout.one,null));
        views.add(inflater.inflate(R.layout.two,null));
        views.add(inflater.inflate(R.layout.three,null));
        views.add(inflater.inflate(R.layout.four,null));

        vpAdapter = new ViewPagerAdapter(views, this);
        vp=(ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        start_btn = (Button) views.get(3).findViewById(R.id.start_btn);
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences perPreferences = getSharedPreferences("registerMode",MODE_PRIVATE);
                isRegister = perPreferences.getBoolean("isRegister",false);

                if(isRegister){
                    goHome();
                }else {
                    goRegister();
                }
            }
        });

        vp.addOnPageChangeListener(this);
//


    }

    private void goHome(){
        Intent i = new Intent(Guide.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goRegister(){
        Intent i = new Intent(Guide.this,SignInActivity.class);
        startActivity(i);
        //finish();
    }

    private void initDots(){
        dots = new ImageView[views.size()];
        for (int i=0;i<views.size();i++){
            dots[i]=(ImageView)findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i=0;i<ids.length;i++){
            if(position==i){
                dots[i].setImageResource(R.drawable.select_point);
            }else {
                dots[i].setImageResource(R.drawable.unselect_point);
            }
        }
        //第四张隐藏小点
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.ll) ;
        if(position==3){
            linearLayout.setVisibility(View.GONE);
        }
        else{
            linearLayout.setVisibility(View.VISIBLE);
        }
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
