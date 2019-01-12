package com.cqut.faymong.betsoftware.ui.second;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.activity.InformationDisplayActivity;
import com.cqut.faymong.betsoftware.adapter.ChatAdapter;
import com.cqut.faymong.betsoftware.adapter.RecordAdapter;
import com.cqut.faymong.betsoftware.base.BaseMainFragment;
import com.cqut.faymong.betsoftware.entity.Chat;
import com.cqut.faymong.betsoftware.entity.Record;
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

import es.dmoral.toasty.Toasty;
import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import okhttp3.Call;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;


/**
 * Created by fei on 2018/11/10.
 */
//下注历史记录
public class QQSecondFragment extends BaseMainFragment implements SwipeRefreshLayout.OnRefreshListener{
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecy;
    private boolean mInAtTop = true;
    private int mScrollTotal;
    private String competitionInfor;
    private RecordAdapter mAdapter;
    private  String eventid="0";
    private  JSONObject jsonObject;
//    private Button deleteRecord;
    List<Map<String,Object>> list = new ArrayList<>();
    private ImageView deleteRecord;
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
     initAlert();

/*     getPenaltykickData();*/
     return view;
    }
    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_betRecord);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout1);
        mRecy = (RecyclerView) view.findViewById(R.id.recy1);
/*        EventBusActivityScope.getDefault(_mActivity).register(this);*/
        mToolbar.setTitle(R.string.betteam);
        deleteRecord = view.findViewById(R.id.deleteRecord);
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
        mAdapter = new RecordAdapter(_mActivity);
        mRecy.setAdapter(mAdapter);
        List<Record> chatList = null;
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

        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getdata();
