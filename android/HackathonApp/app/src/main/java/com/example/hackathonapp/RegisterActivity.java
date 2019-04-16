package com.example.hackathonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.cengalabs.flatui.views.FlatButton;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import scut.carson_ho.diy_view.SuperEditText;

public class RegisterActivity extends AppCompatActivity {

    String college;
    String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        String college;

        ImageView registerPic=(ImageView)findViewById(R.id.register_pic);
        final SuperEditText registerName=(SuperEditText)findViewById(R.id.register_name);
        final SuperEditText registerAccount=(SuperEditText)findViewById(R.id.register_account);
        final SuperEditText registerPassword=(SuperEditText)findViewById(R.id.register_password);
        FlatButton registerRequestButton=(FlatButton)findViewById(R.id.register_request_button);
        Button buttonToLogin=(Button)findViewById(R.id.to_login_button);
        Spinner collegeOpt=(Spinner)findViewById(R.id.college_opt);
        final Spinner majorOpt=(Spinner)findViewById(R.id.major_opt);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
                R.array.college,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeOpt.setAdapter(adapter);
        collegeOpt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner=(Spinner)parent;
                String col=(String)spinner.getItemAtPosition(position);
                college=col;
                ArrayAdapter<CharSequence> majorAdapter=ArrayAdapter.createFromResource(
                        RegisterActivity.this,R.array.shuji,android.R.layout.simple_spinner_item);
                if(col.equals("电气工程与自动化学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.dianqi,android.R.layout.simple_spinner_item);
                }else if(col.equals("机械工程及自动化学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.jixie,android.R.layout.simple_spinner_item);
                }else if(col.equals("软件学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.ruanjian,android.R.layout.simple_spinner_item);
                }else if(col.equals("石油化工学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.shihua,android.R.layout.simple_spinner_item);
                }else if(col.equals("土木工程学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.tumu,android.R.layout.simple_spinner_item);
                }else if(col.equals("环境与资源学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.huanzi,android.R.layout.simple_spinner_item);
                }else if(col.equals("经济与管理学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.jinguan,android.R.layout.simple_spinner_item);
                }else if(col.equals("生物科学与工程学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.shenggong,android.R.layout.simple_spinner_item);
                }else if(col.equals("外国语学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.waiyuan,android.R.layout.simple_spinner_item);
                }else if(col.equals("物理与信息工程学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.wuxin,android.R.layout.simple_spinner_item);
                }else if(col.equals("化学学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.huaxue,android.R.layout.simple_spinner_item);
                }else if(col.equals("建筑学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.jianyuan,android.R.layout.simple_spinner_item);
                }else if(col.equals("紫金矿业学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.zijin,android.R.layout.simple_spinner_item);
                }else if(col.equals("材料科学与工程学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.cailiao,android.R.layout.simple_spinner_item);
                }else if(col.equals("法学院")){
                    majorAdapter=ArrayAdapter.createFromResource(RegisterActivity.this
                            ,R.array.fayuan,android.R.layout.simple_spinner_item);
                }
                majorOpt.setAdapter(majorAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        registerPic.setImageResource(R.drawable.ic_register);
        registerRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputRegisterName=registerName.getText().toString();
                String inputRegisterAccount=registerAccount.getText().toString();
                String inputRegisterPassword=registerPassword.getText().toString();
                account=inputRegisterAccount;
                if(isEmail(inputRegisterAccount)){
                    ProgressDialog dialog=new ProgressDialog(RegisterActivity.this);
                    dialog.setTitle("注册");
                    dialog.setMessage("用户" + inputRegisterName + "注册中...");
                    dialog.setCancelable(true);
                    dialog.show();
                    sendDataWithOkHttp(inputRegisterName,inputRegisterAccount,inputRegisterPassword);
//                    dialog.dismiss();
                }else{
                    Toast.makeText(RegisterActivity.this,"邮箱格式不正确",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void sendDataWithOkHttp(final String inputRegisterName, final String inputRegisterAccount, final String inputRegisterPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client=new OkHttpClient();
                    MediaType JSON=MediaType.parse("application/json; charset=utf-8");
                    String json="{\"username\":\""+inputRegisterName+
                            "\",\"mail\":\""+inputRegisterAccount+
                            "\",\"password\":\""+inputRegisterPassword+
                            "\",\"category\":\""+college+"\"}";
                    RequestBody requestBody=RequestBody.create(JSON,json);

                    Log.d("RegisterActivity",json);
//                    Log.d("RegisterActivity",inputRegisterAccount);
//                    Log.d("RegisterActivity",inputRegisterPassword);
//                    Log.d("RegisterActivity",college);

                    Request request=new Request.Builder()
                            .url("http://192.168.50.55:8080/userRegister")
                            .post(requestBody)
                            .build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    Log.d("RegisterActivity",responseData);
                    parseWithGson(responseData);
                }catch (Exception e){
                    e.printStackTrace();
//                    Toast.makeText(RegisterActivity.this,
//                            "服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }

    private void parseWithGson(String responseData) {
        Gson gson=new Gson();
        RegisterResult result=gson.fromJson(responseData,RegisterResult.class);
        if (result.getResult().equals("true")){
            Intent intent=new Intent(RegisterActivity.this,DataDisplayActivity.class);
            intent.putExtra("account",account);
            intent.putExtra("college",college);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }

}

