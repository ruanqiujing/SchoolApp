package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.CollegePub5;
import com.ruan.administrator.schoolapp.bean.User;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class CollegePublishActivity5 extends AppCompatActivity {

    private EditText et_publish_title;
    private EditText et_publish_content;
    private Button btn_publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
    }

    /**
     * 初始化控件
     */
    private void initView() {
        et_publish_title = (EditText) findViewById(R.id.et_publish_title);
        et_publish_content = (EditText) findViewById(R.id.et_publish_content);
        btn_publish = (Button) findViewById(R.id.btn_publish);
    }

    /**
     * 点击发布按钮触发事件
     * @param view
     */
    public void publish(View view){
        String title = et_publish_title.getText().toString();
        String content = et_publish_content.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 设置当前数据的权限
         */
        //创建一个ACL对象
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(true);//设置所有人可读
        acl.setWriteAccess(BmobUser.getCurrentUser(this),true);//设置当前用户的读写权限
        String username = (String) User.getObjectByKey(this, "username");
        CollegePub5 lp = new CollegePub5();
        lp.setACL(acl);
        //保存当前用户的id
        lp.setUserObjId(BmobUser.getCurrentUser(this).getObjectId());
        lp.setName(username);
        lp.setTitle(title);
        lp.setContent(content);
        lp.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(CollegePublishActivity5.this,"发布成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CollegePublishActivity5.this,CollegeActivity5.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(CollegePublishActivity5.this,"发布失败:"+s,Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(CollegePublishActivity5.this,CollegeActivity5.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
