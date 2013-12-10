package net.kjmaster.cookiemom.booth;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import it.gmariotti.cardslib.library.internal.Card;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kmaster.cookiemom.dao.Booth;
import net.kmaster.cookiemom.dao.BoothAssignments;
import net.kmaster.cookiemom.dao.BoothAssignmentsDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/1/13
 * Time: 10:41 PM
 */
public class BoothListFragmentCard extends Card {
    private final long scoutId;

    public BoothListFragmentCard(Context context, long scoutId) {
        this(context, R.layout.booth_list_fragment, scoutId);
    }

    @SuppressWarnings("WeakerAccess")
    public BoothListFragmentCard(Context context, @SuppressWarnings("SameParameterValue") int innerLayout, long scoutId) {
        super(context, innerLayout);
        this.scoutId = scoutId;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //net.kjm
        Context context = getContext();
        ListView cardListView = (ListView) parent.findViewById(R.id.booth_listview);
        if (cardListView != null) {
            BoothAssignmentsDao boothAssignmentsDao = Main.daoSession.getBoothAssignmentsDao();

            List<BoothAssignments> boothAssignments = boothAssignmentsDao.queryBuilder()
                    .where(
                            BoothAssignmentsDao.Properties.BoothAssignScoutId.eq(scoutId)
                    )
                    .list();
            if (!boothAssignments.isEmpty()) {
                BoothTextAdapterListItem mData = new BoothTextAdapterListItem(context, R.layout.simple_big_text);
                for (BoothAssignments assignment : boothAssignments) {
                    Booth booth = Main.daoSession.getBoothDao().load(assignment.getBoothAssignBoothId());
                    if (booth != null) {
                        mData.add(booth);
                    }
                }


                cardListView.setAdapter(mData);

            }
        }
        //12/2/13

    }
}
