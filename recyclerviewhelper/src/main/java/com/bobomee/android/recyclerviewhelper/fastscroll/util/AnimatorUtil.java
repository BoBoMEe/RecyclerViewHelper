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

package com.bobomee.android.recyclerviewhelper.fastscroll.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created on 2016/12/27.下午1:07.
 *
 * @author bobomee.
 */

public class AnimatorUtil {

  private static final int BUBBLE_ANIMATION_DURATION = 300;

  public static ObjectAnimator to_alpha(View target) {

    ObjectAnimator objectAnimator = null;
    if (null != target && target.getVisibility() != View.VISIBLE) {
      target.setVisibility(View.VISIBLE);

      objectAnimator = ObjectAnimator.ofFloat(target, View.ALPHA, 0f, 1f).setDuration(BUBBLE_ANIMATION_DURATION);

      objectAnimator.start();
    }

    return objectAnimator;
  }

  public static ObjectAnimator alpha_to(final View target) {

    ObjectAnimator objectAnimator = null;

    if (null != target) {
      objectAnimator = ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0f).setDuration(BUBBLE_ANIMATION_DURATION);

      objectAnimator.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationCancel(Animator animation) {
          super.onAnimationCancel(animation);
          target.setVisibility(View.GONE);
        }

        @Override public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);
          target.setVisibility(View.GONE);
        }
      });

      objectAnimator.start();
    }

    return objectAnimator;
  }
}
