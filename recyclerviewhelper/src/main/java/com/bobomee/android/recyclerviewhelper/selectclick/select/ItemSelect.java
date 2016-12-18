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
import com.bobomee.android.recyclerviewhelper.listener.ListenerImpl;
import java.util.List;

/**
 * Created on 2016/12/18.下午6:30.
 *
 * @author bobomee.
 */

public class ItemSelect extends ListenerImpl<ItemSelect.OnItemSelectListener> {

  public static interface OnItemSelectListener {
    void onItemSelect(RecyclerView parent, View view, int position, boolean checked);
  }

  public void select(RecyclerView parent, View view, int position, boolean checked) {
    List<OnItemSelectListener> from = from();
    if (null != from && !from.isEmpty()) {
      for (OnItemSelectListener selectListener : from) {
        selectListener.onItemSelect(parent, view, position, checked);
      }
    }
  }
}
