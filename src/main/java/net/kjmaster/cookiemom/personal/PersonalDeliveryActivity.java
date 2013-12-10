package net.kjmaster.cookiemom.personal;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.*;

import java.util.Calendar;
import java.util.List;

import static net.kjmaster.cookiemom.global.Constants.CookieTypes;
import static net.kmaster.cookiemom.dao.PersonalOrdersDao.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class PersonalDeliveryActivity extends CookieActionActivity  {

    @Extra
    int personalCustomer;

    @Extra
    boolean isEditable;

    @StringRes(R.string.personal_deliver_order_title)
    String personal_deliver_order_title;

    @StringRes(R.string.personal_deliver_order)
    String personal_deliver_order;

    @StringRes(R.string.cancel)
    String resCancel;
    private PersonalOrdersDao dao;
    private PersonalOrders personalOrders;

    @AfterViews
    void afterViewFrag() {

        dao = Main.daoSession.getPersonalOrdersDao();

        personalOrders = dao.load((long) personalCustomer);
        if (personalOrders != null) {

        }
        String customer = personalOrders.getPersonalCustomer();
        replaceFrag(createFragmentTransaction(personal_deliver_order),
                PersonalDeliveryFragment_.builder()
                        .personalCustomer(customer)
                        .isEditable(isEditable)
                        .build(),
                personal_deliver_order
        );
       createActionMode(personal_deliver_order_title);

    }



    private void savePickupData() {
        Calendar c = Calendar.getInstance();

        PersonalOrdersDao dao = this.dao;



        for (String cookieFlavor : CookieTypes) {

            Integer totalBoxes = Integer.valueOf(getFragment().valuesMap().get(cookieFlavor));

            List<PersonalOrders> personalOrdersList=dao.queryBuilder()
                    .where(
                            Properties.PersonalCustomer.eq(personalOrders.getPersonalCustomer()),
                           Properties.PertsonalOrderId.lt(0)
                        )
                    .list();

//           dao.insert(cookieTransactions);
//
//         List<PersonalOrders> orderList =
  //                  orderDao.queryBuilder().where(
//                            Properties.OrderCookieType.eq(cookieFlavor),
//                            Properties.OrderScoutId.eq(ScoutId),
//                            Properties.PickedUpFromCupboard.eq(true)
//                    ).orderAsc(
//                            Properties.OrderDate
//                    )
//                            .list();

            for (PersonalOrders order : personalOrdersList) {
                int orderedBoxes=0;
                if(order.getPersonalTransId()>-1){
                    orderedBoxes=order.getCookieTransactions().getTransBoxes();
                }else{
                    orderedBoxes=order.getOrder().getOrderedBoxes();

                }
                if (totalBoxes >=orderedBoxes){
                    totalBoxes = totalBoxes - orderedBoxes;
                    order
                   dao.update(order);
                } else {
                    if (totalBoxes>0) {

                        if (totalBoxes > 0) {
                            PersonalOrders newPersonalOrders=new PersonalOrders(
                                    null,
                                            order.getPersonalDate(),
                                            order.getPersonalCustomer(),
                                            order.getPersonalContact(),
                                            false,
                                            order.getPersonalPaid(),
                                            order.getPersonalTransId(),
                                            order.getPertsonalOrderId()
                            );
                            dao.insert(newPersonalOrders);
                            if(order.getPersonalTransId()>-1){
                                order.getCookieTransactions().set
                            }
                            order.setPersonalBoxes(totalBoxes);
                            order.setPersonalPickedUp(true);
                            dao.update(order);
                         }

                            totalBoxes = 0;
                    }
                }
            }


        }
    }



    @Override
    protected void saveData() {
        savePickupData();
    }





}
