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

package net.kjmaster.cookiemom.scout.add;


import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.ICookieActionFragment;

import java.util.HashMap;


@EFragment(R.layout.scout_add_customer_dialog)
public class AddCustomerDialogFragment extends DialogFragment implements ICookieActionFragment {
    @ViewById(R.id.editText)
    EditText editText;

    @ViewById(R.id.edit_contact)
    EditText editContact;


    @Override
    public HashMap<String, String> valuesMap() {
        HashMap<String, String> valueMap = new HashMap<String, String>();
        valueMap.put(getString(R.string.add_scout), editText.getText().toString());
        valueMap.put(getString(R.string.contact_info), editContact.getText().toString());
        return valueMap;
    }


    @Override
    public void refreshView() {

    }

    @Override
    public boolean isRefresh() {
        return false;
    }

//  @Click(R.id.btn_ok)
//   void onOKClick()   {
//
//      if (editText != null) {
//          Intent intent= getActivity().getIntent().putExtra("add_scout",editText.getText());
//
//          getActivity().setResult(Constants.ADD_SCOUT_RESULT_OK,intent);
//      }
//
//  }


}

