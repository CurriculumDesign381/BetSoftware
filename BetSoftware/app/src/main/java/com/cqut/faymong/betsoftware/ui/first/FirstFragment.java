package com.cqut.faymong.betsoftware.ui.first;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.cqut.faymong.betsoftware.MainActivity;
import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.activity.loginActivity;
import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.event.TabSelectedEvent;
import com.cqut.faymong.betsoftware.listener.OnItemClickListener;
import com.cqut.faymong.betsoftware.ui.MainFragment;
import com.cqut.faymong.betsoftware.ui.second.QQSecondFragment;
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

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import okhttp3.Call;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fei on 2018/11/14.
 */

public class FirstFragment extends BaseMainFragment implements SwipeRefreshLayout.OnRefreshListener{
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;
    private boolean mInAtTop = true;
    private int mScrollTotal;
    private String competitionInfor;
    private ChatAdapter mAdapter;
    private  String eventid;
    public int result;
    List<Map<String,Object>> list = new ArrayList<>();
    public static FirstFragment newInstance() {

        Bundle args = new Bundle();

        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.qq_tab_first, container, false);

        initView(view);


        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        EventBusActivityScope.getDefault(_mActivity).register(this);

        mToolbar.setTitle(R.string.home);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);

        mRefreshLayout.setOnRefreshListener(this);

        mRecy.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecy.setHasFixedSize(true);
        final int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, getResources().getDisplayMetrics());
        mRecy.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(0, 0, 0, space);
            }
        });

        mAdapter = new ChatAdapter(_mActivity);
        mRecy.setAdapter(mAdapter);
        List<Chat> chatList = initDatas();


        mAdapter.setDatas(chatList);

        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollTotal += dy;
                if (mScrollTotal <= 0) {
                    mInAtTop = true;
                } else {
                    mInAtTop = false;
                }
            }
        });


        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshData();
                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);

    }

    public  void  keepLoginStatus(String  eventid){
        SharedPreferences share = getActivity().getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();//编辑文件
        edit.putString("eventid",eventid);
        edit.commit();
    }

    public int getWebSocketData(){
         result =0;
        try {
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
                            webSocket.send("game");// 发送消息的方法
                            webSocket.send(new byte[10]);
                            webSocket.setStringCallback(new WebSocket.StringCallback() {
                                public void onStringAvailable(String s) {
                                    Gson gson = new Gson();
                                    Logger.addLogAdapter(new AndroidLogAdapter());
                                    Logger.json(s);
                                    Log.i("ddd", "onStringAvailable: " + s);
                                    if(!s.equals("{}")) {
                                        JSONArray jsonArray = parseJSONWithJSONObject(s);
                                        list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                                        }.getType());
                                        Log.d(TAG, "onStringAvailable: " + list);
                                        competitionInfor = s;
                                        result = 1;
                               /*         Toast.makeText(getActivity(),"刷新成功" ,Toast.LENGTH_SHORT).show();*/
                                        mRecy.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                    List<Chat> chatList = initDatas();
                                                    mRecy.setAdapter(mAdapter);
                                                    //还可以更新其他的控件
                                                    chatList = initDatas();
                                                    mAdapter.setDatas(chatList);
                                         /*   JSONObject obj = new JSONObject().fromObject(sd);*/
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"亲，没有数据哦！" ,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            webSocket.setDataCallback(new DataCallback() {
                                public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                                    System.out.println("I got some bytes!");
                                    // note that this data has been read

                                    byteBufferList.recycle();
                                }
                            });
                        }
                    });

        }
        catch (Exception e){
            Log.d(TAG, "getWebSocketData: error");
        }
