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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bobomee.android.common.widget.BackHandledFragment;
import com.bobomee.android.recyclerviewhelper.itemclick.ItemClick;
import com.bobomee.android.recyclerviewhelper.itemclick.ItemClickSupport;
import com.bobomee.android.recyclerviewhelper.itemclick.ItemLongClick;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.recycler.BaseRecyclerAdapter;
import com.bobomee.android.recyclerviewhelperdemo.recycler.DataProvider;
import com.bobomee.android.recyclerviewhelperdemo.recycler.RecyclerViewHolder;
import java.util.ArrayList;
import java.util.List;

public class ItemClickFragment extends BackHandledFragment {

  public static ItemClickFragment newInstance() {
    Bundle args = new Bundle();
    ItemClickFragment fragment = new ItemClickFragment();
    fragment.setArguments(args);
    return fragment;
  }

  private Toast mToast;
  protected List<String> datas = new ArrayList<>();

  protected RecyclerView recyclerView;
  protected BaseRecyclerAdapter<String> mBaseRecyclerAdapter;

  private Activity mActivity;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();

    setHasOptionsMenu(true);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    return inflater.inflate(R.layout.click_fragment, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    datas.addAll(DataProvider.provide(20));

    initView(view);
    initAdapter();
  }

  private void initAdapter() {
    mBaseRecyclerAdapter = new BaseRecyclerAdapter<String>(datas, mActivity) {
      @Override public void bindData(RecyclerViewHolder holder, int position, String item) {
        holder.setText(R.id.tvItemName, item);
      }

      @Override public int getItemLayoutId(int viewType) {
        return R.layout.item_adapter;
      }
    };

    recyclerView.setAdapter(mBaseRecyclerAdapter);
  }

  private void initView(View _view) {
    mToast = Toast.makeText(mActivity, "", Toast.LENGTH_SHORT);
    mToast.setGravity(Gravity.CENTER, 0, 0);

    recyclerView = (RecyclerView) _view.findViewById(R.id.recycler);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
    linearLayoutManager.setRecycleChildrenOnDetach(true);

    recyclerView.setLayoutManager(linearLayoutManager);

    /**
     * set viewpool,cacheSize
     */
    RecyclerView.RecycledViewPool pool = new RecyclerView.RecycledViewPool();
    pool.setMaxRecycledViews(0, 10);
    recyclerView.setRecycledViewPool(pool);
    recyclerView.setItemViewCacheSize(20);

    /**
     * set onitemclick
     */
    ItemClickSupport itemClickSupport = ItemClickSupport.from(recyclerView).add();

    itemClickSupport.addOnItemClickListener(new ItemClick.OnItemClickListener() {
      @Override public void onItemClick(RecyclerView parent, View child, int position, long id) {
        mToast.setText("Item clicked: " + position);
        mToast.show();
        mBaseRecyclerAdapter.delete(position);
      }
    });

    itemClickSupport.addOnItemLongClickListener(new ItemLongClick.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(RecyclerView parent, View child, int position, long id) {
        mToast.setText("Item long pressed: " + position);
        mToast.show();
        return false;
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
      case R.id.add: {
        mBaseRecyclerAdapter.add(0, "new item");
        recyclerView.scrollToPosition(0);
      }
      break;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean interceptBackPressed() {
    return super.interceptBackPressed();
  }
}
