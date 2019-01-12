package com.cqut.faymong.betsoftware.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.entity.TabMessage;
import com.cqut.faymong.betsoftware.event.TabSelectedEvent;
import com.cqut.faymong.betsoftware.ui.first.FirstFragment;
import com.cqut.faymong.betsoftware.ui.fourth.FourthFragment;
import com.cqut.faymong.betsoftware.ui.second.QQSecondFragment;
import com.cqut.faymong.betsoftware.ui.third.ThirdFragment;
import com.cqut.faymong.betsoftware.view.BottomBar;
import com.cqut.faymong.betsoftware.view.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by YoKeyword on 16/6/30.
 *  */
public class MainFragment extends SupportFragment {

    private static final int REQ_MSG = 10;
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;

    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wechat_fragment_main, container, false);
//        initView(view);
        initViewSecond(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(FirstFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = QQSecondFragment.newInstance();
            mFragments[FOURTH] = ThirdFragment.newInstance();
            mFragments[THIRD] = FourthFragment.newInstance();


            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[FOURTH],
                    mFragments[THIRD]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题
            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(QQSecondFragment.class);
            mFragments[FOURTH]= findFragment(ThirdFragment.class);
            mFragments[THIRD] = findFragment(FourthFragment.class);

        }
    }



    private void initView(View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);

        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.drawable.livebroadcast_demo, getString(R.string.msg)))
                .addItem(new BottomBarTab(_mActivity, R.drawable.betrecord, "下注"))
                .addItem(new BottomBarTab(_mActivity,R.drawable.mine,"我"));


        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
    }

private void initViewSecond(View view){
    com.roughike.bottombar.BottomBar bottomBar = (com.roughike.bottombar.BottomBar) view.findViewById(R.id.bottomBar);
    bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
        @Override
        public void onTabSelected(@IdRes int tabId) {
            showHideFragment(mFragments[TabMessage.get(tabId, true)]);
        }
    });
}

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REQ_MSG && resultCode == RESULT_OK) {

        }
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }
}
