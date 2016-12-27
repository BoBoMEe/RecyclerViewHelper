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

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bobomee.android.recyclerviewhelper.R;
import com.bobomee.android.recyclerviewhelper.fastscroll.interfaces.BubbleTextCreator;
import com.bobomee.android.recyclerviewhelper.fastscroll.interfaces.ScrollerControl;
import com.bobomee.android.recyclerviewhelper.fastscroll.util.AnimatorUtil;
import com.bobomee.android.recyclerviewhelper.fastscroll.util.GradientDrawableHelper;
import com.bobomee.android.recyclerviewhelper.fastscroll.util.StateListDrawableHelper;

import static com.bobomee.android.recyclerviewhelper.fastscroll.Utils.getValueInRange;
import static com.bobomee.android.recyclerviewhelper.fastscroll.Utils.setTextBackground;

/**
 * Class taken from GitHub, customized and optimized for FlexibleAdapter project.
 *
 * @see <a href="https://github.com/AndroidDeveloperLB/LollipopContactsRecyclerViewFastScroller">
 * github.com/AndroidDeveloperLB/LollipopContactsRecyclerViewFastScroller</a>
 * @since Up to the date 23/01/2016
 * <br/>23/01/2016 Added onFastScrollerStateChange in the listener
 */
public class FastScroller extends FrameLayout {

  private static final int TRACK_SNAP_RANGE = 5;

  private TextView bubble;
  private ImageView handle;
  private int height;
  private boolean isInitialized = false;
  private ObjectAnimator currentAnimator;
  private RecyclerView recyclerView;
  private RecyclerView.LayoutManager layoutManager;
  private BubbleTextCreator bubbleTextCreator;
  private ScrollerControl mScrollerControl;

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

  public FastScroller(Context context) {
    super(context);
    init();
  }

  public FastScroller(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  protected void init() {
    if (isInitialized) return;
    isInitialized = true;
    setClipChildren(false);
  }

  public void setRecyclerView(final RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
    this.recyclerView.addOnScrollListener(onScrollListener);
    this.recyclerView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
          int oldTop, int oldRight, int oldBottom) {
        layoutManager = FastScroller.this.recyclerView.getLayoutManager();
      }
    });

    this.recyclerView.getViewTreeObserver()
        .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          @Override public boolean onPreDraw() {
            FastScroller.this.recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
            if (bubble == null || handle.isSelected()) return true;
            int verticalScrollOffset = FastScroller.this.recyclerView.computeVerticalScrollOffset();
            int verticalScrollRange = FastScroller.this.computeVerticalScrollRange();
            float proportion =
                (float) verticalScrollOffset / ((float) verticalScrollRange - height);
            setBubbleAndHandlePosition(height * proportion);
            return true;
          }
        });
  }

  public void setBubbleTextCreator(BubbleTextCreator _bubbleTextCreator) {
    bubbleTextCreator = _bubbleTextCreator;
  }

  /**
   * Layout customization.<br/>
   * Color for Selected State is the color defined inside the Drawables.
   *
   * @param layoutResId Main layout of Fast Scroller
   * @param bubbleResId Drawable resource for Bubble containing the Text
   * @param handleResId Drawable resource for the Handle
   */
  public void setViewsToUse(@LayoutRes int layoutResId, @IdRes int bubbleResId,
      @IdRes int handleResId) {
    if (bubble != null) return;//Already inflated
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(layoutResId, this, true);
    bubble = (TextView) findViewById(bubbleResId);
    if (bubble != null) bubble.setVisibility(INVISIBLE);
    handle = (ImageView) findViewById(handleResId);
  }

  /**
   * Layout customization<br/>
   * Color for Selected State is also customized by the user.
   *
   * @param layoutResId Main layout of Fast Scroller
   * @param bubbleResId Drawable resource for Bubble containing the Text
   * @param handleResId Drawable resource for the Handle
   * @param accentColor Color for Selected state during touch and scrolling (usually accent color)
   */
  public void setViewsToUse(@LayoutRes int layoutResId, @IdRes int bubbleResId,
      @IdRes int handleResId, int accentColor) {
    setViewsToUse(layoutResId, bubbleResId, handleResId);
    setBubbleAndHandleColor(accentColor);
  }

  private void setBubbleAndHandleColor(int accentColor) {
    //BubbleDrawable accentColor
    GradientDrawable bubbleDrawable =
        GradientDrawableHelper.getGradientDrawable(getContext(), R.drawable.fast_scroller_bubble);
    assert bubbleDrawable != null;
    bubbleDrawable.setColor(accentColor);
    setTextBackground(bubble, bubbleDrawable);

    //HandleDrawable accentColor
    StateListDrawable stateListDrawable =
        StateListDrawableHelper.getStateListDrawable(getContext(), R.drawable.fast_scroller_handle);
    GradientDrawable handleDrawable =
        (GradientDrawable) StateListDrawableHelper.getStateDrawable(stateListDrawable, 0);
    assert handleDrawable != null;
    handleDrawable.setColor(accentColor);
    handle.setImageDrawable(stateListDrawable);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    height = h;
  }

  @Override public boolean onTouchEvent(@NonNull MotionEvent event) {
    int action = event.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        if (event.getX() < handle.getX() - ViewCompat.getPaddingStart(handle)) return false;
        if (currentAnimator != null) currentAnimator.cancel();
        handle.setSelected(true);
        mScrollerControl.notifyScrollStateChange(true);
        showBubble();
      case MotionEvent.ACTION_MOVE:
        float y = event.getY();
        setBubbleAndHandlePosition(y);
        setRecyclerViewPosition(y);
        return true;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        handle.setSelected(false);
        mScrollerControl.notifyScrollStateChange(false);
        hideBubble();
        return true;
    }
    return super.onTouchEvent(event);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (recyclerView != null) recyclerView.removeOnScrollListener(onScrollListener);
  }

  private void setRecyclerViewPosition(float y) {
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
      //Update bubbleText
      if (bubble != null) {
        String bubbleText = bubbleTextCreator.onCreateBubbleText(targetPos);
        if (bubbleText != null) {
          bubble.setVisibility(View.VISIBLE);
          bubble.setText(bubbleText);
        } else {
          bubble.setVisibility(View.GONE);
        }
      }
    }
  }

  private void setBubbleAndHandlePosition(float y) {
    int handleHeight = handle.getHeight();
    handle.setY(getValueInRange(0, height - handleHeight, (int) (y - handleHeight / 2)));
    if (bubble != null) {
      int bubbleHeight = bubble.getHeight();
      bubble.setY(
          getValueInRange(0, height - bubbleHeight - handleHeight / 2, (int) (y - bubbleHeight)));
    }
  }

  private void showBubble() {
    if (currentAnimator != null) currentAnimator.cancel();
    currentAnimator = AnimatorUtil.to_alpha(bubble);
  }

  private void hideBubble() {
    if (null != currentAnimator) currentAnimator.cancel();
    currentAnimator = AnimatorUtil.alpha_to(bubble);
  }

  public void setScrollerControl(ScrollerControl _scrollerControl) {
    mScrollerControl = _scrollerControl;
  }
}