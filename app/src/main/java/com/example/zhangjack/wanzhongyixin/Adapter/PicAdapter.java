package com.example.zhangjack.wanzhongyixin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.zhangjack.wanzhongyixin.R;
import com.huantansheng.easyphotos.models.album.entity.Photo;

import java.util.ArrayList;

/**
 * 返回图片的列表适配器
 * Created by huan on 2017/10/30.
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.MainVH> {
    private ArrayList<Photo> list;
    private LayoutInflater mInflater;
    private RequestManager mGlide;
    private int lastIndex;

    public PicAdapter(Context cxt, ArrayList<Photo> list) {
        this.list = list;
        mInflater = LayoutInflater.from(cxt);
        mGlide = Glide.with(cxt);
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item, parent, false);
        final MainVH holder = new MainVH(view);

        holder.PicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position==lastIndex){
                    //
                }
            }
        });

        return holder;

        //return new MainVH(mInflater.inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(MainVH holder, int position) {
        lastIndex = this.list.size();
        Photo photo = list.get(position);
        mGlide.load(photo.path).into(holder.ivPhoto);
/*
        if (position!=lastIndex-1){
            mGlide.load(photo.path).into(holder.ivPhoto);
        }else {
            holder.ivPhoto.setImageResource(R.drawable.dotted_box);
        }
        */

        //holder.tvMessage.setText("[图片名称]： "+photo.name+"\n[宽]："+photo.width+"\n[高]："+photo.height+"\n[文件大小,单位bytes]："+photo.size+"\n[日期，时间戳，毫秒]："+photo.time+"\n[图片地址]："+photo.path+"\n[图片类型]："+photo.type+"\n[是否选择原图]："+photo.selectedOriginal);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainVH extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        View PicView;
        //TextView tvMessage;
        MainVH(View itemView) {
            super(itemView);
            PicView = itemView;
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo_item);
        }
    }

}
