package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.AnalysisSampleDescriptor;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Contains the IdentityRef for a YieldAnalysis result
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class YieldAnalysisDescriptor extends AnalysisSampleDescriptor {

    private AnalysisDataSource salesDataSource;
    private AnalysisDataSource rentalsDataSource;

    // ------------------------------------------------------------------------------------------------------

    public YieldAnalysisDescriptor(RegionHandle region, AggregateSet aggregateSet, Interval interval, MonthOfYear monthOfYear, AnalysisDataSource salesDataSource, AnalysisDataSource rentalsDataSource) {
        super(region, aggregateSet, interval, monthOfYear);
        this.salesDataSource = salesDataSource;
        this.rentalsDataSource = rentalsDataSource;
    }

    /** Default constructor for ORM */
    protected YieldAnalysisDescriptor() {
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="SalesDataSourceId")
    public AnalysisDataSource getSalesDataSource() {
        return salesDataSource;
    }

    public void setSalesDataSource(AnalysisDataSource salesDataSource) {
        this.salesDataSource = salesDataSource;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="RentalsDataSourceId")
    public AnalysisDataSource getRentalsDataSource() {
        return rentalsDataSource;
    }

    public void setRentalsDataSource(AnalysisDataSource rentalsDataSource) {
        this.rentalsDataSource = rentalsDataSource;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return salesDataSource.getIdentityName()+"/"+rentalsDataSource.getIdentityName()+" "+getRegion()+" "+getAggregateSet()+" "+ getInterval()+ " "+ getMonthOfYear();
    }
}
