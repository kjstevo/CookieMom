package net.kjmaster.cookiemom.personal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import net.kmaster.cookiemom.dao.PersonalOrders;

public class PersonalDeliveryDialog {
    public PersonalDeliveryDialog() {
    }

    public void PersonalDeliveryDialog(PersonalOrders personalOrders, FragmentActivity activity, Fragment fragment) {
        SimpleDialogFragment.createBuilder(activity, activity.getSupportFragmentManager())
                .setTitle("Delivery for" + personalOrders.getPersonalCustomer())
                .setMessage("Are you delivering now or are you just preparing?")
                .setPositiveButtonText("Deliver Now")
                .setNegativeButtonText("Just Preparing")
                .setCancelable(true)
                .setTargetFragment(fragment, personalOrders.getId().intValue() * -1)
                .setRequestCode(personalOrders.getId().intValue()*-1)
                .setTag(personalOrders.getId().toString())
                .show();
    }
}