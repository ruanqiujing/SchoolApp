package com.ruan.administrator.schoolapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ruan.administrator.schoolapp.R;

public class CollegeActivity extends AppCompatActivity {

    private ListView listView;

    String[] arrCollege = new String[]{"计算机与信息工程学院", "电子与信息工程学院", "国际与教育学院", "外国语学院",
            "经济学院", "机械学院", "矿业学院", "管理学院", "理学院"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_view_college);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_list_college, arrCollege);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity0.class));
                        finish();
                        break;
                    case 1:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity1.class));
                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity2.class));
                        finish();
                        break;
                    case 3:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity3.class));
                        finish();
                        break;
                    case 4:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity4.class));
                        finish();
                        break;
                    case 5:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity5.class));
                        finish();
                        break;
                    case 6:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity6.class));
                        finish();
                        break;
                    case 7:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity7.class));
                        finish();
                        break;
                    case 8:
                        startActivity(new Intent(CollegeActivity.this,CollegeActivity8.class));
                        finish();
                        break;

                }
            }
        });
    }
}
