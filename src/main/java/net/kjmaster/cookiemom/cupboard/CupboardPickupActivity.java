package net.kjmaster.cookiemom.cupboard;

import android.annotation.SuppressLint;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.CookieTransactions;
import net.kjmaster.cookiemom.dao.CookieTransactionsDao;
import net.kjmaster.cookiemom.dao.Order;
import net.kjmaster.cookiemom.dao.OrderDao;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ISettings_;

import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@SuppressLint("Registered")
@EActivity(R.layout.scout_order_layout)
public class CupboardPickupActivity extends CookieActionActivity {


    @Pref
    ISettings_ iSettings;

    @AfterViews
    void afterViewFrag() {

        String fragTag = getString(R.string.pickup_from_cupboard);

        replaceFrag(createFragmentTransaction(fragTag), CupboardPickupFragment_.builder().isBoxes(false).build(), fragTag);

        createActionMode(getString(R.string.pickup_order_title));

    }


    @Extra
    boolean isEditable;

    @Override
    protected boolean isEditable() {
        return isEditable;
    }

    @Override
    protected void saveData() {

        CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();

        OrderDao orderDao = Main.daoSession.getOrderDao();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {

            String cookieFlavor = Constants.CookieTypes[i];

            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));

            Calendar c = Calendar.getInstance();

            dao.insert(new CookieTransactions(
                    null,
                    -1L,
                    -1L,
                    cookieFlavor,
                    totalBoxes,
                    c.getTime(),
                    (double) 0
            ));

            List<Order> orderList = orderDao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderCookieType.eq(cookieFlavor),
                            OrderDao.Properties.OrderedFromCupboard.eq(true),
                            OrderDao.Properties.PickedUpFromCupboard.eq(false))
                    .orderAsc(OrderDao.Properties.OrderDate)
                    .list();

            for (Order order : orderList) {
                if (totalBoxes >= order.getOrderedBoxes()) {
                    totalBoxes = totalBoxes - order.getOrderedBoxes();

                    order.setPickedUpFromCupboard(true);
                } else {
                    if (totalBoxes > 0) {
                        orderDao.insert(new Order(
                                null,
                                order.getOrderDate(),
                                order.getOrderScoutId(),
                                order.getOrderCookieType(),
                                true,
                                (order.getOrderedBoxes() - totalBoxes),
                                false

                        ));

                        order.setOrderedBoxes(totalBoxes);
                        order.setPickedUpFromCupboard(true);
                        totalBoxes = 0;
                    }
                }

                orderDao.update(order);

            }
        }

        setResult(RESULT_OK);

    }


}
