package com.blueskyminds.framework.analysis;

import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.test.DbTestCase;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.paging.Page;
import com.blueskyminds.framework.tasks.TaskPlan;
import com.blueskyminds.framework.tasks.TaskGroup;
import com.blueskyminds.framework.tasks.ExecutorProvider;
import com.blueskyminds.framework.tasks.SimpleExecutorProvider;
import com.blueskyminds.framework.datetime.*;
import com.blueskyminds.analysis.core.statistics.StatisticsWorker;
import com.blueskyminds.analysis.core.statistics.Statistics;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.engine.ComputedResult;
import com.blueskyminds.analysis.engine.ComputeEngine;
import com.blueskyminds.analysis.property.*;
import com.blueskyminds.analysis.property.yield.YieldEngine;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysisSpooler;
import com.blueskyminds.analysis.property.advertised.AdvertisedDataSourceMemento;
import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysis;
import com.blueskyminds.analysis.property.priceAnalysis.PriceAnalysisTask;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.series.UnivariateSeries;
import com.blueskyminds.analysis.core.series.AggregateSeries;
import com.blueskyminds.analysis.core.series.Pair;
import com.blueskyminds.analysis.core.series.BivariateSeries;
import com.blueskyminds.landmine.core.property.*;
import com.blueskyminds.enterprise.address.Suburb;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.enterprise.regionx.country.CountryHandle;
import com.blueskyminds.framework.tools.DebugTools;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import java.util.*;
import java.util.concurrent.Future;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.lang.management.MemoryMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

