package com.blueskyminds.analysis.property.priceAnalysis;

import com.blueskyminds.framework.tasks.SimpleTask;
import com.blueskyminds.framework.datetime.TimePeriod;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysisDataSourceMemento;
import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Task the performs the statistical analysis for a Region and AgregateSet
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PriceAnalysisTask extends SimpleTask {

    private static final Log LOG = LogFactory.getLog(PriceAnalysisTask.class);

    private PersistenceService persistenceService;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PriceAnalysisTask with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public PriceAnalysisTask(String name, DataSource dataSource, RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod, PersistenceService persistenceService) {
        super(name);
        setMemento(new PriceAnalysisDataSourceMemento(dataSource, region, aggregateSet, timePeriod,  timespan, persistenceService));
        this.persistenceService = persistenceService;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Triggers the analysis process:
     *   creates and starts a Spooler for the analysis memento
     *
     * @return true if task completed
     */
    public boolean process() {
        boolean result = false;
        LOG.info("------ starting spooler("+getName()+") ---------");
        
        PersistenceSession session = null;
        try {
            session = persistenceService.openSession();

            PriceAnalysisDataSourceMemento memento = (PriceAnalysisDataSourceMemento) getMemento();
            StatisticsEngine statisticsEngine = AnalysisService.statisticsEngine();
            PriceAnalysisDescriptor descriptor = memento.toDescriptor();

            PriceAnalysisSpooler spooler = (PriceAnalysisSpooler) descriptor.getDataSource().createSpooler();
            // inject properties for the spooler
            spooler.setDescriptor(descriptor);
            spooler.setStatisticsEngine(statisticsEngine);

            // start the spooler
            spooler.start();
            result = true;
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (PersistenceServiceException e) {
                    //
                }
            }
        }

        return result;
    }
}
