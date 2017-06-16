RecyclerViewHelper
============

Android library for easy to use `RecyclerView`

- Paginate ： Paginate for RecyclerView
- ItemClick ： Item Click for RecyclerView
- Expandable ： Expandable for RecyclerView
- FastSmoothScroll ： SmoothScroll speed scroll for RecyclerView

# Screenshot

![paginate](art/demo.gif)

# Setup

Gradle:
```groovy
compile 'com.bobomee.android:recyclerviewhelper:1.0.7'
```

# Usage


- Paginate ：[PaginateFragment](https://github.com/BoBoMEe/RecyclerViewHelper/blob/master/app/src/main/java/com/bobomee/android/recyclerviewhelperdemo/fragment/PaginateFragment.java)
- ItemClick ：[ItemClickFragment](https://github.com/BoBoMEe/RecyclerViewHelper/blob/master/app/src/main/java/com/bobomee/android/recyclerviewhelperdemo/fragment/ItemClickFragment.java)
- Expanable ：[ExpandRecyclerFragment](https://github.com/BoBoMEe/RecyclerViewHelper/blob/master/app/src/main/java/com/bobomee/android/recyclerviewhelperdemo/fragment/ExpandRecyclerFragment.java)
- FastScroll ： [FastScrollFragment](https://github.com/BoBoMEe/RecyclerViewHelper/blob/master/app/src/main/java/com/bobomee/android/recyclerviewhelperdemo/fragment/FastScrollFragment.java)


# Sample


## Click Mode

```java
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
```

## Expandable

```java
mRoot = new ArrayList<>();
TreeNode<Genre> genre = new TreeNode<Genre>(new Genre("Jazz", R.drawable.ic_saxaphone);
genre.addChilds(TreeNode ...);
//...
mRoot.add(genre);

TreeViewAdapter treeViewAdapter =
    new TreeViewAdapter(mRoot, Arrays.asList(new ArtistViewBind(), new GenreViewBinder()));

    rv.setAdapter(treeViewAdapter);

    treeViewAdapter.addExpandCollapseListener(new ExpandCollapseListener() {
      @Override public void onGroupExpanded(RecyclerView.ViewHolder _holder, TreeNode _treeNode) {
      //...
      }

      @Override public void onGroupCollapsed(RecyclerView.ViewHolder _holder, TreeNode _treeNode) {
      //..
      }

      @Override public void toggle(RecyclerView.ViewHolder _holder, TreeNode _treeNode) {
        //ToastUtil.show(getActivity(),"toggle");
      }
    });

    treeViewAdapter.addOnTreeNodeClickListener(new OnTreeNodeClickListener() {
      @Override public void onClick(TreeNode node, RecyclerView.ViewHolder holder) {
        //ToastUtil.show(getActivity(), "addOnTreeNodeClick");
      }
    });
```

## Fastscroller

```java
    mRecyclerView.setAdapter(mItemAdapter);

    RecyclerFastScroller fastScroller = (RecyclerFastScroller) view.findViewById(R.id.fast_scroller);
    fastScroller.setBubbleTextCreator(mItemAdapter);

    fastScroller.setRecyclerView(mRecyclerView);
    fastScroller.addOnScrollStateChangeListener(this);

    int color = getColorAccent(mActivity);
    fastScroller.setAccentColor(color);
```

## Scroll Speed Control

```java
//#TopSnappedSmoothScroller#calculateSpeedPerPixel
@Override
protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
		return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
}

// smoothscroll
mRecyclerView.setLayoutManager(
        new SmoothScrollStaggeredLayoutManager(mActivity,2, StaggeredGridLayoutManager.VERTICAL));

mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mRecyclerView.smoothScrollToPosition(0);
      }
    });
```

## Paginate

```java
Paginate.Callbacks callbacks = new Paginate.Callbacks() {
    @Override
    public void onLoadMore() {
        // Load next page of data (e.g. network or database)
    }

    @Override
    public boolean isLoading() {
        // Indicate whether new page loading is in progress or not
        return loadingInProgress;
    }

    @Override
    public boolean hasLoadedAllItems() {
        // Indicate whether all data (pages) are loaded or not
        return hasLoadedAllItems;
    }
};
```

Thanks&Links
--------

- [MarkoMilos/Paginate](https://github.com/MarkoMilos/Paginate)
- [lucasr/twoway-view](https://github.com/lucasr/twoway-view/)
- [TellH/RecyclerTreeView](https://github.com/TellH/RecyclerTreeView)
- [davideas/FlexibleAdapter](https://github.com/davideas/FlexibleAdapter)
