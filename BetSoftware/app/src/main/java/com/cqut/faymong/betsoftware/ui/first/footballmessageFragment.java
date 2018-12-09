package com.cqut.faymong.betsoftware.ui.first;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.adapter.FootballMessageAdapter;
import com.cqut.faymong.betsoftware.base.BaseBackFragment;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.entity.CompetitionInfor;
import com.cqut.faymong.betsoftware.listener.OnItemClickListener;
import com.cqut.faymong.betsoftware.ui.MainFragment;
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
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;
import okhttp3.Call;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fei on 2018/11/16.
 */

public class footballmessageFragment extends BaseBackFragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final String ARG_MSG = "arg_msg";
    private Chat mChat;
    private RecyclerView mRecy;
    private FootballMessageAdapter fAdapter;
    private  String eventID;
    private Toolbar mToolbar;
    List<Map<String,Object>> list =  new ArrayList<>();
    private   JSONArray jsonArray;
    private TextView hometeam;
    private  TextView awayteam;
    private  TextView scores;
    private SwipeRefreshLayout mRefreshLayout;
    public static footballmessageFragment newInstance(Chat msg) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_MSG, msg);
        footballmessageFragment fragment = new footballmessageFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences share = getActivity().getSharedPreferences("loginStatus",MODE_PRIVATE);
        eventID= share.getString("eventid",null);
        mChat = getArguments().getParcelable(ARG_MSG);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_detail, container, false);
      /*  initView(view);*/

        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        hometeam = (TextView)view.findViewById(R.id.hometeam);
        awayteam = (TextView)view.findViewById(R.id.awayteam);
        scores = (TextView)view.findViewById(R.id.tv_time);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout1);
        mRefreshLayout.setOnRefreshListener(this);
        hometeam.setText(mChat.hometeam);
        awayteam.setText(mChat.awayteam);
        scores.setText(mChat.score);
        mToolbar.setTitle(mChat.name);
        initToolbarNav(mToolbar);

        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mRecy.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecy.setHasFixedSize(true);
        fAdapter = new FootballMessageAdapter(_mActivity);
        mRecy.setAdapter(fAdapter);
//    getWebSocketData();
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
               getFootballDetailMessage();
                mRefreshLayout.setRefreshing(false);
            }
        }, 250);
        return view;
    }
    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
    }

    public void getWebSocketData(){
        try {
            //gameDetail:eventid
            AsyncHttpClient.getDefaultInstance().websocket(
                    "ws://119.23.45.41:9000",// webSocket地址
                    "9000",// 端口
                    new AsyncHttpClient.WebSocketConnectCallback() {
                        @Override
                        public void onCompleted(Exception ex, WebSocket webSocket) {
                            if (ex != null) {
                                ex.printStackTrace();
                                Log.i("ddd", "onStringAvailable: " );
                                return;
                            }
                            String Message = "gameDetail:"+eventID;
                            Log.d(TAG, "onCompleted: "+Message);
                            webSocket.send(Message);// 发送消息的方法
                            webSocket.send(new byte[10]);
                            webSocket.setStringCallback(new WebSocket.StringCallback() {
                                public void onStringAvailable(String s) {
                                    Log.i("ddd", "onStringAvailable: "+s );
                                    Gson gson = new Gson();
                                  jsonArray =  parseJSONWithJSONObject(s);
                                    mRecy.post(new Runnable() {
                                                   @Override
                                                   public void run() {
                                               fAdapter.clear();
                                    for(int i=0;i<jsonArray.length();i++){
                                        try{
                                             mRecy.setAdapter(fAdapter);
                                            fAdapter.addMsg(new CompetitionInfor(jsonArray.get(i).toString()));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                                   }
                                    });
                                   /* list = gson.fromJson(j
                                   sonArray.toString(), new TypeToken<List<Map<String, Object>>>(){}.getType());*/
                                    Log.d("ddd", "onStringAvailable: " );
                                }
                            });
                            webSocket.setDataCallback(new DataCallback() {
                                public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                                    System.out.println("I got some bytes!");
                                    // note that this data has been read
                            /*    Toast.makeText(MainActivity.this,"I got some bytes!" ,Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this,"网络错误2",Toast.LENGTH_SHORT).show();*/
                                    byteBufferList.recycle();
                                }
                            });
                        }
                    });
        }
        catch (Exception e){
            Log.d(TAG, "getWebSocketData: error");
        }
    }

    public void  getFootballDetailMessage(){
        String url = "http://119.23.45.41:8000/gameDetail";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("eventid", eventID)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(e.toString().equals("java.net.ConnectException: Failed to connect to /119.23.45.41:8000"))
                            Toasty.error(getActivity(), "请检查网络是否连接", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String res, int id) {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(res);
                        Gson gson = new Gson();
                        /*        jsonObject =  parseJSONWithJSONObject(response);*/


                        Log.d(TAG, "onStringAvailable: "+list);

                        jsonArray =  parseJSONWithJSONObject(res);
                        if(jsonArray!=null&&!jsonArray.equals("{}"))
                            Toasty.success(getActivity(), "刷新成功", Toast.LENGTH_SHORT, true).show();
                        else
                            Toasty.info(getActivity(), "刷新失败", Toast.LENGTH_SHORT, true).show();
                        mRecy.post(new Runnable() {
                            @Override
                            public void run() {
                                fAdapter.clear();
                                for(int i=0;i<jsonArray.length();i++){

                                    try{
                                        mRecy.setAdapter(fAdapter);
                                        fAdapter.addMsg(new CompetitionInfor(jsonArray.get(i).toString()));
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                });

    }
    private JSONArray parseJSONWithJSONObject(String jsonData){
        JSONArray jsonArray = null;
        try{
            JSONObject object = new JSONObject(jsonData);
            jsonArray = object.getJSONArray("live");
            Log.d("football", "parseJSONWithJSONObject: "+  jsonArray.get(0));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideSoftInput();
    }



    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
//                getWebSocketData();
                getFootballDetailMessage();
                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }
}


