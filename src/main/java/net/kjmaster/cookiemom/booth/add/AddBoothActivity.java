package net.kjmaster.cookiemom.booth.add;

//~--- non-JDK imports --------------------------------------------------------

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentByTag;
import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.Constants;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 10/28/13
 * Time: 1:26 AM
 */
@SuppressLint("Registered")
@EActivity(R.layout.booth_add_booth_layout)
public class AddBoothActivity extends FragmentActivity implements ActionMode.Callback, ISimpleDialogListener {

    @FragmentByTag("add_booth")
    AddBoothDialogFragment addBoothDialogFragment;


    @AfterViews
    void afterViews() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("add_booth");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.replace(R.id.content, AddBoothDialogFragment_.builder().build(), "add_booth");

        ActionMode actionMode = startActionMode(this);

        actionMode.setTitle("Add Booth");

        int doneButtonId = Resources.getSystem().getIdentifier("action_mode_close_button", "id", "android");

        View doneButton = this.findViewById(doneButtonId);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveScoutData();
                finish();
            }
        });

        ft.commit();
    }

    @Override
    public boolean onCreateActionMode(@NotNull ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();

        inflater.inflate(R.menu.add_scout, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;    // To change body of implemented methods use File | Settings | File Templates.
    }

    private void saveScoutData() {
        AddBoothDialogFragment boothDialogFragment = (AddBoothDialogFragment) getSupportFragmentManager().findFragmentByTag(
                "add_booth");
        String boothLocation;
        String boothAddress;
        String boothDate;
        try {
            boothLocation = boothDialogFragment.editText.getText().toString();
            boothAddress = boothDialogFragment.addressText.getText().toString();
            boothDate = boothDialogFragment.hiddenDateTime.getText().toString();
            setResult(Constants.ADD_BOOTH_RESULT_OK, getIntent()
                    .putExtra("add_booth", boothLocation)
                    .putExtra("address", boothAddress)
                    .putExtra("booth_date", Long.valueOf(boothDate))
            );
        } catch (Exception e) {
            Log.e(getApplicationInfo().name, "error in net.kjmaster.cookiemom.booth.add.AddBoothActivity.saveScoutData");

        }

    }


    @Override
    public boolean onActionItemClicked(@NotNull ActionMode mode, MenuItem item) {

        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        finish();
    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {

    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
