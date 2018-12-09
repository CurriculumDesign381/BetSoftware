package com.cqut.faymong.betsoftware.ui.third;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Toast;


import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.activity.AddbetAccountActivity;
import com.cqut.faymong.betsoftware.activity.InformationDisplayActivity;

import com.cqut.faymong.betsoftware.base.BaseMainFragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;


import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fei on 2018/11/14.
 */

public class ThirdFragment extends BaseMainFragment {

    private LinearLayout l1;
    private  LinearLayout l2;
    private LinearLayout l3;
    private  LinearLayout l6;
    private  LinearLayout l7;
    private  String json;
    public  String account;
    List<Map<String ,Object>> list;

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
        l7 = view.findViewById(R.id.linear7);

        getdata();
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 可以使用 switch 通过匹配控件id 设置不同的按钮提示不同内容
                // view.getId() 得到点击的控件的id
                switch (v.getId()) {
                    case R.id.linear1:
                        for(int i =0; i<list.size();i++)
                            allLogin(list.get(i).get("supid").toString(),list.get(i).get("domain").toString(),list.get(i).get("account").toString(),list.get(i).get("password").toString(),list.get(i).get("betamount").toString());
                        break;
                    case R.id.linear3:
                        Intent intent = new Intent(getActivity(),AddbetAccountActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.linear6:
                        Intent intent2 = new Intent(getActivity(),InformationDisplayActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.linear7:
                        for(int i =0;i<list.size();i++)
                        allCancel(list.get(i).get("domain").toString(),list.get(i).get("account").toString());
                        break;
                    default:
                        break;
                }
            }
        };
        l1.setOnClickListener(onClickListener);
        l3.setOnClickListener(onClickListener);
        l6.setOnClickListener(onClickListener);
        l7.setOnClickListener(onClickListener);
        return view;
    }

    private JSONArray parseJSONWithJSONObject(String jsonData){
        JSONArray jsonArray = null;
        try{

            JSONObject object = new JSONObject(jsonData);
            jsonArray = object.getJSONArray("db");
        }
        catch (Exception e){
            e.printStackTrace();

        }
        return jsonArray;
    }


     public void getdata() {
         SharedPreferences share = getActivity().getSharedPreferences("account",MODE_PRIVATE);
         account= share.getString("account",null);
        String url = "http://119.23.45.41:8080/get_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", account)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        JSONArray jsonArray = parseJSONWithJSONObject(response);
                        if(!response.equals("{}")){
                        list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {}.getType());
                        json = response;
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.d(list);
                        }
                            }
                        });

                    }



   public void allLogin(String supid,String domain,String username, String password, String betamount){
     String url = "http://119.23.45.41:8000/login";
     OkHttpUtils
            .get()
            .url(url)
             .addParams("supid",supid)
            .addParams("domain", domain)
            .addParams("username",username)
            .addParams("password",password)
            .addParams("betamount",betamount)
            .build()
            .execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(getActivity(), e.toString() + "error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {

                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

                }
            });
}

public  void  allCancel(String domain, String username){

    String url = "http://119.23.45.41:8000/logout";
    OkHttpUtils
            .get()
            .url(url)
            .addParams("domain", domain)
            .addParams("username",username)
            .build()
            .execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(getActivity(), e.toString() + "error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
               /*     allLogin(response);*/
                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();

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
