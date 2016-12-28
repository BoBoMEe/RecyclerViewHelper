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

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import static com.bobomee.android.recyclerviewhelper.fastscroll.Utils.getValueInRange;

/**
 * Created on 2016/12/28.上午11:04.
 *
 * @author bobomee.
 */

public class RecyclerFastScroller extends FastScroller {
  public RecyclerFastScroller(Context context) {
    super(context);
  }

  public RecyclerFastScroller(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public RecyclerFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private static final int TRACK_SNAP_RANGE = 5;

  private final RecyclerView.OnScrollListener onScrollListener =
      new RecyclerView.OnScrollListener() {
        @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
          if (bubble == null || handle.isSelected()) return;
          int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
          int verticalScrollRange = recyclerView.computeVerticalScrollRange();
          float proportion = (float) verticalScrollOffset / ((float) verticalScrollRange - height);
          setBubbleAndHandlePosition(height * proportion);
        }
      };

  public void setRecyclerView(final RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
    this.recyclerView.addOnScrollListener(onScrollListener);
    this.recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
          int oldTop, int oldRight, int oldBottom) {
        layoutManager = recyclerView.getLayoutManager();
      }
    });

    this.recyclerView.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (bubble == null || handle.isSelected()) return true;
            int verticalScrollOffset = recyclerView.computeVerticalScrollOffset();
            int verticalScrollRange = computeVerticalScrollRange();
            float proportion =
                (float) verticalScrollOffset / ((float) verticalScrollRange - height);
            setBubbleAndHandlePosition(height * proportion);
            return true;
          }
        });
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (recyclerView != null) recyclerView.removeOnScrollListener(onScrollListener);
  }

  public void setRecyclerViewPosition(float y) {
    if (recyclerView != null) {
      int itemCount = recyclerView.getAdapter().getItemCount();
      //Calculate proportion
      float proportion;
      if (handle.getY() == 0) {
        proportion = 0f;
      } else if (handle.getY() + handle.getHeight() >= height - TRACK_SNAP_RANGE) {
        proportion = 1f;
      } else {
        proportion = y / (float) height;
      }
      int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
      //Scroll To Position based on LayoutManager
      if (layoutManager instanceof StaggeredGridLayoutManager) {
        ((StaggeredGridLayoutManager) layoutManager).scrollToPositionWithOffset(targetPos, 0);
      } else {
        ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(targetPos, 0);
      }
      setBubblePos(targetPos);
    }
  }
}
