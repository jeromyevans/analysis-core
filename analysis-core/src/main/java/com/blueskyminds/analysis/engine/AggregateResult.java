package com.blueskyminds.analysis.engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains a payload of multiple ComputedResult's
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AggregateResult extends ComputedResult {

    private List<ComputedResult> computedResults;

    public AggregateResult() {
        init();
    }
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AggregateResult with default attributes
     */
    private void init() {
        computedResults = new LinkedList<ComputedResult>();
    }

    // ------------------------------------------------------------------------------------------------------

    public boolean addComputedResult(ComputedResult computedResult) {
        return computedResults.add(computedResult);
    }

    public List<ComputedResult> getComputedResults() {
        return computedResults;
    }
}
