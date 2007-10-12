package com.blueskyminds.analysis.series;

import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * A series of data that for analysis by a ComputeWorker
 *
 * Date Started: 16/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public interface Series<T> extends Serializable {

    /** Add the specified value to the series.
     * NOTE: NULL values are discarded
     *
     * @param value
     * @return true if added
     */
    boolean add(T value);

    SeriesDescriptor getSeriesDescriptor();

    /** Get an iterator on the series */
    Iterator<T> iterator();

    int size();

    /** Return the series in a array */
    T[] toArray();
}
