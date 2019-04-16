package com.example.hackathonapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoreDataActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DataAdapter adapter;
    String campus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_data);

        Intent intent=getIntent();
        campus=intent.getStringExtra("campus");

        Spinner category=(Spinner)findViewById(R.id.category);
        recyclerView=(RecyclerView)findViewById(R.id.more_data_recycler_view);
        Toolbar toolbar=(Toolbar)findViewById(R.id.more_data_activity_toolbar);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.college,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner=(Spinner)parent;
                String college=(String)spinner.getItemAtPosition(position);
                if(!college.equals("-学院-")) {
                    sendRequestWithOkHttp(college);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sendRequestWithOkHttp(final String college) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    String json = "{\"campus\":\""+campus+"\",\"category\":\""+college
                            +"\"}";
                    Log.d("MoreDataActivity",json);
                    RequestBody requestBody = FormBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url("http://192.168.50.55:8080/RecInfo")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
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
        showInRecyclerView(dataList);
    }

    private void showInRecyclerView(final List<Data> dataList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GridLayoutManager manager=new GridLayoutManager(MoreDataActivity.this,
                        1);
                recyclerView.setLayoutManager(manager);
                adapter=new DataAdapter(dataList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }
}