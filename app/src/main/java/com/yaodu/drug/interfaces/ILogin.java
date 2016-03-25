package com.yaodu.drug.interfaces;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by bobomee on 2016/3/25.
 */
public interface ILogin {

    void doLogin(Activity activity,ILoginCallback callback);

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    void OnDestory();

}
