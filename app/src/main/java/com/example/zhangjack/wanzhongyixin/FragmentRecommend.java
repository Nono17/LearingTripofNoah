package com.example.zhangjack.wanzhongyixin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentRecommend extends Fragment {



    public FragmentRecommend() {
        // Required empty public constructor
    }


    public static FragmentRecommend newInstance() {
        FragmentRecommend fragment = new FragmentRecommend();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        return view;
    }



}
