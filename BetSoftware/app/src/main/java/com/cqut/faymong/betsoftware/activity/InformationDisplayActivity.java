package com.cqut.faymong.betsoftware.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.IBackgroundFormat;
import com.bin.david.form.data.format.draw.ImageResDrawFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.cqut.faymong.betsoftware.R;
import com.cqut.faymong.betsoftware.entity.bet;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.dmoral.toasty.Toasty;
import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.Call;

public class InformationDisplayActivity extends SupportActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SmartTable<bet> table;
     public String loginAccount;
    List<Map<String,Object>> list = new ArrayList<>();
    Column<String> account;
    Column<String> domain;
    Column<String> betamount;
    Column<String> state;
    List<String> name_selected = new ArrayList<String>();
    public String account2;
    Column<Boolean> update;
    Column<Boolean> delete;
    public String delete_Account;
    public String delete_betAccount;
    public String delete_betAmount;
    public String delete_domain;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_display);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        createTable();
        getdata();
    }

    public void createTable(){
        table = (SmartTable<bet>)findViewById(R.id.table);
        table.getConfig().setFixedYSequence(false);
        account = new Column<>("下注账户", "account");
        domain = new Column<>("域名", "domain");
        state = new Column<>("登录成功", "state");
        int size = DensityUtils.dp2px(this,20);
        update = new Column<>("编辑", "update",new ImageResDrawFormat<Boolean>(size,size) {    //设置"操作"这一列以图标显示 true、false 的状态
            @Override
            protected Context getContext() {
                return InformationDisplayActivity.this;
            }
            @Override
            protected int getResourceID(Boolean isCheck, String value, int position) {

                return  R.drawable.update_demo2;
            }
        });

        update.setComputeWidth(40);
        update.setOnColumnItemClickListener(new OnColumnItemClickListener<Boolean>() {
            @Override
            public void onClick(Column<Boolean> column, String value, Boolean bool, int position) {
//                Toast.makeText(CodeListActivity.this,"点击了"+value,Toast.LENGTH_SHORT).show();

                List<String> accountList = account.getDatas();
                List<String> betamountList = betamount.getDatas();
                List<String> domainList = domain.getDatas();
                delete_Account = accountList.get(position);
                delete_betAccount = betamountList.get(position);
                delete_domain = domainList.get(position);
                Intent intent = new Intent(InformationDisplayActivity.this,updateAccountActivity.class);
                intent.putExtra("supid",list.get(position).get("supid").toString());
                intent.putExtra("account",list.get(position).get("account").toString());
                intent.putExtra("password",list.get(position).get("password").toString());
                intent.putExtra("domain",list.get(position).get("domain").toString());
                intent.putExtra("betamount",list.get(position).get("betamount").toString());
                startActivity(intent);

            }
        });

        delete = new Column<>("删除", "delete",new ImageResDrawFormat<Boolean>(size,size) {    //设置"操作"这一列以图标显示 true、false 的状态
            @Override
            protected Context getContext() {
                return InformationDisplayActivity.this;
            }
            @Override
            protected int getResourceID(Boolean isCheck, String value, int position) {

                return  R.drawable.delete;
            }
        });
  /*      delete.setComputeWidth(40);*/
        delete.setOnColumnItemClickListener(new OnColumnItemClickListener<Boolean>() {
            @Override
            public void onClick(Column<Boolean> column, String value, Boolean bool, int position) {
//                Toast.makeText(CodeListActivity.this,"点击了"+value,Toast.LENGTH_SHORT).show();
                list.remove(position);
                List<String> accountList = account.getDatas();
                List<String> betamountList = betamount.getDatas();
                List<String> domainList = domain.getDatas();

                delete_Account = accountList.get(position);
                delete_betAccount = betamountList.get(position);
                delete_domain = domainList.get(position);
                deletedata();

            }
        });
        betamount = new Column<>("下注金额", "betamount");
        table.getConfig().setShowTableTitle(false);
//        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));

        FontStyle style = new FontStyle();
        style.setTextSize(30);
        table.getConfig().setContentStyle(style);       //设置表格主题字体样式
        table.getConfig().setColumnTitleStyle(style);   //设置表格标题字体样式
        table.getConfig().setMinTableWidth(1124);   //设置最小宽度
        table.getConfig().setFixedYSequence(true);
        table.getConfig().setFixedYSequence(true);
        //X序号列
        table.getConfig().setFixedXSequence(true);
        //列标题
        table.getConfig().setFixedCountRow(true);
                table.getConfig().setXSequenceBackground(null);


    }

    public void getdata() {
        SharedPreferences share = InformationDisplayActivity.this.getSharedPreferences("account",MODE_PRIVATE);
        loginAccount = share.getString("account","account");
        String url = "http://119.23.45.41:8080/get_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", loginAccount)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(InformationDisplayActivity.this, e.toString() + "error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                     /*   Toast.makeText(InformationDisplayActivity.this, response.toString(), Toast.LENGTH_SHORT).show();*/
                        Gson gson = new Gson();
                        JSONArray jsonArray = parseJSONWithJSONObject(response);
                        list = gson.fromJson(jsonArray.toString(), new TypeToken<List<Map<String, Object>>>() {
                        }.getType());
                        if(list.size()!=0){
                            Toasty.success(InformationDisplayActivity.this, "刷新成功", Toast.LENGTH_SHORT, true).show();
                        }
                     /*   if(list.size()!=0)
                        Toast.makeText(InformationDisplayActivity.this,list.toString(),Toast.LENGTH_SHORT).show();*/
                        List<bet> codeList = new ArrayList<bet>();
                     for (int i =0;i<list.size();i++){
                         String betsuccess ="否";
                         if(Integer.parseInt(list.get(i).get("stat").toString())==1) {
                             betsuccess = "是";
                             if(list.get(i).get("account")!=null&list.get(i).get("domain")!=null&list.get(i).get("betamount")!=null)
                             codeList.add(new bet(list.get(i).get("account").toString(), list.get(i).get("domain").toString(), list.get(i).get("betamount").toString(), false, false, betsuccess));
                         }
                         else{
                             betsuccess = "否";
                             if(list.get(i).get("account")!=null&list.get(i).get("domain")!=null&list.get(i).get("betamount")!=null)
                             codeList.add(new bet(list.get(i).get("account").toString(),
                                     list.get(i).get("domain").toString(),
                                     list.get(i).get("betamount").toString(), false, false, betsuccess));
                         }
                         }

                        final TableData<bet> tableData = new TableData<>("测试标题",codeList, account, domain,betamount,state,update,delete);
                        table.setTableData(tableData);
                    }
                });
    }

    public void deletedata() {

        String url = "http://119.23.45.41:8080/remove_bet_account.php";
        OkHttpUtils
                .get()
                .url(url)
                .addParams("account", loginAccount)
                .addParams("betaccount", delete_Account)
                .addParams("domain", delete_domain)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toasty.error(InformationDisplayActivity.this, "请检查网络", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                    /*    Toast.makeText(InformationDisplayActivity.this, response.toString(), Toast.LENGTH_SHORT).show();*/
                        if(Integer.parseInt(response)==1)
                        Toasty.success(InformationDisplayActivity.this, "删除成功", Toast.LENGTH_SHORT, true).show();
                        else
                            Toasty.info(InformationDisplayActivity.this, "删除失败", Toast.LENGTH_SHORT, true).show();
                        List<bet> codeList = new ArrayList<bet>();
                        for (int i =0;i<list.size();i++){
                            codeList.add(new bet(list.get(i).get("account").toString(),list.get(i).get("domain").toString(),list.get(i).get("betamount").toString(),false,false,"否"));
                        }

                        final TableData<bet> tableData = new TableData<>("测试标题",codeList, account, domain,betamount,state,update,delete);
                        table.setTableData(tableData);
                        table.refreshDrawableState();
                        table.invalidate();
                    }
                });
    }

    private JSONArray parseJSONWithJSONObject(String jsonData){
        JSONArray jsonArray = null;
        try{
            JSONObject object = new JSONObject(jsonData);
            jsonArray = object.getJSONArray("db");
            Log.d("football", "parseJSONWithJSONObject: "+  jsonArray.get(0));

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
                table.getConfig().setXSequenceBackground(null);
                mRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }
}
