/*
 * (C) Copyright 2013, 2017 Pharmacodia Technology Co.,Ltd.
 */

package com.bobomee.android.recyclerviewhelper.paginate;

import android.support.v7.widget.GridLayoutManager;

/**
 * Project ID：400YF17050 <br/>
 * Resume:      用于给 Gridview 设置跨度 大小 <br/>
 * 如果是 加载更多的item ,则设置跨度 为所占 span的数目,否则为1
 *
 * @author 汪波
 * @version 1.0
 * @since 2017 /2/14 汪波 first commit
 */
class WrapperSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

  private final GridLayoutManager.SpanSizeLookup wrappedSpanSizeLookup;
  private final LoadingListItemSpanLookup loadingListItemSpanLookup;
  private final WrapperAdapter wrapperAdapter;

  /**
   * 构造函数,构造一个SpanSizeLookup 实例.
   *
   * @param gridSpanSizeLookup the grid span size lookup
   * @param loadingListItemSpanLookup the loading list item span lookup
   * @param wrapperAdapter the wrapper adapter
   */
  public WrapperSpanSizeLookup(GridLayoutManager.SpanSizeLookup gridSpanSizeLookup,
      LoadingListItemSpanLookup loadingListItemSpanLookup, WrapperAdapter wrapperAdapter) {
    this.wrappedSpanSizeLookup = gridSpanSizeLookup;
    this.loadingListItemSpanLookup = loadingListItemSpanLookup;
    this.wrapperAdapter = wrapperAdapter;
  }

  @Override public int getSpanSize(int position) {
    if (wrapperAdapter.isLoadingRow(position)) {
      return loadingListItemSpanLookup.getSpanSize();
    } else {
      return wrappedSpanSizeLookup.getSpanSize(position);
    }
  }

  /**
   * 获取一个SpanSizeLookup实例.
   *
   * @return the wrapped span size lookup
   */
  public GridLayoutManager.SpanSizeLookup getWrappedSpanSizeLookup() {
    return wrappedSpanSizeLookup;
  }
}