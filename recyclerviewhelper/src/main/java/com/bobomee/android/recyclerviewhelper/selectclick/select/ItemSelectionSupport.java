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

package com.bobomee.android.recyclerviewhelper.selectclick.select;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Checkable;
import com.bobomee.android.recyclerviewhelper.selectclick.ItemTouchListener;
import java.util.List;

import static android.os.Build.VERSION_CODES.HONEYCOMB;

public class ItemSelectionSupport {
  private static final int INVALID_POSITION = -1;
  //private ItemSelectionSupportManager.OnItemSelectListener onItemSelectListener;
  //private ItemSelectionSupportManager.OnItemSelectChangeListener onItemSelectChangeListener;

  public enum ChoiceMode {
    NONE,
    SINGLE,
    MULTIPLE
  }

  private final RecyclerView mRecyclerView;
  private final TouchListener mTouchListener;

  private ChoiceMode mChoiceMode = ChoiceMode.NONE;
  private StateManager.CheckedStates mCheckedStates;
  private StateManager.CheckedIdStates mCheckedIdStates;
  private int mCheckedCount;

  private static final String STATE_KEY_CHOICE_MODE = "choiceMode";
  private static final String STATE_KEY_CHECKED_STATES = "checkedStates";
  private static final String STATE_KEY_CHECKED_ID_STATES = "checkedIdStates";
  private static final String STATE_KEY_CHECKED_COUNT = "checkedCount";

  private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;

  private ItemSelectionSupport(RecyclerView recyclerView) {
    mRecyclerView = recyclerView;

    mTouchListener = new TouchListener(recyclerView);
  }

  public static ItemSelectionSupport from(RecyclerView _recyclerView) {
    if (null == _recyclerView) return null;
    ItemSelectionSupport itemSelectionSupport = new ItemSelectionSupport(_recyclerView);
    return itemSelectionSupport;
  }

  public ItemSelectionSupport remove() {
    mRecyclerView.removeOnItemTouchListener(mTouchListener);
    return this;
  }

  public ItemSelectionSupport add() {
    mRecyclerView.addOnItemTouchListener(mTouchListener);
    return this;
  }

  private void updateOnScreenCheckedViews() {
    ItemSelectionSupportManager.notifyChage(mCheckedStates);
    final int count = mRecyclerView.getChildCount();
    for (int i = 0; i < count; i++) {
      final View child = mRecyclerView.getChildAt(i);
      final int position = mRecyclerView.getChildPosition(child);
      setViewChecked(child, mCheckedStates.get(position));
    }
  }

  /**
   * Returns the number of items currently selected. This will only be valid
   * if the choice mode is not {@link ChoiceMode#NONE} (default).
   *
   * <p>To determine the specific items that are currently selected, use one of
   * the <code>getChecked*</code> methods.
   *
   * @return The number of items currently selected
   * @see #getCheckedItemPosition()
   * @see #getCheckedItemPositions()
   * @see #getCheckedItemIds()
   */
  public int getCheckedItemCount() {
    return mCheckedCount;
  }

  /**
   * Returns the checked state of the specified position. The result is only
   * valid if the choice mode has been set to {@link ChoiceMode#SINGLE}
   * or {@link ChoiceMode#MULTIPLE}.
   *
   * @param position The item whose checked state to return
   * @return The item's checked state or <code>false</code> if choice mode
   * is invalid
   * @see #setChoiceMode(ChoiceMode)
   */
  public boolean isItemChecked(int position) {
    if (mChoiceMode != ChoiceMode.NONE && mCheckedStates != null) {
      return mCheckedStates.get(position);
    }

    return false;
  }

  /**
   * Returns the currently checked item. The result is only valid if the choice
   * mode has been set to {@link ChoiceMode#SINGLE}.
   *
   * @return The position of the currently checked item or
   * {@link #INVALID_POSITION} if nothing is selected
   * @see #setChoiceMode(ChoiceMode)
   */
  public int getCheckedItemPosition() {
    if (mChoiceMode == ChoiceMode.SINGLE && mCheckedStates != null && mCheckedStates.size() == 1) {
      return mCheckedStates.keyAt(0);
    }

    return INVALID_POSITION;
  }

