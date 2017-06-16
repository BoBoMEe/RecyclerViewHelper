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

  /**
   * the no data item create
   */
  public class DefaultLoadingNoDataItemCreator implements LoadingListItemCreator{

    private final Paginate.Callbacks callBacks;

    public DefaultLoadingNoDataItemCreator(Paginate.Callbacks callBacks) {
      this.callBacks = callBacks;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_no_data_tip, parent, false);
      return new DefaultLoadingNoDataItemViewHolder(view,callBacks) {
      };
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class DefaultLoadingNoDataItemViewHolder extends RecyclerView.ViewHolder{

      private final Paginate.Callbacks callbacks;

      public DefaultLoadingNoDataItemViewHolder(View itemView, final Paginate.Callbacks callbacks) {
        super(itemView);
        this.callbacks = callbacks;

        itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (!callbacks.isLoading() && !callbacks.hasLoadedAllItems()) {
              callbacks.onLoadMore();
            }
          }
        });
      }
    }
  }

  /**
   * the progress item create
   */
  public class DefalutLoadingListItemCreator implements LoadingListItemCreator {

    private final Paginate.Callbacks callBacks;

    public DefalutLoadingListItemCreator(Paginate.Callbacks callbacks) {
      this.callBacks = callbacks;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view =
          LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_row, parent, false);
      return new DefaultLoadingListItemViewHolder(view,callBacks){
      };
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class DefaultLoadingListItemViewHolder extends RecyclerView.ViewHolder{

      private final Paginate.Callbacks callbacks;

      public DefaultLoadingListItemViewHolder(View itemView, final Paginate.Callbacks callbacks) {
        super(itemView);
        this.callbacks = callbacks;

        itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            if (!callbacks.isLoading() && !callbacks.hasLoadedAllItems()) {
                callbacks.onLoadMore();
            }
          }
        });
      }
    }
  }

}