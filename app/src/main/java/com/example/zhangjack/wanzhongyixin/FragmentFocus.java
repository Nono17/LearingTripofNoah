package com.example.zhangjack.wanzhongyixin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentFocus extends Fragment {



    public FragmentFocus() {
        // Required empty public constructor
    }


    public static FragmentFocus newInstance() {
        FragmentFocus fragment = new FragmentFocus();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_focus, container, false);
        return view;
    }



}
