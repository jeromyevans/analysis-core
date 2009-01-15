package com.blueskyminds.analysis.core.series;

import com.blueskyminds.homebyfive.framework.core.analysis.statistics.ComputeAdapter;

import java.math.BigDecimal;

/**
 * Date Started: 4/01/2009
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class DataAdapter implements ComputeAdapter {

    /**
     * Get the value from the Data object
     */
    public BigDecimal valueOf(Object object) {
        return ((Data) object).getValue();
    }
}
