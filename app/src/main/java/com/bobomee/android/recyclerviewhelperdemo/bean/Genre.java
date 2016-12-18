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

package com.bobomee.android.recyclerviewhelperdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.bobomee.android.recyclerviewhelper.expandable.interfaces.LayoutItemType;
import com.bobomee.android.recyclerviewhelperdemo.R;

public class Genre implements Parcelable ,LayoutItemType{

  private int iconResId;
  private String  title;

  public Genre(String title, int iconResId) {
    this.title = title;
    this.iconResId = iconResId;
  }

  protected Genre(Parcel in) {
    iconResId = in.readInt();
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(iconResId);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static final Creator<Genre> CREATOR = new Creator<Genre>() {
    @Override public Genre createFromParcel(Parcel in) {
      return new Genre(in);
    }

    @Override public Genre[] newArray(int size) {
      return new Genre[size];
    }
  };

  public int getIconResId() {
    return iconResId;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Genre)) return false;

    Genre genre = (Genre) o;

    return getIconResId() == genre.getIconResId();

  }

  @Override
  public int hashCode() {
    return getIconResId();
  }

  @Override public int getLayoutId() {
    return R.layout.list_item_genre;
  }
}

