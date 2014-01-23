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

package net.kjmaster.cookiemom.action;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.IAction;

import it.gmariotti.cardslib.library.internal.Card;


/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 11/1/13
 * Time: 1:28 PM
 */

public abstract class ActionContentCard extends Card implements IAction {
    protected ActionContentCard(Context context, int layout) {
        super(context, layout);
    }

    protected ActionContentCard(Context context) {
        this(context, R.layout.action_content);
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        TextView mActionText = (TextView) parent.findViewById(R.id.available_action_text);
        ListView mActionList = (ListView) parent.findViewById(R.id.action_list);

        if (mActionText != null) {
            try {
                String actionText = getActionTitle();
                if (actionText != null) {
                    mActionText.setText(actionText);
                }
            } catch (Exception e) {
                Log.w("cookieMom", "Missing field info");
            }

        }

        if (mActionList != null) {
            try {

                //noinspection unchecked
                mActionList.setAdapter(new ArrayAdapter(
                        getContext(),
                        R.layout.ui_simple_medium_text,
                        R.id.content,
                        getActionList()
                ));
            } catch (Exception e) {
                Log.w("cookieMom", "Missing field info");
            }

        }
    }


}

