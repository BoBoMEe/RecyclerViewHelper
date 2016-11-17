package com.bobomee.android.recyclerviewhelperdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
  protected final List<T> mData;
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
    mData.remove(pos);
    notifyItemRemoved(pos);
  }

  abstract public void bindData(RecyclerViewHolder holder, int position, T item);

  abstract public int getItemLayoutId(int viewType);
}
