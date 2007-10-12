package com.blueskyminds.analysis.engine;

import com.blueskyminds.analysis.series.ImmutableSeries;
import com.blueskyminds.analysis.series.AggregateSeries;
import com.blueskyminds.analysis.series.Series;

import java.util.concurrent.*;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A ComputeEngine contains a pool of ComputeWorkers for performing computational tasks.
 *
 * Each worker may be invoked concurrently
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class ComputeEngine {

    private static final Log LOG = LogFactory.getLog(ComputeEngine.class);

    private ExecutorService executorService;
    private static final int CORE_POOL_SIZE     = 20;
    private static final int MAXIMUM_POOL_SIZE  = 100;
    private static final int KEEP_ALIVE_TIME    = 180;

    public ComputeEngine() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Commence computation on the specified series */
    public Future<ComputedResult> compute(Series series) {
        Future<ComputedResult> result;

        //long timeBefore = System.currentTimeMillis();
        result = executorService.submit(newWorkerInstance(new ImmutableSeries(series)));
        //long timeAfter = System.currentTimeMillis();
        //LOG.debug(timeAfter-timeBefore + " millis");

        return result;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Commence computation on all of the specified series, each in their own worker */
    public List<Future<ComputedResult>> computeAll(AggregateSeries aggregateSeries) {
        List<Future<ComputedResult>> results = new LinkedList<Future<ComputedResult>>();

        for (Series series : aggregateSeries.getSeriesList()) {
            results.add(executorService.submit(newWorkerInstance(new ImmutableSeries(series))));
        }

        return results;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Factory method to create a new worker instance.
     * Implemented by subclasses of the compute engine
     *
     * @param series
     * @return ComputeWorker instance
     */
    protected abstract ComputeWorker newWorkerInstance(Series series);

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------


    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the ComputeWorker with default attributes
     */
    private void init() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        executorService = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue);
    }
}
