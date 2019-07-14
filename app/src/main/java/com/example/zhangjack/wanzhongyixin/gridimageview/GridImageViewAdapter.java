package com.example.zhangjack.wanzhongyixin.gridimageview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.example.zhangjack.wanzhongyixin.R;

import java.util.List;

/**
 * @param <T> 指定类型
 */
public abstract class GridImageViewAdapter<T> {
    protected abstract void onDisplayImage(Context context, ImageView imageView, T path);

    protected abstract void onAddClick(Context context, List<T> list);

    protected void onItemImageClick(Context context, int index, List<T> list) {

    }

    protected int getShowStyle() {
        return GridImageView.STYLE_GRID;
    }

    protected
    @DrawableRes
    int generateAddIcon() {
        return R.drawable.up_pic_btn;
    }
}
