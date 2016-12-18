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

package com.bobomee.android.recyclerviewhelperdemo.viewbinds;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.bobomee.android.recyclerviewhelper.expandable.TreeNode;
import com.bobomee.android.recyclerviewhelper.expandable.TreeViewBinder;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.bean.Artist;

/**
 * Created on 2016/12/18.下午8:25.
 *
 * @author bobomee.
 */

public class ArtistViewBind extends TreeViewBinder<ArtistViewBind.ViewHolder> {

  @Override public ViewHolder provideViewHolder(View itemView) {
    return new ViewHolder(itemView);
  }

  @Override public void bindView(ViewHolder holder, int position, TreeNode node) {

    Artist artist = (Artist) node.getContent();
    holder.mTextView.setText(artist.getName());

    int height = node.getHeight();//获取高度

    holder.itemView.setPadding(120*height,3,3,3);//设置高度，方便看出层级
  }

  @Override public int getLayoutId() {
    return R.layout.list_item_artist;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder{

    private final TextView mTextView;

    public ViewHolder(View rootView) {
      super(rootView);
      mTextView = (TextView)rootView.findViewById(R.id.list_item_artist_name);
    }
  }
}
