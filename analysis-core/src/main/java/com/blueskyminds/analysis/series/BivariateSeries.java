package com.blueskyminds.analysis.series;

/**
 * A series of Pairs of values
 *
 * Date Started: 16/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class BivariateSeries extends AbstractSeries<Pair> {

    public BivariateSeries(SeriesDescriptor seriesDescriptor) {
        super(seriesDescriptor);
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the BivariateSeries with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Return the series in a array */
    public Pair[] toArray() {
        Pair[] valueArray = new Pair[size()];
        getValues().toArray(valueArray);
        return valueArray;
    }
}
