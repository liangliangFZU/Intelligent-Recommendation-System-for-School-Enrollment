package com.example.hackathonapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity {

    String userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FlatButton login=(FlatButton)findViewById(R.id.login_button);
        ImageView loginPic=(ImageView)findViewById(R.id.login_pic);
        final SuperEditText account=(SuperEditText)findViewById(R.id.account);
        final SuperEditText password=(SuperEditText)findViewById(R.id.password);
        Button registerButton=(Button)findViewById(R.id.register_button);
        loginPic.setImageResource(R.drawable.ic_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputAccount=account.getText().toString();
                String inputPassword=password.getText().toString();
                userAccount=inputAccount;
//                Log.d("MainActivity",inputAccount);
//                Log.d("MainActivity",inputPassword);
                if(isEmail(inputAccount)) {
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("登录");
                    progressDialog.setMessage("用户" + inputAccount + "登陆中...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    sendDataWithOkHttp(inputAccount,inputPassword);
//                    progressDialog.dismiss();
                }else{
                    Toast.makeText(MainActivity.this,"邮箱格式错误",Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendDataWithOkHttp(final String account, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON=MediaType.parse("application/json; charset=utf-8");
                    String json="{\"mail\":\""+account+"\",\"password\":\""+password+"\"}";
                    Log.d("MainActivity",json);
                    RequestBody requestBody = FormBody.create(JSON,json);
                    Request request = new Request.Builder()
                            .url("http://192.168.50.55:8080/userLogin")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseDataWithGson(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseDataWithGson(String responseData) {
        Gson gson=new Gson();
        LoginResult result=gson.fromJson(responseData,LoginResult.class);
        if(result.getResult().equals("true")){
            Intent intent=new Intent(MainActivity.this,DataDisplayActivity.class);
            intent.putExtra("account",userAccount);
            intent.putExtra("college",result.getCollege());
            startActivity(intent);
            finish();
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

