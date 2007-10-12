package com.blueskyminds.analysis.basic.statistics;

import java.math.BigDecimal;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class LongAdapter implements ComputeAdapter {

    /**
     * Get the value from the object
     */
    public BigDecimal valueOf(Object object) {
        return new BigDecimal((Long) object);
    }
}