  /**
   * Returns the set of checked items in the list. The result is only valid if
   * the choice mode has not been set to {@link ChoiceMode#NONE}.
   *
   * @return A SparseBooleanArray which will return true for each call to
   * get(int position) where position is a position in the list,
   * or <code>null</code> if the choice mode is set to
   * {@link ChoiceMode#NONE}.
   */
  public SparseBooleanArray getCheckedItemPositions() {
    if (mChoiceMode != ChoiceMode.NONE) {
      return mCheckedStates;
    }

    return null;
  }

  /**
   * Returns the set of checked items ids. The result is only valid if the
   * choice mode has not been set to {@link ChoiceMode#NONE} and the adapter
   * has stable IDs.
   *
   * @return A new array which contains the id of each checked item in the
   * list.
   * @see android.support.v7.widget.RecyclerView.Adapter#hasStableIds()
   */
  public long[] getCheckedItemIds() {
    if (mChoiceMode == ChoiceMode.NONE
        || mCheckedIdStates == null
        || mRecyclerView.getAdapter() == null) {
      return new long[0];
    }

    final int count = mCheckedIdStates.size();
    final long[] ids = new long[count];

    for (int i = 0; i < count; i++) {
      ids[i] = mCheckedIdStates.keyAt(i);
    }

    return ids;
  }

  /**
   * Sets the checked state of the specified position. The is only valid if
   * the choice mode has been set to {@link ChoiceMode#SINGLE} or
   * {@link ChoiceMode#MULTIPLE}.
   *
   * @param position The item whose checked state is to be checked
   * @param checked The new checked state for the item
   */
  public void setItemChecked(int position, boolean checked) {
    if (mChoiceMode == ChoiceMode.NONE) {
      return;
    }

    ensureStates();

    final Adapter adapter = mRecyclerView.getAdapter();

    if (mChoiceMode == ChoiceMode.MULTIPLE) {
      boolean oldValue = mCheckedStates.get(position);
      mCheckedStates.put(position, checked);

      if (mCheckedIdStates != null && adapter.hasStableIds()) {
        if (checked) {
          mCheckedIdStates.put(adapter.getItemId(position), position);
        } else {
          mCheckedIdStates.delete(adapter.getItemId(position));
        }
      }

      if (oldValue != checked) {
        if (checked) {
          mCheckedCount++;
        } else {
          mCheckedCount--;
        }
      }
    } else {
      boolean updateIds = mCheckedIdStates != null && adapter.hasStableIds();

      // Clear all values if we're checking something, or unchecking the currently
      // selected item
      if (checked || isItemChecked(position)) {
        mCheckedStates.clear();

        if (updateIds) {
          mCheckedIdStates.clear();
        }
      }

      // This may end up selecting the checked we just cleared but this way
      // we ensure length of mCheckStates is 1, a fact getCheckedItemPosition relies on
      if (checked) {
        mCheckedStates.put(position, true);

        if (updateIds) {
          mCheckedIdStates.put(adapter.getItemId(position), position);
        }

        mCheckedCount = 1;
      } else if (mCheckedStates.size() == 0 || !mCheckedStates.valueAt(0)) {
        mCheckedCount = 0;
      }
    }

    notifySelected(position, checked);

    updateOnScreenCheckedViews();
  }

  private void notifySelected(int position, boolean checked) {
    List<ItemSelectionSupportManager.OnItemSelectListener> onItemSelectListeners =
        ItemSelectionSupportManager.fromSelectListener();

    if (null != onItemSelectListeners && !onItemSelectListeners.isEmpty()) {
      for (ItemSelectionSupportManager.OnItemSelectListener onItemSelectListener : onItemSelectListeners) {
        onItemSelectListener.onItemSelect(mRecyclerView, mRecyclerView.getChildAt(position),
            position, checked);
      }
    }
  }

  @TargetApi(HONEYCOMB) public void setViewChecked(View view, boolean checked) {
    if (view instanceof Checkable) {
      ((Checkable) view).setChecked(checked);
    } else if (Build.VERSION.SDK_INT >= HONEYCOMB) {
      view.setActivated(checked);
    }
  }

  /**
   * Clears any choices previously set.
   */
  public void clearChoices() {
    if (mCheckedStates != null) {
      mCheckedStates.clear();
    }

    if (mCheckedIdStates != null) {
      mCheckedIdStates.clear();
    }

    mCheckedCount = 0;
    updateOnScreenCheckedViews();
  }

  /**
   * Returns the current choice mode.
   *
   * @see #setChoiceMode(ChoiceMode)
   */
  public ChoiceMode getChoiceMode() {
    return mChoiceMode;
  }

