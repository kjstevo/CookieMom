/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.kjmaster.cookiemom.ui.numberpicker;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


/**
 * User: derek Date: 6/21/13 Time: 10:37 AM
 */
public class NumberPickerErrorTextView extends TextView {

    private static final long LENGTH_SHORT = 3000;

    public NumberPickerErrorTextView(Context context) {
        super(context);
    }

    public NumberPickerErrorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerErrorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void show() {
        fadeInEndHandler.removeCallbacks(hideRunnable);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeInEndHandler.postDelayed(hideRunnable, LENGTH_SHORT);
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(fadeIn);
    }


    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    private Handler fadeInEndHandler = new Handler();

    public void hide() {
        fadeInEndHandler.removeCallbacks(hideRunnable);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(fadeOut);
    }

    public void hideImmediately() {
        fadeInEndHandler.removeCallbacks(hideRunnable);
        setVisibility(View.INVISIBLE);
    }
}
