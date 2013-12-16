package net.kjmaster.cookiemom.booth.order;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.ui.CookieAmountsListInputFragment_;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;
import net.kmaster.cookiemom.dao.Order;
import net.kmaster.cookiemom.dao.OrderDao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class BoothOrderActivity extends CookieActionActivity {

    @Extra
    long boothId;

    @Extra
    int requestCode;

    @StringRes(R.string.add_order)
    String fragTag;

    @StringRes(R.string.add_order_title)
    String fragTitle;

    @AfterViews
    void afterViewFrag() {
        replaceFrag(createFragmentTransaction(fragTag), CookieAmountsListInputFragment_.builder().isEditable(true).isBoxes(true).build(), fragTag);
        createActionMode(fragTitle);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }


    protected void saveData() {
        OrderDao dao = Main.daoSession.getOrderDao();
        HashMap<String, Integer> orderedBoxes = new HashMap<String, Integer>();
        HashMap<String, Integer> availBoxes = new HashMap<String, Integer>();

        for (String cookieType : Constants.CookieTypes) {
            List<Order> orderList = dao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderCookieType.eq(cookieType))
                    .list();

            if (orderList != null) {
                int boxes = 0;
                for (Order order : orderList) {
                    boxes += order.getOrderedBoxes();
                }
                orderedBoxes.put(cookieType, boxes);
            }
            List<CookieTransactions> cookieTransactionsist = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                    .where(CookieTransactionsDao.Properties.CookieType.eq(cookieType)).list();
            if (cookieTransactionsist != null) {
                int boxes = 0;
                for (CookieTransactions cookieTransactions : cookieTransactionsist) {
                    boxes = boxes + cookieTransactions.getTransBoxes();
                }
                availBoxes.put(cookieType, boxes);
            }
        }


        Calendar c = Calendar.getInstance();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            String cookieType = Constants.CookieTypes[i];
            Integer intVal = Integer.valueOf(getFragment()
                    .valuesMap()
                    .get(cookieType)
            );
            Integer availBox = availBoxes.get(cookieType) - orderedBoxes.get(cookieType);
            if (availBox > 0) {
                intVal = intVal - availBox;
            }

            if (intVal > 0) {
                dao.insert(new Order(
                        null,
                        c.getTime(),
                        (boothId * -1) - 100,
                        cookieType,
                        false,
                        intVal,
                        false));
            }
        }
    }
}