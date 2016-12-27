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

package com.bobomee.android.recyclerviewhelperdemo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bobomee.android.common.widget.BackHandledFragment;
import com.bobomee.android.recyclerviewhelper.paginate.WrapperAdapter;
import com.bobomee.android.recyclerviewhelper.selectclick.click.ItemClick;
import com.bobomee.android.recyclerviewhelper.selectclick.click.ItemClickSupport;
import com.bobomee.android.recyclerviewhelper.selectclick.click.ItemLongClick;
import com.bobomee.android.recyclerviewhelper.selectclick.select.ItemSelect;
import com.bobomee.android.recyclerviewhelper.selectclick.select.ItemSelectChange;
import com.bobomee.android.recyclerviewhelper.selectclick.select.ItemSelectionSupport;
import com.bobomee.android.recyclerviewhelper.selectclick.select.StateManager;
import com.bobomee.android.recyclerviewhelperdemo.recycler.BaseRecyclerAdapter;
import com.bobomee.android.recyclerviewhelperdemo.recycler.DataProvider;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.recycler.RecyclerViewHolder;
import java.util.ArrayList;
import java.util.List;

import static com.bobomee.android.recyclerviewhelperdemo.R.id.view;

public class ItemSelectFragment extends BackHandledFragment {
  protected TextView mTvTouch;
  private static final String TAG = "ItemSelectFragment";

  private Toast mToast;
  private ItemClickSupport mItemClickSupport;
  private ItemSelectionSupport mItemSelectionSupport;
  protected List<String> datas = new ArrayList<>();

  protected RecyclerView recyclerView;
  protected BaseRecyclerAdapter mBaseRecyclerAdapter;
  protected SwipeRefreshLayout mSwipeRefreshLayout;

  private Activity mActivity;
  private int mSelect = -1;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();

    setHasOptionsMenu(true);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.activity_main, container, false);

    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    datas.addAll(DataProvider.provide(0));


    initView(view);
    initAdapter();


  }

  private void initAdapter() {
    mBaseRecyclerAdapter = new BaseRecyclerAdapter<String>(datas, mActivity) {
      @Override public String onCreateBubbleText(int pos) {
        return null;
      }

      @Override public void bindData(RecyclerViewHolder holder, int position, String item) {
        holder.setText(R.id.tvItemName, item);
        View view = holder.itemView;
        if (mSelect == position) {
          mItemSelectionSupport.setViewChecked(view, true);
        } else {
          mItemSelectionSupport.setViewChecked(view, false);
        }
      }

      @Override public int getItemLayoutId(int viewType) {
        return R.layout.item_adapter;
      }
    };

    recyclerView.setAdapter(new WrapperAdapter(mBaseRecyclerAdapter));
  }

  private void initView(View _view) {
    mToast = Toast.makeText(mActivity, "", Toast.LENGTH_SHORT);
    mToast.setGravity(Gravity.CENTER, 0, 0);

    recyclerView = (RecyclerView) _view.findViewById(view);
    mSwipeRefreshLayout = (SwipeRefreshLayout) _view.findViewById(R.id.swipe);

    /**
     * set viewpool,cacheSize
     */
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
    linearLayoutManager.setRecycleChildrenOnDetach(true);

    recyclerView.setLayoutManager(linearLayoutManager);
    RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
    pool.setMaxRecycledViews(0, 10);
    recyclerView.setRecycledViewPool(pool);
    recyclerView.setItemViewCacheSize(20);

    mItemClickSupport = ItemClickSupport.from(recyclerView).add();

    mItemClickSupport.addOnItemClickListener(new ItemClick.OnItemClickListener() {
      @Override public void onItemClick(RecyclerView parent, View child, int position, long id) {
        mToast.setText("Item clicked: " + position);
        mToast.show();
        mBaseRecyclerAdapter.delete(position);
      }
    });

    mItemClickSupport.addOnItemLongClickListener(new ItemLongClick.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
        mSelect = position;
        mToast.setText("Item long pressed: " + position);
        mToast.show();
        mItemClickSupport.remove();
        mItemSelectionSupport.add();
        mItemSelectionSupport.setItemChecked(position, true);
        return true;
      }
    });

    ///////
    mItemSelectionSupport = ItemSelectionSupport.from(recyclerView)
        .setChoiceMode(ItemSelectionSupport.ChoiceMode.MULTIPLE);

    mItemSelectionSupport.addOnItemSelectListener(new ItemSelect.OnItemSelectListener() {
      @Override
      public void onItemSelect(RecyclerView parent, View view, int position, boolean checked) {
        mToast.setText("Item --> " + position + ", selected --> " + checked);
        mToast.show();
      }
    });

    mItemSelectionSupport.addonItemSelectChangeListener(
        new ItemSelectChange.OnItemSelectChangeListener() {
          @Override public void onItenSelectChange(StateManager.CheckedStates mCheckedStates) {

            if (mItemSelectionSupport.getCheckedItemCount() == 0) {
              mItemSelectionSupport.remove();
              mItemClickSupport.add();
            }
          }
        });
  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    menu.clear();

    inflater.inflate(R.menu.add, menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add:{
        mBaseRecyclerAdapter.add(0,"new item");
        recyclerView.scrollToPosition(0);
      }
        break;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean interceptBackPressed() {
    if (mItemSelectionSupport.getCheckedItemCount() > 0) {
      mItemSelectionSupport.clearChoices();
      return true;
    }

    return super.interceptBackPressed();
  }
}
