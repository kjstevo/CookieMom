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
