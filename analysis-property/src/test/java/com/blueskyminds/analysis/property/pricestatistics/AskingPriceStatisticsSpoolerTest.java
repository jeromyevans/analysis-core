package com.blueskyminds.analysis.property.pricestatistics;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.AskingPriceTestCase;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 16/11/2007
 * <p/>
 * History:
 */
public class AskingPriceStatisticsSpoolerTest extends AskingPriceTestCase {

    private static final Log LOG = LogFactory.getLog(AskingPriceStatisticsTaskTest.class);

    /** Analysis a single sample or random price data from persistence */
    public void testTaskCreation() throws Exception {

        AnalysisDataSource dataSource = new AskingPriceDataSource("MostRecentEverything", "MostRecentEverything", PropertyAdvertisementTypes.PrivateTreaty);
        em.persist(dataSource);

        RegionHandle carlton = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        LOG.info("region: "+carlton);

        AggregateSet aggregateSet = aggregateSetDAO.findAggregateSet("houses");

        Interval interval = new Interval(2, PeriodTypes.Year);
        MonthOfYear monthOfYear = new MonthOfYear(0, 2007);

        PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(carlton, aggregateSet, interval, monthOfYear, dataSource);

        AskingPriceStatisticsSpooler entitySpooler = new AskingPriceStatisticsSpooler(descriptor, advertisementAnalysisDAO, statisticsEngine, em);

        entitySpooler.start();

        TestTools.printAll(PriceAnalysis.class, em);
    }
}
