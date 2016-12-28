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

package com.bobomee.android.recyclerviewhelper.fastscroll;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.TextView;

public final class Utils {

  public static int getValueInRange(int min, int max, int value) {
    int minimum = Math.max(min, value);
    return Math.min(minimum, max);
  }

  public static void setTextBackground(TextView _textView,Drawable _drawable){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      _textView.setBackground(_drawable);
    } else {
      //noinspection deprecation
      _textView.setBackgroundDrawable(_drawable);
    }

  }
}

