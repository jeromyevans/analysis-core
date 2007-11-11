package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.series.AggregateSeries;
import com.blueskyminds.analysis.core.series.BivariateSeries;
import com.blueskyminds.analysis.core.series.Pair;
import com.blueskyminds.analysis.core.statistics.Statistics;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysis;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysisDescriptor;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.paging.Pager;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Spools PriceAnalysis data for calculating Yield
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldAnalysisSpooler extends EntitySpooler {

    private static final Log LOG = LogFactory.getLog(YieldAnalysisSpooler.class);

    private static final String QUERY_NAME = "priceAnalysis.statisticsForYieldAnalysis";
    private DataSource salesDataSource;
    private DataSource rentalsDataSource;
    private Timespan timespan;
    private AggregateSeries aggregateSeries;

    // ------------------------------------------------------------------------------------------------------

    /** Instantiate a new spooler for the specific source, region, set and time span */
    public YieldAnalysisSpooler(EntityManager entityManager, Pager pager, DataSource salesDataSource, DataSource rentalsDataSource, Timespan timespan) {
        //super(pager, new HibernateNamedQueryImpl(QUERY_NAME));  // todo: come back to this
        super(null, null);
        //this.dataSource = dataSource;
        this.salesDataSource = salesDataSource;
        this.rentalsDataSource = rentalsDataSource;
        this.timespan = timespan;
    }

    // ------------------------------------------------------------------------------------------------------
    //
    /** Mandatory constructor for instantiation by Task */
    public YieldAnalysisSpooler(EntityManager entityManager) {
        //super(new HibernateNamedQueryImpl(QUERY_NAME)); todo: need dependency injection
        super(null, null);
    }

    // ------------------------------------------------------------------------------------------------------

//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }

    public void setSalesDataSource(DataSource salesDataSource) {
        this.salesDataSource = salesDataSource;
    }

    public void setRentalsDataSource(DataSource rentalsDataSource) {
        this.rentalsDataSource = rentalsDataSource;
    }

    public void setTimespan(Timespan timespan) {
        this.timespan = timespan;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    protected void onStart() {
        aggregateSeries = new AggregateSeries(null);

  //      HibernateNamedQueryImpl searchCriteria = (HibernateNamedQueryImpl) getQuery();

        // todo: check this migration
        getQuery().setParameter("ts_periods", timespan.getPeriods()).
              setParameter("ts_periodType", timespan.getPeriodType()).
              setParameter("salesDataSource", salesDataSource).
              setParameter("rentalsDataSource", rentalsDataSource);

//        searchCriteria.attachToSession((Session) session().getSessionImpl());
//
//        Query query = searchCriteria.getUnderlying();
//        query.setParameter("ts_periods", timespan.getPeriods()).
//              setParameter("ts_periodType", timespan.getPeriodType()).
//              setEntity("salesDataSource", salesDataSource).
//              setEntity("rentalsDataSource", rentalsDataSource);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Contribute results to the aggregateSeries for analysis
     *
     * @param queryResults - a List of [Statistics, Statistics]
     * @throws PersistenceServiceException
     */
    protected void process(List queryResults) throws SpoolerException {
        PriceAnalysis sales;
        PriceAnalysis rentals;
        Object[] tuple;
        PriceAnalysisDescriptor salesSeriesDescriptor;
        BivariateSeries inputSeries;

        Statistics salesStatistics;
        Statistics rentalsStatistics;
        for (Object result : queryResults) {
            tuple = (Object[]) result;

            sales = (PriceAnalysis) tuple[0];
            rentals = (PriceAnalysis) tuple[1];
            salesStatistics = sales.getStatistics();
            salesSeriesDescriptor = sales.getDescriptor();
            rentalsStatistics = rentals.getStatistics();

            YieldAnalysisDescriptor descriptor = new YieldAnalysisDescriptor(salesSeriesDescriptor.getRegion(),  salesSeriesDescriptor.getAggregateSet(), salesSeriesDescriptor.getTimespan(), salesSeriesDescriptor.getTimePeriod(), salesDataSource, rentalsDataSource);
            inputSeries = new BivariateSeries(descriptor);
            inputSeries.add(new Pair(salesStatistics.getMedian(), rentalsStatistics.getMedian()));
            inputSeries.add(new Pair(salesStatistics.getMean(), rentalsStatistics.getMean()));
            inputSeries.add(new Pair(salesStatistics.getMode(), rentalsStatistics.getMode()));

            aggregateSeries.add(inputSeries);
        }
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Passes the three aggregate aggregateSeries (means, medians, modes) to the ComputeEngine for calculation.
     *  Blocks until results are available, then persists results
     */
    // todo: broken
   /* protected void onComplete() {
        AggregateResult aggregateResults;
        YieldEngine yieldEngine = AnalysisService.yieldEngine();
        YieldResults yieldResult;

        Future<ComputedResult> results = yieldEngine.compute(aggregateSeries);

        try {
            aggregateResults = (AggregateResult) results.get();

            // persist the yield results
            if (aggregateResults != null) {
                for (ComputedResult result : aggregateResults.getComputedResults()) {
                    yieldResult = (YieldResults) result;


                    YieldAnalysis yieldAnalysis = new YieldAnalysis((YieldAnalysisDescriptor) yieldResult.getDescriptor());
                    // we know what order to extract the values because we placed them this order in the input series
                    yieldAnalysis.setYieldOnMedians(yieldResult.get(0));
                    yieldAnalysis.setYieldOnMeans(yieldResult.get(1));
                    yieldAnalysis.setYieldOnModes(yieldResult.get(2));         // will not be set if no mode

                    LOG.info("Saving yield analysis...");
                    em.persist(yieldAnalysis);
                    em.flush();
                }
            }

        } catch(ExecutionException e) {
            LOG.error("Analysis failed: ", e);
        } catch(InterruptedException e) {
            LOG.error("Analysis failed: ", e);
        }
    }*/

    protected void onError(PersistenceServiceException e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
