package com.blueskyminds.analysis.series;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements some generic methods common to different types of Series
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public abstract class AbstractSeries<T> implements Series<T> {

    private SeriesDescriptor seriesDescriptor;
    private List<T> values;

    public AbstractSeries(SeriesDescriptor seriesDescriptor) {
        this.seriesDescriptor = seriesDescriptor;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    private void init() {
        this.values = new LinkedList<T>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Add the specified value to the series.
     * NOTE: NULL values are discarded
     *
     * @param value
     * @return true if added
     */
    public boolean add(T value) {
        if (value != null) {
            return values.add(value);
        } else {
            return false;
        }
    }

    /** Get an iterator on the series */
    public Iterator<T> iterator() {
        return values.iterator();
    }

    /** Get the descriptor for the series */
    public SeriesDescriptor getSeriesDescriptor() {
        return seriesDescriptor;
    }

    public int size() {
        return values.size();
    }

    /** Return the series in a array */
    public abstract T[] toArray();

    protected List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }
    
    // ------------------------------------------------------------------------------------------------------
}
