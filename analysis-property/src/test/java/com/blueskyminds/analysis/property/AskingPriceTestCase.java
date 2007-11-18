package com.blueskyminds.analysis.property;

import com.blueskyminds.analysis.AdvertisementAnalysisServiceImpl;
import com.blueskyminds.analysis.AnalysisService;
import com.blueskyminds.analysis.core.sets.dao.AggregateSetDAO;
import com.blueskyminds.analysis.core.statistics.StatisticsEngine;
import com.blueskyminds.analysis.property.dao.AdvertisementAnalysisDAO;
import com.blueskyminds.analysis.property.pricestatistics.AskingPriceStatisticsTaskTest;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.address.service.AddressService;
import com.blueskyminds.enterprise.address.service.AddressServiceImpl;
import com.blueskyminds.enterprise.region.dao.RegionDAO;
import com.blueskyminds.enterprise.region.dao.RegionDAOImpl;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.service.RegionServiceImpl;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sets up a common environment for asking price analysis tests
 *
 * Date Started: 17/11/2007
 * <p/>
 * History:
 */
public abstract class AskingPriceTestCase extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(AskingPriceStatisticsTaskTest.class);

    private static final String PERSISTENCE_UNIT_NAME = "TestPremiseAnalysisPersistenceUnit";

    protected AnalysisService analysisService;
    protected AggregateSetDAO aggregateSetDAO;
    protected PropertyDAO propertyDAO;
    protected RegionDAO regionDAO;
    protected StatisticsEngine statisticsEngine;
    protected AdvertisementAnalysisDAO advertisementAnalysisDAO;
    protected AddressService addressService;

    public AskingPriceTestCase() {
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

        PropertyAnalysisTestTools.initialiseAggregateSetGroups(em);

        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

        analysisService.recalculatePremiseToRegionMaps();
        analysisService.recalculatePremiseToAggregateSetMaps(PropertyAnalysisTestTools.GROUP_NAME);
        addressService = new AddressServiceImpl(em);

        PremiseTestTools.initialiseRandomAdsForPremises(PropertyAdvertisementTypes.PrivateTreaty, 2004, 2005, em);

    }

}
