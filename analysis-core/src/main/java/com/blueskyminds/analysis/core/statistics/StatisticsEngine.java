package com.blueskyminds.analysis.core.statistics;

import com.blueskyminds.analysis.core.engine.ComputeEngine;
import com.blueskyminds.analysis.core.engine.ComputeWorker;
import com.blueskyminds.analysis.core.series.Series;
import com.blueskyminds.analysis.core.series.DataAdapter;
import com.blueskyminds.homebyfive.framework.core.analysis.statistics.BigDecimalAdapter;

/**
 *
 * Implements a ComputeEngine that performs statistics operations on a series
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd<br/>
 */
public class StatisticsEngine extends ComputeEngine {

    public StatisticsEngine() {
        super();
    }

    /**
     * Create a new StatisticsWorker instance.
     * The series is assumed to contain simple Data instances
     **/
    protected ComputeWorker newWorkerInstance(Series series) {
        return new StatisticsWorker(series, new DataAdapter());
    }

    /**
     * Initialise the StatisticsEngine with default attributes
     */
    private void init() {
    }

}
