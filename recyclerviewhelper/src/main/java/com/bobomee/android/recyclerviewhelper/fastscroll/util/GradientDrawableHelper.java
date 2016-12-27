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

package com.bobomee.android.recyclerviewhelper.fastscroll.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;

/**
 * Created on 2016/12/27.上午10:54.
 *
 * @author bobomee.
 */

public class GradientDrawableHelper {

  public static GradientDrawable getGradientDrawable(Context _context, @DrawableRes int drawable) {
    GradientDrawable gradientDrawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      gradientDrawable = (GradientDrawable) _context.getResources().getDrawable(drawable, null);
    } else {
      //noinspection deprecation
      gradientDrawable = (GradientDrawable) _context.getResources().getDrawable(drawable);
    }

    return gradientDrawable;
  }

  public static GradientDrawable setColor(GradientDrawable _gradientDrawable, int color) {
    _gradientDrawable.setColor(color);
    return _gradientDrawable;
  }
}
