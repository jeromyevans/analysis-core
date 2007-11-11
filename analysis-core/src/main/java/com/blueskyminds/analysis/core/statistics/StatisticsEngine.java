package com.blueskyminds.analysis.core.statistics;

import com.blueskyminds.analysis.core.engine.ComputeEngine;
import com.blueskyminds.analysis.core.engine.ComputeWorker;
import com.blueskyminds.analysis.core.series.Series;
import com.blueskyminds.analysis.basic.statistics.BigDecimalAdapter;

/**
 *
 * Implements a ComputeEngine that performs statistics operations on a series
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class StatisticsEngine extends ComputeEngine {

    public StatisticsEngine() {
        super();
    }

    /**
     * Create a new StatisticsWorker instance.
     * The series is assumed to contain simple BigDecimal values
     **/
    protected ComputeWorker newWorkerInstance(Series series) {
        return new StatisticsWorker(series, new BigDecimalAdapter());
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the StatisticsEngine with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
