package com.blueskyminds.analysis.property.priceAnalysis;

import com.blueskyminds.framework.AbstractDomainObject;
import com.blueskyminds.analysis.core.statistics.Statistics;

import javax.persistence.Entity;
import javax.persistence.Embedded;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.PrintStream;

/**
 * Persists the results of statistical price analysis for an AnalysisSet
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
@NamedQueries( {
    @NamedQuery(
        name="priceAnalysis.statisticsForYieldAnalysis",
        // Lookup a pair of PriceAnalysis for a Sales datasource and Rentals datasouce calculated for a given analysis TimeSpan
        query="from PriceAnalysis sales, PriceAnalysis rentals where " +
                     "sales.descriptor.dataSource = :salesDataSource and " +
                     "rentals.descriptor.dataSource = :rentalsDataSource and " +
                     "sales.descriptor.timespan.periods = :ts_periods and " +
                     "sales.descriptor.timespan.periodType = :ts_periodType and " +
                     "sales.descriptor.region = rentals.descriptor.region and " +
                     "sales.descriptor.aggregateSet = rentals.descriptor.aggregateSet and " +
                     "sales.descriptor.timespan.periods = rentals.descriptor.timespan.periods and " +
                     "sales.descriptor.timespan.periodType = rentals.descriptor.timespan.periodType and " +
                     "sales.descriptor.timePeriod.year = rentals.descriptor.timePeriod.year and " +
                     "sales.descriptor.timePeriod.month = rentals.descriptor.timePeriod.month")})
public class PriceAnalysis extends AbstractDomainObject {

    private PriceAnalysisDescriptor descriptor;
    private Statistics statistics;

    // ------------------------------------------------------------------------------------------------------

    public PriceAnalysis(PriceAnalysisDescriptor descriptor, Statistics statistics) {
        this.descriptor = descriptor;
        this.statistics = statistics;
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Default constructor for ORM */
    protected PriceAnalysis() {
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PriceAnalysis with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** The descriptor for the analysis */
    @Embedded
    public PriceAnalysisDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(PriceAnalysisDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    // ------------------------------------------------------------------------------------------------------

    /** The calculated statistics properties, embeded into this entity for the analysis key */
    @Embedded
    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void print(PrintStream out) {
        out.println(getIdentityName()+ " descriptor: "+descriptor+" size="+statistics.getSize()+" min="+statistics.getMin()+" max="+statistics.getMax()+" median="+statistics.getMedian());
    }
}
