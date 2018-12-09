package com.cqut.faymong.betsoftware;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.listener.OnItemClickListener;
import com.cqut.faymong.betsoftware.ui.MainFragment;
import com.cqut.faymong.betsoftware.ui.first.footballmessageFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import okhttp3.Call;

import static android.content.ContentValues.TAG;

public class MainActivity extends SupportActivity  implements BaseMainFragment.OnBackToFirstListener{

    String  ddd="111";
    List<Map<String,Object>> list = new ArrayList<>();
     MediaPlayer mp = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mp =MediaPlayer.create(this, R.raw.hometeam);
        setContentView(R.layout.wechat_activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }

//        new Thread(new MyThread()).start();



    }

    public void getWebSocketData(){
        AsyncHttpClient.getDefaultInstance().websocket(
                "ws://119.23.45.41:9000",// webSocket地址
                "9000",// 端口
                new AsyncHttpClient.WebSocketConnectCallback() {
                    @Override
                    public void onCompleted(Exception ex, WebSocket webSocket) {
                        if (ex != null) {
                            ex.printStackTrace();
                            Log.i("ddd", "onStringAvailable: " );
                            Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        webSocket.send("1");// 发送消息的方法
                        webSocket.send(new byte[10]);
                        webSocket.setStringCallback(new WebSocket.StringCallback() {
                            public void onStringAvailable(String s) {
                                Log.i("ddd", "onStringAvailable: " + s);
                                ddd = s;
                                Log.d("ddd", "onStringAvailable: "+ddd);
                                Toast.makeText(MainActivity.this,"ddd",Toast.LENGTH_SHORT).show();
                            }
                        });
                        webSocket.setDataCallback(new DataCallback() {
                            public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                                System.out.println("I got some bytes!");
                                // note that this data has been read
                                Toast.makeText(MainActivity.this,"I got some bytes!" ,Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this,"网络错误2",Toast.LENGTH_SHORT).show();
                                byteBufferList.recycle();
                            }
                        });
                    }
                });

    }  @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onBackToFirstFragment() {

    }
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情
//                Toasty.error(MainActivity.this, "This is an error toast.", Toast.LENGTH_SHORT, true).show();
            refreshData();
            super.handleMessage(msg);

        }
    };

    class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    public void refreshData(){
        String url = "http://119.23.45.41:8000/penalty";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(e.toString().equals("java.net.ConnectException: Failed to connect to /119.23.45.41:8000"))
                            Toasty.error(MainActivity.this, "请检查网络是否连接", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String res, int id) {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(res);
                        Gson gson = new Gson();
                        /*        jsonObject =  parseJSONWithJSONObject(response);*/
                        JSONArray      jsonArray =  parseJSONWithJSONObject(res);
                        list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                        }.getType());

                        if(list.size()!=0) {

                            Log.d(TAG, "onStringAvailable: " + list);

                            Toasty.info(MainActivity.this, "hometeam:" + list.get(0).get("hometeam").toString() + "  " + list.get(0).get("score").toString() +
                                    "  " + "awayteam:" + list.get(0).get("awayteam").toString()).show();
//
                            mp.start();
                        }

                    }
                });
    }

    private  JSONArray parseJSONWithJSONObject(String jsonData){
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

}
