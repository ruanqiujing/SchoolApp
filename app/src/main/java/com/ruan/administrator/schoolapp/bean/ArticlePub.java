package com.ruan.administrator.schoolapp.bean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/3/31 0031.
 */
public class ArticlePub extends BmobObject {
    //标题
    private String title;
    //内容
    private String content;
    //发布美文的用户
    private String name;
    //发布活动的用户的id
    private String userObjId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserObjId() {
        return userObjId;
    }

    public void setUserObjId(String userObjId) {
        this.userObjId = userObjId;
    }
}

