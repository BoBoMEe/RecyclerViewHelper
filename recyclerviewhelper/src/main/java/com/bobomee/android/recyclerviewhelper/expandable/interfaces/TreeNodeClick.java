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

package com.bobomee.android.recyclerviewhelper.expandable.interfaces;

import android.support.v7.widget.RecyclerView;
import com.bobomee.android.recyclerviewhelper.expandable.TreeNode;
import com.bobomee.android.recyclerviewhelper.listener.ListenerImpl;
import java.util.List;

/**
 * Created on 2016/12/18.下午7:50.
 *
 * @author bobomee.
 */

public class TreeNodeClick extends ListenerImpl<OnTreeNodeClickListener> {

  public void onNodeClick(TreeNode node, RecyclerView.ViewHolder holder) {
    List<OnTreeNodeClickListener> from = from();
    if (hasListener()) {
      for (OnTreeNodeClickListener onTreeNodeClickListener : from) {
        onTreeNodeClickListener.onClick(node, holder);
      }
    }
  }
}
