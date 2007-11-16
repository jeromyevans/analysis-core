package com.blueskyminds.analysis.property.classification;

import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.service.RegionServiceImpl;
import com.blueskyminds.framework.analysis.PropertyAnalysisTestTools;
import com.blueskyminds.framework.persistence.spooler.EntitySpooler;
import com.blueskyminds.framework.test.OutOfContainerTestCase;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseTestTools;
import com.blueskyminds.landmine.core.property.dao.PropertyDAO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Unit test assignment of Premise's to Region's
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class TestPremiseRegionSpoolerTask extends OutOfContainerTestCase {

    private static final Log LOG = LogFactory.getLog(TestPremiseRegionSpoolerTask.class);
    private static final String TEST_PREMISE_ANALYSIS_PERSISTENCE_UNIT = "TestPremiseAnalysisPersistenceUnit";

    private PropertyDAO propertyDAO;
    private RegionService regionService;
    private AggregateSetGroup aggregateSetGroup;

    public TestPremiseRegionSpoolerTask() {
        super(TEST_PREMISE_ANALYSIS_PERSISTENCE_UNIT);
    }

    /**
     * Creates some reference data
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAddressSubstitutionPatterns(em);
        AddressTestTools.initialiseSampleAusAddresses();
        PremiseTestTools.initialiseSampleAusPremises();

        aggregateSetGroup = new PropertyAnalysisTestTools(em).initialiseAggregateSetGroups();
        regionService = new RegionServiceImpl(em);
        propertyDAO = new PropertyDAO(em);
    }

    public void testSpoolerTask() throws Exception {
        EntitySpooler<Premise> entitySpooler = new EntitySpooler<Premise>(propertyDAO, new PremiseRegionSpoolerTask(em, regionService));

        entitySpooler.start();
    }
}