return result;

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

  /*  private List<Chat> initDatas() {
        List<Chat> msgList = new ArrayList<>();
        String[] name = new String[]{"印果阿超", "印果阿超", "国际友谊", "俄甲", "伊朗超"};
        String[] chats = new String[]{"19：00", "19：30", "20：00", "20：30", "20：30"};
        String[] scores = new String[]{"0-1", "0-0", "1-1", "3-4", "5-5"};
        for (int i = 0; i < 15; i++) {
            int index = (int) (Math.random() * 5);
            Chat chat = new Chat();
            chat.name = name[index];
            chat.message = chats[index];
            chat.score = scores[index];
            msgList.add(chat);
        }
        return msgList;
    }*/

    public List<Chat> initDatas(){
        List<Chat> msgList = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            Chat chat = new Chat();
            chat.name = list.get(i).get("league").toString();
            chat.message = list.get(i).get("retimeset").toString();
            chat.score = list.get(i).get("score").toString();
            chat.hometeam = list.get(i).get("hometeam").toString();
            chat.awayteam = list.get(i).get("awayteam").toString();
            chat.evenid = list.get(i).get("eventid").toString();
            msgList.add(chat);
        }
        return  msgList;
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
            /*  int result =   getWebSocketData();*/
                refreshData();

                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }


    /**
     * Reselected Tab
     */
    @Subscribe
    public void onTabSelectedEvent(TabSelectedEvent event) {
        if (event.position != MainFragment.FIRST) return;

        if (mInAtTop) {
            mRefreshLayout.setRefreshing(true);
            onRefresh();
        } else {
            scrollToTop();
        }
    }

    private void scrollToTop() {
        mRecy.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBusActivityScope.getDefault(_mActivity).unregister(this);
    }

public void getdata(){
    String url = "http://119.23.45.41:8000/game";
    OkHttpUtils
            .get()
            .url(url)
            .build()
            .execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if(e.toString().equals("java.net.ConnectException: Failed to connect to /119.23.45.41:8000"))
                        Toast.makeText(getActivity(), "请检查网络是否连接", Toast.LENGTH_SHORT).show();
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

                    Log.d(TAG, "onStringAvailable: "+list);
                    mRecy.post(new Runnable() {
                        @Override
                        public void run() {
                            List<Chat> chatList = null;

                            chatList = initDatas();
//                            if(chatList!=null)
//                                Toast.makeText(getActivity(),  "刷新成功", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();

                            mRecy.setAdapter(mAdapter);
                            //还可以更新其他的控件
                            mAdapter = new ChatAdapter(_mActivity);
                            mAdapter.setDatas(chatList);
                            /*   JSONObject obj = new JSONObject().fromObject(sd);*/
                        }
                    });
                }
            });
}

    public void refreshData(){
        String url = "http://119.23.45.41:8000/game";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(e.toString().equals("java.net.ConnectException: Failed to connect to /119.23.45.41:8000"))
                        Toast.makeText(getActivity(), "请检查网络是否连接", Toast.LENGTH_SHORT).show();
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

                        Log.d(TAG, "onStringAvailable: "+list);
                        mRecy.post(new Runnable() {
                            @Override
                            public void run() {
                                List<Chat> chatList = null;

                                    chatList = initDatas();
                                    if(chatList!=null)
                                        Toast.makeText(getActivity(),  "刷新成功", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();


                                //还可以更新其他的控件
                                mAdapter = new ChatAdapter(_mActivity);
                                mRecy.setAdapter(mAdapter);
                                mAdapter.setDatas(chatList);
                                /*   JSONObject obj = new JSONObject().fromObject(sd);*/

                                mAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position, View view, RecyclerView.ViewHolder vh) {
                                        // 因为启动的MsgFragment是MainFragment的兄弟Fragment,所以需要MainFragment.start()

                                        // 也可以像使用getParentFragment()的方式,拿到父Fragment来操作 或者使用 EventBusActivityScope
                                        Chat chat = mAdapter.getMsg(position);
                                        eventid = chat.evenid;
                                        keepLoginStatus(eventid);
                                        ((MainFragment) getParentFragment()).startBrotherFragment(footballmessageFragment.newInstance(mAdapter.getMsg(position)));


                                    }
                                });
                            }
                        });
                    }
                });
    }
}