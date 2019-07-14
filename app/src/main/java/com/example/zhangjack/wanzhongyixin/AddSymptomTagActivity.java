package com.example.zhangjack.wanzhongyixin;

import android.content.Intent;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangjack.wanzhongyixin.widget.FlowLayout;
import com.example.zhangjack.wanzhongyixin.widget.TagItem;

import java.io.Serializable;
import java.util.ArrayList;

public class AddSymptomTagActivity extends AppCompatActivity {

    private View statusBarView;

    FlowLayout mTagLayout, mAddTagLayout,testLayout;
    EditText mEditText;
    TextView saveSymptom;

    private ArrayList<TagItem> mAddTags = new ArrayList<TagItem>();
    private int MAX_TAG_CNT = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_symptom_tag);

        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_add_symptom_tag);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddSymptomTagActivity.this.finish();
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




        mTagLayout = (FlowLayout) findViewById(R.id.add_symptom_tag_layout);
        mAddTagLayout = (FlowLayout) findViewById(R.id.add_symptom_addtag_layout);

        mEditText = (EditText) findViewById(R.id.add_symptom_add_edit);
        mEditText
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        doResetAddTagsStatus();
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            String text = mEditText.getEditableText()
                                    .toString().trim();
                            if (text.length() > 0) {
                                if (idxTextTag(text) < 0) {
                                    doAddText(text, true, -1);
                                }
                                mEditText.setText("");
                            }
                            return true;
                        }
                        return false;
                    }
                });
        initLayout();

        saveSymptom = (TextView)findViewById(R.id.add_symptom_tag_next);
        saveSymptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                //bundle.putSerializable("symptomList",(Serializable)mAddTags);
                //intent.putExtras(bundle);
                int mTagCnt = mAddTags.size();
                String[] mTextStr = new String[mTagCnt];
                for (int i = 0; i < mTagCnt; i++) {
                    TagItem item = mAddTags.get(i);
                    mTextStr[i] = item.tagText;
                }

                bundle.putSerializable("symptomList",mTextStr);
                intent.putExtras(bundle);
                //Toast.makeText(AddSymptomTagActivity.this, "Size=" + mTagCnt + "项，"+"第一项是"+mAddTags.get(0).tagText, Toast.LENGTH_LONG).show();
                setResult(RESULT_OK,intent);
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

    protected int idxTextTag(String text) {
        int mTagCnt = mAddTags.size();
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (text.equals(item.tagText)) {
                return i;
            }
        }
        return -1;
    }

    String[] mTextStr = { "全身干燥", "下肢淡棕色鳞屑", "腹部淡棕色鳞屑", "背部白色鳞屑", "臀部毛囊角化性丘疹", "眼睑外翻", "全身皲裂", "全身脱皮"};

    private void initLayout() {
        for (int i = 0; i < mTextStr.length; i++) {
            final int pos = i;
            final TextView text = (TextView) LayoutInflater.from(this).inflate(
                    R.layout.tag_text, mTagLayout, false);
            text.setText(mTextStr[i]);
            text.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    text.setActivated(!text.isActivated());
                    doResetAddTagsStatus();
                    if (text.isActivated()) {
                        boolean bResult = doAddText(mTextStr[pos], false, pos);
                        text.setActivated(bResult);
                    } else {
                        doDelText(mTextStr[pos]);
                    }
                }
            });
            mTagLayout.addView(text);
        }
    }


    protected void doDelText(String string) {
        int mTagCnt = mAddTags.size();
        mEditText.setVisibility(View.VISIBLE);
        for (int i = 0; i < mTagCnt; i++) {
            TagItem item = mAddTags.get(i);
            if (string.equals(item.tagText)) {
                mAddTagLayout.removeViewAt(i);
                mAddTags.remove(i);
                if (!item.tagCustomEdit) {
                    mTagLayout.getChildAt(item.idx).setActivated(false);
                }
                return;
            }
        }
    }

    protected void doAddTagLayout(String str) {
        final TextView text = (TextView) LayoutInflater.from(this).inflate(
                R.layout.addtag_text, mAddTagLayout, false);
        text.setText(str);
        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (text.isActivated()) {
                    mAddTagLayout.removeView(text);
                    mEditText.setVisibility(View.GONE);
                } else {
                    text.setActivated(true);
                }
            }
        });
        mAddTagLayout.addView(text, 0);
    }

    private boolean doAddText(final String str, boolean bCustom, int idx) {
        int tempIdx = idxTextTag(str);
        if (tempIdx >= 0) {
            TagItem item = mAddTags.get(tempIdx);
            item.tagCustomEdit = false;
            item.idx = tempIdx;

            return true;
        }

        int tagCnt = mAddTags.size();
        if (tagCnt == MAX_TAG_CNT) {
            Toast.makeText(AddSymptomTagActivity.this, "最多添加" + MAX_TAG_CNT + "个症状", Toast.LENGTH_SHORT).show();
            return false;
        }

        TagItem item = new TagItem();
        item.tagText = str;
        item.tagCustomEdit = bCustom;
        item.idx = idx;
        mAddTags.add(item);

        final TextView view = (TextView) LayoutInflater.from(this).inflate(
                R.layout.addtag_text, mAddTagLayout, false);
        item.mView = view;
        view.setText(str);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (view.isActivated()) {
                    doDelText(str);
                } else {
                    doResetAddTagsStatus();
                    view.setText(view.getText() + " ×");
                    view.setActivated(true);
                }
            }
        });
        mAddTagLayout.addView(view, tagCnt);
        tagCnt++;
        if (tagCnt == MAX_TAG_CNT) {
            mEditText.setVisibility(View.GONE);
        }

        return true;
    }

    protected void doResetAddTagsStatus() {
        int cnt = mAddTags.size();
        for (int i = 0; i < cnt; i++) {
            TagItem item = mAddTags.get(i);
            item.mView.setActivated(false);
            item.mView.setText(item.tagText);
        }
    }

}
