

package net.kjmaster.cookiemom.booth;

import android.support.v4.app.Fragment;
import android.view.MenuItem;
import com.googlecode.androidannotations.annotations.*;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.booth.expander.CustomBoothExpander;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.scout.SelectScoutListActivity_;
import net.kmaster.cookiemom.dao.Booth;
import net.kmaster.cookiemom.dao.BoothAssignments;
import net.kmaster.cookiemom.dao.BoothAssignmentsDao;
import net.kmaster.cookiemom.dao.BoothDao;

import java.util.ArrayList;
import java.util.List;

@OptionsMenu(R.menu.booth_frag)
@EFragment(R.layout.fragment_sample)
public class BoothFragment
        extends Fragment implements ISimpleDialogListener {


//    private DaoMaster daoMaster;
//
//    private DaoSession daoSession;
//    private NoteDao noteDao;
//
//    private Cursor cursor;

    @ViewById(R.id.carddemo_list_base1)
    CardListView cardView;


    @AfterViews
    void afterViews() {
//        Bundle bundle = getArguments();
//        String label = bundle.getString("label");

//       DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getActivity(), "scouts-db", null);
//        db = helper.getWritableDatabase();
//        daoMaster = new DaoMaster(db);
//        daoSession=daoMaster.newSession();
        BoothDao boothDao = Main.daoSession.getBoothDao();

        List booths = boothDao.queryBuilder()
                .list();
        if (!booths.isEmpty()) {
            List<Card> mData = new ArrayList<Card>();
            for (Object booth1 : booths) {
                final Booth booth = (Booth) booth1;
                BoothContentCard mCard = new BoothContentCard(getActivity(), booth);


                CardHeader cardHeader = new CardHeader(getActivity());
                cardHeader.setButtonOverflowVisible(true);
                cardHeader.setPopupMenu(R.menu.booth_overflow, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                    @Override
                    public void onMenuItemClick(BaseCard card, MenuItem item) {
                        selectBoothMenu(card, item);
                    }
                });


                mCard.addCardHeader(cardHeader);
                cardHeader.setTitle(booth.getBoothLocation());
                mCard.setTitle("More Info");
                CustomBoothExpander customExpander = new CustomBoothExpander(getActivity(), booth);
                mCard.addCardExpand(customExpander);
                mData.add(mCard);
            }

            CardArrayAdapter cardArrayAdapter = new CardArrayAdapter(getActivity(), mData);
            cardArrayAdapter.setRowLayoutId(R.layout.scout_card_layout);
            cardView.setAdapter(cardArrayAdapter);
        }


    }

    private void selectBoothMenu(BaseCard card, MenuItem item) {
        Booth booth = ((BoothContentCard) card.getParentCard()).getBooth();
        switch (item.getItemId()) {
            case R.id.menu_assign_scout:
                SelectScoutListActivity_.intent(getActivity()).requestCode(Constants.ASSIGN_SCOUT_REQUEST_CODE).targetId(booth.getId()).startForResult(Constants.ASSIGN_SCOUT_REQUEST_CODE);
                break;
            case R.id.menu_booth_delete:
                SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                        .setTitle("WARNING!")
                        .setMessage("Are you sure you want to delete this booth? This action can not be undone!")
                        .setPositiveButtonText("Delete Booth")
                        .setNegativeButtonText("Cancel")
                        .setCancelable(true)
                        .setTargetFragment(this, booth.getId().intValue())
                        .setRequestCode(booth.getId().intValue())
                        .setTag(booth.getId().toString())
                        .show();
                break;
            case R.id.menu_booth_checkout:
                BoothCheckOutActivity_.intent(getActivity()).BoothId(booth.getId()).start();

                break;
            case R.id.menu_booth_checkin:
                break;
            default:
        }
    }


    @OptionsItem(R.id.menu_add_booth)
    void addBooth() {
        AddBoothActivity_.intent(getActivity()).startForResult(Constants.ADD_BOOTH_REQUEST_CODE);
    }


    @Override
    public void onPositiveButtonClicked(int requestCode) {
        BoothDao boothDao = Main.daoSession.getBoothDao();
        List<BoothAssignments> list = Main.daoSession.getBoothAssignmentsDao().queryBuilder()
                .where(
                        BoothAssignmentsDao.Properties.BoothAssignBoothId.eq((long) requestCode))
                .list();
        if (list != null) {
            for (BoothAssignments assignments : list) {
                Main.daoSession.getBoothAssignmentsDao().delete(assignments);
            }
        }
        boothDao.delete(boothDao.loadByRowId((long) requestCode));

        afterViews();

    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }
}



