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

package com.bobomee.android.recyclerviewhelper.selectclick.click;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/12/18.下午2:43.
 *
 * @author bobomee.
 */

public class ItemClickSupportManager {
  /*
   * Interface definition for a callback to be invoked when an item in the
   * RecyclerView has been clicked.
   */
  public interface OnItemClickListener {
    /**
     * Callback method to be invoked when an item in the RecyclerView
     * has been clicked.
     *
     * @param parent The RecyclerView where the click happened.
     * @param view The view within the RecyclerView that was clicked
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     */
    void onItemClick(RecyclerView parent, View view, int position, long id);
  }

  /**
   * Interface definition for a callback to be invoked when an item in the
   * RecyclerView has been clicked and held.
   */
  public interface OnItemLongClickListener {
    /**
     * Callback method to be invoked when an item in the RecyclerView
     * has been clicked and held.
     *
     * @param parent The RecyclerView where the click happened
     * @param view The view within the RecyclerView that was clicked
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     * @return true if the callback consumed the long click, false otherwise
     */
    boolean onItemLongClick(RecyclerView parent, View view, int position, long id);
  }

  private static List<OnItemClickListener> mOnItemClickListeners;
  private static List<OnItemLongClickListener> mOnItemLongClickListeners;

  public static void addOnItemClickListener(OnItemClickListener _onItemClickListener) {
    if (null == mOnItemClickListeners) {
      mOnItemClickListeners = new ArrayList<>();
    }
    mOnItemClickListeners.add(_onItemClickListener);
  }

  public static void addOnItemLongClickListener(OnItemLongClickListener _onItemLongClickListener) {
    if (null == mOnItemLongClickListeners) {
      mOnItemLongClickListeners = new ArrayList<>();
    }
    mOnItemLongClickListeners.add(_onItemLongClickListener);
  }

  public static void removeOnItemLongClickListener(
      OnItemLongClickListener _onItemLongClickListener) {
    if (null != mOnItemLongClickListeners) {
      mOnItemLongClickListeners.remove(_onItemLongClickListener);
    }
  }

  public static void removeOnItemClickListener(OnItemClickListener _onItemClickListener) {
    if (null != mOnItemClickListeners) {
      mOnItemClickListeners.remove(_onItemClickListener);
    }
  }

  public static List<OnItemClickListener> fromClickListener() {
    return mOnItemClickListeners;
  }

  public static List<OnItemLongClickListener> fromLongClickListener() {
    return mOnItemLongClickListeners;
  }
}
