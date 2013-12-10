package net.kjmaster.cookiemom.cupboard;

import android.widget.RadioGroup;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.ui.CookieAmountsListInputFragment_;
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
public class CupboardOrderActivity extends CookieActionActivity {


    private final HashMap<String, String> valMap = new HashMap<String, String>();
    private String fragName;

    @AfterViews
    void afterViewFrag() {

        fragName = getString(R.string.add_cupboard_order);
        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder().isBoxes(true).isEditable(this.isEditable()).build(),
                fragName);


//        replaceFrag(
//                createFragmentTransaction(fragName),
//                CupboardOrderFragment_.builder().build(),
//                fragName);

        createActionMode(fragName);
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

    @Override
    protected boolean isEditable() {
        return true;
    }

    @Override
    public HashMap<String, String> getValMap() {
        //net.kjmaster.cookiemom.cupboard.CupboardOrderActivity.getValMap returns HashMap<String, String>

        //12/5/13
        for (String cookieType : Constants.CookieTypes) {
            List<Order> orderList = Main.daoSession.getOrderDao().queryBuilder()
                    .where(OrderDao.Properties.OrderedFromCupboard.eq(false),
                            OrderDao.Properties.OrderCookieType.eq(cookieType),
                            OrderDao.Properties.PickedUpFromCupboard.eq(false),
                            OrderDao.Properties.OrderedBoxes.gt(0)).list();
            int totalOrdered = 0;
            if (orderList != null) {
                for (Order order : orderList) {
                    totalOrdered += order.getOrderedBoxes();
                }


            }
            valMap.put(cookieType, String.valueOf(totalOrdered));
        }

        return this.valMap;
    }

    @Click(R.id.boxes_radio_button)
    void boxesClick() {
        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder().isBoxes(true).isEditable(this.isEditable()).build(),
                fragName);

    }

    @Click(R.id.cases_radio_button)
    void casesClick() {
        replaceFrag(
                createFragmentTransaction(fragName),
                CookieAmountsListInputFragment_.builder().isBoxes(false).isEditable(this.isEditable()).build(),
                fragName);

    }

    @Override
    protected void saveData() {
        Calendar c = Calendar.getInstance();

        OrderDao dao = Main.daoSession.getOrderDao();

        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            String cookieFlavor = Constants.CookieTypes[i];
            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));

            List<Order> orders = dao.queryBuilder()
                    .where(
                            OrderDao.Properties.OrderCookieType.eq(cookieFlavor),
                            OrderDao.Properties.OrderedFromCupboard.eq(false))
                    .orderAsc(OrderDao.Properties.OrderDate)
                    .list();

            for (Order order : orders) {
                if ((totalBoxes >= order.getOrderedBoxes())) {
                    order.setOrderedFromCupboard(true);
                    totalBoxes = totalBoxes - order.getOrderedBoxes();
                    dao.update(order);

                } else {
                    Integer newTotal = order.getOrderedBoxes() - totalBoxes;

                    dao.insert(
                            new Order(
                                    null,
                                    order.getOrderDate(),
                                    order.getOrderScoutId(),
                                    cookieFlavor,
                                    true,
                                    totalBoxes, false
                            ));

                    order.setOrderedBoxes(newTotal);

                    dao.update(order);

                    totalBoxes = 0;
                }
            }

            if (totalBoxes > 0) {

                dao.insert(
                        new Order(
                                null,
                                c.getTime(),
                                -1L,
                                cookieFlavor,
                                Boolean.TRUE,
                                totalBoxes, false
                        ));
            }
        }
    }


}










