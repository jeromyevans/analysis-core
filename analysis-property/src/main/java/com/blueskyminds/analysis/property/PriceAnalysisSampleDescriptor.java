package com.blueskyminds.analysis.property;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Identifies the sample used for the price analysis
 *
 * Date Started: 25/09/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class PriceAnalysisSampleDescriptor extends AnalysisSampleDescriptor {

    private DataSource dataSource;

    public PriceAnalysisSampleDescriptor(RegionHandle region, AggregateSet aggregateSet, Interval interval, MonthOfYear monthOfYear, DataSource dataSource) {
        super(region, aggregateSet, interval, monthOfYear);
        this.dataSource = dataSource;
    }

    /** Default constructor for ORM */
    protected PriceAnalysisSampleDescriptor() {
    }


    /**
     * The datasource used for the analysis.  eg. sales asking price
     * 
     * @return
     */
    @ManyToOne
    @JoinColumn(name="DataSourceId")
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
