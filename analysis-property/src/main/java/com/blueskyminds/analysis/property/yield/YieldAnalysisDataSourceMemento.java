package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.framework.memento.XMLMemento;
import com.blueskyminds.framework.IdentityRef;
import com.blueskyminds.framework.DomainObject;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.analysis.core.datasource.DataSource;

/**
 * A memento describing a Yield datasource - the memento identifies the sales datasource, rentals datasource
 *   and analysis timespan
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldAnalysisDataSourceMemento extends XMLMemento {

    private IdentityRef salesDataSource;
    private IdentityRef rentalsDataSource;

    private Timespan timespan;

    // ------------------------------------------------------------------------------------------------------

    public YieldAnalysisDataSourceMemento(DataSource salesDataSource, DataSource rentalsDataSource, Timespan timespan) {
        this.salesDataSource = salesDataSource.getIdentity();
        this.rentalsDataSource = rentalsDataSource.getIdentity();
        this.timespan = timespan;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the YieldAnalysisDataSourceMemento with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public IdentityRef getSalesDataSource() {
        return salesDataSource;
    }

    public IdentityRef getRentalsDataSource() {
        return rentalsDataSource;
    }

    public Timespan getTimespan() {
        return timespan;
    }

    public DataSource realizeSalesDataSource() {
        return (DataSource) realizeFromIdentity(salesDataSource);
    }

    public DataSource realizeRentalsDataSource() {
        return (DataSource) realizeFromIdentity(rentalsDataSource);
    }

    private DomainObject realizeFromIdentity(IdentityRef identityRef) {
        DomainObject domainObject = null;
        /*PersistenceService gateway = getPersistenceService();

        try {
            domainObject = gateway.findById(identityRef);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }*/
        // todo: come back to this
        return domainObject;
    }

    // ------------------------------------------------------------------------------------------------------

}
