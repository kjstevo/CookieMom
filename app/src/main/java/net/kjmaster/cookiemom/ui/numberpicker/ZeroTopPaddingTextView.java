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
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import net.kjmaster.cookiemom.R;

/**
 * Displays text with no padding at the top.
 */
@SuppressWarnings("ALL")
public class ZeroTopPaddingTextView extends TextView {

    private static final float NORMAL_FONT_PADDING_RATIO = 0.328f;
    // the bold fontface has less empty space on the top
    private static final float BOLD_FONT_PADDING_RATIO = 0.208f;

    private static final float NORMAL_FONT_BOTTOM_PADDING_RATIO = 0.25f;
    // the bold fontface has less empty space on the top
    private static final float BOLD_FONT_BOTTOM_PADDING_RATIO = 0.208f;

    // pre-ICS (Droid Sans) has weird empty space on the bottom
    private static final float PRE_ICS_BOTTOM_PADDING_RATIO = 0.233f;

    private static final Typeface SAN_SERIF_BOLD = Typeface.create("san-serif", Typeface.BOLD);
    private static final Typeface SAN_SERIF_CONDENSED_BOLD = Typeface.create("sans-serif-condensed", Typeface.BOLD);

    private int mPaddingRight = 0;

    private String decimalSeperator = "";
    private String timeSeperator = "";

    public ZeroTopPaddingTextView(Context context) {
        this(context, null);
    }

    public ZeroTopPaddingTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZeroTopPaddingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        setIncludeFontPadding(false);
        updatePadding();
    }

    private void init() {
        decimalSeperator = getResources().getString(R.string.number_picker_seperator);
        timeSeperator = getResources().getString(R.string.time_picker_time_seperator);
    }

    public void updatePadding() {
        float paddingRatio = NORMAL_FONT_PADDING_RATIO;
        float bottomPaddingRatio = NORMAL_FONT_BOTTOM_PADDING_RATIO;
        if (getPaint().getTypeface() != null && getPaint().getTypeface().equals(Typeface.DEFAULT_BOLD)) {
            paddingRatio = BOLD_FONT_PADDING_RATIO;
            bottomPaddingRatio = BOLD_FONT_BOTTOM_PADDING_RATIO;
        }
        if (getTypeface() != null && getTypeface().equals(SAN_SERIF_BOLD)) {
            paddingRatio = BOLD_FONT_PADDING_RATIO;
            bottomPaddingRatio = BOLD_FONT_BOTTOM_PADDING_RATIO;
        }
        if (getTypeface() != null && getTypeface().equals(SAN_SERIF_CONDENSED_BOLD)) {
            paddingRatio = BOLD_FONT_PADDING_RATIO;
            bottomPaddingRatio = BOLD_FONT_BOTTOM_PADDING_RATIO;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
                getText() != null &&
                (getText().toString().equals(decimalSeperator) ||
                        getText().toString().equals(timeSeperator))) {
            bottomPaddingRatio = PRE_ICS_BOTTOM_PADDING_RATIO;
        }
        // no need to scale by display density because getTextSize() already returns the font
        // height in px
        setPadding(0, (int) (-paddingRatio * getTextSize()), mPaddingRight,
                (int) (-bottomPaddingRatio * getTextSize()));
    }

    public void updatePaddingForBoldDate() {
        float paddingRatio = BOLD_FONT_PADDING_RATIO;
        float bottomPaddingRatio = BOLD_FONT_BOTTOM_PADDING_RATIO;
        // no need to scale by display density because getTextSize() already returns the font
        // height in px
        setPadding(0, (int) (-paddingRatio * getTextSize()), mPaddingRight,
                (int) (-bottomPaddingRatio * getTextSize()));
    }

    public void setPaddingRight(int padding) {
        mPaddingRight = padding;
        updatePadding();
    }
}