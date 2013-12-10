package net.kjmaster.cookiemom.scout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import net.kmaster.cookiemom.dao.Scout;

@SuppressWarnings("MethodNameSameAsClassName")
public class ScoutPickupDialog {
    public ScoutPickupDialog() {
    }

    public void ScoutPickupDialog(Scout scout, FragmentActivity activity, Fragment fragment) {
        SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
                .setTitle("Pickup for" + scout.getScoutName())
                .setMessage("Is she picking up now or are you just preparing?")
                .setPositiveButtonText("Pickup Now")
                .setNegativeButtonText("Just Preparing")
                .setCancelable(true)
                .setTargetFragment(fragment, scout.getId().intValue())
                .setRequestCode(scout.getId().intValue())
                .setTag(scout.getId().toString())
                .show();
    }
}