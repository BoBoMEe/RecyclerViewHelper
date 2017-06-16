/*
 * (C) Copyright 2013, 2017 Pharmacodia Technology Co.,Ltd.
 */

package com.bobomee.android.recyclerviewhelper.paginate;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Project ID：400YF17050 <br/>
 * Resume:     Adapter的包装类 ,区分 是 Loading的 item ,还是 正常 的 item<br/>
 *
 * @author 汪波
 * @version 1.0
 * @since 2017 /2/14 汪波 first commit
 */
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int ITEM_VIEW_TYPE_LOADING = Integer.MAX_VALUE - 50; // Magic
  private static final int ITEM_VIEW_TYPE_NO_DATA = Integer.MAX_VALUE - 51; // Magic

  private final RecyclerView.Adapter mWrappedAdapter;
  private final LoadingListItemCreator loadingListItemCreator;
  private final LoadingListItemCreator noDataTip;
  private boolean mDisplayLoadingRow = true;
  private boolean mDisplayNoDataRow;

  /**
   * 构造函数,构造一个 Wrapper adapter.
   *
   * @param wrappedAdapter the wrapped adapter
   */
  public WrapperAdapter(RecyclerView.Adapter wrappedAdapter, Paginate.Callbacks callbacks) {
    this.mWrappedAdapter = wrappedAdapter;
    loadingListItemCreator = new LoadingListItemCreator.DefalutLoadingListItemCreator(callbacks);
    noDataTip = null;
  }

  /**
   * 构造函数,构造一个 Wrapper adapter.
   *
   * @param adapter the adapter
   * @param creator the creator
   */
  public WrapperAdapter(RecyclerView.Adapter adapter, LoadingListItemCreator creator) {
    this.mWrappedAdapter = adapter;
    this.loadingListItemCreator = creator;
    noDataTip = null;
  }

  /**
   * 构造函数,构造一个 Wrapper adapter.
   *
   * @param adapter the adapter
   * @param creator the creator
   */
  public WrapperAdapter(RecyclerView.Adapter adapter, LoadingListItemCreator creator,
      LoadingListItemCreator noData) {
    this.mWrappedAdapter = adapter;
    this.loadingListItemCreator = creator;
    noDataTip = noData;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_VIEW_TYPE_LOADING) {
      return loadingListItemCreator.onCreateViewHolder(parent, viewType);
    } else if (viewType == ITEM_VIEW_TYPE_NO_DATA && null != noDataTip) {
      return noDataTip.onCreateViewHolder(parent, viewType);
    } else {
      return mWrappedAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    int type = getItemViewType(position);
    if (type == ITEM_VIEW_TYPE_LOADING) {
      loadingListItemCreator.onBindViewHolder(holder, position);
    } else if (type == ITEM_VIEW_TYPE_NO_DATA && noDataTip != null) {
      noDataTip.onBindViewHolder(holder, position);
    } else {
      mWrappedAdapter.onBindViewHolder(holder, position);
    }
  }

  @Override public int getItemCount() {
    int itemCount = mWrappedAdapter.getItemCount();
    if (itemCount > 0 && (mDisplayLoadingRow || mDisplayNoDataRow)) {
      return itemCount + 1;
    } else {
      return itemCount;
    }
  }

  @Override public int getItemViewType(int position) {
    if (isLoadingRow(position)) {
      return ITEM_VIEW_TYPE_LOADING;
    } else if (isNoDataRow(position)) {
      return ITEM_VIEW_TYPE_NO_DATA;
    } else {
      return mWrappedAdapter.getItemViewType(position);
    }
  }

  @Override public long getItemId(int position) {
    if (isLoadingRow(position)) {
      return RecyclerView.NO_ID;
    } else if (isNoDataRow(position)) {
      return RecyclerView.NO_ID;
    } else {
      return mWrappedAdapter.getItemId(position);
    }
  }

  @Override public void setHasStableIds(boolean hasStableIds) {
    super.setHasStableIds(hasStableIds);
    mWrappedAdapter.setHasStableIds(hasStableIds);
  }

  /**
   * 获取一个 adapter包装类实例.
   *
   * @return the wrapped adapter
   */
  public RecyclerView.Adapter getWrappedAdapter() {
    return mWrappedAdapter;
  }

  /**
   * 是否展示 加载中的 条目.
   *
   * @return the boolean
   */
  boolean isDisplayLoadingRow() {
    return mDisplayLoadingRow;
  }

  void displayRow(boolean displayLoadingRow, boolean displayNoDataRow) {
    boolean isDisplayLoadingRowChanged = displayLoadingRow(displayLoadingRow);
    boolean isDisplayNoDataRowChanged = displayNoDataRow(displayNoDataRow);
    if (isDisplayLoadingRowChanged || isDisplayNoDataRowChanged) {
      notifyDataSetChanged();
    }
  }

  /**
   * 展示 加载中的 条目.
   *
   * @param displayLoadingRow the display loading row
   * @return 是否修改
   */
  private boolean displayLoadingRow(boolean displayLoadingRow) {
    if (this.mDisplayLoadingRow != displayLoadingRow) {
      this.mDisplayLoadingRow = displayLoadingRow;
      return true;
    }
    return false;
  }

  private boolean displayNoDataRow(boolean displayNoDataRow) {
    if (canDisplayNoDataRow()) {
      mDisplayNoDataRow = displayNoDataRow;
      return true;
    }
    return false;
  }

  private boolean canDisplayNoDataRow() {
    return this.noDataTip != null;
  }

  /**
   * 判断当前 pos 是否是 加载中的item.
   *
   * @param position the position
   * @return the boolean
   */
  boolean isLoadingRow(int position) {
    return mDisplayLoadingRow && position == getLoadingRowPosition();
  }

  private boolean isNoDataRow(int position) {
    return mDisplayNoDataRow && position == getNoDataRowPosition();
  }

  private int getNoDataRowPosition() {
    return mDisplayNoDataRow ? getItemCount() - 1 : -1;
  }

  private int getLoadingRowPosition() {
    return mDisplayLoadingRow ? getItemCount() - 1 : -1;
  }
}