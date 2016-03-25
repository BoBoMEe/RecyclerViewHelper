package com.yaodu.drug.loginmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yaodu.drug.interfaces.IConstant;
import com.yaodu.drug.interfaces.ILogin;
import com.yaodu.drug.interfaces.ILoginCallback;
import com.yaodu.drug.model.QQInfoModel;
import com.yaodu.drug.model.QQTokenModel;
import com.yaodu.drug.util.GsonUtil;

import org.json.JSONObject;

/**
 * Created by bobomee on 2016/3/25.
 */
public class QQLogin implements ILogin, IConstant {

    private final int INFO = 0;
    private final int TOKEN = 1;
    private static Tencent mTencent;
    private BaseUiListener baseUiListener;

    private Activity activity;
    private ILoginCallback iLoginCallback;

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
                QQTokenModel qqTokenModel = GsonUtil.jsonToBean(values.toString(), QQTokenModel.class);
                if (null != iLoginCallback) iLoginCallback.tokeCallBack(qqTokenModel);
                UserInfo userInfo = new UserInfo(context, mTencent.getQQToken());
                userInfo.getUserInfo(new BaseUiListener(activity, INFO));
            } else {
                QQInfoModel qqInfoModel = GsonUtil.jsonToBean(values.toString(), QQInfoModel.class);
                if (null != iLoginCallback) iLoginCallback.infoCallBack(qqInfoModel);
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
    public void doLogin(Activity activity, ILoginCallback callback) {

        //instance
        this.activity = activity;
        this.iLoginCallback = callback;

        baseUiListener = new BaseUiListener(activity, TOKEN);
        mTencent = Tencent.createInstance(TENCENT_APP_ID, activity);

        //login
        login();

    }

    private void login() {
        mTencent.login(activity, "all", baseUiListener);
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_API && resultCode == Constants.RESULT_LOGIN && null != iLoginCallback) {
            mTencent.handleLoginData(data, baseUiListener);
            return true;
        }
        return false;
    }

    @Override
    public void OnDestory() {

    }
}
