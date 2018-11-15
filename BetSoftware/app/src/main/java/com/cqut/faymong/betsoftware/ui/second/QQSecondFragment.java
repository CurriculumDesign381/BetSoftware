package com.cqut.faymong.betsoftware.ui.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;


/**
 * Created by fei on 2018/11/10.
 */

public class QQSecondFragment extends BaseMainFragment {
    public static QQSecondFragment newInstance() {

        Bundle args = new Bundle();

        QQSecondFragment fragment = new QQSecondFragment();
        fragment.setArguments(args);

        return fragment;
    }
 @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d2, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (findChildFragment(ViewPagerFragment.class) == null) {
            loadRootFragment(R.id.fl_second_container, ViewPagerFragment.newInstance());
        }*/
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        // 这里可以不用懒加载,因为Adapter的场景下,Adapter内的子Fragment只有在父Fragment是show状态时,才会被Attach,Create+
    }
}
