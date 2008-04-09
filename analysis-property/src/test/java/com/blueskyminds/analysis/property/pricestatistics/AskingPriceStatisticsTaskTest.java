package com.blueskyminds.analysis.property.pricestatistics;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.analysis.property.AskingPriceTestCase;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.persistence.spooler.PageSpooler;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.landmine.core.property.AskingPrice;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Query;
import java.util.Date;

/**
 * Date Started: 15/11/2007
 * <p/>
 * History:
 */
public class AskingPriceStatisticsTaskTest extends AskingPriceTestCase {

    private static final Log LOG = LogFactory.getLog(AskingPriceStatisticsTaskTest.class);

    /** Analysis a single sample or random price data from persistence */
    public void testTaskCreation() throws Exception {
        PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(null, null, null, null, null);

        Query query = em.createQuery("select price from PropertyAdvertisement propertyAdvertisement");

        AskingPriceStatisticsTask spoolerTask = new AskingPriceStatisticsTask(em, descriptor, statisticsEngine);
        EntitySpooler entitySpooler = new EntitySpooler<AskingPrice>(propertyDAO, query, spoolerTask);
        entitySpooler.addListener(spoolerTask);

        entitySpooler.start();

        TestTools.printAll(PriceAnalysis.class, em);
    }

    /** Analysis within a region and AggregateSet using the DAO's pager */
    public void testMostRecentPriceAnalysis() throws Exception {
        PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(null, null, null, null, null);

        RegionHandle carlton = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        LOG.info("region: "+carlton);
        AggregateSet aggregateSet = aggregateSetDAO.findAggregateSet("houses");
        Date startDate = DateTools.createDate(2004, 1, 1, 0, 0,0);
        Date endDate = DateTools.createDate(2006, 1, 1, 0, 0,0);

        AskingPriceStatisticsTask spoolerTask = new AskingPriceStatisticsTask(em, descriptor, statisticsEngine);
        PageSpooler spooler = new PageSpooler<AskingPrice>(advertisementAnalysisDAO.pageMostRecentPrice(PropertyAdvertisementTypes.PrivateTreaty,
                carlton, aggregateSet, startDate, endDate), spoolerTask);
        spooler.addListener(spoolerTask);

        spooler.start();

        TestTools.printAll(PriceAnalysis.class, em);
    }

    /** Analysis with a property series descriptor over a time period  */
    public void testMostRecentPriceAnalysisDescriptorOverTimePeriod() throws Exception {

        // todo: datasource may be redundant, although i do need something
        AnalysisDataSource dataSource = new AnalysisDataSource("MostRecentEverything", "MostRecentEverything");
        em.persist(dataSource);

        RegionHandle carlton = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        LOG.info("region: "+carlton);
        AggregateSet aggregateSet = aggregateSetDAO.findAggregateSet("houses");
        Date startDate = DateTools.createDate(2004, 1, 1, 0, 0,0);
        Date endDate = DateTools.createDate(2006, 1, 1, 0, 0,0);
        Interval interval = new Interval(3, PeriodTypes.Month);
        MonthOfYear currentPeriod = new MonthOfYear(4, 2005);
        MonthOfYear finalPeriod = new MonthOfYear(1, 2007);
        while (currentPeriod.compareTo(finalPeriod) < 0) {
            PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(carlton, aggregateSet, interval, currentPeriod, dataSource);

            AskingPriceStatisticsTask spoolerTask = new AskingPriceStatisticsTask(em, descriptor, statisticsEngine);
            PageSpooler spooler = new PageSpooler<AskingPrice>(advertisementAnalysisDAO.pageMostRecentPrice(PropertyAdvertisementTypes.PrivateTreaty,
                    carlton, aggregateSet, startDate, endDate), spoolerTask);
            spooler.addListener(spoolerTask);

            spooler.start();

            currentPeriod = currentPeriod.add(1);
        }


        TestTools.printAll(PriceAnalysis.class, em);
    }

     /** Analysis of all aggregate sets within a region  */
    public void testMostRecentPriceAggregateSetGroup() throws Exception {

        // todo: datasource may be redundant, although i do need something
        AnalysisDataSource dataSource = new AnalysisDataSource("MostRecentEverything", "MostRecentEverything");
        em.persist(dataSource);

        RegionHandle carlton = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        LOG.info("region: "+carlton);

        AggregateSetGroup group = aggregateSetDAO.findAggregateSetGroup(PropertyAnalysisTestTools.GROUP_NAME);

        for (AggregateSet aggregateSet : group.getAggregateSets()) {
            Date startDate = DateTools.createDate(2004, 1, 1, 0, 0,0);
            Date endDate = DateTools.createDate(2006, 1, 1, 0, 0,0);
            Interval interval = new Interval(2, PeriodTypes.Year);
            MonthOfYear monthOfYear = new MonthOfYear(0, 2007);
            PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(carlton, aggregateSet, interval, monthOfYear, dataSource);

            AskingPriceStatisticsTask spoolerTask = new AskingPriceStatisticsTask(em, descriptor, statisticsEngine);

            PageSpooler spooler = new PageSpooler<AskingPrice>(advertisementAnalysisDAO.pageMostRecentPrice(PropertyAdvertisementTypes.PrivateTreaty, carlton, aggregateSet, startDate, endDate), spoolerTask);
            spooler.addListener(spoolerTask);

            spooler.start();
        }

        TestTools.printAll(PriceAnalysis.class, em);
    }
}
