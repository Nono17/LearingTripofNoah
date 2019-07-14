package com.example.zhangjack.wanzhongyixin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhouwei.blurlibrary.EasyBlur;

public class MedicalRecordOfMine extends AppCompatActivity implements MyCustomScrollView
        .ScrollViewListener, View.OnClickListener {
    private View statusBarView;

    private ImageView mImageBg,mHead;
    private TextView view;

    ImageView IVshare;//分享按钮

    /**
     顶部导航按钮用参数
     start
     */
    Context MedicalRecordOfMineContext;

    TextView tvDiagnostic,tvTreatment;//页面上方两个标题
    View viewDiagnostic,viewTreatment;//标题下面的横线
    MyCustomScrollView MedicalRecordOfMineScrollView;
    LinearLayout MedicalRecordOfMineTitle;                  //标题布局
    LinearLayout MedicalRecordOfMineDiagnostic;           //诊断信息标题
    LinearLayout MedicalRecordOfMineTreatment;         //治疗过程标题

    private int diagnosticHeight;                  //诊断信息板块控件高度
    private int treatmentHeight;                         //治疗过程板块控件高度

    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    /**
     end
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_of_mine);

        //返回事件
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_medical_record_of_mine);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(MedicalRecordOfMine.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        //去掉Toolbar默认标题栏
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
        /**
        模糊图片
        */
        mImageBg = (ImageView) findViewById(R.id.medical_record_image_bg);
        view = (TextView) findViewById(R.id.blur_text);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);
        long startTime = System.currentTimeMillis();

        //Bitmap finalBitmap = EasyBlur.fastBlur(bitmap,scale,20);

        long endTime = System.currentTimeMillis();

        Log.i("zhouwei","cost Time:"+(endTime - startTime));

        mImageBg.setImageBitmap(bitmap);

        mImageBg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageBg.getViewTreeObserver().removeOnPreDrawListener(this);
                mImageBg.buildDrawingCache();
                Bitmap bmp = mImageBg.getDrawingCache();
                Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
                        (int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(overlay);

                canvas.translate(-view.getLeft(), -view.getTop());
                canvas.drawBitmap(bmp, 0, 0, null);

                Bitmap finalBitmap = EasyBlur.with(MedicalRecordOfMine.this)
                        .bitmap(overlay) //要模糊的图片
                        .radius(10)//模糊半径
                        .blur();

                view.setBackground(new BitmapDrawable(
                        getResources(), finalBitmap));
                return true;
            }
        });

        /**
         头像
         */
        mHead = (ImageView)findViewById(R.id.medical_record_head_pic);
        mHead.setImageBitmap(createCircleImage(bitmap));


        //顶部导航用方法
        initView();
        initTitle();
        initListeners();
        initWidget();

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

    //圆形图片裁剪
    public static Bitmap createCircleImage(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }


    //顶部导航用方法
    private void initView(){
        tvDiagnostic = (TextView)findViewById(R.id.medical_record_of_mine_diagnostic_information_tv);
        tvTreatment = (TextView)findViewById(R.id.medical_record_of_mine_treatment_process_tv);

        viewDiagnostic = findViewById(R.id.medical_record_of_mine_diagnostic_information_view);
        viewTreatment = findViewById(R.id.medical_record_of_mine_treatment_process_view);

        MedicalRecordOfMineScrollView = (MyCustomScrollView)findViewById(R.id.medical_record_of_mine_scrollview);

        MedicalRecordOfMineTitle = (LinearLayout)findViewById(R.id.medical_record_of_mine_title);

        MedicalRecordOfMineDiagnostic = (LinearLayout)findViewById(R.id.medical_record_of_mine_title_diagnostic);
        MedicalRecordOfMineTreatment = (LinearLayout)findViewById(R.id.medical_record_of_mine_title_treatment);
    }

    private void initTitle() {
        tvDiagnostic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToPosition();
            }
        });

        tvTreatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler2.post(new Runnable() {
                    @Override
                    public void run() {
                        MedicalRecordOfMineScrollView.smoothScrollTo(0,diagnosticHeight);
                    }
                });
            }
        });
    }

    //滑动到顶部
    private void scrollToPosition() {
        handler1.post(new Runnable() {

            @Override
            public void run() {
                MedicalRecordOfMineScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }


    //包含点击事件的控件
    private void initWidget() {
        IVshare = (ImageView)findViewById(R.id.toolbar_medical_record_of_mine_share);

        IVshare.setOnClickListener(this);
    }

    //获取顶部图片高度后，设置滚动监听
    private void initListeners() {
        ViewTreeObserver mAddMedicalDiagnostic = MedicalRecordOfMineDiagnostic.getViewTreeObserver();
        ViewTreeObserver mAddMedicalTreatment = MedicalRecordOfMineTreatment.getViewTreeObserver();

        mAddMedicalTreatment.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                MedicalRecordOfMineTreatment.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                treatmentHeight = MedicalRecordOfMineTreatment.getHeight();
            }
        });

        mAddMedicalDiagnostic.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                MedicalRecordOfMineDiagnostic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                diagnosticHeight = MedicalRecordOfMineDiagnostic.getHeight() -ScreenUtils.getStatusHeight(MedicalRecordOfMineContext);

                MedicalRecordOfMineScrollView.setScrollViewListener(MedicalRecordOfMine.this);
            }
        });
    }

    @Override
    public void onScrollChanged(MyCustomScrollView scrollView, int x, int y, int oldx, int oldy) {
        try {
            if (y < diagnosticHeight){
                clearAndShowThis(viewDiagnostic);
            }
            else {
                clearAndShowThis(viewTreatment);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void clearAndShowThis(View currentView) {
        if (currentView == viewDiagnostic){
            viewTreatment.setVisibility(View.INVISIBLE);
        }
        else {
            viewDiagnostic.setVisibility(View.INVISIBLE);
        }
        currentView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_medical_record_of_mine_share:
                //finish();
                break;
        }
    }


}
