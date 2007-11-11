package com.blueskyminds.analysis.property.priceAnalysis;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.BaseDescriptor;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.datetime.TimePeriod;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

/**
 * IdentityRef for a PriceAnalysis dataset
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class PriceAnalysisDescriptor extends BaseDescriptor {

    private DataSource dataSource;

    public PriceAnalysisDescriptor(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod, DataSource dataSource) {
        super(region, aggregateSet, timespan, timePeriod);
        this.dataSource = dataSource;
    }

    /** Default constructor for ORM */
    protected PriceAnalysisDescriptor() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PriceAnalysisDescriptor with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="DataSourceId")
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // ------------------------------------------------------------------------------------------------------

}
