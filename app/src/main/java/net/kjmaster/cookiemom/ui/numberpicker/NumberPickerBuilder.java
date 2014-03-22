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

package net.kjmaster.cookiemom.ui.numberpicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.Vector;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
@SuppressWarnings("ALL")
public class NumberPickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private Integer minNumber;
    private Integer maxNumber;
    private Integer plusMinusVisibility;
    private Integer decimalVisibility;
    private String labelText;
    private int mReference;

    private Vector<NumberPickerDialogFragment.NumberPickerDialogHandler> mNumberPickerDialogHandlers = new Vector<NumberPickerDialogFragment.NumberPickerDialogHandler>();

    /**
     * Attach a FragmentManager. This is required for creation of the Fragment.
     *
     * @param manager the FragmentManager that handles the transaction
     * @return the current Builder object
     */

    public NumberPickerBuilder setFragmentManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    /**
     * Attach a style resource ID for theming. This is required for creation of the Fragment. Two expander styles are
     * provided using R.style.BetterPickersDialogFragment and R.style.BetterPickersDialogFragment.Light
     *
     * @param styleResId the style resource ID to use for theming
     * @return the current Builder object
     */

    public NumberPickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    /**
     * Attach a target Fragment. This is optional and useful if creating a Picker within a Fragment.
     *
     * @param targetFragment the Fragment to attach to
     * @return the current Builder object
     */

    public NumberPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    /**
     * Attach a reference to this Picker instance. This is used to track multiple pickers, if the user wishes.
     *
     * @param reference a user-defined int intended for Picker tracking
     * @return the current Builder object
     */

    public NumberPickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    /**
     * Set a minimum number required
     *
     * @param minNumber the minimum required number
     * @return the current Builder object
     */

    public NumberPickerBuilder setMinNumber(int minNumber) {
        this.minNumber = minNumber;
        return this;
    }

    /**
     * Set a maximum number required
     *
     * @param maxNumber the maximum required number
     * @return the current Builder object
     */

    public NumberPickerBuilder setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        return this;
    }

    /**
     * Set the visibility of the +/- button. This takes an int corresponding to Android's View.VISIBLE, View.INVISIBLE,
     * or View.GONE.  When using View.INVISIBLE, the +/- button will still be present in the layout but be
     * non-clickable. When set to View.GONE, the +/- button will disappear entirely, and the "0" button will occupy its
     * space.
     *
     * @param plusMinusVisibility an int corresponding to View.VISIBLE, View.INVISIBLE, or View.GONE
     * @return the current Builder object
     */

    public NumberPickerBuilder setPlusMinusVisibility(int plusMinusVisibility) {
        this.plusMinusVisibility = plusMinusVisibility;
        return this;
    }

    /**
     * Set the visibility of the decimal button. This takes an int corresponding to Android's View.VISIBLE,
     * View.INVISIBLE, or View.GONE.  When using View.INVISIBLE, the decimal button will still be present in the layout
     * but be non-clickable. When set to View.GONE, the decimal button will disappear entirely, and the "0" button will
     * occupy its space.
     *
     * @param decimalVisibility an int corresponding to View.VISIBLE, View.INVISIBLE, or View.GONE
     * @return the current Builder object
     */

    public NumberPickerBuilder setDecimalVisibility(int decimalVisibility) {
        this.decimalVisibility = decimalVisibility;
        return this;
    }

    /**
     * Set the (optional) text shown as a label. This is useful if wanting to identify data with the number being
     * selected.
     *
     * @param labelText the String text to be shown
     * @return the current Builder object
     */

    public NumberPickerBuilder setLabelText(String labelText) {
        this.labelText = labelText;
        return this;
    }


    /**
     * Attach universal objects as additional handlers for notification when the Picker is set. For most use cases, this
     * method is not necessary as attachment to an Activity or Fragment is done automatically.  If, however, you would
     * like additional objects to subscribe to this Picker being set, attach Handlers here.
     *
     * @param handler an Object implementing the appropriate Picker Handler
     * @return the current Builder object
     */

    public NumberPickerBuilder addNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandler handler) {
        this.mNumberPickerDialogHandlers.add(handler);
        return this;
    }

    /**
     * Remove objects previously added as handlers.
     *
     * @param handler the Object to remove
     * @return the current Builder object
     */

    public NumberPickerBuilder removeNumberPickerDialogHandler(NumberPickerDialogFragment.NumberPickerDialogHandler handler) {
        this.mNumberPickerDialogHandlers.remove(handler);
        return this;
    }

    /**
     * Instantiate and show the Picker
     */
    public void show() {
        if (manager == null || styleResId == null) {
            Log.e("NumberPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
            return;
        }
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final NumberPickerDialogFragment fragment = NumberPickerDialogFragment
                .newInstance(mReference, styleResId, minNumber, maxNumber, plusMinusVisibility, decimalVisibility,
                        labelText);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.setNumberPickerDialogHandlers(mNumberPickerDialogHandlers);
        fragment.show(ft, "number_dialog");
    }
}
