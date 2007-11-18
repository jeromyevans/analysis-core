package com.blueskyminds.analysis.property.dao;

import com.blueskyminds.analysis.AdvertisementAnalysisServiceImpl;
import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.region.dao.RegionDAO;
import com.blueskyminds.enterprise.region.dao.RegionDAOImpl;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.service.RegionServiceImpl;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.persistence.paging.Pager;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.framework.test.TestTools;
import com.blueskyminds.framework.tools.DebugTools;
import com.blueskyminds.landmine.core.property.AskingPrice;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.Query;
import java.util.Date;
import java.util.Set;

/**
 * Date Started: 15/11/2007
 * <p/>
 * History:
 */
public class AdvertisementAnalysisDAOTest extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(AdvertisementAnalysisDAOTest.class);

    private static final String PERSISTENCE_UNIT_NAME = "TestPremiseAnalysisPersistenceUnit";

    private AnalysisService analysisService;
    private AggregateSetDAO aggregateSetDAO;
    private PropertyDAO propertyDAO;
    private RegionDAO regionDAO;
    private AdvertisementAnalysisDAO analysisDAO;
    private AddressService addressService;
    private AdvertisementAnalysisDAO advertisementAnalysisDAO;
    private StatisticsEngine statisticsEngine;

    public AdvertisementAnalysisDAOTest() {
        super(PERSISTENCE_UNIT_NAME);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RegionService regionService = new RegionServiceImpl(em);
        regionDAO = new RegionDAOImpl(em);
        propertyDAO = new PropertyDAO(em);
        aggregateSetDAO = new AggregateSetDAO(em);
        addressService = new AddressServiceImpl(em);
        advertisementAnalysisDAO = new AdvertisementAnalysisDAO(em);
        statisticsEngine = new StatisticsEngine();

        analysisService = new AdvertisementAnalysisServiceImpl(aggregateSetDAO, regionService, propertyDAO, advertisementAnalysisDAO, statisticsEngine, em);

        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

        PropertyAnalysisTestTools.initialiseAggregateSetGroups(em);

        analysisService.recalculatePremiseToRegionMaps();
        analysisService.recalculatePremiseToAggregateSetMaps(PropertyAnalysisTestTools.GROUP_NAME);

        PremiseTestTools.initialiseRandomAdsForPremises(PropertyAdvertisementTypes.PrivateTreaty, 2004, 2005, em);

        analysisDAO = new AdvertisementAnalysisDAO(em);
    }

    public void testMostRecentAskingPrice() throws Exception {
        RegionHandle carlton = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        LOG.info("region: "+carlton);
        AggregateSet aggregateSet = aggregateSetDAO.findAggregateSet("houses");
        Date startDate = DateTools.createDate(2004, 1, 1, 0, 0,0);
        Date endDate = DateTools.createDate(2006, 1, 1, 0, 0,0);

        Query query = em.createQuery("select pasm from PremiseAggregateSetMap pasm where aggregateSet = :aggregateSet");
        query.setParameter("aggregateSet", aggregateSet);
        DebugTools.printCollection(query.getResultList());

        TestTools.printAll(PropertyAdvertisement.class, em);
        Set<AskingPrice> askingPrices = analysisDAO.lookupMostRecentPrice(PropertyAdvertisementTypes.PrivateTreaty, carlton, aggregateSet, startDate, endDate);
        assertNotNull(askingPrices);
        assertTrue(askingPrices.size() > 0);
        DebugTools.printCollection(askingPrices);
    }

    public void testPageMostRecentAskingPrice() throws Exception {
        RegionHandle carlton = addressService.parseAddress("Carlton VIC", "AUS").getSuburb();
        LOG.info("region: "+carlton);
        AggregateSet aggregateSet = aggregateSetDAO.findAggregateSet("houses");
        Date startDate = DateTools.createDate(2004, 1, 1, 0, 0,0);
        Date endDate = DateTools.createDate(2006, 1, 1, 0, 0,0);

        Query query = em.createQuery("select pasm from PremiseAggregateSetMap pasm where aggregateSet = :aggregateSet");
        query.setParameter("aggregateSet", aggregateSet);
        DebugTools.printCollection(query.getResultList());

        TestTools.printAll(PropertyAdvertisement.class, em);
        Pager pager = analysisDAO.pageMostRecentPrice(PropertyAdvertisementTypes.PrivateTreaty, carlton, aggregateSet, startDate, endDate);
        assertNotNull(pager);
        assertTrue(pager.findPage(0, 10).getPageResults().size() > 0);
    }

}
