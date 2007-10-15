package com.blueskyminds.analysis.property.frequency;

import com.blueskyminds.analysis.property.BaseDescriptor;
import com.blueskyminds.analysis.persistent.DataSource;
import com.blueskyminds.analysis.sets.AggregateSet;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.datetime.TimePeriod;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;

/**
 * Idntity for a FrequencyAnalysis dataset
 *
 * Date Started: 2/10/2006
 * <p/>
 * History:
 * <p/>
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Embeddable
public class FrequencyAnalysisDescriptor extends BaseDescriptor {

    private DataSource dataSource;

    public FrequencyAnalysisDescriptor(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod, DataSource dataSource) {
        super(region, aggregateSet, timespan, timePeriod);
        this.dataSource = dataSource;
    }

    /** Default constructor for ORM */
    protected FrequencyAnalysisDescriptor() {
    }
    
    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the FrequencyAnalysisDescriptor with default attributes
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
