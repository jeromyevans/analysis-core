package com.blueskyminds.analysis.basic.statistics;

import java.math.BigDecimal;

/**
 * An adapter that simply passes through a BigDecimal value
 * 
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class BigDecimalAdapter implements ComputeAdapter {

    /**
     * Get the value from the object
     * The object is assumed to be a big decimal, so this is just a pass-through
     */
    public BigDecimal valueOf(Object object) {
        return (BigDecimal) object;
    }
}
