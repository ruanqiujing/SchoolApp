package com.ruan.administrator.schoolapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.CommunityPub;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 社团公告
 */
public class CommunityActivity extends Activity {

    private ListView list_view_community;
    private List<CommunityPub> cpList;
    private ComunityAdapter adapter;
    public static final int RESULT_CODE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (adapter == null) {
                adapter = new ComunityAdapter();
                list_view_community.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
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
                BmobQuery<CommunityPub> bmobQuery = new BmobQuery<CommunityPub>();
                bmobQuery.order("-createdAt");
                boolean isCache = bmobQuery.hasCachedResult(CommunityActivity.this, CommunityPub.class);
                if (isCache) {
                    bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
                } else {
                    bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                }
                bmobQuery.findObjects(CommunityActivity.this, new FindListener<CommunityPub>() {
                    @Override
                    public void onSuccess(List<CommunityPub> list) {

                        cpList = list;
                        if (cpList != null) {
                            handler.sendEmptyMessage(0);

                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        if(s.contains("The network is not available")){
                            Toast.makeText(CommunityActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Toast.makeText(CommunityActivity.this, "下载错误：" + s, Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });
            }
        }.start();


    }

    /**
     * 初始化控件
     */
    private void initView() {
        list_view_community = (ListView) findViewById(R.id.list_view_community);
    }

    /**
     * 点击发布(TextView)事件
     */
    public void publish(View view) {
        startActivity(new Intent(CommunityActivity.this, CommunityPublishActivity.class));
        finish();
    }

    private class ComunityAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return cpList.size();
        }

        @Override
        public Object getItem(int position) {
            return cpList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(CommunityActivity.this, R.layout.item_list_view, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            CommunityPub pub = cpList.get(position);
            /**
             * 发布时间
             */
            String createdAt = pub.getCreatedAt();
            String substring = createdAt.substring(2);
            holder.tv_time.setText(substring);
            //标题
            holder.tv_title.setText(pub.getTitle());
            //内容
            holder.tv_content.setText(pub.getContent());
            //发布者用户名
            holder.tv_name.setText(pub.getName());
            list_view_community.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    CommunityPub pub = cpList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(CommunityActivity.this, ShowDetailCommunityActivity.class);
                    intent.putExtra("userObjId",pub.getUserObjId());
                    intent.putExtra("title", pub.getTitle());
                    intent.putExtra("content", pub.getContent());
                    intent.putExtra("objectId",pub.getObjectId());
                    startActivityForResult(intent, RESULT_CODE);
                    finish();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            TextView tv_title;
            TextView tv_content;
            TextView tv_name;
            TextView tv_time;
        }
    }


}
