package com.yaodu.drug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yaodu.drug.model.QQInfoModel;
import com.yaodu.drug.model.QQTokenModel;
import com.yaodu.drug.model.WeixinInfoModel;
import com.yaodu.drug.model.WeixinTokenModel;
import com.yaodu.drug.service.WeixinInfoService;
import com.yaodu.drug.service.WeixinTokenService;
import com.yaodu.drug.sinaapi.LogoutAPI;
import com.yaodu.drug.sinaapi.User;
import com.yaodu.drug.sinaapi.UsersAPI;
import com.yaodu.drug.util.AccessTokenKeeper;
import com.yaodu.drug.util.GsonUtil;
import com.yaodu.drug.util.ThreadManager;
import com.yaodu.drug.util.ToastUtil;

import org.json.JSONObject;

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


    public void login(View v) {
        switch (v.getId()) {
            case R.id.login_weixin:
                weiXinLogin();
                //微信登录
                break;
            case R.id.login_qq:
                loginQQ();
                //QQ登陆
                break;
            case R.id.login_weibo:
                logoutWeibo();
                //新浪微博
                break;

        }
    }

    //====================================QQ====================================================
    private final int INFO = 0;
    private final int TOKEN = 1;
    public static Tencent mTencent;
    BaseUiListener baseUiListener;
    QQTokenModel qqTokenModel;
    QQInfoModel qqInfoModel;

    private void rigistQQ() {
        baseUiListener = new BaseUiListener(this, TOKEN);
        mTencent = Tencent.createInstance(TENCENT_APP_ID, this.getApplicationContext());
    }

    private void loginQQ() {
        mTencent.login(this, "all", baseUiListener);
    }

    public void logoutQQ() {
        mTencent.logout(this);
    }

    class BaseUiListener implements IUiListener {

        private int type;
        private Context context;

        //V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
        public BaseUiListener(Context context, int i) {
            this.context = context;
            this.type = i;
        }

        @Override
        public void onComplete(Object o) {
            JSONObject object = (JSONObject) o;
            doComplete(object);
        }

        //在这里可以做一些登录成功的处理
        protected void doComplete(JSONObject values) {

            if (type == TOKEN) {
                qqTokenModel = GsonUtil.jsonToBean(values.toString(), QQTokenModel.class);
                UserInfo userInfo = new UserInfo(context, mTencent.getQQToken());
                userInfo.getUserInfo(new BaseUiListener(MainActivity.this, INFO));
            } else {
                qqInfoModel = GsonUtil.jsonToBean(values.toString(), QQInfoModel.class);

                runOnUiThread(()->text1.setText(qqInfoModel.toString()));
            }

        }

        //在这里可以做登录失败的处理
        @Override
        public void onError(UiError e) {

        }

        //在这里可以做登录被取消的处理
        @Override
        public void onCancel() {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.RESULT_LOGIN) {
                mTencent.handleLoginData(data, baseUiListener);
            }
        }
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    //====================================weibo====================================================

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    public static Oauth2AccessToken mAccessToken;


    private AuthInfo mAuthInfo;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;


    private void rigistWeibo() {
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(this, WB_APP_KEY, REDIRECT_URL, SCOPE);
        mSsoHandler = new SsoHandler(this, mAuthInfo);
    }

    private void loginWeibo() {
        mSsoHandler.authorize(new AuthListener());
    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

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

                AccessTokenKeeper.writeAccessToken(MainActivity.this, mAccessToken);
                //获取用户信息
                UsersAPI mUserAPI = new UsersAPI(MainActivity.this, WB_APP_KEY, mAccessToken);

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
               ToastUtil.show(MainActivity.this, message);
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
                runOnUiThread(() -> text1.setText(user.toString()));
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    };

    private void logoutWeibo() {
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (0 != mAccessToken.getExpiresTime()) {
            new LogoutAPI(MainActivity.this, WB_APP_KEY,
                    mAccessToken).logout(new RequestListener() {
                @Override
                public void onComplete(String s) {
                    AccessTokenKeeper.clear(MainActivity.this);
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

    //====================================weixin=======================================================


    // 微信相关
    private BaseResp resp;
    public static IWXAPI api;
    public WeixinTokenModel tokenModel;
    private WeixinInfoModel infoModel;


    // 注册到微信
    private void rigistWeixin() {
        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);
    }


    // 微信登陆请求
    private void weiXinLogin() {
        Constant.baseResp = null;
        infoModel = null;
        tokenModel = null;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        api.sendReq(req);//第三方发送消息给微信。

    }

    @Override
    protected void onResume() {
        super.onResume();
        resp = Constant.baseResp;
        if (null != resp && resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            // code返回
            final String weixinCode = ((SendAuth.Resp) resp).code;

            if (!TextUtils.isEmpty(weixinCode)) {
                ThreadManager.getShortPool().execute(() -> {
                    tokenModel = WeixinTokenService.getWeixinToken(weixinCode);
                    if (null != tokenModel) {
                        String accessToken = tokenModel.access_token;
                        String openId = tokenModel.openid;

                        if (!TextUtils.isEmpty(tokenModel.access_token) && !TextUtils.isEmpty(tokenModel.openid)) {
                            infoModel = WeixinInfoService.getWeixinInfo(accessToken, openId);
                            runOnUiThread(() -> text1.setText(infoModel.toString()));
                        }
                    }
                });
            }
        }
    }
    //=============================================================================================

}
