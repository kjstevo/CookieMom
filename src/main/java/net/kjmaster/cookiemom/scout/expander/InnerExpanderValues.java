/*
 * ******************************************************************************
 *   Copyright (c) 2013 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */


package net.kjmaster.cookiemom.scout.expander;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class InnerExpanderValues {
    String date;
    String cookie;
    double cash;
    int boxes;

    public InnerExpanderValues() {
    }

    public InnerExpanderValues(Date date, String cookieType, Double cash, int boxes) {
        this.boxes = boxes;
        this.cash = cash;
        this.cookie = cookieType;
        this.date = DateFormat.getInstance().format(date);

    }

    public int getBoxes() {
        return boxes;
    }

    public void setBoxes(int boxes) {
        this.boxes = boxes;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
