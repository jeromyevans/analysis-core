package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.engine.ComputeWorker;
import com.blueskyminds.analysis.engine.ComputeEngine;
import com.blueskyminds.analysis.series.Series;

/**
 * Implements a ComputeEngine that calculates the Yield on a series
 *
 * The input series must be:
 *    an aggregate of bivariate series, where the left value in each pair is the sale price, and the right
 *   value the rental price.  Each bivariate series may contain any number of pairs
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldEngine extends ComputeEngine {

    public YieldEngine() {
        super();
    }

    /** Create a new YieldWorker instance */
    protected ComputeWorker newWorkerInstance(Series series) {
        return new YieldWorker(series);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the StatisticsEngine with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------
}
