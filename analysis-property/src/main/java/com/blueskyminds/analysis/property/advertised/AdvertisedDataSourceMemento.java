package com.blueskyminds.analysis.property.advertised;

import com.blueskyminds.framework.memento.XMLMemento;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

/**
 * A XMLMemento describing an Advertisement-based DataSource
 *  The memento identifies the type of advertisement included
 *
 * Date Started: 9/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AdvertisedDataSourceMemento extends XMLMemento {

    private PropertyAdvertisementTypes type;

    // ------------------------------------------------------------------------------------------------------

    public AdvertisedDataSourceMemento(PropertyAdvertisementTypes type) {
        this.type = type;
    }

    public AdvertisedDataSourceMemento() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AdvertisedDataSourceMemento with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public PropertyAdvertisementTypes getType() {
        return type;
    }

    public void setType(PropertyAdvertisementTypes type) {
        this.type = type;
    }

    public String toString() {
        return "AdvertisedDataSourceMemento["+type+"]";
    }
}