//                refreshData();
                mRefreshLayout.setRefreshing(false);
            }
        }, 250);
    }

    public List<Record> initDatas() throws JSONException {
        List<Record> msgList = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            Record record = new Record();
            record.name = list.get(i).get("league").toString();
            record.message = list.get(i).get("retimeset").toString();
            record.score = list.get(i).get("score").toString();
            record.hometeam = list.get(i).get("hometeam").toString();
            record.awayteam = list.get(i).get("awayteam").toString();
         /*   chat.evenid = list.get(i).get("eventid").toString();*/
            if(list.get(i).get("domain").toString().equals("https://www.hg912b.com"))
                record.domain ="A8";
            else if(list.get(i).get("domain").toString().equals("https://www.hgxin99.com"))
                record.domain ="A5-1";
            else if(list.get(i).get("domain").toString().equals("https://www.73567tg20.com"))
                record.domain ="A15";
            else if(list.get(i).get("domain").toString().equals("https://www.12777888.com"))
                record.domain ="A22";
            else
                record.domain ="A3";

            record.betaccount =list.get(i).get("account").toString();

            msgList.add(record);
        }
        return  msgList;
    }

    public void getdata() {
        String loginAccount;
        SharedPreferences share = getActivity().getSharedPreferences("account",MODE_PRIVATE);
        loginAccount = share.getString("account","account");
        String url = "http://119.23.45.41:8080/get_bet_record.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", loginAccount)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toasty.error(getActivity(), "请检查网络是否连接", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(response);
                        Gson gson = new Gson();

             /*        jsonObject =  parseJSONWithJSONObject(response);*/
                  JSONArray      jsonArray =  parseJSONWithJSONObject(response);
                  Logger.d("list1"+list);
                     list.clear();
                      if(jsonArray!=null)
                      list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                      }.getType());
                        Logger.d("list2"+list);
                        if(list.size()!=0) {
                      Log.d(TAG, "onStringAvailable: " + list);
                      mRecy.post(new Runnable() {
                          @Override
                          public void run() {
                              List<Record> chatList = null;
                              try {
                                  chatList = initDatas();
                                  if(chatList!=null)
                                      Toasty.success(getActivity(), "刷新成功", Toast.LENGTH_SHORT, true).show();
                                  else
                                      Toasty.info(getActivity(), "刷新失败", Toast.LENGTH_SHORT, true).show();
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                              mAdapter = new RecordAdapter(_mActivity);
                              mAdapter.setDatas(chatList);
                              mRecy.setAdapter(mAdapter);

                              /*   JSONObject obj = new JSONObject().fromObject(sd);*/

                          }
                      });
                  }

                  else{
                         /*   Toasty.info(getActivity(), "暂时没有下注比赛", Toast.LENGTH_SHORT, true).show();*/
                            mRecy.post(new Runnable() {
                                @Override
                                public void run() {
                                    List<Record> chatList = null;
                                    try {
                                        chatList = initDatas();
                                        if(chatList!=null)
                                            Toasty.success(getActivity(), "刷新成功", Toast.LENGTH_SHORT, true).show();
                                        else
                                            Toasty.info(getActivity(), "刷新失败", Toast.LENGTH_SHORT, true).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter = new RecordAdapter(_mActivity);
                                    mAdapter.setDatas(chatList);
                                    mRecy.setAdapter(mAdapter);

                                    /*   JSONObject obj = new JSONObject().fromObject(sd);*/

                                }
                            });
                        }

                    }


                });
    }
    public void initAlert(){
        deleteRecord.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //    设置Title的图标
//                builder.setIcon(R.drawable.ic_add_48dp);
                //    设置Title的内容
                builder.setTitle("弹出警告框");
                //    设置Content来显示一个信息
                builder.setMessage("确定删除吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        Toast.makeText(MainActivity.this, "positive: " + which, Toast.LENGTH_SHORT).show();
                        deleteRecord();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        Toast.makeText(MainActivity.this, "negative: " + which, Toast.LENGTH_SHORT).show();
                    }
                });
                //    设置一个NeutralButton
                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                      /*  Toast.makeText(getActivity(), "neutral: " + which, Toast.LENGTH_SHORT).show();*/
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });
    }
    public void  deleteRecord(){
        String loginAccount;
        SharedPreferences share = getActivity().getSharedPreferences("account",MODE_PRIVATE);
        loginAccount = share.getString("account","account");
        String url = "http://119.23.45.41:8000/delete_all_record?supid=888";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("supid", loginAccount)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toasty.error(getActivity(), "请检查网络是否连接", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(response);
                        Gson gson = new Gson();

                        /*        jsonObject =  parseJSONWithJSONObject(response);*/
                        JSONArray      jsonArray =  parseJSONWithJSONObject(response);
                        Logger.d("list1"+list);
                        list.clear();
                        if(jsonArray!=null)
                            list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                            }.getType());
                        Logger.d("list2"+list);
                        if(list.size()!=0) {
                            Log.d(TAG, "onStringAvailable: " + list);
                            mRecy.post(new Runnable() {
                                @Override
                                public void run() {
                                    List<Record> chatList = null;
                                    try {
                                        chatList = initDatas();
                                        if(chatList!=null)
                                            Toasty.success(getActivity(), "刷新成功", Toast.LENGTH_SHORT, true).show();
                                        else
                                            Toasty.info(getActivity(), "刷新失败", Toast.LENGTH_SHORT, true).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mAdapter = new RecordAdapter(_mActivity);
                                    mAdapter.setDatas(chatList);
                                    mRecy.setAdapter(mAdapter);

                                    /*   JSONObject obj = new JSONObject().fromObject(sd);*/

                                }
                            });
                        }

                        else{
                            Toasty.info(getActivity(), "暂时没有下注比赛", Toast.LENGTH_SHORT, true).show();
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
                                List<Record> chatList = null;
                                try {
                                    chatList = initDatas();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                mRecy.setAdapter(mAdapter);
                                //还可以更新其他的控件
                                mAdapter = new RecordAdapter(_mActivity);
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
                                    List<Record> chatList = null;

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
                                    mAdapter = new RecordAdapter(_mActivity);
                                    mAdapter.setDatas(chatList);
                                    /*   JSONObject obj = new JSONObject().fromObject(sd);*/
                                }
                            });
                        }
                    }
                });
    }
}
