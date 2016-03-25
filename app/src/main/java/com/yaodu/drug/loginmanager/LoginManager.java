package com.yaodu.drug.loginmanager;

import android.app.Activity;
import android.content.Intent;

import com.yaodu.drug.interfaces.ILogin;
import com.yaodu.drug.interfaces.ILoginCallback;

/**
 * Created by bobomee on 2016/3/25.
 */
public class LoginManager {

    private final Class<? extends ILogin> loginClass;
    private final Activity activity;
    private final ILoginCallback loginCallback;
    private ILogin iLogin;

    public LoginManager(Activity activity, Class<? extends ILogin> loginClass, ILoginCallback loginCallback) {
        this.activity = activity;
        this.loginClass = loginClass;
        this.loginCallback = loginCallback;
    }

    public void doLogin() {

        try {

            iLogin = loginClass.newInstance();
            iLogin.doLogin(activity, loginCallback);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return null != iLogin && iLogin.onActivityResult(requestCode, resultCode, data);
    }

    public void OnDestory(){
        iLogin.OnDestory();
    }
}
