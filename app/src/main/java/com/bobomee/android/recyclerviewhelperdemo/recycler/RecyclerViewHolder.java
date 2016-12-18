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

package com.bobomee.android.recyclerviewhelperdemo.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
  private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
  private Context mContext;//上下文对象

  public RecyclerViewHolder(Context context, View itemView) {
    super(itemView);
    mContext = context;
    mViews = new SparseArray<View>();
  }

  private <T extends View> T findViewById(int viewId) {
    View view = mViews.get(viewId);
    if (view == null) {
      view = itemView.findViewById(viewId);
      mViews.put(viewId, view);
    }
    return (T) view;
  }

  public View getView(int viewId) {
    return findViewById(viewId);
  }

  public TextView getTextView(int viewId) {
    return (TextView) getView(viewId);
  }

  public Button getButton(int viewId) {
    return (Button) getView(viewId);
  }

  public ImageView getImageView(int viewId) {
    return (ImageView) getView(viewId);
  }

  public ImageButton getImageButton(int viewId) {
    return (ImageButton) getView(viewId);
  }

  public EditText getEditText(int viewId) {
    return (EditText) getView(viewId);
  }

  public RecyclerViewHolder setText(int viewId, String value) {
    TextView view = findViewById(viewId);
    view.setText(value);
    return this;
  }

  public RecyclerViewHolder setBackground(int viewId, int resId) {
    View view = findViewById(viewId);
    view.setBackgroundResource(resId);
    return this;
  }
}
