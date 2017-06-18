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
package com.bobomee.android.recyclerviewhelper.smoothlayout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Optimized implementation of GridLayoutManager to SmoothScroll to a Top position.
 *
 * @since 5.0.0-b6
 */
class SmoothScrollGridLayoutManager extends GridLayoutManager {

	private final RecyclerView.SmoothScroller mSmoothScroller;

	public SmoothScrollGridLayoutManager(Context context, int spanCount) {
		this(context, spanCount, VERTICAL, false);
	}

	private SmoothScrollGridLayoutManager(Context context, int spanCount, int orientation,
			boolean reverseLayout) {
		super(context, spanCount, orientation, reverseLayout);
		mSmoothScroller = new TopSnappedSmoothScroller(context, this);
	}

	@Override
	public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
		mSmoothScroller.setTargetPosition(position);
		startSmoothScroll(mSmoothScroller);
	}

}