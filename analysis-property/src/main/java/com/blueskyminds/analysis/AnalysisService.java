package com.blueskyminds.analysis;

import com.blueskyminds.framework.ServiceLocator;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.yield.YieldEngine;

/**
 * A ServiceLocator for the Analysis framework
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AnalysisService extends ServiceLocator {

    /** Instance of the StatisticsEngine */
    private StatisticsEngine statisticsEngine;
    /** Instance of the YieldEngine */
    private YieldEngine yieldEngine;

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AnalysisService with default attributes
     */
    protected void templateInit() {
        super.templateInit();
        statisticsEngine = new StatisticsEngine();
        yieldEngine = new YieldEngine();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get an instance of the StatisticsEngine */
    public static StatisticsEngine statisticsEngine() {
        return instance(AnalysisService.class).statisticsEngine;
    }

    /** Get an instance of the YieldEngine */
    public static YieldEngine yieldEngine() {
        return instance(AnalysisService.class).yieldEngine;
    }
}
