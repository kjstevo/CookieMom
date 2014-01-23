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

package net.kjmaster.cookiemom.booth;

import android.content.Context;
import android.widget.ArrayAdapter;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.dao.Booth;

import java.text.DateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: KJ Stevo
 * Date: 12/2/13
 * Time: 9:41 AM
 */
public class BoothTextAdapterListItem extends ArrayAdapter<Object> {
    public BoothTextAdapterListItem(Context context) {
        super(context, R.layout.ui_simple_medium_text);
    }

    @Override
    public void add(Object object) {
        //net.kjmaster.cookiemom.booth.BoothTextAdapterListItem.add returns void
        Booth booth = (Booth) object;
        String boothText;
        DateFormat fmt = DateFormat.getDateInstance();
        if (booth != null) {
            boothText = fmt.format(booth.getBoothDate()) + "      " + booth.getBoothLocation();
            super.add(boothText);
        }
        //12/2/13

    }
}
