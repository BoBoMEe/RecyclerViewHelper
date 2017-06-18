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
import com.bobomee.android.recyclerviewhelperdemo.bean.PagerBean;
import java.util.List;

/**
 * Created on 2016/12/16.下午2:52.
 *
 * @author bobomee.
 */

class PageAdapter extends FragmentPagerAdapter {

  private final List<PagerBean> pagerBeans;

  public PageAdapter(FragmentManager fm, List<PagerBean> pagerBeens) {
    super(fm);
    this.pagerBeans = pagerBeens;
  }

  @Override public Fragment getItem(int i) {
    PagerBean pagerBean = pagerBeans.get(i);
    return pagerBean.getFragment();
  }

  @Override public int getCount() {
    return pagerBeans.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    return pagerBeans.get(position).getTitle();
  }
}