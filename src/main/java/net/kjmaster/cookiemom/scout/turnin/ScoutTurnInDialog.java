package net.kjmaster.cookiemom.scout.turnin;

import android.support.v4.app.FragmentActivity;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import net.kmaster.cookiemom.dao.Scout;

@SuppressWarnings("MethodNameSameAsClassName")
public class ScoutTurnInDialog {
    public ScoutTurnInDialog() {
    }

    public void ScoutTurnInDialog(Scout scout, FragmentActivity activity) {
        SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
                .setTitle("Turn in for" + scout.getScoutName())
                .setMessage("Is she turning in money or cookies?")
                .setPositiveButtonText("Money")
                .setNegativeButtonText("Cookies")
                .setCancelable(true)
                .setRequestCode(scout.getId().intValue())
                .setTag(scout.getId().toString())
                .show();
    }
}