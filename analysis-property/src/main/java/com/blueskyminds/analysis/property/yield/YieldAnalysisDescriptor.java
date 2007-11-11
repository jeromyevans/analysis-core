package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.BaseDescriptor;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.datetime.TimePeriod;

import javax.persistence.*;

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
public class YieldAnalysisDescriptor extends BaseDescriptor {

    private DataSource salesDataSource;
    private DataSource rentalsDataSource;

    // ------------------------------------------------------------------------------------------------------

    public YieldAnalysisDescriptor(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod, DataSource salesDataSource, DataSource rentalsDataSource) {
        super(region, aggregateSet, timespan, timePeriod);
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
    public DataSource getSalesDataSource() {
        return salesDataSource;
    }

    public void setSalesDataSource(DataSource salesDataSource) {
        this.salesDataSource = salesDataSource;
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="RentalsDataSourceId")
    public DataSource getRentalsDataSource() {
        return rentalsDataSource;
    }

    public void setRentalsDataSource(DataSource rentalsDataSource) {
        this.rentalsDataSource = rentalsDataSource;
    }

    // ------------------------------------------------------------------------------------------------------

    public String toString() {
        return salesDataSource.getIdentityName()+"/"+rentalsDataSource.getIdentityName()+" "+getRegion()+" "+getAggregateSet()+" "+getTimespan()+ " "+getTimePeriod();
    }
}
