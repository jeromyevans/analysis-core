package com.blueskyminds.analysis.series;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * A series that is immutable, derived from a mutable series
 *
 * Create a new copy of the data in the original series, and a new SeriesDescriptor
 *  The new SeriesDescriptor will have the same references as the orginal 
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class ImmutableSeries extends AbstractSeries<Object> {

    public ImmutableSeries(Series series) {
        super(series.getSeriesDescriptor());
        super.setValues(Arrays.asList(series.toArray()));
    }

    public boolean add(BigDecimal value) {
        throw new UnsupportedOperationException("Attempted to add values to an immutable series");
    }

    public void setValues(List<Object> values) {
        throw new UnsupportedOperationException("Attempted to modify values to an immutable series");
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return the series in a array */
    public Object[] toArray() {
        Object[] valueArray = new Object[size()];
        getValues().toArray(valueArray);
        return valueArray;
    }
    
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
}
