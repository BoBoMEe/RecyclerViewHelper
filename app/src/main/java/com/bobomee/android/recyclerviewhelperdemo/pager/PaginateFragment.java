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

package com.bobomee.android.recyclerviewhelperdemo.pager;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import com.bobomee.android.recyclerviewhelper.paginate.Paginate;
import com.bobomee.android.recyclerviewhelperdemo.DataProvider;

/**
 * Created on 2016/12/9.下午1:31.
 *
 * @author bobomee.
 *         https://github.com/BoBoMEe
 */

public class PaginateFragment extends ItemSelectFragment
    implements Paginate.Callbacks, SwipeRefreshLayout.OnRefreshListener {

  private boolean loading = false;
  private int page = 0;
  private Handler handler;
  private Paginate paginate;

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    handler = new Handler();
    setupPagination();

    setUpRefreshLayout();
  }

  private void setUpRefreshLayout() {
    mSwipeRefreshLayout.setOnRefreshListener(this);
  }

  protected void setupPagination() {
    // If RecyclerView was recently bound, unbind
    if (paginate != null) {
      paginate.unbind();
    }
    reset();
    paginate = Paginate.with(recyclerView, this).build();
  }

  private void reset() {
    handler.removeCallbacks(fakeCallback);
    loading = false;
    page = 0;
  }

  private Runnable fakeCallback = new Runnable() {
    @Override public void run() {
      page++;
      mBaseRecyclerAdapter.addAll(DataProvider.provide(mBaseRecyclerAdapter.getItemCount()));
      loading = false;
    }
  };

  @Override public void onLoadMore() {
    loading = true;
    handler.postDelayed(fakeCallback, 3000L);
  }

  @Override public boolean isLoading() {
    return loading;
  }

  @Override public boolean hasLoadedAllItems() {
    return page == 3;
  }

  @Override public void onRefresh() {
    reset();
    paginate.setHasMoreDataToLoad(false);
    handler.postDelayed(refreshCallBack, 3000L);
  }

  private Runnable refreshCallBack = new Runnable() {
    @Override public void run() {
      mBaseRecyclerAdapter.clear();
      mBaseRecyclerAdapter.addAll(DataProvider.provide(mBaseRecyclerAdapter.getItemCount()));
      mSwipeRefreshLayout.setRefreshing(false);
    }
  };
}
