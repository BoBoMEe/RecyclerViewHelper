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

package com.bobomee.android.recyclerviewhelper.paginate;

import android.support.v7.widget.GridLayoutManager;

class WrapperSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private final GridLayoutManager.SpanSizeLookup wrappedSpanSizeLookup;
    private final LoadingListItemSpanLookup loadingListItemSpanLookup;
    private final WrapperAdapter wrapperAdapter;

    public WrapperSpanSizeLookup(GridLayoutManager.SpanSizeLookup gridSpanSizeLookup,
                                 LoadingListItemSpanLookup loadingListItemSpanLookup,
                                 WrapperAdapter wrapperAdapter) {
        this.wrappedSpanSizeLookup = gridSpanSizeLookup;
        this.loadingListItemSpanLookup = loadingListItemSpanLookup;
        this.wrapperAdapter = wrapperAdapter;
    }

    @Override
    public int getSpanSize(int position) {
        if (wrapperAdapter.isLoadingRow(position)) {
            return loadingListItemSpanLookup.getSpanSize();
        } else {
            return wrappedSpanSizeLookup.getSpanSize(position);
        }
    }

    public GridLayoutManager.SpanSizeLookup getWrappedSpanSizeLookup() {
        return wrappedSpanSizeLookup;
    }
}