/*
 * (C) Copyright 2013, 2017 Pharmacodia Technology Co.,Ltd.
 */

package com.bobomee.android.recyclerviewhelperdemo.fragment;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import com.bobomee.android.recyclerviewhelper.paginate.Paginate;

/**
 * Project ID：400YF17050
 * Resume:     自带分页信息<br/>
 *
 * @author 汪波
 * @version 1.0
 * @since 2017/3/13.汪波.
 */
public abstract class BasePaginationFragment extends Fragment implements Paginate.Callbacks {

  public abstract RecyclerView providerRecyclerview();//provider recyclerview

  protected boolean mLoading = false;                 //是否正在加载

  protected Handler mHandler = new Handler();         //Handler
  protected Paginate mPaginate;                       //分页工具

  protected int mCurrentPage = 0;                     //当前页数
  protected int mTotalPage;                           //总页数
  protected int mTotalItemCount;                      //总条目

  protected boolean mIsRequested = false;             //是否请求过

  protected void loadError() {
    mIsRequested = false;
    if (mTotalPage == 0 && mCurrentPage == 0) {
      //第一次加载
      return;
    }
    mPaginate.setHasMoreDataToLoad(false);
    mLoading = false;
  }

  protected void loadComplete() {
    mIsRequested = true;

    mLoading = false;
  }

  /**
   * 设置分页加载器
   */
  protected void setupPagination() {
    if (mPaginate != null) {
      mPaginate.unbind();
    }
    resetPagination();
    mPaginate = Paginate.with(providerRecyclerview(), this).build();

    mPaginate.setHasMoreDataToLoad(false);
  }

  protected void resetPagination() {
    mHandler.removeCallbacks(fakeCallback);
    mLoading = false;
    mCurrentPage = 0;
    mTotalPage = 0;
    mIsRequested = false;
  }

  /**
   * 重新加载数据的runnable
   */
  private Runnable fakeCallback = new Runnable() {
    @Override public void run() {
      mCurrentPage++;
      // 重新请求
      requestMoreData();
      mLoading = false;
    }
  };

  public abstract void requestMoreData();

  /**
   * 加载更多功能
   */
  @Override public void onLoadMore() {
    mLoading = true;
    mHandler.post(fakeCallback);
  }

  /**
   * 是否是正在加载中。。。
   *
   * @return true, 正在加载
   */
  @Override public boolean isLoading() {
    return mTotalItemCount == 0 || mLoading;
  }

  /**
   * 是否加载完成
   *
   * @return true，加载完成
   */
  @Override public boolean hasLoadedAllItems() {
    return mTotalItemCount == 0 || mCurrentPage == mTotalPage - 1;
  }

  /**
   * 页面关闭时，停止runnable
   */
  @Override public void onDestroy() {
    if (mHandler != null && fakeCallback != null) mHandler.removeCallbacks(fakeCallback);
    super.onDestroy();
  }
}
