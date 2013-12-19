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
        replaceFrag(
                createFragmentTransaction(fragTag),
                CookieAmountsListInputFragment_.builder()
                        .isEditable(true)
                        .isBoxes(true)
                        .build(),
                fragTag
        );

        createActionMode(fragTitle);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }


    protected void saveData() {
        OrderDao dao = Main.daoSession.getOrderDao();

        //Get a list of orders whos cookies are in the inventory awaiting
        //pick up so we dont jack 'em
        for (String cookieType : Constants.CookieTypes) {
            List<Order> orderList = dao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderCookieType.eq(cookieType),
                            OrderDao.Properties.PickedUpFromCupboard.eq(true))
                    .list();

            //add the total orders awaiting pickup from scout
            int orderedBoxesWaiting = 0;
            if (orderList != null) {
                for (Order order : orderList) {
                    orderedBoxesWaiting += order.getOrderedBoxes();
                }
            }
            //get current inventory total
            List<CookieTransactions> cookieTransactionsist = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                    .where(CookieTransactionsDao.Properties.CookieType.eq(cookieType)).list();
            if (cookieTransactionsist != null) {
                int boxes = 0;
                for (CookieTransactions cookieTransactions : cookieTransactionsist) {
                    boxes = boxes + cookieTransactions.getTransBoxes();
                }
                //sub the boxes awaiting p/u
                boxes = boxes - orderedBoxesWaiting;
                if (boxes < 0) boxes = 0;

                // get the current booth order num of boxes
                Integer currentOrder = Integer.valueOf(getFragment()
                        .valuesMap()
                        .get(cookieType)
                );

                //number of boxes not in inv need to be ordered
                //the extra are marked awaiting p/u
                Integer currentShortfall = currentOrder - boxes;

                if (currentShortfall <= 0) {
                    currentShortfall = 0;
                    createOrder(cookieType, currentOrder, true);
                }
                if (currentShortfall > 0) {
                    createOrder(cookieType, currentShortfall, false);
                    if (currentOrder > currentShortfall) {
                        createOrder(cookieType, currentOrder - currentShortfall, true);

                    }
                }

            }
        }
    }


    private void createOrder(String cookieType, Integer amountl, boolean pickedUp) {
        Calendar c = Calendar.getInstance();
        OrderDao dao = Main.daoSession.getOrderDao();

        dao.insert(new Order(
                null,
                c.getTime(),
                (boothId * -1) - 100,
                cookieType,
                pickedUp,
                amountl,
                pickedUp));
    }
}
