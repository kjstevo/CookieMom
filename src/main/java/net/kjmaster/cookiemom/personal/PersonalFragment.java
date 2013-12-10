package net.kjmaster.cookiemom.personal;

//~--- non-JDK imports --------------------------------------------------------

import android.app.Activity;
import android.content.Intent;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.MenuItem;

import android.widget.EditText;

import com.googlecode.androidannotations.annotations.*;

import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.MainActivity;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.IActivityResult;

import net.kjmaster.cookiemom.global.ISettings_;
import net.kjmaster.cookiemom.scout.SelectScoutListActivity_;
import net.kmaster.cookiemom.dao.*;

//~--- JDK imports ------------------------------------------------------------

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@OptionsMenu(R.menu.personal_frag)
@EFragment(R.layout.fragment_sample)
public class PersonalFragment extends Fragment implements IActivityResult, ISimpleDialogListener {
    @App
    Main             main;
    private EditText editText;

//  private DaoMaster daoMaster;
//
//  private DaoSession daoSession;
//  private NoteDao noteDao;
//
//  private Cursor cursor;
    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;
    private List<Card> mData;
    private int          delivBoxSum    = 0;
    private int          orderBoxSum    = 0;
    private boolean      readyForPickup = false;
    private double       CashSum        = 0;
    private String       lastCustomer   = "";
    private String selectedCustomer;
    private long scoutId;

    @AfterViews
    void afterViews() {

//      Bundle bundle = getArguments();
//      String label = bundle.getString("label");
//       DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "scouts-db", null);
//      db = helper.getWritableDatabase();
//      daoMaster = new DaoMaster(db);
//      daoSession=daoMaster.newSession();
        PersonalOrdersDao personalOrdersDao = Main.daoSession.getPersonalOrdersDao();
        List              personalOrderses  =
            personalOrdersDao.queryBuilder().orderAsc(PersonalOrdersDao.Properties.PersonalCustomer).list();

        if (!personalOrderses.isEmpty()) {
            mData = new ArrayList<Card>();
                      delivBoxSum    = 0;
                orderBoxSum    = 0;
                readyForPickup = false;
                CashSum        = 0;
                lastCustomer   = "";

            for (int i = 0; i < personalOrderses.size(); i++) {
                final PersonalOrders personalOrders = (PersonalOrders) personalOrderses.get(i);

                if (personalOrders.getPersonalCustomer().equals(lastCustomer) || lastCustomer.equals("")) {
                    if (personalOrders.getPersonalTransId()>-1) {
                        delivBoxSum = delivBoxSum + personalOrders.getPersonalBoxes();
                        CashSum = CashSum + personalOrders.getCookieTransactions().getTransCash();
                    } else {
                             orderBoxSum = orderBoxSum + personalOrders.getPersonalBoxes();
                        if(personalOrders.getPertsonalOrderId()<0) {
                                        readyForPickup=true;
                        }
                    }
                } else {
                    addCard(personalOrders,delivBoxSum,orderBoxSum,CashSum,readyForPickup);
                     createNewSection(personalOrders);
                      }

               lastCustomer=personalOrders.getPersonalCustomer();
              if (i+1 == personalOrderses.size()) {
                   addCard(personalOrders, delivBoxSum, orderBoxSum, CashSum,readyForPickup);

                }

            }
            CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
            cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
            cardView.setAdapter(cardArrayAdapter);
        }
    }

    private void createNewSection(PersonalOrders personalOrders) {
         readyForPickup=false;

        if(personalOrders.getPersonalBoxes()<0){


           delivBoxSum    = personalOrders.getPersonalBoxes()*-1;
            if(personalOrders.getPersonalTransId()>-1){
                CashSum        = personalOrders.getCookieTransactions().getTransCash();
            }
            orderBoxSum=0;
        } else {

            delivBoxSum=0;
           orderBoxSum    = personalOrders.getOrder().getOrderedBoxes();
          if(personalOrders.getPertsonalOrderId()<0){
              readyForPickup=true;
            }

        }

}





