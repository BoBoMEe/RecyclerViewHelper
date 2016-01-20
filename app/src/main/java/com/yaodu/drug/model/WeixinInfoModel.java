package com.yaodu.drug.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/10.
 */
public class WeixinInfoModel {

    public String openid;
    public String nickname;
    public int sex;
    public String language;
    public String city;
    public String province;

    public String country;

    public String headimgurl;

    private ArrayList privilege;

    public String unionid;

    @Override
    public String toString() {
        return "WeixinInfoModel{" + "\n" +
                "openid='" + openid + '\'' + "\n" +
                ", nickname='" + nickname + '\'' + "\n" +
                ", sex=" + sex + "\n" +
                ", language='" + language + '\'' + "\n" +
                ", city='" + city + '\'' + "\n" +
                ", province='" + province + '\'' + "\n" +
                ", country='" + country + '\'' + "\n" +
                ", headimgurl='" + headimgurl + '\'' + "\n" +
                ", privilege=" + privilege + "\n" +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
