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

package net.kjmaster.cookiemom.scout.expander;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.kjmaster.cookiemom.R;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/11/13
 * Time: 4:39 PM
 */
public class InnerExpanderAdapter extends ArrayAdapter<InnerExpanderValues> {
    public InnerExpanderAdapter(Context context, List<InnerExpanderValues> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InnerExpanderValues item = getItem(position);
        View view = convertView;

        if (view == null) {
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = li.inflate(R.layout.scout_expander_list_item, parent, false);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView textView2 = (TextView) view.findViewById(R.id.textView2);
            TextView textView3 = (TextView) view.findViewById(R.id.textView3);
            TextView textView4 = (TextView) view.findViewById(R.id.textView4);
            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            fmt.setMaximumFractionDigits(0);
            fmt.setMinimumFractionDigits(0);
            textView1.setText(item.getDate());
            textView2.setText("" + String.valueOf(item.getBoxes()));
            textView3.setText("" + fmt.format(item.getCash()));
            textView4.setText(item.getCookie());

        }
        return view;


    }
}