    private void addCard(final PersonalOrders personalOrders, int delivBoxSum, int orderBoxSum,  double cashPaid,
                         boolean readyForPickup) {
        PersonalCard card=new PersonalCard(getActivity());
        card.setReadyForPickup(readyForPickup);
        card.setAltCount(String.valueOf(delivBoxSum));
        card.setTitle(String.valueOf(orderBoxSum));
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        String owed = fmt.format( (delivBoxSum*4)-cashPaid);
        String paid = fmt.format(cashPaid);
        card.setPaidText(paid);
        card.setOwedText(owed);
        double totalDue=(delivBoxSum*4);
        card.setTotalText(fmt.format(totalDue));
        CardHeader cardHeader = new CardHeader(getActivity());

        cardHeader.setTitle(lastCustomer);

        cardHeader.setButtonExpandVisible(true);

        cardHeader.setButtonOverflowVisible(true);
        card.addCardHeader(cardHeader);

        cardHeader.setPopupMenu(R.menu.personal_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                selectPersonalMenu(item,personalOrders);
            }
        });
        createExpander(mData,lastCustomer,card);
    }
    @Pref()
    ISettings_ iSettings;

    @OptionsItem(R.id.menu_eat_cookies)
    void eatCookies(){
       PersonalOrderActivity_.intent(getActivity()).isEating(true).startForResult(Constants.PERSONAL_REQUEST);
    }



    @OptionsItem(R.id.menu_add_personal)
    void addPersonal(){
          PersonalOrderActivity_ .intent(getActivity()).requestCode(Constants.ADD_PERSONAL_ORDER).startForResult(Constants.ADD_PERSONAL_ORDER);
    }
    private void createExpander(List<Card> mData, String customer, PersonalCard mCard) {
        PersonalExpander personalExpander = new PersonalExpander(getActivity(),customer);
        personalExpander.setTitle("Transactions");
        mCard.addCardExpand(personalExpander);
        mData.add(mCard);
    }

    private void selectPersonalMenu( MenuItem item, PersonalOrders personalOrders) {

    String customer=personalOrders.getPersonalCustomer();
     if (item.getItemId()==R.id.menu_personal_delete){
         lastCustomer=personalOrders.getPersonalCustomer();
         SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                          .setTitle("WARNING!")
                          .setMessage("Are you sure you want to delete this order? This action can not be undone!")
                          .setPositiveButtonText("Delete Order")
                          .setNegativeButtonText("Cancel")
                          .setCancelable(true)
                          .setTargetFragment(this, Constants.REQUEST_CODE_DELETE)
                          .setRequestCode(Constants.REQUEST_CODE_DELETE)
                         .setTag(customer)
                          .show();
              }
        if (item.getItemId() == R.id.menu_personal_delivered) {
            PersonalDeliveryDialog scoutPickupDialog = new PersonalDeliveryDialog();
            scoutPickupDialog.PersonalDeliveryDialog(personalOrders, getActivity(), this);
            this.selectedCustomer=customer;
        }

        if(item.getItemId()==R.id.menu_personal_collect){
           PersonalTurnInActivity_.intent(getActivity()).customerName(customer).startForResult(Constants.PERSONAL_REQUEST);
        }


    }





//      SelectScoutListActivity_.intent(getActivity()).requestCode(Constants.ASSIGN_SCOUT_REQUEST_CODE).targetId(booth.getId()).startForResult(Constants.ASSIGN_SCOUT_REQUEST_CODE);
//      }
//      if(item.getItemId()==R.id.menu_booth_delete){
//
//                  SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
//                          .setTitle("WARNING!")
//                          .setMessage("Are you sure you want to delete this booth? This action can not be undone!")
//                          .setPositiveButtonText("Delete Booth")
//                          .setNegativeButtonText("Cancel")
//                          .setCancelable(true)
//                          .setTargetFragment(this,booth.getId().intValue())
//                          .setRequestCode(booth.getId().intValue())
//                         .setTag(booth.getId().toString())
//                          .show();
//              }




    @Override
    public void onFragmentRecieveResults(int requestCose, int resultCode, Intent data) {}

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode<0){
            PersonalDeliveryActivity_.intent(getActivity()).isEditable(false).personalCustomer(requestCode*-1).startForResult(Constants.PERSONAL_REQUEST);

        } else {

                    PersonalOrders personalOrders=Main.daoSession.getPersonalOrdersDao().load(Long.valueOf(requestCode*-1));
                    if(personalOrders!=null){
                        if(personalOrders.getOrder()!=null){
                            Main.daoSession.getOrderDao().delete(personalOrders.getOrder());
                        }
                        Main.daoSession.getPersonalOrdersDao().delete(personalOrders);

                    }

                }
            //Main.daoSession.getPersonalOrdersDao().queryBuilder().where(PersonalOrdersDao.Properties.PersonalCustomer.eq(this.selectedCustomer)).list();

                List<PersonalOrders>list=Main.daoSession.getPersonalOrdersDao().queryBuilder().where(PersonalOrdersDao.Properties.PersonalCustomer.eq(lastCustomer)).list();
                Main.daoSession.getPersonalOrdersDao().deleteInTx(list);

        }




//      BoothDao boothDao=main.daoSession.getBoothDao();
//          boothDao.delete(boothDao.loadByRowId(Long.valueOf(requestCode)));
//
//       afterViews();


    @Override
    public void onNegativeButtonClicked(int requestCode) {

//      To change body of implemented methods use File | Settings | File Templates.
    }
    @OnActivityResult(Constants.PERSONAL_REQUEST)
    void personalResponse(int resultCode){
        ((MainActivity)getActivity()).refreshAll();
    }

}


//~ Formatted by Jindent --- http://www.jindent.com
