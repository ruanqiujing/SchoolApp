package com.ruan.administrator.schoolapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends Activity {

    private EditText et_register_user;
    private EditText et_register_pwd;
    private EditText et_register_pwd_confirm;
    private Button btn_register;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Bmob.initialize(this, "8e6a9663ed805f4f07b5b3782e6adadd");
        //初始化控件
        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_register_user = (EditText) findViewById(R.id.et_register_user);
        et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
        et_register_pwd_confirm = (EditText) findViewById(R.id.et_register_pwd_confirm);
        btn_register = (Button) findViewById(R.id.btn_register);
        RadioGroup group = (RadioGroup) findViewById(R.id.rg_register_sex);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int buttonId = group.getCheckedRadioButtonId();
                RadioButton rbSex = (RadioButton) RegisterActivity.this.findViewById(buttonId);
                sex = rbSex.getText().toString();


            }
        });
    }

    /**
     * 注册
     */
    public void btn_register(View view) {
        String username = et_register_user.getText().toString();
        String pwd = et_register_pwd.getText().toString();
        String pwd_confirm = et_register_pwd_confirm.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd_confirm)) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.equals(pwd_confirm)) {
            Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(pwd);
        if (TextUtils.isEmpty(sex)){
            Toast.makeText(this,"请选择您的性别",Toast.LENGTH_SHORT).show();
            return;
        }
        user.setSex(sex);
        user.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                if (s.contains("username") && s.contains("already taken")) {
                    Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "注册失败：" + s, Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}
