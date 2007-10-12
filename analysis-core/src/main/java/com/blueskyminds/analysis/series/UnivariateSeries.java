package com.blueskyminds.analysis.series;

import java.math.BigDecimal;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Arrays;
import java.io.Serializable;

/**
 *
 * A series of data for analysis by a ComputeWorker
 *
 * The series always includes a reference to a SeriesDescriptor - the descriptor contains the metadata about
 *  this series
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class UnivariateSeries extends AbstractSeries<BigDecimal> {

    // ------------------------------------------------------------------------------------------------------

    /** Create a new, empty series */
    public UnivariateSeries(SeriesDescriptor seriesDescriptor) {
        super(seriesDescriptor);
        init();
    }

    /** Create a new series containing the given initial values */
    public UnivariateSeries(SeriesDescriptor seriesDescriptor, BigDecimal[] initialValues) {
        super(seriesDescriptor);
        init();
        getValues().addAll(Arrays.asList(initialValues));
    }

    // ------------------------------------------------------------------------------------------------------

    private void init() {
    }

    /** Return the series in a array */
    public BigDecimal[] toArray() {
        BigDecimal[] valueArray = new BigDecimal[size()];
        getValues().toArray(valueArray);
        return valueArray;
    }    

}
