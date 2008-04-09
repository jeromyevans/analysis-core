package com.blueskyminds.analysis.property.pricestatistics;

import com.blueskyminds.analysis.core.engine.ComputedResult;
import com.blueskyminds.analysis.core.series.UnivariateSeries;
import com.blueskyminds.analysis.core.statistics.Statistics;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.framework.persistence.spooler.PageSpoolerListener;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;
import com.blueskyminds.framework.persistence.spooler.SpoolerTask;
import com.blueskyminds.landmine.core.property.AskingPrice;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * A SpoolerTask that calculates the statistics for prices within a region and aggregate set
 *
 * Each page generates an entry in a series for analysis
 * Upon completion, the statistics of the series is calculated and persisted
 *
 * Date Started: 28/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class AskingPriceStatisticsTask implements SpoolerTask<AskingPrice>, PageSpoolerListener {

    private static final Log LOG = LogFactory.getLog(AskingPriceStatisticsTask.class);

    private EntityManager em;
    private PriceAnalysisSampleDescriptor descriptor;
    private StatisticsEngine statisticsEngine;

    private UnivariateSeries series;

    public AskingPriceStatisticsTask(EntityManager em, PriceAnalysisSampleDescriptor descriptor, StatisticsEngine engine) {
        this.em = em;
        this.descriptor = descriptor;
        this.statisticsEngine = engine;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Instantiate a new spooler for the specific source, region, set and time span */
//    public AskingPriceStatisticsTask(StatisticsEngine statisticsEngine, DataSource dataSource, RegionHandle region, AggregateSet aggregateSet, Interval timespan, MonthOfYear timePeriod) {
//        //super(new HibernateNamedQueryImpl(QUERY_NAME));
//        super(null, null, null);  // todo: come back to this
//        this.descriptor = new PriceAnalysisSampleDescriptor(region, aggregateSet, timespan, timePeriod, dataSource);
//        this.statisticsEngine = statisticsEngine;
//    }

    // ------------------------------------------------------------------------------------------------------

//    /** Instantiate a new spooler for the specific source, region, set and time span */
//    public AskingPriceStatisticsTask(StatisticsEngine statisticsEngine, PriceAnalysisSampleDescriptor seriesDescriptor) {
//        //super(new HibernateNamedQueryImpl(QUERY_NAME));
//        super(null, null, null);  // todo: come back to this
//
//        this.descriptor = seriesDescriptor;
//        this.statisticsEngine = statisticsEngine;
//    }
//
//    /** Mandatory constructor for instantiation by Task */
//      public AskingPriceStatisticsTask() {
//          //super(new HibernateNamedQueryImpl(QUERY_NAME));
//          super(null, null, null);  // todo: come back to this
//      }

    // ------------------------------------------------------------------------------------------------------

//    /** Inject the statistics engine */
//    public void setStatisticsEngine(StatisticsEngine statisticsEngine) {
//        this.statisticsEngine = statisticsEngine;
//    }
//
//    /** Inject the series descriptor */
//    public void setDescriptor(PriceAnalysisSampleDescriptor descriptor) {
//        this.descriptor = descriptor;
//    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new series that will contain the prices */
//    protected void onStart() {
//        series = new UnivariateSeries(descriptor);
//
//        Date endDate = descriptor.getMonthOfYear().lastSecond();
//        Date startDate = descriptor.getInterval().firstSecond(endDate);
//
//        // todo: check this migration
//        getQuery().setParameter("type", ((AdvertisedDataSourceMemento) descriptor.getDataSource().getMemento()).getType()).
//              setParameter("region", descriptor.getRegion()).
//              setParameter("aggregateSet", descriptor.getAggregateSet()).
//              setParameter("startDate", startDate, TemporalType.DATE).
//              setParameter("endDate", endDate, TemporalType.DATE);
//
////        HibernateNamedQueryImpl searchCriteria = (HibernateNamedQueryImpl) getQuery();
////        searchCriteria.attachToSession((Session) session().getSessionImpl());
////
////        Query query = searchCriteria.getUnderlying();
////        query.setParameter("type", ((AdvertisedDataSourceMemento) descriptor.getDataSource().getMemento()).getType()).
////              setEntity("region", descriptor.getNamed()).
////              setEntity("aggregateSet", descriptor.getAggregateSet()).
////              setDate("startDate", startDate).
////              setDate("endDate", endDate);
//    }

    // ------------------------------------------------------------------------------------------------------

    /** Populate the Series for analysis */
    public void process(List queryResults) throws SpoolerException {

        for (AskingPrice price : (List<AskingPrice>) queryResults) {

            //todo: if price is a range, then estimate a suitable value...
            if (price != null) {
                series.add(price.getLowerPrice().amount());
            }

            //advertisement.print();
        }
    }

    // ------------------------------------------------------------------------------------------------------

//    // nothing to to
//    protected void onError(PersistenceServiceException e) {
//    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Called when all of the domain objects have been processed and the series extracted - the series can be
     * passed to the engine for computation.  Blocks until the result it available.
     **/
    public void onComplete(boolean aborted) {
        Statistics statistics;
        Future<ComputedResult> result = statisticsEngine.compute(series);
        try {
            statistics = (Statistics) result.get();

            if (statistics != null) {
                // persist the analysis results
                PriceAnalysis priceAnalysis = new PriceAnalysis(descriptor, statistics);
                LOG.info("Saving statistics...");
                em.persist(priceAnalysis);
                em.flush();
            }

        } catch(ExecutionException e) {
            LOG.error("Analysis failed: ", e);
        } catch(InterruptedException e) {
            LOG.error("Analysis failed: ", e);
        }
    }

    // ------------------------------------------------------------------------------------------------------


    public void onStart() {
         series = new UnivariateSeries(descriptor);
    }

    public boolean onError(SpoolerException e) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
