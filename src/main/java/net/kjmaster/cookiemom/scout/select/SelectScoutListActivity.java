/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.kjmaster.cookiemom.scout.select;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.widget.ArrayAdapter;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ItemClick;

import net.kjmaster.cookiemom.Main;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Scout;
import net.kjmaster.cookiemom.dao.ScoutDao;
import net.kjmaster.cookiemom.global.ISendActivityResult;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/31/13
 * Time: 9:51 AM
 */


@SuppressLint("Registered")
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