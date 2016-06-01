package com.ruan.administrator.schoolapp.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/3/27 0027.
 */
public class User extends BmobUser{
    private String sex;
    private Integer age;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
