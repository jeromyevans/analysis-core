package com.blueskyminds.analysis.engine;

/**
 * Contains partial results from a compute-engine calculation.
 * The partial results can be merged into a complete ComputedResult via the merge method
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */

// ------------------------------------------------------------------------------------------------------

public abstract class PartialResult<T extends ComputedResult> extends ComputedResult {

    /** Merge these results into the given ComputedResult */
    public abstract void merge(T computedResult);

    /** This flag indicates that this result is only part of a ComputedResult and needs to be merged
     *  with other parts.
     * @return true
     * @see PartialResult
     */
    public boolean isPartial() {
        return true;
    }
}
