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


package net.kjmaster.cookiemom.summary.stat.booth;

//~--- non-JDK imports --------------------------------------------------------

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.kjmaster.cookiemom.R;

import java.text.NumberFormat;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SummaryStatBoothValuesListAdapter extends ArrayAdapter<SummaryStatBoothValues> {
    public SummaryStatBoothValuesListAdapter(Context context, List<SummaryStatBoothValues> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SummaryStatBoothValues item = getItem(position);

        // Without ViewHolder for demo purpose
        View view = convertView;

        if (view == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = li.inflate(R.layout.scout_expander_list_item, parent, false);
        }

        TextView textView1 = (TextView) view.findViewById(R.id.textView1);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        TextView textView3 = (TextView) view.findViewById(R.id.textView3);
        TextView textView4 = (TextView) view.findViewById(R.id.textView4);
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        fmt.setMaximumFractionDigits(0);
        fmt.setMinimumFractionDigits(0);
        textView1.setText(item.code);
        textView2.setText("" + Double.valueOf(item.value).intValue());
        textView3.setText("" + fmt.format(Double.valueOf(item.delta).intValue()));
        textView4.setText(fmt.format((double) item.deltaPerc));

        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
