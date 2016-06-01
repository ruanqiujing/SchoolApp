package com.ruan.administrator.schoolapp.bean;

import android.content.Context;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class OutdoorPub extends BmobObject{
    private String title;//标题
    private BmobFile image;//图片
    private String startTime;//开始时间
    private String endTime;//结束时间
    private String position;//活动地点
    private String menthod;//报名方式
    private String description;//描述
    private String userName;//发布者
    private String userId;//发布者的ObjectId

    public OutdoorPub(){

    }
    public OutdoorPub(BmobFile image){
        this.image = image;
        this.setImage(image);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMenthod() {
        return menthod;
    }

    public void setMenthod(String menthod) {
        this.menthod = menthod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
