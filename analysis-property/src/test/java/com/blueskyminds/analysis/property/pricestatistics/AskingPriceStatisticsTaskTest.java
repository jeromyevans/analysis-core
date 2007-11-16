package com.blueskyminds.analysis.property.pricestatistics;

import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.AnalysisServiceImpl;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.PriceAnalysisSampleDescriptor;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.dao.RegionDAO;
import com.blueskyminds.enterprise.region.dao.RegionDAOImpl;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.service.RegionServiceImpl;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.landmine.core.property.AskingPrice;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Query;

/**
 * Date Started: 15/11/2007
 * <p/>
 * History:
 */
public class AskingPriceStatisticsTaskTest extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(AskingPriceStatisticsTaskTest.class);

    private static final String PERSISTENCE_UNIT_NAME = "TestPremiseAnalysisPersistenceUnit";

    private AnalysisService analysisService;
    private AggregateSetDAO aggregateSetDAO;
    private PropertyDAO propertyDAO;
    private RegionDAO regionDAO;
    private StatisticsEngine statisticsEngine;


    public AskingPriceStatisticsTaskTest() {
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

        statisticsEngine = new StatisticsEngine();

        analysisService.recalculatePremiseToRegionMaps();
        analysisService.recalculatePremiseToAggregateSetMaps(PropertyAnalysisTestTools.GROUP_NAME);

        PremiseTestTools.initialiseRandomAdsForPremises(PropertyAdvertisementTypes.PrivateTreaty, 2004, 2005, em);

    }

    public void testTaskCreation() throws Exception {
        PriceAnalysisSampleDescriptor descriptor = new PriceAnalysisSampleDescriptor(null, null, null, null, null);

        Query query = em.createQuery("select price from PropertyAdvertisement propertyAdvertisement");

        AskingPriceStatisticsTask spoolerTask = new AskingPriceStatisticsTask(em, descriptor, statisticsEngine);
        EntitySpooler entitySpooler = new EntitySpooler<AskingPrice>(propertyDAO, query, spoolerTask);
        entitySpooler.addListener(spoolerTask);

        entitySpooler.start();

        TestTools.printAll(PriceAnalysis.class, em);
    }
}
