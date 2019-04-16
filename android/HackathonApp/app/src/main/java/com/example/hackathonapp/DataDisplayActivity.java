package com.example.hackathonapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DataDisplayActivity extends AppCompatActivity {

    private DataAdapter adapter;
    private DrawerLayout mDrawerLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);
        final Intent intent=getIntent();
        final String account=intent.getStringExtra("account");
        final String college=intent.getStringExtra("college");
        final NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        TextView userAccount;
        TextView userCollege;

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        View headerView=navigationView.getHeaderView(0);
        userAccount=(TextView)headerView.findViewById(R.id.drawer_account);
        userCollege=(TextView)headerView.findViewById(R.id.drawer_major);

        Log.d("DataDisplayActivity",account);
        userAccount.setText(account);
        userCollege.setText(college);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        sendRequestWithOkHttp("true",college);
        navigationView.setCheckedItem(R.id.campus);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.web){
                    if(navigationView.getCheckedItem().getItemId()!=R.id.web) {
                        sendRequestWithOkHttp("false", college);
                        navigationView.setCheckedItem(R.id.web);
                        mDrawerLayout.closeDrawers();
                    }else{
                        mDrawerLayout.closeDrawers();
                    }
                }else if (menuItem.getItemId()==R.id.more_campus_data){
                    Intent intent1=new Intent(DataDisplayActivity.this,
                            MoreDataActivity.class);
                    intent1.putExtra("campus","true");
                    startActivity(intent1);
                }else if(menuItem.getItemId()==R.id.campus){
                    if(navigationView.getCheckedItem().getItemId()!=R.id.campus){
                        sendRequestWithOkHttp("true", college);
                        navigationView.setCheckedItem(R.id.campus);
                        mDrawerLayout.closeDrawers();
                    }else {
                        mDrawerLayout.closeDrawers();
                    }
                }else if(menuItem.getItemId()==R.id.more_web_data){
                    Intent intent2=new Intent(DataDisplayActivity.this,MoreDataActivity.class);
                    intent2.putExtra("campus","false");
                    startActivity(intent2);
                }
                return true;
            }
        });
    }

    private void sendRequestWithOkHttp(final String campus, final String college) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    MediaType JSON=MediaType.parse("application/json; charset=utf-8");
                    String json="{\"campus\":\""+campus+"\",\"category\":\""+college
                            +"\"}";
                    RequestBody requestBody=RequestBody.create(JSON,json);
                    Request request=new Request.Builder()
                            .url("http://192.168.50.55:8080/RecInfo")
//                            .url("http://127.0.0.1/campus_data.json")
                            .post(requestBody)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
//                    Log.d("DataDisplayActivity",responseData);
                    parseWithGson(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseWithGson(String responseData) {
        Gson gson=new Gson();
        List<Data> dataList=gson.fromJson(responseData,new TypeToken<List<Data>>()
        {}.getType());
        showDataInRecyclerView(dataList);
    }

    private void showDataInRecyclerView(final List<Data> dataList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                GridLayoutManager layoutManager=new GridLayoutManager(DataDisplayActivity.this,
                        1);
                recyclerView.setLayoutManager(layoutManager);
                adapter=new DataAdapter(dataList);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
