package com.blueskyminds.analysis.property.pricestatistics.task;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.framework.IdentityRef;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.memento.XMLMemento;

/**
 * Contains some common code for an analysis datasource memento
 *
 * Date Started: 2/10/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class BaseAnalysisMemento extends XMLMemento {

    private IdentityRef dataSource;
    private IdentityRef region;
    private IdentityRef aggregateSet;
    private MonthOfYear monthOfYear;
    private Interval interval;

    public BaseAnalysisMemento(DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, MonthOfYear monthOfYear, Interval interval) {
        this.dataSource = dataSource.getIdentity();
        this.region = region.getIdentity();
        this.aggregateSet = aggregateSet.getIdentity();
        this.monthOfYear = monthOfYear;
        this.interval = interval;
    }

    public IdentityRef getDataSource() {
        return dataSource;
    }

    public void setDataSource(IdentityRef dataSource) {
        this.dataSource = dataSource;
    }

    public IdentityRef getRegion() {
        return region;
    }

    public void setRegion(IdentityRef region) {
        this.region = region;
    }

    public IdentityRef getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(IdentityRef aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    public MonthOfYear getTimePeriod() {
        return monthOfYear;
    }

    public void setTimePeriod(MonthOfYear monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public Interval getTimespan() {
        return interval;
    }

    public void setTimespan(Interval interval) {
        this.interval = interval;
    }
    
    // ------------------------------------------------------------------------------------------------------
}
