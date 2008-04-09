package com.blueskyminds.analysis;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;

/**
 * Date Started: 15/11/2007
 * <p/>
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 *
 */
public interface AnalysisService {
    
    /**
     * Lookup an analysis datasource by its unique key
     *
     * @param key
     * @return
     */
    AnalysisDataSource findDataSource(String key);

    /**
     * Lookup an AggregateSetGroup by its unique key
     *
     * @param key
     * @return
     */
    AggregateSetGroup findAggregateSetGroup(String key);

    /**
     * In one long blocking iteration, calculates all the mappings between premises to aggregatesets in the specified group
     *
     * @param aggregateSetGroupKey
     */
    void recalculatePremiseToAggregateSetMaps(String aggregateSetGroupKey);

    /**
     * In one long blocking iteration, calculates all the mappings between premises and regions
     *
     */
    void recalculatePremiseToRegionMaps();

    /**
     * Perform the analysis for the specified region over a date range for the specified aggregate sets and interval
     *
     * @param region
     * @param recurseSubregions    if true, each subregion will also be analysed
     * @param aggregateSetGroup
     * @param startDate
     * @param endDate
     * @param interval
     */
    void analyseRegion(RegionHandle region, boolean recurseSubregions, AggregateSetGroup aggregateSetGroup, MonthOfYear startDate, MonthOfYear endDate, Interval interval);
}
