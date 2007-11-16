package com.blueskyminds.analysis;

import com.blueskyminds.analysis.core.sets.AggregateSetGroup;

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
}
