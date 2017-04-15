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

package com.bobomee.android.recyclerviewhelperdemo.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.bobomee.android.recyclerviewhelperdemo.fragment.ExpandRecyclerFragment;
import com.bobomee.android.recyclerviewhelperdemo.fragment.FastScrollFragment;
import com.bobomee.android.recyclerviewhelperdemo.fragment.ItemClickFragment;

/**
 * Created on 2016/12/16.下午2:52.
 *
 * @author bobomee.
 */

public class PageAdapter extends FragmentPagerAdapter {

  public PageAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int i) {
    Fragment fragment;
    switch (i) {
      case 0:
        fragment = new ItemClickFragment();
        break;
      case 1:
        fragment = new ExpandRecyclerFragment();
        break;
      case 2:
        fragment = new FastScrollFragment();
        break;
      default:
        fragment = new Fragment();
        break;
    }
    return fragment;
  }

  @Override public int getCount() {
    return 3;
  }

  @Override public CharSequence getPageTitle(int position) {
    return "pos:" + position;
  }
}