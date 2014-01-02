package net.kjmaster.cookiemom.summary.stat;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/25/13
 * Time: 5:42 PM
 */

import android.support.v4.app.Fragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import it.gmariotti.cardslib.library.view.CardView;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.summary.stat.booth.SummaryStatBoothCard;
import net.kjmaster.cookiemom.summary.stat.scout.SummaryStatScoutCard;
import net.kjmaster.cookiemom.summary.stat.totals.SummaryStatTotalsCard;

@EFragment(R.layout.summary_stat_fragment)
public class SummaryStatFragment extends Fragment {

    @ViewById(R.id.summary_stat_totals_cardview)
    CardView totalsCardView;

    @ViewById(R.id.summary_stat_scout_cardview)
    CardView scoutCardView;

    @ViewById(R.id.summary_stat_booth_cardview)
    CardView boothCardView;

    @AfterViews
    void afterViews() {
        SummaryStatTotalsCard statTotalsCard = new SummaryStatTotalsCard(getActivity());
        SummaryStatScoutCard statScoutCard = new SummaryStatScoutCard(getActivity());
        SummaryStatBoothCard statBoothCard = new SummaryStatBoothCard(getActivity());
        scoutCardView.setCard(statScoutCard);
        totalsCardView.setCard(statTotalsCard);
        boothCardView.setCard(statBoothCard);


    }

}
