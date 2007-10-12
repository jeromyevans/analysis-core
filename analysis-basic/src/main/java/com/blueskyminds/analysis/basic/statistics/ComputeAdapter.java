package com.blueskyminds.analysis.basic.statistics;

import java.math.BigDecimal;

/**
 * Implementations can extract a numerical value to use in computations from any object or provider that realizes this interface
 *
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public interface ComputeAdapter {

    /** Get the value from the object */
    BigDecimal valueOf(Object object);
}