  /**
   * Defines the choice behavior for the List. By default, Lists do not have any choice behavior
   * ({@link ChoiceMode#NONE}). By setting the choiceMode to {@link ChoiceMode#SINGLE}, the
   * List allows up to one item to  be in a chosen state. By setting the choiceMode to
   * {@link ChoiceMode#MULTIPLE}, the list allows any number of items to be chosen.
   *
   * @param choiceMode One of {@link ChoiceMode#NONE}, {@link ChoiceMode#SINGLE}, or
   * {@link ChoiceMode#MULTIPLE}
   */
  public ItemSelectionSupport setChoiceMode(ChoiceMode choiceMode) {
    if (mChoiceMode != choiceMode) {
      mChoiceMode = choiceMode;

      ensureStates();
    }

    return this;
  }

  private void ensureStates() {
    if (mChoiceMode != ChoiceMode.NONE) {
      if (mCheckedStates == null) {
        mCheckedStates = new StateManager.CheckedStates();
      }

      final Adapter adapter = mRecyclerView.getAdapter();
      if (mCheckedIdStates == null && adapter != null && adapter.hasStableIds()) {
        mCheckedIdStates = new StateManager.CheckedIdStates();
      }
    }
  }

  public Bundle onSaveInstanceState() {
    final Bundle state = new Bundle();

    state.putInt(STATE_KEY_CHOICE_MODE, mChoiceMode.ordinal());
    state.putParcelable(STATE_KEY_CHECKED_STATES, mCheckedStates);
    state.putParcelable(STATE_KEY_CHECKED_ID_STATES, mCheckedIdStates);
    state.putInt(STATE_KEY_CHECKED_COUNT, mCheckedCount);

    return state;
  }

  public void onRestoreInstanceState(Bundle state) {
    mChoiceMode = ChoiceMode.values()[state.getInt(STATE_KEY_CHOICE_MODE)];
    mCheckedStates = state.getParcelable(STATE_KEY_CHECKED_STATES);
    mCheckedIdStates = state.getParcelable(STATE_KEY_CHECKED_ID_STATES);
    mCheckedCount = state.getInt(STATE_KEY_CHECKED_COUNT);

    // TODO confirm ids here
  }

  private class TouchListener extends ItemTouchListener {
    TouchListener(RecyclerView recyclerView) {
      super(recyclerView);
    }

    @Override
    public boolean performItemClick(RecyclerView parent, View view, int position, long id) {
      final Adapter adapter = mRecyclerView.getAdapter();
      boolean checkedStateChanged = false;

      ensureStates();

      if (mChoiceMode == ChoiceMode.MULTIPLE) {
        boolean checked = !mCheckedStates.get(position, false);

        mCheckedStates.put(position, checked);

        if (mCheckedIdStates != null && adapter.hasStableIds()) {
          if (checked) {
            mCheckedIdStates.put(adapter.getItemId(position), position);
          } else {
            mCheckedIdStates.delete(adapter.getItemId(position));
          }
        }

        if (checked) {
          mCheckedCount++;
        } else {
          mCheckedCount--;
        }

        notifySelected(position, checked);

        checkedStateChanged = true;
      } else if (mChoiceMode == ChoiceMode.SINGLE) {
        boolean checked = !mCheckedStates.get(position, false);

        if (checked) {
          mCheckedStates.clear();
          mCheckedStates.put(position, true);

          if (mCheckedIdStates != null && adapter.hasStableIds()) {
            mCheckedIdStates.clear();
            mCheckedIdStates.put(adapter.getItemId(position), position);
          }

          mCheckedCount = 1;
        } else if (mCheckedStates.size() == 0 || !mCheckedStates.valueAt(0)) {
          mCheckedCount = 0;
        }

        notifySelected(position, checked);

        checkedStateChanged = true;
      }

      if (checkedStateChanged) {
        updateOnScreenCheckedViews();
      }

      return false;
    }

    @Override
    public boolean performItemLongClick(RecyclerView parent, View view, int position, long id) {
      return true;
    }
  }

  public void setOnItemSelectListener(
      ItemSelectionSupportManager.OnItemSelectListener _onItemSelectListener) {
    ItemSelectionSupportManager.addOnItemSelectListener(_onItemSelectListener);
  }

  public void setonItemSelectChangeListener(
      ItemSelectionSupportManager.OnItemSelectChangeListener _onItemSelectChangeListener) {
    ItemSelectionSupportManager.addOnItemSelectChangeListener(_onItemSelectChangeListener);
  }
}
