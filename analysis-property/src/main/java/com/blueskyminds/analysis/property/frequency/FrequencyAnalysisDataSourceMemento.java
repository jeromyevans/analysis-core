package com.blueskyminds.analysis.property.frequency;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.analysis.property.pricestatistics.task.BaseAnalysisMemento;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;

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

    public FrequencyAnalysisDataSourceMemento(DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, MonthOfYear monthOfYear, Interval interval, PersistenceService persistenceService) {
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
