package com.cqut.faymong.betsoftware.base;

import android.content.Context;
import android.widget.Toast;


import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.ui.second.QQSecondFragment;

import me.yokeyword.fragmentation.SupportFragment;


public abstract class BaseMainFragment extends SupportFragment {
    protected OnBackToFirstListener _mBackToFirstListener;
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBackToFirstListener) {
            _mBackToFirstListener = (OnBackToFirstListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBackToFirstListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _mBackToFirstListener = null;
    }





    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public interface OnBackToFirstListener {
        void onBackToFirstFragment();
    }

}
