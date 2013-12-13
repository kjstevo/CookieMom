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

            view = li.inflate(R.layout.expander_list_item, parent, false);
            TextView textView1 = (TextView) view.findViewById(R.id.textView1);
            TextView textView2 = (TextView) view.findViewById(R.id.textView2);
            TextView textView3 = (TextView) view.findViewById(R.id.textView3);
            TextView textView4 = (TextView) view.findViewById(R.id.textView4);
            NumberFormat fmt = NumberFormat.getCurrencyInstance();
            textView1.setText(item.getDate());
            textView2.setText("" + String.valueOf(item.getBoxes()));
            textView3.setText("" + fmt.format(item.getCash()));
            textView4.setText(item.getCookie());

        }
        return view;


    }
}
