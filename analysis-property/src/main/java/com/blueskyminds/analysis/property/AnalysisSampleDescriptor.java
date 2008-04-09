package com.blueskyminds.analysis.property;

import com.blueskyminds.analysis.core.series.SeriesDescriptor;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;

import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * A common-superclass identify the sample used for aggregated property analysis
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@MappedSuperclass
public abstract class AnalysisSampleDescriptor implements SeriesDescriptor {

    private RegionHandle region;
    private AggregateSet aggregateSet;
    private Interval interval;
    private MonthOfYear monthOfYear;

    protected AnalysisSampleDescriptor(RegionHandle region, AggregateSet aggregateSet, Interval interval, MonthOfYear monthOfYear) {
        this.region = region;
        this.aggregateSet = aggregateSet;
        this.interval = interval;
        this.monthOfYear = monthOfYear;
    }

    /** Default constructor for ORM */
    protected AnalysisSampleDescriptor() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the region specified in this descriptor */
    @ManyToOne
    @JoinColumn(name="RegionId")
    public RegionHandle getRegion() {
        return region;
    }

    protected  void setRegion(RegionHandle region) {
        this.region = region;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the AggregateSet specified in this descriptor */
    @ManyToOne
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    protected  void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the Interval specified in this descriptor */
    @Embedded
    public Interval getInterval() {
        return interval;
    }

    protected  void setInterval(Interval interval) {
        this.interval = interval;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the MonthOfYear specified in this descriptor */
    @Embedded
    public MonthOfYear getMonthOfYear() {
        return monthOfYear;
    }

    protected void setMonthOfYear(MonthOfYear monthOfYear) {
        this.monthOfYear = monthOfYear;
    }


    // ------------------------------------------------------------------------------------------------------
}
