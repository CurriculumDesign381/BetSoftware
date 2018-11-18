package com.cqut.faymong.betsoftware.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.cqut.faymong.betsoftware.MainActivity;
import com.cqut.faymong.betsoftware.R;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class loginActivity extends SupportActivity {

   /* @BindView(R.id.part_second)
    LinearLayout l2;
    @BindView(R.id.part_third)
    LinearLayout l3;*/

    private Handler handler2 = new Handler();
private  static final String TAG = "loginActivity";

    private EditText mUsername;
    private EditText mPassWord;
     private Button mButton;

    private String account ;
    private String password;
    private int status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        initView();
        login();
    }

    public void initView(){
         mButton=(Button)findViewById(R.id.sign_login);
        mUsername=(EditText)findViewById(R.id.admin);
        mPassWord=(EditText)findViewById(R.id.login_password);

    }
    public void getdata(){
        String url = "http://47.106.177.111/login.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", account)
                .addParams("password", password)
                .build()
                .execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(loginActivity.this,e.toString()+"error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(loginActivity.this,response,Toast.LENGTH_SHORT).show();
                        status = Integer.parseInt(response);
                        if(status==1){
                            Log.d(TAG, "success: "+mUsername.getText().toString());
                            Toast.makeText(loginActivity.this,  "登录成功", Toast.LENGTH_SHORT).show();

                            status = 1;
                            keepLoginStatus(status);
                            Intent intent = new Intent(loginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Log.d(TAG, "mUsername: "+mUsername.getText().toString());
                            Toast.makeText(loginActivity.this, "密码或账户名错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void login(){


   mButton.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
          account= mUsername.getText().toString();
          password = mPassWord.getText().toString();
         /*  getLoginData();*/
           getdata();
       }
   });
    }


public  void  keepLoginStatus(int status){
    SharedPreferences share = loginActivity.this.getSharedPreferences("loginStatus", MODE_PRIVATE);
    SharedPreferences.Editor edit = share.edit();//编辑文件
    edit.putInt("status",status);
    edit.commit();
}

public void getLoginData(){
    mUsername=(EditText)findViewById(R.id.admin);
    mPassWord=(EditText)findViewById(R.id.login_password);
account = mUsername.getText().toString();
password = mPassWord.getText().toString();

    RequestBody requestBody = new FormBody.Builder()
            .add("taskID",account)
            .add("isReceive",password)
            .build();
    OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
    try {
        Request request = new Request.Builder()
                    /*192.168.43.92*/
                .url("http://47.106.177.111/login.php")//请求接口。如果需要传参拼接到接口后面
                .post(requestBody)
                .build();//创建Request 对象

        Response response = null;
        response = client.newCall(request).execute();//得到Response 对象
        Log.d("LoginActivity", response.toString());

        if (response.isSuccessful()) {
            //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
            String BackStageString = response.body().string();
            Log.d("LoginActivity", "doLogin: "+BackStageString);
        }
    }  catch (SocketTimeoutException e) {
        client.dispatcher().cancelAll();
        client.connectionPool().evictAll();
        //TODO: 重新请求
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



}
