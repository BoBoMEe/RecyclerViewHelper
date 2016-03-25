package com.yaodu.drug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.yaodu.drug.interfaces.IConstant;
import com.yaodu.drug.interfaces.ILoginCallback;
import com.yaodu.drug.loginmanager.WeiXinLogin;
import com.yaodu.drug.loginmanager.WeiboLogin;
import com.yaodu.drug.loginmanager.LoginManager;
import com.yaodu.drug.loginmanager.QQLogin;
import com.yaodu.drug.model.QQInfoModel;
import com.yaodu.drug.model.QQTokenModel;
import com.yaodu.drug.model.WeixinInfoModel;
import com.yaodu.drug.model.WeixinTokenModel;
import com.yaodu.drug.sinaapi.User;

public class MainActivity extends AppCompatActivity implements IConstant {

    private TextView text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tpl_login_page);

        text1 = (TextView) findViewById(android.R.id.text1);

        rigistWeixin();
        rigistQQ();
        rigistWeibo();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weixinLogin.OnDestory();
    }


    public void login(View v) {
        switch (v.getId()) {
            case R.id.login_weixin:
                loginWeixin();
                //微信登录
                break;
            case R.id.login_qq:
                loginQQ();
                //QQ登陆
                break;
            case R.id.login_weibo:
                loginWeibo();
                //新浪微博
                break;

        }
    }

    //====================================QQ====================================================

    private LoginManager qqLogin;

    private void rigistQQ() {
        qqLogin = new LoginManager(this, QQLogin.class, new ILoginCallback() {
            @Override
            public void tokeCallBack(Object o) {
                QQTokenModel qqTokenModel = (QQTokenModel) o;
            }

            @Override
            public void infoCallBack(Object o) {
                QQInfoModel qqInfoModel = (QQInfoModel) o;
                runOnUiThread(() -> text1.setText(qqInfoModel.toString()));
            }
        });
    }

    private void loginQQ() {
        qqLogin.doLogin();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (qqLogin.onActivityResult(requestCode, resultCode, data)) {

        }
        if (weiboLogin.onActivityResult(requestCode, resultCode, data)) {

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //====================================weibo====================================================
    private LoginManager weiboLogin;

    private void rigistWeibo() {
        weiboLogin = new LoginManager(this, WeiboLogin.class, new ILoginCallback() {
            @Override
            public void tokeCallBack(Object o) {
                Oauth2AccessToken mAccessToken = (Oauth2AccessToken) o;
            }

            @Override
            public void infoCallBack(Object o) {
                User user = (User) o;
                runOnUiThread(() -> text1.setText(user.toString()));
            }
        });
    }

    private void loginWeibo() {
        weiboLogin.doLogin();
    }

    //====================================weixin=======================================================

    private LoginManager weixinLogin;

    // 注册到微信
    private void rigistWeixin() {
        weixinLogin = new LoginManager(this, WeiXinLogin.class, new ILoginCallback() {
            @Override
            public void tokeCallBack(Object o) {
                WeixinTokenModel tokenModel = (WeixinTokenModel) o;
            }

            @Override
            public void infoCallBack(Object o) {
                WeixinInfoModel infoModel = (WeixinInfoModel) o;
                runOnUiThread(() -> text1.setText(infoModel.toString()));
            }
        });
    }


    // 微信登陆请求
    private void loginWeixin() {
        weixinLogin.doLogin();

    }

    //=============================================================================================

}
