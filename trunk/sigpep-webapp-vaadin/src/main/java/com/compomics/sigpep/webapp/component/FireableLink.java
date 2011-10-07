package com.compomics.sigpep.webapp.component;

import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Link;

/**
 * This class is a
 */
public class FireableLink extends Link {

    /**
     * {@inheritDoc}
     */
    public FireableLink(String aDownload, StreamResource aStreamResource) {
        super(aDownload, aStreamResource);
    }

    /**
     * Emits the component event. It is transmitted to all registered listeners
     * interested in such events.
     */
    @Override
    public void fireComponentEvent() {
        super.fireComponentEvent();
    }

    /**
     * Sends the event to all listeners.
     *
     * @param event the Event to be sent to all listeners.
     */
    @Override
    public void fireEvent(Event event) {
        super.fireEvent(event);
    }
}
