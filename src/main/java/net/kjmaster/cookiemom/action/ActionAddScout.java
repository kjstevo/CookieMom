package net.kjmaster.cookiemom.action;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/14/13
 * Time: 6:59 PM
 */

public class ActionAddScout extends ActionContentCard {
    private final Context mActivity;

    public ActionAddScout(Context mActivity) {
        super(mActivity);
        this.mActivity = mActivity;


    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);    //To change body of overridden methods use File | Settings | File Templates.

        final ListView listView = (ListView) parent.findViewById(R.id.action_list);
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    net.kjmaster.cookiemom.scout.AddScoutActivity_.intent(mActivity).startForResult(Constants.SCOUT_REQUEST);

                }
            });
        }
    }


    @Override
    public Boolean isCardVisible() {
        long cnt = Main.daoSession.getScoutDao().count();
        return cnt <= 0;
    }

    @Override
    public List<String> getActionList() {
        List<String> stringList = new ArrayList<String>();
        stringList.add("Add Scout");
        return stringList;
    }

    @Override
    public String getActionTitle() {
        return "No scouts currently in database!";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHeaderText() {
        return "Scouts";
    }


}
