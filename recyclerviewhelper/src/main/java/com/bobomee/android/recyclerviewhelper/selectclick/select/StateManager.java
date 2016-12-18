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

package com.bobomee.android.recyclerviewhelper.selectclick.select;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LongSparseArray;
import android.util.SparseBooleanArray;

/**
 * Created on 2016/12/18.下午2:59.
 *
 * @author bobomee.
 */

public class StateManager {

  public static class CheckedIdStates extends LongSparseArray<Integer> implements Parcelable {
    public CheckedIdStates() {
      super();
    }

    private CheckedIdStates(Parcel in) {
      final int size = in.readInt();
      if (size > 0) {
        for (int i = 0; i < size; i++) {
          final long key = in.readLong();
          final int value = in.readInt();
          put(key, value);
        }
      }
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int flags) {
      final int size = size();
      parcel.writeInt(size);

      for (int i = 0; i < size; i++) {
        parcel.writeLong(keyAt(i));
        parcel.writeInt(valueAt(i));
      }
    }

    public static final Creator<CheckedIdStates> CREATOR = new Creator<CheckedIdStates>() {
      @Override public CheckedIdStates createFromParcel(Parcel in) {
        return new CheckedIdStates(in);
      }

      @Override public CheckedIdStates[] newArray(int size) {
        return new CheckedIdStates[size];
      }
    };
  }

  public static class CheckedStates extends SparseBooleanArray implements Parcelable {
    private static final int FALSE = 0;
    private static final int TRUE = 1;

    public CheckedStates() {
      super();
    }

    private CheckedStates(Parcel in) {
      final int size = in.readInt();
      if (size > 0) {
        for (int i = 0; i < size; i++) {
          final int key = in.readInt();
          final boolean value = (in.readInt() == TRUE);
          put(key, value);
        }
      }
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int flags) {
      final int size = size();
      parcel.writeInt(size);

      for (int i = 0; i < size; i++) {
        parcel.writeInt(keyAt(i));
        parcel.writeInt(valueAt(i) ? TRUE : FALSE);
      }
    }

    public static final Creator<CheckedStates> CREATOR = new Creator<CheckedStates>() {
      @Override public CheckedStates createFromParcel(Parcel in) {
        return new CheckedStates(in);
      }

      @Override public CheckedStates[] newArray(int size) {
        return new CheckedStates[size];
      }
    };
  }
}
