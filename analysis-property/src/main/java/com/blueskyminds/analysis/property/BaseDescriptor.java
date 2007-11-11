package com.blueskyminds.analysis.property;

import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.series.SeriesDescriptor;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.datetime.TimePeriod;

import javax.persistence.*;

/**
 * A common-superclass for descriptors used by the analysis results
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@MappedSuperclass
public abstract class BaseDescriptor implements SeriesDescriptor {

    private RegionOLD region;
    private AggregateSet aggregateSet;
    private Timespan timespan;
    private TimePeriod timePeriod;

    protected BaseDescriptor(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod) {
        this.region = region;
        this.aggregateSet = aggregateSet;
        this.timespan = timespan;
        this.timePeriod = timePeriod;
    }

    /** Default constructor for ORM */
    protected BaseDescriptor() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the region specified in this descriptor */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="RegionId")
    public RegionOLD getRegion() {
        return region;
    }

    protected  void setRegion(RegionOLD region) {
        this.region = region;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the AggregateSet specified in this descriptor */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    protected  void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the TimeSpan specified in this descriptor */
    @Embedded
    public Timespan getTimespan() {
        return timespan;
    }

    protected  void setTimespan(Timespan timespan) {
        this.timespan = timespan;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the TimePeriod specified in this descriptor */
    @Embedded
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    protected void setTimePeriod(TimePeriod timePeriod) {
        this.timePeriod = timePeriod;
    }


    // ------------------------------------------------------------------------------------------------------
}
