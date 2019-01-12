package com.cqut.faymong.betsoftware.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

import static android.content.ContentValues.TAG;

public class AddbetAccountActivity extends AppCompatActivity {
    public String account ;
    public  String betaccount;
    public  String betpassword;
    public String domain;
    public  String betamount;
    private  EditText ed_betaccount;
    private  EditText ed_betpassword;
    private EditText ed_domain;
    private  EditText ed_betamount;
    private Button button;
    private String status ="0";
    private String  dType;

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
                SharedPreferences share = AddbetAccountActivity.this.getSharedPreferences("account",MODE_PRIVATE);
                account= share.getString("account",null);
                betaccount = ed_betaccount.getText().toString();
               betpassword =ed_betpassword.getText().toString();
               domain = ed_domain.getText().toString();
                betamount = ed_betamount.getText().toString();

                switch (v.getId()) {
                    case R.id.addaccount:
                        checkDomain(domain);

                        break;
                    default:
                        break;
                }
            }
        };

        button.setOnClickListener(onClickListener);
    }


    public void initView(){

        ed_betaccount = findViewById(R.id.betaccount);
        ed_betpassword = findViewById(R.id.betpassword);
        ed_betamount = findViewById(R.id.betamount);
        ed_domain = findViewById(R.id.domain);
        button = findViewById(R.id.addaccount);
    }
    public void checkDomain(String check_domainText ) {
        check_domainText =ed_domain.getText().toString();

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
                        Toasty.error(AddbetAccountActivity.this, "请重新填写网址", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if(response.equals(""))
                            Toasty.success(AddbetAccountActivity.this, "暂无该网址类型", Toast.LENGTH_SHORT, true).show();
                        else {
//                            Toasty.info(AddbetAccountActivity.this, "网址类型为：" + response, Toast.LENGTH_SHORT, true).show();
                            status = "1";
                            dType  = response;
                            if(status.equals("1"))
                                getdata();
                        }


                    }
                });
    }

    public void getdata() {
        String url = "http://119.23.45.41:8080/add_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", account)
                .addParams("betaccount",betaccount)
                .addParams("betpassword",betpassword)
                .addParams("domain",domain)
                .addParams("betamount",betamount)
                .addParams("dtype",dType)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toasty.error(AddbetAccountActivity.this, "请检查信息是否填写正确", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        if(Integer.parseInt(response)==1)
                                        Toasty.success(AddbetAccountActivity.this, "添加成功", Toast.LENGTH_SHORT, true).show();
                         else
                            Toasty.info(AddbetAccountActivity.this, "添加失败", Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

}
