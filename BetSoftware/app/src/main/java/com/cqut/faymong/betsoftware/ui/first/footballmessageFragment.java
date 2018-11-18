package com.cqut.faymong.betsoftware.ui.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.adapter.FootballMessageAdapter;
import com.cqut.faymong.betsoftware.base.BaseBackFragment;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.entity.CompetitionInfor;

import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * Created by fei on 2018/11/16.
 */

public class footballmessageFragment extends BaseBackFragment {
    private static final String ARG_MSG = "arg_msg";
    private Chat mChat;
    private RecyclerView mRecy;
    private FootballMessageAdapter fAdapter;
    public static footballmessageFragment newInstance(Chat msg) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, msg);
        footballmessageFragment fragment = new footballmessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChat = getArguments().getParcelable(ARG_MSG);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_detail, container, false);
      /*  initView(view);*/
        SwipeBackFragment swipeBackFragment = new SwipeBackFragment();
        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        return view;
    }
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        // 入场动画结束后执行  优化,防动画卡顿

        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mRecy.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecy.setHasFixedSize(true);
        fAdapter = new FootballMessageAdapter(_mActivity);
        mRecy.setAdapter(fAdapter);
        fAdapter.addMsg(new CompetitionInfor("君不见，高堂明镜悲白发，朝如青丝暮成雪。"));


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideSoftInput();
    }

}


