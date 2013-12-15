package net.kjmaster.cookiemom.scout;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.res.StringRes;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import net.kjmaster.cookiemom.global.CookieActionActivity;
import net.kjmaster.cookiemom.ui.CookieAmountsListInputFragment_;
import net.kmaster.cookiemom.dao.Order;
import net.kmaster.cookiemom.dao.OrderDao;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/4/13
 * Time: 2:16 PM
 */
@EActivity(R.layout.scout_order_layout)
public class ScoutOrderActivity extends CookieActionActivity {

    @Extra
    long scoutId;

    @Extra
    int requestCode;

    @StringRes(R.string.add_order)
    String fragTag;

    @StringRes(R.string.add_order_title)
    String fragTitle;

    @AfterViews
    void afterViewFrag() {
        replaceFrag(createFragmentTransaction(fragTag), CookieAmountsListInputFragment_.builder().isEditable(true).isBoxes(true).build(), fragTag);
        createActionMode(fragTitle);
    }

    @Override
    protected boolean isEditable() {
        return true;
    }


    protected void saveData() {
        OrderDao dao = Main.daoSession.getOrderDao();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < Constants.CookieTypes.length; i++) {
            Integer intVal = Integer.valueOf(getFragment()
                    .valuesMap()
                    .get(Constants.CookieTypes[i])
            );

            if (intVal > 0) {
                dao.insert(new Order(
                        null,
                        c.getTime(),
                        scoutId,
                        Constants.CookieTypes[i],
                        false,
                        intVal,
                        false));
            }
        }
    }
}