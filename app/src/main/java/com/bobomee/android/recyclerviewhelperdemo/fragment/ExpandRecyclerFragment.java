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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bobomee.android.common.util.ToastUtil;
import com.bobomee.android.recyclerviewhelper.expandable.TreeNode;
import com.bobomee.android.recyclerviewhelper.expandable.TreeViewAdapter;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.ExpandCollapseListener;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.OnTreeNodeClickListener;
import com.bobomee.android.recyclerviewhelperdemo.R;
import com.bobomee.android.recyclerviewhelperdemo.bean.Artist;
import com.bobomee.android.recyclerviewhelperdemo.bean.Genre;
import com.bobomee.android.recyclerviewhelperdemo.viewbinds.ArtistViewBind;
import com.bobomee.android.recyclerviewhelperdemo.viewbinds.GenreViewBinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2016/12/18.下午8:07.
 *
 * @author bobomee.
 */

public class ExpandRecyclerFragment extends Fragment {

  private List<TreeNode> mRoot;
  private RecyclerView rv;

  public static ExpandRecyclerFragment newInstance() {
    Bundle args = new Bundle();
    ExpandRecyclerFragment fragment = new ExpandRecyclerFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    return inflater.inflate(R.layout.expandable_fragment, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initData();

    initViews(view);
  }

  private void initViews(View _view) {
    rv = (RecyclerView) _view.findViewById(R.id.recycler_view);
    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
    TreeViewAdapter treeViewAdapter =
        new TreeViewAdapter(mRoot, Arrays.asList(new ArtistViewBind(), new GenreViewBinder()));

    rv.setAdapter(treeViewAdapter);

    treeViewAdapter.addExpandCollapseListener(new ExpandCollapseListener() {
      @Override public void onGroupExpanded(RecyclerView.ViewHolder _holder, TreeNode _treeNode) {
        if (_holder instanceof GenreViewBinder.ViewHolder) {
          GenreViewBinder.ViewHolder viewHolder = (GenreViewBinder.ViewHolder) _holder;
          viewHolder.animateExpand();
        }
      }

      @Override public void onGroupCollapsed(RecyclerView.ViewHolder _holder, TreeNode _treeNode) {
        if (_holder instanceof GenreViewBinder.ViewHolder) {
          GenreViewBinder.ViewHolder viewHolder = (GenreViewBinder.ViewHolder) _holder;
          viewHolder.animateCollapse();
        }
      }

      @Override public void toggle(RecyclerView.ViewHolder _holder, TreeNode _treeNode) {
        //ToastUtil.show(getActivity(),"toggle");
      }
    });

    treeViewAdapter.addOnTreeNodeClickListener(new OnTreeNodeClickListener() {
      @Override public void onClick(TreeNode node, RecyclerView.ViewHolder holder) {
        ToastUtil.show(getActivity(), "addOnTreeNodeClick");
      }
    });
  }

  private void initData() {
    mRoot = new ArrayList<>();

    mRoot.add(new TreeNode<Genre>(new Genre("Jazz", R.drawable.ic_saxaphone)).addChild(
        new TreeNode<Artist>(new Artist("Bill Monroe")))
        .addChild(new TreeNode<Artist>(new Artist("Earl Scruggs")))
        .addChild(new TreeNode<Artist>(new Artist("Osborne Brothers")))
        .addChild(new TreeNode<Artist>(new Artist("John Hartford"))));

    mRoot.add(new TreeNode<Genre>(new Genre("Salsa", R.drawable.ic_maracas)).addChild(
        new TreeNode<Artist>(new Artist("Earl Scruggs")))
        .addChild(new TreeNode<Artist>(new Artist("Osborne Brothers")))
        .addChild(new TreeNode<Artist>(new Artist("John Hartford")))
        .addChild(new TreeNode<Genre>(new Genre("Bluegrass", R.drawable.ic_banjo)).addChild(
            new TreeNode<Artist>(new Artist("Earl Scruggs")))
            .addChild(new TreeNode<Artist>(new Artist("Osborne Brothers")))
            .addChild(new TreeNode<Artist>(new Artist("John Hartford")))
            .addChild(new TreeNode<Genre>(new Genre("Jazz", R.drawable.ic_saxaphone)).addChild(
                new TreeNode<Artist>(new Artist("Earl Scruggs")))
                .addChild(new TreeNode<Artist>(new Artist("Osborne Brothers")))
                .addChild(new TreeNode<Artist>(new Artist("John Hartford")))
                .addChild(new TreeNode<Genre>(new Genre("Jazz", R.drawable.ic_saxaphone)).addChild(
                    new TreeNode<Artist>(new Artist("Earl Scruggs")))
                    .addChild(new TreeNode<Artist>(new Artist("Bill Monroe")))
                    .addChild(new TreeNode<Artist>(new Artist("Earl Scruggs")))
                    .addChild(new TreeNode<Artist>(new Artist("Osborne Brothers")))
                    .addChild(new TreeNode<Artist>(new Artist("John Hartford")))))));
  }
}
