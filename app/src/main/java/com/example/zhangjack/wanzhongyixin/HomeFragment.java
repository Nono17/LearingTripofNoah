package com.example.zhangjack.wanzhongyixin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangjack.wanzhongyixin.Adapter.BaseRecyclerAdapter;
import com.example.zhangjack.wanzhongyixin.Adapter.SmartViewHolder;
import com.example.zhangjack.wanzhongyixin.util.StatusBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Arrays;
import java.util.Collection;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class HomeFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private TextView gotoPersonalInformation;

    private class PatientCircle{
        int avatarId;
        int moodId;
        int image1Id;
        int image2Id;
        int image3Id;
        String nickname;
        String medicalname;
        String majorSymptom;
        String contentText;
        String distanceTime;
    }

    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<PatientCircle> mAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View root, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(root, savedInstanceState);

        //去掉Toolbar默认标题栏
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) root.findViewById(R.id.toolbar_fragment_home);



        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        recyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<PatientCircle>(loadModels(),R.layout.item_patient_circle) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, PatientCircle model, int position) {
                holder.text(R.id.patient_circle_nickname,model.nickname);
                holder.text(R.id.patient_circle_medical_name,model.medicalname);
                holder.text(R.id.patient_circle_major_symptom,model.majorSymptom);
                holder.text(R.id.patient_circle_content_text,model.contentText);
                holder.text(R.id.patient_circle_distance_time,model.distanceTime);


                holder.image(R.id.patient_circle_avatar,model.avatarId);
                holder.image(R.id.patient_circle_mood,model.moodId);
                holder.image(R.id.patient_circle_image1,model.image1Id);
                holder.image(R.id.patient_circle_image2,model.image2Id);
            }
        });

        RefreshLayout refreshLayout = root.findViewById(R.id.refreshLayout);

        //第一次进入演示刷新
        if (isFirstEnter) {
            isFirstEnter = false;
            refreshLayout.autoRefresh();
        }

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);//恢复上拉状态
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter.getCount() > 36) {
                            Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//设置之后，将不会再触发加载事件
                        } else {
                            mAdapter.loadMore(loadModels());
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 0);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
                return false;
            }
        });

        //StatusBarUtil.darkMode(getActivity());
        //StatusBarUtil.setPaddingSmart(getContext(), root);
        //StatusBarUtil.setPaddingSmart(getContext(), toolbar);
        //StatusBarUtil.setPaddingSmart(getContext(), root.findViewById(R.id.blurView));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        floatingActionButton = (FloatingActionButton)getActivity().findViewById(R.id.home_floating_action_button);
        gotoPersonalInformation = (TextView)getActivity().findViewById(R.id.home_goto_personal_information);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),AddMedicalRecord.class);
                startActivity(i);
            }
        });

        gotoPersonalInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),EditPersonalInformationActivity.class);
                startActivity(i);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    /**
     * 模拟数据
     */
    private Collection<PatientCircle> loadModels() {
        return Arrays.asList(
                new PatientCircle() {{
                    this.nickname = "张小迪";
                    this.medicalname = "鱼鳞病";
                    this.majorSymptom = "皮肤瘙痒";
                    this.contentText = "      今天终于康复出院了。历时三年，我的鱼鳞病终于好啦！接下来我会给大家分享我的治疗过程和心路历程，虽然很辛苦但是我也得到了回报！大家一起加油！";
                    this.distanceTime = "1小时前";
                    this.avatarId = R.drawable.ic_avatar1;
                    this.moodId = R.mipmap.ic_emoj_very_good;
                    this.image1Id = R.drawable.ic_pic1;
                    this.image2Id = R.drawable.ic_pic2;
                }}, new PatientCircle() {{
                    this.nickname = "李露露";
                    this.medicalname = "鱼鳞病";
                    this.majorSymptom = "皮肤干燥";
                    this.contentText = "      第无数次治疗，每天浑身被药的味道包围，也内服不少药，也没有什么明显好转，这样下去什么时候才是个头呢……绝望中。";
                    this.distanceTime = "1小时前";
                    this.avatarId = R.drawable.ic_avatar2;
                    this.moodId = R.mipmap.ic_emoj_good;
                    this.image1Id = R.drawable.ic_pic3;
                    this.image2Id = R.drawable.ic_pic4;
                }}, new PatientCircle() {{
                    this.nickname = "倪文龙";
                    this.medicalname = "鱼鳞病";
                    this.majorSymptom = "皮肤潮红";
                    this.contentText = "      现在每一天都越来越有信心啦！看着朋友们都和我一样在与鱼鳞病抗争，我每天都能积极的配合生的治疗听取教授们的建议，三个疗程坚持了下来感觉非常不错！大家可以多多关注王教授的讲座！";
                    this.distanceTime = "1小时前";
                    this.avatarId = R.drawable.ic_avatar3;
                    this.moodId = R.mipmap.ic_emoj_good;
                    this.image1Id = R.drawable.ic_pic5;
                    this.image2Id = R.drawable.ic_pic6;
                }}, new PatientCircle() {{
                    this.nickname = "李小春";
                    this.medicalname = "鱼鳞病";
                    this.majorSymptom = "全身干燥脱皮";
                    this.contentText = "      今天终于康复出院了。历时三年，我的鱼鳞病终于好啦！接下来我会给大家分享我的治疗过程和心路历程，虽然很辛苦但是我也得到了回报！大家一起加油！";
                    this.distanceTime = "1小时前";
                    this.avatarId = R.drawable.ic_avatar1;
                    this.moodId = R.mipmap.ic_emoj_general;
                    this.image1Id = R.drawable.ic_pic1;
                    this.image2Id = R.drawable.ic_pic2;
                }}, new PatientCircle() {{
                    this.nickname = "李强人";
                    this.medicalname = "鱼鳞病";
                    this.majorSymptom = "皮肤干燥";
                    this.contentText = "      第无数次治疗，每天浑身被药的味道包围，也内服不少药，也没有什么明显好转，这样下去什么时候才是个头呢……绝望中。";
                    this.distanceTime = "1小时前";
                    this.avatarId = R.drawable.ic_avatar2;
                    this.moodId = R.mipmap.ic_emoj_bad;
                    this.image1Id = R.drawable.ic_pic3;
                    this.image2Id = R.drawable.ic_pic4;
                }}, new PatientCircle() {{
                    this.nickname = "王小小";
                    this.medicalname = "鱼鳞病";
                    this.majorSymptom = "皮肤潮红";
                    this.contentText = "      现在每一天都越来越有信心啦！看着朋友们都和我一样在与鱼鳞病抗争，我每天都能积极的配合生的治疗听取教授们的建议，三个疗程坚持了下来感觉非常不错！大家可以多多关注王教授的讲座！";
                    this.distanceTime = "1小时前";
                    this.avatarId = R.drawable.ic_avatar3;
                    this.moodId = R.mipmap.ic_emoj_good;
                    this.image1Id = R.drawable.ic_pic5;
                    this.image2Id = R.drawable.ic_pic6;
                }});
    }
}
