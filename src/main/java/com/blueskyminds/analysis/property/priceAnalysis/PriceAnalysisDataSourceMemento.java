package com.blueskyminds.analysis.property.priceAnalysis;

import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.datetime.TimePeriod;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.analysis.sets.AggregateSet;
import com.blueskyminds.analysis.persistent.DataSource;

/**
 * Contains the memento for the analysis task.
 * The properties are designed to be serialised and persisted in XML so they use an IdentityRef for the domain
 *  objects instead of referring to all of the state of each domain object.
 *
 * The PriceAnalysisDataSourceMemento can be converted to a SeriesDescriptor
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PriceAnalysisDataSourceMemento extends BaseAnalysisMemento {

    private PersistenceService persistenceService;

    public PriceAnalysisDataSourceMemento(DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, TimePeriod timePeriod, Timespan timespan, PersistenceService persistenceService) {
        super(dataSource, region, aggregateSet, timePeriod, timespan);
        this.persistenceService = persistenceService;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Realizes an instance of a SeriesDescriptor from these memento */
    public PriceAnalysisDescriptor toDescriptor() {
        PriceAnalysisDescriptor seriesDescriptor = null;

        try {
            DataSource dataSource = (DataSource) persistenceService.findById(getDataSource());
            RegionOLD region = (RegionOLD) persistenceService.findById(getRegion());
            AggregateSet aggregateSet = (AggregateSet) persistenceService.findById(getAggregateSet());

            seriesDescriptor = new PriceAnalysisDescriptor(region, aggregateSet, getTimespan(), getTimePeriod(), dataSource);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        return seriesDescriptor;
    }

    // ------------------------------------------------------------------------------------------------------

}