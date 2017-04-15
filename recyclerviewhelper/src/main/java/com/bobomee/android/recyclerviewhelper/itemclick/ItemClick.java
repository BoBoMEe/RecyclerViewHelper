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
import com.bobomee.android.recyclerviewhelper.listener.ListenerImpl;
import java.util.List;

/**
 * Created on 2016/12/18.下午7:36.
 *
 * @author bobomee.
 */

public class ItemClick extends ListenerImpl<ItemClick.OnItemClickListener> {
  /*
  * Interface definition for a callback to be invoked when an item in the
  * RecyclerView has been clicked.
  */
  public static interface OnItemClickListener {
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

  public void click(RecyclerView parent, View view, int position, long id) {
    List<OnItemClickListener> from = from();
    if (null != from && !from.isEmpty()) {
      for (OnItemClickListener onItemClickListener : from) {
        onItemClickListener.onItemClick(parent, view, position, id);
      }
    }
  }
}
