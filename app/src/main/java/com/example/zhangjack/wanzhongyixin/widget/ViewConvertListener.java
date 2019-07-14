package com.example.zhangjack.wanzhongyixin.widget;

import java.io.Serializable;

public interface ViewConvertListener extends Serializable {
    long serialVersionUID = System.currentTimeMillis();

    void convertView(ViewHolder holder, BaseDialog dialog);
}
