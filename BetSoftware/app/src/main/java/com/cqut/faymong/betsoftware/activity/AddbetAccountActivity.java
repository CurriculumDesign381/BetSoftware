package com.cqut.faymong.betsoftware.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.List;

import okhttp3.Call;

import static android.content.ContentValues.TAG;

public class AddbetAccountActivity extends AppCompatActivity {
    public String account ;
    public  String betaccount;
    public  String betpassword;
    public String domain;
    public  String betamount;
    private EditText ed_account ;
    private  EditText ed_betaccount;
    private  EditText ed_betpassword;
    private EditText ed_domain;
    private  EditText ed_betamount;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("hello");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addbetaccount);
        initView();
        onclick();

    }


    public void onclick(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 可以使用 switch 通过匹配控件id 设置不同的按钮提示不同内容
                // view.getId() 得到点击的控件的id
                account = ed_account.getText().toString();
                betaccount = ed_betaccount.getText().toString();
               betpassword =ed_betpassword.getText().toString();
               domain = ed_domain.getText().toString();
                betamount = ed_betamount.getText().toString();

                switch (v.getId()) {
                    case R.id.addaccount:
                        Toast.makeText(AddbetAccountActivity.this, "您点击添加button"+account, Toast.LENGTH_SHORT).show();

                        getdata();
                        break;
                    default:
                        break;
                }
            }
        };

        button.setOnClickListener(onClickListener);
    }


    public void initView(){
        ed_account = findViewById(R.id.account);
        ed_betaccount = findViewById(R.id.betaccount);
        ed_betpassword = findViewById(R.id.betpassword);
        ed_betamount = findViewById(R.id.betamount);
        ed_domain = findViewById(R.id.domain);
        button = findViewById(R.id.addaccount);
    }



    public void getdata() {
        String url = "http://47.106.177.111/add_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", account)
                .addParams("betaccount",betaccount)
                .addParams("betpassword",betpassword)
                .addParams("domain",domain)
                .addParams("betamount",betamount)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(AddbetAccountActivity.this, e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(AddbetAccountActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
