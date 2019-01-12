package com.cqut.faymong.betsoftware.activity;

import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cqut.faymong.betsoftware.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class CheckTypeActivity extends AppCompatActivity {
    EditText check_domain ;
    Button check_button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_type);
        initView();
        onclick();

    }
    public void initView(){
        check_domain = (EditText) findViewById(R.id.check_domain);
        check_button = (Button) findViewById(R.id.button_check);

    }

    public void getdata(String check_domainText ) {
 check_domainText =check_domain.getText().toString();

        String url = "http://119.23.45.41:8000/checker";
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("account", oldAccount)
//                .addParams("betaccount", oldBetAccount)
//                .addParams("domain", oldDomain)
//                .addParams("new_account", newAccount)
//                .addParams("new_betaccount", newBetaccount)
//                .addParams("new_betpassword", newPassword)
//                .addParams("new_domain", newDomain)
               .addParams("domain", check_domainText)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toasty.error(CheckTypeActivity.this, "请重新输入域名", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if(response.equals(""))
                            Toasty.success(CheckTypeActivity.this, "暂无该类型", Toast.LENGTH_SHORT, true).show();
                        else
                            Toasty.info(CheckTypeActivity.this, "类型："+response, Toast.LENGTH_SHORT, true).show();


                    }
                });
    }

    public void onclick(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 可以使用 switch 通过匹配控件id 设置不同的按钮提示不同内容
                // view.getId() 得到点击的控件的id
                String check_domainText =check_domain.getText().toString();

                switch (v.getId()) {
                    case R.id.button_check:
                        getdata(check_domainText);
                        break;
                    default:
                        break;
                }
            }
        };

        check_button.setOnClickListener(onClickListener);
    }
}
