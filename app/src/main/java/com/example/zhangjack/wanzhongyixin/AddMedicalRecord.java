package com.example.zhangjack.wanzhongyixin;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Paint;
import android.icu.lang.UProperty;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.zhangjack.wanzhongyixin.Adapter.PicAdapter;
import com.example.zhangjack.wanzhongyixin.gridimageview.GridImageView;
import com.example.zhangjack.wanzhongyixin.gridimageview.GridImageViewAdapter;
import com.example.zhangjack.wanzhongyixin.widget.BaseDialog;
import com.example.zhangjack.wanzhongyixin.widget.FlowLayout;
import com.example.zhangjack.wanzhongyixin.widget.TagItem;
import com.example.zhangjack.wanzhongyixin.widget.ViewConvertListener;
import com.example.zhangjack.wanzhongyixin.widget.ViewHolder;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.squareup.picasso.Picasso;

public class AddMedicalRecord extends AppCompatActivity implements MyCustomScrollView
        .ScrollViewListener, View.OnClickListener{

    private View statusBarView;

    Context c;
    ConstraintLayout addMedicalParentLayout;

    TextView tvDiagnostic,tvTreatment;//页面上方两个标题
    TextView tvNext,tvSymptom;//下一步按钮
    TextView selectDiagnosisTime,selectStartStopTime,selectEndTime;
    TextView selectMedical;



    View viewDiagnostic,viewTreatment;//标题下面的横线
    MyCustomScrollView addMedicalScrollView;
    LinearLayout addMedicalTitle;                  //标题
    LinearLayout addMedicalDiagnostic;           //诊断信息标题
    LinearLayout addMedicalTreatment;         //治疗过程标题
    FlowLayout mTagLayout;





    private int diagnosticHeight;                  //诊断信息板块控件高度
    private int treatmentHeight;                         //治疗过程板块控件高度


    //图片选择功能相关-start-
    /**
     * 选择的图片集
     */
    private ArrayList<Photo> selectedPhotoList = new ArrayList<>();


    private GridImageView<String> mGridSymptom,mGridImageView,mGridTreatmentProcess_1,mGridTreatmentSymptom_1;

    private int imageSelectNow = IMAGE_SYMPTOM;//当前是哪个按钮在选择图片

    private static final int IMAGE_SYMPTOM = 2000;
    private static final int IMAGE_DIAGNOSE = 2001;
    private static final int IMAGE_TREATMENT_1 = 2100;//治疗过程1照片
    private static final int IMAGE_TREATMENT_SYMPTOM_1 = 2200;//治疗过程1之后症状
    private static final int IMAGE_TREATMENT_2 = 2101;//治疗过程2照片
    private static final int IMAGE_TREATMENT_SYMPTOM_2 = 2202;//治疗过程2之后症状
    private static final int IMAGE_TREATMENT_3 = 2102;//治疗过程3照片
    private static final int IMAGE_TREATMENT_SYMPTOM_3 = 2202;//治疗过程3之后症状
    //图片选择功能相关-end-

    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    private Handler handler3 = new Handler();

    //选择器
    private TimePickerView diagnosisTime,pvCustomTime,pvCustomEndTime;
    int startOrEndFlag=0;//是startTime还是endTime
    Calendar diagnoseTime = Calendar.getInstance();//用户选择的开始时间
    Calendar startTime = Calendar.getInstance();//用户选择的开始时间
    Calendar stopTime = Calendar.getInstance();//用户选择的结束时间
    Calendar nowTime = Calendar.getInstance();//用户选择的结束时间

    //private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_add_medical_record);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicalRecord.this.finish();
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

        initView();
        initTitle();
        initListeners();
        initWidget();
        initDiagnosisTimePicker();
        initCustomTimePicker();
        initCustomEndTimePicker();
        initGridImage();


    }

    private void initGridImage(){
        mGridSymptom = (GridImageView<String>) this.findViewById(R.id.add_medical_symptom_gridview);
        mGridImageView = (GridImageView<String>) this.findViewById(R.id.add_medical_diagnostic_pic_gridview);
        mGridTreatmentProcess_1 = (GridImageView<String>) this.findViewById(R.id.add_medical_treatment_process_gridview);
        mGridTreatmentSymptom_1 = (GridImageView<String>) this.findViewById(R.id.add_medical_treatment_process_symptom_gridview);

        mGridSymptom.setAdapter(new GridImageViewAdapter<String>(){
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String path) {
                Picasso.with(context).load("file://"+path).centerCrop().resize(400,400).into(imageView);
            }

            @Override
            protected void onAddClick(Context context, List<String> list) {
                imageSelectNow = IMAGE_SYMPTOM;
                selectPictures();
            }

            @Override
            protected int getShowStyle() {
                return GridImageView.STYLE_GRID;
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                super.onItemImageClick(context, index, list);
                Toast.makeText(getApplicationContext(),"--->"+index,Toast.LENGTH_SHORT).show();
            }
        } );

        mGridImageView.setAdapter(new GridImageViewAdapter<String>(){
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String path) {
                Picasso.with(context).load("file://"+path).centerCrop().resize(400,400).into(imageView);
            }

            @Override
            protected void onAddClick(Context context, List<String> list) {
                imageSelectNow = IMAGE_DIAGNOSE;
                selectPictures();
            }

            @Override
            protected int getShowStyle() {
                return GridImageView.STYLE_GRID;
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                super.onItemImageClick(context, index, list);
                Toast.makeText(getApplicationContext(),"--->"+index,Toast.LENGTH_SHORT).show();
            }
        } );

        mGridTreatmentProcess_1.setAdapter(new GridImageViewAdapter<String>(){
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String path) {
                Picasso.with(context).load("file://"+path).centerCrop().resize(400,400).into(imageView);
            }

            @Override
            protected void onAddClick(Context context, List<String> list) {
                imageSelectNow = IMAGE_TREATMENT_1;
                selectPictures();
            }

            @Override
            protected int getShowStyle() {
                return GridImageView.STYLE_GRID;
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                super.onItemImageClick(context, index, list);
                Toast.makeText(getApplicationContext(),"--->"+index,Toast.LENGTH_SHORT).show();
            }
        } );

        mGridTreatmentSymptom_1.setAdapter(new GridImageViewAdapter<String>(){
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String path) {
                Picasso.with(context).load("file://"+path).centerCrop().resize(400,400).into(imageView);
            }

            @Override
            protected void onAddClick(Context context, List<String> list) {
                imageSelectNow = IMAGE_TREATMENT_SYMPTOM_1;
                selectPictures();
            }

            @Override
            protected int getShowStyle() {
                return GridImageView.STYLE_GRID;
            }

            @Override
            protected void onItemImageClick(Context context, int index, List<String> list) {
                super.onItemImageClick(context, index, list);
                Toast.makeText(getApplicationContext(),"--->"+index,Toast.LENGTH_SHORT).show();
            }
        } );

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

    private void initView(){
        tvDiagnostic = (TextView)findViewById(R.id.add_medical_diagnostic_information_tv);
        tvTreatment = (TextView)findViewById(R.id.add_medical_treatment_process_tv);

        viewDiagnostic = findViewById(R.id.add_medical_diagnostic_information_view);
        viewTreatment = findViewById(R.id.add_medical_treatment_process_view);

        addMedicalScrollView = (MyCustomScrollView)findViewById(R.id.add_medical_scrollview);

        addMedicalTitle = (LinearLayout)findViewById(R.id.add_medical_title);

        addMedicalDiagnostic = (LinearLayout)findViewById(R.id.add_medical_title_diagnostic);
        addMedicalTreatment = (LinearLayout)findViewById(R.id.add_medical_title_treatment);

        //addMedical_AddSymptomPicLinearlayout = (LinearLayout)findViewById(R.id.add_medical_add_symptom_pic_linear_layout);

        addMedicalParentLayout = (ConstraintLayout)findViewById(R.id.add_medical_record_parent_layout) ;

        mTagLayout = (FlowLayout)findViewById(R.id.add_medical_tag_layout);





        //rvImage = (RecyclerView) findViewById(R.id.add_medical_add_symptom_pic__recyclerView);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3, GridLayoutManager.VERTICAL, false);
        //adapter = new PicAdapter(this,selectedPhotoList);
        //rvImage.setLayoutManager(gridLayoutManager);
        //rvImage.setAdapter(adapter);
        //SnapHelper snapHelper = new PagerSnapHelper();
        //snapHelper.attachToRecyclerView(rvImage);

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
                        addMedicalScrollView.smoothScrollTo(0,diagnosticHeight);
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
                addMedicalScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }


    //包含点击事件的控件
    private void initWidget() {
        tvNext = (TextView)findViewById(R.id.add_medical_next);
        tvSymptom = (TextView) findViewById(R.id.add_medical_add_symptom_tv);
        selectDiagnosisTime = (TextView)findViewById(R.id.add_medical_diagnostic_time_tv);
        selectStartStopTime = (TextView)findViewById(R.id.add_medical_treatment_time_tv) ;
        selectEndTime = (TextView)findViewById(R.id.add_medical_treatment_end_time_tv);
        selectMedical = (TextView)findViewById(R.id.add_medical_select_medical_tv);


        tvNext.setOnClickListener(this);
        tvSymptom.setOnClickListener(this);
        selectDiagnosisTime.setOnClickListener(this);
        selectStartStopTime.setOnClickListener(this);
        selectEndTime.setOnClickListener(this);
        selectMedical.setOnClickListener(this);
    }

     //获取顶部图片高度后，设置滚动监听
    private void initListeners() {
        ViewTreeObserver mAddMedicalDiagnostic = addMedicalDiagnostic.getViewTreeObserver();
        ViewTreeObserver mAddMedicalTreatment = addMedicalTreatment.getViewTreeObserver();

        mAddMedicalTreatment.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addMedicalTreatment.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                treatmentHeight = addMedicalTreatment.getHeight();
            }
        });

        mAddMedicalDiagnostic.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                addMedicalDiagnostic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                diagnosticHeight = addMedicalDiagnostic.getHeight() -ScreenUtils.getStatusHeight(c);

                addMedicalScrollView.setScrollViewListener(AddMedicalRecord.this);
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


    public void selectPictures(){
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setFileProviderAuthority("com.example.zhangjack.wanzhongyixin.fileprovider")
                .setCount(9)
                .setOriginalMenu(true, true, null)
                .start(101);
    }

