package com.example.zhangjack.wanzhongyixin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CommunityFragment extends Fragment {


    private RadioButton rbtn_1;
    private RadioButton rbtn_2;
    private RadioButton rbtn_3;
    private RadioGroup rgroup;
    private FrameLayout frg_content;
    private Fragment currentFragment;
    private FragmentLearn mFragmentLearn;
    private FragmentRecommend mFragmentRecommend;
    private FragmentFocus mFragmentFocus;

    public CommunityFragment() {
        // Required empty public constructor
    }


    public static CommunityFragment newInstance() {
        CommunityFragment fragment = new CommunityFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_community, container, false);
        initView(view);

        mFragmentLearn = FragmentLearn.newInstance();
        mFragmentFocus = FragmentFocus.newInstance();
        mFragmentRecommend = FragmentRecommend.newInstance();

        switchFragment(mFragmentLearn);

        return view;
    }


    private void initView(View view) {
        rbtn_1 = (RadioButton) view.findViewById(R.id.rbtn_1);
        rbtn_2 = (RadioButton) view.findViewById(R.id.rbtn_2);
        rbtn_3 = (RadioButton) view.findViewById(R.id.rbtn_3);
        rgroup = (RadioGroup) view.findViewById(R.id.rgroup);
        frg_content = (FrameLayout) view.findViewById(R.id.frg_content);

        rgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.rbtn_1:
                        switchFragment(mFragmentFocus);
                        break;
                    case R.id.rbtn_2:
                        switchFragment(mFragmentRecommend);
                        break;
                    case R.id.rbtn_3:
                        switchFragment(mFragmentLearn);
                        break;
                }
            }
        });
    }

    private void switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getChildFragmentManager()
                .beginTransaction();

        if (!targetFragment.isAdded()) {

            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.frg_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;

        transaction.commit();

    }
}
