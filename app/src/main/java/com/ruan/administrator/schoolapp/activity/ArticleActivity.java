package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruan.administrator.schoolapp.R;
import com.ruan.administrator.schoolapp.bean.ArticlePub;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * 美文赏析
 */
public class ArticleActivity extends AppCompatActivity {

    private ListView list_view_article;
    private List<ArticlePub> apList;
    private ArticleAdapter adapter;
    public static final int RESULT_CODE = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (adapter == null) {
                adapter = new ArticleAdapter();
                list_view_article.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
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
                BmobQuery<ArticlePub> bmobQuery = new BmobQuery<ArticlePub>();
                bmobQuery.order("-createdAt");
                boolean isCache = bmobQuery.hasCachedResult(ArticleActivity.this, ArticlePub.class);
                if (isCache) {
                    bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_THEN_NETWORK);
                } else {
                    bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                }
                bmobQuery.findObjects(ArticleActivity.this, new FindListener<ArticlePub>() {
                    @Override
                    public void onSuccess(List<ArticlePub> list) {

                        apList = list;

                        if (apList != null) {
                            handler.sendEmptyMessage(0);
//
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        if(s.contains("The network is not available")){
                            Toast.makeText(ArticleActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            Toast.makeText(ArticleActivity.this, "下载错误：" + s, Toast.LENGTH_SHORT).show();
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
        list_view_article = (ListView) findViewById(R.id.list_view_article);
    }

    /**
     * 点击发布(TextView)事件
     */
    public void publish(View view) {
        startActivity(new Intent(ArticleActivity.this, ArticlePublishActivity.class));
        finish();
    }

    private class ArticleAdapter extends BaseAdapter {


        @Override
        public int getCount() {

            return apList.size();
        }

        @Override
        public Object getItem(int position) {
            return apList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(ArticleActivity.this, R.layout.item_list_view, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ArticlePub pub = apList.get(position);
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
            list_view_article.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   ArticlePub pub = apList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(ArticleActivity.this, ShowDetailArticleActivity.class);
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
