package com.blueskyminds.analysis.engine;

import com.blueskyminds.analysis.core.series.Series;
import com.blueskyminds.analysis.basic.statistics.ComputeAdapter;

import java.util.concurrent.Callable;

/**
 * A ComputationTask performs a computation task on a series of data and a result of type T (extending ComputedResult)
 *
 * Each ComputationTask may be executed asynchronously by the ComputeEngine - the computation
 *  tasks within a single worker must be independent
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 *
 */
public abstract class ComputationTask<T extends ComputedResult> implements Callable<T> {

    protected Object[] values;
    protected ComputeAdapter adapter;


    protected ComputationTask(ComputeAdapter adapter) {
        this.adapter = adapter;
    }

    protected ComputationTask() {
    }

    public void setSeries(Series series) {
        this.values = series.toArray();
    }

    /** The adapter is used to extract values for analysis from the underlying object */
    public ComputeAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ComputeAdapter adapter) {
        this.adapter = adapter;
    }

    public T call() throws Exception {
        return compute(values);
    }

    /**
     * Computer a result from the given array of objects
     *
     * @param values
     * @return
     */
    public abstract T compute(Object[] values);
}
