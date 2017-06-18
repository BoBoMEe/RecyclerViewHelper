package com.bobomee.android.recyclerviewhelperdemo.bean;

import android.support.v4.app.Fragment;
import com.bobomee.android.recyclerviewhelperdemo.fragment.ExpandRecyclerFragment;
import com.bobomee.android.recyclerviewhelperdemo.fragment.FastScrollFragment;
import com.bobomee.android.recyclerviewhelperdemo.fragment.ItemClickFragment;
import com.bobomee.android.recyclerviewhelperdemo.fragment.PaginateFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * Project ID：400YF17051<br/>
 * Resume:
 *
 * @author 汪波
 * @version 1.0
 * @see
 * @since 2017/4/15 汪波 first commit
 */
public class PagerBean {

  private final String title;
  private final Fragment mFragment;

  private PagerBean(String title, Fragment fragment) {
    this.title = title;
    mFragment = fragment;
  }

  public String getTitle() {
    return title;
  }

  public Fragment getFragment() {
    return mFragment;
  }

  public static List<PagerBean> provide(){
    ArrayList<PagerBean> pagerBeens = new ArrayList<>();

    pagerBeens.add(new PagerBean("ClickMode", ItemClickFragment.newInstance()));
    pagerBeens.add(new PagerBean("Expanable", ExpandRecyclerFragment.newInstance()));
    pagerBeens.add(new PagerBean("FastScroller", FastScrollFragment.newInstance()));
    pagerBeens.add(new PagerBean("Paginate", PaginateFragment.newInstance()));

    return pagerBeens;
  }
}
