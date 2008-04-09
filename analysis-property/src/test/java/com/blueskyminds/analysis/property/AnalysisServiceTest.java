package com.blueskyminds.analysis.property;

import com.blueskyminds.analysis.AdvertisementAnalysisServiceImpl;
import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.dao.AdvertisementAnalysisDAO;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.region.dao.RegionDAO;
import com.blueskyminds.enterprise.region.dao.RegionDAOImpl;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.service.RegionServiceImpl;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.tasks.TaskGroup;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.tools.DebugTools;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.List;

/**
 * Tests the ServiceLocator of the Analysis Module
 *
 * Date Started: 4/09/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AnalysisServiceTest extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(AnalysisServiceTest.class);

    private static final String PERSISTENCE_UNIT_NAME = "TestPremiseAnalysisPersistenceUnit";

    private AnalysisService analysisService;
    private AggregateSetDAO aggregateSetDAO;
    private PropertyDAO propertyDAO;
    private RegionDAO regionDAO;
    private AddressService addressService;
    private AdvertisementAnalysisDAO advertisementAnalysisDAO;
    private StatisticsEngine statisticsEngine;

    public AnalysisServiceTest() {
        super(PERSISTENCE_UNIT_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RegionService regionService = new RegionServiceImpl(em);
        propertyDAO = new PropertyDAO(em);
        aggregateSetDAO = new AggregateSetDAO(em);
        regionDAO = new RegionDAOImpl(em);
        advertisementAnalysisDAO = new AdvertisementAnalysisDAO(em);
        statisticsEngine = new StatisticsEngine();
        
        analysisService = new AdvertisementAnalysisServiceImpl(aggregateSetDAO, regionService, propertyDAO, advertisementAnalysisDAO, statisticsEngine, em);
        addressService = new AddressServiceImpl(em);
        PropertyAnalysisTestTools.initialiseAggregateSetGroups(em);
        PropertyAnalysisTestTools.initialiseAnalysisDataSources(em);
        
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

        PremiseTestTools.initialiseRandomAdsForPremises(PropertyAdvertisementTypes.PrivateTreaty, 2004, 2005, em);
    }

    public void testFindAggregrateSetGroup() throws Exception {
        AggregateSetGroup group = analysisService.findAggregateSetGroup(PropertyAnalysisTestTools.GROUP_NAME);
        assertNotNull(group);
    }

    public void testRecalculatePremiseToRegionMaps() throws Exception {
        analysisService.recalculatePremiseToRegionMaps();
    }

    public void testRecalculatePremiseToAggregateSetMaps() throws Exception {
        analysisService.recalculatePremiseToAggregateSetMaps(PropertyAnalysisTestTools.GROUP_NAME);
    }

    /** Create analysis tasks for all the aggregate groups in the specified region and data source */
    private void createPriceAnalysisTasks(TaskGroup parent, RegionHandle region, List<AggregateSet> aggregateSets, AnalysisDataSource dataSource, Interval interval, MonthOfYear monthOfYear) {
        TaskGroup regionGroup;
        regionGroup = new TaskGroup(region.getName());
        parent.addTask(regionGroup);
//        for (AggregateSet aggregateSet : aggregateSets) {
//            regionGroup.addTask(new PriceAnalysisTask("Test Task: " + region + "-" + aggregateSet, dataSource, region, aggregateSet, interval, monthOfYear, em).asTask());
//        }
    }

    public void testRegionAnalysis() throws Exception {
        analysisService.recalculatePremiseToRegionMaps();
        analysisService.recalculatePremiseToAggregateSetMaps(PropertyAnalysisTestTools.GROUP_NAME);

        DebugTools.printAvailableHeap();

        Interval interval = new Interval(3, PeriodTypes.Month);
        MonthOfYear startDate = new MonthOfYear(Calendar.JANUARY, 2005);
        MonthOfYear endDate = new MonthOfYear(Calendar.DECEMBER, 2005);

        RegionHandle region = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        AggregateSetGroup aggregateSetGroup = analysisService.findAggregateSetGroup(PropertyAnalysisTestTools.GROUP_NAME);
        analysisService.analyseRegion(region, false, aggregateSetGroup, startDate, endDate, interval);


//
//        TaskPlan taskPlan = new TaskPlan("Analysis Tasks");
//        TaskGroup all = new TaskGroup("All");
//        taskPlan.setRootTask(all);
//
//        createPriceAnalysisTasks(all, region, aggregateSets, dataSources.get(0), interval, monthOfYear);  // sales
//        createPriceAnalysisTasks(all, region, aggregateSets, dataSources.get(1), interval, monthOfYear);  // rentals
//
//        ExecutorProvider executorProvider = new SimpleExecutorProvider();
//        taskPlan.start(executorProvider);
//
//        // now execute the yield analysis
//        AnalysisDataSource salesDataSource = dataSources.get(0);
//        AnalysisDataSource rentalsDataSource = dataSources.get(1);
//        //DataSource dataSource = new DataSource("Yield", YieldAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
//
//        LOG.info("------ starting spooler for Yield Analysis ---------");
//// todo: enable
//        YieldAnalysisSpooler spooler = new YieldAnalysisSpooler(em, salesDataSource, rentalsDataSource, interval);
//        spooler.start();


    }
//    public void testAnalysisService() {
//
//        // this first call will get the statistics engine from the Analysis Service
//        StatisticsEngine statisticsEngine = AdvertisementAnalysisServiceImpl.statisticsEngine();
//        assertNotNull(statisticsEngine);
//        // this call will delegate to the root ServiceLocator instance
//        PersistenceService gateway = AdvertisementAnalysisServiceImpl.persistenceService();
//        assertNotNull(gateway);
//
//    }

}
