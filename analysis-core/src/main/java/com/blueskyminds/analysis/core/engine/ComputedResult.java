package com.blueskyminds.analysis.core.engine;

import java.io.Serializable;

/**
 *
 * Contains the result from a ComputeWorker
 *
 * This class will be extended for the specific implementation of the ComputeWorker
 *
 * Date Started: 24/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class ComputedResult implements Serializable {

    // ------------------------------------------------------------------------------------------------------

    protected ComputedResult() {
    }

    /** This flag indicates whether this result is part of a ComputedResult and needs to be merged
     *  with other parts.
     * @return false
     * @see PartialResult
     */
    public boolean isPartial() {
        return false;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