/*
    private void showSelectPictures(ArrayList<Photo> resultPhotos){

        for (int i=resultPhotos.size();i>0;i--){

            final ImageView imageview = (ImageView)LayoutInflater.from(this).inflate(
                    R.layout.add_pic,addMedicalRecord_AddSymptom,false
            ) ;
            addMedicalRecord_AddSymptom.addView(imageview,0);
        }


        //Toast.makeText(this, "showSelectPictures"+resultPhotos.size(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "showSelectPictures"+resultPhotos.get(1).path, Toast.LENGTH_SHORT).show();

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            //相机或相册回调
            if (requestCode == 101) {
                //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);

                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);


                if (imageSelectNow==IMAGE_SYMPTOM){
                    List<String> list = resultPaths;
                    //  PictureUtil.cropPhoto(this, Uri.parse("file://"+list.get(0)));
                    mGridSymptom.setImageData(list,true);
                    List<String> l=mGridSymptom.getImgDataList();
                }else if (imageSelectNow == IMAGE_DIAGNOSE){
                    List<String> list = resultPaths;
                    //  PictureUtil.cropPhoto(this, Uri.parse("file://"+list.get(0)));
                    mGridImageView.setImageData(list,true);
                    List<String> l=mGridImageView.getImgDataList();
                }else if (imageSelectNow == IMAGE_TREATMENT_1){
                    List<String> list = resultPaths;
                    //  PictureUtil.cropPhoto(this, Uri.parse("file://"+list.get(0)));
                    mGridTreatmentProcess_1.setImageData(list,true);
                    List<String> l=mGridTreatmentProcess_1.getImgDataList();
                }else if (imageSelectNow == IMAGE_TREATMENT_SYMPTOM_1){
                    List<String> list = resultPaths;
                    //  PictureUtil.cropPhoto(this, Uri.parse("file://"+list.get(0)));
                    mGridTreatmentSymptom_1.setImageData(list,true);
                    List<String> l=mGridTreatmentSymptom_1.getImgDataList();
                }


                //为了显示上传图片的图标按钮，还需要加一行
                //Photo tempPhoto = selectedPhotoList.get(0);
                //selectedPhotoList.add(tempPhoto);



                //rvImage.smoothScrollToPosition(0);
                //showSelectPictures(resultPhotos);


                //addMedical_AddSymptomPicLinearlayout.setVisibility(View.GONE);



                return;
            }

            //为拼图选择照片的回调
            if (requestCode == 102) {

                ArrayList<Photo> resultPhotos =
                        data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() == 1) {
                    resultPhotos.add(resultPhotos.get(0));
                }
                selectedPhotoList.clear();
                selectedPhotoList.addAll(resultPhotos);

                EasyPhotos.startPuzzleWithPhotos(this, selectedPhotoList, Environment.getExternalStorageDirectory().getAbsolutePath(), "AlbumBuilder", 103, false, GlideEngine.getInstance());
                return;
            }

            //拼图回调
            if (requestCode == 103) {
                String puzzlePath = data.getStringExtra(EasyPhotos.RESULT_PATHS);

                Photo puzzlePhoto = data.getParcelableExtra(EasyPhotos.RESULT_PHOTOS);
                selectedPhotoList.clear();
                selectedPhotoList.add(puzzlePhoto);
                //rvImage.smoothScrollToPosition(0);
            }

            if (requestCode == 201){
                String[] mAddTags =  data.getStringArrayExtra("symptomList");
                mTagLayout.removeAllViews();
                initTagLayout(mAddTags);
                //Toast.makeText(this, "requestCode == 201", Toast.LENGTH_SHORT).show();
                initListeners();//改变UI之后更新布局高度值
            }


        } else if (RESULT_CANCELED == resultCode) {
            Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
        }
    }

    private void initTagLayout(String[] mTextStr) {
        for (int i = 0; i < mTextStr.length; i++) {
            final int pos = i;
            final TextView text = (TextView) LayoutInflater.from(this).inflate(
                    R.layout.add_medical_tag_text, mTagLayout, false);
            text.setText(mTextStr[i]);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mTagLayout.addView(text);
        }
    }



    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    private String getYearMonth(Date date) {//可根据需要自行截取数据显示
        Log.d("getYearMonth()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        return format.format(date);
    }

    private String getYear(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    private String getMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(date);
    }

    private String getDate(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    private int getYearInt(Calendar nowTime){
        return Integer.parseInt(getYear(nowTime.getTime()));
    }

    private int getMonthInt(Calendar nowTime){
        return Integer.parseInt(getMonth(nowTime.getTime()));
    }

    private int getDateInt(Calendar nowTime){
        return Integer.parseInt(getDate(nowTime.getTime()));
    }

    private void initDiagnosisTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        final Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 23);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2027, 2, 28);



        //时间选择器 ，自定义布局
        diagnosisTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                selectDiagnosisTime.setText(getTime(date));
                Toast.makeText(AddMedicalRecord.this, getTime(date), Toast.LENGTH_SHORT).show();
                selectDiagnosisTime.setText("确诊时间："+getYearMonth(date));

                diagnoseTime.set(Integer.parseInt(getYear(date)),Integer.parseInt(getMonth(date)),Integer.parseInt(getDate(date)));


            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_diagnose_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvComplete = (TextView) v.findViewById(R.id.pickerview_time_complete);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.pickerview_time_cancel);
                        tvComplete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                diagnosisTime.returnData();
                                diagnosisTime.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                diagnosisTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(17)
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.4f)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFFDEDEDE)
                .setOutSideCancelable(false)
                .setDecorView(addMedicalParentLayout)
                .build();
    }


    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        final Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 23);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2027, 2, 28);



        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                selectStartStopTime.setText(getTime(date));
                Toast.makeText(AddMedicalRecord.this, getTime(date), Toast.LENGTH_SHORT).show();
                selectStartStopTime.setText("开始治疗的时间："+getYearMonth(date));

                startTime.set(Integer.parseInt(getYear(date)),Integer.parseInt(getMonth(date)),Integer.parseInt(getDate(date)));


            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvComplete = (TextView) v.findViewById(R.id.pickerview_time_complete);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.pickerview_time_cancel);
                        tvComplete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(17)
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.4f)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFFDEDEDE)
                .setOutSideCancelable(false)
                .setDecorView(addMedicalParentLayout)
                .build();
    }

    private void initCustomEndTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 23);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2027, 2, 28);



        //时间选择器 ，自定义布局
        pvCustomEndTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                //selectEndTime.setText(getTime(date));
                Toast.makeText(AddMedicalRecord.this, getTime(date), Toast.LENGTH_SHORT).show();

                selectEndTime.setText("结束治疗的时间："+getYearMonth(date));

                stopTime.set(Integer.parseInt(getYear(date)),Integer.parseInt(getMonth(date)),Integer.parseInt(getDate(date)));

                if (getYearInt(startTime)>getYearInt(stopTime)){
                    Toast.makeText(AddMedicalRecord.this, "结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
                }else if ((getYearInt(startTime)== getYearInt(stopTime))&&(getMonthInt(startTime)>getMonthInt(stopTime))){
                    Toast.makeText(AddMedicalRecord.this, "结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
                }



            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_end_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvComplete = (TextView) v.findViewById(R.id.pickerview_end_time_complete);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.pickerview_end_time_cancel);
                        tvComplete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomEndTime.returnData();
                                pvCustomEndTime.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomEndTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(17)
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.4f)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFFDEDEDE)
                .setOutSideCancelable(false)
                .setDecorView(addMedicalParentLayout)
                .build();
    }

    private void medicalRecordShowMajorClassClick(){
        CommonDialog.newInstance()
                .setLayoutId(R.layout.dialog_disease_major_class)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {

                        final TextView majorClass = (TextView)findViewById(R.id.add_medical_select_medical_tv);
                        Resources resource = (Resources) getBaseContext().getResources();
                        final ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.product_detail_common);

                        holder.setOnClickListener(R.id.major_class_1, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(DiseaseSelectActivity.this, "点击了1", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                majorClass.setText("鱼鳞病");
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
                                //majorClass.setTextColor(csl);
                                //majorClass.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                            }
                        });
                        holder.setOnClickListener(R.id.major_class_4, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                majorClass.setText("肝豆状核变性病");
                            }
                        });
                        holder.setOnClickListener(R.id.major_class_5, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                majorClass.setText("肺癌");
                            }
                        });
                    }
                })
                .setShowBottom(true)
                .setSize(0, 273)
                .show(getSupportFragmentManager());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_medical_next:
                Intent i=new Intent(AddMedicalRecord.this,TreatmentExperienceActivity.class);
                startActivity(i);
                //finish();
                break;
            case android.R.id.home:
                //AddMedicalRecord.this.finish();
                break;
            case R.id.add_medical_add_symptom_tv:
                Intent a=new Intent(AddMedicalRecord.this,AddSymptomTagActivity.class);
                startActivityForResult(a,201);
                break;
            case R.id.add_medical_select_medical_tv://选择疾病
                medicalRecordShowMajorClassClick();
                break;
        }
        if (v.getId() == R.id.add_medical_treatment_time_tv && pvCustomTime != null){
            pvCustomTime.show(); //弹出自定义时间选择器
        }else if (v.getId() == R.id.add_medical_treatment_end_time_tv && pvCustomEndTime != null){
            pvCustomEndTime.show(); //弹出自定义时间选择器
        }else if (v.getId() == R.id.add_medical_diagnostic_time_tv &&  diagnosisTime!= null){
            diagnosisTime.show();
        }
    }

}