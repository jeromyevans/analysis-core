package com.blueskyminds.analysis.core.engine;

import com.blueskyminds.analysis.core.series.Series;
import com.blueskyminds.homebyfive.framework.core.analysis.statistics.ComputeAdapter;

import java.util.concurrent.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * An abstract implementation of a compute worker
 *
 * A compute worker performs a computational task on a Series and returns a ComputedResult
 * The worker:
 *    performs one or more computation tasks on an input Series
 *    generates a ComputedResult
 *
 * Subtasks may be performed concurrently and must be independet.  If there are multiple tasks, each task
 *   generates a PartialResult that is merged into the ComputedResult
 *
 * Date Started: 24/08/2006
 *
 * History:
 *
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd<br/>
 */
public abstract class ComputeWorker implements Callable<ComputedResult> {

    private static final Log LOG = LogFactory.getLog(ComputeWorker.class);

    private ExecutorService executorService;
    private Collection<Callable<ComputedResult>> tasks;

    private Series series;
    private ComputeAdapter adapter;

    /** Create a ComputerWorker that will perform an operation on a single series */
    protected ComputeWorker(int poolSize, Series series, ComputeAdapter adapter) {
        init(poolSize);
        this.series = series;
        this.adapter = adapter;
    }

    /** Create a ComputerWorker that will perform an operation on a single series */
    protected ComputeWorker(int poolSize, ComputeAdapter adapter) {
        init(poolSize);
        this.adapter = adapter;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialsie a new ComputeWorker instance that can be re-used for multiple series
     *
     * The worker creates two ComputationTasks ready for computation - call compute directly to use
     *  the worker
     */
    public ComputeWorker(int poolSize) {
        init(poolSize);
        this.series = null;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Perform the unit of work - blocking call 
     * This method is intended for use by the ComputeEngine */
    public ComputedResult call() throws Exception {
        return compute(series);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the ComputeWorker with default attributes
     */
    private void init(int poolSize) {
        executorService = Executors.newFixedThreadPool(poolSize);
        tasks = new HashSet<Callable<ComputedResult>>(poolSize);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add a compuation task to this worker.  Each computation task is potentially executed concurrently */
    protected boolean addTask(ComputationTask<ComputedResult> task) {
        return tasks.add(task);
    }

    /**
     * Return true if there's multiple sub-tasks in this worker.  If there are multiple tasks,
     * each task is expected to return a PartialResult that are merged into the ComputedResult
     * @return true if there's more than 1 task
     */
    public boolean hasMultipleTasks() {
        return tasks.size() > 1;
    }

    /** Get the adapter that's used to extract a BigDecimal value from the underlying object */
    public ComputeAdapter getAdapter() {
        return adapter;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Construct a new ComputedResult, ready to have partialResults merged into it */
    protected abstract ComputedResult newResultInstance();

    // ------------------------------------------------------------------------------------------------------

    /** Computes the result for the provided series */
    public ComputedResult compute(Series series) {
        ComputedResult result = null;

        List<Future<ComputedResult>> futureResults;
        ComputedResult interimResult;
        boolean failed = false;

        try {
            // perpare the tasks to use the series
            for (Callable<ComputedResult> task : tasks) {
                ((ComputationTask) task).setSeries(series);
            }

            // invoke all of the ComputionTasks
            futureResults = executorService.invokeAll(tasks);

            // prepare the combined result
            if (hasMultipleTasks()) {
                result = newResultInstance();
            }

            // iterate through any partial results, merging them into the computed result object
            for (Future<ComputedResult> future : futureResults) {
                interimResult = future.get();

                if (interimResult != null) {
                    if (interimResult.isPartial()) {
                        // merge results into the computed result object
                        ((PartialResult) interimResult).merge(result);
                    } else {
                        // only one result expected
                        result = interimResult;
                        break;
                    }
                }
            }
        } catch (ExecutionException e) {
            failed = true;
            LOG.error(e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            failed = true;
        } catch (CancellationException e) {
            failed = true;
        }

        if (!failed) {
            return result;
        } else {
            return null;
        }
    }
}
