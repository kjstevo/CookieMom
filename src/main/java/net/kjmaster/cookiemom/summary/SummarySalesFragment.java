package net.kjmaster.cookiemom.summary;

import android.support.v4.app.Fragment;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static net.kmaster.cookiemom.dao.CookieTransactionsDao.Properties.TransBoothId;
import static net.kmaster.cookiemom.dao.CookieTransactionsDao.Properties.TransBoxes;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/21/13
 * Time: 9:38 PM
 */
@EFragment(R.layout.fragment_bar)
public class SummarySalesFragment extends Fragment {

    @ViewById(net.kjmaster.cookiemom.R.id.bargraph)
    BarGraph barGraph;

    @AfterViews
    void afterViews() {
        List<CookieTransactions> list = Main.daoSession.getCookieTransactionsDao().loadAll();
        int total = 0;
        Double totalCash = 0.0;
        int totalScout = 0;
        int totalBooth = 0;
        Double totalScoutCash = 0.0;
        Double totalBoothCash = 0.0;
        String boxesColumn = TransBoxes.columnName;
        String boothIdColumn = TransBoothId.columnName;
        String tablename = CookieTransactionsDao.TABLENAME;

        for (CookieTransactions cookieTransactions : list) {

            totalCash += cookieTransactions.getTransCash();
            if (cookieTransactions.getTransBoothId() >= 0) {
                // totalBooth+=(cookieTransactions.getTransBoxes());
                totalBoothCash += cookieTransactions.getTransCash();
            } else {
                if (cookieTransactions.getTransScoutId() >= 0) {
                    //  totalScout+=(cookieTransactions.getTransBoxes());
                    totalScoutCash += cookieTransactions.getTransCash();
                } else {
                    total += cookieTransactions.getTransBoxes();
                }

            }
        }
        NumberFormat fmt = NumberFormat.getCurrencyInstance();

        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d = new Bar();

        d.setColor(getResources().getColor(R.color.bar_due));
        d.setName("Due");
        d.setValue(total * 4);

        d.setValueString(fmt.format(total * 4));
        Bar d2 = new Bar();
        d2.setColor(getResources().getColor(R.color.bar_cash));
        d2.setName("Cash");
        d2.setValue(totalCash.floatValue());
        d2.setValueString(fmt.format(totalCash));

//        Bar d3=new Bar();
//        d3.setColor(Color.parseColor("#99CC00"));
//        d3.setName("Booths");
//        d3.setValue(totalBooth*-4);
//        d3.setValueString(fmt.format(totalBooth*4));
        Bar d4 = new Bar();
        d4.setColor(getResources().getColor(R.color.bar_booth));
        d4.setName("Booths");
        d4.setValue(totalBoothCash.floatValue());
        d4.setValueString(fmt.format(totalBoothCash));
        //     Bar d5=new Bar();
//        d5.setColor(Color.parseColor("#99CC00"));
//        d5.setName("Scouts");
//        d5.setValue(totalScout*-4);
//        d5.setValueString(fmt.format(totalScout*4));
        Bar d6 = new Bar();
        d6.setColor(getResources().getColor(R.color.bar_scout));
        d6.setName("Scouts");
        d6.setValue(totalScoutCash.floatValue());
        d6.setValueString(fmt.format(totalScoutCash));
        points.add(d);
        points.add(d2);
        //points.add(d3);
        points.add(d4);
        //       points.add(d5);
        points.add(d6);


        BarGraph g = barGraph;
        g.setBars(points);

        g.setOnBarClickedListener(new BarGraph.OnBarClickedListener() {

            @Override
            public void onClick(int index) {

            }

        });
    }
}
