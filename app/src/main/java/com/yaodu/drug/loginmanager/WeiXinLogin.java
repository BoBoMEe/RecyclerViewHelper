package com.yaodu.drug.loginmanager;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yaodu.drug.interfaces.IConstant;
import com.yaodu.drug.interfaces.ILogin;
import com.yaodu.drug.interfaces.ILoginCallback;
import com.yaodu.drug.model.WeixinInfoModel;
import com.yaodu.drug.model.WeixinTokenModel;
import com.yaodu.drug.service.WeixinInfoService;
import com.yaodu.drug.service.WeixinTokenService;
import com.yaodu.drug.util.ThreadManager;

/**
 * Created by bobomee on 2016/3/25.
 */
public class WeiXinLogin implements ILogin, IConstant, ObserverManager.MRObserver {

    // 微信相关
    public static IWXAPI api;

    private ILoginCallback iLoginCallback;

    public WeiXinLogin() {

        ObserverManager.getInstance().addObserver(WEIXINLOGINOBSERVER, this);
    }

    @Override
    public void doLogin(Activity activity, ILoginCallback callback) {
        this.iLoginCallback = callback;

        api = WXAPIFactory.createWXAPI(activity, APP_ID);
        api.registerApp(APP_ID);

        login();

    }

    private void login() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        api.sendReq(req);//第三方发送消息给微信。
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (iLoginCallback != null) {
            return true;
        }
        return false;
    }

    @Override
    public void OnDestory() {
        ObserverManager.getInstance().removeObserver(this);
    }

    @Override
    public void notify(String name, Object sender, Object data) {

        if (name.equals(WEIXINLOGINOBSERVER)) {

            String code = (String) data;

            if (TextUtils.isEmpty(code)) return;
            ThreadManager.getShortPool().execute(new Runnable() {
                @Override
                public void run() {
                    WeixinTokenModel tokenModel = WeixinTokenService.getWeixinToken(code);

                    if (null != iLoginCallback) iLoginCallback.tokeCallBack(tokenModel);

                    if (null != tokenModel) {
                        String accessToken = tokenModel.access_token;
                        String openId = tokenModel.openid;

                        if (!TextUtils.isEmpty(tokenModel.access_token) && !TextUtils.isEmpty(tokenModel.openid)) {
                            WeixinInfoModel infoModel = WeixinInfoService.getWeixinInfo(accessToken, openId);
                            if (null != iLoginCallback) iLoginCallback.infoCallBack(infoModel);
                        }
                    }
                     ObserverManager.getInstance().removeObserver(this);
                }
            });
        }
    }

}
