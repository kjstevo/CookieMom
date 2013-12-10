package net.kmaster.cookiemom.dao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table ORDERS.
 */
@SuppressWarnings("ALL")
public class Order {

    private Long id;
    private java.util.Date orderDate;
    private long orderScoutId;
    private String orderCookieType;
    private Boolean orderedFromCupboard;
    private Integer orderedBoxes;
    private Boolean pickedUpFromCupboard;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient OrderDao myDao;

    private Scout scout;
    private Long scout__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long id, java.util.Date orderDate, long orderScoutId, String orderCookieType, Boolean orderedFromCupboard, Integer orderedBoxes, Boolean pickedUpFromCupboard) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderScoutId = orderScoutId;
        this.orderCookieType = orderCookieType;
        this.orderedFromCupboard = orderedFromCupboard;
        this.orderedBoxes = orderedBoxes;
        this.pickedUpFromCupboard = pickedUpFromCupboard;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOrderDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(java.util.Date orderDate) {
        this.orderDate = orderDate;
    }

    public long getOrderScoutId() {
        return orderScoutId;
    }

    public void setOrderScoutId(long orderScoutId) {
        this.orderScoutId = orderScoutId;
    }

    public String getOrderCookieType() {
        return orderCookieType;
    }

    public void setOrderCookieType(String orderCookieType) {
        this.orderCookieType = orderCookieType;
    }

    public Boolean getOrderedFromCupboard() {
        return orderedFromCupboard;
    }

    public void setOrderedFromCupboard(Boolean orderedFromCupboard) {
        this.orderedFromCupboard = orderedFromCupboard;
    }

    public Integer getOrderedBoxes() {
        return orderedBoxes;
    }

    public void setOrderedBoxes(Integer orderedBoxes) {
        this.orderedBoxes = orderedBoxes;
    }

    public Boolean getPickedUpFromCupboard() {
        return pickedUpFromCupboard;
    }

    public void setPickedUpFromCupboard(Boolean pickedUpFromCupboard) {
        this.pickedUpFromCupboard = pickedUpFromCupboard;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Scout getScout() {
        long __key = this.orderScoutId;
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
        if (scout == null) {
            throw new DaoException("To-one property 'orderScoutId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.scout = scout;
            orderScoutId = scout.getId();
            scout__resolvedKey = orderScoutId;
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
