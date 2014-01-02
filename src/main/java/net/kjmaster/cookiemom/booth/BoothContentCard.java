package net.kjmaster.cookiemom.booth;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.*;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/1/13
 * Time: 1:28 PM
 */

public class BoothContentCard extends Card {

    private void init() {

        // No Header
        addPartialOnClickListener(CLICK_LISTENER_CONTENT_VIEW, new OnCardClickListener() {
            @Override
            public void onClick(@NotNull Card card, View view) {
                View view1 = card.getCardView().findViewById(R.id.card_content_expand_layout);
                if (view1 != null) {


                    if (card.isExpanded()) {
                        card.getCardView().getOnExpandListAnimatorListener().onCollapseStart(card.getCardView(), view1);
                    } else {
                        card.getCardView().getOnExpandListAnimatorListener().onExpandStart(card.getCardView(), view1);
                    }
                }
            }
        });
    }

    public Booth getBooth() {
        return mBooth;
    }


    public BoothContentCard(Context context, Booth booth) {
        super(context, R.layout.booth_content);
        mBooth = booth;
        init();
    }

    private Booth mBooth;

    @Override
    public void setupInnerViewElements(@NotNull ViewGroup parent, View view) {

        //Retrieve elements
        TextView mDate = (TextView) parent.findViewById(R.id.booth_date);
        TextView mTime = (TextView) parent.findViewById(R.id.booth_time);
        TextView mAddress = (TextView) parent.findViewById(R.id.booth_address);
        ListView mScoutList = (ListView) parent.findViewById(R.id.booth_scout_list);
        TextView mSold = (TextView) parent.findViewById(R.id.booth_owed);
        TextView mCash = (TextView) parent.findViewById(R.id.booth_recieved);

        if (mDate != null) {
            try {
                String date = new SimpleDateFormat("EEE, MMM d").format(mBooth.getBoothDate());
                if (date != null) {
                    mDate.setText(date);
                }
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }

        }
        if (mTime != null) {
            try {
                String time = DateFormat.getTimeFormat(getContext()).format(mBooth.getBoothDate());

                //String time=new SimpleDateFormat("HH:mm PM").format(mBooth.getBoothDate());
                if (time != null) {
                    mTime.setText(time);
                }
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }
        }

        if (mSold != null) {
            try {
                List<CookieTransactions> list = Main.daoSession.getCookieTransactionsDao().queryBuilder()
                        .where(
                                CookieTransactionsDao.Properties.TransBoothId.eq(mBooth.getId()))
                        .list();
                if (list != null) {
                    Integer totalBoxes = 0;
                    Double totalCash = 0.0;
                    for (CookieTransactions cookieTransactions : list) {
                        totalBoxes += cookieTransactions.getTransBoxes();
                        totalCash += cookieTransactions.getTransCash();
                    }
                    mSold.setText(NumberFormat.getCurrencyInstance().format(totalCash
                    ));
                    mCash.setText(NumberFormat.getCurrencyInstance().format(totalBoxes * -4));

                }


                //String time=new SimpleDateFormat("HH:mm PM").format(mBooth.getBoothDate());


            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }
        }

        if (mAddress != null) {
            try {
                String address = mBooth.getBoothAddress();
                if (address != null) {
                    mAddress.setText(address);
                }
            } catch (Exception e) {

                Log.w("cookiemom", "Missing field info");
            }
        }

        if (mScoutList != null) {
            try {
                mScoutList.setAdapter(new ArrayAdapter<Scout>(
                        getContext(),
                        R.layout.ui_simple_text,
                        R.id.content,
                        getScoutList()
                ));
            } catch (Exception e) {
                Log.w("cookiemom", "Missing field info");
            }

        }
    }

    @NotNull
    private List<Scout> getScoutList() {
        List<Scout> arrayList = new ArrayList<Scout>();
        List<BoothAssignments> boothAssignmentses = Main.daoSession.getBoothAssignmentsDao().queryBuilder().where(BoothAssignmentsDao.Properties.BoothAssignBoothId.eq(mBooth.getId())).list();
        ScoutDao scoutDao = Main.daoSession.getScoutDao();
        if (boothAssignmentses != null) {
            for (BoothAssignments boothAssignments : boothAssignmentses) {
                Scout scout = scoutDao.load(boothAssignments.getBoothAssignScoutId());
                arrayList.add(scout);
            }
        }


        return arrayList;
    }

}

