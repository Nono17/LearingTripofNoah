package com.example.zhangjack.wanzhongyixin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.zhangjack.wanzhongyixin.bean.JsonBean;
import com.example.zhangjack.wanzhongyixin.widget.BaseDialog;
import com.example.zhangjack.wanzhongyixin.widget.ViewConvertListener;
import com.example.zhangjack.wanzhongyixin.widget.ViewHolder;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditPersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private View statusBarView;

    FrameLayout selectGender,selectAge;

    LinearLayout selectLocation;

    TextView locationText,ageText,ageTimeText,selectMedical;

    ConstraintLayout editPersonalParentLayout;



    //头像
    private ImageView mHead;

    //Location选择相关
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private static boolean isLoaded = false;

    //选择器
    private TimePickerView pvAgeTime;
    private OptionsPickerView pvLocationOptions;
    Calendar ageTime = Calendar.getInstance();//用户选择的开始时间
    Calendar nowTime = Calendar.getInstance();//系统现在时间
    int ageTimeInt;//年龄（XX岁）








    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

                        //Toast.makeText(EditPersonalInformationActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Toast.makeText(EditPersonalInformationActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(EditPersonalInformationActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_information);

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        //返回事件
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_edit_personal_information);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
         头像
         */
        /*
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);
        mHead = (ImageView)findViewById(R.id.medical_record_head_pic);
        mHead.setImageBitmap(createCircleImage(bitmap));
*/
        Button button = (Button)findViewById(R.id.edit_personal_information_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EditPersonalInformationActivity.this,AddMedicalRecord.class);
                startActivity(i);
                //finish();
            }
        });
        initView();
        initCustomTimePicker();
    }

    private void initView(){
        selectGender = (FrameLayout)findViewById(R.id.edit_personal_gender_framelayout);
        selectAge = (FrameLayout)findViewById(R.id.edit_personal_age_framelayout);

        selectLocation = (LinearLayout)findViewById(R.id.edit_personal_location_linearlayout);

        editPersonalParentLayout = (ConstraintLayout)findViewById(R.id.edit_personal_parent_constraint_layout) ;

        locationText = (TextView)findViewById(R.id.edit_personal_location_tv) ;
        ageText = (TextView)findViewById(R.id.edit_personal_age_tv);
        ageTimeText = (TextView)findViewById(R.id.edit_personal_age_time_tv);
        selectMedical = (TextView)findViewById(R.id.edit_personal_information_select_medical_tv) ;



        selectGender.setOnClickListener(this);
        selectLocation.setOnClickListener(this);
        selectAge.setOnClickListener(this);
        selectMedical.setOnClickListener(this);
        //locationText.setOnClickListener(this);
    }

    //选择地址
    private void showPickerView() {// 弹出选择器

        pvLocationOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";
/*
                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";
*/
                String tx = opt2tx;

                Resources resource = (Resources) getBaseContext().getResources();
                final ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.product_detail_common);

                locationText.setText(tx);
                locationText.setTextColor(csl);
                //Toast.makeText(EditPersonalInformationActivity.this, tx, Toast.LENGTH_SHORT).show();
            }
        })

                .setLayoutRes(R.layout.pickerview_location, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvComplete = (TextView) v.findViewById(R.id.pickerview_location_complete);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.pickerview_location_cancel);
                        tvComplete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvLocationOptions.returnData();
                                pvLocationOptions.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvLocationOptions.dismiss();
                            }
                        });


                    }
                })
                .setLineSpacingMultiplier(2.4f)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvLocationOptions.setPicker(options1Items, options2Items);//二级选择器
        pvLocationOptions.show();
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

    //选择性别
    private void selectGender(){
        CommonDialog.newInstance()
                .setLayoutId(R.layout.dialog_select_gender)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {

                        final TextView textView = (TextView)findViewById(R.id.edit_personal_gender_tv);
                        Resources resource = (Resources) getBaseContext().getResources();
                        final ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.product_detail_common);

                        holder.setOnClickListener(R.id.gender_male, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(DiseaseSelectActivity.this, "点击了1", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                textView.setText("男");
                                textView.setTextColor(csl);
                                //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                            }
                        });

                        holder.setOnClickListener(R.id.gender_female, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Toast.makeText(DiseaseSelectActivity.this, "点击了2", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                textView.setText("女");
                                textView.setTextColor(csl);
                                //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                            }
                        });
                    }
                })
                .setSize(0, 87)
                .setMargin(51)
                .show(getSupportFragmentManager());
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
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
        pvAgeTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                Resources resource = (Resources) getBaseContext().getResources();
                final ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.product_detail_common);
                ageTimeText.setText(getYearMonth(date));
                ageTimeText.setTextColor(csl);

                //Toast.makeText(EditPersonalInformationActivity.this, getYearMonth(date), Toast.LENGTH_SHORT).show();

                ageTime.set(Integer.parseInt(getYear(date)),Integer.parseInt(getMonth(date)),Integer.parseInt(getDate(date)));

                ageTimeInt = (int) ((getYearInt(nowTime) - getYearInt(ageTime))*12 + (getMonthInt(nowTime) - getMonthInt(ageTime)))/12;

                ageText.setText(ageTimeInt + "岁");


            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_age, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvComplete = (TextView) v.findViewById(R.id.pickerview_time_complete);
                        final TextView tvCancel = (TextView) v.findViewById(R.id.pickerview_time_cancel);

                        tvComplete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvAgeTime.returnData();
                                pvAgeTime.dismiss();
                            }
                        });
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvAgeTime.dismiss();
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
                .setDecorView(editPersonalParentLayout)
                .build();
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

    private void medicalRecordShowMajorClassClick(){
        CommonDialog.newInstance()
                .setLayoutId(R.layout.dialog_disease_major_class)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseDialog dialog) {

                        final TextView majorClass = (TextView)findViewById(R.id.edit_personal_information_select_medical_tv);
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
            case R.id.edit_personal_gender_framelayout:
                selectGender();
                //Intent i=new Intent(AddMedicalRecord.this,TreatmentExperienceActivity.class);
                //startActivity(i);
                //finish();
                break;
            case android.R.id.home:
                //AddMedicalRecord.this.finish();
                break;
            case R.id.edit_personal_location_linearlayout:
                if (isLoaded) {
                    showPickerView();
                } else {
                    Toast.makeText(EditPersonalInformationActivity.this, "Please waiting until the data is parsed", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edit_personal_information_select_medical_tv:
                medicalRecordShowMajorClassClick();
                break;
        }

        if (v.getId() == R.id.edit_personal_age_framelayout && pvAgeTime != null){
            pvAgeTime.show(); //弹出时间选择器
        }
    }

}
