package com.blueskyminds.analysis.property.yield;

import com.blueskyminds.framework.tasks.SimpleTask;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.analysis.core.datasource.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Performs the analysis of estimated Yield for regions and aggregate sets
 *
 * Date Started: 9/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class YieldAnalysisTask extends SimpleTask {

    private static final Log LOG = LogFactory.getLog(YieldAnalysisTask.class);

    private PersistenceService persistenceService;

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Creates a task that performs YieldAnalysis for the sales and rentals source and timespan */
    public YieldAnalysisTask(String name, DataSource salesDataSource, DataSource rentalsDataSource, Timespan timespan) {
        super(name);
        setMemento(new YieldAnalysisDataSourceMemento(salesDataSource, rentalsDataSource, timespan));
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

            YieldAnalysisDataSourceMemento memento = (YieldAnalysisDataSourceMemento) getMemento();
            DataSource salesDataSource = memento.realizeSalesDataSource();
            DataSource rentalsDataSource = memento.realizeRentalsDataSource();
// todo: enable
            //YieldAnalysisSpooler spooler = new YieldAnalysisSpooler(persistenceService, salesDataSource, rentalsDataSource, memento.getTimespan());

            // start the spooler
            //spooler.start();
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
