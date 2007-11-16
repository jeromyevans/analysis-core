package com.blueskyminds.analysis.property;

import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.AnalysisServiceImpl;
import com.blueskyminds.analysis.core.datasource.DataSource;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.property.advertised.AdvertisedDataSourceMemento;
import com.blueskyminds.analysis.property.pricestatistics.AskingPriceStatisticsTask;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.dao.RegionDAO;
import com.blueskyminds.enterprise.region.dao.RegionDAOImpl;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.service.RegionServiceImpl;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.enterprise.regionx.suburb.SuburbHandle;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.tasks.ExecutorProvider;
import com.blueskyminds.framework.tasks.SimpleExecutorProvider;
import com.blueskyminds.framework.tasks.TaskGroup;
import com.blueskyminds.framework.tasks.TaskPlan;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.tools.DebugTools;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.LinkedList;
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

        analysisService = new AnalysisServiceImpl(aggregateSetDAO, regionService, propertyDAO, em);

        new PropertyAnalysisTestTools(em).initialiseAggregateSetGroups();

        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

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

    private List<DataSource> initialiseDataSources() {
        DataSource privateTreaty = new DataSource("Advertisements: Private Treaty", AskingPriceStatisticsTask.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));
        DataSource lease = new DataSource("Advertisements: Lease", AskingPriceStatisticsTask.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.Lease));
        em.persist(privateTreaty);
        em.persist(lease);

        List<DataSource> dataSources = new LinkedList<DataSource>();
        dataSources.add(privateTreaty);
        dataSources.add(lease);
        return dataSources;
    }

    /** Create analysis tasks for all the aggregate groups in the specified region and data source */
    private void createPriceAnalysisTasks(TaskGroup parent, RegionHandle region, List<AggregateSet> aggregateSets, DataSource dataSource, Interval interval, MonthOfYear monthOfYear) {
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

        PremiseTestTools.initialiseRandomAdsForPremises(PropertyAdvertisementTypes.Lease, 2004, 2005, em);

        DebugTools.printAvailableHeap();

        SuburbHandle region = (SuburbHandle) regionDAO.findRegionByName("Neutral Bay");

        List<DataSource> dataSources = initialiseDataSources();

        DebugTools.printAvailableHeap();

        Interval interval = new Interval(4, PeriodTypes.Year);
        MonthOfYear monthOfYear = new MonthOfYear(Calendar.DECEMBER, 2005);

        TaskPlan taskPlan = new TaskPlan("Analysis Tasks");
        TaskGroup all = new TaskGroup("All");
        taskPlan.setRootTask(all);

        List<AggregateSet> aggregateSets = new PropertyAnalysisTestTools(em).findAllAggregateSets();

        createPriceAnalysisTasks(all, region, aggregateSets, dataSources.get(0), interval, monthOfYear);  // sales
        createPriceAnalysisTasks(all, region, aggregateSets, dataSources.get(1), interval, monthOfYear);  // rentals

        ExecutorProvider executorProvider = new SimpleExecutorProvider();
        taskPlan.start(executorProvider);

        // now execute the yield analysis
        DataSource salesDataSource = dataSources.get(0);
        DataSource rentalsDataSource = dataSources.get(1);
        //DataSource dataSource = new DataSource("Yield", YieldAnalysisSpooler.class, new AdvertisedDataSourceMemento(PropertyAdvertisementTypes.PrivateTreaty));

        LOG.info("------ starting spooler for Yield Analysis ---------");
// todo: enable
//        YieldAnalysisSpooler spooler = new YieldAnalysisSpooler(em, salesDataSource, rentalsDataSource, interval);
//        spooler.start();


    }
//    public void testAnalysisService() {
//
//        // this first call will get the statistics engine from the Analysis Service
//        StatisticsEngine statisticsEngine = AnalysisServiceImpl.statisticsEngine();
//        assertNotNull(statisticsEngine);
//        // this call will delegate to the root ServiceLocator instance
//        PersistenceService gateway = AnalysisServiceImpl.persistenceService();
//        assertNotNull(gateway);
//
//    }

}
