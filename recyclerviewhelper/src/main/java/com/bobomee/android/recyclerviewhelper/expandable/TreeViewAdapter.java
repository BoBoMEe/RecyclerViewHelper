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

package com.bobomee.android.recyclerviewhelper.expandable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.ExpandCollapse;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.ExpandCollapseListener;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.OnTreeNodeClickListener;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.TreeNodeClick;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlh on 2016/10/1 :)
 */
public class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements ExpandCollapseListener {
  private final List<? extends TreeViewBinder> viewBinders;
  private final List<TreeNode> displayNodes;
  private boolean toCollapseChild;

  private final ExpandCollapse mExpandCollapse;
  private final TreeNodeClick mTreeNodeClick;

  public TreeViewAdapter(List<TreeNode> nodes, List<? extends TreeViewBinder> viewBinders) {
    displayNodes = new ArrayList<>();
    if (nodes != null) findDisplayNodes(nodes);
    this.viewBinders = viewBinders;

    mExpandCollapse = new ExpandCollapse();
    mTreeNodeClick = new TreeNodeClick();
  }

  private void findDisplayNodes(List<TreeNode> nodes) {
    for (TreeNode node : nodes) {
      displayNodes.add(node);
      if (!node.isLeaf() && node.isExpand()) //noinspection unchecked
        findDisplayNodes(node.getChildList());
    }
  }

  @Override public int getItemViewType(int position) {
    return displayNodes.get(position).getContent().getLayoutId();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    if (viewBinders.size() == 1) return viewBinders.get(0).provideViewHolder(v);
    for (TreeViewBinder viewBinder : viewBinders) {
      if (viewBinder.getLayoutId() == viewType) return viewBinder.provideViewHolder(v);
    }
    return viewBinders.get(0).provideViewHolder(v);
  }

  @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        TreeNode selectedNode = displayNodes.get(holder.getLayoutPosition());

        toggle(holder, selectedNode);

        if (mTreeNodeClick.hasListener()) {
          mTreeNodeClick.onNodeClick(selectedNode, holder);
        }
      }
    });
    for (TreeViewBinder viewBinder : viewBinders) {
      if (viewBinder.getLayoutId() == displayNodes.get(position).getContent().getLayoutId()) {
        //noinspection unchecked
        viewBinder.bindView(holder, position, displayNodes.get(position));
      }
    }
  }

  private int addChildNodes(TreeNode pNode, int startIndex) {
    @SuppressWarnings("unchecked") List<TreeNode> childList = pNode.getChildList();
    int addChildCount = 0;
    for (TreeNode treeNode : childList) {
      displayNodes.add(startIndex + addChildCount++, treeNode);
      if (treeNode.isExpand()) {
        addChildCount += addChildNodes(treeNode, startIndex + addChildCount);
      }
    }
    return addChildCount;
  }

  private int removeChildNodes(TreeNode pNode) {
    if (pNode.isLeaf()) return 0;
    @SuppressWarnings("unchecked") List<TreeNode> childList = pNode.getChildList();
    int removeChildCount = childList.size();
    displayNodes.removeAll(childList);
    for (TreeNode treeNode : childList) {
      if (treeNode.isExpand()) {
        if (toCollapseChild) treeNode.toggle();
        removeChildCount += removeChildNodes(treeNode);
      }
    }
    return removeChildCount;
  }

  @Override public int getItemCount() {
    return displayNodes == null ? 0 : displayNodes.size();
  }

  public void ifCollapseChildWhileCollapseParent(boolean toCollapseChild) {
    this.toCollapseChild = toCollapseChild;
  }

  public void addOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
    mTreeNodeClick.addListener(onTreeNodeClickListener);
  }

  public void addExpandCollapseListener(ExpandCollapseListener _expandCollapseListener) {
    mExpandCollapse.addListener(_expandCollapseListener);
  }

  public void refresh(List<TreeNode> treeNodes) {
    displayNodes.clear();
    findDisplayNodes(treeNodes);
    notifyDataSetChanged();
  }

  @Override public void onGroupExpanded(RecyclerView.ViewHolder holder, TreeNode _treeNode) {
    if (mExpandCollapse.hasListener()) mExpandCollapse.expand(holder, _treeNode);
    notifyItemRangeInserted(holder.getLayoutPosition() + 1,
        addChildNodes(_treeNode, holder.getLayoutPosition() + 1));
  }

  @Override public void onGroupCollapsed(RecyclerView.ViewHolder holder, TreeNode _treeNode) {
    if (mExpandCollapse.hasListener()) mExpandCollapse.collaps(holder, _treeNode);
    notifyItemRangeRemoved(holder.getLayoutPosition() + 1, removeChildNodes(_treeNode));
  }

  @Override public void toggle(RecyclerView.ViewHolder holder, TreeNode _treeNode) {
    if (_treeNode.isParent()) {
      _treeNode.toggle();
      if (mExpandCollapse.hasListener()) mExpandCollapse.toggle(holder, _treeNode);
      boolean expand = _treeNode.isExpand();
      if (expand) {
        onGroupExpanded(holder, _treeNode);
      } else {
        onGroupCollapsed(holder, _treeNode);
      }
    }
  }
}