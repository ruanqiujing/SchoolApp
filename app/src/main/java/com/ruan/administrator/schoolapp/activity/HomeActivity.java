package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ruan.administrator.schoolapp.R;

public class HomeActivity extends AppCompatActivity {
    //ViewFlipper的图片资源
//    private int[] vf_image = new int[]{R.mipmap.campaign_one, R.mipmap.campaign_two, R.mipmap.campaign_three};
    private int[] vf_image = new int[]{R.layout.vf_imageview1, R.layout.vf_imageview2, R.layout.vf_imageview3};
    //GridView的图片资源和文字资源
    private int[] iv_item = new int[]{R.mipmap.corporation, R.mipmap.lecture,  R.mipmap.outdoor,
            R.mipmap.recruitment,R.mipmap.administration,R.mipmap.setting};
    private String[] tv_item = new String[]{"社团活动", "学院通知","户外活动", "美文赏析",
            "教务系统","设置"};
    private GridView gv_home;
    private ViewFlipper vf_home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化控件
        initView();
        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        gv_home.setAdapter(new HomeAdapter());
        //动态的方式为ViewFlipper导入子View
        for (int i = 0; i < vf_image.length; i++) {
            vf_home.addView(getImage(vf_image[i]));
        }
        //为ViewFlipper添加动画效果
        vf_home.setInAnimation(this, R.anim.left_in);
        vf_home.setOutAnimation(this, R.anim.right_out);
        //设定ViewFlipper视图切换的时间间隔
        vf_home.setFlipInterval(1000);
        //开始播放
        vf_home.startFlipping();
    }

    private View getImage(int resId) {


        View view = View.inflate(this,resId,null);
        return view;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        gv_home = (GridView) findViewById(R.id.gv_home);

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(HomeActivity.this, CommunityActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, CollegeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this, OutdoorActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this, ArticleActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(HomeActivity.this, EducationalAdministratorActivity.class));
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this, MyActivity.class));
                        break;

                }
            }
        });
        //图片循环播放的ViewFlipper
        vf_home = (ViewFlipper) findViewById(R.id.vf_home);

    }

    /**
     * 附近的人的点击事件
     *
     * @param view
     */
    public void near(View view) {

    }

    /**
     * 我的
     * @param view
     */
    public void my(View view){
        startActivity(new Intent(HomeActivity.this,MyActivity.class));
        finish();
    }
    /**
     * 主界面适配器
     */
    private class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return tv_item.length;
        }

        @Override
        public Object getItem(int position) {
            return tv_item[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(HomeActivity.this, R.layout.view_home_item, null);
                holder = new ViewHolder();
                holder.iv_home_item = (ImageView) convertView.findViewById(R.id.iv_home_item);
                holder.tv_home_item = (TextView) convertView.findViewById(R.id.tv_home_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_home_item.setImageResource(iv_item[position]);
            holder.tv_home_item.setText(tv_item[position]);
            return convertView;
        }

        private class ViewHolder {
            private ImageView iv_home_item;
            private TextView tv_home_item;
        }
    }
}
