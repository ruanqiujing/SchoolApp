package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.User;
import com.ruan.administrator.schoolapp.util.ImageLoadOptions;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * “我的”的界面
 */
public class MyActivity extends AppCompatActivity {

    private TextView tv_my_name;
    private TextView tv_my_age;
    private ImageView icon;
//    BmobUserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String username = (String) User.getObjectByKey(this, "username");
        String sex = (String) User.getObjectByKey(this, "sex");
        tv_my_name.setText(username);
        tv_my_age.setText(sex);

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_my_name = (TextView) findViewById(R.id.tv_my_name);
        tv_my_age = (TextView) findViewById(R.id.tv_my_age);
        icon = (ImageView) findViewById(R.id.iv_myIcon);
    }

    /**
     * 切换用户
     *
     * @param view
     */
    public void switchUser(View view) {
        User.logOut(this);
        User user = BmobUser.getCurrentUser(this, User.class);
        startActivity(new Intent(MyActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * 退出登录
     *
     * @param view
     */
    public void logOut(View view) {
        User.logOut(this);
        User user = BmobUser.getCurrentUser(this, User.class);
        System.exit(0);
    }

    /**
     * 更新头像 refreshAvatar
     *
     * @return void
     * @throws
     */
    private void refreshAvatar(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            ImageLoader.getInstance().displayImage(avatar, icon,
                    ImageLoadOptions.getOptions());
        } else {
            icon.setImageResource(R.mipmap.add_image);
        }
    }
//    private void updateUserData(User user,UpdateListener listener){
//        User current = (User) userManager.getCurrentUser(User.class);
//        user.setObjectId(current.getObjectId());
//        user.update(this, listener);
//    }
//}
//    private void updateUserAvatar(final String url) {
//        User  u =new User();
//        u.setAvatar(url);
//        updateUserData(u,new UpdateListener() {
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                ShowToast("头像更新成功！");
//                // 更新头像
//                refreshAvatar(url);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                // TODO Auto-generated method stub
//                ShowToast("头像更新失败：" + msg);
//            }
//        });
    }