/**
 *
 * Test the DataSeries aggregation classes
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class TestAnalysis extends DbTestCase {

    private static final Log LOG = LogFactory.getLog(TestAnalysis.class);

    public TestAnalysis(String string) {
        super(string);
    }

    public void testAggregateSets() {
        new AnalysisTestTools(getPersistenceService()).initialiseAnalysisSets();

        TestTools.printAll(AggregateSet.class, getPersistenceService());
    }

    public void testStatsLibrary() {
        DescriptiveStatistics stats1 = DescriptiveStatistics.newInstance();
        stats1.addValue(10);
        stats1.addValue(20);
        stats1.addValue(30);
        stats1.addValue(40);
        stats1.addValue(50);

        double mean = stats1.getMean();

    }

    // ------------------------------------------------------------------------------------------------------

    private void analyseData(RegionOLD region, AggregateSet aggregateSet) {

    }

    public void testPagination() {
        int pageNo = 0;
        Page page;
        boolean lastPage = false;
        new AnalysisTestTools(getPersistenceService()).generateRandomAdvertisements(1000);
        try {
            PersistenceService gateway = getPersistenceService();
            PersistenceSession ps = gateway.openSession();

            while (!lastPage) {
                page = gateway.findPage(PropertyAdvertisement.class, pageNo, 20);
                System.out.println(pageNo);
                lastPage = !page.hasNextPage();
                pageNo++;
            }

            /*Session session = (Session) ps.getSessionImpl();
            while (!lastPage) {
                page = new HibernatePageImpl(session.createQuery("from PropertyAdvertisement"), pageNo, 20);
                System.out.println(pageNo);
                lastPage = !page.hasNextPage();
                pageNo++;
            }*/
            ps.close();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void testDataSeries() {
//        DataSeries dataSeries = new DataSeries();
//        AnalysisTestTools.initialiseAnalysisSets();
//        AnalysisTestTools.generateRandomAdvertisements(100);

        // generate price analysis statistics on the random advertisements:

        // for each AnalysisSet...
        // get the advertisements that fit in this analysis set .... no that, will be SLOW

        // ALTERNATIVE (slow...but may be able to be done concurrently)
        // for each region...
        //  for each analysis set...
        //   for each timeperiod...
        //    page through ALL of the properties
        //      if the property fits in this set...
        //         transfer the APPROPRIATE price values into the in-memory dataseries (does it have to be in-memory to calculate median? YES)
        //   calculate the statistics for the series (region|aggregate|dataSource|timeperiod)

        // (the one above could be run in multiple processes/threads/processors)

        // ALTERNATIVE:
        //  create an index for each analysis set the persists which properties belong in the set
        //  a single paged query can get all of the properties in the set and recalculate the statistics
        // (NOT SCALABLE)

        // ALTERNATIVE
        // page through all the properties
        //  determine which analysisSets the property belongs to
        //  for each set...
    }

    // ------------------------------------------------------------------------------------------------------

    private UnivariateSeries generateRandomSeries(int size) {
        UnivariateSeries series = new UnivariateSeries(null);
        MathContext mc = new MathContext(18, RoundingMode.HALF_UP);
        int d;
        RandomData r = new RandomDataImpl();
        for (int i = 0; i < size; i++) {
            d = ((Double) r.nextGaussian(1000.0, 50.0)).intValue();
            series.add(new BigDecimal(d, mc));
        }
        return series;
    }


    private UnivariateSeries generateTestSeries() {

        BigDecimal[] values = { new BigDecimal(7), new BigDecimal(9), new BigDecimal(-2), new BigDecimal(-9), new BigDecimal(2)};
        UnivariateSeries series = new UnivariateSeries(null, values);

        return series;
    }

        // ------------------------------------------------------------------------------------------------------

    public void testStatisticsWorkerExact() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();
        UnivariateSeries s = generateTestSeries();
        Statistics result = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result);
        assertEquals(new BigDecimal(9), result.getMax());
        assertEquals(new BigDecimal(-9), result.getMin());
        assertEquals((Integer) 5, (Integer) result.getSize());
        assertEquals(new BigDecimal("1.4"), result.getMean());
        assertEquals(new BigDecimal("7.23187389"), result.getStdDev().round(new MathContext(9, RoundingMode.HALF_UP)));
        assertNull(result.getMode());
        assertEquals(new BigDecimal("2"), result.getMedian());
    }

    // ------------------------------------------------------------------------------------------------------

    public void testStatisticsWorker2() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();
        UnivariateSeries s = generateRandomSeries(100000);
        Statistics result = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result);
    }

    public void testStatisticsWorkerReuse() {
        StatisticsWorker statisticsWorker = new StatisticsWorker();

        // use the worker  multiple times
        UnivariateSeries s = generateRandomSeries(100000);
        Statistics result1 = (Statistics) statisticsWorker.compute(s);
        s = generateRandomSeries(100000);
        Statistics result2 = (Statistics) statisticsWorker.compute(s);
        s = generateRandomSeries(100000);
        Statistics result3 = (Statistics) statisticsWorker.compute(s);
        s = generateRandomSeries(100000);
        Statistics result4 = (Statistics) statisticsWorker.compute(s);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Returns true if there's not much heap left */
    private boolean almostOutOfHeap() {
        Runtime.getRuntime().gc();

        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage memUsage = mem.getHeapMemoryUsage();
        Long used = memUsage.getUsed()/1000;
        Long max= memUsage.getMax()/1000;
        double percent = used.doubleValue()/max*100.0;
        return (percent > 80.0);
    }

    private static final int SIZE = 100000;

    /** Feed random series into the compute engine */
    private List<Future<ComputedResult>> feedEngine(ComputeEngine engine, int maxCount) {
        UnivariateSeries series;
        List<Future<ComputedResult>> futureResults = new LinkedList<Future<ComputedResult>>();

        LOG.info("Submitting up to "+maxCount+" series to the engine...");
        int submitted = 0;
        while ((submitted < maxCount) && (!almostOutOfHeap())) {
            series = generateRandomSeries(SIZE);
            futureResults.add(engine.compute(series));
            DebugTools.printAvailableHeap();
            submitted++;
        }

        return futureResults;
    }

    public void testStatisticsEngine() {
        int count = 200;
        int loops = 3;
        int currentLoop = 0;
        StatisticsEngine engine = new StatisticsEngine();

        for (currentLoop = 0; currentLoop < loops; currentLoop++) {
            List<Future<ComputedResult>> futureResults = feedEngine(engine, count);
            int submitted = futureResults.size();

            LOG.info("Waiting for "+submitted+" results...");

            int completed;
            boolean waiting = true;
            while (waiting) {
                completed = 0;
                for (Future<ComputedResult> future : futureResults) {
                    if (future.isDone()) {
                        completed++;
                    }
                }
                LOG.info(completed +" completed");
                if (completed == submitted) {
                    waiting = false;
                } else {
                    // sleep a bit
                    try {
                        Thread.sleep(200);
                        DebugTools.printAvailableHeap();
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    public void testAggregateSeries() {
       int count = 5;
       StatisticsEngine engine = new StatisticsEngine();

       AggregateSeries aggregateSeries = new AggregateSeries(null);

       for (int i = 0; i < count; i++) {
           if (!almostOutOfHeap()) {
               aggregateSeries.add(generateRandomSeries(SIZE));
           }
       }

       List<Future<ComputedResult>> futureResults = engine.computeAll(aggregateSeries);

       int submitted = futureResults.size();

       LOG.info("Waiting for "+submitted+" results...");

       int completed;
       boolean waiting = true;
       while (waiting) {
           completed = 0;
           for (Future<ComputedResult> future : futureResults) {
               if (future.isDone()) {
                   completed++;
               }
           }
           LOG.info(completed +" completed");
           if (completed == submitted) {
               waiting = false;
           } else {
               // sleep a bit
               try {
                   Thread.sleep(200);
                   DebugTools.printAvailableHeap();
               } catch (InterruptedException e) {
                   //
               }
           }
       }
   }

    /** Test the tool that generates a random property */
    public void testRandomProperty() {
        int count = 20;
        List<Premise> properties = new LinkedList<Premise>();
        for (int i = 0; i < count; i++) {
            properties.add(new AnalysisTestTools(getPersistenceService()).generateRandomProperty(null));
        }

        try {
            PersistenceService gateway = getPersistenceService();
            PersistenceSession ps = gateway.openSession();

            for (Premise property : properties) {
                gateway.save(property);
                property.print();
            }

            ps.close();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }



    /** Test the tool that generates a random property and adds an advertisement for each one*/
    public void testRandomPropertyAdvertisement() {
        new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(20, 1990, 2005);
        TestTools.printAll(PropertyAdvertisement.class, getPersistenceService());
    }

    /** Test the algorithm that maps a property to one or more regions */
    public void testMapPropertyToRegion() throws Exception {
        Premise property = new AnalysisTestTools(getPersistenceService()).generateRandomProperty(null);
        CountryHandle australia = new AddressDAO(getPersistenceService()).findCountry("AUS");
// todo: enable
//        PropertyToRegionSpooler propertyToRegionSpooler = new PropertyToRegionSpooler(getPersistenceService(), australia);
//        Set<Region> regions = propertyToRegionSpooler.mapPropertyToRegions(property);

//        assertNotNull(regions);
        // assert that the property is in 4 regions: suburb, postcode, state and country
//        assertEquals(4, regions.size());
    }

    public void testRegionComputer() {
        new AnalysisTestTools(getPersistenceService()).generateRandomProperties(20);

        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();

        TestTools.printAll(PremiseRegionMap.class, getPersistenceService());
    }

    public void testAggregateSetComputer() {

        new AnalysisTestTools(getPersistenceService()).generateRandomProperties(20);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        TestTools.printAll(PremiseAggregateSetMap.class, getPersistenceService());
    }

    private void doQuery(String query) {
        PersistenceService gateway = getPersistenceService();
        try {
            PersistenceSession ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();

            List<Premise> properties = session.createQuery(query).list();

            if (properties != null) {
                LOG.info("Found "+properties.size()+" properties");

                for (Premise property : properties) {
                    property.print();
                }
            } else {
                LOG.info("Found "+properties.size()+" properties");
                fail();
            }
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * @param queryStr
     * @param params needs to be of the form: name, value, name, value, name, value
     */
    private void doQuery(String queryStr, Object... params) {
        PersistenceService gateway = getPersistenceService();
        int index;
        try {
            PersistenceSession ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(queryStr);

            index = 0;
            while (index < params.length) {
                query.setParameter((String) params[index], params[index+1]);
                index += 2;
            }

            List<Premise> properties = query.list();

            if (properties != null) {
                LOG.info("Found "+properties.size()+" properties");

                for (Premise property : properties) {
                    property.print();
                }
            } else {
                LOG.info("Found 0 properties");
                fail();
            }
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * @param queryStr
     * @param params needs to be of the form: name, value, name, value, name, value
     */
    private List<Object> doGenericQuery(String queryStr, Object... params) {
        PersistenceService gateway = getPersistenceService();
        int index;
        List<Object> results = null;

        try {
            PersistenceSession ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(queryStr);

            index = 0;
            while (index < params.length) {
                query.setParameter((String) params[index], params[index+1]);
                index += 2;
            }

            results = query.list();

            if (results != null) {
                LOG.info("Found "+results.size()+" results");
            } else {
                LOG.info("Found 0 results");
                fail();
            }
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }
        return results;
    }

    public void testQueries() {
        new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(20, 1990, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        // all properties with advertisements (BROKEN)
        //String query1 = "from RealProperty as realProperty left join PropertyAdvertisement as propertyAdvertisement where propertyAdvertisement.premise = realProperty";

        // get all properties that have an advertisement (from advertisements)
        String query1 = "select advertisement.premise from PropertyAdvertisement as advertisement where advertisement.premise is not null";

        // get all properties that have an advertisement since 2000
        String query2 = "select advertisement.premise from PropertyAdvertisement as advertisement where advertisement.premise is not null and advertisement.dateListed >= '2000-01-01 00:00:00'";

        // get all properties that have an advertisement between 2000 and 2003
        String query3 = "select advertisement.premise from PropertyAdvertisement as advertisement where advertisement.premise is not null and advertisement.dateListed between '2000-01-01 00:00:00' and '2003-12-31 23:59:59'";

        // get all properties that have an advertisement and are in a region (BROKEN)
        //String query4 = "from RealProperty as realProperty inner join realProperty.propertyRegionMaps as propertyRegionMaps with propertyRegionMaps.region.id = '1'";

        // creates a cross producted (properties * regions) then returns the properties (eg. if 20 properties, 80 results)
        //String query4 = "select realProperty from RealProperty as realProperty, PropertyRegionMap as propertyRegionMap where propertyRegionMap.premise = realProperty";
        //TestTools.printAll(Region.class);

        //String query5 = "select propertyRegionMap.premise from PropertyRegionMap as propertyRegionMap inner join RealProperty as realProperty with ";
        //String query4 = "select propertyRegionMap.premise from PropertyRegionMap as propertyRegionMap inner join PropertyAdvertisement as propertyAdvertisement where propertyRegionMap.premise = propertyAdvertisment.premise";

        RegionOLD nsw = null;
        try {
            nsw = getPersistenceService().findById(RegionOLD.class, 99L);
        } catch(PersistenceServiceException e) {
            fail();
        }

        // get all properties in a particular region (NSW)
        String query4 = "select propertyRegionMap.premise from PropertyRegionMap as propertyRegionMap where propertyRegionMap.region = :region";

//        doQuery(query1);
//        doQuery(query2);
//        doQuery(query3);
//        doQuery(query4);
        //doQuery(query4, "region", nsw);

        // get all properties with advertisements in the region using a sub select
        String query5 = "select propertyAdvertisement.premise from PropertyAdvertisement as propertyAdvertisement where propertyAdvertisement.premise in (select propertyRegionMap.premise from PropertyRegionMap as propertyRegionMap where propertyRegionMap.region = :region)";
        doQuery(query5, "region", nsw);

        // get all properties with advertisements in the region using a cross join
        String query6 = "select propertyAdvertisement.premise " +
                "from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap " +
                "where propertyAdvertisement.premise = propertyRegionMap.premise " +
                "and propertyRegionMap.region = :region)";
        //doQuery(query6, "region", nsw);

        AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, 1L);
        } catch(PersistenceServiceException e) {
            fail();
        }

        // get all properties with advertisements in the region and aggregate set using a cross join
        String query7 = "select propertyAdvertisement.premise " +
                "from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                "where propertyAdvertisement.premise = propertyRegionMap.premise " +
                "and propertyAdvertisement.premise = propertyAggregateSetMap.premise " +
                "and propertyRegionMap.region = :region "+
                "and propertyAggregateSetMap.aggregateSet = :aggregateSet";

        //doQuery(query7, "region", nsw, "aggregateSet", aggregateSet);


        // get all properties with advertisements in the region and aggregate set using an inner join (generates 400 results)
        /*String query8 = "select propertyAdvertisement.premise from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                        "inner join propertyAdvertisement.premise " +
                        "inner join propertyRegionMap.premise " +
                        "inner join propertyAggregateSetMap.premise " +
                        "where propertyRegionMap.region = :region "+
                        "and propertyAggregateSetMap.aggregateSet = :aggregateSet";
        doQuery(query8, "region", nsw, "aggregateSet", aggregateSet);*/

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);

        Date endDate = timePeriod.lastSecond();
        Date startDate = timespan.firstSecond(endDate);

        LOG.info("startDate = "+startDate);
        LOG.info("endDate = "+endDate);
        // get all properties that have an advertisement between for a timespan and timeperiod
        String query9 = "select advertisement.premise from PropertyAdvertisement as advertisement where advertisement.premise is not null and advertisement.dateListed between :startDate and :endDate";
        //doQuery(query9, "startDate", startDate, "endDate", endDate);

        // subquery to get only the latest property advertisement - works, but can't be used in in clause
        //String query10 = "select advertisement.id, max(advertisement.dateListed) as lastUpdate from PropertyAdvertisement as advertisement group by advertisement.id";
        //String query10 = "select advertisement.premise.id, max(advertisement.dateListed) as lastUpdate from PropertyAdvertisement as advertisement where advertisement.dateListed between :startDate and :endDate group by advertisement.premise.id";
        //String query10 = "select advertisement.id, advertisement.premise.id, max(advertisement.dateListed) as lastUpdate from PropertyAdvertisement as advertisement where advertisement.dateListed between :startDate and :endDate group by advertisement.premise.id";
        // this alternative approach works better (probably slower though)
        LOG.info("---[Distinct]--------------");

        String query10 = "select distinct advertisement.id from PropertyAdvertisement as advertisement where advertisement.dateListed between :startDate and :endDate order by advertisement.dateListed desc";
        List<Object> results = doGenericQuery(query10, "startDate", startDate, "endDate", endDate);

        // first get the max dateListed in the aggregation...
        LOG.info("-----------------");
        String query11 = "select advertisement.premise.id, max(advertisement.dateListed) as lastUpdate from PropertyAdvertisement as advertisement where advertisement.dateListed between :startDate and :endDate group by advertisement.premise.id";
        List<Object> results1 = doGenericQuery(query11, "startDate", startDate, "endDate", endDate);

        // then restrict the query to the advertisements that equal the max value...
        String query12 = "select propertyAdvertisement.id from PropertyAdvertisement as propertyAdvertisement " +
                         "left join ("+query11+")";
         List<Object> results2 = doGenericQuery(query12, "startDate", startDate, "endDate", endDate);

        // can't use max like this in a having clause
        //String query10 = "select advertisement.id from PropertyAdvertisement as advertisement group by advertisement.id having max(advertisement.dateListed)";

    }

    /**
     * Lookup properties with an advertisement during the timespan for a specific region and aggregateSet
     *
     * The problem with this is that the property may have multiple advertisements in the period and a separate query
     *  would be required to find the most recent advertisement
     *
     * @param region
     * @param aggregateSet
     * @param timespan
     * @param timePeriod
     * @return list of properties matching the criteria
     */
    public List<Premise> lookupProperties(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod) {
        String queryString = "select propertyAdvertisement.premise " +
                "from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                "where propertyAdvertisement.premise = propertyRegionMap.premise " +
                "and propertyAdvertisement.premise = propertyAggregateSetMap.premise " +
                "and propertyRegionMap.region = :region "+
                "and propertyAggregateSetMap.aggregateSet = :aggregateSet "+
                "and propertyAdvertisement.dateListed between :startDate and :endDate";

        List<Premise> properties = null;
        PersistenceService gateway = getPersistenceService();

        Date endDate = timePeriod.lastSecond();
        Date startDate = timespan.firstSecond(endDate);
        try {
            PersistenceSession ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(queryString);

            query.setParameter("region", region);
            query.setParameter("aggregateSet", aggregateSet);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            // perform the query
            properties = query.list();

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }

        return properties;
    }

    // see note above - it's better to lookup the advertisements, not the properties
    public void testLookupProperties() {
        new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(50, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        TestTools.printAll(AggregateSet.class, getPersistenceService());

        AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }

        RegionOLD nsw = null;
        try {
            nsw = getPersistenceService().findById(RegionOLD.class, 99L);
        } catch(PersistenceServiceException e) {
            fail();
        }

        // lookup all properties in the region and aggregate set for the timeperiod and timespan
        List<Premise> properties = lookupProperties(nsw, aggregateSet, new Timespan(4, PeriodTypes.Year), new TimePeriod(Calendar.DECEMBER, 2005));
        if (properties != null) {
            LOG.info("Found "+properties.size()+" properties");

            for (Premise property : properties) {
                property.print();
            }
        } else {
            LOG.info("Found 0 properties");
            fail();
        }
    }

    /**
     * Lookup advertisements for properties featuring an during the timespan for a specific region and aggregateSet
     *
     *  Returns the most recent advertisement for each property during the timespan
     *
     * @param region
     * @param aggregateSet
     * @param timespan
     * @param timePeriod
     * @return list of advertisements for properties matching the criteria
     */
    public List<PropertyAdvertisement> lookupAdvertisments(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod) {
        String queryString = "" +
                "from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                "where propertyAdvertisement.premise = propertyRegionMap.premise " +
                "and propertyAdvertisement.premise = propertyAggregateSetMap.premise " +
                "and propertyRegionMap.region = :region "+
                "and propertyAggregateSetMap.aggregateSet = :aggregateSet "+
                "and propertyAdvertisement.id in (select distinct advertisement.id from PropertyAdvertisement as advertisement where advertisement.dateListed between :startDate and :endDate order by advertisement.dateListed desc)";

        List<PropertyAdvertisement> advertisements = null;
        PersistenceService gateway = getPersistenceService();

        Date endDate = timePeriod.lastSecond();
        Date startDate = timespan.firstSecond(endDate);
        try {
            PersistenceSession ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(queryString);

            query.setParameter("region", region);
            query.setParameter("aggregateSet", aggregateSet);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            // perform the query
            advertisements = query.list();

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }

        return advertisements;
    }

    public void testLookupAdvertisements() {
        new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(50, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }

        RegionOLD nsw = null;
        try {
            nsw = getPersistenceService().findById(RegionOLD.class, 99L);
        } catch(PersistenceServiceException e) {
            fail();
        }

        // lookup all advertisements for properties in the region and aggregate set for the timeperiod and timespan
        List<PropertyAdvertisement> advertisements = lookupAdvertisments(nsw, aggregateSet, new Timespan(4, PeriodTypes.Year), new TimePeriod(Calendar.DECEMBER, 2005));
        if (advertisements != null) {
            LOG.info("Found "+advertisements.size()+" advertisements");

            for (PropertyAdvertisement advertisement : advertisements) {
                advertisement.print();
            }
        } else {
            LOG.info("Found 0 advertisements");
            fail();
        }

    }

    public Session openSession() throws PersistenceServiceException {
        PersistenceService gateway = getPersistenceService();
        PersistenceSession ps = gateway.openSession();
        return (Session) ps.getSessionImpl();
    }

    public void testNativeSql() {
        new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(50, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        try {
            Timespan timespan = new Timespan(4, PeriodTypes.Year);
            TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);

            Date endDate = timePeriod.lastSecond();
            Date startDate = timespan.firstSecond(endDate);

            LOG.info("startDate = "+startDate);
            LOG.info("endDate = "+endDate);

            AggregateSet aggregateSet = null;
            try {
                aggregateSet = getPersistenceService().findById(AggregateSet.class, 2L); // houses
            } catch(PersistenceServiceException e) {
                fail();
            }

            RegionOLD region = null;
            try {
                region = getPersistenceService().findById(RegionOLD.class, 99L);     // nsw
            } catch(PersistenceServiceException e) {
                fail();
            }

            Session session = openSession();
            String queryStr1 = "select propertyId, max(dateListed) as lastUpdate from PropertyAdvertisement where dateListed between :startDate and :endDate group by propertyId";

            SQLQuery query = session.createSQLQuery(queryStr1);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            List<Object> results = query.list();

            DebugTools.printCollection(results);
// working version:
//            String queryStr2 = "select {advertisement.*} from PropertyAdvertisement advertisement " +
//                    " join ( "+queryStr1+" ) mostRecentAd " +
//                    " on (advertisement.propertyId = mostRecentAd.propertyId and advertisement.dateListed = mostRecentAd.lastUpdate)";

            String queryStr2 = "select {advertisement.*} from PropertyAdvertisement advertisement " +
                    "join ( "+queryStr1+" ) mostRecentAd on (advertisement.propertyId = mostRecentAd.propertyId and advertisement.dateListed = mostRecentAd.lastUpdate) " +
                    "join PropertyRegionMap propertyRegionMap on (advertisement.propertyId = propertyRegionMap.propertyId) " +
                    "join PropertyAggregateSetMap propertyAggregateSetMap on (advertisement.propertyId = propertyAggregateSetMap.propertyId) " +
                    "where propertyRegionMap.regionId = :region " +
                    "and propertyAggregateSetMap.aggregateSetId = :aggregateSet";

            SQLQuery query2 = session.createSQLQuery(queryStr2);
            query2.setParameter("startDate", startDate);
            query2.setParameter("endDate", endDate);
            query2.setParameter("region", region);
            query2.setParameter("aggregateSet", aggregateSet);
            query2.addEntity("advertisement", PropertyAdvertisement.class);
            List<Object> results2 = query2.list();

            DebugTools.printCollection(results2);
            for (Object advertisement : results2) {
                ((PropertyAdvertisement) advertisement).print();
            }

            session.getTransaction().commit();

        } catch(PersistenceServiceException e) {
            fail();
        }
    }

    public void testAdvertisementSpooler() {
        new AnalysisTestTools(getPersistenceService()).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(getPersistenceService()).findRegionByName("Neutral Bay");
        // todo: broken - repair for new region implementation
        //new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 50, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);
        AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }

        LOG.info("------ starting statistics engine ---------");
        StatisticsEngine statisticsEngine = new StatisticsEngine();

        DataSource dataSource = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
        LOG.info("------ starting spooler ---------");
        PriceAnalysisSpooler spooler = new PriceAnalysisSpooler(statisticsEngine, dataSource, region, aggregateSet, timespan, timePeriod);
        spooler.start();
    }

    // ------------------------------------------------------------------------------------------------------

    public void testDataSourcePersistence() {
        DataSource dataSource = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));

        PersistenceService gateway = getPersistenceService();
        try {
            gateway.save(dataSource);

            DataSource result = gateway.findById(DataSource.class, dataSource.getId());

            result.print();
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }


    }

    // ------------------------------------------------------------------------------------------------------

    public void testAnalysisTasks() {

        DebugTools.printAvailableHeap();

        new AnalysisTestTools(getPersistenceService()).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(getPersistenceService()).findRegionByName("Neutral Bay");

        // todo: broken - repair for new region implementation
        //new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(region, null, 5000, 2004, 2005);
        DebugTools.printAvailableHeap();

        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        DebugTools.printAvailableHeap();

        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        DebugTools.printAvailableHeap();

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);
        /*AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }*/

        //Region region = AnalysisTestTools.findRegionByName("New South Wales");

        DebugTools.printAvailableHeap();

        PersistenceService gateway = getPersistenceService();
        DataSource dataSource = null;
        try {
            dataSource = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
            gateway.save(dataSource);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }

        TestTools.printAll(DataSource.class, getPersistenceService());

        TaskPlan taskPlan = new TaskPlan("Analysis Tasks");
        TaskGroup all = new TaskGroup("All");
        taskPlan.setRootTask(all);

        //AnalysisTestTools.printPropertiesInRegion(region);
        //AnalysisTestTools.printPropertiesInAggregateSet(aggregateSet);

        DebugTools.printAvailableHeap();
        //TestTools.printAll(PropertyRegionMap.class);
        //TestTools.printAll(PropertyAggregateSetMap.class);

        RegionOLD australia;
        Set<RegionOLD> regions = null;
        List<AggregateSet> aggregateSets = null;
        // add some analysis tasks
        try {
            PersistenceSession session = gateway.openSession();
            australia = new AnalysisTestTools(getPersistenceService()).findRegionByName("Western Australia");
            regions = australia.getChildRegions();
            aggregateSets = new AnalysisTestTools(getPersistenceService()).findAllAggregateSets();
            session.close();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }

        int limit = 200;
        int count = 0;
        TaskGroup regionGroup;
        TaskGroup lastGroup = null;
        //for (Region region : regions) {
            regionGroup = new TaskGroup(region.getName());
            all.addTask(regionGroup);
            for (AggregateSet aggregateSet : aggregateSets) {
                regionGroup.addTask(new PriceAnalysisTask("Test Task: "+region+"-"+aggregateSet, dataSource, region, aggregateSet, timespan, timePeriod, getPersistenceService()).asTask());
                count++;
                if (count >= limit) {
                    break;
                }
            }
            // make the group dependent on completion of the previous one
            if (lastGroup != null) {
                regionGroup.addDependency(lastGroup);
            }
            lastGroup = regionGroup;
            DebugTools.printAvailableHeap();
//            if (count > limit) {
//                break;
//            }
        //}

        //taskPlan.print();
        /*LOG.info("--- Saving the task plan ---");
        // persist the task plan
        try {
            gateway.save(taskPlan);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }*/

        //TestTools.printAll(RealProperty.class);

        //all.addTask(new PriceAnalysisTask("Test Task 1", dataSource, region, aggregateSet, timespan, timePeriod).asTask());
        ExecutorProvider executorProvider = new SimpleExecutorProvider();
        taskPlan.start(executorProvider);

        TestTools.printAll(PriceAnalysis.class, getPersistenceService());
    }

    private List<DataSource> initialiseDataSources() {
        PersistenceService gateway = getPersistenceService();
        DataSource privateTreaty = null;
        DataSource lease = null;
        try {
            privateTreaty = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
            lease = new DataSource("Advertisements: Lease", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.Lease));
            gateway.save(privateTreaty);
            gateway.save(lease);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        List<DataSource> dataSources = new LinkedList<DataSource>();
        dataSources.add(privateTreaty);
        dataSources.add(lease);
        return dataSources;
    }

    /** Load all regions decending from the specified parent */
    private Set<RegionOLD> loadRegions(String parentName) {
        PersistenceService gateway = getPersistenceService();
        RegionOLD parent;
        Set<RegionOLD> regions = null;
        List<AggregateSet> aggregateSets = null;
        // add some analysis tasks
        try {
            PersistenceSession session = gateway.openSession();
            parent = new AnalysisTestTools(getPersistenceService()).findRegionByName(parentName);
            regions = parent.getChildRegions();
            session.close();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        return regions;
    }


    public void testBetterQueriesForPriceAnalysis() {
        new AnalysisTestTools(getPersistenceService()).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(getPersistenceService()).findRegionByName("Neutral Bay");
          // todo: broken - repair for new region implementation
        //new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 50, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }
        System.out.println("--------AggregateSet: -------");
        aggregateSet.print();

        /** Advertisements in the Region and Aggregate Set using a theta-style join */
        /*String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm, PropertyAggregateSetMap pasm " +
                "where pa.premise = prm.premise and " +
                "prm.premise = pasm.premise and " +
                "pa.premise = pasm.premise and " +
                "prm.region = :region and " +
                "pasm.aggregateSet = :aggregateSet ";*/

//        String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm " +
//                "where pa.premise = prm.premise and " +
//                "prm.region = :region";

        //String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm ";

        /** The sub query gets the most recent advertisement for a property within the time span */
        String mostRecentAd = "select pa2.premise.id, max(pa2.dateListed) from PropertyAdvertisement pa2 " +
                           "where type = :type " +
                           "and dateListed between :startDate and :endDate " +
                           "group by pa2.premise";

        List<Object[]> results;
        PersistenceService gateway = getPersistenceService();
        PersistenceSession ps;

        Date endDate = new TimePeriod(Calendar.DECEMBER, 2005).lastSecond();
        Date startDate = new Timespan(4, PeriodTypes.Year).firstSecond(endDate);


        try {
            ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(mostRecentAd);

            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            query.setParameter("type", PropertyAdvertisementTypes.PrivateTreaty);
            // perform the query
            results = query.list();
            DebugTools.printCollection(results);

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }

        String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm, PropertyAggregateSetMap pasm where " +
                "pa.premise = prm.premise and prm.region = :region and " +
                "pa.premise = pasm.premise and pasm.aggregateSet = :aggregateSet and " +
                "pa.type = :type";

        List<PropertyAdvertisement> advertisements = null;

        try {
            ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(queryString);

            query.setEntity("region", region);
            query.setEntity("aggregateSet", aggregateSet);
            query.setParameter("type", PropertyAdvertisementTypes.PrivateTreaty);

            // perform the query
            advertisements = query.list();

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }

        if (advertisements != null) {
            LOG.info("Found "+advertisements.size()+" advertisements");

            for (PropertyAdvertisement advertisement : advertisements) {
                advertisement.print();
            }
        } else {
            LOG.info("Found 0 advertisements");
        }

        String queryString2 = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm, PropertyAggregateSetMap pasm where " +
                "pa.premise = prm.premise and prm.region = :region and " +
                "pa.premise = pasm.premise and pasm.aggregateSet = :aggregateSet and " +
                "pa.dateListed in (select max(pa2.dateListed) from PropertyAdvertisement pa2 where " +
                                    "pa2.premise = pa.premise and " +
                                    "type = :type and " +
                                    "pa2.dateListed between :startDate and :endDate group by pa2.premise)";

        try {
            ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();
            Query query = session.createQuery(queryString2);

            query.setEntity("region", region);
            query.setEntity("aggregateSet", aggregateSet);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            query.setParameter("type", PropertyAdvertisementTypes.PrivateTreaty);

            // perform the query
            advertisements = query.list();

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }


        if (advertisements != null) {
            LOG.info("Found "+advertisements.size()+" advertisements");

            for (PropertyAdvertisement advertisement : advertisements) {
                advertisement.print();
            }
        } else {
            LOG.info("Found 0 advertisements");
            fail();
        }
    }

    AggregateSet lookupAggregateSet(Long id) {
        AggregateSet aggregateSet = null;
        try {
            aggregateSet = getPersistenceService().findById(AggregateSet.class, id);
        } catch(PersistenceServiceException e) {
            fail();
        }
        System.out.println("--------AggregateSet: -------");
        aggregateSet.print();

        return aggregateSet;
    }

    public void testNamedQuery() {
        new AnalysisTestTools(getPersistenceService()).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(getPersistenceService()).findRegionByName("Neutral Bay");
          // todo: broken - repair for new region implementation
        //new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 50, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();

        PersistenceService gateway = getPersistenceService();
        PersistenceSession ps;

        AggregateSet aggregateSet = lookupAggregateSet(2L); // houses

        Date endDate = new TimePeriod(Calendar.DECEMBER, 2005).lastSecond();
        Date startDate = new Timespan(4, PeriodTypes.Year).firstSecond(endDate);
        List<PropertyAdvertisement> advertisements = null;
        try {
            ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();

            //noinspection unchecked
            advertisements = session.getNamedQuery("propertyAdvertisement.mostRecent").
                    setEntity("region", region).
                    setEntity("aggregateSet", aggregateSet).
                    setDate("startDate", startDate).
                    setDate("endDate", endDate).
                    setParameter("type", PropertyAdvertisementTypes.PrivateTreaty).list();

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }


        if (advertisements != null) {
            LOG.info("Found "+advertisements.size()+" advertisements");

            for (PropertyAdvertisement advertisement : advertisements) {
                advertisement.print();
            }
        } else {
            LOG.info("Found 0 advertisements");
            fail();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create analysis tasks for all the aggregate groups in the specified region and data source */
      private void createPriceAnalysisTasks(TaskGroup parent, RegionOLD region, List<AggregateSet> aggregateSets, DataSource dataSource, Timespan timespan, TimePeriod timePeriod) {
          TaskGroup regionGroup;
          regionGroup = new TaskGroup(region.getName());
          parent.addTask(regionGroup);
          for (AggregateSet aggregateSet : aggregateSets) {
              regionGroup.addTask(new PriceAnalysisTask("Test Task: "+region+"-"+aggregateSet, dataSource, region, aggregateSet, timespan, timePeriod, getPersistenceService()).asTask());
          }
      }

    // ------------------------------------------------------------------------------------------------------

    public void testYieldAnalysis() {

        DebugTools.printAvailableHeap();

        new AnalysisTestTools(getPersistenceService()).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(getPersistenceService()).findRegionByName("Neutral Bay");
        // todo: broken - repair for new region implementation
        //new AnalysisTestTools(getPersistenceService()).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 500, 2004, 2005);
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToRegions();
        new AnalysisTestTools(getPersistenceService()).mapPropertiesToAggregateSets();
        new AnalysisTestTools(getPersistenceService()).generateRandomAdsForProperties(PropertyAdvertisementTypes.Lease, 2004, 2005);
        List<DataSource> dataSources = initialiseDataSources();

        DebugTools.printAvailableHeap();

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);

        TaskPlan taskPlan = new TaskPlan("Analysis Tasks");
        TaskGroup all = new TaskGroup("All");
        taskPlan.setRootTask(all);

        List<AggregateSet> aggregateSets = new AnalysisTestTools(getPersistenceService()).findAllAggregateSets();

        createPriceAnalysisTasks(all, region, aggregateSets, dataSources.get(0), timespan, timePeriod);  // sales
        createPriceAnalysisTasks(all, region, aggregateSets, dataSources.get(1), timespan, timePeriod);  // rentals

        ExecutorProvider executorProvider = new SimpleExecutorProvider();
        taskPlan.start(executorProvider);

        // now execute the yield analysis
        DataSource salesDataSource = dataSources.get(0);
        DataSource rentalsDataSource = dataSources.get(1);
        //DataSource dataSource = new DataSource("Yield", YieldAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));

        LOG.info("------ starting spooler for Yield Analysis ---------");
// todo: enable        
//        YieldAnalysisSpooler spooler = new YieldAnalysisSpooler(getPersistenceService(), salesDataSource, rentalsDataSource, timespan);
//        spooler.start();


    }

    // ------------------------------------------------------------------------------------------------------

    private BivariateSeries generateRandomPairs(int size) {
        BivariateSeries series = new BivariateSeries(null);
        MathContext mc = new MathContext(18, RoundingMode.HALF_UP);
        BigDecimal d1;
        BigDecimal d2;
        RandomData r = new RandomDataImpl();
        for (int i = 0; i < size; i++) {
            d1 = new BigDecimal(r.nextGaussian(1000.0, 50.0));
            d2 = new BigDecimal(r.nextGaussian(1000.0, 50.0));
            series.add(new Pair(d1, d2));
        }
        return series;
    }

    /** Feed random series into the compute engine */
    private List<Future<ComputedResult>> feedYieldEngine(YieldEngine engine, int maxCount) {
        BivariateSeries series;
        AggregateSeries aggregateSeries = new AggregateSeries(null);
        List<Future<ComputedResult>> futureResults = new LinkedList<Future<ComputedResult>>();

        LOG.info("Submitting up to "+maxCount+" series to the engine...");
        int submitted = 0;
        while ((submitted < maxCount) && (!almostOutOfHeap())) {
            aggregateSeries.add(generateRandomPairs(3));
            submitted++;           
        }

        futureResults.add(engine.compute(aggregateSeries));
        DebugTools.printAvailableHeap();

        return futureResults;
    }

    public void testYieldEngine() {
        int count = 200;
        int loops = 3;
        int currentLoop = 0;
        YieldEngine engine = new YieldEngine();

        for (currentLoop = 0; currentLoop < loops; currentLoop++) {
            List<Future<ComputedResult>> futureResults = feedYieldEngine(engine, count);
            int submitted = futureResults.size();

            LOG.info("Waiting for "+submitted+" results...");

            int completed;
            boolean waiting = true;
            while (waiting) {
                completed = 0;
                for (Future<ComputedResult> future : futureResults) {
                    if (future.isDone()) {
                        completed++;
                    }
                }
                LOG.info(completed +" completed");
                if (completed == submitted) {
                    waiting = false;
                } else {
                    // sleep a bit
                    try {
                        Thread.sleep(200);
                        DebugTools.printAvailableHeap();
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        }
    }


}
