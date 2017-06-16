/*
 * (C) Copyright 2013, 2017 Pharmacodia Technology Co.,Ltd.
 */

package com.bobomee.android.recyclerviewhelper.paginate;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Project ID：400YF17050 <br/>
 * Resume:     Recyclerview的分页功能 <br/>
 *
 * @author 汪波
 * @version 1.0
 * @since 2017 /2/14 汪波 first commit
 */
public final class RecyclerPaginate extends Paginate {

  private final RecyclerView recyclerView;
  private final Callbacks callbacks;
  private final int loadingTriggerThreshold;
  private WrapperAdapter wrapperAdapter;
  private WrapperSpanSizeLookup wrapperSpanSizeLookup;
  private boolean mAutoLoading = true;

  /**
   * 构造函数,构造一个RecyclerPaginate实例.
   *
   * @param recyclerView the recycler view
   * @param callbacks the callbacks
   * @param loadingTriggerThreshold the loading trigger threshold
   * @param addLoadingListItem the add loading list item
   * @param loadingListItemSpanLookup the loading list item span lookup
   */
  RecyclerPaginate(RecyclerView recyclerView, Callbacks callbacks, int loadingTriggerThreshold,
      boolean addLoadingListItem, LoadingListItemSpanLookup loadingListItemSpanLookup,
      boolean autoLoading) {
    this.recyclerView = recyclerView;
    this.callbacks = callbacks;
    this.loadingTriggerThreshold = loadingTriggerThreshold;
    this.mAutoLoading = autoLoading;

    // Attach scrolling listener in order to perform end offset check on each scroll event
    recyclerView.addOnScrollListener(mOnScrollListener);

    if (addLoadingListItem) {
      // Wrap existing adapter with new adapter that will add loading row
      wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
      wrapperAdapter.getWrappedAdapter().registerAdapterDataObserver(mDataObserver);

      // For GridLayoutManager use separate/customisable span lookup for loading row
      if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
        wrapperSpanSizeLookup = new WrapperSpanSizeLookup(
            ((GridLayoutManager) recyclerView.getLayoutManager()).getSpanSizeLookup(),
            loadingListItemSpanLookup, wrapperAdapter);
        ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(
            wrapperSpanSizeLookup);
      }
    }

