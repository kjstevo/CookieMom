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
import net.kmaster.cookiemom.dao.BoothDao;
import org.jetbrains.annotations.NotNull;

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
        super(context, R.layout.booth_list_fragment);
        this.scoutId = scoutId;
    }

    @Override
    public void setupInnerViewElements(@NotNull ViewGroup parent, View view) {
        //net.kjm
        final Context context = getContext();
        final BoothDao boothDao = Main.daoSession.getBoothDao();
        final BoothAssignmentsDao assignmentsDao = Main.daoSession.getBoothAssignmentsDao();
        final ListView cardListView = (ListView) parent.findViewById(R.id.booth_listview);

        if (
                cardListView != null
                        && assignmentsDao != null
                        && boothDao != null
                ) {

            List<BoothAssignments> boothAssignments =
                    assignmentsDao.queryBuilder()
                            .where(
                                    BoothAssignmentsDao.Properties.BoothAssignScoutId.eq(scoutId))
                            .list();

            if (!boothAssignments.isEmpty()) {

                BoothTextAdapterListItem mData = new BoothTextAdapterListItem(context);

                for (BoothAssignments assignment : boothAssignments) {

                    Booth booth = boothDao.load(assignment.getBoothAssignBoothId());

                    if (booth != null) {

                        mData.add(booth);

                    }

                }

                cardListView.setAdapter(mData);

            }

        }

    }

}

