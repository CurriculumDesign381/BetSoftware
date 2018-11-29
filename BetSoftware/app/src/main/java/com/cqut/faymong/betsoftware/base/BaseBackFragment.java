package com.cqut.faymong.betsoftware.base;

import android.support.v7.widget.Toolbar;
import android.view.View;


import com.cqut.faymong.betsoftware.R;

import me.yokeyword.fragmentation.SupportFragment;





public class BaseBackFragment extends SupportFragment {

    protected void initToolbarNav(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.return_icon);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });
    }
}
