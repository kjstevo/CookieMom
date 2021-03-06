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

import android.widget.TextView;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.global.Constants;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/21/13
 * Time: 6:35 PM
 */
@EFragment(R.layout.summary_piegraph_fragment)
public class SummaryCookiesFragment extends android.support.v4.app.Fragment {

    @ViewById(R.id.piegraph)
    PieGraph pg;
    @ViewById(R.id.legend)
    TextView legend;

//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //final View v = inflater.inflate(R.layout.summary_piegraph_fragment, container, false);


    @AfterViews
    void afterViews() {
        String[] cookieTypes = Constants.CookieTypes;
        final HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
        for (int i = 0; i < cookieTypes.length; i++) {
            String cookieType = cookieTypes[i];
            List<CookieTransactions> list =
                    Main.daoSession.getCookieTransactionsDao().queryBuilder()
                            .where(
                                    CookieTransactionsDao.Properties.CookieType.eq(cookieType),
                                    CookieTransactionsDao.Properties.TransBoothId.lt(0),
                                    CookieTransactionsDao.Properties.TransScoutId.lt(0))
                            .list();
            int total = 0;
            for (CookieTransactions trans : list) {
                total += trans.getTransBoxes();

            }
            hashMap.put(cookieType, total);
            PieSlice slice = new PieSlice();

            slice.setValue(total);
            if (total > 255) {
                total = total - ((total - 255) * 2);
            }
            if (total < 0) {
                //noinspection UnusedAssignment
                total = 0;
            }
            //    slice.setColor(Color.parseColor("#FFBB33"));
            slice.setColor(getResources().getColor(Constants.CookieColors[i]));
            slice.setTitle(cookieType);

            pg.addSlice(slice);

//        slice = new PieSlice();
//        slice.setColor(Color.parseColor("#FFBB33"));
//        slice.setValue(3);
//        pg.addSlice(slice);
//        slice = new PieSlice();
//        slice.setColor(Color.parseColor("#AA66CC"));
//        slice.setValue(8);
//        pg.addSlice(slice);
        }

        pg.setOnSliceClickedListener(new PieGraph.OnSliceClickedListener() {

            @Override
            public void onClick(int index) {
                try {
                    String title = pg.getSlice(index).getTitle();
                    legend.setText(title + ": " + hashMap.get(title).toString() + "bxs.");
                } catch (Exception ex) {

                }

            }

        });


    }
}
