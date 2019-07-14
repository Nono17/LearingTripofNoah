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
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    TextView RegisterTV,RetrievePasswordTV;
    ImageView signInAlipay,signInWechat,signInWeibo,signInBack;
    Button signInBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setStatusBarColor(this,R.color.SignInBackgroud);

        initWidget();

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

    //包含点击事件的控件
    private void initWidget() {
        RegisterTV = (TextView)findViewById(R.id.sign_in_to_register_tv);
        RetrievePasswordTV = (TextView) findViewById(R.id.sign_in_to_retrieve_password_tv);
        signInAlipay = (ImageView)findViewById(R.id.sign_in_alipay);
        signInWechat = (ImageView)findViewById(R.id.sign_in_wechat);
        signInWeibo = (ImageView)findViewById(R.id.sign_in_weibo);
        signInBtn = (Button)findViewById(R.id.sign_in_btn);
        signInBack = (ImageView)findViewById(R.id.sign_in_back);

        RegisterTV.setOnClickListener(this);
        RetrievePasswordTV.setOnClickListener(this);
        signInAlipay.setOnClickListener(this);
        signInWechat.setOnClickListener(this);
        signInWeibo.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        signInBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_to_register_tv:
                Intent i=new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(i);
                //finish();
                break;
            case R.id.sign_in_to_retrieve_password_tv:
                Intent a=new Intent(SignInActivity.this,RetrievePasswordActivity.class);
                startActivity(a);
                break;
            case R.id.sign_in_alipay:
                Intent b=new Intent(SignInActivity.this,IdentityActivity.class);
                startActivity(b);
                break;
            case R.id.sign_in_wechat:
                Intent c=new Intent(SignInActivity.this,IdentityActivity.class);
                startActivity(c);
                break;
            case R.id.sign_in_weibo:
                Intent d=new Intent(SignInActivity.this,IdentityActivity.class);
                startActivity(d);
                break;
            case R.id.sign_in_btn:
                Intent e=new Intent(SignInActivity.this,IdentityActivity.class);
                startActivity(e);
                break;
            case R.id.sign_in_back:
                Intent f=new Intent(SignInActivity.this,IdentityActivity.class);
                startActivity(f);
                break;

        }
    }
}
