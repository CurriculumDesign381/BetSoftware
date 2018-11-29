package com.cqut.faymong.betsoftware;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.ui.MainFragment;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import okhttp3.Call;

public class MainActivity extends SupportActivity  implements BaseMainFragment.OnBackToFirstListener{

      String  ddd="111";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_activity_main);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
    }

    public void getWebSocketData(){
        AsyncHttpClient.getDefaultInstance().websocket(
                "ws://47.106.177.111:9000",// webSocket地址
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
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

   /* *//**
     * 第一种解决办法 通过监听keyUp
     * @param keyCode
     * @param event
     * @return
     *//*


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }

        return super.onKeyUp(keyCode, event);
    }*/



}
