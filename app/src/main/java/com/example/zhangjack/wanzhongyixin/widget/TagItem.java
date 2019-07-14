package com.example.zhangjack.wanzhongyixin.widget;

import android.widget.TextView;

import java.io.Serializable;

public class TagItem implements Serializable {
    public String tagText;
    public boolean tagCustomEdit;//true表示是选择的标签，false表示编写的标签
    public int idx;
    public TextView mView;

}
