package net.kjmaster.cookiemom.scout;

import android.widget.RadioGroup;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.global.ICookieActionFragment;
import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.ui.CookieAmountsListInputFragment_;
import net.kmaster.cookiemom.dao.CookieTransactions;
import net.kmaster.cookiemom.dao.CookieTransactionsDao;
import net.kmaster.cookiemom.dao.Order;
import net.kmaster.cookiemom.dao.OrderDao;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static net.kjmaster.cookiemom.global.Constants.CookieTypes;
import static net.kmaster.cookiemom.dao.OrderDao.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class ScoutPickupActivity extends CookieActionActivity {

    @Extra
    long ScoutId;

    @Extra
    boolean isEditable;

    @StringRes(R.string.scout_pickup_order_title)
    String scout_pickup_title;

    @StringRes(R.string.scout_pickup_order)
    String scout_pickup_order;

    @StringRes(R.string.pickup_order)
    String pickup_order;

    @Pref
    ISettings_ iSettings;


    @StringRes(R.string.cancel)
    String resCancel;
    private final HashMap<String, String> valuesMap = new HashMap<String, String>();

    @AfterViews
    void afterViewFrag() {

        replaceFrag(
                createFragmentTransaction(
                        scout_pickup_order), CookieAmountsListInputFragment_
                .builder()
                .isBoxes(true)
                .isEditable(this.isEditable)
                .build(),
                scout_pickup_order
        );

        createActionMode(scout_pickup_title);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup_boxes_cases);
        if (radioGroup != null) {
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.boxes_radio_button:
                            boxesClick();
                            break;
                        case R.id.cases_radio_button:
                            casesClick();
                            break;

                    }

                }
            });
        }

    }


    private void savePickupData() {
        if (!isEditable) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            Calendar c = Calendar.getInstance();

            CookieTransactionsDao dao = Main.daoSession.getCookieTransactionsDao();

            OrderDao orderDao = Main.daoSession.getOrderDao();

            for (String cookieFlavor : CookieTypes) {
                Integer boxesInInventory = 0;
                List<CookieTransactions> transactionsList = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                        .where(
                                CookieTransactionsDao.Properties.CookieType.eq(cookieFlavor)
                        )
                        .list();
                for (CookieTransactions transaction : transactionsList) {
                    boxesInInventory += transaction.getTransBoxes();
                }

                Integer requestedBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));
                if (boxesInInventory > 0) {
                    if (requestedBoxes <= boxesInInventory) {
                        CookieTransactions cookieTransactions = new CookieTransactions(
                                null, ScoutId, -1L, cookieFlavor, requestedBoxes * -1,
                                c.getTime(),
                                (double) 0
                        );
                        dao.insert(cookieTransactions);
                    } else {
                        CookieTransactions cookieTransactions = new CookieTransactions(
                                null, ScoutId, -1L, cookieFlavor, boxesInInventory * -1,
                                c.getTime(),
                                (double) 0
                        );
                        dao.insert(cookieTransactions);
                        requestedBoxes = boxesInInventory;
                    }
                } else {
                    List<Order> orders = orderDao.queryBuilder()
                            .where(
                                    Properties.OrderScoutId.eq(ScoutId),
                                    Properties.OrderCookieType.eq(cookieFlavor)
                            ).list();
                    for (Order order : orders) {
                        order.setPickedUpFromCupboard(false);
                        orderDao.update(order);
                    }

                }


                List<Order> orderList =
                        orderDao.queryBuilder().where(
                                Properties.OrderCookieType.eq(cookieFlavor),
                                Properties.OrderScoutId.eq(ScoutId)

                        ).orderAsc(
                                Properties.OrderDate
                        )
                                .list();

                for (Order order : orderList) {
                    if (requestedBoxes >= order.getOrderedBoxes()) {
                        requestedBoxes = requestedBoxes - order.getOrderedBoxes();

                        if (order.getPickedUpFromCupboard()) {
                            orderDao.delete(order);
                        } else {
                            order.setOrderScoutId(-1);
                            orderDao.update(order);
                        }
                    } else {
                        if (requestedBoxes > 0) {
                            Order order2 = new Order(
                                    null,
                                    order.getOrderDate(),
                                    order.getOrderScoutId(),
                                    order.getOrderCookieType(),
                                    false,
                                    (order.getOrderedBoxes() - requestedBoxes),
                                    false

                            );

                            orderDao.insert(order2);

                            if (order.getPickedUpFromCupboard()) {

                                orderDao.delete(order);
                            } else {
                                order.setOrderScoutId(-1);
                                order.setOrderedBoxes(order.getOrderedBoxes() - order2.getOrderedBoxes());
                                orderDao.update(order);
                                orderDao.delete(order);
                            }

                            requestedBoxes = 0;
                        }
                    }

                }

            }
        }
    }


    @Override
    protected boolean isEditable() {
        return isEditable;
    }


    @Click(R.id.boxes_radio_button)
    void boxesClick() {

        replaceFrag(
                createFragmentTransaction(
                        scout_pickup_order), CookieAmountsListInputFragment_
                .builder()
                .isBoxes(true)
                .isEditable(this.isEditable)
                .build(),
                scout_pickup_order
        );
    }

    @Click(R.id.cases_radio_button)
    void casesClick() {

        replaceFrag(
                createFragmentTransaction(
                        scout_pickup_order), CookieAmountsListInputFragment_
                .builder()
                .isBoxes(false)
                .isEditable(this.isEditable)
                .build(),
                scout_pickup_order
        );
    }

    @Override
    public HashMap<String, String> getValMap() {

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            final String cookieType = Constants.CookieTypes[i];
            Integer amountExpected = 0;
            List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.OrderScoutId.eq(ScoutId),
                            OrderDao.Properties.OrderCookieType.eq(cookieType))
                    .list();
            if (!orderList.isEmpty()) {
                for (Order order : orderList) {
                    amountExpected += order.getOrderedBoxes();
                }
            }
            if (!((ICookieActionFragment) getSupportFragmentManager().findFragmentByTag(scout_pickup_order)).isRefresh()) {

                valuesMap.put(cookieType, String.valueOf(amountExpected));

            }
        }
        return valuesMap;
    }


    @Override
    protected void saveData() {

        savePickupData();
    }


}
