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

package net.kjmaster.cookiemom.summary;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ItemSelect;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.summary.stat.SummaryStatFragment_;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/21/13
 * Time: 6:10 PM
 */
@EFragment(R.layout.summary_main_fragment)
public class SummaryFragment extends Fragment {

    @ViewById(R.id.spinner)
    Spinner spinner;

    private final String[] tags = new String[]{"Cookies", "Sales", "Stats"};

    @AfterViews
    void afterViews() {

// Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.tab_names, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.addAll(tags);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.graph_content, SummaryCookiesFragment_.builder().build(), tags[0]);
        fragmentTransaction.commit();
    }

    @ItemSelect(R.id.spinner)
    void onSelected(boolean selected, int pos) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(tags[pos]);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }
        switch (pos) {
            case 0:
                ft.replace(R.id.graph_content, SummaryCookiesFragment_.builder().build(), tags[pos]);
                break;
            case 1:
                ft.replace(R.id.graph_content, SummarySalesFragment_.builder().build(), tags[pos]);
                break;
            case 2:
                ft.replace(R.id.graph_content, SummaryStatFragment_.builder().build(), tags[pos]);
                break;
            default:
                ft.replace(R.id.graph_content, SummaryCookiesFragment_.builder().build(), tags[pos]);


                break;
        }

        ft.commit();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
