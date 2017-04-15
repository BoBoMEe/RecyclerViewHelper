package com.bobomee.android.recyclerviewhelper.itemclick;

import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created on 2016/11/17.上午11:07.
 *
 * @author bobomee.
 * @description
 */

public abstract class ItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {

  private final GestureDetectorCompat mGestureDetectorCompat;

  public ItemTouchListener(final RecyclerView _recyclerView) {
    mGestureDetectorCompat = new GestureDetectorCompat(_recyclerView.getContext(),
        new ItemGestureListener(_recyclerView));
  }

  private boolean isAttachedToWindow(RecyclerView hostView) {
    if (Build.VERSION.SDK_INT >= 19) {
      return hostView.isAttachedToWindow();
    } else {
      return (hostView.getHandler() != null);
    }
  }

  private boolean hasAdapter(RecyclerView hostView) {
    return (hostView.getAdapter() != null);
  }

  @Override public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
    if (!isAttachedToWindow(recyclerView) || !hasAdapter(recyclerView)) {
      return false;
    }

    mGestureDetectorCompat.onTouchEvent(event);
    return false;
  }

  public abstract boolean performItemClick(RecyclerView parent, View view, int position, long id);

  public abstract boolean performItemLongClick(RecyclerView parent, View view, int position, long id);

  private class ItemGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final RecyclerView mHostView;
    private View mTargetChild;

    public ItemGestureListener(RecyclerView hostView) {
      mHostView = hostView;
    }

    @Override public boolean onDown(MotionEvent event) {
      final int x = (int) event.getX();
      final int y = (int) event.getY();

      mTargetChild = mHostView.findChildViewUnder(x, y);
      return (mTargetChild != null);
    }

    @Override public void onShowPress(MotionEvent event) {
      if (mTargetChild != null) {
        mTargetChild.setPressed(true);
      }
    }

    @Override public boolean onSingleTapUp(MotionEvent event) {
      boolean handled = false;

      if (mTargetChild != null) {
        mTargetChild.setPressed(false);

        final int position = mHostView.getChildPosition(mTargetChild);
        final long id = mHostView.getAdapter().getItemId(position);
        handled = performItemClick(mHostView, mTargetChild, position, id);

        mTargetChild = null;
      }

      return handled;
    }

    @Override public boolean onScroll(MotionEvent event, MotionEvent event2, float v, float v2) {
      if (mTargetChild != null) {
        mTargetChild.setPressed(false);
        mTargetChild = null;

        return true;
      }

      return false;
    }

    @Override public void onLongPress(MotionEvent event) {
      if (mTargetChild == null) {
        return;
      }

      final int position = mHostView.getChildPosition(mTargetChild);
      final long id = mHostView.getAdapter().getItemId(position);
      final boolean handled = performItemLongClick(mHostView, mTargetChild, position, id);

      if (handled) {
        mTargetChild.setPressed(false);
        mTargetChild = null;
      }
    }
  }
}
