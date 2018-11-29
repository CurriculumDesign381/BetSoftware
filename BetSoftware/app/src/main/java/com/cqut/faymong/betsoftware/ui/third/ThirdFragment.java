package com.cqut.faymong.betsoftware.ui.third;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.cqut.faymong.betsoftware.MainActivity;
import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.activity.AddbetAccountActivity;
import com.cqut.faymong.betsoftware.activity.InformationDisplayActivity;
import com.cqut.faymong.betsoftware.activity.loginActivity;
import com.cqut.faymong.betsoftware.activity.updateAccountActivity;
import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.ui.second.QQSecondFragment;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;

import static android.content.ContentValues.TAG;

/**
 * Created by fei on 2018/11/14.
 */

public class ThirdFragment extends BaseMainFragment {

    private LinearLayout l1;
    private  LinearLayout l2;
    private LinearLayout l3;
    private  LinearLayout l6;
    public static ThirdFragment newInstance() {

        Bundle args = new Bundle();

        ThirdFragment fragment = new ThirdFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.person_page, container, false);
        l1 = view.findViewById(R.id.linear1);
        l3= view.findViewById(R.id.linear3);
        l6 = view.findViewById(R.id.linear6);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 可以使用 switch 通过匹配控件id 设置不同的按钮提示不同内容
                // view.getId() 得到点击的控件的id
                switch (v.getId()) {
                    case R.id.linear1:
                        Toast.makeText(getActivity(), "批量登录", Toast.LENGTH_SHORT).show();getdata();
                        break;

                    case R.id.linear3:

                        Intent intent = new Intent(getActivity(),AddbetAccountActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.linear6:
                        Intent intent2 = new Intent(getActivity(),InformationDisplayActivity.class);
                        startActivity(intent2);

                    default:
                        break;
                }
            }
        };
        l1.setOnClickListener(onClickListener);
        l3.setOnClickListener(onClickListener);
        l6.setOnClickListener(onClickListener);


        return view;
    }

            public void getdata() {
        String url = "http://47.106.177.111/get_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", "a123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();

                   /*     jsonObject =  parseJSONWithJSONObject(response);


                        Log.d(TAG, "onStringAvailable: "+list);
                        mRecy.post(new Runnable() {
                            @Override
                            public void run() {
                                List<Chat> chatList = null;
                                try {
                                    chatList = initDatas();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mRecy.setAdapter(mAdapter);
                                //还可以更新其他的控件
                                mAdapter = new ChatAdapter(_mActivity);
                                mAdapter.setDatas(chatList);
                                         *//*   JSONObject obj = new JSONObject().fromObject(sd);*/

                            }
                        });

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
