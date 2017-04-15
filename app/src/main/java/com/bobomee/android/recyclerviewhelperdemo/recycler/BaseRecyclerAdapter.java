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
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bobomee.android.recyclerviewhelper.fastscroll.interfaces.BubbleTextCreator;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> implements
    BubbleTextCreator {
  protected  List<T> mData;
  protected final Context mContext;
  protected LayoutInflater mInflater;

  protected BaseRecyclerAdapter(List<T> data, Context context) {
    mData = data;
    mContext = context;
    mInflater = LayoutInflater.from(context);
  }

  @Override public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final RecyclerViewHolder holder = new RecyclerViewHolder(mContext,
        mInflater.inflate(getItemLayoutId(viewType), parent, false));
    return holder;
  }

  @Override public void onBindViewHolder(RecyclerViewHolder holder, int position) {
    bindData(holder, position, mData.get(position));
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public void add(int pos, T item) {
    mData.add(pos, item);
    notifyItemInserted(pos);
  }

  public void delete(int pos) {
    if (pos >= 0&& pos< mData.size()){
      mData.remove(pos);
      notifyItemRemoved(pos);
    }
  }

  public void addAll(List<T> items) {
    mData.addAll(mData.size(), items);
    notifyItemRangeInserted(mData.size(), items.size());
  }

  public void setData(List<T> items){
    mData = new ArrayList<>(items);
    notifyDataSetChanged();
  }

  public void clear(){
    mData.clear();
    notifyDataSetChanged();
  }

  abstract public void bindData(RecyclerViewHolder holder, int position, T item);

  abstract public int getItemLayoutId(int viewType);
}
