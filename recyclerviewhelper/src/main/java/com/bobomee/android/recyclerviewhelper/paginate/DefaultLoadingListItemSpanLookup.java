/*
 * (C) Copyright 2013, 2017 Pharmacodia Technology Co.,Ltd.
 */

package com.bobomee.android.recyclerviewhelper.paginate;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 默认的加载 SpanLookup
 */
class DefaultLoadingListItemSpanLookup implements LoadingListItemSpanLookup {

  private final int loadingListItemSpan;

  /**
   * 构造函数.
   *
   * @param layoutManager the layout manager
   */
  public DefaultLoadingListItemSpanLookup(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof GridLayoutManager) {
      // By default full span will be used for loading list item
      loadingListItemSpan = ((GridLayoutManager) layoutManager).getSpanCount();
    } else {
      loadingListItemSpan = 1;
    }
  }

  @Override public int getSpanSize() {
    return loadingListItemSpan;
  }
}