package com.cqut.faymong.betsoftware.ui.second;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import okhttp3.Call;

import static android.content.ContentValues.TAG;


/**
 * Created by fei on 2018/11/10.
 */

public class QQSecondFragment extends BaseMainFragment implements SwipeRefreshLayout.OnRefreshListener{
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;
    private boolean mInAtTop = true;
    private int mScrollTotal;
    private String competitionInfor;
    private ChatAdapter mAdapter;
    private  String eventid="0";
    private  JSONObject jsonObject;
    List<Map<String,Object>> list = new ArrayList<>();
    public static QQSecondFragment newInstance() {
        Bundle args = new Bundle();
        QQSecondFragment fragment = new QQSecondFragment();
        fragment.setArguments(args);
        return fragment;
    }
 @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     View view = inflater.inflate(R.layout.bettedteam, container, false);
     initView(view);
     getdata();
/*     getPenaltykickData();*/
     return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout1);
        mRecy = (RecyclerView) view.findViewById(R.id.recy1);
/*        EventBusActivityScope.getDefault(_mActivity).register(this);*/
        mToolbar.setTitle(R.string.betteam);
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
        List<Chat> chatList = null;
        try {
            chatList = initDatas();
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
    }

    public List<Chat> initDatas() throws JSONException {
        List<Chat> msgList = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            Chat chat = new Chat();
            chat.name = list.get(i).get("league").toString();
            chat.message = list.get(i).get("retimeset").toString();
            chat.score = list.get(i).get("score").toString();
            chat.hometeam = list.get(i).get("hometeam").toString();
            chat.awayteam = list.get(i).get("awayteam").toString();
         /*   chat.evenid = list.get(i).get("eventid").toString();*/
            msgList.add(chat);
        }
        return  msgList;
    }


    public void getdata() {
        String url = "http://119.23.45.41:8080/get_bet_record.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", "888")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(response);
                        Gson gson = new Gson();

             /*        jsonObject =  parseJSONWithJSONObject(response);*/
                  JSONArray      jsonArray =  parseJSONWithJSONObject(response);

                      list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                      }.getType());
                        if(list.size()!=0) {
                      Log.d(TAG, "onStringAvailable: " + list);
                      mRecy.post(new Runnable() {
                          @Override
                          public void run() {
                              List<Chat> chatList = null;
                              try {
                                  chatList = initDatas();
                                  if(chatList!=null)
                                      Toast.makeText(getActivity(),  "刷新成功", Toast.LENGTH_SHORT).show();
                                  else
                                      Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                              mRecy.setAdapter(mAdapter);
                              //还可以更新其他的控件
                              mAdapter = new ChatAdapter(_mActivity);
                              mAdapter.setDatas(chatList);
                              /*   JSONObject obj = new JSONObject().fromObject(sd);*/

                          }
                      });
                  }

                    }


                });
    }



    public void getPenaltykickData(){
        String url = "http://119.23.45.41:8080/penalty";
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(getActivity(), e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(response);
                        Gson gson = new Gson();

                        /*        jsonObject =  parseJSONWithJSONObject(response);*/
                        JSONArray      jsonArray =  parseJSONWithJSONObject(response);
                        list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                        }.getType());

                        Log.d(TAG, "onStringAvailable: "+list);
                        mRecy.post(new Runnable() {
                            @Override
                            public void run() {
                                List<Chat> chatList = null;
                                try {
                                    chatList = initDatas();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
    @Override
    public void onRefresh() {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
           getdata();
//                refreshData();
                    mRefreshLayout.setRefreshing(false);
            }
        }, 2500);

    }

    public void refreshData(){
        String url = "http://119.23.45.41:8080/get_bet_record.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", "888")
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
                        if(list.size()!=0) {
                            list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                            }.getType());

                            Log.d(TAG, "onStringAvailable: " + list);
                            mRecy.post(new Runnable() {
                                @Override
                                public void run() {
                                    List<Chat> chatList = null;

                                    try {
                                        chatList = initDatas();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (chatList != null)
                                        Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getActivity(), "刷新失败", Toast.LENGTH_SHORT).show();

                                    mRecy.setAdapter(mAdapter);
                                    //还可以更新其他的控件
                                    mAdapter = new ChatAdapter(_mActivity);
                                    mAdapter.setDatas(chatList);
                                    /*   JSONObject obj = new JSONObject().fromObject(sd);*/
                                }
                            });
                        }
                    }
                });
    }
}
