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

package com.bobomee.android.recyclerviewhelperdemo.recycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2016/12/9.下午1:41.
 *
 * @author bobomee.
 *         https://github.com/BoBoMEe
 */

public class DataProvider {

  public static List<String> provide(int from, int pageSize) {
    List<String> result = new ArrayList<>();
    for (int i = from; i < from + pageSize; i++) {
      result.add(i + " ---> position");
    }

    return result;
  }

  public static class Data {
    public int mTotalItemCount;
    public int mTotalPage;
    public int mCurrentPage;
    public List<Content> mContent;

    public static class Content {
      public String mContent;
    }
  }

  public static Data providePaginateData(int current, int pageSize, int total) {
    Data data = new Data();

    data.mTotalItemCount = total;
    boolean zero = total % pageSize == 0;
    int count = total / pageSize;
    data.mTotalPage = zero ? count : count + 1;
    data.mCurrentPage = current;
    List<Data.Content> inner = new ArrayList<>();
    data.mContent = inner;

    int has = current * pageSize;
    int need = total - has;
    boolean b = need > pageSize;

    for (int i = has; i < (has + (b ? pageSize : need)); i++) {
      Data.Content content = new Data.Content();
      content.mContent = i + " ---> position";
      inner.add(content);
    }

    return data;
  }
}
