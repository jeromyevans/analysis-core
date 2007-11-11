package com.blueskyminds.analysis.core.engine;

/**
 * Contains partial results from a compute-engine calculation.
 * The partial results can be merged into a complete ComputedResult via the merge method
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/> 
 */

public interface PartialResult<T extends ComputedResult> extends ComputedResult {

    /**
     * Merge these results into the given ComputedResult
     **/
    void merge(T computedResult);

}
