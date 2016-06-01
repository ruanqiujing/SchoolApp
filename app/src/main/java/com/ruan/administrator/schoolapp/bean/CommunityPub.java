package com.ruan.administrator.schoolapp.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/3/27 0027.
 * 发布社团活动的数据表格
 */
public class CommunityPub extends BmobObject{
    //标题
    private String title;
    //内容
    private String content;
    //发布活动的用户
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
