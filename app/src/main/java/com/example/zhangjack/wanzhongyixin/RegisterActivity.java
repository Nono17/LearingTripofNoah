package com.example.zhangjack.wanzhongyixin;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView registerBack;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setStatusBarColor(this,R.color.SignInBackgroud);
        initView();
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
        }
    }
    private void initView(){
        registerBack = (ImageView)findViewById(R.id.register_back);
        registerBtn = (Button)findViewById(R.id.register_btn);

        registerBack.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_back:
                finish();
                break;
            case R.id.register_btn:
                Intent a=new Intent(RegisterActivity.this,IdentityActivity.class);
                startActivity(a);
                break;

        }
    }
}
