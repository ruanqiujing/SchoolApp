package com.ruan.administrator.schoolapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.ArticlePub;
import com.ruan.administrator.schoolapp.bean.OutdoorPub;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UploadFileListener;

public class OutdoorDetailActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title;
    private TextView time;
    private TextView position;
    private TextView method;
    private TextView desc;
    private TextView pubTime;
    private TextView name;
    private ImageView delete;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor_detail);
        //初始化Bmob
        Bmob.initialize(this, "8e6a9663ed805f4f07b5b3782e6adadd");
        initView();
        initData();
    }

    private void initView() {
        image = (ImageView) findViewById(R.id.iv_adapter_outdoor);
        title = (TextView) findViewById(R.id.tv_adapter_outdoor_title);
        time = (TextView) findViewById(R.id.tv_adapter_outdoor_time);
        position = (TextView) findViewById(R.id.tv_adapter_outdoor_position);
        method = (TextView) findViewById(R.id.tv_adapter_outdoor_method);
        desc = (TextView) findViewById(R.id.tv_adapter_outdoor_desc);
        //内容过多时的滚动事件
        desc.setMovementMethod(ScrollingMovementMethod.getInstance());
        pubTime = (TextView) findViewById(R.id.tv_adapter_outdoor_time_pub);
        name = (TextView) findViewById(R.id.tv_adapter_outdoor_name);
        delete = (ImageView) findViewById(R.id.iv_outdoor_show_delete);
        back = (ImageView) findViewById(R.id.iv_show_content);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        new Thread() {
            @Override
            public void run() {
                Intent intent = getIntent();
                final String objectId = intent.getStringExtra("objectId");
                //获取当前登录用户的ObjectId
                String currentUserObjId = BmobUser.getCurrentUser(OutdoorDetailActivity.this).getObjectId();
                //比较当前用户与发布活动的用户Id是否相同，如果相同则具有写的权限，再显示出删除图标
                if(intent.getStringExtra("userId").equals(currentUserObjId)){
                    delete.setVisibility(View.VISIBLE);
                }
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(OutdoorDetailActivity.this, OutdoorActivity.class), 0);
                        finish();
                    }
                });
                //删除活动
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(OutdoorDetailActivity.this).create();
                        dialog.setMessage("是否删除活动");
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OutdoorPub pub = new OutdoorPub();
                                pub.setObjectId(objectId);
                                pub.delete(OutdoorDetailActivity.this, new DeleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(OutdoorDetailActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                        startActivityForResult(new Intent(OutdoorDetailActivity.this, OutdoorActivity.class), 0);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        Toast.makeText(OutdoorDetailActivity.this,"删除失败:"+s,Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                });
                            }
                        });
                        dialog.show();

                    }
                });
                //从服务器中查询OutdoorPub的信息
                BmobQuery<OutdoorPub> pub = new BmobQuery<OutdoorPub>();
                //是否有缓存，此方法必须放在查询条件都设置完之后再来调用才有效
                boolean isCache = pub.hasCachedResult(OutdoorDetailActivity.this, OutdoorPub.class);
                if (isCache) {
                    pub.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);//先从缓存取，再从网络取
                } else {
                    pub.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
                }
                pub.getObject(OutdoorDetailActivity.this, objectId, new GetListener<OutdoorPub>() {
                    @Override
                    public void onSuccess(OutdoorPub outdoorPub) {
                        title.setText(outdoorPub.getTitle());
                        time.setText(outdoorPub.getStartTime() + "-" + outdoorPub.getEndTime());
                        position.setText(outdoorPub.getPosition());
                        method.setText(outdoorPub.getMenthod());
                        desc.setText(outdoorPub.getDescription());
                        pubTime.setText(outdoorPub.getCreatedAt());
                        name.setText(outdoorPub.getUserName());
                        BmobFile file = outdoorPub.getImage();
                        if (file != null) {
                            file.download(OutdoorDetailActivity.this, new DownloadFileListener() {
                                @Override
                                public void onSuccess(String s) {
                                    Log.i("test", "lalalalalalla::" + s);
                                    Bitmap bitmap = BitmapFactory.decodeFile(s);
                                    image.setImageBitmap(bitmap);
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    image.setImageResource(R.mipmap.picture_error);
                                    Toast.makeText(OutdoorDetailActivity.this, "图片加载失败：" + s, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(OutdoorDetailActivity.this, "数据查询失败：" + s, Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        }.start();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(OutdoorDetailActivity.this, OutdoorActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}