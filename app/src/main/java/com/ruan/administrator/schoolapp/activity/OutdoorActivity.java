package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.OutdoorPub;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class OutdoorActivity extends AppCompatActivity {

    private ListView listView;
    private List<OutdoorPub> pubList;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (adapter == null) {
                adapter = new OutdoorAdapter();
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }
    };
    private OutdoorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);
        //初始化Bmob
        Bmob.initialize(this, "8e6a9663ed805f4f07b5b3782e6adadd");
        //初始化控件
        initView();
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                //从服务器中查询OutdoorPub的信息
                BmobQuery<OutdoorPub> pub = new BmobQuery<OutdoorPub>();
                pub.order("createdAt");
                //是否有缓存，此方法必须放在查询条件都设置完之后再来调用才有效
                boolean isCache = pub.hasCachedResult(OutdoorActivity.this, OutdoorPub.class);
                if (isCache) {
                    pub.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);//先从缓存取，再从网络取
                } else {
                    pub.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
                }
                pub.findObjects(OutdoorActivity.this, new FindListener<OutdoorPub>() {
                    @Override
                    public void onSuccess(List<OutdoorPub> list) {
                        pubList = list;
                        if (pubList != null) {
                            handler.sendEmptyMessage(0);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(OutdoorActivity.this, "查询数据失败：" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.list_view_outdoor);

    }

    /**
     * 发布活动
     *
     * @param view
     */
    public void publish(View view) {
        startActivity(new Intent(OutdoorActivity.this, OutdoorPublishActivity.class));
        finish();
    }

    private class OutdoorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return pubList.size();
        }

        @Override
        public Object getItem(int position) {
            return pubList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(OutdoorActivity.this,R.layout.adapter_outdoor,null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.iv_adapter_outdoor);
                holder.title = (TextView) convertView.findViewById(R.id.tv_adapter_outdoor_title);
                holder.time = (TextView) convertView.findViewById(R.id.tv_adapter_outdoor_time);
                holder.position = (TextView) convertView.findViewById(R.id.tv_adapter_outdoor_position);
                holder.pubTime = (TextView) convertView.findViewById(R.id.tv_adapter_outdoor_time_pub);
                holder.name = (TextView) convertView.findViewById(R.id.tv_adapter_outdoor_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            OutdoorPub outdoorPub = pubList.get(position);
            holder.title.setText(outdoorPub.getTitle());
            holder.time.setText(outdoorPub.getStartTime()+"-"+outdoorPub.getEndTime());
            holder.position.setText(outdoorPub.getPosition());
            holder.pubTime.setText(outdoorPub.getCreatedAt());
            holder.name.setText(outdoorPub.getUserName());
            BmobFile image = outdoorPub.getImage();
            if (image != null){
                image.download(OutdoorActivity.this, new DownloadFileListener() {
                    @Override
                    public void onSuccess(String s) {
                        Bitmap bitmap = BitmapFactory.decodeFile(s);
                        holder.image.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        holder.image.setImageResource(R.mipmap.picture_error);
                    }
                });
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    OutdoorPub outdoor = pubList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(OutdoorActivity.this,OutdoorDetailActivity.class);
                    intent.putExtra("objectId",outdoor.getObjectId());
                    intent.putExtra("userId",outdoor.getUserId());
                    startActivity(intent);
                    finish();
                }
            });
            return convertView;
        }

        private class ViewHolder{
            ImageView image;
            TextView title;
            TextView time;
            TextView position;
            TextView pubTime;
            TextView name;
        }
    }
}
