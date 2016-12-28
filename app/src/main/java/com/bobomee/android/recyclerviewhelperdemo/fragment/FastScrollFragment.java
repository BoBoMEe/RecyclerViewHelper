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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bobomee.android.recyclerviewhelper.fastscroll.RecyclerFastScroller;
import com.bobomee.android.recyclerviewhelper.fastscroll.Utils;
import com.bobomee.android.recyclerviewhelper.fastscroll.interfaces.OnScrollStateChange;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.recycler.BaseRecyclerAdapter;
import com.bobomee.android.recyclerviewhelperdemo.recycler.RecyclerViewHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/12/27.下午11:21.
 *
 * @author bobomee.
 */

public class FastScrollFragment extends Fragment
    implements OnScrollStateChange.OnScrollStateChangeListener {

  private RecyclerView mRecyclerView;
  private FloatingActionButton mFloatingActionButton;
  Activity mActivity;
  private BaseRecyclerAdapter<String> mItemAdapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_fast_scroll, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);

    mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

    mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    mRecyclerView.setAdapter(mItemAdapter);

    RecyclerFastScroller fastScroller = (RecyclerFastScroller) view.findViewById(R.id.fast_scroller);
    fastScroller.setBubbleTextCreator(mItemAdapter);

    fastScroller.setRecyclerView(mRecyclerView);
    fastScroller.addOnScrollStateChangeListener(this);

    int color = Utils.fetchAccentColor(mActivity,MyUtils.getColorAccent(mActivity));
    fastScroller.setAccentColor(color);

  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = getActivity();

    prepareData();

    mItemAdapter = new BaseRecyclerAdapter<String>(mDatas, mActivity) {
      @Override public void bindData(RecyclerViewHolder holder, int position, String item) {
        holder.setText(R.id.tvItemName, item);
      }

      @Override public int getItemLayoutId(int viewType) {
        return R.layout.item_adapter;
      }

      @Override public String onCreateBubbleText(int pos) {
        return String.valueOf(pos);
      }
    };
  }

  private List<String> mDatas = new ArrayList<>();

  private void prepareData() {

    for (int i = 0; i < 50; ++i) {

      mDatas.add(i + "");
    }
  }

  @Override public void onFastScrollerStateChange(boolean scrolling) {
    if (scrolling) {
      hideFab();
    } else {
      showFab();
    }
  }

  private void hideFab() {
    ViewCompat.animate(mFloatingActionButton)
        .scaleX(0f)
        .scaleY(0f)
        .alpha(0f)
        .setDuration(100)
        .start();
  }

  private void showFab() {
    ViewCompat.animate(mFloatingActionButton)
        .scaleX(1f)
        .scaleY(1f)
        .alpha(1f)
        .setDuration(200)
        .setStartDelay(300L)
        .start();
  }
}
