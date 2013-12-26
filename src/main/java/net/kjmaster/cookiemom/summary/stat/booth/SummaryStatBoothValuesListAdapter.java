/*
 * ******************************************************************************
 *   Copyright (c) 2013 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
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
