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


package net.kjmaster.cookiemom.summary.stat.scout;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;


/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SummaryStatScoutListLayout extends LinearLayout implements View.OnClickListener {

    public SummaryStatScoutListLayout(Context context) {
        super(context);
    }

    public SummaryStatScoutListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SummaryStatScoutListLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(SummaryStatScoutValuesListAdapter list) {
        setOrientation(VERTICAL);

        // Popolute list
        if (list != null) {
            for (int i = 0; i < list.getCount(); i++) {
                View view = list.getView(i, null, null);
                this.addView(view);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void setListener(OnClickListener listener) {
    }

    @Override
    public void onClick(View v) {
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
