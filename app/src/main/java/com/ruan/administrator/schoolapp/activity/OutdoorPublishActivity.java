package com.ruan.administrator.schoolapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.OutdoorPub;
import com.ruan.administrator.schoolapp.bean.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Calendar;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ThumbnailUrlListener;
import cn.bmob.v3.listener.UploadFileListener;

public class OutdoorPublishActivity extends AppCompatActivity {

    private EditText title;
    private ImageView image;
    private TextView startTime;
    private TextView endTime;
    private EditText position;
    private EditText method;
    private EditText description;
    private ImageButton imgBtn;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor_publish);
        //初始化Bmob
        Bmob.initialize(this, "8e6a9663ed805f4f07b5b3782e6adadd");
        //初始化控件
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        title = (EditText) findViewById(R.id.et_outdoor_title);
        imgBtn = (ImageButton) findViewById(R.id.ib_outdoor_publish);
        image = (ImageView) findViewById(R.id.iv_outdoor_publish);
        startTime = (TextView) findViewById(R.id.tv_time1_outdoor);
        endTime = (TextView) findViewById(R.id.tv_time2_outdoor);
        position = (EditText) findViewById(R.id.tv_position_outdoor_publish);
        method = (EditText) findViewById(R.id.tv_method_outdoor_publish);
        description = (EditText) findViewById(R.id.et_description_outdoor_publish);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog pickerDialog = new DatePickerDialog(OutdoorPublishActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startTime.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog pickerDialog = new DatePickerDialog(OutdoorPublishActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endTime.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.show();
            }
        });
    }

    /**
     * 上传图片
     *
     * @param view
     */
    public void upload(View view) {
        imgBtn.setVisibility(View.INVISIBLE);
        image.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        int width = getWindowManager().getDefaultDisplay().getWidth();
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            int index = uri.toString().indexOf(":");
            path = uri.toString().substring(index + 1);
            Log.i("test", path.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr
                        .openInputStream(uri));
                image.setImageBitmap(bitmap);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(OutdoorPublishActivity.this).create();
                        dialog.setMessage("是否删除图片");
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                image.setImageBitmap(null);
                                image.setVisibility(View.GONE);
                                imgBtn.setVisibility(View.VISIBLE);
                            }
                        });
                        dialog.show();
                    }
                });

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 取消发布
     *
     * @param view
     */
    public void cancle(View view) {
        startActivity(new Intent(OutdoorPublishActivity.this, OutdoorActivity.class));
        finish();
    }

    /**
     * 确定发布
     *
     * @param view
     */
    public void confirm(View view) {
        AlertDialog dialog = new AlertDialog.Builder(OutdoorPublishActivity.this).create();
        dialog.setMessage("是否发布活动");
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (path != null) {
                    final BmobFile file = new BmobFile(new File(path));
                    file.uploadblock(OutdoorPublishActivity.this, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            insertObject(new OutdoorPub(file));
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(OutdoorPublishActivity.this, "图片上传失败：" + s, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }

            }
        });
        dialog.show();

    }

    private void insertObject(OutdoorPub pub) {
        /**
         * 设置当前数据的权限
         */
        //创建一个ACL对象
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(true);//设置所有人可读
        acl.setWriteAccess(BmobUser.getCurrentUser(this),true);//设置当前用户的读写权限
        //获取当前发布活动的用户
        User user = BmobUser.getCurrentUser(OutdoorPublishActivity.this, User.class);
        pub.setACL(acl);//设置这条数据的ACL信息
        //保存当前用户的id
        pub.setUserId(BmobUser.getCurrentUser(this).getObjectId());
        String username = user.getUsername();
        //获取用户输入的活动内容
        String titleString = title.getText().toString();
        String startTimeString = startTime.getText().toString();
        String endTimeString = endTime.getText().toString();
        String positionString = position.getText().toString();
        String methodString = method.getText().toString();
        String descString = description.getText().toString();
        pub.setTitle(titleString);
        pub.setStartTime(startTimeString);
        pub.setEndTime(endTimeString);
        pub.setPosition(positionString);
        pub.setMenthod(methodString);
        pub.setDescription(descString);
        pub.setUserName(username);
        pub.save(OutdoorPublishActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(OutdoorPublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(OutdoorPublishActivity.this, OutdoorActivity.class));
                finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(OutdoorPublishActivity.this, "发布失败：" + s, Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(OutdoorPublishActivity.this, OutdoorActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
