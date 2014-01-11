package net.kjmaster.cookiemom.dao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

/**
 * Entity mapped to table BOOTH_ASSIGNMENTS.
 */
@SuppressWarnings("ALL")
public class BoothAssignments {

    private Long id;
    private Long boothAssignScoutId;
    private Long boothAssignBoothId;

    /**
     * Used to resolve relations
     */
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    private transient BoothAssignmentsDao myDao;

    private Scout scout;
    private Long scout__resolvedKey;

    private Booth booth;
    private Long booth__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public BoothAssignments() {
    }

    public BoothAssignments(Long id) {
        this.id = id;
    }

    public BoothAssignments(Long id, Long boothAssignScoutId, Long boothAssignBoothId) {
        this.id = id;
        this.boothAssignScoutId = boothAssignScoutId;
        this.boothAssignBoothId = boothAssignBoothId;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBoothAssignmentsDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBoothAssignScoutId() {
        return boothAssignScoutId;
    }

    public void setBoothAssignScoutId(Long boothAssignScoutId) {
        this.boothAssignScoutId = boothAssignScoutId;
    }

    public Long getBoothAssignBoothId() {
        return boothAssignBoothId;
    }

    public void setBoothAssignBoothId(Long boothAssignBoothId) {
        this.boothAssignBoothId = boothAssignBoothId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Scout getScout() {
        Long __key = this.boothAssignScoutId;
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
            boothAssignScoutId = scout == null ? null : scout.getId();
            scout__resolvedKey = boothAssignScoutId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    public Booth getBooth() {
        Long __key = this.boothAssignBoothId;
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
            boothAssignBoothId = booth == null ? null : booth.getId();
            booth__resolvedKey = boothAssignBoothId;
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