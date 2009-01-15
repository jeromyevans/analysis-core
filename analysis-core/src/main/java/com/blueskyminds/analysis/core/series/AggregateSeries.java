package com.blueskyminds.analysis.core.series;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Multiple series grouped together
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class AggregateSeries implements Series<Series> {

    private SeriesDescriptor seriesDescriptor;
    private List<Series> seriesList;

    public AggregateSeries(SeriesDescriptor seriesDescriptor) {
        this.seriesDescriptor = seriesDescriptor;
        init();
    }

    /**
     * Initialise the AggregateSeries with default attributes
     */
    private void init() {
        seriesList = new LinkedList<Series>();
    }

    public boolean add(Series series) {
        return seriesList.add(series);
    }

    public List<Series> getSeriesList() {
        return seriesList;
    }

    public Iterator<Series> iterator() {
        return seriesList.iterator();
    }

    public int size() {
        return seriesList.size();
    }

    /** Return the series in a array */
    public Series[] toArray() {
        Series[] valueArray = new Series[size()];
        seriesList.toArray(valueArray);
        return valueArray;
    }

    public SeriesDescriptor getSeriesDescriptor() {
        return seriesDescriptor;
    }

}
