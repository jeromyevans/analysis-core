package com.blueskyminds.analysis.property.frequency;

import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.datetime.TimePeriod;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.analysis.property.priceAnalysis.BaseAnalysisMemento;
import com.blueskyminds.analysis.persistent.DataSource;
import com.blueskyminds.analysis.sets.AggregateSet;
import com.blueskyminds.enterprise.region.RegionOLD;

/**
 * A memento for a Freqency analysis datasource
 *
 * Date Started: 2/10/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class FrequencyAnalysisDataSourceMemento extends BaseAnalysisMemento {

    private PersistenceService persistenceService;

    public FrequencyAnalysisDataSourceMemento(DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, TimePeriod timePeriod, Timespan timespan, PersistenceService persistenceService) {
        super(dataSource, region, aggregateSet, timePeriod, timespan);
        this.persistenceService = persistenceService;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Realizes an instance of a SeriesDescriptor from these memento */
    public FrequencyAnalysisDescriptor toDescriptor() {
        FrequencyAnalysisDescriptor seriesDescriptor = null;

        try {
            DataSource dataSource = (DataSource) persistenceService.findById(getDataSource());
            RegionOLD region = (RegionOLD) persistenceService.findById(getRegion());
            AggregateSet aggregateSet = (AggregateSet) persistenceService.findById(getAggregateSet());

            seriesDescriptor = new FrequencyAnalysisDescriptor(region, aggregateSet, getTimespan(), getTimePeriod(), dataSource);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        return seriesDescriptor;
    }

    // ------------------------------------------------------------------------------------------------------
}
