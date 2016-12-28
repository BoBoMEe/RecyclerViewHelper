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

package com.bobomee.android.recyclerviewhelper.fastscroll;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

public final class Utils {

  public static int getValueInRange(int min, int max, int value) {
    int minimum = Math.max(min, value);
    return Math.min(minimum, max);
  }

  public static void setTextBackground(TextView _textView,Drawable _drawable){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      _textView.setBackground(_drawable);
    } else {
      //noinspection deprecation
      _textView.setBackgroundDrawable(_drawable);
    }

  }

  /**
   * Finds the layout orientation of the RecyclerView.
   *
   * @param recyclerView the RV instance
   * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
   * @deprecated Use {@link #getOrientation(RecyclerView.LayoutManager)} instead
   */
  @Deprecated
  public static int getOrientation(RecyclerView recyclerView) {
    return getOrientation(recyclerView.getLayoutManager());
  }

  /**
   * Finds the layout orientation of the RecyclerView, no matter which LayoutManager is in use.
   *
   * @param layoutManager the LayoutManager instance in use by the RV
   * @return one of {@link OrientationHelper#HORIZONTAL}, {@link OrientationHelper#VERTICAL}
   */
  public static int getOrientation(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) layoutManager).getOrientation();
    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
    }
    return OrientationHelper.HORIZONTAL;
  }

  /**
   * Helper method to find the adapter position of the First completely visible view [for each
   * span], no matter which Layout is.
   *
   * @param layoutManager the layout manager in use
   * @return the adapter position of the first fully visible item or {@code RecyclerView.NO_POSITION}
   * if there aren't any visible items.
   * @see #findLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager)
   * @since 5.0.0-b8
   */
  public static int findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).findFirstCompletelyVisibleItemPositions(null)[0];
    } else {
      return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
    }
  }

  /**
   * Helper method to find the adapter position of the Last completely visible view [for each
   * span], no matter which Layout is.
   *
   * @param layoutManager the layout manager in use
   * @return the adapter position of the last fully visible item or {@code RecyclerView.NO_POSITION}
   * if there aren't any visible items.
   * @see #findFirstCompletelyVisibleItemPosition(RecyclerView.LayoutManager)
   * @since 5.0.0-b8
   */
  public static int findLastCompletelyVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof StaggeredGridLayoutManager) {
      return ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null)[0];
    } else {
      return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
    }
  }
}

