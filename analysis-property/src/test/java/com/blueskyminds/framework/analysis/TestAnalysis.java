package com.blueskyminds.framework.analysis;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.property.classification.PremiseAggregateSetMap;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.regionx.country.CountryHandle;
import com.blueskyminds.framework.persistence.paging.Page;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseRegionMap;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.advertisement.dao.AdvertisementDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.LinkedList;
import java.util.List;

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
public class TestAnalysis extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestAnalysis.class);

    public TestAnalysis(String string) {
        super(string);
    }

    public void testAggregateSets() {
        new AnalysisTestTools(em).initialiseAnalysisGroups();

        TestTools.printAll(em, AggregateSet.class);
    }

    /**
     * Simple test to check that advertisements can be paged
     */
    public void testAdvertisementPagination() {
        int pageNo = 0;
        Page page;
        boolean lastPage = false;
        new AnalysisTestTools(em).initialiseRandomAdvertisements(1000);
        AdvertisementDAO advertisementDAO = new AdvertisementDAO(em);

        while (!lastPage) {
            page = advertisementDAO.findPage(pageNo, 20);
            System.out.println(pageNo);
            lastPage = !page.hasNextPage();
            pageNo++;
        }
    }

    /** Test the tool that generates a random property - ensure they can be persisted */
    public void testCreateRandomProperty() {
        int count = 20;
        List<Premise> properties = new LinkedList<Premise>();
        for (int i = 0; i < count; i++) {
            properties.add(PremiseTestTools.createRandomPremise(null, em));
        }

        for (Premise property : properties) {
            em.persist(property);
            property.print(System.out);
        }
    }

    /** Test the tool that generates a random property and adds an advertisement for each one*/
    public void testCreateRandomPropertyAdvertisement() {
        new AnalysisTestTools(em).initialiseRandomPropertiesWithAds(20, 1990, 2005);
        TestTools.printAll(em, PropertyAdvertisement.class);
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



    /** Test the algorithm that maps a property to one or more regions */
    public void testMapPropertyToRegion() throws Exception {
        Premise property = PremiseTestTools.createRandomPremise(null, em);
        CountryHandle australia = new AddressDAO(em).findCountry("AUS");
// todo: enable
//        PropertyToRegionSpooler propertyToRegionSpooler = new PropertyToRegionSpooler(em, australia);
//        Set<Region> regions = propertyToRegionSpooler.mapPropertyToRegions(property);

//        assertNotNull(regions);
        // assert that the property is in 4 regions: suburb, postcode, state and country
//        assertEquals(4, regions.size());
    }

    public void testRegionComputer() {
        new AnalysisTestTools(em).initialiseRandomProperties(20);

        new AnalysisTestTools(em).mapPropertiesToRegions();

        TestTools.printAll(PremiseRegionMap.class, em);
    }

    public void testAggregateSetComputer() {

        new AnalysisTestTools(em).initialiseRandomProperties(20);
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        TestTools.printAll(PremiseAggregateSetMap.class, em);
    }

  /*  private void doQuery(String query) {
        PersistenceService gateway = em;
        try {
            PersistenceSession ps = gateway.openSession();
            Session session = (Session) ps.getSessionImpl();

            List<Premise> properties = session.createQuery(query).list();

            if (properties != null) {
                LOG.info("Found "+properties.size()+" properties");

                for (Premise property : properties) {
                    property.print(System.out);
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

    *//**
     * @param queryStr
     * @param params needs to be of the form: name, value, name, value, name, value
     *//*
    private void doQuery(String queryStr, Object... params) {
        PersistenceService gateway = em;
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
                    property.print(System.out);
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

    *//**
     * @param queryStr
     * @param params needs to be of the form: name, value, name, value, name, value
     *//*
    private List<Object> doGenericQuery(String queryStr, Object... params) {
        PersistenceService gateway = em;
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
        new AnalysisTestTools(em).initialiseRandomPropertiesWithAds(20, 1990, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

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
            nsw = em.findById(RegionOLD.class, 99L);
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
            aggregateSet = em.findById(AggregateSet.class, 1L);
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
        *//*String query8 = "select propertyAdvertisement.premise from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                        "inner join propertyAdvertisement.premise " +
                        "inner join propertyRegionMap.premise " +
                        "inner join propertyAggregateSetMap.premise " +
                        "where propertyRegionMap.region = :region "+
                        "and propertyAggregateSetMap.aggregateSet = :aggregateSet";
        doQuery(query8, "region", nsw, "aggregateSet", aggregateSet);*//*

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

    }*/

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
   /* public List<Premise> lookupProperties(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod) {
        String queryString = "select propertyAdvertisement.premise " +
                "from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                "where propertyAdvertisement.premise = propertyRegionMap.premise " +
                "and propertyAdvertisement.premise = propertyAggregateSetMap.premise " +
                "and propertyRegionMap.region = :region "+
                "and propertyAggregateSetMap.aggregateSet = :aggregateSet "+
                "and propertyAdvertisement.dateListed between :startDate and :endDate";

        List<Premise> properties = null;
        PersistenceService gateway = em;

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
    }*/

    // see note above - it's better to lookup the advertisements, not the properties
   /* public void testLookupProperties() {
        new AnalysisTestTools(em).initialiseRandomPropertiesWithAds(50, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        TestTools.printAll(AggregateSet.class, em);

        AggregateSet aggregateSet = null;
        try {
            aggregateSet = em.findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }

        RegionOLD nsw = null;
        try {
            nsw = em.findById(RegionOLD.class, 99L);
        } catch(PersistenceServiceException e) {
            fail();
        }

        // lookup all properties in the region and aggregate set for the timeperiod and timespan
        List<Premise> properties = lookupProperties(nsw, aggregateSet, new Timespan(4, PeriodTypes.Year), new TimePeriod(Calendar.DECEMBER, 2005));
        if (properties != null) {
            LOG.info("Found "+properties.size()+" properties");

            for (Premise property : properties) {
                property.print(System.out);
            }
        } else {
            LOG.info("Found 0 properties");
            fail();
        }
    }
*/
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
   /* public List<PropertyAdvertisement> lookupAdvertisments(RegionOLD region, AggregateSet aggregateSet, Timespan timespan, TimePeriod timePeriod) {
        String queryString = "" +
                "from PropertyAdvertisement as propertyAdvertisement, PropertyRegionMap as propertyRegionMap, PropertyAggregateSetMap as propertyAggregateSetMap " +
                "where propertyAdvertisement.premise = propertyRegionMap.premise " +
                "and propertyAdvertisement.premise = propertyAggregateSetMap.premise " +
                "and propertyRegionMap.region = :region "+
                "and propertyAggregateSetMap.aggregateSet = :aggregateSet "+
                "and propertyAdvertisement.id in (select distinct advertisement.id from PropertyAdvertisement as advertisement where advertisement.dateListed between :startDate and :endDate order by advertisement.dateListed desc)";

        List<PropertyAdvertisement> advertisements = null;
        PersistenceService gateway = em;

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
    }*/

   /* public void testLookupAdvertisements() {
        new AnalysisTestTools(em).initialiseRandomPropertiesWithAds(50, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        AggregateSet houses = new AggregateSetDAO(em).findById(2L); // houses

        RegionOLD nsw = null;
        try {
            nsw = em.findById(RegionOLD.class, 99L);
        } catch(PersistenceServiceException e) {
            fail();
        }

        // lookup all advertisements for properties in the region and aggregate set for the timeperiod and timespan
        List<PropertyAdvertisement> advertisements = lookupAdvertisments(nsw, houses, new Timespan(4, PeriodTypes.Year), new TimePeriod(Calendar.DECEMBER, 2005));
        if (advertisements != null) {
            LOG.info("Found "+advertisements.size()+" advertisements");

            for (PropertyAdvertisement advertisement : advertisements) {
                advertisement.print(System.out);
            }
        } else {
            LOG.info("Found 0 advertisements");
            fail();
        }

    }
*/
    /*public Session openSession() throws PersistenceServiceException {
        PersistenceService gateway = em;
        PersistenceSession ps = gateway.openSession();
        return (Session) ps.getSessionImpl();
    }*/

    /*public void testNativeSql() {
        new AnalysisTestTools(em).initialiseRandomPropertiesWithAds(50, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        try {
            Timespan timespan = new Timespan(4, PeriodTypes.Year);
            TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);

            Date endDate = timePeriod.lastSecond();
            Date startDate = timespan.firstSecond(endDate);

            LOG.info("startDate = "+startDate);
            LOG.info("endDate = "+endDate);

            AggregateSet aggregateSet = null;
            try {
                aggregateSet = em.findById(AggregateSet.class, 2L); // houses
            } catch(PersistenceServiceException e) {
                fail();
            }

            RegionOLD region = null;
            try {
                region = em.findById(RegionOLD.class, 99L);     // nsw
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
                ((PropertyAdvertisement) advertisement).print(System.out);
            }

            session.getTransaction().commit();

        } catch(PersistenceServiceException e) {
            fail();
        }
    }*/

    /*public void testAdvertisementSpooler() {
        new AnalysisTestTools(em).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(em).findRegionByName("Neutral Bay");
        // todo: broken - repair for new region implementation
        //new AnalysisTestTools(em).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 50, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);
        AggregateSet aggregateSet = null;
        try {
            aggregateSet = em.findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }

        LOG.info("------ starting statistics engine ---------");
        StatisticsEngine statisticsEngine = new StatisticsEngine();

        DataSource dataSource = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
        LOG.info("------ starting spooler ---------");
        PriceAnalysisSpooler spooler = new PriceAnalysisSpooler(statisticsEngine, dataSource, region, aggregateSet, timespan, timePeriod);
        spooler.start();
    }*/

    // ------------------------------------------------------------------------------------------------------

  /*  public void testDataSourcePersistence() {
        DataSource dataSource = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));

        em.persist(dataSource);

        DataSource result = gateway.findById(DataSource.class, dataSource.getId());

        result.print(System.out);
    }*/

    // ------------------------------------------------------------------------------------------------------

   /* public void testAnalysisTasks() {

        DebugTools.printAvailableHeap();

        new AnalysisTestTools(em).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(em).findRegionByName("Neutral Bay");

        // todo: broken - repair for new region implementation
        //new AnalysisTestTools(em).generateRandomPropertiesWithAds(region, null, 5000, 2004, 2005);
        DebugTools.printAvailableHeap();

        new AnalysisTestTools(em).mapPropertiesToRegions();
        DebugTools.printAvailableHeap();

        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        DebugTools.printAvailableHeap();

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);
        *//*AggregateSet aggregateSet = null;
        try {
            aggregateSet = em.findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }*//*

        //Region region = AnalysisTestTools.findRegionByName("New South Wales");

        DebugTools.printAvailableHeap();

        DataSource dataSource = null;

        dataSource = new DataSource("Advertisements: Private Treaty", PriceAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
        em.persist(dataSource);

        TestTools.printAll(DataSource.class, em);

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

        australia = new AnalysisTestTools(em).findRegionByName("Western Australia");
        regions = australia.getChildRegions();
        aggregateSets = new AnalysisTestTools(em).findAllAggregateSets();


        int limit = 200;
        int count = 0;
        TaskGroup regionGroup;
        TaskGroup lastGroup = null;
        //for (Region region : regions) {
            regionGroup = new TaskGroup(region.getName());
            all.addTask(regionGroup);
            for (AggregateSet aggregateSet : aggregateSets) {
                regionGroup.addTask(new PriceAnalysisTask("Test Task: "+region+"-"+aggregateSet, dataSource, region, aggregateSet, timespan, timePeriod, em).asTask());
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

        //taskPlan.print(System.out);
        *//*LOG.info("--- Saving the task plan ---");
        // persist the task plan
        try {
            gateway.save(taskPlan);
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
            fail();
        }*//*

        //TestTools.printAll(RealProperty.class);

        //all.addTask(new PriceAnalysisTask("Test Task 1", dataSource, region, aggregateSet, timespan, timePeriod).asTask());
        ExecutorProvider executorProvider = new SimpleExecutorProvider();
        taskPlan.start(executorProvider);

        TestTools.printAll(PriceAnalysis.class, em);
    }

    private List<DataSource> initialiseDataSources() {
        PersistenceService gateway = em;
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

    *//** Load all regions decending from the specified parent *//*
    private Set<RegionOLD> loadRegions(String parentName) {
        PersistenceService gateway = em;
        RegionOLD parent;
        Set<RegionOLD> regions = null;
        List<AggregateSet> aggregateSets = null;
        // add some analysis tasks
        try {
            PersistenceSession session = gateway.openSession();
            parent = new AnalysisTestTools(em).findRegionByName(parentName);
            regions = parent.getChildRegions();
            session.close();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }
        return regions;
    }


    public void testBetterQueriesForPriceAnalysis() {
        new AnalysisTestTools(em).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(em).findRegionByName("Neutral Bay");
          // todo: broken - repair for new region implementation
        //new AnalysisTestTools(em).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 50, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        AggregateSet aggregateSet = null;
        try {
            aggregateSet = em.findById(AggregateSet.class, 2L); // houses
        } catch(PersistenceServiceException e) {
            fail();
        }
        System.out.println("--------AggregateSet: -------");
        aggregateSet.print(System.out);

        *//** Advertisements in the Region and Aggregate Set using a theta-style join *//*
        *//*String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm, PropertyAggregateSetMap pasm " +
                "where pa.premise = prm.premise and " +
                "prm.premise = pasm.premise and " +
                "pa.premise = pasm.premise and " +
                "prm.region = :region and " +
                "pasm.aggregateSet = :aggregateSet ";*//*

//        String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm " +
//                "where pa.premise = prm.premise and " +
//                "prm.region = :region";

        //String queryString = "select pa from PropertyAdvertisement pa, PropertyRegionMap prm ";

        *//** The sub query gets the most recent advertisement for a property within the time span *//*
        String mostRecentAd = "select pa2.premise.id, max(pa2.dateListed) from PropertyAdvertisement pa2 " +
                           "where type = :type " +
                           "and dateListed between :startDate and :endDate " +
                           "group by pa2.premise";

        List<Object[]> results;
        PersistenceService gateway = em;
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
                advertisement.print(System.out);
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
                advertisement.print(System.out);
            }
        } else {
            LOG.info("Found 0 advertisements");
            fail();
        }
    }

    AggregateSet lookupAggregateSet(Long id) {
        AggregateSet aggregateSet = null;
        try {
            aggregateSet = em.findById(AggregateSet.class, id);
        } catch(PersistenceServiceException e) {
            fail();
        }
        System.out.println("--------AggregateSet: -------");
        aggregateSet.print(System.out);

        return aggregateSet;
    }

    public void testNamedQuery() {
        new AnalysisTestTools(em).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(em).findRegionByName("Neutral Bay");
          // todo: broken - repair for new region implementation
        //new AnalysisTestTools(em).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 50, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();

        PersistenceService gateway = em;
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
                advertisement.print(System.out);
            }
        } else {
            LOG.info("Found 0 advertisements");
            fail();
        }
    }

    // ------------------------------------------------------------------------------------------------------

    *//** Create analysis tasks for all the aggregate groups in the specified region and data source *//*
      private void createPriceAnalysisTasks(TaskGroup parent, RegionOLD region, List<AggregateSet> aggregateSets, DataSource dataSource, Timespan timespan, TimePeriod timePeriod) {
          TaskGroup regionGroup;
          regionGroup = new TaskGroup(region.getName());
          parent.addTask(regionGroup);
          for (AggregateSet aggregateSet : aggregateSets) {
              regionGroup.addTask(new PriceAnalysisTask("Test Task: "+region+"-"+aggregateSet, dataSource, region, aggregateSet, timespan, timePeriod, em).asTask());
          }
      }

    // ------------------------------------------------------------------------------------------------------

    public void testYieldAnalysis() {

        DebugTools.printAvailableHeap();

        new AnalysisTestTools(em).loadSampleSuburbs();
        Suburb region = (Suburb) new AnalysisTestTools(em).findRegionByName("Neutral Bay");
        // todo: broken - repair for new region implementation
        //new AnalysisTestTools(em).generateRandomPropertiesWithAds(region, PropertyAdvertisementTypes.PrivateTreaty, 500, 2004, 2005);
        new AnalysisTestTools(em).mapPropertiesToRegions();
        new AnalysisTestTools(em).mapPropertiesToAggregateSets();
        new AnalysisTestTools(em).generateRandomAdsForProperties(PropertyAdvertisementTypes.Lease, 2004, 2005);
        List<DataSource> dataSources = initialiseDataSources();

        DebugTools.printAvailableHeap();

        Timespan timespan = new Timespan(4, PeriodTypes.Year);
        TimePeriod timePeriod = new TimePeriod(Calendar.DECEMBER, 2005);

        TaskPlan taskPlan = new TaskPlan("Analysis Tasks");
        TaskGroup all = new TaskGroup("All");
        taskPlan.setRootTask(all);

        List<AggregateSet> aggregateSets = new AnalysisTestTools(em).findAllAggregateSets();

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
//        YieldAnalysisSpooler spooler = new YieldAnalysisSpooler(em, salesDataSource, rentalsDataSource, timespan);
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

    *//** Feed random series into the compute engine *//*
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
    }*/


}
