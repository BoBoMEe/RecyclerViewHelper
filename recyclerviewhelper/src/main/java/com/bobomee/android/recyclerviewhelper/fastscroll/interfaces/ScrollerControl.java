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

package com.bobomee.android.recyclerviewhelper.fastscroll.interfaces;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.bobomee.android.recyclerviewhelper.R;
import com.bobomee.android.recyclerviewhelper.fastscroll.FastScroller;
import com.bobomee.android.recyclerviewhelper.fastscroll.RecyclerFastScroller;
import com.bobomee.android.recyclerviewhelper.fastscroll.Utils;

/**
 * Created on 2016/12/26.下午2:28.
 *
 * @author bobomee.
 */

public class ScrollerControl {

  private final RecyclerFastScroller mFastScroller;
  private final RecyclerView mRecyclerView;

  private OnScrollStateChange mOnScrollStateChange;

  public ScrollerControl(@NonNull RecyclerView mRecyclerView, @NonNull RecyclerFastScroller mFastScroller) {
    this.mFastScroller = mFastScroller;
    this.mRecyclerView = mRecyclerView;
    mOnScrollStateChange = new OnScrollStateChange();
    mFastScroller.setScrollerControl(this);
  }

  /**
   * Sets up the {@link FastScroller} with automatic fetch of accent color.
   * <p><b>IMPORTANT:</b> Call this method after the adapter is added to the RecyclerView.</p>
   * <b>NOTE:</b> If the device has at least Lollipop, the Accent color is fetched, otherwise
   * for previous version, the default value is used.
   *
   * @param accentColor the default value color if the accentColor cannot be fetched
   * @param stateChangeListener the listener to monitor when fast scrolling state changes
   * @since 5.0.0-b6
   */
  public void setFastScroller(int accentColor,
      OnScrollStateChange.OnScrollStateChangeListener stateChangeListener) {
    mFastScroller.setRecyclerView(mRecyclerView);
    mOnScrollStateChange.addListener(stateChangeListener);
    accentColor = Utils.fetchAccentColor(mFastScroller.getContext(), accentColor);
    mFastScroller.setViewsToUse(R.layout.library_fast_scroller_layout, R.id.fast_scroller_bubble,
        R.id.fast_scroller_handle, accentColor);
  }

  /**
   * Convenience method of {@link #setFastScroller(int,
   * OnScrollStateChange.OnScrollStateChangeListener)}.
   * <p><b>IMPORTANT:</b> Call this method after the adapter is added to the RecyclerView.</p>
   *
   * @see #setFastScroller(int, OnScrollStateChange.OnScrollStateChangeListener)
   * @since 5.0.0-b1
   */
  public void setFastScroller(@NonNull FastScroller fastScroller, int accentColor) {
    setFastScroller(accentColor, null);
  }

  /**
   * Displays or Hides the {@link FastScroller} if previously configured.
   *
   * @see #setFastScroller(FastScroller, int)
   * @since 5.0.0-b1
   */
  public void toggleFastScroller() {
    if (mFastScroller != null) {
      if (mFastScroller.getVisibility() != View.VISIBLE) {
        mFastScroller.setVisibility(View.VISIBLE);
      } else {
        mFastScroller.setVisibility(View.GONE);
      }
    }
  }

  /**
   * @return true if {@link FastScroller} is configured and shown, false otherwise
   * @since 5.0.0-b1
   */
  public boolean isFastScrollerEnabled() {
    return mFastScroller != null && mFastScroller.getVisibility() == View.VISIBLE;
  }

  /**
   * @return the current instance of the {@link FastScroller} object
   * @since 5.0.0-b1
   */
  public FastScroller getFastScroller() {
    return mFastScroller;
  }

  public void notifyScrollStateChange(boolean scrolling) {
    mOnScrollStateChange.onFastScrollerStateChange(scrolling);
  }
}
