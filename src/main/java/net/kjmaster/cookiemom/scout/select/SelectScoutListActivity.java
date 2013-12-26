package net.kjmaster.cookiemom.scout.select;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import com.googlecode.androidannotations.annotations.*;
import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.ISendActivityResult;
import net.kmaster.cookiemom.dao.Scout;
import net.kmaster.cookiemom.dao.ScoutDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/31/13
 * Time: 9:51 AM
 */


@EActivity(R.layout.scout_select_scout)
public class SelectScoutListActivity extends ListActivity implements ISendActivityResult {
    //    private DaoMaster daoMaster;
//    private DaoSession daoSession;
//    private Cursor cursor;

    @App
    Main main;

    @Extra
    int requestCode;

    @Extra
    long targetId;

    @AfterViews
    void afterViews() {
        setListAdapter(new ArrayAdapter<Scout>(
                this,
                R.layout.ui_simple_big_text,
                R.id.content,
                getScoutList()
        ));
    }

    private List<Scout> getScoutList() {
        ScoutDao scoutDao = Main.daoSession.getScoutDao();
        return scoutDao.queryBuilder().list();
    }

    @ItemClick()
    public void listItemClicked(int position) {
        Scout scout = (Scout) getListAdapter().getItem(position);
        setResult(
                RESULT_OK,
                getIntent()
                        .putExtra("scout_name", scout.getScoutName())
                        .putExtra("target_id", targetId)
                        .putExtra("scout_id", scout.getId()

                        ));
        finish();


    }

    @Extra
    String FragmentTag;

    @Override
    public void setFragmentTag(String fragmentTag) {
        FragmentTag = fragmentTag;
    }
}