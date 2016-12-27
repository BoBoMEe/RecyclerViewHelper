/*
 * Copyright (C) 2016.  BoBoMEe(wbwjx115@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.bobomee.android.recyclerviewhelperdemo.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import com.bobomee.android.recyclerviewhelper.fastscroll.Utils;
import com.bobomee.android.recyclerviewhelperdemo.R;

/**
 * Created on 2016/12/27.下午11:31.
 *
 * @author bobomee.
 */

public class MyUtils {

  public static String getVersionName(Context context) {
    try {
      PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      return "v" + pInfo.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      return context.getString(android.R.string.unknownName);
    }
  }

  private static int colorAccent = -1;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP) public static int getColorAccent(Context context) {
    if (colorAccent < 0) {
      int accentAttr = Utils.hasLollipop() ? android.R.attr.colorAccent : R.attr.colorAccent;
      TypedArray androidAttr = context.getTheme().obtainStyledAttributes(new int[] { accentAttr });
      colorAccent = androidAttr.getColor(0, 0xFF009688); //Default: material_deep_teal_500
      androidAttr.recycle();
    }
    return colorAccent;
  }

}
