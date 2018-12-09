package com.cqut.faymong.betsoftware.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bin.david.form.data.table.TableData;
import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.entity.bet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class updateAccountActivity extends AppCompatActivity {

    private TextView account;
    private TextView betaccount;
    private TextView betpassword;
    private  TextView domain;
    private TextView betamount;
    private String newAccount;
    private String newBetaccount;
    private String newPassword;
    private String newDomain;
    private String newBetamount;
    public  String oldAccount;
    public  String oldBetAccount;
    public  String oldDomain;
    private Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_update);
        initview();
        Intent intent = getIntent();//获取传来的intent对象
        String accountdata = intent.getStringExtra("supid");
        String betaccountdata = intent.getStringExtra("account");
        String betpassworddata = intent.getStringExtra("password");
        String domaindata = intent.getStringExtra("domain");
        String betamountdata = intent.getStringExtra("betamount");

        oldAccount = accountdata;
        oldBetAccount = betaccountdata;
        oldDomain = domaindata;

        account.setText(accountdata);
        betaccount.setText(betaccountdata);
        betpassword.setText(betpassworddata);
        domain.setText(domaindata);
        betamount.setText(betamountdata);



        onclick();

    }
    public void onclick(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 可以使用 switch 通过匹配控件id 设置不同的按钮提示不同内容
                // view.getId() 得到点击的控件的id


                newAccount = account.getText().toString();
                newBetaccount = betaccount.getText().toString();
                newPassword = betpassword.getText().toString();
                newDomain = domain.getText().toString();
                newBetamount = betamount.getText().toString();
                switch (v.getId()) {

                    case R.id.updateaccount:
                        Toast.makeText(updateAccountActivity.this, "您点击添加button"+account, Toast.LENGTH_SHORT).show();
                        getdata();


                        break;
                    default:
                        break;
                }
            }
        };

        update.setOnClickListener(onClickListener);
    }

    public void getdata() {


        String url = "http://119.23.45.41:8080/alter_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", oldAccount)
                .addParams("betaccount", oldBetAccount)
                .addParams("domain", oldDomain)
                .addParams("new_account", newAccount)
                .addParams("new_betaccount", newBetaccount)
                .addParams("new_betpassword", newPassword)
                .addParams("new_domain", newDomain)
                .addParams("new_betamount", newBetamount)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(updateAccountActivity.this, e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(updateAccountActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        if(Integer.parseInt(response)==1)
                        Toast.makeText(updateAccountActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(updateAccountActivity.this, "修改失败", Toast.LENGTH_SHORT).show();


                    }
                });
    }
public void initview(){
    account = findViewById(R.id.account);
    update = findViewById(R.id.updateaccount);
    betaccount = findViewById(R.id.betaccount);
    betpassword = findViewById(R.id.betpassword);
    domain = findViewById(R.id.domain);
    betamount = findViewById(R.id.betamount);
}

}
