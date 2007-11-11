package com.blueskyminds.analysis.property.priceAnalysis;

import com.blueskyminds.landmine.core.property.AskingPrice;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.datetime.TimePeriod;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.series.UnivariateSeries;
import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysis;
import com.blueskyminds.analysis.property.advertised.AdvertisedDataSourceMemento;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.core.statistics.Statistics;
import com.blueskyminds.analysis.engine.ComputedResult;
import com.blueskyminds.framework.persistence.spooler.DomainObjectSpooler;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;

import java.util.List;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.TemporalType;

/**
 * Spools prices from property advertisements into the ComputeEngine for analysis
 *
 * Date Started: 28/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PriceAnalysisSpooler extends DomainObjectSpooler {

    private static final Log LOG = LogFactory.getLog(PriceAnalysisSpooler.class);
    private static final String QUERY_NAME = "propertyAdvertisement.mostRecentPrice";

    private PriceAnalysisDescriptor descriptor;
    private UnivariateSeries series;
    private StatisticsEngine statisticsEngine;

    // ------------------------------------------------------------------------------------------------------

    /** Instantiate a new spooler for the specific source, region, set and time span */
    public PriceAnalysisSpooler(StatisticsEngine statisticsEngine, DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod) {
        //super(new HibernateNamedQueryImpl(QUERY_NAME));
        super(null, null, null);  // todo: come back to this
        this.descriptor = new PriceAnalysisDescriptor(region, aggregateSet, timespan, timePeriod, dataSource);
        this.statisticsEngine = statisticsEngine;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Instantiate a new spooler for the specific source, region, set and time span */
    public PriceAnalysisSpooler(StatisticsEngine statisticsEngine, PriceAnalysisDescriptor seriesDescriptor) {
        //super(new HibernateNamedQueryImpl(QUERY_NAME));
        super(null, null, null);  // todo: come back to this

        this.descriptor = seriesDescriptor;
        this.statisticsEngine = statisticsEngine;
    }

    /** Mandatory constructor for instantiation by Task */
      public PriceAnalysisSpooler() {
          //super(new HibernateNamedQueryImpl(QUERY_NAME));
          super(null, null, null);  // todo: come back to this
      }

    // ------------------------------------------------------------------------------------------------------

    /** Inject the statistics engine */
    public void setStatisticsEngine(StatisticsEngine statisticsEngine) {
        this.statisticsEngine = statisticsEngine;
    }

    /** Inject the series descriptor */
    public void setDescriptor(PriceAnalysisDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new series that will contain the prices */
    protected void onStart() {
        series = new UnivariateSeries(descriptor);

        Date endDate = descriptor.getTimePeriod().lastSecond();
        Date startDate = descriptor.getTimespan().firstSecond(endDate);

        // todo: check this migration
        getQuery().setParameter("type", ((AdvertisedDataSourceMemento) descriptor.getDataSource().getMemento()).getType()).
              setParameter("region", descriptor.getRegion()).
              setParameter("aggregateSet", descriptor.getAggregateSet()).
              setParameter("startDate", startDate, TemporalType.DATE).
              setParameter("endDate", endDate, TemporalType.DATE);

//        HibernateNamedQueryImpl searchCriteria = (HibernateNamedQueryImpl) getQuery();
//        searchCriteria.attachToSession((Session) session().getSessionImpl());
//
//        Query query = searchCriteria.getUnderlying();
//        query.setParameter("type", ((AdvertisedDataSourceMemento) descriptor.getDataSource().getMemento()).getType()).
//              setEntity("region", descriptor.getNamed()).
//              setEntity("aggregateSet", descriptor.getAggregateSet()).
//              setDate("startDate", startDate).
//              setDate("endDate", endDate);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Populate the Series for analysis */
    protected void process(List queryResults) throws SpoolerException {

        for (AskingPrice price : (List<AskingPrice>) queryResults) {

            //todo: if price is a range, then estimate a suitable value...
            if (price != null) {
                series.add(price.getLowerPrice().amount());
            }

            //advertisement.print();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    // nothing to to
    protected void onError(PersistenceServiceException e) {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Called when all of the domain objects have been processed and the series extracted - the series can be
     * passed to the engine for computation.  Blocks until the result it available. */
    protected void onComplete() {
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
}
