/*
 * (C) Copyright 2013, 2017 Pharmacodia Technology Co.,Ltd.
 */

package com.bobomee.android.recyclerviewhelper.paginate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bobomee.android.recyclerviewhelper.R;

/** RecyclerView creator that will be called to create and bind loading list item */
public interface LoadingListItemCreator {

  /**
   * Create new loading list item {@link android.support.v7.widget.RecyclerView.ViewHolder}.
   *
   * @param parent parent ViewGroup.
   * @param viewType type of the loading list item.
   * @return ViewHolder that will be used as loading list item.
   */
  RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

  /**
   * Bind the loading list item.
   *
   * @param holder loading list item ViewHolder.
   * @param position loading list item position.
   */
  void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

  LoadingListItemCreator DEFAULT = new LoadingListItemCreator() {
    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, parent, false);
      return new RecyclerView.ViewHolder(view) {
      };
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      // No binding for default loading row
    }
  };

  LoadingListItemCreator DEFAULT_NO_DATA_ITEM_CREATOR = new LoadingListItemCreator() {
    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_no_data_tip, parent, false);
      return new RecyclerView.ViewHolder(view) {
      };
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
  };
}