package com.blueskyminds.analysis.property.pricestatistics.task;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;

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

    public PriceAnalysisDataSourceMemento(DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, MonthOfYear monthOfYear, Interval interval, PersistenceService persistenceService) {
        super(dataSource, region, aggregateSet, monthOfYear, interval);
        this.persistenceService = persistenceService;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Realizes an instance of a SeriesDescriptor from these memento */
    public PriceAnalysisSampleDescriptor toDescriptor() {
        PriceAnalysisSampleDescriptor seriesDescriptor = null;

        try {
            DataSource dataSource = (DataSource) persistenceService.findById(getDataSource());
            RegionHandle region = (RegionHandle) persistenceService.findById(getRegion());
            AggregateSet aggregateSet = (AggregateSet) persistenceService.findById(getAggregateSet());

            seriesDescriptor = new PriceAnalysisSampleDescriptor(region, aggregateSet, getTimespan(), getTimePeriod(), dataSource);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        return seriesDescriptor;
    }

    // ------------------------------------------------------------------------------------------------------

}