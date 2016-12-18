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
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.bobomee.android.recyclerviewhelper.expandable.TreeNode;
import com.bobomee.android.recyclerviewhelper.expandable.TreeViewBinder;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.bean.Genre;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created on 2016/12/18.下午8:16.
 *
 * @author bobomee.
 */

public class GenreViewBinder extends TreeViewBinder<GenreViewBinder.ViewHolder> {

  @Override public ViewHolder provideViewHolder(View itemView) {
    return new ViewHolder(itemView);
  }

  @Override public void bindView(ViewHolder holder, int position, TreeNode node) {
    Genre genre = (Genre) node.getContent();
    holder.sTextView.setText(genre.getTitle());
    holder.mImageView.setImageResource(genre.getIconResId());

    int height = node.getHeight();//获取高度

    holder.itemView.setPadding(60*height,3,3,3);//设置高度，方便看出层级
  }

  @Override public int getLayoutId() {
    return R.layout.list_item_genre;
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView sTextView;
    private final ImageView sArrow;
    private final ImageView mImageView;

    public ViewHolder(View rootView) {
      super(rootView);

      mImageView = (ImageView) rootView.findViewById(R.id.list_item_genre_icon);
      sTextView = (TextView) rootView.findViewById(R.id.list_item_genre_name);
      sArrow = (ImageView) rootView.findViewById(R.id.list_item_genre_arrow);
    }

    public void animateExpand() {
      RotateAnimation rotate =
          new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
      rotate.setDuration(300);
      rotate.setFillAfter(true);
      sArrow.setAnimation(rotate);
    }

    public void animateCollapse() {
      RotateAnimation rotate =
          new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
      rotate.setDuration(300);
      rotate.setFillAfter(true);
      sArrow.setAnimation(rotate);
    }
  }
}
