package com.example.zhangjack.wanzhongyixin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangjack.wanzhongyixin.widget.BaseDialog;
import com.example.zhangjack.wanzhongyixin.widget.ViewConvertListener;
import com.example.zhangjack.wanzhongyixin.widget.ViewHolder;

public class DiseaseSelectActivity extends AppCompatActivity {

    private View statusBarView;
    private int diseaseMajorClass = 0;
    private int diseaseSubclass = 0;
    private int diseaseclass = 0;

    private static final int MEDICAL_DEF = 2000;
    private static final int MEDICAL_1 = 2001;
    private static final int MEDICAL_2 = 2002;
    private static final int MEDICAL_3 = 2003;
    private static final int MEDICAL_4 = 2004;
    private static final int MEDICAL_5 = 2005;
    private int medicalNow = MEDICAL_DEF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_select);




        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_disease_select);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(DiseaseSelectActivity.this,IdentityActivity.class);
                //startActivity(i);
                finish();
            }
        });

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

        Button button=(Button)findViewById(R.id.disease_select_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences perPreferences = getSharedPreferences("medical",MODE_PRIVATE);
                medicalNow = perPreferences.getInt("myMedical",MEDICAL_DEF);
                SharedPreferences.Editor editor = perPreferences.edit();
                editor.putInt("myMedical",medicalNow);
                editor.commit();

                Intent i=new Intent(DiseaseSelectActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        TextView textView=(TextView)findViewById(R.id.disease_select_skip);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DiseaseSelectActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
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

    //疾病大类选择
    public void showMajorClassClick(View view){
        CommonDialog.newInstance()
                .setLayoutId(R.layout.dialog_disease_major_class)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {

                        final TextView majorClass = (TextView)findViewById(R.id.disease_select_major_class_background_tv);
                        Resources resource = (Resources) getBaseContext().getResources();
                        final ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.product_detail_common);

                        holder.setOnClickListener(R.id.major_class_1, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(DiseaseSelectActivity.this, "点击了1", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                majorClass.setText("鱼鳞病");
                                medicalNow = MEDICAL_1;
                                //majorClass.setTextColor(csl);
                                //majorClass.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                            }
                        });

                        holder.setOnClickListener(R.id.major_class_2, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(DiseaseSelectActivity.this, "点击了2", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                majorClass.setText("白癜风");
                                medicalNow = MEDICAL_2;
                                //majorClass.setTextColor(csl);
                                //majorClass.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                            }
                        });
                        holder.setOnClickListener(R.id.major_class_3, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(DiseaseSelectActivity.this, "点击了2", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                majorClass.setText("白化病");
                                medicalNow = MEDICAL_3;
                                //majorClass.setTextColor(csl);
                                //majorClass.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                            }
                        });
                        holder.setOnClickListener(R.id.major_class_4, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                majorClass.setText("肝豆状核变性病");
                                medicalNow = MEDICAL_4;
                            }
                        });
                        holder.setOnClickListener(R.id.major_class_5, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                majorClass.setText("肺癌");
                                medicalNow = MEDICAL_5;
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .setSize(0, 273)
                .show(getSupportFragmentManager());
    }
}
