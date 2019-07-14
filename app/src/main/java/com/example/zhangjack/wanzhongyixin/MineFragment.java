package com.example.zhangjack.wanzhongyixin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MineFragment extends Fragment {



    public MineFragment() {
        // Required empty public constructor
    }


    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }



}
