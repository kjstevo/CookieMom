package net.kjmaster.cookiemom.summary.stat.totals;


import android.support.v4.app.Fragment;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import it.gmariotti.cardslib.library.view.CardView;
import net.kjmaster.cookiemom.R;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/25/13
 * Time: 10:58 PM
 */

@EFragment(R.layout.summary_stat_totals_fragment)
public class SummaryStatTotalsFragment extends Fragment {

    @ViewById(R.id.summary_stat_totals_cardview)
    CardView cardView;


    @AfterViews
    void afterViews() {
        SummaryStatTotalsCard mCard = new SummaryStatTotalsCard(getActivity());
        cardView.setCard(mCard);
    }
}