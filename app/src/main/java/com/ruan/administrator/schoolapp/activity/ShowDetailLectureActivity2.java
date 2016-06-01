package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.CollegePub2;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;

public class ShowDetailLectureActivity2 extends AppCompatActivity {

    private TextView tv_show_content_title;
    private TextView tv_show_content_content;
    private ImageView iv_show_content;
    private ImageView iv_community_show_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_show_content);
        initView();

    }

    private void initView() {
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        tv_show_content_title = (TextView) findViewById(R.id.tv_show_content_title);
        tv_show_content_content = (TextView) findViewById(R.id.tv_show_content_content);
        //内容过多时的滚动事件
        tv_show_content_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        iv_show_content = (ImageView) findViewById(R.id.iv_show_content);
        iv_community_show_delete = (ImageView) findViewById(R.id.iv_community_show_delete);
        //获取当前登录用户的ObjectId
        String currentUserObjId = BmobUser.getCurrentUser(this).getObjectId();
        //比较当前用户与发布活动的用户Id是否相同，如果相同则具有写的权限，再显示出删除图标
        if(bundle.getString("userObjId").equals(currentUserObjId)){
            iv_community_show_delete.setVisibility(View.VISIBLE);
        }
        String title = bundle.getString("title");////getString()返回指定key的值
        tv_show_content_title.setText(title);
        String content = bundle.getString("content");
        tv_show_content_content.setText(content);
        //获取objectId
        final String objectId = bundle.getString("objectId");
        iv_show_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShowDetailLectureActivity2.this, CollegeActivity2.class), 0);
                finish();
            }
        });
        //删除活动
        iv_community_show_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollegePub2 pub = new CollegePub2();
                pub.setObjectId(objectId);
                pub.delete(ShowDetailLectureActivity2.this, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ShowDetailLectureActivity2.this,"删除成功",Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(ShowDetailLectureActivity2.this, CollegeActivity2.class), 0);
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(ShowDetailLectureActivity2.this,"删除失败:"+s,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == CollegeActivity2.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            String title = bundle.getString("title");
            tv_show_content_title.setText(title);
            String content = bundle.getString("content");
            tv_show_content_content.setText(content);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(ShowDetailLectureActivity2.this, CollegeActivity2.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
