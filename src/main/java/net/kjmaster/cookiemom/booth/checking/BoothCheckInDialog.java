package net.kjmaster.cookiemom.booth.checking;

import android.support.v4.app.FragmentActivity;

import net.kjmaster.cookiemom.dao.Booth;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;


@SuppressWarnings("MethodNameSameAsClassName")
public class BoothCheckInDialog {
    public BoothCheckInDialog() {
    }

    public void BoothCheckInDialog(Booth booth, FragmentActivity activity) {
        SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
                .setTitle("Turn in for" + booth.getBoothLocation())
                .setMessage("Is she turning in money or cookies?")
                .setPositiveButtonText("Money")
                .setNegativeButtonText("Cookies")
                .setCancelable(true)
                .setRequestCode(booth.getId().intValue())
                .setTag(booth.getId().toString())

                .show();
    }
}