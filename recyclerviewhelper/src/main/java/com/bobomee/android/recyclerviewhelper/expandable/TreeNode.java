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

import android.support.annotation.NonNull;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.LayoutItemType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class TreeNode<T extends LayoutItemType> {
  private T content;
  private TreeNode parent;
  private List<TreeNode> childList;
  private boolean isExpand;
  //the tree high
  private int height = UNDEFINE;

  private static final int UNDEFINE = -1;

  public TreeNode(@NonNull T content) {
    this.content = content;
  }

  public int getHeight() {
    if (isRoot()) {
      height = 0;
    } else if (height == UNDEFINE) height = parent.getHeight() + 1;
    return height;
  }

  public boolean isRoot() {
    return parent == null;
  }

  public boolean isLeaf() {
    return childList == null || childList.isEmpty();
  }

  public boolean isParent() {
    return !isLeaf();
  }

  public void setContent(T content) {
    this.content = content;
  }

  public T getContent() {
    return content;
  }

  public List<TreeNode> getChildList() {
    return childList;
  }

  public void setChildList(List<TreeNode> childList) {
    this.childList = childList;
  }

  public TreeNode addChild(TreeNode node) {
    if (childList == null) childList = new ArrayList<>();
    childList.add(node);
    node.parent = this;
    return this;
  }

  public boolean toggle() {
    isExpand = !isExpand;
    return isExpand;
  }

  public boolean isExpand() {
    return isExpand;
  }

  public void setParent(TreeNode parent) {
    this.parent = parent;
  }

  @Override public String toString() {
    return "TreeNode{" +
        "content=" + this.content +
        ", parent=" + (parent == null ? "null" : parent.getContent().toString()) +
        ", childList=" + (childList == null ? "null" : childList.toString()) +
        ", isExpand=" + isExpand +
        '}';
  }
}
