package com.yaodu.drug.loginmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.yaodu.drug.interfaces.IConstant;
import com.yaodu.drug.interfaces.ILogin;
import com.yaodu.drug.interfaces.ILoginCallback;
import com.yaodu.drug.sinaapi.LogoutAPI;
import com.yaodu.drug.sinaapi.User;
import com.yaodu.drug.sinaapi.UsersAPI;
import com.yaodu.drug.util.AccessTokenKeeper;
import com.yaodu.drug.util.ToastUtil;

/**
 * Created by bobomee on 2016/3/25.
 */
public class WeiboLogin implements ILogin, IConstant {

    private Activity activity;
    private ILoginCallback iLoginCallback;

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;


    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    private class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            // 从这里获取用户输入的 电话号码信息
            // String phoneNum = mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                // 保存 Token 到 SharedPreferences
                // 保存 Token 到 SharedPreferences

                AccessTokenKeeper.writeAccessToken(activity, mAccessToken);

                if (null != iLoginCallback) iLoginCallback.tokeCallBack(mAccessToken);

                //获取用户信息
                UsersAPI mUserAPI = new UsersAPI(activity, WB_APP_KEY, mAccessToken);

                long uid = Long.parseLong(mAccessToken.getUid());// 获取openid

                mUserAPI.show(uid, mListener);

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                ToastUtil.show(activity, message);
            }
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }
    }


    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (null != iLoginCallback) iLoginCallback.infoCallBack(user);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

    private void logoutWeibo() {
        mAccessToken = AccessTokenKeeper.readAccessToken(activity);
        if (0 != mAccessToken.getExpiresTime()) {
            new LogoutAPI(activity, WB_APP_KEY,
                    mAccessToken).logout(new RequestListener() {
                @Override
                public void onComplete(String s) {
                    AccessTokenKeeper.clear(activity);
                    mAccessToken = null;
                    loginWeibo();
                }

                @Override
                public void onWeiboException(WeiboException e) {

                }
            });

        } else {
            loginWeibo();
        }

    }


    @Override
    public void doLogin(Activity activity, ILoginCallback callback) {

        //instance
        this.activity = activity;
        this.iLoginCallback = callback;

        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        AuthInfo mAuthInfo = new AuthInfo(activity, WB_APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler(activity, mAuthInfo);

        logoutWeibo();


    }

    private void loginWeibo() {
        //login
        mSsoHandler.authorize(new AuthListener());
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != iLoginCallback && null != mSsoHandler) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            return true;
        }
        return false;
    }

    @Override
    public void OnDestory() {

    }
}
