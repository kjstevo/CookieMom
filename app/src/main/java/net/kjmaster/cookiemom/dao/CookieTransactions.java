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

package net.kjmaster.cookiemom.dao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table COOKIE_TRANSACTIONS.
 */
@SuppressWarnings("all")
public class CookieTransactions {

    private Long id;
    private Long transScoutId;
    private Long transBoothId;
    private String cookieType;
    private Integer transBoxes;
    private java.util.Date transDate;
    private Double transCash;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient CookieTransactionsDao myDao;

    private Scout scout;
    private Long scout__resolvedKey;

    private Booth booth;
    private Long booth__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public CookieTransactions() {
    }

    public CookieTransactions(Long id) {
        this.id = id;
    }

    public CookieTransactions(Long id, Long transScoutId, Long transBoothId, String cookieType, Integer transBoxes, java.util.Date transDate, Double transCash) {
        this.id = id;
        this.transScoutId = transScoutId;
        this.transBoothId = transBoothId;
        this.cookieType = cookieType;
        this.transBoxes = transBoxes;
        this.transDate = transDate;
        this.transCash = transCash;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCookieTransactionsDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransScoutId() {
        return transScoutId;
    }

    public void setTransScoutId(Long transScoutId) {
        this.transScoutId = transScoutId;
    }

    public Long getTransBoothId() {
        return transBoothId;
    }

    public void setTransBoothId(Long transBoothId) {
        this.transBoothId = transBoothId;
    }

    public String getCookieType() {
        return cookieType;
    }

    public void setCookieType(String cookieType) {
        this.cookieType = cookieType;
    }

    public Integer getTransBoxes() {
        return transBoxes;
    }

    public void setTransBoxes(Integer transBoxes) {
        this.transBoxes = transBoxes;
    }

    public java.util.Date getTransDate() {
        return transDate;
    }

    public void setTransDate(java.util.Date transDate) {
        this.transDate = transDate;
    }

    public Double getTransCash() {
        return transCash;
    }

    public void setTransCash(Double transCash) {
        this.transCash = transCash;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Scout getScout() {
        Long __key = this.transScoutId;
        if (scout__resolvedKey == null || !scout__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ScoutDao targetDao = daoSession.getScoutDao();
            Scout scoutNew = targetDao.load(__key);
            synchronized (this) {
                scout = scoutNew;
                scout__resolvedKey = __key;
            }
        }
        return scout;
    }

    public void setScout(Scout scout) {
        synchronized (this) {
            this.scout = scout;
            transScoutId = scout == null ? null : scout.getId();
            scout__resolvedKey = transScoutId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Booth getBooth() {
        Long __key = this.transBoothId;
        if (booth__resolvedKey == null || !booth__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BoothDao targetDao = daoSession.getBoothDao();
            Booth boothNew = targetDao.load(__key);
            synchronized (this) {
                booth = boothNew;
                booth__resolvedKey = __key;
            }
        }
        return booth;
    }

    public void setBooth(Booth booth) {
        synchronized (this) {
            this.booth = booth;
            transBoothId = booth == null ? null : booth.getId();
            booth__resolvedKey = transBoothId;
        }
    }

    /**
     * Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context.
     */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context.
     */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context.
     */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
