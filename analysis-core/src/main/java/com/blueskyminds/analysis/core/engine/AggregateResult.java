package com.blueskyminds.analysis.core.engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains a payload of multiple ComputedResult's
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/> 
 */
public class AggregateResult implements ComputedResult {

    private List<ComputedResult> computedResults;

    public AggregateResult() {
        init();
    }

    /**
     * Initialise the AggregateResult with default attributes
     */
    private void init() {
        computedResults = new LinkedList<ComputedResult>();
    }

    public boolean addComputedResult(ComputedResult computedResult) {
        return computedResults.add(computedResult);
    }

    public List<ComputedResult> getComputedResults() {
        return computedResults;
    }


    /**
     * True if any result is partial
     *
     * @return false
     * @see com.blueskyminds.analysis.core.engine.PartialResult
     */
    public boolean isPartial() {
        for (ComputedResult result : computedResults) {
            if (result.isPartial()) {
                return true;
            }
        }
        return false;
    }
}
