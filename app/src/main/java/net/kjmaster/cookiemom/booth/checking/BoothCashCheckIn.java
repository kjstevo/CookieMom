/*
 * Copyright (c) 2014.  Author:Steven Dees(kjstevokjmaster@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.kjmaster.cookiemom.booth.checking;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;

import net.kjmaster.cookiemom.R;
import net.kjmaster.cookiemom.global.CookieOrCashDialogBase;

/**
 * Created by Dell_Owner on 2/26/14.
 */
@EActivity(R.layout.scout_order_layout)
public class BoothCashCheckIn extends CookieOrCashDialogBase {


    @Override
    protected void createActionFragment() {

    }

    @Override
    protected boolean isEditableValue() {
        return false;
    }

    @Override
    protected void saveForemData() {

    }


    @Extra
    long BoothId;


    @Override
    protected void saveData() {

    }

    @Override
    protected boolean isNegative() {
        return false;
    }

    @AfterViews
    void afterViewFrag() {
        CreateDialog(BoothId, this, "Booth Check-in");
    }



}
