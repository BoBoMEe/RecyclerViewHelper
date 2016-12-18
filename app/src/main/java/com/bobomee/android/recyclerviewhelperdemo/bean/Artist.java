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

public class Artist implements Parcelable ,LayoutItemType{

  private String name;

  public Artist(String name) {
    this.name = name;
  }

  protected Artist(Parcel in) {
    name = in.readString();
  }

  public String getName() {
    return name;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Artist)) return false;

    Artist artist = (Artist) o;

    return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;
  }

  @Override public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    return result;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
  }

  @Override public int describeContents() {
    return 0;
  }

  public static final Creator<Artist> CREATOR = new Creator<Artist>() {
    @Override public Artist createFromParcel(Parcel in) {
      return new Artist(in);
    }

    @Override public Artist[] newArray(int size) {
      return new Artist[size];
    }
  };

  @Override public int getLayoutId() {
    return R.layout.list_item_artist;
  }
}

