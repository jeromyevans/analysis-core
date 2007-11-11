package com.blueskyminds.framework.analysis;

import com.blueskyminds.framework.test.BaseTestCase;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;

/**
 * Tests the ServiceLocator of the Analysis Module
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestAnalysisService extends BaseTestCase {


    public TestAnalysisService(String string) {
        super(string);
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the TestAnalysisService with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    public void testAnalysisService() {

        // this first call will get the statistics engine from the Analysis Service
        StatisticsEngine statisticsEngine = AnalysisService.statisticsEngine();
        assertNotNull(statisticsEngine);
        // this call will delegate to the root ServiceLocator instance
        PersistenceService gateway = AnalysisService.persistenceService();
        assertNotNull(gateway);

    }
}
