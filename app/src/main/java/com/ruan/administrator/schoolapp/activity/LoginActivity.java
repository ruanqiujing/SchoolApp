package com.ruan.administrator.schoolapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity {

    private EditText et_login_user;
    private EditText et_login_pwd;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "8e6a9663ed805f4f07b5b3782e6adadd");

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_login_user = (EditText) findViewById(R.id.et_login_user);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    /**
     * 登录按钮的点击事件
     */
    public void login(View view) {
        String username = et_login_user.getText().toString();
        String pwd = et_login_pwd.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        user.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                if (s.contains("username or password incorrect")){
                    Toast.makeText(LoginActivity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "登录失败：" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 跳转到注册页面(立即注册TextView的点击事件)
     *
     * @param view
     */
    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