    // Trigger initial check since adapter might not have any items initially so no scrolling events upon
    // RecyclerView (that triggers check) will occur
    checkEndOffset();
  }

  @Override public void setHasMoreDataToLoad(boolean hasMoreDataToLoad) {
    if (wrapperAdapter != null) {
      wrapperAdapter.displayRow(hasMoreDataToLoad, false);
    }
  }

  @Override public void unbind() {
    recyclerView.removeOnScrollListener(mOnScrollListener);   // Remove scroll listener
    if (recyclerView.getAdapter() instanceof WrapperAdapter) {
      WrapperAdapter wrapperAdapter = (WrapperAdapter) recyclerView.getAdapter();
      RecyclerView.Adapter adapter = wrapperAdapter.getWrappedAdapter();
      adapter.unregisterAdapterDataObserver(mDataObserver); // Remove data observer
      recyclerView.setAdapter(adapter);                     // Swap back original adapter
    }
    if (recyclerView.getLayoutManager() instanceof GridLayoutManager
        && wrapperSpanSizeLookup != null) {
      // Swap back original SpanSizeLookup
      GridLayoutManager.SpanSizeLookup spanSizeLookup =
          wrapperSpanSizeLookup.getWrappedSpanSizeLookup();
      ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanSizeLookup(spanSizeLookup);
    }
  }

  /**
   * 检查是否超过边界.
   */
  private void checkEndOffset() {
    int visibleItemCount = recyclerView.getChildCount();
    int totalItemCount = recyclerView.getLayoutManager().getItemCount();

    int firstVisibleItemPosition;
    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
      firstVisibleItemPosition =
          ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
      // https://code.google.com/p/android/issues/detail?id=181461
      if (recyclerView.getLayoutManager().getChildCount() > 0) {
        firstVisibleItemPosition =
            ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(
                null)[0];
      } else {
        firstVisibleItemPosition = 0;
      }
    } else {
      throw new IllegalStateException(
          "LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager");
    }

    // Check if end of the list is reached (counting threshold) or if there is no items at all
    if ((totalItemCount > visibleItemCount && (totalItemCount - visibleItemCount) <= (
        firstVisibleItemPosition
            + loadingTriggerThreshold)) || totalItemCount == 0) {
      if (!callbacks.isLoading() && !callbacks.hasLoadedAllItems()) {
        if (mAutoLoading) {
          callbacks.onLoadMore();
        }
      }
    }
  }

  /**
   * adapter改变
   */
  private void onAdapterDataChanged() {
    boolean hasLoadedAllItems = callbacks.hasLoadedAllItems();
    wrapperAdapter.displayRow(!hasLoadedAllItems, hasLoadedAllItems);
    checkEndOffset();
  }

  /**
   * 列表滑动监听器
   */
  private final RecyclerView.OnScrollListener mOnScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          checkEndOffset(); // Each time when list is scrolled check if end of the list is reached
        }
      };

  /**
   * 数据改变监听器
   */
  private final RecyclerView.AdapterDataObserver mDataObserver =
      new RecyclerView.AdapterDataObserver() {
        @Override public void onChanged() {
          wrapperAdapter.notifyDataSetChanged();
          onAdapterDataChanged();
        }

        @Override public void onItemRangeInserted(int positionStart, int itemCount) {
          wrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
          onAdapterDataChanged();
        }

        @Override public void onItemRangeChanged(int positionStart, int itemCount) {
          wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
          onAdapterDataChanged();
        }

        @Override public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
          wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
          onAdapterDataChanged();
        }

        @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
          wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
          onAdapterDataChanged();
        }

        @Override public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
          wrapperAdapter.notifyItemMoved(fromPosition, toPosition);
          onAdapterDataChanged();
        }
      };

  /**
   * Project ID：400YF17050 <br/>
   * Resume:     构造器 <br/>
   *
   * @author 汪波
   * @version 1.0
   * @since 2017/2/14 汪波 first commit
   */
  public static class Builder {

    private final RecyclerView recyclerView;
    private final Paginate.Callbacks callbacks;

    private int loadingTriggerThreshold = 0;
    private boolean addLoadingListItem = true;
    private LoadingListItemSpanLookup loadingListItemSpanLookup;

    private boolean autoLoading = true;//是自动加载，还是点击加载

    /**
     * 构造函数,构造一个Builder实例.
     *
     * @param recyclerView the recycler view
     * @param callbacks the callbacks
     */
    public Builder(RecyclerView recyclerView, Paginate.Callbacks callbacks) {
      this.recyclerView = recyclerView;
      this.callbacks = callbacks;
    }

    /**
     * Set the offset from the end of the list at which the load more event needs to be triggered.
     * Default offset
     * if 5.
     *
     * @param threshold number of items from the end of the list.
     * @return {@link RecyclerPaginate.Builder}
     */
    public Builder setLoadingTriggerThreshold(int threshold) {
      this.loadingTriggerThreshold = threshold;
      return this;
    }

    /**
     * Setup loading row. If loading row is used original adapter set on RecyclerView will be
     * wrapped with
     * internal adapter that will add loading row as the last item in the list. Paginate will
     * observer the
     * changes upon original adapter and remove loading row if there is no more data to load. By
     * default loading
     * row will be added.
     *
     * @param addLoadingListItem true if loading row needs to be added, false otherwise.
     * @return {@link RecyclerPaginate.Builder}
     * @see {@link Paginate.Callbacks#hasLoadedAllItems()}
     */
    public Builder addLoadingListItem(boolean addLoadingListItem) {
      this.addLoadingListItem = addLoadingListItem;
      return this;
    }

    /**
     * Set custom SpanSizeLookup for loading list item. Use this when {@link GridLayoutManager} is
     * used and
     * loading list item needs to have custom span. Full span of {@link GridLayoutManager} is used
     * by default.
     *
     * @param loadingListItemSpanLookup LoadingListItemSpanLookup that will be called for loading
     * list item span.
     * @return {@link RecyclerPaginate.Builder}
     */
    public Builder setLoadingListItemSpanSizeLookup(
        LoadingListItemSpanLookup loadingListItemSpanLookup) {
      this.loadingListItemSpanLookup = loadingListItemSpanLookup;
      return this;
    }

    public Builder setAutoLoading(boolean autoLoading) {
      this.autoLoading = autoLoading;
      return this;
    }

    /**
     * 构造一个Recyclerview的分页加载器.
     *
     * @return {@link Paginate} instance.
     */
    public Paginate build() {
      if (recyclerView.getAdapter() == null) {
        throw new IllegalStateException("Adapter needs to be set!");
      }
      if (recyclerView.getLayoutManager() == null) {
        throw new IllegalStateException("LayoutManager needs to be set on the RecyclerView");
      }

      if (loadingListItemSpanLookup == null) {
        loadingListItemSpanLookup =
            new DefaultLoadingListItemSpanLookup(recyclerView.getLayoutManager());
      }

      return new RecyclerPaginate(recyclerView, callbacks, loadingTriggerThreshold,
          addLoadingListItem, loadingListItemSpanLookup, autoLoading);
    }
  }
}