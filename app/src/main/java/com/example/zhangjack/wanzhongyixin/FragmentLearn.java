package com.example.zhangjack.wanzhongyixin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLearn extends Fragment {



    public FragmentLearn() {
        // Required empty public constructor
    }


    public static FragmentLearn newInstance() {
        FragmentLearn fragment = new FragmentLearn();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        return view;
    }



}
