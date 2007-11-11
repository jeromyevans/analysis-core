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
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface ComputedResult extends Serializable {        

    /**
     * This flag indicates whether this result is part of a ComputedResult and needs to be merged
     *  with other parts.
     *
     * @return false
     * @see PartialResult
     */
    boolean isPartial();

}
