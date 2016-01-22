package com.yaodu.drug;

/**
 * @author：BoBoMEe Created at 2016/1/4.
 */
public interface IConstant {
    //==========================================================================
    //weixin
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    String APP_ID = "****";

    String WEIXIN_APP_SECRET = "****";


    //==========================================================================
    //QQ
    String TENCENT_APP_ID = "****";


    //=========================================================================
    // weibo
    String WB_APP_KEY = "****";
    String REDIRECT_URL = "****";
    String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


}
