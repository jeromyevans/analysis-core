package com.blueskyminds.analysis.property.priceAnalysis;

import com.blueskyminds.framework.IdentityRef;
import com.blueskyminds.framework.memento.XMLMemento;
import com.blueskyminds.framework.datetime.TimePeriod;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.analysis.persistent.DataSource;
import com.blueskyminds.analysis.sets.AggregateSet;
import com.blueskyminds.enterprise.region.RegionOLD;

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
    private TimePeriod timePeriod;
    private Timespan timespan;

    public BaseAnalysisMemento(DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, TimePeriod timePeriod, Timespan timespan) {
        this.dataSource = dataSource.getIdentity();
        this.region = region.getIdentity();
        this.aggregateSet = aggregateSet.getIdentity();
        this.timePeriod = timePeriod;
        this.timespan = timespan;
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

    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }

    public Timespan getTimespan() {
        return timespan;
    }

    public void setTimespan(Timespan timespan) {
        this.timespan = timespan;
    }
    
    // ------------------------------------------------------------------------------------------------------
}
