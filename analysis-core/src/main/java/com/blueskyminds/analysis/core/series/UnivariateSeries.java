package com.blueskyminds.analysis.core.series;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class UnivariateSeries extends AbstractSeries<Data> {

    /** Create a new, empty series */
    public UnivariateSeries(SeriesDescriptor seriesDescriptor) {
        super(seriesDescriptor);
        init();
    }

    /** Create a new series containing the given initial values */
    public UnivariateSeries(SeriesDescriptor seriesDescriptor, BigDecimal[] initialValues) {
        super(seriesDescriptor);
        init();
        getValues().addAll(Data.asList(initialValues));
    }

    private void init() {
    }

    /** Return the data series in a array */
    public Data[] toArray() {
        Data[] valueArray = new Data[size()];
        return getValues().toArray(valueArray);
    }

    /**
     * Get the array of BigDecimal values
     * @return
     */
    public BigDecimal[] values() {
        BigDecimal[] valueArray = new BigDecimal[size()];
        List<Data> values = getValues();
        for (int i = 0; i < values.size(); i++) {
            Data value = values.get(i);
            valueArray[i] = value.getValue();
        }
        return valueArray;
    }

}
