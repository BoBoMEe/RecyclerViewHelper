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

package com.bobomee.android.recyclerviewhelper.itemclick;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemClickSupport {

  private final RecyclerView mRecyclerView;
  private final TouchListener mTouchListener;
  private final ItemClick mItemClick;
  private final ItemLongClick mItemLongClick;

  public static ItemClickSupport from(RecyclerView _recyclerView) {
    if (null == _recyclerView) return null;
    return new ItemClickSupport(_recyclerView);
  }

  public ItemClickSupport remove() {
    mRecyclerView.removeOnItemTouchListener(mTouchListener);
    return this;
  }

  public ItemClickSupport add() {
    mRecyclerView.addOnItemTouchListener(mTouchListener);
    return this;
  }

  private ItemClickSupport(RecyclerView recyclerView) {
    mRecyclerView = recyclerView;

    mTouchListener = new TouchListener(recyclerView);

    mItemClick = new ItemClick();
    mItemLongClick = new ItemLongClick();

  }

  /**
   * Register a callback to be invoked when an item in the
   * RecyclerView has been clicked.
   *
   * @param listener The callback that will be invoked.
   */
  public void addOnItemClickListener(ItemClick.OnItemClickListener listener) {
    mItemClick.addListener(listener);
  }

  /**
   * Register a callback to be invoked when an item in the
   * RecyclerView has been clicked and held.
   *
   * @param listener The callback that will be invoked.
   */
  public void addOnItemLongClickListener(ItemLongClick.OnItemLongClickListener listener) {
    if (!mRecyclerView.isLongClickable()) {
      mRecyclerView.setLongClickable(true);
    }
    mItemLongClick.addListener(listener);
  }

  private class TouchListener extends ItemTouchListener {
    TouchListener(RecyclerView recyclerView) {
      super(recyclerView);
    }

    @Override
    public boolean performItemClick(RecyclerView parent, View view, int position, long id) {

      if (mItemClick.hasListener()) {
        mItemClick.click(parent, view, position, id);
        return true;
      }

      return false;
    }

    @Override
    public boolean performItemLongClick(RecyclerView parent, View view, int position, long id) {

      if (mItemLongClick.hasListener()) {
        mItemLongClick.longClick(parent, view, position, id);
        return true;
      }

      return false;
    }
  }
}
