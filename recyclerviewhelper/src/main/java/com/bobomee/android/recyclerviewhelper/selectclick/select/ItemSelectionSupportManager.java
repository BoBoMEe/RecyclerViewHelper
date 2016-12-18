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

package com.bobomee.android.recyclerviewhelper.selectclick.select;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/12/18.下午2:54.
 *
 * @author bobomee.
 */

public class ItemSelectionSupportManager {

  private static List<OnItemSelectChangeListener> mOnItemSelectChangeListeners;

  private static List<OnItemSelectListener> mOnItemSelectListeners;

  public interface OnItemSelectChangeListener {
    void onItenSelectChange(StateManager.CheckedStates mCheckedStates);
  }

  public interface OnItemSelectListener {
    void onItemSelect(RecyclerView parent, View view, int position, boolean checked);
  }

  public static void addOnItemSelectChangeListener(
      OnItemSelectChangeListener _onItemSelectChangeListener) {
    if (null == mOnItemSelectChangeListeners) {
      mOnItemSelectChangeListeners = new ArrayList<>();
    }
    mOnItemSelectChangeListeners.add(_onItemSelectChangeListener);
  }

  public static void addOnItemSelectListener(OnItemSelectListener _onItemSelectListener) {
    if (null == mOnItemSelectListeners) {
      mOnItemSelectListeners = new ArrayList<>();
    }
    mOnItemSelectListeners.add(_onItemSelectListener);
  }

  public static void removeOnItemSelectChangeListener(
      OnItemSelectChangeListener _onItemSelectChangeListener) {
    if (null != mOnItemSelectChangeListeners) {
      mOnItemSelectChangeListeners.remove(_onItemSelectChangeListener);
    }
  }

  public static void removeOnItemSelectListener(OnItemSelectListener _onItemSelectListener) {
    if (null != mOnItemSelectListeners) {
      mOnItemSelectListeners.remove(_onItemSelectListener);
    }
  }

  public static void notifyChage(StateManager.CheckedStates mCheckedStates) {
    if (null != mOnItemSelectChangeListeners && !mOnItemSelectChangeListeners.isEmpty()) {
      for (OnItemSelectChangeListener onItemSelectChangeListener : mOnItemSelectChangeListeners) {
        onItemSelectChangeListener.onItenSelectChange(mCheckedStates);
      }
    }
  }

  public static List<OnItemSelectListener> fromSelectListener() {
    return mOnItemSelectListeners;
  }
}
