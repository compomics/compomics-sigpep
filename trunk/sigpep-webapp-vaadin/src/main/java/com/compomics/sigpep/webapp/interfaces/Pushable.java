package com.compomics.sigpep.webapp.interfaces;

/**
 * This interfaces binds a class to accept push notifications.
 */
public interface Pushable {

    /**
     * Persist a push event.
     */
    public void push();
}
