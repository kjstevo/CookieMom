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


package net.kjmaster.cookiemom.summary.stat.scout;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
class SummaryStatScoutValues {
    String code;
    float value;
    float delta;
    float deltaPerc;

    public SummaryStatScoutValues() {
    }

    public SummaryStatScoutValues(String code, float value, float delta, float deltaPerc) {
        this.code = code;
        this.value = value;
        this.delta = delta;
        this.deltaPerc = deltaPerc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getDelta() {
        return delta;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDeltaPerc() {
        return deltaPerc;
    }

    public void setDeltaPerc(float deltaPerc) {
        this.deltaPerc = deltaPerc;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
