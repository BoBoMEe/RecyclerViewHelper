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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import java.lang.reflect.Method;

public class StateListDrawableHelper {
  private static final Method METHOD_GET_STATE_COUNT;
  private static final Method METHOD_GET_STATE_SET;
  private static final Method METHOD_GET_STATE_DRAWABLE;
  private static final Method METHOD_GET_STATE_DRAWABLE_INDEX;

  static {
    Method getStateCount = null,
        getStateSet = null,
        getStateDrawable = null,
        getStateDrawableIndex = null;

    Class<StateListDrawable> cls = StateListDrawable.class;

    try {
      getStateCount = cls.getDeclaredMethod("getStateCount");
      getStateSet = cls.getDeclaredMethod("getStateSet", int.class);
      getStateDrawable = cls.getDeclaredMethod("getStateDrawable", int.class);
      getStateDrawableIndex = cls.getDeclaredMethod("getStateDrawableIndex", int[].class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    METHOD_GET_STATE_COUNT = getStateCount;
    METHOD_GET_STATE_SET = getStateSet;
    METHOD_GET_STATE_DRAWABLE = getStateDrawable;
    METHOD_GET_STATE_DRAWABLE_INDEX = getStateDrawableIndex;
  }

  private StateListDrawableHelper() {
  }

  public static int getStateCount(StateListDrawable drawable) {
    try {
      return (int) METHOD_GET_STATE_COUNT.invoke(drawable);
    } catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public static int[] getStateSet(StateListDrawable drawable, int index) {
    try {
      return (int[]) METHOD_GET_STATE_SET.invoke(drawable, index);
    } catch (Exception e) {
      e.printStackTrace();
      return new int[0];
    }
  }

  public static Drawable getStateDrawable(StateListDrawable drawable, int index) {
    try {
      return (Drawable) METHOD_GET_STATE_DRAWABLE.invoke(drawable, index);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static int getStateDrawableIndex(StateListDrawable drawable, int[] stateSet) {
    try {
      return (int) METHOD_GET_STATE_DRAWABLE_INDEX.invoke(drawable, (Object) stateSet);
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  public static StateListDrawable getStateListDrawable(Context _context,
      @DrawableRes int drawable) {
    StateListDrawable stateListDrawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      stateListDrawable = (StateListDrawable) _context.getResources().getDrawable(drawable, null);
    } else {
      //noinspection deprecation
      stateListDrawable = (StateListDrawable) _context.getResources().getDrawable(drawable);
    }
    return stateListDrawable;
  }
}
