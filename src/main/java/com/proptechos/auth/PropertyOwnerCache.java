package com.proptechos.auth;

/**
 * PropertyOwnerCache - instance of this class intends to keep Property Owner,
 * that should be used for all requests to ProptechOS
 */
public class PropertyOwnerCache {

    private static volatile PropertyOwnerCache INSTANCE;
    private static final Object lock = new Object();
    private String propertyOwnerId;

    private PropertyOwnerCache() {
    }

    public static PropertyOwnerCache getInstance() {
        if (INSTANCE == null) {
            synchronized (lock) {
                if (INSTANCE == null) {
                    INSTANCE = new PropertyOwnerCache();
                }
            }
        }

        return INSTANCE;
    }

    public void setPropertyOwner(String propertyOwnerId) {
        this.propertyOwnerId = propertyOwnerId;
    }

    public String getPropertyOwner() {
        return propertyOwnerId;
    }

}
